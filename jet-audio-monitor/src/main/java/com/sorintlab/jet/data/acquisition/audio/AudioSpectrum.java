package com.sorintlab.jet.data.acquisition.audio;

import java.io.Serializable;

public class AudioSpectrum implements Serializable {
 
    private static final long serialVersionUID = 2431246120211018952L;

    private int id;
    private long timestamp;
    private SpectrumComponent []components;  

    public AudioSpectrum(){

    }

    public AudioSpectrum(int id, long timestamp, SpectrumComponent[] components) {
        this.id = id;
        this.timestamp = timestamp;
        this.components = components;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public SpectrumComponent[] getComponents() {
        return components;
    }

    public void setComponents(SpectrumComponent[] components) {
        this.components = components;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id).append(" time=").append(timestamp);
        for(SpectrumComponent c: components){
            sb.append("\n\t").append(c.toString());
        }
        return sb.toString();
    }
    
}
