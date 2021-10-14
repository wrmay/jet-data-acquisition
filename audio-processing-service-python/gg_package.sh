#!/bin/bash
set -e
SCRIPTDIR=`dirname $0`

zip $SCRIPTDIR/audio_processor.zip \
    $SCRIPTDIR/audio_processor.py \
    $SCRIPTDIR/audio_processor_pb2_grpc.py \
    $SCRIPTDIR/audio_processor_pb2.py \
    $SCRIPTDIR/greengrass_bootstrap.sh \
    $SCRIPTDIR/greengrass_startup.sh \
    $SCRIPTDIR/greengrass_shutdown.sh \
    $SCRIPTDIR/requirements.txt 

aws s3 cp $SCRIPTDIR/audio_processor.zip s3://${GG_S3_BUCKET}/com.sorintlab.audioprocessor.service/1.0.8/audio_processor.zip 