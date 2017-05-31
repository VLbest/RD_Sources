package com.rdproj.vli.rdproject;

import android.annotation.SuppressLint;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import com.rdproj.vli.rdproject.fragments.HomeFragment;
import com.rdproj.vli.rdproject.fragments.MyoStateFragment;
import com.rdproj.vli.rdproject.myo.MyoBaseManager;
import com.rdproj.vli.rdproject.speaker.Speaker;
import com.rdproj.vli.rdproject.speaker.interfaces.I_TTSEngine;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, I_TTSEngine {

    private static final int UI_ANIMATION_DELAY = 0;
    private final Handler mHideHandler = new Handler();
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };

    private MyoBaseManager myoBaseManager;
    private TextToSpeech tts;

    HomeFragment homeFragment;
    MyoStateFragment myoStateFragment;
    @BindView(R.id.fullscreen_content) View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        createHomeFragment(savedInstanceState);
        createMyoStateFragment(savedInstanceState);

        tts = new TextToSpeech(this, this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //hideUI();
    }

    private void hideUI() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void createMyoStateFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.myo_state_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            Bundle bundle = new Bundle();
            myoStateFragment = new MyoStateFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.myo_state_fragment_container, myoStateFragment).commit();
        }
    }

    private void createHomeFragment(Bundle savedInstanceState) {
        if (findViewById(R.id.home_fragment_container) != null) {
            if (savedInstanceState != null) {
                return;
            }
            homeFragment = new HomeFragment();
            homeFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.home_fragment_container, homeFragment).commit();
        }
    }

    public MyoBaseManager getMyoBaseManager() {
        if (myoBaseManager == null){
            myoBaseManager = new MyoBaseManager(this.getPackageName(), this);
        }
        return myoBaseManager;
    }

    @Override
    public void onInit(int status) {
        Log.v("TTS", "oninit");
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.v("TTS", "Language is not available.");
            } else {
                Log.v("TTS", "language is available");
                Speaker.getInstance().setTTSEngine(this);
            }
        } else {
            Log.v("TTS", "Could not initialize TextToSpeech.");
        }
    }

    @Override
    public void speakWords(String speech) {
        //tts.speak(speech, TextToSpeech.QUEUE_ADD, null);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.shutdown();
        }
        super.onDestroy();
    }
}
