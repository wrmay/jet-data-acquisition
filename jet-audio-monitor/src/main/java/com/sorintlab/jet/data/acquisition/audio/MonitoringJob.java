package com.sorintlab.jet.data.acquisition.audio;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.datamodel.Tuple2;
import com.hazelcast.jet.pipeline.*;
import com.hazelcast.jet.python.PythonServiceConfig;

import java.math.BigInteger;
import java.util.Map;

import static com.hazelcast.jet.python.PythonTransforms.mapUsingPython;

public class MonitoringJob {
    public static void main(String []args){

        JetInstance jet = Jet.newJetInstance();

        Pipeline p = buildPipeLine();

        JobConfig config = new JobConfig().setName("audio-monitor").addClass(MonitoringJob.class);
        Job job = jet.newJob(p, config);
        // job.join();
    }

    static Pipeline buildPipeLine(){
        Pipeline pipeline = Pipeline.create();
        StreamStage<Map.Entry<Integer, AudioSample>> audioSamples = pipeline.readFrom(Sources.<Integer, AudioSample>mapJournal("audio",
                JournalInitialPosition.START_FROM_CURRENT)).withTimestamps(item -> item.getValue().getTimestamp(), 5000);

        StreamStage<Tuple2<Integer, Short>> volumes = audioSamples.map(item -> Tuple2.tuple2(item.getKey(), rmsVolume(item.getValue().getSample())));
        volumes.writeTo(Sinks.logger());

        ServiceFactory<?, ObjectMapper> jsonServiceFactory = ServiceFactories.<ObjectMapper>nonSharedService(ctx -> new ObjectMapper());
        StreamStage<String> audioSamplesAsJson = audioSamples.mapUsingService(jsonServiceFactory, (mapper, item) -> mapper.writeValueAsString(item.getValue()));

        StreamStage<String> dftResults = audioSamplesAsJson.apply(mapUsingPython(new PythonServiceConfig().setHandlerFile("python/dft.py"))).setLocalParallelism(2);

        StreamStage<AudioSpectrum> spectrum = dftResults.mapUsingService(jsonServiceFactory, (json, item) -> json.readValue(item, AudioSpectrum.class));
        spectrum.writeTo(Sinks.logger());

        return pipeline;
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
