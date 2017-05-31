package com.rdproj.vli.rdproject.myo;


import com.thalmic.myo.Myo;

interface MyoLockObserver {

    void onUnlock(Myo myo, long timestamp);
    void onLock(Myo myo, long timestamp);

}
