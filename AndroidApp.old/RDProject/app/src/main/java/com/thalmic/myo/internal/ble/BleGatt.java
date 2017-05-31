// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.internal.ble;

import java.util.UUID;

public interface BleGatt
{
    void setBleGattCallback(final BleGattCallback p0);
    
    void discoverServices(final String p0);
    
    void readCharacteristic(final String p0, final UUID p1, final UUID p2);
    
    void writeCharacteristic(final String p0, final UUID p1, final UUID p2, final byte[] p3);
    
    void setCharacteristicNotification(final String p0, final UUID p1, final UUID p2, final boolean p3, final boolean p4);
    
    void readRemoteRssi(final String p0);
}
