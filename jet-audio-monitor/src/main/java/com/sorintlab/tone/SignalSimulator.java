package com.sorintlab.tone;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.sorintlab.jet.data.acquisition.audio.AudioSample;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class SignalSimulator {
    private static final int SAMPLE_SECONDS = 5;

    private SineWave16Generator []generators;

    

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("SignalSimulator").build().defaultHelp(true)
                .description("Generate sound samples and send them to Hazelcast Jet");
        parser.addArgument("--disableHazelcast").type(Boolean.class).required(false).setDefault(Boolean.FALSE)
                .setConst(Boolean.TRUE).nargs("?");
        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e1) {
            parser.handleError(e1);
            System.exit(1);
        }

        boolean disableHazelcast = ns.getBoolean("disableHazelcast");

        HazelcastInstance hz = null;
        IMap<Integer, AudioSample> map = null;

        if (!disableHazelcast){
            hz = HazelcastClient.newHazelcastClient();
            map = hz.getMap("audio");
        }

        ObjectMapper mapper = new ObjectMapper();
        SignalSimulator ss = null;
        try {
            ss = mapper.readValue(new File("SignalSimulator.json"), SignalSimulator.class);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }
        int sampleRate = ss.generators[0].getSampleRate();

        // NOTE: sample rate configuration should be the same for every generator

        // now and n seconds after now, generate one second's worth of samples
        // put those in a hz entry in a map
        long nextWakeup = System.currentTimeMillis();
        long sleepTime = 0;
        ShortBuffer secondBuffer = ShortBuffer.allocate(sampleRate);

        ByteBuffer[] exampleBuffers = new ByteBuffer[ss.generators.length];
        for(int i=0;i < exampleBuffers.length; ++i){
            exampleBuffers[i] = ByteBuffer.allocate(2 *sampleRate * SAMPLE_SECONDS);
            exampleBuffers[i].order(ByteOrder.LITTLE_ENDIAN);
        }

        int s = 0;
        while (true) {
            long now = System.currentTimeMillis();
            int buffer_num = 0;
            AudioSample sample = null;
            for (SineWave16Generator generator : ss.generators) {
                secondBuffer.clear();
                generator.writeSamples(secondBuffer, s);
                if (s < SAMPLE_SECONDS)
                    generator.writeSamples(exampleBuffers[buffer_num], s);

                /*
                 * Note that the array backing secondBuffer is reused. We can only get away with
                 * this because the put sends the data to another process, effectively making a
                 * copy. If the putAsync method is used, it will become necessary to copy the
                 * byte array.
                 */
                sample = new AudioSample(buffer_num, now, secondBuffer.array());
                if (map != null) map.put(sample.getId(), sample);

                buffer_num += 1;
            }

            s += 1;
            nextWakeup += 1000;
            if (s == SAMPLE_SECONDS) {
                buffer_num = 0;
                for (ByteBuffer exampleBuffer : exampleBuffers) {
                    exampleBuffer.flip();
                    ByteArrayInputStream bis = new ByteArrayInputStream(exampleBuffer.array());
                    AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float) sampleRate, 16,
                            1, 2, (float) sampleRate, false);
                    AudioInputStream ais = new AudioInputStream(bis, audioFormat, 5 * sampleRate);

                    File outFile = new File("sample_" + buffer_num + ".wav");
                    try {
                        AudioSystem.write(ais, AudioFileFormat.Type.WAVE, outFile);
                        System.out.println("SAMPLE WRITTEN TO " + outFile.getAbsolutePath());
                    } catch (IOException iox) {
                        iox.printStackTrace(System.err);
                    }
                    buffer_num += 1;
                }
            }

            sleepTime = nextWakeup - System.currentTimeMillis();
            if (sleepTime < -5000) {
                System.err.println("cant't go that fast");
                System.exit(1);
            }

            if (sleepTime > 0) sleep(sleepTime);
        }

    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ix) {
            System.out.println("Interrupted - exiting");
            System.exit(0);
        }
    }

    public SineWave16Generator[] getGenerators() {
        return generators;
    }

    public void setGenerators(SineWave16Generator[] generators) {
        this.generators = generators;
    }
}
