package com.sorintlab.jet.data.acquisition.audio;

import audio_processor.AudioProcessor;
import com.google.protobuf.InvalidProtocolBufferException;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.concurrent.ExecutionException;

public class MQTTPrometheusBridge {
    public static void main(String []args){
        Namespace arguments = parseArguments(args);

        MQTTService mqttService = null;
        try{
            mqttService = MQTTService.build(arguments.getString("aws_client_cert_file"),
                    arguments.getString("aws_client_private_key_file"),
                    arguments.getString("aws_mqtt_endpoint"),
                    arguments.getString("aws_mqtt_client_id"));
        } catch(ExecutionException | InterruptedException x){
            x.printStackTrace();
            System.exit(1);
        }
        final MQTTService finalMQTTService = mqttService;  // so I can access it from the shutdown hook, which is an anonymous inner class

        PrometheusContext prometheus = new PrometheusContext();

        mqttService.subscribe(arguments.getString("aws_mqtt_topic"), message -> {
            AudioProcessor.AudioSummary summary;
            try {
                summary = AudioProcessor.AudioSummary.parseFrom(message.getPayload());
                prometheus.logAudioSummary(summary);
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }
        });

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            finalMQTTService.close();
            prometheus.close();
        }));

    }

    /**
     * This method will attempt to parse the input.  If the arguments are not valid, an error message will be
     * printed and System.exit will be called.
     *
     * @param input the array of input arguments to be parsed
     * @return a Namespace argument containing the parsed argument values
     */
    private static Namespace parseArguments(String []input) {
        ArgumentParser parser = ArgumentParsers.newFor("MQTTPrometheusBridge").build()
                .defaultHelp(true)
                .description("Listens for audio summaries on mqtt and exposes metrics about them via Prometheus");
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
        try {
            return parser.parseArgs(input);
        } catch(ArgumentParserException x){
            parser.handleError(x);
            System.exit(1);
            return null;  // this is only here to avoid compiler errors about not returning a value
        }
    }

}

