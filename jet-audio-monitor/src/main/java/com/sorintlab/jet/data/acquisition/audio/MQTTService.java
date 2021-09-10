package com.sorintlab.jet.data.acquisition.audio;

import software.amazon.awssdk.crt.io.ClientBootstrap;
import software.amazon.awssdk.crt.io.EventLoopGroup;
import software.amazon.awssdk.crt.io.HostResolver;
import software.amazon.awssdk.crt.mqtt.MqttClientConnection;
import software.amazon.awssdk.crt.mqtt.MqttMessage;
import software.amazon.awssdk.crt.mqtt.QualityOfService;
import software.amazon.awssdk.iot.AwsIotMqttConnectionBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

public class MQTTService  {

    private  final MqttClientConnection mqttConnection;

    private MQTTService(MqttClientConnection mqttConnection){
        this.mqttConnection = mqttConnection;
    }

    public void subscribe(String topic, Consumer<MqttMessage> consumer) {
        CompletableFuture<Integer> subscribed = mqttConnection.subscribe(topic, QualityOfService.AT_LEAST_ONCE, consumer);
        try {
            subscribed.get();
            System.out.println("Subscribed to " + topic);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void publish(String topic, byte []message) throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> published = mqttConnection.publish(new MqttMessage(topic, message, QualityOfService.AT_LEAST_ONCE, false, false));
        published.get();
    }

    public  static MQTTService build(String clientCertFile, String clientPrivateKeyFile, String endpoint, String clientId) throws ExecutionException, InterruptedException {
        // TODO - I've no idea what an eventloopgroup is and have yet to see any documentation
        // should it be shared ?
        EventLoopGroup elGroup = new EventLoopGroup(1);
        HostResolver resolver = new HostResolver(elGroup);
        ClientBootstrap clientBootstrap = new ClientBootstrap(elGroup, resolver);
        AwsIotMqttConnectionBuilder connectionBuilder = AwsIotMqttConnectionBuilder.newMtlsBuilderFromPath(clientCertFile, clientPrivateKeyFile);
        MqttClientConnection mqttConnection = connectionBuilder
                .withEndpoint(endpoint)
                .withPort((short) 443)
                .withBootstrap(clientBootstrap)
                .withClientId(clientId)
                .withCleanSession(true)
                .build();

        CompletableFuture<Boolean> connected = mqttConnection.connect();
        if (connected.get())
            System.out.println("Connected to existing session.");
        else
            System.out.println("Connection to new session.");

        return new MQTTService(mqttConnection);
    }
}
