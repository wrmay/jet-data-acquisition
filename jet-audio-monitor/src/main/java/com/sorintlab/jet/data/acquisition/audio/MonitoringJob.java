package com.sorintlab.jet.data.acquisition.audio;

import audio_processor.AudioAnalyzerGrpc;
import audio_processor.AudioProcessor;
import com.google.protobuf.ByteString;
import com.hazelcast.jet.Jet;
import com.hazelcast.jet.JetInstance;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.grpc.GrpcService;
import com.hazelcast.jet.grpc.GrpcServices;
import com.hazelcast.jet.pipeline.*;
import io.grpc.ManagedChannelBuilder;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.Map;

public class MonitoringJob {
    public static void main(String[] args) {
        Namespace arg = parseArguments(args);

        //JetInstance jet = Jet.bootstrappedInstance();
        JetInstance jet = Jet.newJetInstance();  // use default configuration routine

        Pipeline p = buildPipeLine(
                arg.getString("audio_service_host"),
                arg.getInt("audio_service_port"),
                arg.getString("aws_client_cert_file"),
                arg.getString("aws_client_private_key_file"),
                arg.get("aws_mqtt_endpoint"),
                arg.get("aws_mqtt_client_id"),
                arg.getString("output_topic"));

        JobConfig config = new JobConfig().setName("audio-monitor");
//        JobConfig config = new JobConfig().setName("audio-monitor").addClass(MonitoringJob.class);
        jet.newJob(p, config);
    }

    /**
     * This method will attempt to parse the input.  If the arguments are not valid, an error message will be
     * printed and System.exit will be called.
     *
     * @param input the array of input arguments to be parsed
     * @return a Namespace argument containing the parsed argument values
     */
    private static Namespace parseArguments(String []input) {
        ArgumentParser parser = ArgumentParsers.newFor("MonitoringJob").build()
                .defaultHelp(true)
                .description("Jet job to process audio");
        parser.addArgument("--audio-service-host")
                .required(true)
                .help("The hostname of the gRPC audio service that provides audio summaries");
        parser.addArgument("--audio-service-port")
                .required(true)
                .type(Integer.class)
                .help("The port number of the gRPC audio service that provided audio summaries");
        parser.addArgument("--aws-mqtt-endpoint")
                .required(true)
                .help("The AWS endpoint for the MQTT service from which events will be received");
        parser.addArgument("--aws-mqtt-client-id")
                .required(true)
                .help("The name used to identify this MQTT client");
        parser.addArgument("--aws-mqtt-topic")
                .required(true)
                .help("The MQTT topic on which to listen for audio samples");
        parser.addArgument("--aws-client-cert-file")
                .required(true)
                .help("The path to the client certificate file that will be used to authenticate to AWS MQTT");
        parser.addArgument("--aws-client-private-key-file")
                .required(true)
                .help("The path to the private key file that will be used to authenticate to AWS MQTT");
        parser.addArgument("--output-topic")
                .required(true)
                .help("The MQTT topic to which audio summaries will be published");
        try {
            return parser.parseArgs(input);
        } catch(ArgumentParserException x){
            parser.handleError(x);
            System.exit(1);
            return null;  // this is only here to avoid compiler errors about not returning a value
        }
    }


    static Pipeline buildPipeLine(String grpcHost, int grpcPort,  String clientCertFile, String clientPrivateKeyFile, String endpoint, String clientId, String outputTopic) {
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
                        .setSample(ByteString.copyFrom(item.getValue().getSample()))
                        .build());

        StreamStage<AudioProcessor.AudioSummary> summaries =
                protobufSamples.mapUsingServiceAsync(audioService, GrpcService::call);

        StreamStage<String> textSummaries = summaries.map(MonitoringJob::formatSummary);

        Sink<AudioProcessor.AudioSummary> mqttSink = SinkBuilder.sinkBuilder("mqtt-sink", ctx -> MQTTService.build(clientCertFile,clientPrivateKeyFile,endpoint,clientId))
                .<AudioProcessor.AudioSummary>receiveFn( (ctx, item) -> ctx.publish(outputTopic, item.toByteArray())).build();

        summaries.writeTo(mqttSink);

        textSummaries.writeTo(Sinks.logger());

        return pipeline;
    }

    public static String formatSummary(AudioProcessor.AudioSummary summary) {
        StringBuilder result = new StringBuilder("Summary of " + summary.getId() + " at t=" + summary.getTimestamp() + " rms volume: " + summary.getRmsVolume());
        result.append("\n\tSpectrum Components");
        for (AudioProcessor.SpectrumComponent component : summary.getComponentsList()) {
            result.append("\n\t").append(component.getAmplitude()).append(" @ ").append(component.getFrequency()).append("Hz");
        }
        result.append("\n");
        return result.toString();
    }

}
