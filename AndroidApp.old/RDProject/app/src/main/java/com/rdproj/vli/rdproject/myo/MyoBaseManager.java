package com.rdproj.vli.rdproject.myo;


import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import com.thalmic.myo.scanner.ScanActivity;

import java.util.LinkedList;
import java.util.List;

public class MyoBaseManager {

    private String packageName;
    private Context context;
    private List<Myo> knownMyos;
    private Hub hub;
    private MyoListenner myoListenner;
    private boolean inialized = false;

    public MyoBaseManager(String packageName, Context context){
        this.packageName = packageName;
        this.context = context;
        this.knownMyos = new LinkedList<>();
        this.myoListenner = new MyoListenner(this);
    }

    public void initHub(){
        if (!inialized){
            try{
                this.hub = Hub.getInstance();
                if(!hub.init(context, this.packageName)){
                    Toast.makeText(context, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
                }else {
                    hub.setLockingPolicy(Hub.LockingPolicy.NONE);
                    hub.addListener(this.myoListenner);
                }
                this.inialized = true;
            }catch (Exception ex){
                Toast.makeText(context, "Error during hub initialization", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void Start(){
        this.initHub();
        onScanActionSelected();
        //this.StartMyoSearching();
    }

    public void End(){
        this.hub.getScanner().stopScanning();
        for (Myo myo : knownMyos){
            this.hub.detach(myo.getMacAddress());
        }
        this.hub.removeListener(myoListenner);
        this.hub.getConnectedDevices().clear();
        this.knownMyos.clear();
        //this.hub.shutdown();
    }

    private void StartMyoSearching(){
        this.hub.setMyoAttachAllowance(2);
        if(knownMyos.size() < 2) {
            this.hub.attachToAdjacentMyos(2);
        }
    }

    public MyoListenner getMyoListenner(){
        return myoListenner;
    }

    public List<Myo> getKnownMyos(){
        return knownMyos;
    }

    public void stopScan() {
        this.hub.getScanner().stopScanning();
    }

    private void onScanActionSelected() {
        Intent intent = new Intent(this.context, ScanActivity.class);
        this.context.startActivity(intent);
    }

}
