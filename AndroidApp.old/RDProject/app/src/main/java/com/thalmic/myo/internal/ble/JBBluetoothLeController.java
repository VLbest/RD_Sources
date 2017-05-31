//
// Decompiled by Procyon v0.5.30
//

package com.thalmic.myo.internal.ble;

import android.bluetooth.BluetoothGattService;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import android.bluetooth.BluetoothDevice;
import java.util.concurrent.Callable;
import java.util.Iterator;
import java.util.Set;
import java.util.Collection;
import java.util.HashSet;
import android.bluetooth.BluetoothManager;
import java.util.concurrent.Executors;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattCharacteristic;
import android.util.Log;
import android.bluetooth.BluetoothGattCallback;
import android.os.Handler;
import java.util.concurrent.ExecutorService;
import android.bluetooth.BluetoothGatt;
import java.util.HashMap;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import java.util.UUID;
import android.annotation.TargetApi;

@TargetApi(18)
class JBBluetoothLeController implements BleGatt
{
    private static final String TAG = "JBBluetoothLeController";
    private static final UUID CLIENT_CHARACTERISTIC_CONFIG;
    private Context mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private HashMap<String, BluetoothGatt> mGattConnections;
    private ExecutorService mOperationExecutor;
    private boolean mOperationPending;
    private Handler mCallbackHandler;
    private BleGattCallback mExternalCallback;
    private final BluetoothGattCallback mGattCallback;

