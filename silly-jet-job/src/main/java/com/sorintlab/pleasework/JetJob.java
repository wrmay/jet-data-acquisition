package com.sorintlab.pleasework;

import com.hazelcast.jet.*;
import com.hazelcast.jet.config.JobConfig;
import com.hazelcast.jet.pipeline.Pipeline;
import com.hazelcast.jet.pipeline.Sinks;
import com.hazelcast.jet.pipeline.StreamStage;
import com.hazelcast.jet.pipeline.test.TestSources;
import com.hazelcast.jet.python.PythonServiceConfig;
import com.hazelcast.jet.python.PythonTransforms;

public class JetJob {

    public static void main(String []args){
        System.setProperty("hazelcast.config", "hazelcast.yaml");
        JetInstance jet = Jet.newJetInstance();

        Job job = jet.newJob(buildPipeline());
        job.join();
    }

    static Pipeline buildPipeline(){
        Pipeline result = Pipeline.create();
        StreamStage<String> stringStreamStage =
                result.readFrom(TestSources.<String>itemStream(1, (ts, seq) -> Long.valueOf(seq).toString())).withoutTimestamps();

//        String baseDir = Util.getFilePathOfClasspathResource("python").toString();
//        System.out.println("BASEDIR: " + baseDir);
        StreamStage<String> outputStream = stringStreamStage.apply(PythonTransforms.mapUsingPython(new PythonServiceConfig().setBaseDir("python").setHandlerModule("echo"))).setLocalParallelism(1);

        outputStream.writeTo(Sinks.logger());

        return result;
    }
}
