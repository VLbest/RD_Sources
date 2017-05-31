// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import java.util.UUID;
import android.os.Build;
import com.thalmic.myo.internal.ble.BleManager;

class MyoGatt
{
    private Hub mHub;
    private BleManager mBleManager;
    
    public MyoGatt(final Hub hub) {
        this.mHub = hub;
    }
    
    public void setBleManager(final BleManager bleManager) {
        this.mBleManager = bleManager;
    }
    
    public boolean connect(final String address) {
        return this.connect(address, false);
    }
    
    public boolean connect(final String address, final boolean autoConnect) {
        final boolean connecting = this.mBleManager.connect(address, autoConnect);
        if (connecting) {
            final Myo myo = this.mHub.getDevice(address);
            myo.setConnectionState(Myo.ConnectionState.CONNECTING);
        }
        return connecting;
    }
    
    public void disconnect(final String address) {
        if (Build.VERSION.SDK_INT >= 21) {
            this.configureDataAcquisition(address, ControlCommand.EmgMode.DISABLED, false, true);
        }
        this.mBleManager.disconnect(address);
        final Myo myo = this.mHub.getDevice(address);
        if (myo.getConnectionState() == Myo.ConnectionState.CONNECTING) {
            myo.setConnectionState(Myo.ConnectionState.DISCONNECTED);
            this.mHub.getScanner().getScanListAdapter().notifyDeviceChanged();
        }
        myo.setAttached(false);
    }
    
    public void configureDataAcquisition(final String address, final ControlCommand.EmgMode streamEmg, final boolean streamImu, final boolean enableClassifier) {
        final byte[] enableCommand = ControlCommand.createForSetMode(streamEmg, streamImu, enableClassifier);
        this.writeControlCommand(address, enableCommand);
    }
    
    public void requestRssi(final String address) {
        this.mBleManager.getBleGatt().readRemoteRssi(address);
    }
    
    public void vibrate(final String address, final Myo.VibrationType vibrationType) {
        final byte[] vibrateCommand = ControlCommand.createForVibrate(vibrationType);
        this.writeControlCommand(address, vibrateCommand);
    }
    
    public void unlock(final String address, final Myo.UnlockType unlockType) {
        final byte[] unlockCommand = ControlCommand.createForUnlock(unlockType);
        this.writeControlCommand(address, unlockCommand);
    }
    
    public void notifyUserAction(final String address) {
        final byte[] command = ControlCommand.createForUserAction();
        this.writeControlCommand(address, command);
    }
    
    private void writeControlCommand(final String address, final byte[] controlCommand) {
        final UUID serviceUuid = GattConstants.CONTROL_SERVICE_UUID;
        final UUID charUuid = GattConstants.COMMAND_CHAR_UUID;
        this.mBleManager.getBleGatt().writeCharacteristic(address, serviceUuid, charUuid, controlCommand);
    }
}
