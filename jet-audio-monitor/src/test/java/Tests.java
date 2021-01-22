import com.google.gson.Gson;
import com.sorintlab.jet.data.acquisition.audio.MonitoringJob;
import com.sorintlab.tone.SignalSimulator;
import com.sorintlab.tone.SineWave16Generator;

import org.junit.Assert;
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
        ByteBuffer testBuffer = ByteBuffer.allocate(sampleRate * 2);
        testBuffer.order(ByteOrder.LITTLE_ENDIAN);
        SineWave16Generator generator = new SineWave16Generator(Short.MAX_VALUE, 1, sampleRate, 0.0);
        generator.writeSamples(testBuffer, 0);
        testBuffer.flip();

        short[] samples = new short[sampleRate];
        testBuffer.asShortBuffer().get(samples);

        short result = MonitoringJob.rmsVolume(samples);
        short expected = (short) (Short.MAX_VALUE * .707);
        Assert.assertTrue(result >= (short) (.99 * expected));
        Assert.assertTrue( result <= (short)(1.01 * expected));
    }

    @Test
    public void encodeDecode(){
        int sampleRate = 44100;
        int freq = 1000;

        ByteBuffer testBuffer = ByteBuffer.allocate(sampleRate * 2);
        testBuffer.order(ByteOrder.LITTLE_ENDIAN);
        SineWave16Generator generator = new SineWave16Generator(Short.MAX_VALUE, freq, sampleRate, 0.0);
        generator.writeSamples(testBuffer, 0);
        testBuffer.flip();

        short []samples = new short[sampleRate];
        testBuffer.asShortBuffer().get(samples);

        byte [] encodedBytes = testBuffer.array();
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
