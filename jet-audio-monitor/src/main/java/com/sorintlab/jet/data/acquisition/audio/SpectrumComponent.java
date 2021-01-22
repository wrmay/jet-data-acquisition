package com.sorintlab.jet.data.acquisition.audio;

import java.io.Serializable;

public class SpectrumComponent implements Serializable{
    private static final long serialVersionUID = 2683074280207482651L;
    private short frequency;
    private short amplitude;

    public SpectrumComponent(){

    }

    public SpectrumComponent(short frequency, short amplitude) {
        this.frequency = frequency;
        this.amplitude = amplitude;
    }

    public short getFrequency() {
        return frequency;
    }

    public void setFrequency(short frequency) {
        this.frequency = frequency;
    }

    public short getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(short amplitude) {
        this.amplitude = amplitude;
    }

    public String toString(){
        return "" + frequency + "Hz: " + amplitude;
    }
}
