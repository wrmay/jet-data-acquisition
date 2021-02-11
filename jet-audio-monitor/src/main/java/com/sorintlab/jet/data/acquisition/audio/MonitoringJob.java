package com.sorintlab.jet.data.acquisition.audio;

import audio_processor.AudioAnalyzerGrpc;
import audio_processor.AudioProcessor;
import com.google.protobuf.ByteString;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.datamodel.Tuple2;
import com.hazelcast.jet.grpc.GrpcService;
import com.hazelcast.jet.grpc.GrpcServices;
import com.hazelcast.jet.pipeline.*;
import io.grpc.ManagedChannelBuilder;

import java.math.BigInteger;
import java.util.Map;

public class MonitoringJob {
    public static void main(String []args){

        JetInstance jet = Jet.newJetInstance();

        Pipeline p = buildPipeLine(9090);

        JobConfig config = new JobConfig().setName("audio-monitor").addClass(MonitoringJob.class);
        Job job = jet.newJob(p, config);
        // job.join();
    }

    static Pipeline buildPipeLine(int grpcPort){
        Pipeline pipeline = Pipeline.create();
        StreamStage<Map.Entry<Integer, AudioSample>> audioSamples = pipeline.readFrom(Sources.<Integer, AudioSample>mapJournal("audio",
                JournalInitialPosition.START_FROM_CURRENT)).withTimestamps(item -> item.getValue().getTimestamp(), 5000);

        StreamStage<Tuple2<Integer, Short>> volumes = audioSamples.map(item -> Tuple2.tuple2(item.getKey(), rmsVolume(item.getValue().getSample())));
        volumes.writeTo(Sinks.logger());

        ServiceFactory<?, ? extends GrpcService<AudioProcessor.AudioSample, AudioProcessor.Spectrum>> audioService =
                GrpcServices.bidirectionalStreamingService(
                        () -> ManagedChannelBuilder.forAddress("localhost", grpcPort).usePlaintext(),
                        channel -> AudioAnalyzerGrpc.newStub(channel)::computeSpectrum
                );

        StreamStage<AudioProcessor.AudioSample> protobufSamples =
                audioSamples.map(item -> AudioProcessor.AudioSample.newBuilder()
                        .setId(item.getValue().getId())
                        .setTimestamp(item.getValue().getTimestamp())
                        .setSample(ByteString.copyFrom(item.getValue().getSampleAsLittleEndianByteBuffer()))
                        .build());


        StreamStage<AudioProcessor.Spectrum> spectrums =
                protobufSamples.mapUsingServiceAsync(audioService, GrpcService::call);

        spectrums.map(MonitoringJob::formatSpectrum).writeTo(Sinks.logger());

        return pipeline;
    }

    public static String formatAudioSample(AudioProcessor.AudioSample sample){
        StringBuilder result = new StringBuilder("AudioSample for " + sample.getId() + " at " + sample.getTimestamp());
        result.append("\n").append(sample.getSample().toString());
        return result.toString();
    }

    public static String formatSpectrum(AudioProcessor.Spectrum spectrum){
        StringBuilder result = new StringBuilder("Spectrum of " + spectrum.getId() + " at t=" + spectrum.getTimestamp());
        for(AudioProcessor.SpectrumComponent component: spectrum.getComponentsList()){
            result.append("\n\t").append(component.getAmplitude()).append(" @ ").append(component.getFrequency()).append("Hz");
        }
        result.append("\n");
        return result.toString();
    }

    /*
     * Using immutable types here create sample_size temporary objects (plus or minus a few).  Look into something that can
     * operate on long values in place.
     */
    public static short rmsVolume(short []audio){
        BigInteger result = BigInteger.ZERO;
        for (short value : audio) result = result.add(BigInteger.valueOf(value).pow(2));
        result = result.divide(BigInteger.valueOf(audio.length));

        double d = result.doubleValue();
        d = Math.sqrt(d);

        // it should actually fit wth no overflow
        return (short) d;
    }
}