    JBBluetoothLeController(final Context context) {
        this.mGattConnections = new HashMap<String, BluetoothGatt>();
        this.mGattCallback = new BluetoothGattCallback() {
            public void onConnectionStateChange(final BluetoothGatt gatt, final int status, final int newState) {
                JBBluetoothLeController.this.mCallbackHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final Address address = addressOf(gatt);
                        if (status != 0) {
                            Log.e("JBBluetoothLeController", "Received error status=" + status + " for onConnectionStateChange on address=" + address);
                            if (newState == 0) {
                                JBBluetoothLeController.this.mExternalCallback.onDeviceConnectionFailed(address);
                            }
                        }
                        else if (newState == 2) {
                            JBBluetoothLeController.this.mExternalCallback.onDeviceConnected(address);
                        }
                        else if (newState == 0) {
                            JBBluetoothLeController.this.mExternalCallback.onDeviceDisconnected(address);
                        }
                    }
                });
            }

            public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
                JBBluetoothLeController.this.mCallbackHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final Address address = addressOf(gatt);
                        final boolean success = status == 0;
                        if (!success) {
                            Log.e("JBBluetoothLeController", "Received error status=" + status + " for onServicesDiscovered on address=" + address);
                        }
                        JBBluetoothLeController.this.mExternalCallback.onServicesDiscovered(address, success);
                    }
                });
            }

            public void onCharacteristicChanged(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
                try {

                    JBBluetoothLeController.this.mCallbackHandler.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            JBBluetoothLeController.this.mExternalCallback.onCharacteristicChanged(addressOf(gatt), characteristic.getUuid(), characteristic.getValue());
                        }
                    });

                }catch (Exception e){
                    int i =5;
                }
            }


            public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
                this.onOperationFinished();
                JBBluetoothLeController.this.mCallbackHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final Address address = addressOf(gatt);
                        final boolean success = status == 0;
                        if (!success) {
                            Log.e("JBBluetoothLeController", "Received error status=" + status + " for onCharacteristicRead of " + characteristic.getUuid() + " on address=" + address);
                        }
                        JBBluetoothLeController.this.mExternalCallback.onCharacteristicRead(address, characteristic.getUuid(), characteristic.getValue(), success);
                    }
                });
            }

            public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
                this.onOperationFinished();
                JBBluetoothLeController.this.mCallbackHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final Address address = addressOf(gatt);
                        final boolean success = status == 0;
                        if (!success) {
                            Log.e("JBBluetoothLeController", "Received error status=" + status + " for onCharacteristicWrite of " + characteristic.getUuid() + " on address=" + address);
                        }
                        JBBluetoothLeController.this.mExternalCallback.onCharacteristicWrite(address, characteristic.getUuid(), success);
                    }
                });
            }

            public void onDescriptorRead(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
                this.onOperationFinished();
            }

            public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
                this.onOperationFinished();
            }

            public void onReadRemoteRssi(final BluetoothGatt gatt, final int rssi, final int status) {
                this.onOperationFinished();
                JBBluetoothLeController.this.mCallbackHandler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        final Address address = addressOf(gatt);
                        final boolean success = status == 0;
                        if (!success) {
                            Log.e("JBBluetoothLeController", "Received error status=" + status + " for onReadRemoteRssi on address=" + address);
                        }
                        JBBluetoothLeController.this.mExternalCallback.onReadRemoteRssi(address, rssi, success);
                    }
                });
            }

            private void onOperationFinished() {
                JBBluetoothLeController.this.mOperationPending = false;
            }

            private Address addressOf(final BluetoothGatt gatt) {
                return new Address(gatt.getDevice().getAddress());
            }
        };
        this.mContext = context;
        this.mOperationExecutor = Executors.newSingleThreadExecutor();
        this.mCallbackHandler = new Handler();
        final BluetoothManager bluetoothManager = (BluetoothManager)this.mContext.getSystemService("bluetooth");
        this.mBluetoothAdapter = bluetoothManager.getAdapter();
    }

    @Override
    public void setBleGattCallback(final BleGattCallback callback) {
        this.mExternalCallback = callback;
    }

    public void close() {
        this.submitTask(new Runnable() {
            @Override
            public void run() {
                final Set<String> keySet = new HashSet<String>(JBBluetoothLeController.this.mGattConnections.keySet());
                for (final String address : keySet) {
                    final BluetoothGatt bluetoothGatt = JBBluetoothLeController.this.mGattConnections.remove(address);
                    bluetoothGatt.close();
                }
            }
        });
        this.mOperationExecutor.shutdown();
    }

    public boolean connect(final String address, final boolean autoConnect) {
        if (this.mOperationExecutor.isShutdown()) {
            Log.w("JBBluetoothLeController", "Could not connect to address " + address + ". Executor shutdown.");
            return false;
        }
        final Future<Boolean> result = this.mOperationExecutor.submit((Callable<Boolean>)new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                if (JBBluetoothLeController.this.mBluetoothAdapter == null || address == null) {
                    Log.w("JBBluetoothLeController", "BluetoothAdapter not initialized or unspecified address.");
                    return false;
                }
                final BluetoothGatt existingGatt = JBBluetoothLeController.this.mGattConnections.get(address);
                BluetoothDevice device;
                if (existingGatt != null) {
                    existingGatt.close();
                    device = existingGatt.getDevice();
                }
                else {
                    device = JBBluetoothLeController.this.mBluetoothAdapter.getRemoteDevice(address);
                }
                final BluetoothGatt bluetoothGatt = device.connectGatt(JBBluetoothLeController.this.mContext, autoConnect, JBBluetoothLeController.this.mGattCallback);
                JBBluetoothLeController.this.mGattConnections.put(address, bluetoothGatt);
                return bluetoothGatt != null;
            }
        });
        try {
            return result.get();
        }
        catch (InterruptedException e) {
            Log.w("JBBluetoothLeController", "GATT connect interrupted for address: " + address, (Throwable)e);
            return false;
        }
        catch (ExecutionException e2) {
            Log.e("JBBluetoothLeController", "Problem during GATT connect for address: " + address, (Throwable)e2);
            return false;
        }
    }

    public void disconnect(final String address) {
        this.submitTask(new Runnable() {
            @Override
            public void run() {
                final BluetoothGatt bluetoothGatt = JBBluetoothLeController.this.mGattConnections.get(address);
                if (JBBluetoothLeController.this.mBluetoothAdapter == null || bluetoothGatt == null) {
                    Log.w("JBBluetoothLeController", "BluetoothAdapter not initialized");
                    return;
                }
                bluetoothGatt.disconnect();
            }
        });
    }

    @Override
    public void discoverServices(final String address) {
        this.submitTask(new Runnable() {
            @Override
            public void run() {
                final BluetoothGatt bluetoothGatt = JBBluetoothLeController.this.mGattConnections.get(address);
                bluetoothGatt.discoverServices();
            }
        });
    }

    @Override
    public void readCharacteristic(final String address, final UUID serviceUuid, final UUID charUuid) {
        this.submitTask(new Runnable() {
            @Override
            public void run() {
                final BluetoothGatt bluetoothGatt = JBBluetoothLeController.this.mGattConnections.get(address);
                final BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
                final BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
                JBBluetoothLeController.this.readCharacteristic(bluetoothGatt, characteristic);
            }
        });
    }

    @Override
    public void writeCharacteristic(final String address, final UUID serviceUuid, final UUID charUuid, final byte[] value) {
        this.submitTask(new Runnable() {
            @Override
            public void run() {
                final BluetoothGatt bluetoothGatt = JBBluetoothLeController.this.mGattConnections.get(address);
                final BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
                final BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
                JBBluetoothLeController.this.writeCharacteristic(bluetoothGatt, characteristic, value);
            }
        });
    }

    @Override
    public void setCharacteristicNotification(final String address, final UUID serviceUuid, final UUID charUuid, final boolean enable, final boolean indicate) {
        this.submitTask(new Runnable() {
            @Override
            public void run() {
                final BluetoothGatt bluetoothGatt = JBBluetoothLeController.this.mGattConnections.get(address);
                final BluetoothGattService service = bluetoothGatt.getService(serviceUuid);
                final BluetoothGattCharacteristic characteristic = service.getCharacteristic(charUuid);
                JBBluetoothLeController.this.setCharacteristicNotification(bluetoothGatt, characteristic, enable, indicate);
            }
        });
    }

    @Override
    public void readRemoteRssi(final String address) {
        this.submitTask(new Runnable() {
            @Override
            public void run() {
                final BluetoothGatt gatt = JBBluetoothLeController.this.mGattConnections.get(address);
                if (gatt.readRemoteRssi()) {
                    JBBluetoothLeController.this.mOperationPending = true;
                    JBBluetoothLeController.this.waitForOperationCompletion();
                }
                else {
                    Log.e("JBBluetoothLeController", "Failed reading remote rssi");
                }
            }
        });
    }

    private Future<?> submitTask(final Runnable task) {
        if (this.mOperationExecutor.isShutdown()) {
            Log.w("JBBluetoothLeController", "Could not submit task. Executor shutdown.");
            return null;
        }
        return this.mOperationExecutor.submit(task);
    }

    private void readCharacteristic(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic) {
        if (gatt.readCharacteristic(characteristic)) {
            this.mOperationPending = true;
            this.waitForOperationCompletion();
        }
        else {
            Log.e("JBBluetoothLeController", "Failed reading characteristic " + characteristic.getUuid());
        }
    }

    private void writeCharacteristic(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final byte[] value) {
        characteristic.setValue(value);
        if (gatt.writeCharacteristic(characteristic)) {
            this.mOperationPending = true;
            this.waitForOperationCompletion();
        }
        else {
            Log.e("JBBluetoothLeController", "Failed writing characteristic " + characteristic.getUuid());
        }
    }

    private void setCharacteristicNotification(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final boolean enable, final boolean indicate) {
        if (!gatt.setCharacteristicNotification(characteristic, enable)) {
            Log.e("JBBluetoothLeController", "Failed setting characteristic notification " + characteristic.getUuid());
            return;
        }
        final BluetoothGattDescriptor clientConfig = characteristic.getDescriptor(JBBluetoothLeController.CLIENT_CHARACTERISTIC_CONFIG);
        final byte[] value = enable ? (indicate ? BluetoothGattDescriptor.ENABLE_INDICATION_VALUE : BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE) : BluetoothGattDescriptor.DISABLE_NOTIFICATION_VALUE;
        clientConfig.setValue(value);
        if (gatt.writeDescriptor(clientConfig)) {
            this.mOperationPending = true;
            this.waitForOperationCompletion();
        }
        else {
            Log.e("JBBluetoothLeController", "Failed writing descriptor " + clientConfig.getUuid());
        }
    }

    private void waitForOperationCompletion() {
        final long timeout = 1000L;
        final long interval = 10L;
        long t;
        for (t = 1000L; t > 0L && this.mOperationPending; t -= 10L) {
            try {
                Thread.sleep(10L);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (t == 0L) {
            Log.w("JBBluetoothLeController", "Wait for operation completion timed out.");
        }
    }

    static {
        CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    }
}
