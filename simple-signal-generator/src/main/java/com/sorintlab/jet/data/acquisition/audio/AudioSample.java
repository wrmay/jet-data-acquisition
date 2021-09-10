package com.sorintlab.jet.data.acquisition.audio;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class AudioSample implements Serializable {
    private static final long serialVersionUID = -2168796813961698955L;
 
    private int id;
    private long timestamp;
    private byte []sample;

    public int getId(){
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte []getSample() {
        return sample;
    }

    public AudioSample(){

    }

    public AudioSample(int id, long timestamp, byte []sample) {
        this.id = id;
        this.timestamp = timestamp;
        this.sample = sample;
    }

    @Override
    public String toString() {
        return "AudioSample{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", sample=(" + sample.length + " bytes)" +
                '}';
    }

}
