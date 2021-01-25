import json
import math
import numpy as np
import random

# TODO rework this to reflect AudioSamples in and AudioSpectrum out

def transform_list(in_list):
    # the input list is a list of string, each of which is a json-encoded audio sample with the 
    # following format
    #
    # {
    #   "id": 9,
    #   "timestamp" : 123456789,
    #    "sample": [22,23,...]
    # }
    #

    sample_lists = [json.loads(item) for item in in_list]

    # assumes all lists are of the same length !
    count = len(sample_lists[0]['sample'])

    # freq_components is a list where each entry is an ndarray of scalar with one entry for each positive freq
    # in [0,count/2) Hz representing the magnitude of the component (phase ignored)
    freq_components = [np.abs(np.fft.fft(item['sample'])[0:int(count / 2)])/(count/2) for item in sample_lists]

    results = [ {"id": item["id"], "timestamp" : item["timestamp"], "components" : []} for item in sample_lists]

    result = []
    for component_list, result in zip(freq_components, results):
        for f, c in enumerate(component_list[0: int(count/2)]):
            if c > 328:
                result["components"].append({"frequency": int(f), "amplitude": int(c)})

    return [json.dumps(item) for item in results]


# if __name__ == '__main__':
#
#     sample_rate = 100
#     freq1 = 10
#     freq2 = 25
#     signal1 = [int(32768 * math.sin(n * freq1 * 2 * math.pi / sample_rate)) for n in range(sample_rate)]
#     signal2 = [int(32768 * math.sin(n * freq2 * 2 * math.pi / sample_rate)) for n in range(sample_rate)]
#     signal3 = [s1/2 + s2/2 for (s1, s2) in zip(signal1, signal2)]
#     signal4 = [s1 + 20000 * 2 * (random.random() - .5) for s1 in signal1]
#
#     input_list = [json.dumps(signal) for signal in [signal1, signal2, signal3, signal4]]
#
#     print(transform_list(input_list))
