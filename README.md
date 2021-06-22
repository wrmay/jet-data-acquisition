# Overview

This project is a demonstration of Hazelcast Jet as a data acquisition layer and a sandbox for experimenting with high frequency data acquisition use cases.  Jet can provide a light weight infrastructure for pre-processing very large event streams and turning them into more meaningful information that is much lower volume.

In this project, Jet is used to process 200kHz audio data sources (i.e. sensors that produce 200,00 reading each second ).  The input is a stream of raw data consisting of 200,000 sound level measurements per second per source and the output is a list of major spectrum component of the sound signal, and the volume.  Not only are the frequency analysis and the volume of more use to other systems (e.g. monitoring and alerting systems), they are smaller by more than 4 orders of magnitude.  The input is a few hundred thousand integers per source per second and the output is a small handful of integers per source per second.  

Additionally, the actual calculations,  a discrete Fourier transform and a root mean square calculation are performed in python using numpy.  This project demonstrates the use of Jet's GrpcService capability to incorporate non java processors (potentially multiple, written in different languages) into an event processing pipeline.

The diagram below shows the components of this system and how they interact.

![Architecture Overview](overview.png)

The Audio Event Generator generates 200,000 measurements each second for each source.  For each source, for each second, the generated data consistst of regularly spaced samples from a pure sine wave of configurable frequency and amplitude.  For each source + second, an `AudioSample` object is generated containing 1 seconds' worth of data.  That object is  `put` into a Hazelcast `IMap` that is hosted within the pipeline. A stripped down version of the `AudioSample` object is shown below.  

```java
public class AudioSample implements Serializable { 
    private int id;
    private long timestamp;
    private short []sample;  
}
```

The Jet pipeline running within the IMDG/Jet node treats the "puts" to the IMDG map as input events.  These are routed to a service that is hosted in python and running on the same machine (it could also be on a different host).  Using gRPC's 2 way event streaming protocol, `AudioSample` objects are serialized using protobuf protocol and sent to the (python) Audio Analyzer service.  The Audio Analyzer service performs the requisite calculations and sends back one `AudioSummary` for each source + second (i.e. for each input event).  The Jet pipeline then prints the summaries to the console.

In a more realistic application, the Jet pipeline would  forward the summaries on to a different system for further analysis and /or perform monitoring and alerting directly within the pipeline.

The file `jet-audio-monitor/src/main/proto/audio_processor.proto` defines the contract between the Jet stream stage and the python service.  It is included below.

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

# How To

Create a file called ".env" to hold the environment variables that will be used by 
Docker Compose. Set the JET_LICENSE_KEY and MC_LICENSE_KEY environment variable to 
your license key as shown below (both entries have the same value).

```
JET_LICENSE_KEY=5Nodes#ABCDefg
MC_LICENSE_KEY=5Nodes#ABCDefg
```


# Configuring GRPC/Protobuf Source Code Generation for Java 
Obtain the grpc java plugin to the protoc compiler here: https://repo1.maven.org/maven2/io/grpc/protoc-gen-grpc-java/1.38.1/ .  Rename the download to `protoc-gen-grpc-java` 
and make it executable.  Make sure it is on the path.  Execute it once while holding 
down the "Option" key so you can tell MacOs to trust it.

Install the protobuf compiler from here: https://github.com/protocolbuffers/protobuf/releases.  Do not get the language specific package, get the platform specific package, 
for example, https://github.com/protocolbuffers/protobuf/releases/download/v3.17.3/protoc-3.17.3-osx-x86_64.zip.  Execute it once while holding down the "Option" key so you can tell MacOs to trust it.