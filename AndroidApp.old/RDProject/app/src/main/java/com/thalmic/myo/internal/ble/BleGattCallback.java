// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.internal.ble;

import java.util.UUID;

public abstract class BleGattCallback
{
    public void onDeviceConnectionFailed(final Address address) {
    }
    
    public void onDeviceConnected(final Address address) {
    }
    
    public void onDeviceDisconnected(final Address address) {
    }
    
    public void onServicesDiscovered(final Address address, final boolean success) {
    }
    
    public void onCharacteristicRead(final Address address, final UUID uuid, final byte[] value, final boolean success) {
    }
    
    public void onCharacteristicWrite(final Address address, final UUID uuid, final boolean success) {
    }
    
    public void onCharacteristicChanged(final Address address, final UUID uuid, final byte[] value) {
    }
    
    public void onReadRemoteRssi(final Address address, final int rssi, final boolean success) {
    }
}
