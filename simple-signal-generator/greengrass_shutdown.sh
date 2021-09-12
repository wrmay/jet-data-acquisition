#!/bin/bash
set -e

if [[ ! -f "signal-emulator.pid" ]]
then
   echo signal-emulator.pid file not present
   exit 1
fi

kill `cat signal-emulator.pid`
rm signal-emulator.pid

