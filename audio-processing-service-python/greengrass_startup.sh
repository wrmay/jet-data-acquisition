#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`
echo startup is activating virtualenv at `pwd`/venv 
. venv/bin/activate
nohup python $SCRIPTDIR/audio_processor.py </dev/null >> /tmp/audio_processor.log 2>&1 &
echo $! > audio_processor.pid    
