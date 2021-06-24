# Overview

This project is a demonstration of Hazelcast Jet as a data acquisition layer and a sandbox for experimenting with high frequency data acquisition use cases.  Jet can provide a light weight infrastructure for pre-processing very large event streams and turning them into more meaningful information that is much lower volume.

In this project, Jet is used to process 200kHz audio data sources (i.e. sensors that produce 200,00 reading each second ).  The input is a stream of raw data consisting of 200,000 sound level measurements per second per source and the output is a list of major spectrum component of the sound signal, and the volume. The input is a few hundred thousand integers per source per second and the output is a small handful of integers per source per second.   Not only are the frequency analysis and the volume of more use to other systems (e.g. monitoring and alerting systems), they are smaller by more than 4 orders of magnitude.  

Additionally, the actual calculations,  a discrete Fourier transform and a root mean square calculation are performed in python using numpy.  This project demonstrates the use of Jet's GrpcService capability to incorporate non java processors (potentially multiple, written in different languages) into an event processing pipeline.

The diagram below shows the components of this system and how they interact.

![Architecture Overview](overview.png)

The Audio Event Generator generates 200,000 measurements each second for each source.  The audio consists of a configurable combination of simple sine waves.  For each source + second, an `AudioSample` object is generated .  That object is  `put` into a Hazelcast `IMap` that is hosted within the pipeline. A stripped down version of the `AudioSample` object is shown below.  

```java
public class AudioSample implements Serializable { 
    private int id;
    private long timestamp;
    private short []sample;  
}
```

The Jet pipeline running within the IMDG/Jet node treats the "puts" to the IMDG map as input events.  These are routed to a service that is hosted in python and running on the same machine (it could also be on a different host).  Using gRPC's 2 way event streaming protocol, `AudioSample` objects are serialized using protobuf protocol and sent to the (python) Audio Analyzer service.  The Audio Analyzer service performs the requisite calculations and sends back one `AudioSummary` for each source + second (i.e. for each input event).  The Jet pipeline then prints the summaries to the console.

In a more realistic application, the Jet pipeline would  forward the summaries on to a different system for further analysis and /or perform monitoring and alerting directly within the pipeline.

The file `audio_processor.proto` defines the contract between the Jet stream stage and the python service.  It is included below.

```protobuf
syntax = "proto3";

package audio_processor;

message AudioSample {
    sint32 id = 1;
    int64  timestamp = 2;
    bytes  sample = 3;
}

message SpectrumComponent {
    int32 frequency = 1;
    int32 amplitude = 2;
}

message Spectrum {
    sint32 id = 1;
    int64  timestamp = 2;
    repeated SpectrumComponent components = 3;
}

message AudioSummary {
    sint32 id = 1;
    int64 timestamp = 2;
    int32 rmsVolume = 3;
    repeated SpectrumComponent components = 4;
}

service AudioAnalyzer {
    rpc ComputeSpectrum(stream AudioSample) returns (stream Spectrum){}
    rpc ComputeSummary(stream AudioSample) returns (stream AudioSummary){}
}
```

# Walk Through

### Setup and Prerequisites

1. This demo uses Hazelcast Jet Enterprise so you will need a license.
2. Docker desktop.
3. A working Maven installation to build the java portions of the project

Create a file called ".env" to hold the environment variables that will be used by 
Docker Compose. Set the JET_LICENSE_KEY and MC_LICENSE_KEY environment variable to 
your license key as shown below (both entries have the same value).

```
JET_LICENSE_KEY=5Nodes#ABCDefg
MC_LICENSE_KEY=5Nodes#ABCDefg
```



Build and install the gRPC stubs.

```
cd audio-processing-service-java
mvn install
cd ..
```


Build and install the signal generator.  Note that the "simple-signal-generator" project defines POJOs that are used by the Jet job.

```
cd simple-signal-generator
mvn install
cd ..
```



Build the Audio Monitor Jet job.

```
cd jet-audio-monitor
mvn package
cd ..
```



### Start Everything

```
docker-compose up -d
```

To start a multi-node Jet cluster, use the "scale" option as shown below.

```
docker-compose up -d --scale jet=2
```

To view the logs, use `docker-compose logs --follow`. Press ctrl-c to exit.



This will start Jet, the signal generator, the Hazelcast Management Center , the python audio processor gRPC service , the Prometheus time series DB and Grafana.  It does not deploy the Jet job.

The Hazelcast Management Center can be accessed on Port 8080.  The following images show how to log in and view the cluster.

In a browser, go to "localhost:8080".

![](screenshots/Snapshot 4.png)
![](screenshots/Snapshot 5.png)
![](screenshots/Snapshot 10.png)
![](screenshots/Snapshot 7.png)
![](screenshots/Snapshot 8.png)
![](screenshots/Snapshot 9.png)



### Submit the Job

```
docker-compose run submitjob
```



You should now be able to see the running Jet job in the Management Center as show below.

![](screenshots/Snapshot 12.png)
![](screenshots/Snapshot 13.png)
![](screenshots/Snapshot 14.png)

### View the results in Grafana

Grafana will need to be configured.  Go to localhost:3000 in the browser and follow the instructions below.

![](screenshots/Snapshot 15.png)
![](screenshots/Snapshot 16.png)
![](screenshots/Snapshot 17.png)
![](screenshots/Snapshot 18.png)
![](screenshots/Snapshot 19.png)
![](screenshots/Snapshot 20.png)
![](screenshots/Snapshot 21.png)
![](screenshots/Snapshot 22.png)
![](screenshots/Snapshot 23.png)



### Update the Signal Generator

Now we can change the signal generator



# Developer Information



### Project Contents

TODO

### Regenerating the gRPC stubs and skeletons for Python

TODO

### Regenerating gRPC stubs and skeletons for Java

Install the protobuf compiler from here: https://github.com/protocolbuffers/protobuf/releases.  
Do not get the language specific package, get the platform specific package, for example, 
https://github.com/protocolbuffers/protobuf/releases/download/v3.17.3/protoc-3.17.3-osx-x86_64.zip.  
Execute it once while holding down the "Option" key so you can tell MacOs to trust it.

Obtain the grpc java plugin to the protoc compiler here: 
https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/1.38.1/ .  Rename the download to 
`protoc-gen-grpc-java` make it executable, _and put it in the same directory as protoc_.  
Execute it once while holding down the "Option" key so you can tell MacOs to trust it.

Edit `generate-audio-processing-service-java.sh` and set PROTOC_PATH to the directory that 
contains `protoc` and `protoc-gen-grpc-java`. You can now generate the java components of 
the audio processing service by running the script.

