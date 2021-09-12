#!/bin/bash
set -e

if [[ ! -f "jet-audio-monitor.pid" ]]
then
   echo jet-audio-monitor.pid file not present
   exit 1
fi

kill `cat jet-audio-monitor.pid`
rm jet-audio-monitor.pid

