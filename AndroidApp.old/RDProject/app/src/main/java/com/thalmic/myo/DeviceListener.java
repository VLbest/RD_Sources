// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

public interface DeviceListener
{
    void onAttach(final Myo p0, final long p1);
    
    void onDetach(final Myo p0, final long p1);
    
    void onConnect(final Myo p0, final long p1);
    
    void onDisconnect(final Myo p0, final long p1);
    
    void onArmSync(final Myo p0, final long p1, final Arm p2, final XDirection p3);
    
    void onArmUnsync(final Myo p0, final long p1);
    
    void onUnlock(final Myo p0, final long p1);
    
    void onLock(final Myo p0, final long p1);
    
    void onPose(final Myo p0, final long p1, final Pose p2);
    
    void onOrientationData(final Myo p0, final long p1, final Quaternion p2);
    
    void onAccelerometerData(final Myo p0, final long p1, final Vector3 p2);
    
    void onGyroscopeData(final Myo p0, final long p1, final Vector3 p2);

    void onEMGData(final Myo p0, final long p1, final Emg p2);
    
    void onRssi(final Myo p0, final long p1, final int p2);
}
