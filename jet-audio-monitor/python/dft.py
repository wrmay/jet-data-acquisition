import json
import math
import numpy as np
import random


def transform_list(in_list):
    # the input list is a list of string, each of which is a json-encoded list of numbers
    sample_lists = [json.loads(item) for item in in_list]

    # assumes all lists are of the same length !
    count = len(sample_lists[0])

    # freq_components is a list where each entry is an ndarray of scalar with one entry for each positive freq
    # in [0,count/2) Hz representing the magnitude of the component (phase ignored)
    freq_components = [np.abs(np.fft.fft(item)[0:int(count / 2)])/(count/2) for item in sample_lists]

    result = []
    for component_list in freq_components:
        selector = component_list > 328  # selector is an ndarray of boolean
        magnitudes = [int(item) for item in list(component_list[selector])]
        freqs = list(np.array(range(int(count/2)))[selector])
        freqs = [int(f) for f in freqs]  # json doesn't like numpy types
        result.append([(f, m) for (f, m) in zip(freqs, magnitudes)])

    return [json.dumps(item) for item in result]


if __name__ == '__main__':

    sample_rate = 100
    freq1 = 10
    freq2 = 25
    signal1 = [int(32768 * math.sin(n * freq1 * 2 * math.pi / sample_rate)) for n in range(sample_rate)]
    signal2 = [int(32768 * math.sin(n * freq2 * 2 * math.pi / sample_rate)) for n in range(sample_rate)]
    signal3 = [s1/2 + s2/2 for (s1, s2) in zip(signal1, signal2)]
    signal4 = [s1 + 20000 * 2 * (random.random() - .5) for s1 in signal1]

    input_list = [json.dumps(signal) for signal in [signal1, signal2, signal3, signal4]]

    print(transform_list(input_list))
