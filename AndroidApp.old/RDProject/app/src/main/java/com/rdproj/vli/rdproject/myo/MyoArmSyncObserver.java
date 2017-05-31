package com.rdproj.vli.rdproject.myo;

import com.thalmic.myo.Arm;
import com.thalmic.myo.Myo;
import com.thalmic.myo.XDirection;

interface MyoArmSyncObserver {

    void onArmSync(Myo myo, long timestamp, Arm arm, XDirection xDirection);
    void onArmUnsync(Myo myo, long timestamp);

}
