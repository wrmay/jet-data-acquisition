#!/bin/sh
python -m grpc_tools.protoc -I ../jet-audio-monitor/src/main/proto  --python_out . --grpc_python_out . audio_processor.proto
