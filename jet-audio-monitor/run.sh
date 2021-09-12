#!/bin/bash

# example invocation

java -Dhazelcast.config=../config/hazelcast.yaml \
-cp target/jet-audio-monitor-1.0-SNAPSHOT.jar com.sorintlab.jet.data.acquisition.audio.MonitoringJob  \
--audio-service-host localhost --audio-service-port 9091 \
--aws-mqtt-endpoint a17sav9lrv8l6k-ats.iot.us-east-2.amazonaws.com \
--aws-client-cert-file ../simple-signal-generator/auth/2ffa1c1a83-certificate.pem.crt \
--aws-client-private-key-file ../simple-signal-generator/auth/2ffa1c1a83-private.pem.key \
--output-topic audio_summaries --aws-mqtt-topic TEST --aws-mqtt-client-id jet