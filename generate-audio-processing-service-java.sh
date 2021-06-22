#!/bin/bash


# protoc and protoc-gen-grpc-java must be on the path
PROTOC_PATH=/Users/rmay_sorint/Downloads/software/protoc-3/bin

if [[ "$PATH" == *"$PROTOC_PATH"* ]]; then
    echo "$PROTOC_PATH is alread part of the PATH"
else
    export PATH=$PROTOC_PATH:$PATH
    echo "$PROTOC_PATH was added to the PATH"
fi

HERE=`dirname $0`

protoc --java_out=$HERE/audio-processing-service-java/src/main/java --grpc-java_out=$HERE/audio-processing-service-java/src/main/java $HERE/audio_processor.proto
echo DONE