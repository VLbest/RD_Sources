package com.rdproj.vli.rdproject.myo;


import com.thalmic.myo.Emg;
import com.thalmic.myo.Myo;

interface MyoEMGObserver {

    void onEMGData(Myo p0, long p1, Emg p2);

}
