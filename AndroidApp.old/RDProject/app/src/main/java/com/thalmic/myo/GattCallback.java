// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import android.util.Pair;
import android.util.Log;
import java.util.Iterator;
import com.thalmic.myo.internal.util.ByteUtil;
import java.util.HashSet;
import java.util.UUID;
import java.util.LinkedHashMap;
import com.thalmic.myo.internal.ble.Address;
import java.util.HashMap;
import com.thalmic.myo.internal.ble.BleGatt;
import com.thalmic.myo.internal.ble.BleGattCallback;

class GattCallback extends BleGattCallback
{
    private static final String TAG = "GattCallback";
    private Hub mHub;
    private BleGatt mBleGatt;
    private MyoGatt mMyoGatt;
    private UpdateParser mParser;
    private HashMap<Address, LinkedHashMap<UUID, InitReadChar>> mInitializingMyos;
    private HashSet<ValueListener> mListeners;
    
    public GattCallback(final Hub hub) {
        this.mInitializingMyos = new HashMap<Address, LinkedHashMap<UUID, InitReadChar>>();
        this.mListeners = new HashSet<ValueListener>();
        this.mHub = hub;
    }
    
    void setBleGatt(final BleGatt bleGatt) {
        this.mBleGatt = bleGatt;
    }
    
    void setMyoGatt(final MyoGatt myoGatt) {
        this.mMyoGatt = myoGatt;
    }
    
    void setUpdateParser(final UpdateParser updateParser) {
        this.mParser = updateParser;
    }
    
    void addValueListener(final ValueListener listener) {
        this.mListeners.add(listener);
    }
    
    void removeValueListener(final ValueListener listener) {
        this.mListeners.remove(listener);
    }
    
    @Override
    public void onDeviceConnectionFailed(final Address address) {
        this.onDeviceDisconnected(address);
    }
    
    @Override
    public void onDeviceConnected(final Address address) {
        if (!Hub.allowedToConnectToMyo(this.mHub, address.toString())) {
            this.onMyoInitializationFailed(address);
            return;
        }
        this.mInitializingMyos.put(address, new LinkedHashMap<UUID, InitReadChar>());
        this.mBleGatt.discoverServices(address.toString());
    }
    
    @Override
    public void onDeviceDisconnected(final Address address) {
        if (!this.mInitializingMyos.containsKey(address)) {
            final Myo myo = this.getMyoDevice(address);
            this.mParser.onMyoDisconnected(myo);
        }
        else {
            this.mInitializingMyos.remove(address);
        }
    }
    
    @Override
    public void onServicesDiscovered(final Address address, final boolean success) {
        if (!success) {
            this.onMyoInitializationFailed(address);
            return;
        }
        this.readNecessaryCharacteristics(address);
    }
    
    @Override
    public void onCharacteristicRead(final Address address, final UUID uuid, final byte[] value, final boolean success) {
        final Myo myo = this.getMyoDevice(address);
        if (!success) {
            if (myo.getConnectionState() == Myo.ConnectionState.CONNECTING) {
                this.onMyoInitializationFailed(address);
            }
            return;
        }
        if (myo.getConnectionState() == Myo.ConnectionState.CONNECTING && this.mInitializingMyos.get(address).remove(uuid) != null) {
            if (GattConstants.DEVICE_NAME_CHAR_UUID.equals(uuid)) {
                myo.setName(ByteUtil.getString(value, 0));
            }
            else if (GattConstants.FIRMWARE_VERSION_CHAR_UUID.equals(uuid)) {
                final boolean firmwareSupported = this.onFirmwareVersionRead(myo, value);
                if (!firmwareSupported) {
                    this.onMyoInitializationFailed(address);
                    return;
                }
            }
            else if (GattConstants.FIRMWARE_INFO_CHAR_UUID.equals(uuid)) {
                this.onFirmwareInfoRead(myo, value);
            }
            if (!this.readNextInitializationCharacteristic(address)) {
                this.mInitializingMyos.remove(address);
                this.onMyoInitializationSucceeded(address);
            }
        }
    }
    
    @Override
    public void onCharacteristicChanged(final Address address, final UUID uuid, final byte[] value) {
        final Myo myo = this.getMyoDevice(address);
        this.mParser.onCharacteristicChanged(myo, uuid, value);
        for (final ValueListener listener : this.mListeners) {
            listener.onCharacteristicChanged(myo, uuid, value);
        }
    }
    
    @Override
    public void onReadRemoteRssi(final Address address, final int rssi, final boolean success) {
        if (!success) {
            return;
        }
        final Myo myo = this.getMyoDevice(address);
        this.mParser.onReadRemoteRssi(myo, rssi);
    }
    
