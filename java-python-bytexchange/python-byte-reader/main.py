import numpy as np

if __name__ == '__main__':
    with open('../java-byte-writer/bytes.data', 'rb') as f:
        all_bytes = f.read()

    a = np.frombuffer(all_bytes, dtype='<i2')
    print(a)
    print(type(a))
    print(a.ndim)
    print(a.shape)
    print(a.dtype)

