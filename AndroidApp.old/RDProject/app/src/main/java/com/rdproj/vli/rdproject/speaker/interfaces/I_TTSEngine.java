package com.rdproj.vli.rdproject.speaker.interfaces;

import android.speech.tts.TextToSpeech;

/**
 * Created by VLI on 11/05/2016.
 */
public interface I_TTSEngine extends TextToSpeech.OnInitListener {

    void speakWords(String text);

}
