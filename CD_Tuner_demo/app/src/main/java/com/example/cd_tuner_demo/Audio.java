package com.example.cd_tuner_demo;

public class Audio{
    private final String broadcast;
    private final String frequency;

    public Audio(String broadcast,String frequency){
        this.broadcast = broadcast;
        this.frequency = frequency;
    }

    public String getBroadcast(){
        return broadcast;
    }
    public String getFrequency(){
        return frequency;
    }
}
