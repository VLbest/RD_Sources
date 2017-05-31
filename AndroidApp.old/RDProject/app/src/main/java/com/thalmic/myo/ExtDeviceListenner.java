package com.thalmic.myo;



import com.thalmic.myo.DeviceListener;
import com.thalmic.myo.Emg;
import com.thalmic.myo.Myo;

/**
 * Created by VLI on 10/04/2016.
 */
public interface ExtDeviceListenner extends DeviceListener {

    void onEMGData(Myo myo, long l, Emg emgData);

}
