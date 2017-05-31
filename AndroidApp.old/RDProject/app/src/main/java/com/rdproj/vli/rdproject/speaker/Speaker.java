package com.rdproj.vli.rdproject.speaker;

import com.rdproj.vli.rdproject.speaker.interfaces.I_Speaker;
import com.rdproj.vli.rdproject.speaker.interfaces.I_TTSEngine;

import java.util.logging.Handler;

public class Speaker implements I_Speaker {

    // states based stuff. again. what's wrong with me?
    private SpeakerState myState;
    private I_TTSEngine ttsEngine;

    private Speaker(){
        this.myState = SpeakerState.BUSY;
    }

    public static I_Speaker getInstance() {  return InstanceHolder.INSTANCE;  }

    @Override
    public void say(String text) {
        this.myState = SpeakerState.BUSY;

        //TODO: Probably i need something more secure than just '!= null'
        if (this.ttsEngine != null) this.ttsEngine.speakWords(text);

        this.myState = SpeakerState.AVAILABLE;
    }

    @Override
    public SpeakerState getState() { return this.myState; }

    @Override
    public void setTTSEngine(I_TTSEngine ttsEngine) { this.ttsEngine = ttsEngine; }

    @Override
    public void waitUntilAvailable(Handler handler) {

    }

    private static class InstanceHolder{
        private static final I_Speaker INSTANCE;
        static {
            INSTANCE = new Speaker();
        }
    }

}
