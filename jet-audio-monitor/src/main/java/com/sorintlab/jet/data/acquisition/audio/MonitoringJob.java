package com.sorintlab.jet.data.acquisition.audio;

import com.hazelcast.core.EntryEventType;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.pipeline.*;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;
import java.util.Map;

public class MonitoringJob {
    public static void main(String []args){
        JetInstance jet = Jet.newJetInstance();

        Pipeline p = buildPipeLine();
        Job job = jet.newJob(p);

        job.join();
    }

    static Pipeline buildPipeLine(){
        Pipeline pipeline = Pipeline.create();
        StreamStage<Map.Entry<Long, byte[]>> rawBytes = pipeline.readFrom(Sources.<Long, byte[]>mapJournal("audio",
                JournalInitialPosition.START_FROM_CURRENT)).withTimestamps(item -> item.getKey(), 5000);

        StreamStage<short[]> audioStream = rawBytes.map(item -> MonitoringJob.decode16BitRawAudio(item.getValue()));
        audioStream.map( item -> rmsVolume(item)).writeTo(Sinks.logger());

        return pipeline;
    }

    /*
     * Although it would seem to be more efficient to  pass audio data around
     * as a ShortBuffer, which would be just a view of the underlying byte [], in
     * anticipation of having to pass this to python, I'm going to go ahead and
     * copy into a new short []
     *
     * Since they byte [] should be storing short values (i.e. 2 byte  signed integers), this method assumes,
     * without checking,  that the length of the byte [] is a multiple of 2. If it's not, the last byte will
     * be ignored.  If the length of the input is <= 1 bad things may happen.
     */
    public static short []decode16BitRawAudio(byte []bytes){

        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        ShortBuffer sb = buffer.asShortBuffer();

        short[] result = new short[bytes.length / 2];
        for(int i=0;i < result.length; ++i) result[i] = sb.get(i);

        return result;
    }

    /*
     * Using immutable types here create 44100 temporary objects (plus or minus a few).  Look into something that can
     * operate on long values in place.
     */
    public static short rmsVolume(short []audio){
        BigInteger result = BigInteger.ZERO;
        for(int i=0;i<audio.length; ++i) result = result.add(BigInteger.valueOf(audio[i]).pow(2));
        result = result.divide(BigInteger.valueOf(audio.length));

        double d = result.doubleValue();
        d = Math.sqrt(d);

        // it should actually fit wth no overflow
        return (short) d;
    }
}
