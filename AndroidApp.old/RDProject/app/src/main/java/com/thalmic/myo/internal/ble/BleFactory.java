// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.internal.ble;

import android.util.Log;
import android.content.Context;

import com.thalmic.myo.BuildConfig;

public abstract class BleFactory
{
    private static final String TAG = "BleFactory";

    public static BleManager createBleManager(Context context) {
        if (!BuildConfig.BLE_STACK.equals("ss1")) {
            return new JBBleManager(context);
        }
        try {
            return (BleManager) Class.forName("com.thalmic.myo.internal.ble.SS1BleManager").newInstance();
        } catch (Exception e) {
            Log.e(TAG, "Failed creating SS1BleManager", e);
            return null;
        }
    }
}
