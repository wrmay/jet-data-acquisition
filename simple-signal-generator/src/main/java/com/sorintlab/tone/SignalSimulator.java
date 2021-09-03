package com.sorintlab.tone;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
// import java.util.concurrent.ScheduledThreadPoolExecutor;
// import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;
import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.EventLoopGroup;
import software.amazon.awssdk.crt.io.HostResolver;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

public class SignalSimulator {

    // settings
    private SineWave16Generator []generators;
    private String awsIoTCoreMessagingEndpoint;
    private String clientCertFile;
    private String clientPrivateKeyFile;
    private String topic;

    // internal state
    @JsonIgnore
    MqttClientConnection []mqttConnections;


    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("SignalSimulator").build().defaultHelp(true)
                .description("Generate sound samples and send them to the AWS IOT Core data plane over MQTT");
        parser.addArgument("--config-file").required(true).help("the path to a json file containing the configuration for this program.");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e1) {
            parser.handleError(e1);
            System.exit(1);
        }

        String configFile = ns.getString("config_file");

        ObjectMapper mapper = new ObjectMapper();
        SignalSimulator ss = null;
        try {
            ss = mapper.readValue(new File(configFile), SignalSimulator.class);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        // TODO - I want a final variable to call in a shutdown hook
        //   is there a smarter way to do this ?
        final SignalSimulator finalss = ss;

        // creates an MQTT connection for each generator
        ss.initializeConnections();

        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(10);
        Runtime.getRuntime().addShutdownHook(new Thread(){
            public void run(){
                executor.shutdown();
                finalss.closeConnections();
            }
        });
        ss.start(executor);
    }

    public SineWave16Generator[] getGenerators() {
        return generators;
    }

    public void setGenerators(SineWave16Generator[] generators) {
        this.generators = generators;
    }

    public String getAwsIoTCoreMessagingEndpoint() {
        return awsIoTCoreMessagingEndpoint;
    }

    public void setAwsIoTCoreMessagingEndpoint(String awsIoTCoreMessagingEndpoint) {
        this.awsIoTCoreMessagingEndpoint = awsIoTCoreMessagingEndpoint;
    }

    public String getClientCertFile() {
        return clientCertFile;
    }

    public void setClientCertFile(String clientCertFile) {
        this.clientCertFile = clientCertFile;
    }

    public String getClientPrivateKeyFile() {
        return clientPrivateKeyFile;
    }

    public void setClientPrivateKeyFile(String clientPrivateKeyFile) {
        this.clientPrivateKeyFile = clientPrivateKeyFile;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    private void start(ScheduledExecutorService executorService){
        for(SineWave16Generator generator: generators){
            executorService.scheduleAtFixedRate(generator, 0, 1, TimeUnit.SECONDS);
        }
    }

    private void initializeConnections(){
        mqttConnections = new MqttClientConnection[generators.length];
        for(int i=0; i< generators.length; ++i){
            // TODO - I've no idea what an eventloopgroup is and have yet to see any documentation
            // should it be shared ?
            EventLoopGroup elGroup = new EventLoopGroup(1);
            HostResolver resolver = new HostResolver(elGroup);
            ClientBootstrap clientBootstrap = new ClientBootstrap(elGroup, resolver);
            AwsIotMqttConnectionBuilder connectionBuilder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(clientCertFile, clientPrivateKeyFile);
            mqttConnections[i] = connectionBuilder
                .withEndpoint(awsIoTCoreMessagingEndpoint)
                .withPort((short) 443)
                .withBootstrap(clientBootstrap)
                .withClientId("fritos")
                .build();

            generators[i].init(mqttConnections[i]);

            CompletableFuture<Boolean> connected = mqttConnections[i].connect();
            try {
                if (connected.get())
                    System.out.println("Connected to existing session.");
                else
                    System.out.println("Connection to new session.");

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    private void closeConnections(){
        for(MqttClientConnection connection: mqttConnections){
            try {
                connection.disconnect().get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }
}
