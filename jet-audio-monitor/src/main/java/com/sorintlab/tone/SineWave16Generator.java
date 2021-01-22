package com.sorintlab.tone;

import java.nio.ByteBuffer;
/**
 * Generates 16bit sine wave samples with configurable frequency, amplitude, phase and sample rate
 */
public class SineWave16Generator {
    private int id;
    private short amplitude;
    private int   frequency;    // cycles per second
    private int sampleRate;     // samples per second
    private double phase;      //in radians
    
    public int getId(){
        return id;
    }

    public short getAmplitude() {
        return amplitude;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public double getPhase() {
        return phase;
    }
    
    public void setId(int id) {
        this.id = id;
    }

    public void setAmplitude(short amplitude) {
        this.amplitude = amplitude;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setPhase(double phase) {
        this.phase = phase;
    }

    

    public SineWave16Generator(short amplitude, int frequency, int sampleRate, double phase) {
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
     * @param the buffer to which samples will be written
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

    private short sinwave(int n){
        double angle = phase + (n * Math.PI * 2.0d * (double) frequency) / (double) sampleRate;
        double val = Math.sin(angle);

        double result = amplitude * val;
        return (short) result;
    }
}
