package com.xz.fucklepao;

public class SensorModel {
    public boolean alreadyThere = false;
    public int handle;
    public boolean isAlreadyNative = false;
    public float maxRange;
    public int minDelay;
    public String name;
    public String permission;
    public float resolution;
    public String stringType;

    public SensorModel(int sensorType, String name2, int handle2, float resolution2, int minDelay2, float maxRange2, String stringType2, String permission2) {
        this.name = name2;
        this.handle = handle2;
        this.resolution = resolution2;
        this.minDelay = minDelay2;
        this.maxRange = maxRange2;
        this.permission = permission2;
        this.stringType = stringType2;
    }
}