package com.sorintlab.jet.data.acquisition.audio;

import java.io.Serializable;

public class AudioSample implements Serializable {
    private static final long serialVersionUID = -2168796813961698955L;
 
    private long timestamp;
    private short []sample;


    public long getTimestamp() {
        return timestamp;
    }

    public short[] getSample() {
        return sample;
    }

    public AudioSample(long timestamp, short[] sample) {
        this.timestamp = timestamp;
        this.sample = sample;
    }

}
