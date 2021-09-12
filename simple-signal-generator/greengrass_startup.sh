#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`
nohup java -cp $SCRIPTDIR/target/simple-signal-generator-1.0-SNAPSHOT.jar \
 -Dhazelcast.client.config=$SCRIPTDIR/hazelcast-client.xml  com.sorintlab.tone.SignalSimulator \
  --generator-config  $SCRIPTDIR/$1 --audio-sample-dir /tmp \
  < /dev/null >> /tmp/signal-emulator.log &

echo $! > signal-emulator.pid    
