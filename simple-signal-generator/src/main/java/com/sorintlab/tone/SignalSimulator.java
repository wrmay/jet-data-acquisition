package com.sorintlab.tone;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import com.sorintlab.jet.data.acquisition.audio.AudioSample;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;
import net.sourceforge.argparse4j.inf.Namespace;

import java.util.concurrent.TimeUnit;

public class SignalSimulator {

    // settings
    private SineWave16Generator[] generators;

    public static void main(String[] args) {
        ArgumentParser parser = ArgumentParsers.newFor("SignalSimulator").build().defaultHelp(true)
                .description("Generate sound samples and send them to Hazelcast Jet");
        parser.addArgument("--disableHazelcast").type(Boolean.class).required(false).setDefault(Boolean.FALSE)
                .setConst(Boolean.TRUE).nargs("?").help("disable hazelcast connection for testing purposes");
        parser.addArgument("--generator-config").required(true).help("the path to a json file containing the signal generator configuration.");
        parser.addArgument("--audio-sample-dir").required(false).help("if provided, audio samples will be written to this directory");

        Namespace ns = null;
        try {
            ns = parser.parseArgs(args);
        } catch (ArgumentParserException e1) {
            parser.handleError(e1);
            System.exit(1);
        }

        boolean disableHazelcast = ns.getBoolean("disableHazelcast");
        String configFile = ns.getString("generator_config");
        String sampleDir = ns.get("audio_sample_dir");

        HazelcastInstance hz = null;
        IMap<Integer, AudioSample> map = null;

        if (!disableHazelcast){
            hz = HazelcastClient.newHazelcastClient();
            map = hz.getMap("audio");
        }

        ObjectMapper mapper = new ObjectMapper();
        SignalSimulator ss = null;
        try {
            ss = mapper.readValue(new File(configFile), SignalSimulator.class);
        } catch (IOException e) {
            e.printStackTrace(System.err);
            System.exit(1);
        }

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        for (SineWave16Generator generator: ss.generators){
            generator.setMap(map);
            generator.setSampleDir(sampleDir);
            executor.scheduleAtFixedRate(generator, 0, 1, TimeUnit.SECONDS);
        }

        if (!disableHazelcast) {
            Runtime.getRuntime().addShutdownHook(new Thread(hz::shutdown));
        }

        System.out.println("Launched");
    }

    public SineWave16Generator[] getGenerators() {
        return generators;
    }

    public void setGenerators(SineWave16Generator[] generators) {
        this.generators = generators;
    }


}
