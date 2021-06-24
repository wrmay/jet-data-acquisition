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
import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;
import java.util.Map;

public class MonitoringJob {
    public static void main(String []args){

        JetInstance jet = Jet.bootstrappedInstance();

        if (args.length < 2)
            throw new RuntimeException("MonitoringJob requires 2 arguments: audio service grpc host and port");

        String host = args[0];
        Integer port = Integer.parseInt(args[1]);

        Pipeline p = buildPipeLine(host, port);

        JobConfig config = new JobConfig().setName("audio-monitor").addClass(MonitoringJob.class);
        Job job = jet.newJob(p, config);
        // job.join();
    }

    public static class PrometheusContext {
        Gauge audioComponentGauge;
        HTTPServer httpServer;

        public PrometheusContext(){
            audioComponentGauge = Gauge.build().name("audio_components").help("audio signal components").labelNames("ordinal").register();
            try {
                httpServer = new HTTPServer(7070);
            } catch(IOException iox){
                throw new RuntimeException("Could not initialize Prometheus exporter");
            }
        }

        public void close(){
            CollectorRegistry.defaultRegistry.unregister(audioComponentGauge);
            httpServer.stop();
        }

        public void logAudioSummary(AudioProcessor.AudioSummary summary){
            int i=0;
            for(AudioProcessor.SpectrumComponent component: summary.getComponentsList()){
                i++;
                audioComponentGauge.labels(Integer.valueOf(i).toString()).set(component.getFrequency());
            }
        }


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

        Sink<AudioProcessor.AudioSummary> prometheusSink = SinkBuilder.sinkBuilder("prometheus-sink",
                ctx -> new PrometheusContext())
                .<AudioProcessor.AudioSummary>receiveFn((promCtx, item) -> promCtx.logAudioSummary(item))
                .destroyFn(promCtx -> promCtx.close())
                .build();


        summaries.map(MonitoringJob::formatSummary).writeTo(Sinks.logger());
        summaries.writeTo(prometheusSink);

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
