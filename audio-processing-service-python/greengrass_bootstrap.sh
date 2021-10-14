#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`

apt install -f libatlas-base-dev >> /tmp/audio_processor.log 2>&1


if [[ ! -d "venv" ]]
then
    echo bootstrap is installing virtualenv at `pwd`/venv >> /tmp/audio_processor.log
    virtualenv -p /usr/bin/python3 venv
fi

echo bootstrap is activating virtual environment >> /tmp/audio_processor.log
. venv/bin/activate
pip install -r $SCRIPTDIR/requirements.txt >> /tmp/audio_processor.log 2>&1
deactivate
echo bootstrap deactivated virtual environment >> /tmp/audio_processor.log 
echo bootstrap finished >> /tmp/audio_processor.log 
