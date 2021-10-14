#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`

zip $SCRIPTDIR/jet-audio-monitor.zip \
    $SCRIPTDIR/target/jet-audio-monitor-1.0-SNAPSHOT.jar \
    $SCRIPTDIR/hazelcast.yaml \
    $SCRIPTDIR/greengrass_startup.sh \
    $SCRIPTDIR/greengrass_shutdown.sh 

aws s3 cp $SCRIPTDIR/jet-audio-monitor.zip s3://${GG_S3_BUCKET}/com.sorintlab.jet.audio.monitor/1.0.8/jet-audio-monitor.zip

