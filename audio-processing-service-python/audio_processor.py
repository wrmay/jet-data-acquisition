import audio_processor_pb2
import audio_processor_pb2_grpc
import concurrent.futures
import grpc
import numpy as np


class AudioProcessor(audio_processor_pb2_grpc.AudioAnalyzerServicer):
    def ComputeSpectrum(self, request_iterator, context):
        for sample in request_iterator:
            spectrum = self.calculate_spectrum(sample.sample)
            result = audio_processor_pb2.Spectrum(id=sample.id, timestamp=sample.timestamp, components=spectrum)
            yield result

    def ComputeSummary(self, request_iterator, context):
        for sample in request_iterator:
            spectrum = self.calculate_spectrum(sample.sample)
            vol = self.calculate_rms_volume(sample.sample)
            result = audio_processor_pb2.AudioSummary(id=sample.id, timestamp=sample.timestamp, components=spectrum,
                                                      rmsVolume=int(vol))
            yield result

    def calculate_rms_volume(self, sample):
        """ sample is bytes, return is an integer """
        shorts = np.frombuffer(sample, dtype='<i2')
        mean_of_squares = np.mean(np.square(shorts, dtype=np.int64))
        return int(np.sqrt(mean_of_squares, dtype=np.float32))

    def calculate_spectrum(self, sample):
        """ sample is bytes, return is a list of SpectrumComponent"""
        shorts = np.frombuffer(sample, dtype='<i2')
        count = shorts.shape[0]
        spec = np.abs(np.fft.fft(shorts)[0:int(count / 2)]) / (count / 2)
        selector = spec > 328
        freqs = np.nonzero(selector)[0]
        mags = spec[selector]
        result =  [audio_processor_pb2.SpectrumComponent(frequency=freqs[i], amplitude=int(mags[i])) for i in
                range(len(freqs))]
        print(f"SPECTRUM HAS {len(result)} COMPONENTS")
        return result


if __name__ == '__main__':
    server = grpc.server(concurrent.futures.ThreadPoolExecutor(max_workers=10))
    audio_processor_pb2_grpc.add_AudioAnalyzerServicer_to_server(AudioProcessor(), server)
    server.add_insecure_port('[::]:9091')
    server.start()    
    server.wait_for_termination()
