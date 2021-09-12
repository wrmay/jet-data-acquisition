#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`

zip $SCRIPTDIR/jet-audio-monitor.zip \
    $SCRIPTDIR/target/jet-audio-monitor-1.0-SNAPSHOT.jar \
    $SCRIPTDIR/hazelcast.yaml \
    $SCRIPTDIR/greengrass_startup.sh \
    $SCRIPTDIR/greengrass_shutdown.sh 

aws s3 cp $SCRIPTDIR/jet-audio-monitor.zip s3://greengrass-components-691990859209-us-east-2/com.sorintlab.jet.audio.monitor/1.0.5/jet-audio-monitor.zip

