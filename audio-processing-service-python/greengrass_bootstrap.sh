#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`

echo bootstrap running as `whoami` in `pwd` > /tmp/audio_processor.log 2>&1

if [[ ! -d "venv" ]]
then
    echo bootstrap is installing virtualenv at `pwd`/venv >> /tmp/audio_processor.log
    virtualenv -p /usr/bin/python3 venv >> /tmp/audio_processor.log 2>&1
else 
    echo using existing virtual environment at `pwd`/venv >> /tmp/audio_processor.log
fi

. venv/bin/activate
echo bootstrap is installing required packages into virtual environment at `pwd`/venv >> /tmp/audio_processor.log
pip install -r $SCRIPTDIR/requirements.txt >> /tmp/audio_processor.log 2>&1
deactivate
echo bootstrap finished >> /tmp/audio_processor.log 
