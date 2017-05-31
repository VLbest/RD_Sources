package com.rdproj.vli.rdproject.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdproj.vli.rdproject.MainActivity;
import com.rdproj.vli.rdproject.R;
import com.rdproj.vli.rdproject.myo.MyoAttachObserver;
import com.rdproj.vli.rdproject.myo.MyoBaseManager;
import com.rdproj.vli.rdproject.myo.MyoConnectionObserver;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.scanner.ScanActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyoStateFragment extends Fragment{

    @BindView(R.id.txt_right_myo_name) TextView txtRightMyoName;
    @BindView(R.id.start_connection_proc) ImageView startConnectionProc;
    @BindView(R.id.start_disconnect_proc) ImageView startDisConnectionProc;
    @BindView(R.id.txt_devices) TextView txtDevices;
    private MyoBaseManager myoBaseManager;
    Handler mHandler = new Handler();
    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                updateStatus();
            } finally {
                mHandler.postDelayed(mStatusChecker, 500);
            }
        }
    };

    public MyoStateFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_myo_state, container, false);
        ButterKnife.bind(this, view);
        this.myoBaseManager = ((MainActivity)getActivity()).getMyoBaseManager();
        return view;
    }

    @OnClick(R.id.start_connection_proc)
    public void startSearchingProcedure(){
        myoBaseManager.Start();
        mStatusChecker.run();
        updateStatus();
    }

    @OnClick(R.id.start_disconnect_proc)
    public void freeHub(){
        myoBaseManager.End();
        updateStatus();
    }


    public void updateStatus() {

        String info ="";
        txtDevices.setText("");
        ArrayList<Myo> devices = Hub.getInstance().getConnectedDevices();
        info += devices.size()+" Myos";
        for (Myo device:devices) {
            info += "\n"+device.getName() + " : " + device.getArm() + " & " + device.getConnectionState();
        }
        txtDevices.setText(info);
        if(!Hub.getInstance().getScanner().isScanning()){
            startConnectionProc.setBackground(getResources().getDrawable(R.drawable.fingerprint_scan));
        }else {
            startConnectionProc.setBackground(getResources().getDrawable(R.drawable.stop_scan));
        }

    }



}
