package com.sorintlab.tone;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hazelcast.map.IMap;
import com.sorintlab.jet.data.acquisition.audio.AudioSample;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
    private ByteBuffer secondBuffer;

    @JsonIgnore
    private int s;

    @JsonIgnore
    private String sampleDir;

    public void setSampleDir(String dir){ this.sampleDir = dir;}

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

//    public SineWave16Generator(short []amplitude, int []frequency, int sampleRate, double []phase) {
//        this.amplitude = amplitude;
//        this.frequency = frequency;
//        this.sampleRate = sampleRate;
//        this.phase = phase;
//    }

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
    public void writeAudioSample(ByteBuffer buffer, int s){
        for(int i=0; i < sampleRate; ++i){
            int n = s * sampleRate + i;
            short w = sinwave(n);
            buffer.putShort(w);
        }

    }

    private short sinwave(int n){
        double result = 0;
        // loop over each generator and calculate the output signal by adding the signal from each generator
        for(int i=0;i < amplitude.length; ++i){
            double angle = phase[i] + (n * Math.PI * 2.0d * (double) frequency[i]) / (double) sampleRate;
            double val = Math.sin(angle);

            result += amplitude[i] * val;
        }
        return (short) result;
    }

    @JsonIgnore
    private IMap<Integer, AudioSample> map;

    public void setMap(IMap<Integer, AudioSample> map){
        this.map = map;
        this.secondBuffer = ByteBuffer.allocate(sampleRate * 2);
        this.secondBuffer.order(ByteOrder.LITTLE_ENDIAN);
        this.sampleBytes = ByteBuffer.allocate(sampleRate * SAMPLE_SECONDS * 2);
        this.sampleBytes.order(ByteOrder.LITTLE_ENDIAN);
    }

    @Override
    public void run() {
        try {
            secondBuffer.clear();
            writeAudioSample(secondBuffer, s);
            secondBuffer.flip();
            AudioSample sample = new AudioSample(id, System.currentTimeMillis(), secondBuffer.array());
            map.put(sample.getId(), sample);

            if (sampleDir != null && s < SAMPLE_SECONDS) {
                writeAudioSample(sampleBytes, s);
            }

            s += 1;

            if (sampleDir != null && s == SAMPLE_SECONDS) {
                sampleBytes.flip();
                ByteArrayInputStream bis = new ByteArrayInputStream(sampleBytes.array());
                AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) sampleRate, 16,
                        1, 2, (float) sampleRate, false);
                AudioInputStream ais = new AudioInputStream(bis, audioFormat, 5 * sampleRate);

                File outDir = new File(sampleDir);
                File outFile = new File(outDir, "sample_" + id + ".wav");
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
