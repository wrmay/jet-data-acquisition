import com.google.gson.Gson;
import com.sorintlab.jet.data.acquisition.audio.MonitoringJob;
import com.sorintlab.tone.Generator;
import junit.framework.Assert;
import org.junit.Test;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Tests {


    @Test
    public void rmsDoesntOverflow(){
        BigInteger shortSquared = BigInteger.valueOf(Short.MAX_VALUE).pow(2);

        BigInteger result = BigInteger.ZERO;
        for(int i=0;i< 44100; ++i){
            result = result.add(shortSquared);
        }

        BigInteger max = BigInteger.valueOf(Long.MAX_VALUE);

        Assert.assertTrue(result.compareTo(max) < 0);
    }

    @Test
    public void testRMSVolume() {
        int sampleRate = 44100;
        short expected = (short) (Short.MAX_VALUE * .707);
        short[] samples = new short[sampleRate];
        for (int i = 0; i < samples.length; ++i) {
            samples[i] = Generator.sinwave((double) sampleRate, 1.0, 0.0, Short.MAX_VALUE, i);
        }

        short result = MonitoringJob.rmsVolume(samples);
        Assert.assertTrue(result >= (short) (.99 * expected));
        Assert.assertTrue( result <= (short)(1.01 * expected));
    }

    @Test
    public void encodeDecode(){
        int sampleRate = 44100;
        int freq = 1000;

        short []samples = new short[sampleRate];

        ByteBuffer buffer = ByteBuffer.allocate( sampleRate * 2);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        for(int n=0;n < sampleRate; ++n){
            samples[n] = Generator.sinwave((double) sampleRate, (double) freq, 0.0, Short.MAX_VALUE, n);
            buffer.putShort(samples[n]);
        }

        byte [] encodedBytes = buffer.array();

        short []decoded = MonitoringJob.decode16BitRawAudio(encodedBytes);

        Assert.assertEquals(samples.length, decoded.length);
        for(int n=0;n < samples.length; ++n){
            Assert.assertEquals(samples[n], decoded[n]);
        }

    }

    @Test
    public void gsonTest(){
        short [][]shorts = {{1,1},{2,2},{3,17},{4,99}};
        Gson g = new Gson();
        String json = g.toJson(shorts);

        short[][] result = g.fromJson(json, short[][].class);
        Assert.assertEquals(shorts.length, result.length);
        for(int i=0; i < shorts.length; ++i) {
            Assert.assertEquals(shorts[i].length, result[i].length);
            for(int j=0; j < shorts[i].length; ++j){
                Assert.assertEquals(shorts[i][j], result[i][j]);
            }
        }
    }
}
