package com.rdproj.vli.rdproject.myo;


import com.thalmic.myo.Myo;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;

interface MyoIMUObserver {

    void onOrientationData(Myo myo, long timestamp, Quaternion rotation);
    void onAccelerometerData(Myo myo, long timestamp, Vector3 accel);
    void onGyroscopeData(Myo myo, long timestamp, Vector3 gyro);

}
