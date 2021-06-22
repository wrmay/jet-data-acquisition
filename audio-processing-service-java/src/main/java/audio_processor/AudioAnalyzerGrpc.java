package audio_processor;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.38.1)",
    comments = "Source: audio_processor.proto")
public final class AudioAnalyzerGrpc {

  private AudioAnalyzerGrpc() {}

  public static final String SERVICE_NAME = "audio_processor.AudioAnalyzer";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<audio_processor.AudioProcessor.AudioSample,
      audio_processor.AudioProcessor.Spectrum> getComputeSpectrumMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ComputeSpectrum",
      requestType = audio_processor.AudioProcessor.AudioSample.class,
      responseType = audio_processor.AudioProcessor.Spectrum.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<audio_processor.AudioProcessor.AudioSample,
      audio_processor.AudioProcessor.Spectrum> getComputeSpectrumMethod() {
    io.grpc.MethodDescriptor<audio_processor.AudioProcessor.AudioSample, audio_processor.AudioProcessor.Spectrum> getComputeSpectrumMethod;
    if ((getComputeSpectrumMethod = AudioAnalyzerGrpc.getComputeSpectrumMethod) == null) {
      synchronized (AudioAnalyzerGrpc.class) {
        if ((getComputeSpectrumMethod = AudioAnalyzerGrpc.getComputeSpectrumMethod) == null) {
          AudioAnalyzerGrpc.getComputeSpectrumMethod = getComputeSpectrumMethod =
              io.grpc.MethodDescriptor.<audio_processor.AudioProcessor.AudioSample, audio_processor.AudioProcessor.Spectrum>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ComputeSpectrum"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  audio_processor.AudioProcessor.AudioSample.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  audio_processor.AudioProcessor.Spectrum.getDefaultInstance()))
              .setSchemaDescriptor(new AudioAnalyzerMethodDescriptorSupplier("ComputeSpectrum"))
              .build();
        }
      }
    }
    return getComputeSpectrumMethod;
  }

  private static volatile io.grpc.MethodDescriptor<audio_processor.AudioProcessor.AudioSample,
      audio_processor.AudioProcessor.AudioSummary> getComputeSummaryMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ComputeSummary",
      requestType = audio_processor.AudioProcessor.AudioSample.class,
      responseType = audio_processor.AudioProcessor.AudioSummary.class,
      methodType = io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
  public static io.grpc.MethodDescriptor<audio_processor.AudioProcessor.AudioSample,
      audio_processor.AudioProcessor.AudioSummary> getComputeSummaryMethod() {
    io.grpc.MethodDescriptor<audio_processor.AudioProcessor.AudioSample, audio_processor.AudioProcessor.AudioSummary> getComputeSummaryMethod;
    if ((getComputeSummaryMethod = AudioAnalyzerGrpc.getComputeSummaryMethod) == null) {
      synchronized (AudioAnalyzerGrpc.class) {
        if ((getComputeSummaryMethod = AudioAnalyzerGrpc.getComputeSummaryMethod) == null) {
          AudioAnalyzerGrpc.getComputeSummaryMethod = getComputeSummaryMethod =
              io.grpc.MethodDescriptor.<audio_processor.AudioProcessor.AudioSample, audio_processor.AudioProcessor.AudioSummary>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ComputeSummary"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  audio_processor.AudioProcessor.AudioSample.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  audio_processor.AudioProcessor.AudioSummary.getDefaultInstance()))
              .setSchemaDescriptor(new AudioAnalyzerMethodDescriptorSupplier("ComputeSummary"))
              .build();
        }
      }
    }
    return getComputeSummaryMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AudioAnalyzerStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AudioAnalyzerStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AudioAnalyzerStub>() {
        @java.lang.Override
        public AudioAnalyzerStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AudioAnalyzerStub(channel, callOptions);
        }
      };
    return AudioAnalyzerStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AudioAnalyzerBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AudioAnalyzerBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AudioAnalyzerBlockingStub>() {
        @java.lang.Override
        public AudioAnalyzerBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AudioAnalyzerBlockingStub(channel, callOptions);
        }
      };
    return AudioAnalyzerBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AudioAnalyzerFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<AudioAnalyzerFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<AudioAnalyzerFutureStub>() {
        @java.lang.Override
        public AudioAnalyzerFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new AudioAnalyzerFutureStub(channel, callOptions);
        }
      };
    return AudioAnalyzerFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class AudioAnalyzerImplBase implements io.grpc.BindableService {

    /**
     */
    public io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.AudioSample> computeSpectrum(
        io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.Spectrum> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getComputeSpectrumMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.AudioSample> computeSummary(
        io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.AudioSummary> responseObserver) {
      return io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall(getComputeSummaryMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getComputeSpectrumMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                audio_processor.AudioProcessor.AudioSample,
                audio_processor.AudioProcessor.Spectrum>(
                  this, METHODID_COMPUTE_SPECTRUM)))
          .addMethod(
            getComputeSummaryMethod(),
            io.grpc.stub.ServerCalls.asyncBidiStreamingCall(
              new MethodHandlers<
                audio_processor.AudioProcessor.AudioSample,
                audio_processor.AudioProcessor.AudioSummary>(
                  this, METHODID_COMPUTE_SUMMARY)))
          .build();
    }
  }

  /**
   */
  public static final class AudioAnalyzerStub extends io.grpc.stub.AbstractAsyncStub<AudioAnalyzerStub> {
    private AudioAnalyzerStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AudioAnalyzerStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AudioAnalyzerStub(channel, callOptions);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.AudioSample> computeSpectrum(
        io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.Spectrum> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getComputeSpectrumMethod(), getCallOptions()), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.AudioSample> computeSummary(
        io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.AudioSummary> responseObserver) {
      return io.grpc.stub.ClientCalls.asyncBidiStreamingCall(
          getChannel().newCall(getComputeSummaryMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class AudioAnalyzerBlockingStub extends io.grpc.stub.AbstractBlockingStub<AudioAnalyzerBlockingStub> {
    private AudioAnalyzerBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AudioAnalyzerBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AudioAnalyzerBlockingStub(channel, callOptions);
    }
  }

  /**
   */
  public static final class AudioAnalyzerFutureStub extends io.grpc.stub.AbstractFutureStub<AudioAnalyzerFutureStub> {
    private AudioAnalyzerFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AudioAnalyzerFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new AudioAnalyzerFutureStub(channel, callOptions);
    }
  }

  private static final int METHODID_COMPUTE_SPECTRUM = 0;
  private static final int METHODID_COMPUTE_SUMMARY = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AudioAnalyzerImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AudioAnalyzerImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_COMPUTE_SPECTRUM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.computeSpectrum(
              (io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.Spectrum>) responseObserver);
        case METHODID_COMPUTE_SUMMARY:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.computeSummary(
              (io.grpc.stub.StreamObserver<audio_processor.AudioProcessor.AudioSummary>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class AudioAnalyzerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AudioAnalyzerBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return audio_processor.AudioProcessor.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AudioAnalyzer");
    }
  }

  private static final class AudioAnalyzerFileDescriptorSupplier
      extends AudioAnalyzerBaseDescriptorSupplier {
    AudioAnalyzerFileDescriptorSupplier() {}
  }

  private static final class AudioAnalyzerMethodDescriptorSupplier
      extends AudioAnalyzerBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AudioAnalyzerMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (AudioAnalyzerGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AudioAnalyzerFileDescriptorSupplier())
              .addMethod(getComputeSpectrumMethod())
              .addMethod(getComputeSummaryMethod())
              .build();
        }
      }
    }
    return result;
  }
}
