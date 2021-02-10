import grpc
import concurrent.futures
import time
import numpy as np;

import audio_processor_pb2
import audio_processor_pb2_grpc


class AudioProcessor(audio_processor_pb2_grpc.AudioAnalyzerServicer):
    def ComputeSpectrum(self, request, context):
        result = 


server = grpc.server(concurrent.futures.ThreadPoolExecutor(max_workers=2))

audio_processor_pb2_grpc.add_AudioAnalyzerServicer_to_server(AudioProcessor, server)

server.add_insecure_port('[::]:50051')
server.start()

try:
    while True:
        time.sleep(86400)
except KeyboardInterrupt:
    server.stop(0)

