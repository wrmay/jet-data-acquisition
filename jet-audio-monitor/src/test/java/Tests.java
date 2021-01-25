import com.sorintlab.jet.data.acquisition.audio.MonitoringJob;
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


}
