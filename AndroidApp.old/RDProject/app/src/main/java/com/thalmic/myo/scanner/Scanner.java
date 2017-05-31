// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.scanner;

import com.thalmic.myo.Myo;
import com.thalmic.myo.internal.ble.Address;
import com.thalmic.myo.internal.util.ByteUtil;
import java.util.Iterator;
import android.util.Log;
import java.util.ArrayList;
import android.os.Handler;
import com.thalmic.myo.internal.ble.BleManager;
import java.util.UUID;

public class Scanner
{
    private static final String TAG = "Scanner";
    private static UUID sAdvertisedUuid;
    private static final long SCAN_PERIOD = 5000L;
    private BleManager mBleManager;
    private Handler mHandler;
    private boolean mScanning;
    private final Runnable mStopRunnable;
    private long mRestartInterval;
    private final Runnable mRestartRunnable;
    private ArrayList<OnScanningStartedListener> mScanningStartedListeners;
    private OnMyoScannedListener mMyoScannedListener;
    private OnMyoClickedListener mMyoClickedListener;
    private MyoDeviceListAdapter mListAdapter;
    private BleManager.BleScanCallback mBleScanCallback;
    
    public Scanner(final BleManager bleManager, final OnMyoScannedListener scannedListener, final OnMyoClickedListener clickedListener) {
        this.mStopRunnable = new Runnable() {
            @Override
            public void run() {
                Scanner.this.stopScanning();
            }
        };
        this.mRestartRunnable = new Runnable() {
            @Override
            public void run() {
                Scanner.this.restartScanning();
                if (Scanner.this.mRestartInterval > 0L) {
                    Scanner.this.mHandler.postDelayed(Scanner.this.mRestartRunnable, Scanner.this.mRestartInterval);
                }
            }
        };
        this.mScanningStartedListeners = new ArrayList<OnScanningStartedListener>();
        this.mListAdapter = new MyoDeviceListAdapter();
        this.mBleScanCallback = new ScanCallback();
        this.mBleManager = bleManager;
        this.mMyoScannedListener = scannedListener;
        this.mMyoClickedListener = clickedListener;
        this.mHandler = new Handler();
    }
    
    public void setBleManager(final BleManager bleManager) {
        this.mBleManager = bleManager;
    }
    
    public void startScanning() {
        this.startScanning(5000L);
    }
    
    public void startScanning(final long scanPeriod) {
        this.startScanning(scanPeriod, 0L);
    }
    
    public void startScanning(final long scanPeriod, final long restartInterval) {
        if (this.mScanning) {
            Log.w("Scanner", "Scan is already in progress. Ignoring call to startScanning.");
            return;
        }
        this.mHandler.removeCallbacks(this.mStopRunnable);
        if (scanPeriod > 0L) {
            this.mHandler.postDelayed(this.mStopRunnable, scanPeriod);
        }
        this.mHandler.removeCallbacks(this.mRestartRunnable);
        if (restartInterval > 0L) {
            this.mHandler.postDelayed(this.mRestartRunnable, restartInterval);
        }
        this.mRestartInterval = restartInterval;
        final boolean started = this.mBleManager.startBleScan(this.mBleScanCallback);
        if (started) {
            this.mScanning = true;
            this.mListAdapter.clear();
            for (final OnScanningStartedListener listener : this.mScanningStartedListeners) {
                listener.onScanningStarted();
            }
        }
    }
    
    public void stopScanning() {
        this.mScanning = false;
        this.mHandler.removeCallbacks(this.mStopRunnable);
        this.mHandler.removeCallbacks(this.mRestartRunnable);
        this.mBleManager.stopBleScan(this.mBleScanCallback);
        for (final OnScanningStartedListener listener : this.mScanningStartedListeners) {
            listener.onScanningStopped();
        }
    }
    
    private void restartScanning() {
        this.mBleManager.stopBleScan(this.mBleScanCallback);
        final boolean started = this.mBleManager.startBleScan(this.mBleScanCallback);
        if (!started) {
            for (final OnScanningStartedListener listener : this.mScanningStartedListeners) {
                listener.onScanningStopped();
            }
        }
    }
    
    public boolean isScanning() {
        return this.mScanning;
    }
    
    public void addOnScanningStartedListener(final OnScanningStartedListener listener) {
        this.mScanningStartedListeners.add(listener);
        if (this.mScanning) {
            listener.onScanningStarted();
        }
    }
    
    public void removeOnScanningStartedListener(final OnScanningStartedListener listener) {
        this.mScanningStartedListeners.remove(listener);
    }
    
    OnMyoClickedListener getOnMyoClickedListener() {
        return this.mMyoClickedListener;
    }
    
    public ScanListAdapter getScanListAdapter() {
        return this.mListAdapter;
    }
    
    MyoDeviceListAdapter getAdapter() {
        return this.mListAdapter;
    }
    
    static boolean isMyo(final UUID serviceUuid) {
        if (Scanner.sAdvertisedUuid == null) {
            final byte[] uuidBytes = getServiceInfoUuidBytes();
            Scanner.sAdvertisedUuid = ByteUtil.getUuidFromBytes(uuidBytes, 0);
        }
        return Scanner.sAdvertisedUuid.equals(serviceUuid);
    }
    
    private static native byte[] getServiceInfoUuidBytes();
    
    private static boolean isDalvikVm() {
        return "Dalvik".equals(System.getProperty("java.vm.name"));
    }
    
    static {
        try {
            System.loadLibrary("gesture-classifier");
        }
        catch (UnsatisfiedLinkError e) {
            if (isDalvikVm()) {
                throw e;
            }
        }
    }
    
    private class ScanCallback implements BleManager.BleScanCallback
    {
        @Override
        public void onBleScan(final Address address, final String name, final int rssi, final UUID serviceUuid) {
            Scanner.this.mHandler.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (Scanner.isMyo(serviceUuid)) {
                        final Myo myo = Scanner.this.mMyoScannedListener.onMyoScanned(address, name, rssi);
                        Scanner.this.mListAdapter.addDevice(myo, rssi);
                    }
                }
            });
        }
    }
    
    public interface ScanListAdapter
    {
        void addDevice(final Myo p0, final int p1);
        
        void notifyDeviceChanged();
    }
    
    public interface OnMyoClickedListener
    {
        void onMyoClicked(final Myo p0);
    }
    
    public interface OnMyoScannedListener
    {
        Myo onMyoScanned(final Address p0, final String p1, final int p2);
    }
    
    public interface OnScanningStartedListener
    {
        void onScanningStarted();
        
        void onScanningStopped();
    }
}
