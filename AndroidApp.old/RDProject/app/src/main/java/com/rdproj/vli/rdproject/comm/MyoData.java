package com.rdproj.vli.rdproject.comm;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

@Parcel
public class MyoData {

    @SerializedName("AccX")
    public double accX;
    @SerializedName("AccY")
    public double accY;
    @SerializedName("AccZ")
    public double accZ;

    @SerializedName("GyroX")
    public double gyroX;
    @SerializedName("GyroY")
    public double gyroY;
    @SerializedName("GyroZ")
    public double gyroZ;

    @SerializedName("OriX")
    public double oriX;
    @SerializedName("OriY")
    public double oriY;
    @SerializedName("OriZ")
    public double oriZ;
    @SerializedName("OriW")
    public double oriW;


    @SerializedName("Emg1")
    public int emg1;
    @SerializedName("Emg2")
    public int emg2;
    @SerializedName("Emg3")
    public int emg3;
    @SerializedName("Emg4")
    public int emg4;
    @SerializedName("Emg5")
    public int emg5;
    @SerializedName("Emg6")
    public int emg6;
    @SerializedName("Emg7")
    public int emg7;
    @SerializedName("Emg8")
    public int emg8;

}
