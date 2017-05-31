package com.rdproj.vli.rdproject.myo;

import android.util.Log;

import com.rdproj.vli.rdproject.speaker.Speaker;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Arm;
import com.thalmic.myo.Emg;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Quaternion;
import com.thalmic.myo.Vector3;
import com.thalmic.myo.XDirection;

import java.util.LinkedList;

public class MyoListenner extends AbstractDeviceListener{

    private LinkedList<MyoConnectionObserver> connectionObservers;
    private LinkedList<MyoAttachObserver> attachObservers;
    private LinkedList<MyoIMUObserver> imuObservers;
    private LinkedList<MyoEMGObserver> emgObservers;
    private LinkedList<MyoLockObserver> lockObservers;
    private LinkedList<MyoArmSyncObserver> armSyncObservers;
    private MyoBaseManager myoBaseManager;

    public MyoListenner(MyoBaseManager myoBaseManager){
        connectionObservers = new LinkedList<>();
        attachObservers = new LinkedList<>();
        imuObservers = new LinkedList<>();
        emgObservers = new LinkedList<>();
        lockObservers = new LinkedList<>();
        armSyncObservers = new LinkedList<>();
        this.myoBaseManager = myoBaseManager;
    }


    public void subscribeToConnectionEvent(MyoConnectionObserver observer, boolean status){
        if (status) connectionObservers.add(observer);
        else connectionObservers.remove(observer);
    }

    public void subscribeToAttachEvent(MyoAttachObserver observer, boolean status){
        if (status) attachObservers.add(observer);
        else attachObservers.remove(observer);
    }

    public void subscribeToIMUEvent(MyoIMUObserver observer, boolean status){
        if (status) imuObservers.add(observer);
        else imuObservers.remove(observer);
    }

    public void subscribeToEMGEvent(MyoEMGObserver observer, boolean status){
        if (status) emgObservers.add(observer);
        else emgObservers.remove(observer);
    }

    public void subscribeToLockEvent(MyoLockObserver observer, boolean status){
        if (status) lockObservers.add(observer);
        else lockObservers.remove(observer);
    }

    public void subscribeToArmSyncEvent(MyoArmSyncObserver observer, boolean status){
        if (status) armSyncObservers.add(observer);
        else armSyncObservers.remove(observer);
    }

    @Override
    public void onEMGData(Myo myo, long timestamp, Emg emg) {
        for ( MyoEMGObserver observer: emgObservers) {
            observer.onEMGData(myo, timestamp, emg);
        }
    }

    @Override
    public void onOrientationData(final Myo myo, final long timestamp, final Quaternion rotation) {
        for ( MyoIMUObserver observer: imuObservers) {
            observer.onOrientationData(myo, timestamp, rotation);
        }
    }

    @Override
    public void onAccelerometerData(final Myo myo, final long timestamp, final Vector3 accel) {
        for ( MyoIMUObserver observer: imuObservers) {
            observer.onAccelerometerData(myo, timestamp, accel);
        }
    }

    @Override
    public void onGyroscopeData(final Myo myo, final long timestamp, final Vector3 gyro) {
        for ( MyoIMUObserver observer: imuObservers) {
            observer.onGyroscopeData(myo, timestamp, gyro);
        }
    }

    @Override
    public void onAttach(final Myo myo, final long timestamp) {
        Speaker.getInstance().say(myo.getName() + " attached");
        for ( MyoAttachObserver observer: attachObservers) {
            observer.onAttach(myo, timestamp);
        }
    }

    @Override
    public void onDetach(final Myo myo, final long timestamp) {
        Speaker.getInstance().say(myo.getName() + " detached");
        for ( MyoAttachObserver observer: attachObservers) {
            observer.onDetach(myo, timestamp);
        }
    }

    @Override
    public void onConnect(final Myo myo, final long timestamp) {
        myo.vibrate(Myo.VibrationType.MEDIUM);
        Speaker.getInstance().say(myo.getName() + " connected");
        this.myoBaseManager.getKnownMyos().add(myo);
        for (MyoConnectionObserver observer:connectionObservers) {
            observer.onConnect(myo, timestamp);
        }
    }

    @Override
    public void onDisconnect(final Myo myo, final long timestamp) {
        myo.vibrate(Myo.VibrationType.MEDIUM);
        Speaker.getInstance().say(myo.getName() + " disconnected");
        this.myoBaseManager.getKnownMyos().remove(myo);
        for (MyoConnectionObserver observer:connectionObservers) {
            observer.onDisonnect(myo, timestamp);
        }
    }

    @Override
    public void onArmSync(final Myo myo, final long timestamp, final Arm arm, final XDirection xDirection) {
        for ( MyoArmSyncObserver observer: armSyncObservers) {
            observer.onArmSync(myo, timestamp, arm, xDirection);
        }
    }

    @Override
    public void onArmUnsync(final Myo myo, final long timestamp) {
        for ( MyoArmSyncObserver observer: armSyncObservers) {
            observer.onArmUnsync(myo, timestamp);
        }
    }

    @Override
    public void onUnlock(final Myo myo, final long timestamp) {
        for ( MyoLockObserver observer: lockObservers) {
            observer.onUnlock(myo, timestamp);
        }
    }

    @Override
    public void onLock(final Myo myo, final long timestamp) {
        for ( MyoLockObserver observer: lockObservers) {
            observer.onLock(myo, timestamp);
        }
    }

}
