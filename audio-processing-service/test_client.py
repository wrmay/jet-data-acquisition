import audio_processor_pb2
import audio_processor_pb2_grpc
import grpc
import math
import struct
import time

class AudioGenerator:
    def __init__(self):
        self.period = 1
        self.next_wakeup = time.time()
        self.sample_rate = 200000

        freq1 = 10
        freq2 = 25
        signal1 = [int(32768 * math.sin(n * freq1 * 2 * math.pi / self.sample_rate)) for n in range(self.sample_rate)]
        signal2 = [int(32768 * math.sin(n * freq2 * 2 * math.pi / self.sample_rate)) for n in range(self.sample_rate)]
        self.signal = [ int(s1/2 + s2/2) for (s1, s2) in zip(signal1, signal2)]
        self.signal_bytes = struct.pack(f'<{self.sample_rate}h', *self.signal)

    def samples(self):
        while(True):
            sleep_time = self.next_wakeup - time.time()
            if sleep_time < -5:
                raise Exception('Cant keep up')
            elif sleep_time > 0:
                time.sleep(sleep_time)

            self.next_wakeup += 1
            yield audio_processor_pb2.AudioSample(id=1, timestamp = int(time.time() * 1000), sample=self.signal_bytes)

if __name__ == '__main__':
    audio_generator = AudioGenerator()

    channel = grpc.insecure_channel('localhost:9090')
    audio_processor = audio_processor_pb2_grpc.AudioAnalyzerStub(channel)
    for spectrum in audio_processor.ComputeSpectrum(audio_generator.samples()):
        print(spectrum)

