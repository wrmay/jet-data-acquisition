#!/bin/bash
set -e

if [[ ! -f "audio_processor.pid" ]]
then
   echo audio_processor.pid file not present
   exit 1
fi

kill `cat audio_processor.pid`
rm audio_processor.pid

