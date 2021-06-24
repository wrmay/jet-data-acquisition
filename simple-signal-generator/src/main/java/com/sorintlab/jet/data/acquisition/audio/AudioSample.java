package com.sorintlab.jet.data.acquisition.audio;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;

public class AudioSample implements Serializable {
    private static final long serialVersionUID = -2168796813961698955L;
 
    private int id;
    private long timestamp;
    private short []sample;  

    public int getId(){
        return id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public short []getSample() {
        return sample;
    }

    public AudioSample(int id, long timestamp, short []sample) {
        this.id = id;
        this.timestamp = timestamp;
        this.sample = sample;
    }

    public ByteBuffer getSampleAsLittleEndianByteBuffer(){
        ByteBuffer bytes   = ByteBuffer.allocate(2*sample.length);
        bytes.order(ByteOrder.LITTLE_ENDIAN);
        for(short s: sample) bytes.putShort(s);
        bytes.flip();
        return bytes;
    }

    @Override
    public String toString() {
        return "AudioSample{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", sample=" + Arrays.toString(sample) +
                '}';
    }
}
