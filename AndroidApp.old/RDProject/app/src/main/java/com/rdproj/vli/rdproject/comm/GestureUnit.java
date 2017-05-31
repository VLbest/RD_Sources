package com.rdproj.vli.rdproject.comm;


import com.google.gson.annotations.SerializedName;
import org.parceler.Parcel;

@Parcel
public class GestureUnit {

    @SerializedName("Gesture")
    public String gestureName;
    @SerializedName("Timestamp")
    public long timestamp;
    @SerializedName("SampleNumber")
    public int sampleNumber;
    @SerializedName("Left")
    public MyoData leftMyo;
    @SerializedName("Right")
    public MyoData rightMyo;

}
