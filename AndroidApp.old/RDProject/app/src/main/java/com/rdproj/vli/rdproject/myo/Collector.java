package com.rdproj.vli.rdproject.myo;

import android.util.Log;

import com.thalmic.myo.Emg;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;

public class Collector implements MyoEMGObserver, MyoIMUObserver{

    private boolean recording;

    public Collector(){
        this.recording = false;
    }


    @Override
    public void onEMGData(Myo myo, long timestamp, Emg emg) {
        if (isRecording()){
            Log.d("MYOD", myo.getArm() + " EMG");
        }
    }

    @Override
    public void onOrientationData(Myo myo, long timestamp, Quaternion rotation) {
        if (isRecording()) {
            Log.d("MYOD", myo.getArm() + " ORI");
        }
    }

    @Override
    public void onAccelerometerData(Myo myo, long timestamp, Vector3 accel) {
        if (isRecording()) {
            Log.d("MYOD", myo.getArm() + " ACC");
        }
    }

    @Override
    public void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro) {
        if (isRecording()) {
            Log.d("MYOD", myo.getArm() + " GYR");
        }
    }

    public boolean isRecording() {
        return recording;
    }

    public void setRecording(boolean recording) {
        this.recording = recording;
    }
}
