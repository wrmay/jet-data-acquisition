package com.sorintlab.tone;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;

public class Generator
{
    // generates 16 bit (signed) samples
    public static short sinwave(double sampleRate, double freq, double phase, short amplitude, int n){
        double angle = phase + (n * Math.PI * 2.0d * freq) / sampleRate;
        double val = Math.sin(angle);

        double result = amplitude * val;
        return (short) result;
    }

    private static Random rand = new Random();

    public static short noise(short amplitude){
        double result = rand.nextDouble() * amplitude;
        return (short) result;
    }

    /**
     * Weights must add to one but this is not checked
     */
    public static short weightedSum(short v1, double w1, short v2, double w2, short v3, double w3){
        double result = 0;
        result += v1 * w1;
        result += v2 * w2;
        result += v3 * w3;

        return (short) result;
    }

    static void sleep(long millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ix) {
            System.out.println("Interrupted - exiting");
            System.exit(0);
        }
    }

    public static void main( String[] args )
    {
        HazelcastInstance hz = HazelcastClient.newHazelcastClient();
        IMap<Long, byte[]> map = hz.getMap("audio");

        // now and n seconds after now, generate sampleRate samples starting with sample for n*sampleRate
        // put those in a hz entry in a map

        int durationSecs = 300;
        double sampleRate = 44100d;

        double wave1Freq = 8000;
        short wave1Amp = Short.MAX_VALUE;
        double wave1Weight = 0.5d;
        double wave2Freq = 800;
        short wave2Amp = Short.MAX_VALUE;
        double wave2Weight = 0.5d;
        short noiseAmp = Short.MAX_VALUE;
        double noiseWeight = 0.0d;

        int samples = (int) (durationSecs * sampleRate);

        long nextWakeup = System.currentTimeMillis();
        long sleepTime = 0;
        ByteBuffer buffer = ByteBuffer.allocate(2 * (int)sampleRate);
        for(int s = 0; s < durationSecs; ++s){
            buffer.clear();
            for(int i=0; i<sampleRate; ++i){
                int n = s * (int) sampleRate + i;
                short w1 = sinwave(sampleRate, wave1Freq, 0.0, wave1Amp, i);
                short w2 = sinwave(sampleRate, wave2Freq, 0.0, wave2Amp, i);
                short noise = noise(noiseAmp);
                buffer.putShort(weightedSum(w1, wave1Weight, w2, wave2Weight, noise, noiseWeight));
            }

            long now = System.currentTimeMillis();
            map.put(now, buffer.array());  // if we go async, make sure to copy the byte []
            System.out.println("PUT at " + now);


            nextWakeup += 1000;
            sleepTime = nextWakeup - System.currentTimeMillis();
            if (sleepTime < 0){
                System.err.println("cant't go that fast");
                System.exit(1);
            }
            Generator.sleep(sleepTime);
        }

//        buffer.flip();
//        ByteArrayInputStream bis = new ByteArrayInputStream(buffer.array());
//        AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) sampleRate, 16, 1, 2, (float) sampleRate, false);
//        AudioInputStream ais = new AudioInputStream(bis, audioFormat, samples);
//
//        File outFile = new File("test.wav");
//        try {
//            AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outFile);
//        } catch(IOException iox){
//            iox.printStackTrace(System.err);
//            System.exit(1);
//        }
    }
}
