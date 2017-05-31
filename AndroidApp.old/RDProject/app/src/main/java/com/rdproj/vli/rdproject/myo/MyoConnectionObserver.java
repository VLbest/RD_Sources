package com.rdproj.vli.rdproject.myo;


import com.thalmic.myo.Myo;

public interface MyoConnectionObserver {
    void onConnect(Myo myo, long timestamp);
    void onDisonnect(Myo myo, long timestamp);
}
