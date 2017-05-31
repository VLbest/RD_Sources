// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import java.util.ArrayList;

class MultiListener implements DeviceListener
{
    private ArrayList<DeviceListener> mListeners;
    
    MultiListener() {
        this.mListeners = new ArrayList<DeviceListener>();
    }
    
    public void add(final DeviceListener listener) {
        this.mListeners.add(listener);
    }
    
    public void remove(final DeviceListener listener) {
        this.mListeners.remove(listener);
    }
    
    public boolean contains(final DeviceListener listener) {
        return this.mListeners.contains(listener);
    }
    
    public void clear() {
        this.mListeners.clear();
    }
    
    @Override
    public void onAttach(final Myo myo, final long timestamp) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onAttach(myo, timestamp);
        }
    }
    
    @Override
    public void onDetach(final Myo myo, final long timestamp) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onDetach(myo, timestamp);
        }
    }
    
    @Override
    public void onConnect(final Myo myo, final long timestamp) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onConnect(myo, timestamp);
        }
    }
    
    @Override
    public void onDisconnect(final Myo myo, final long timestamp) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onDisconnect(myo, timestamp);
        }
    }
    
    @Override
    public void onArmSync(final Myo myo, final long timestamp, final Arm arm, final XDirection xDirection) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onArmSync(myo, timestamp, arm, xDirection);
        }
    }
    
    @Override
    public void onArmUnsync(final Myo myo, final long timestamp) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onArmUnsync(myo, timestamp);
        }
    }
    
    @Override
    public void onUnlock(final Myo myo, final long timestamp) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onUnlock(myo, timestamp);
        }
    }
    
    @Override
    public void onLock(final Myo myo, final long timestamp) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onLock(myo, timestamp);
        }
    }
    
    @Override
    public void onPose(final Myo myo, final long timestamp, final Pose pose) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onPose(myo, timestamp, pose);
        }
    }
    
    @Override
    public void onOrientationData(final Myo myo, final long timestamp, final Quaternion rotation) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onOrientationData(myo, timestamp, rotation);
        }
    }
    
    @Override
    public void onAccelerometerData(final Myo myo, final long timestamp, final Vector3 accel) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onAccelerometerData(myo, timestamp, accel);
        }
    }
    
    @Override
    public void onGyroscopeData(final Myo myo, final long timestamp, final Vector3 gyro) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onGyroscopeData(myo, timestamp, gyro);
        }
    }

    @Override
    public void onEMGData(final Myo myo, final long timestamp, final Emg emg) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onEMGData(myo, timestamp, emg);
        }
    }
    
    @Override
    public void onRssi(final Myo myo, final long timestamp, final int rssi) {
        for (int i = 0; i < this.mListeners.size(); ++i) {
            this.mListeners.get(i).onRssi(myo, timestamp, rssi);
        }
    }
}
