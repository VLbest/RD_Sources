// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.internal.ble;

import java.util.UUID;

public interface BleManager
{
    boolean isBluetoothSupported();
    
    BleGatt getBleGatt();
    
    boolean startBleScan(final BleScanCallback p0);
    
    void stopBleScan(final BleScanCallback p0);
    
    boolean connect(final String p0, final boolean p1);
    
    void disconnect(final String p0);
    
    void dispose();
    
    public interface BleScanCallback
    {
        void onBleScan(final Address p0, final String p1, final int p2, final UUID p3);
    }
}
