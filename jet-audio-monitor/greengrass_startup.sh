#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`
nohup java -cp  $SCRIPTDIR/target/jet-audio-monitor-1.0-SNAPSHOT.jar \
    -Dhazelcast.config=$SCRIPTDIR/hazelcast.yaml\
    com.sorintlab.jet.data.acquisition.audio.MonitoringJob \
    --audio-service-host localhost \
    --audio-service-port 9091 \
    --aws-mqtt-endpoint a17sav9lrv8l6k-ats.iot.us-east-2.amazonaws.com \
    --aws-mqtt-client-id jet \
    --aws-mqtt-topic audio_samples \
    --aws-client-cert-file /opt/greengrass/v2/thingCert.crt \
    --aws-client-private-key-file /opt/greengrass/v2/privKey.key \
    --output-topic audio_summaries  </dev/null >> /tmp/jet-audio-monitor.log 2>&1 &
echo $! > jet-audio-monitor.pid    
