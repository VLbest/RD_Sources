// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.internal.ble;

import android.bluetooth.BluetoothDevice;
import com.thalmic.myo.internal.util.ByteUtil;
import java.util.ArrayList;
import java.util.UUID;
import java.util.List;
import android.bluetooth.BluetoothManager;
import java.util.HashMap;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.annotation.TargetApi;

@TargetApi(18)
class JBBleManager implements BleManager
{
    private Context mContext;
    private BluetoothAdapter mAdapter;
    private JBBluetoothLeController mController;
    private HashMap<BleScanCallback, BluetoothAdapter.LeScanCallback> mCallbacks;
    
    JBBleManager(final Context context) {
        this.mCallbacks = new HashMap<BleScanCallback, BluetoothAdapter.LeScanCallback>();
        this.mContext = context.getApplicationContext();
        final BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService("bluetooth");
        this.mAdapter = bluetoothManager.getAdapter();
        this.mController = new JBBluetoothLeController(context);
    }
    
    @Override
    public boolean isBluetoothSupported() {
        return this.mContext.getPackageManager().hasSystemFeature("android.hardware.bluetooth_le") && this.mAdapter != null;
    }
    
    @Override
    public BleGatt getBleGatt() {
        return this.mController;
    }
    
    @Override
    public boolean startBleScan(final BleScanCallback callback) {
        BluetoothAdapter.LeScanCallback leScanCallback = this.mCallbacks.get(callback);
        if (leScanCallback == null) {
            leScanCallback = this.createCallback(callback);
            this.mCallbacks.put(callback, leScanCallback);
        }
        return this.mAdapter.startLeScan(leScanCallback);
    }
    
    @Override
    public void stopBleScan(final BleScanCallback callback) {
        final BluetoothAdapter.LeScanCallback leScanCallback = this.mCallbacks.remove(callback);
        this.mAdapter.stopLeScan(leScanCallback);
    }
    
    @Override
    public boolean connect(final String address, final boolean autoConnect) {
        return this.mController.connect(address, autoConnect);
    }
    
    @Override
    public void disconnect(final String address) {
        this.mController.disconnect(address);
    }
    
    @Override
    public void dispose() {
        this.mController.close();
    }
    
    private BluetoothAdapter.LeScanCallback createCallback(final BleScanCallback callback) {
        return (BluetoothAdapter.LeScanCallback)new LeScanCallback(callback);
    }
    
    static List<UUID> parseServiceUuids(final byte[] adv_data) {
        final List<UUID> uuids = new ArrayList<UUID>();
        int len;
        for (int offset = 0; offset < adv_data.length - 2; offset += len - 1) {
            len = adv_data[offset++];
            if (len == 0) {
                break;
            }
            final int type = adv_data[offset++];
            switch (type) {
                case 2:
                case 3: {
                    while (len > 1) {
                        int uuid16 = adv_data[offset++];
                        uuid16 += adv_data[offset++] << 8;
                        len -= 2;
                        uuids.add(UUID.fromString(String.format("%08x-0000-1000-8000-00805f9b34fb", uuid16)));
                    }
                    break;
                }
                case 6:
                case 7: {
                    while (len > 15) {
                        final UUID uuid17 = ByteUtil.getUuidFromBytes(adv_data, offset);
                        len -= 16;
                        offset += 16;
                        uuids.add(uuid17);
                    }
                    break;
                }
            }
        }
        return uuids;
    }
    
    static class LeScanCallback implements BluetoothAdapter.LeScanCallback
    {
        private BleScanCallback mCallback;
        
        LeScanCallback(final BleScanCallback callback) {
            this.mCallback = callback;
        }
        
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            final Address address = new Address(device.getAddress());
            final List<UUID> uuids = JBBleManager.parseServiceUuids(scanRecord);
            final UUID serviceUuid = uuids.isEmpty() ? null : uuids.get(0);
            this.mCallback.onBleScan(address, device.getName(), rssi, serviceUuid);
        }
    }
}
