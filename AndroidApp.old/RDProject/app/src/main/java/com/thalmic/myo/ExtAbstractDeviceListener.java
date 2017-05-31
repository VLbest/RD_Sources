package com.thalmic.myo;

/**
 * Created by VLI on 10/04/2016.
 */
public abstract class ExtAbstractDeviceListener implements ExtDeviceListenner {

    @Override
    public void onEMGData(Myo myo, long l, Emg emgData) {

    }

    @Override
    public void onAttach(Myo myo, long l) {

    }

    @Override
    public void onDetach(Myo myo, long l) {

    }

    @Override
    public void onConnect(Myo myo, long l) {

    }

    @Override
    public void onDisconnect(Myo myo, long l) {

    }

    @Override
    public void onArmSync(Myo myo, long l, Arm arm, XDirection xDirection) {

    }

    @Override
    public void onArmUnsync(Myo myo, long l) {

    }

    @Override
    public void onUnlock(Myo myo, long l) {

    }

    @Override
    public void onLock(Myo myo, long l) {

    }

    @Override
    public void onPose(Myo myo, long l, Pose pose) {

    }

    @Override
    public void onOrientationData(Myo myo, long l, Quaternion quaternion) {

    }

    @Override
    public void onAccelerometerData(Myo myo, long l, Vector3 vector3) {

    }

    @Override
    public void onGyroscopeData(Myo myo, long l, Vector3 vector3) {

    }

    @Override
    public void onRssi(Myo myo, long l, int i) {

    }
}