    private Myo getMyoDevice(final Address address) {
        Myo device = this.mHub.getDevice(address.toString());
        if (device == null) {
            device = this.mHub.addKnownDevice(address);
        }
        return device;
    }
    
    private void readNecessaryCharacteristics(final Address address) {
        this.mInitializingMyos.get(address).put(GattConstants.FIRMWARE_VERSION_CHAR_UUID, new InitReadChar(GattConstants.CONTROL_SERVICE_UUID, GattConstants.FIRMWARE_VERSION_CHAR_UUID));
        this.mInitializingMyos.get(address).put(GattConstants.FIRMWARE_INFO_CHAR_UUID, new InitReadChar(GattConstants.CONTROL_SERVICE_UUID, GattConstants.FIRMWARE_INFO_CHAR_UUID));
        this.mInitializingMyos.get(address).put(GattConstants.DEVICE_NAME_CHAR_UUID, new InitReadChar(GattConstants.GAP_SERVICE_UUID, GattConstants.DEVICE_NAME_CHAR_UUID));
        this.readNextInitializationCharacteristic(address);
    }
    
    private boolean readNextInitializationCharacteristic(final Address address) {
        final Iterator<InitReadChar> iterator = this.mInitializingMyos.get(address).values().iterator();
        if (!iterator.hasNext()) {
            return false;
        }
        final InitReadChar readChar = iterator.next();
        this.mBleGatt.readCharacteristic(address.toString(), readChar.getService(), readChar.getCharacteristic());
        return true;
    }
    
    boolean onFirmwareVersionRead(final Myo myo, final byte[] value) {
        FirmwareVersion fwVersion;
        try {
            fwVersion = new FirmwareVersion(value);
        }
        catch (IllegalArgumentException e) {
            Log.e("GattCallback", "Problem reading FirmwareVersion.", (Throwable)e);
            fwVersion = new FirmwareVersion();
        }
        myo.setFirmwareVersion(fwVersion);
        if (!myo.isFirmwareVersionSupported()) {
            final String format = "Myo (address=%s) firmware version (%s) is not supported. The SDK requires firmware version %d.x.x, minimum %d.%d.0.";
            Log.e("GattCallback", String.format(format, myo.getMacAddress(), fwVersion.toDisplayString(), 1, 1, 1));
            return false;
        }
        return true;
    }
    
    void onFirmwareInfoRead(final Myo myo, final byte[] value) {
        try {
            final FirmwareInfo fwInfo = new FirmwareInfo(value);
            myo.setUnlockPose(fwInfo.unlockPose);
        }
        catch (IllegalArgumentException e) {
            Log.e("GattCallback", "Problem reading FirmwareInfo.", (Throwable)e);
        }
    }
    
    private void onMyoInitializationSucceeded(final Address address) {
        final Myo myo = this.getMyoDevice(address);
        final String addressString = address.toString();
        this.mBleGatt.setCharacteristicNotification(addressString, GattConstants.EMG_SERVICE_UUID, GattConstants.EMG0_DATA_CHAR_UUID, true, false);
        this.mBleGatt.setCharacteristicNotification(addressString, GattConstants.FV_SERVICE_UUID, GattConstants.FV_DATA_CHAR_UUID, true, false);
        this.mBleGatt.setCharacteristicNotification(addressString, GattConstants.IMU_SERVICE_UUID, GattConstants.IMU_DATA_CHAR_UUID, true, false);
        this.mBleGatt.setCharacteristicNotification(addressString, GattConstants.CLASSIFIER_SERVICE_UUID, GattConstants.CLASSIFIER_EVENT_CHAR_UUID, true, true);
        this.mMyoGatt.configureDataAcquisition(addressString, ControlCommand.EmgMode.EMG, true, true);
        this.mParser.onMyoConnected(myo);
    }
    
    private void onMyoInitializationFailed(final Address address) {
        Log.e("GattCallback", "Failure in initialization of Myo. Disconnecting from Myo with address=" + address);
        this.mMyoGatt.disconnect(address.toString());
    }
    
    private static class InitReadChar extends Pair<UUID, UUID>
    {
        public InitReadChar(final UUID serviceUuid, final UUID characteristicUuid) {
            super(serviceUuid, characteristicUuid);
        }
        
        public UUID getService() {
            return (UUID)this.first;
        }
        
        public UUID getCharacteristic() {
            return (UUID)this.second;
        }
    }
    
    interface UpdateParser extends ValueListener
    {
        void onMyoConnected(final Myo p0);
        
        void onMyoDisconnected(final Myo p0);
        
        void onReadRemoteRssi(final Myo p0, final int p1);
    }
    
    interface ValueListener
    {
        void onCharacteristicChanged(final Myo p0, final UUID p1, final byte[] p2);
    }
}
