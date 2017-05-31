// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;
import android.util.Log;
import java.util.UUID;

import com.thalmic.myo.scanner.Scanner;

class MyoUpdateParser implements GattCallback.UpdateParser
{
    private static final String TAG = "MyoUpdateParser";
    static final int IMU_EXPECTED_BYTE_LENGTH = 20;
    private static final double ORIENTATION_CONVERSION_CONSTANT = 16384.0;
    private static final double ACCELERATION_CONVERSION_CONSTANT = 2048.0;
    private static final double GYRO_CONVERSION_CONSTANT = 16.0;
    private Hub mHub;
    private DeviceListener mListener;
    private Scanner mScanner;
    private Reporter mReporter;
    
    MyoUpdateParser(final Hub hub, final DeviceListener listener) {
        this.mHub = hub;
        this.mListener = listener;
    }
    
    void setListener(final DeviceListener listener) {
        this.mListener = listener;
    }
    
    void setScanner(final Scanner scanner) {
        this.mScanner = scanner;
    }
    
    void setReporter(final Reporter reporter) {
        this.mReporter = reporter;
    }
    
    @Override
    public void onMyoConnected(final Myo myo) {
        final long now = this.mHub.now();
        if (!myo.isAttached()) {
            myo.setAttached(true);
            this.mListener.onAttach(myo, now);
            this.mReporter.sendMyoEvent(this.mHub.getApplicationIdentifier(), this.mHub.getInstallUuid(), "AttachedMyo", myo);
        }
        this.setMyoConnectionState(myo, Myo.ConnectionState.CONNECTED);
        this.mListener.onConnect(myo, now);
    }
    
    @Override
    public void onMyoDisconnected(final Myo myo) {
        final long now = this.mHub.now();
        this.setMyoConnectionState(myo, Myo.ConnectionState.DISCONNECTED);
        if (myo.getPose() != Pose.UNKNOWN) {
            myo.setCurrentPose(Pose.UNKNOWN);
            this.mListener.onPose(myo, now, myo.getPose());
        }
        if (myo.isUnlocked()) {
            myo.setUnlocked(false);
            this.mListener.onLock(myo, now);
        }
        if (myo.getArm() != Arm.UNKNOWN) {
            myo.setCurrentArm(Arm.UNKNOWN);
            myo.setCurrentXDirection(XDirection.UNKNOWN);
            this.mListener.onArmUnsync(myo, now);
        }
        this.mListener.onDisconnect(myo, now);
        if (myo.isAttached()) {
            this.mHub.getMyoGatt().connect(myo.getMacAddress(), true);
        }
        else {
            this.mListener.onDetach(myo, now);
            this.mReporter.sendMyoEvent(this.mHub.getApplicationIdentifier(), this.mHub.getInstallUuid(), "DetachedMyo", myo);
        }
    }
    
    @Override
    public void onCharacteristicChanged(final Myo myo, final UUID uuid, final byte[] value) {
        if (GattConstants.IMU_DATA_CHAR_UUID.equals(uuid)) {
            this.notifyMotionData(myo, value);
        }
        else if (GattConstants.EMG0_DATA_CHAR_UUID.equals(uuid)){
            this.notifyEMGData(myo, value);
        }
        else if (GattConstants.CLASSIFIER_EVENT_CHAR_UUID.equals(uuid)) {
            this.onClassifierEventData(myo, value);
        }
    }
    
    @Override
    public void onReadRemoteRssi(final Myo myo, final int rssi) {
        this.mListener.onRssi(myo, this.mHub.now(), rssi);
    }
    
    private void onClassifierEventData(final Myo myo, final byte[] classifierEventData) {
        try {
            final ClassifierEvent classifierEvent = new ClassifierEvent(classifierEventData);
            switch (classifierEvent.getType()) {
                case ARM_SYNCED: {
                    this.onArmSync(myo, classifierEvent);
                    break;
                }
                case ARM_UNSYNCED: {
                    this.onArmUnsync(myo);
                    break;
                }
                case POSE: {
                    this.onPose(myo, classifierEvent.getPose());
                    break;
                }
                case UNLOCKED: {
                    myo.setUnlocked(true);
                    this.mListener.onUnlock(myo, this.mHub.now());
                    break;
                }
                case LOCKED: {
                    myo.setUnlocked(false);
                    this.mListener.onLock(myo, this.mHub.now());
                    break;
                }
            }
        }
        catch (IllegalArgumentException e) {
            Log.e("MyoUpdateParser", "Received malformed classifier event.", (Throwable)e);
        }
    }
    
