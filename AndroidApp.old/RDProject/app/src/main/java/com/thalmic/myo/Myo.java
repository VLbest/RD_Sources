// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import com.thalmic.myo.internal.ble.Address;

public class Myo
{
    private final MyoGatt mMyoGatt;
    private String mName;
    private final String mAddress;
    private boolean mAttached;
    private ConnectionState mConnState;
    private FirmwareVersion mFirmwareVersion;
    private Pose mCurrentPose;
    private Arm mCurrentArm;
    private XDirection mCurrentXDirection;
    private boolean mUnlocked;
    private Pose mUnlockPose;
    
    Myo(final Hub hub, final Address address) {
        this.mConnState = ConnectionState.DISCONNECTED;
        this.mCurrentPose = Pose.UNKNOWN;
        this.mCurrentArm = Arm.UNKNOWN;
        this.mCurrentXDirection = XDirection.UNKNOWN;
        this.mUnlockPose = Pose.UNKNOWN;
        this.mMyoGatt = hub.getMyoGatt();
        this.mName = "";
        this.mAddress = address.toString();
    }
    
    public String getName() {
        return this.mName;
    }
    
    public String getMacAddress() {
        return this.mAddress;
    }
    
    public FirmwareVersion getFirmwareVersion() {
        return this.mFirmwareVersion;
    }
    
    public boolean isUnlocked() {
        return this.mUnlocked;
    }
    
    public Arm getArm() {
        return this.mCurrentArm;
    }
    
    public XDirection getXDirection() {
        return this.mCurrentXDirection;
    }
    
    public Pose getPose() {
        return this.mCurrentPose;
    }
    
    public void requestRssi() {
        this.mMyoGatt.requestRssi(this.mAddress);
    }
    
    public void vibrate(final VibrationType vibrationType) {
        this.mMyoGatt.vibrate(this.mAddress, vibrationType);
    }
    
    public void unlock(final UnlockType unlockType) {
        this.mMyoGatt.unlock(this.mAddress, unlockType);
    }
    
    public void lock() {
        this.mMyoGatt.unlock(this.mAddress, null);
    }
    
    public void notifyUserAction() {
        this.mMyoGatt.notifyUserAction(this.mAddress);
    }
    
    public boolean isFirmwareVersionSupported() {
        return this.mFirmwareVersion == null || (!this.mFirmwareVersion.isNotSet() && (this.mFirmwareVersion.major == 1 && this.mFirmwareVersion.minor >= 1));
    }
    
    public boolean isConnected() {
        return this.getConnectionState() == ConnectionState.CONNECTED;
    }
    
    public ConnectionState getConnectionState() {
        return this.mConnState;
    }
    
    void setAttached(final boolean attached) {
        this.mAttached = attached;
    }
    
    boolean isAttached() {
        return this.mAttached;
    }
    
    void setName(final String name) {
        this.mName = name;
    }
    
    void setConnectionState(final ConnectionState state) {
        this.mConnState = state;
    }
    
    void setCurrentPose(final Pose pose) {
        this.mCurrentPose = pose;
    }
    
    void setCurrentArm(final Arm arm) {
        this.mCurrentArm = arm;
    }
    
    void setCurrentXDirection(final XDirection xDirection) {
        this.mCurrentXDirection = xDirection;
    }
    
    void setUnlocked(final boolean unlocked) {
        this.mUnlocked = true;
    }
    
    Pose getUnlockPose() {
        return this.mUnlockPose;
    }
    
    void setUnlockPose(final Pose unlockPose) {
        this.mUnlockPose = unlockPose;
    }
    
    void setFirmwareVersion(final FirmwareVersion firmwareVersion) {
        this.mFirmwareVersion = firmwareVersion;
    }
    
    public enum VibrationType
    {
        SHORT, 
        MEDIUM, 
        LONG;
    }
    
    public enum UnlockType
    {
        TIMED, 
        HOLD;
    }
    
    public enum ConnectionState
    {
        CONNECTED, 
        CONNECTING, 
        DISCONNECTED;
    }
}
