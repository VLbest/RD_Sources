// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

public abstract class AbstractDeviceListener implements DeviceListener
{
    @Override
    public void onAttach(final Myo myo, final long timestamp) {
    }
    
    @Override
    public void onDetach(final Myo myo, final long timestamp) {
    }
    
    @Override
    public void onConnect(final Myo myo, final long timestamp) {
    }
    
    @Override
    public void onDisconnect(final Myo myo, final long timestamp) {
    }
    
    @Override
    public void onArmSync(final Myo myo, final long timestamp, final Arm arm, final XDirection xDirection) {
    }
    
    @Override
    public void onArmUnsync(final Myo myo, final long timestamp) {
    }
    
    @Override
    public void onUnlock(final Myo myo, final long timestamp) {
    }
    
    @Override
    public void onLock(final Myo myo, final long timestamp) {
    }
    
    @Override
    public void onPose(final Myo myo, final long timestamp, final Pose pose) {
    }
    
    @Override
    public void onOrientationData(final Myo myo, final long timestamp, final Quaternion rotation) {
    }
    
    @Override
    public void onAccelerometerData(final Myo myo, final long timestamp, final Vector3 accel) {
    }
    
    @Override
    public void onGyroscopeData(final Myo myo, final long timestamp, final Vector3 gyro) {
    }
    
    @Override
    public void onRssi(final Myo myo, final long timestamp, final int rssi) {
    }
}
