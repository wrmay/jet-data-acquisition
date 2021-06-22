#!/bin/bash

# you must have python grpcio tools installed

HERE=`dirname $0`
python -m grpc_tools.protoc --python_out $HERE/audio-processing-service-python --grpc_python_out $HERE/audio-processing-service-python  -I $HERE  audio_processor.proto
