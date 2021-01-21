package com.sorintlab.tone;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.ArgumentParsers;

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
        ArgumentParser parser = ArgumentParsers.newFor("Generator").build().defaultHelp(true).description("Generate simulated audio samples and write them to a Jet cluster");
        parser.addArgument("--signal-count").type(Integer.TYPE).setDefault(3);

        

        int freq = 1000;
        short amp = Short.MAX_VALUE;

        if (args.length > 0){
            try {
                freq = Integer.parseInt(args[0]);
            } catch(NumberFormatException nfx){

            }
        }

        if (args.length > 1){
            try {
                amp = Short.parseShort(args[1]);
            } catch(NumberFormatException nfx){

            }
        }

        System.out.println("FREQUENCY: " + freq + "Hz");
        System.out.println("AMPLITUDE: " + amp);

        HazelcastInstance hz = HazelcastClient.newHazelcastClient();
        IMap<Long, byte[]> map = hz.getMap("audio");

        // now and n seconds after now, generate sampleRate samples starting with sample for n*sampleRate
        // put those in a hz entry in a map
        int sampleRate = 40000;
        long nextWakeup = System.currentTimeMillis();
        long sleepTime = 0;
        ByteBuffer exampleBuffer = ByteBuffer.allocate(2 * sampleRate * 5);
        exampleBuffer.order(ByteOrder.LITTLE_ENDIAN);
        ByteBuffer secondBuffer = ByteBuffer.allocate(2 *  sampleRate);
        secondBuffer.order(ByteOrder.LITTLE_ENDIAN);

        int s = 0;
        while(true){
            s += 1;
            secondBuffer.clear();
            for(int i=0; i <sampleRate; ++i){
                int n = s * (int) sampleRate + i;
                short w = sinwave(sampleRate, freq, 0.0, amp, n);
                secondBuffer.putShort(w);
                if (exampleBuffer != null)
                    exampleBuffer.putShort(w);
            }

            long now = System.currentTimeMillis();
            map.put(now, secondBuffer.array());  // if we go async, make sure to copy the byte []

            nextWakeup += 1000;
            if (s == 5){
                nextWakeup += 2000;
                exampleBuffer.flip();
                ByteArrayInputStream bis = new ByteArrayInputStream(exampleBuffer.array());
                AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) sampleRate, 16, 1, 2, (float) sampleRate, false);
                AudioInputStream ais = new AudioInputStream(bis, audioFormat, 5 * sampleRate);

                File outFile = new File("test.wav");
                try {
                    AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outFile);
                    System.out.println("SAMPLE WRITTEN TO \"test.wav\"");
                } catch(IOException iox){
                    iox.printStackTrace(System.err);
                    System.exit(1);
                }
                exampleBuffer = null;
            }
            sleepTime = nextWakeup - System.currentTimeMillis();
            if (sleepTime < 0){
                System.err.println("cant't go that fast");
                System.exit(1);
            }
            Generator.sleep(sleepTime);
        }

    }
}
