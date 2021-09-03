package com.sorintlab.tone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sorintlab.jet.data.acquisition.audio.AudioSample;

import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.concurrent.CompletableFuture;

/**
 * Generates 16bit sine wave samples with configurable frequency, amplitude, phase and sample rate
 */
public class SineWave16Generator implements Runnable {
    private static final int SAMPLE_SECONDS = 5;

    private int id;
    private short []amplitude;
    private int   []frequency;    // cycles per second
    private int sampleRate;     // samples per second
    private double []phase;      //in radians

    @JsonIgnore
    private ByteBuffer  sampleBytes;

    @JsonIgnore
    private ShortBuffer secondBuffer;

    @JsonIgnore
    private int s;

    @JsonIgnore
    MqttClientConnection connection;

    public int getId(){
        return id;
    }

    public short []getAmplitude() {
        return amplitude;
    }

    public int []getFrequency() {
        return frequency;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public double []getPhase() {
        return phase;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setAmplitude(short []amplitude) {
        this.amplitude = amplitude;
    }

    public void setFrequency(int []frequency) {
        this.frequency = frequency;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setPhase(double []phase) {
        this.phase = phase;
    }

    public SineWave16Generator(short []amplitude, int []frequency, int sampleRate, double []phase) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.sampleRate = sampleRate;
        this.phase = phase;
    }

    // probably needed for JSON deserialization
    public SineWave16Generator(){
    }

    /**
     * Writes one second's worth of samples to the provided ByteBuffer
     * 
     * @param  buffer to which samples will be written
     * @param s identifies the particular second to write. s is external to the Generator so the same sample can be
     *          written multiple times (e.g. to different buffers)
     */
    public void writeSamples(ByteBuffer buffer, int s){
        for(int i=0; i < sampleRate; ++i){
            int n = s * sampleRate + i;
            short w = sinwave(n);
            buffer.putShort(w);
        }

    }

    public void writeSamples(ShortBuffer buffer, int s){
        for(int i=0; i < sampleRate; ++i){
            int n = s * sampleRate + i;
            short w = sinwave(n);
            buffer.put(w);
        }

    }

    private short sinwave(int n){
        double result = 0;
        for(int i=0;i < amplitude.length; ++i){
            double angle = phase[i] + (n * Math.PI * 2.0d * (double) frequency[i]) / (double) sampleRate;
            double val = Math.sin(angle);

            result += amplitude[i] * val;
        }
        return (short) result;
    }

    public void init(MqttClientConnection connection){
        this.connection = connection;
        this.secondBuffer = ShortBuffer.allocate(sampleRate);
        this.sampleBytes = ByteBuffer.allocate(sampleRate * SAMPLE_SECONDS * 2);
        this.sampleBytes.order(ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public void run() {
        try {
            secondBuffer.clear();
            writeSamples(secondBuffer, s);
            secondBuffer.flip();
            AudioSample sample = new AudioSample(id, System.currentTimeMillis(), secondBuffer.array());

            CompletableFuture<Integer> published = connection.publish(new MqttMessage("TEST", "AUDIO".getBytes(), QualityOfService.AT_LEAST_ONCE, false));
            published.get();
            System.out.println("It didn' blow up!");

            //map.put(sample.getId(), sample);

            if (s < SAMPLE_SECONDS) {
                writeSamples(sampleBytes, s);
            }

            s += 1;

            if (s == SAMPLE_SECONDS) {
                sampleBytes.flip();
                ByteArrayInputStream bis = new ByteArrayInputStream(sampleBytes.array());
                AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) sampleRate, 16,
                        1, 2, (float) sampleRate, false);
                AudioInputStream ais = new AudioInputStream(bis, audioFormat, 5 * sampleRate);

                File outFile = new File("sample_" + id + ".wav");
                try {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outFile);
                    System.out.println("SAMPLE WRITTEN TO " + outFile.getAbsolutePath());
                } catch (IOException iox) {
                    iox.printStackTrace(System.err);
                }
            }
        } catch(Exception x){
            x.printStackTrace();
        }
    }

}
