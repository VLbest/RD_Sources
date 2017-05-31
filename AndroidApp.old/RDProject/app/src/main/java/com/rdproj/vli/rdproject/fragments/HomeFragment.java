package com.rdproj.vli.rdproject.fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.rdproj.vli.rdproject.MainActivity;
import com.rdproj.vli.rdproject.R;
import com.rdproj.vli.rdproject.comm.RestClient;
import com.rdproj.vli.rdproject.comm.WordPOJO;
import com.rdproj.vli.rdproject.myo.Collector;
import com.rdproj.vli.rdproject.myo.MyoBaseManager;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    @BindView(R.id.img_add_new_word) ImageView imgAddNewWord;
    @BindView(R.id.img_start_recording) ImageView btnStartRecording;
    @BindView(R.id.known_words_list) Spinner spinner;
    private RestClient restClient;
    private MyoBaseManager myoBaseManager;
    private Collector dataCollector;

    public HomeFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);
        restClient = new RestClient();
        getApiStatus();
        fillSpinner();
        this.myoBaseManager = ((MainActivity)getActivity()).getMyoBaseManager();
        this.dataCollector = new Collector();
        myoBaseManager.getMyoListenner().subscribeToEMGEvent(dataCollector, true);
        myoBaseManager.getMyoListenner().subscribeToIMUEvent(dataCollector, true);
        return view;
    }

    private void fillSpinner(){
        final Handler handler = new Handler();
        restClient.getKnownWords(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                final List<WordPOJO> wordPOJOs = new LinkedList<WordPOJO>();
                for (Map.Entry<String, String> entry : response.body().entrySet()){
                    wordPOJOs.add(new WordPOJO(entry.getKey(), entry.getValue()));
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), R.layout.spinner_item);
                        for (WordPOJO wordPOJO: wordPOJOs){
                            adapter.add(wordPOJO.word);
                        }
                        spinner.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.d("d", "d");
            }
        });
    }

    public void getApiStatus(){
        final Handler handler = new Handler();
        restClient.getApiStatus(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Log.d("API", "OK");
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("API", "FAIL");
            }
        });
    }

    @OnClick(R.id.img_add_new_word)
    public void addNewWord(){
        show_popup();
    }

    @OnClick(R.id.img_start_recording)
    public void startRecording(){
        if(!checkRequirements()) return;
        if (!dataCollector.isRecording()){
            dataCollector.setRecording(true);
            //Send it a word
        }else {
            dataCollector.setRecording(false);
        }
        changeOnAirImage(dataCollector.isRecording());
    }

    private boolean checkRequirements() {
        if (Hub.getInstance().getConnectedDevices().size() != 2) {
            Toast.makeText(getContext(), "< 2 armbands are connected", Toast.LENGTH_SHORT).show();
            return false;
        }
        for (Myo myo: Hub.getInstance().getConnectedDevices()){
            if (myo.getArm() == Arm.UNKNOWN){
                Toast.makeText(getContext(), "some armband is not synchronized", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (!myo.isConnected()){
                Toast.makeText(getContext(), "some armband is not properly connected", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private void changeOnAirImage(boolean b) {
        if (b){
            btnStartRecording.setBackground(getResources().getDrawable(R.drawable.rec));
        }else {
            btnStartRecording.setBackground(getResources().getDrawable(R.drawable.start_rec));
        }
    }

    private void sendToServer(WordPOJO word_holder) {
        RestClient restClient = new RestClient();
        restClient.addToWordlist(word_holder);
    }

    private void show_popup() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("New word");
        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String word_holder = input.getText().toString();
                WordPOJO wordObj = new WordPOJO();
                wordObj.word = word_holder;
                sendToServer(wordObj);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
