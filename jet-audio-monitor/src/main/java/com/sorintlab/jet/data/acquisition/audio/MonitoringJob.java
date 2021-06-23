package com.sorintlab.jet.data.acquisition.audio;

import audio_processor.AudioAnalyzerGrpc;
import audio_processor.AudioProcessor;
import com.google.protobuf.ByteString;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.Job;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.grpc.GrpcService;
import com.hazelcast.jet.grpc.GrpcServices;
import com.hazelcast.jet.pipeline.*;
import io.grpc.ManagedChannelBuilder;

import java.util.Map;

public class MonitoringJob {
    public static void main(String []args){

        JetInstance jet = Jet.bootstrappedInstance();

        Pipeline p = buildPipeLine("audioservice", 9091);

        JobConfig config = new JobConfig().setName("audio-monitor").addClass(MonitoringJob.class);
        Job job = jet.newJob(p, config);
        // job.join();
    }

    static Pipeline buildPipeLine(String grpcHost, int grpcPort){
        Pipeline pipeline = Pipeline.create();
        StreamStage<Map.Entry<Integer, AudioSample>> audioSamples = pipeline.readFrom(Sources.<Integer, AudioSample>mapJournal("audio",
                JournalInitialPosition.START_FROM_CURRENT)).withTimestamps(item -> item.getValue().getTimestamp(), 5000);

        ServiceFactory<?, ? extends GrpcService<AudioProcessor.AudioSample, AudioProcessor.AudioSummary>> audioService =
                GrpcServices.bidirectionalStreamingService(
                        () -> ManagedChannelBuilder.forAddress(grpcHost, grpcPort).usePlaintext(),
                        channel -> AudioAnalyzerGrpc.newStub(channel)::computeSummary
                );

        // TODO - is there some way to eliminate the copy in getSampleAsLittleEndianByteBuffer and the one in ByteString.copyFrom
        StreamStage<AudioProcessor.AudioSample> protobufSamples =
                audioSamples.map(item -> AudioProcessor.AudioSample.newBuilder()
                        .setId(item.getValue().getId())
                        .setTimestamp(item.getValue().getTimestamp())
                        .setSample(ByteString.copyFrom(item.getValue().getSampleAsLittleEndianByteBuffer()))
                        .build());

        StreamStage<AudioProcessor.AudioSummary> summaries =
                protobufSamples.mapUsingServiceAsync(audioService, GrpcService::call);

        summaries.map(MonitoringJob::formatSummary).writeTo(Sinks.logger());

        return pipeline;
    }

    public static String formatAudioSample(AudioProcessor.AudioSample sample){
        StringBuilder result = new StringBuilder("AudioSample for " + sample.getId() + " at " + sample.getTimestamp());
        result.append("\n").append(sample.getSample().toString());
        return result.toString();
    }

    public static String formatSummary(AudioProcessor.AudioSummary summary){
        StringBuilder result = new StringBuilder("Summary of " + summary.getId() + " at t=" + summary.getTimestamp() + " rms volume: " + summary.getRmsVolume());
        result.append("\n\tSpectrum Components");
        for(AudioProcessor.SpectrumComponent component: summary.getComponentsList()){
            result.append("\n\t").append(component.getAmplitude()).append(" @ ").append(component.getFrequency()).append("Hz");
        }
        result.append("\n");
        return result.toString();
    }

}
