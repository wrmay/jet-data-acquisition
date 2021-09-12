#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`

zip $SCRIPTDIR/signal-emulator.zip \
    $SCRIPTDIR/target/simple-signal-generator-1.0-SNAPSHOT.jar  \
    $SCRIPTDIR/hazelcast-client.xml \
    $SCRIPTDIR/greengrass_startup.sh \
    $SCRIPTDIR/greengrass_shutdown.sh \
    $SCRIPTDIR/HealthySignal.json \
    $SCRIPTDIR/UnhealthySignal.json 

aws s3 cp $SCRIPTDIR/signal-emulator.zip s3://greengrass-components-691990859209-us-east-2/com.sorintlab.jet.audio.signal.emulator/1.0.3/signal-emulator.zip

