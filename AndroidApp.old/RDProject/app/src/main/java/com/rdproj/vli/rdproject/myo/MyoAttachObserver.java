package com.rdproj.vli.rdproject.myo;

import com.thalmic.myo.Myo;

public interface MyoAttachObserver{

    void onAttach(Myo myo, long timestamp);
    void onDetach(Myo myo, long timestamp);

}
