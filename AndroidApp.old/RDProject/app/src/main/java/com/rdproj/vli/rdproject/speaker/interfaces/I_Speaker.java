package com.rdproj.vli.rdproject.speaker.interfaces;


import com.rdproj.vli.rdproject.speaker.SpeakerState;

import java.util.logging.Handler;

/**
 * Created by VLI on 11/05/2016.
 */
public interface I_Speaker {

    void say(String text);
    SpeakerState getState();
    void setTTSEngine(I_TTSEngine ttsEngine);
    void waitUntilAvailable(Handler handler);

}