    private void onArmSync(final Myo myo, final ClassifierEvent classifierEvent) {
        final long now = this.mHub.now();
        myo.setCurrentArm(classifierEvent.getArm());
        myo.setCurrentXDirection(classifierEvent.getXDirection());
        this.mListener.onArmSync(myo, now, myo.getArm(), myo.getXDirection());
        this.mReporter.sendMyoEvent(this.mHub.getApplicationIdentifier(), this.mHub.getInstallUuid(), "SyncedMyo", myo);
    }
    
    private void onArmUnsync(final Myo myo) {
        final long now = this.mHub.now();
        myo.setCurrentArm(Arm.UNKNOWN);
        myo.setCurrentXDirection(XDirection.UNKNOWN);
        this.mListener.onArmUnsync(myo, now);
        this.mReporter.sendMyoEvent(this.mHub.getApplicationIdentifier(), this.mHub.getInstallUuid(), "UnsyncedMyo", myo);
    }
    
    private void onPose(final Myo myo, final Pose pose) {
        if (this.mHub.getLockingPolicy() == Hub.LockingPolicy.NONE || myo.isUnlocked()) {
            myo.setCurrentPose(pose);
            this.mListener.onPose(myo, this.mHub.now(), myo.getPose());
        }
        else if (this.mHub.getLockingPolicy() == Hub.LockingPolicy.STANDARD && pose == myo.getUnlockPose()) {
            myo.unlock(Myo.UnlockType.TIMED);
        }
    }
    
    private void notifyMotionData(final Myo myo, final byte[] imuData) {
        if (imuData.length >= 20) {
            final Quaternion rotation = readQuaternion(imuData);
            final Vector3 accel = readAcceleration(imuData);
            final Vector3 gyro = readGyroscope(imuData);
            final long time = this.mHub.now();
            this.mListener.onOrientationData(myo, time, rotation);
            this.mListener.onAccelerometerData(myo, time, accel);
            this.mListener.onGyroscopeData(myo, time, gyro);
        }
    }

    private void notifyEMGData(final Myo myo, final byte[] emgData) {
        if (emgData.length == 16) {
            ByteReader emg_br = new ByteReader();
            final long time = this.mHub.now();
            int[] EMGarray = new int[16];
            emg_br.setByteData(emgData);
            for(int emgInputIndex = 0;emgInputIndex<16;emgInputIndex++) {
                EMGarray[emgInputIndex] = emg_br.getByte();
            }
            Emg emg = new Emg(EMGarray);
            this.mListener.onEMGData(myo, time, emg);
        }
    }
    
    private void setMyoConnectionState(final Myo myo, final Myo.ConnectionState connectionState) {
        myo.setConnectionState(connectionState);
        if (this.mScanner != null) {
            this.mScanner.getScanListAdapter().notifyDeviceChanged();
        }
    }
    
    private static Quaternion readQuaternion(final byte[] imuData) {
        final double w = getShort(imuData, 0) / 16384.0;
        final double x = getShort(imuData, 2) / 16384.0;
        final double y = getShort(imuData, 4) / 16384.0;
        final double z = getShort(imuData, 6) / 16384.0;
        return new Quaternion(x, y, z, w);
    }
    
    private static Vector3 readAcceleration(final byte[] imuData) {
        final double x = getShort(imuData, 8) / 2048.0;
        final double y = getShort(imuData, 10) / 2048.0;
        final double z = getShort(imuData, 12) / 2048.0;
        return new Vector3(x, y, z);
    }
    
    private static Vector3 readGyroscope(final byte[] imuData) {
        final double x = getShort(imuData, 14) / 16.0;
        final double y = getShort(imuData, 16) / 16.0;
        final double z = getShort(imuData, 18) / 16.0;
        return new Vector3(x, y, z);
    }
    
    static short getShort(final byte[] array, final int offset) {
        final ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        return buffer.getShort(offset);
    }
}
