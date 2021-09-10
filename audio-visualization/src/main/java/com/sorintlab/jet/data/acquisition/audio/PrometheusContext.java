package com.sorintlab.jet.data.acquisition.audio;

import audio_processor.AudioProcessor;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;

import java.io.IOException;

public  class PrometheusContext {
    Gauge frequencyGauge;
    Gauge amplitudeGauge;
    Gauge volumeGauge;
    HTTPServer httpServer;

    public PrometheusContext() {
        frequencyGauge = Gauge.build().name("frequency").help("frequency of an audio component").labelNames("ordinal", "source").register();
        amplitudeGauge = Gauge.build().name("amplitude").help("amplitude of an audio component").labelNames("ordinal", "source").register();
        volumeGauge = Gauge.build().name("volume").help("RMS volume").labelNames("source").register();
        try {
            httpServer = new HTTPServer(7070);
        } catch (IOException iox) {
            throw new RuntimeException("Could not initialize Prometheus exporter");
        }
    }

    public void close() {
        System.out.println("Shutting down Prometheus");
        CollectorRegistry.defaultRegistry.unregister(frequencyGauge);
        CollectorRegistry.defaultRegistry.unregister(amplitudeGauge);
        CollectorRegistry.defaultRegistry.unregister(volumeGauge);
        httpServer.stop();
    }

    public void logAudioSummary(AudioProcessor.AudioSummary summary) {
        volumeGauge.labels(Integer.valueOf(summary.getId()).toString()).set(summary.getRmsVolume());
        int i = 0;
        for (AudioProcessor.SpectrumComponent component : summary.getComponentsList()) {
            i++;
            frequencyGauge.labels(Integer.valueOf(i).toString(), Integer.valueOf(summary.getId()).toString()).set(component.getFrequency());
            amplitudeGauge.labels(Integer.valueOf(i).toString(), Integer.valueOf(summary.getId()).toString()).set(component.getAmplitude());
        }
    }

}
