// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import java.util.Iterator;
import com.thalmic.myo.internal.ble.Address;
import com.thalmic.myo.scanner.Scanner;

class ScanListener implements Scanner.OnScanningStartedListener, Scanner.OnMyoScannedListener
{
    private static final String TAG = "ScanListener";
    private static final int MINIMUM_ADJACENT_RSSI = -39;
    private static final long ADJACENT_SCAN_INTERVAL = 500L;
    private final Hub mHub;
    private AttachMode mAttachMode;
    private int mAttachCount;
    private Address mTargetAddress;
    
    public ScanListener(final Hub hub) {
        this.mAttachMode = AttachMode.NONE;
        this.mHub = hub;
    }
    
    private Scanner getScanner() {
        return this.mHub.getScanner();
    }
    
    public void attachToAdjacent(final int count) {
        if (count == 0) {
            throw new IllegalArgumentException("Attach count must be greater than 0");
        }
        this.mAttachMode = AttachMode.ADJACENT;
        this.mAttachCount = count;
        this.getScanner().startScanning(0L, 500L);
    }
    
    public void attachByMacAddress(final String targetAddress) {
        this.mAttachMode = AttachMode.ADDRESS;
        this.mAttachCount = 1;
        this.mTargetAddress = new Address(targetAddress);
        this.getScanner().startScanning(0L);
    }
    
    @Override
    public void onScanningStarted() {
        final Scanner.ScanListAdapter adapter = this.mHub.getScanner().getScanListAdapter();
        for (final Myo myo : this.mHub.getKnownDevices()) {
            switch (myo.getConnectionState()) {
                case CONNECTED:
                case CONNECTING: {
                    adapter.addDevice(myo, 0);
                    continue;
                }
            }
        }
    }
    
    @Override
    public void onScanningStopped() {
        this.mAttachMode = AttachMode.NONE;
    }
    
    @Override
    public Myo onMyoScanned(final Address address, final String name, final int rssi) {
        final Myo myo = this.mHub.addKnownDevice(address);
        myo.setName(name);
        if (this.mAttachMode != AttachMode.NONE && myo.getConnectionState() == Myo.ConnectionState.DISCONNECTED && this.shouldAttach(address, rssi)) {
            --this.mAttachCount;
            if (this.mAttachCount == 0 && this.getScanner().isScanning()) {
                this.getScanner().stopScanning();
            }
            this.mHub.connectToScannedMyo(address.toString());
        }
        return myo;
    }
    
    private boolean shouldAttach(final Address address, final int rssi) {
        switch (this.mAttachMode) {
            case ADJACENT: {
                return rssi >= -39;
            }
            case ADDRESS: {
                return address.equals(this.mTargetAddress);
            }
            default: {
                return false;
            }
        }
    }
    
    private enum AttachMode
    {
        NONE, 
        ADJACENT, 
        ADDRESS;
    }
}
