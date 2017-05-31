// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import com.thalmic.myo.internal.ble.Address;
import android.os.SystemClock;
import java.util.ArrayList;

import android.os.Build;
import com.thalmic.myo.internal.ble.BleGatt;
import android.content.SharedPreferences;

import java.util.UUID;
import android.text.TextUtils;
import android.util.Log;
import com.thalmic.myo.internal.ble.BleFactory;
import android.content.Context;
import java.util.HashMap;
import com.thalmic.myo.scanner.Scanner;
import android.os.Handler;
import com.thalmic.myo.internal.ble.BleManager;

public class Hub
{
    private static final String TAG = "Hub";
    private static final String PREF_INSTALL_UUID_KEY = "INSTALL_UUID";
    private static final String PREF_FILE_NAME = "com.thalmic.myosdk";
    private static final int MAX_APPLICATION_IDENTIFIER_LENGTH = 255;
    private String mApplicationIdentifier;
    private String mInstallUuid;
    private BleManager mBleManager;
    private Handler mHandler;
    private int mMyoAttachAllowance;
    private LockingPolicy mLockingPolicy;
    private Scanner mScanner;
    private final ScanListener mScanListener;
    private final HashMap<String, Myo> mKnownDevices;
    private final MultiListener mListeners;
    private final MyoUpdateParser mParser;
    private final GattCallback mGattCallback;
    private final MyoGatt mMyoGatt;
    private final Reporter mReporter;
    
    public static Hub getInstance() {
        return InstanceHolder.INSTANCE;
    }
    
    static Hub createInstanceForTests(final BleManager bleManager, final Handler handler, final Scanner scanner, final ScanListener scanListener, final MultiListener listeners, final MyoUpdateParser parser, final GattCallback gattCallback, final MyoGatt myoGatt) {
        return new Hub(bleManager, handler, scanner, scanListener, listeners, parser, gattCallback, myoGatt);
    }
    
    private Hub() {
        this.mKnownDevices = new HashMap<String, Myo>();
        this.mReporter = new Reporter();
        this.mListeners = new MultiListener();
        this.mParser = new MyoUpdateParser(this, this.mListeners);
        this.mGattCallback = new GattCallback(this);
        this.mMyoGatt = new MyoGatt(this);
        this.mScanListener = new ScanListener(this);
        this.mParser.setReporter(this.mReporter);
        this.mGattCallback.setUpdateParser(this.mParser);
        this.mGattCallback.setMyoGatt(this.mMyoGatt);
    }
    
    private Hub(final BleManager bleManager, final Handler handler, final Scanner scanner, final ScanListener scanListener, final MultiListener listeners, final MyoUpdateParser parser, final GattCallback gattCallback, final MyoGatt myoGatt) {
        this.mKnownDevices = new HashMap<String, Myo>();
        this.mReporter = new Reporter();
        this.mBleManager = bleManager;
        this.mHandler = handler;
        this.mScanner = scanner;
        this.mScanListener = scanListener;
        this.mListeners = listeners;
        this.mParser = parser;
        this.mGattCallback = gattCallback;
        this.mMyoGatt = myoGatt;
        this.setMyoAttachAllowance(1);
    }
    
    public boolean init(final Context context) {
        return this.init(context, "");
    }
    
    public boolean init(final Context context, final String applicationIdentifier) throws IllegalArgumentException {
        if (!this.isValidApplicationIdentifier(applicationIdentifier)) {
            throw new IllegalArgumentException("Invalid application identifier");
        }
        this.mApplicationIdentifier = applicationIdentifier;
        if (this.mBleManager == null) {
            this.mBleManager = BleFactory.createBleManager(context.getApplicationContext());
        }
        if (this.mBleManager == null) {
            Log.e("Hub", "Could not create BleManager");
            return false;
        }
        if (!this.mBleManager.isBluetoothSupported()) {
            Log.e("Hub", "Bluetooth not supported");
            return false;
        }
        if (this.mScanner == null) {
            this.setMyoAttachAllowance(1);
            this.setLockingPolicy(LockingPolicy.STANDARD);
            final SharedPreferences prefs = context.getSharedPreferences("com.thalmic.myosdk", 0);
            if (prefs != null) {
                this.mInstallUuid = prefs.getString("INSTALL_UUID", "");
                if (TextUtils.isEmpty((CharSequence)this.mInstallUuid)) {
                    this.mInstallUuid = UUID.randomUUID().toString();
                    prefs.edit().putString("INSTALL_UUID", this.mInstallUuid).apply();
                }
            }
            else {
                this.mInstallUuid = UUID.randomUUID().toString();
            }
            this.mHandler = new Handler();
            (this.mScanner = new Scanner(this.mBleManager, this.mScanListener, new ScanItemClickListener())).addOnScanningStartedListener(this.mScanListener);
            this.addListener(new ExtAbstractDeviceListener() {
                @Override
                public void onConnect(final Myo myo, final long timestamp) {
                    Hub.this.mScanner.getScanListAdapter().notifyDeviceChanged();
                }
                
                @Override
                public void onDisconnect(final Myo myo, final long timestamp) {
                    Hub.this.mScanner.getScanListAdapter().notifyDeviceChanged();
                }
            });
            this.mParser.setScanner(this.mScanner);
        }
        final BleGatt bleGatt = this.mBleManager.getBleGatt();
        bleGatt.setBleGattCallback(this.mGattCallback);
        this.mGattCallback.setBleGatt(bleGatt);
        this.mMyoGatt.setBleManager(this.mBleManager);
        this.mScanner.setBleManager(this.mBleManager);
        return true;
    }
    
    public void setSendUsageData(final boolean sendUsageData) {
        this.mReporter.setSendUsageData(sendUsageData);
    }
    
    public boolean isSendingUsageData() {
        return this.mReporter.isSendingUsageData();
    }
    
    public void shutdown() {
        if (Build.VERSION.SDK_INT >= 21) {
            for (final Myo myo : this.mKnownDevices.values()) {
                if (myo.isConnected()) {
                    this.mMyoGatt.configureDataAcquisition(myo.getMacAddress(), ControlCommand.EmgMode.DISABLED, false, true);
                }
            }
        }
        this.mBleManager.dispose();
        this.mBleManager = null;
        this.mListeners.clear();
        for (final Myo myo : this.mKnownDevices.values()) {
            myo.setAttached(false);
            myo.setConnectionState(Myo.ConnectionState.DISCONNECTED);
        }
    }
    
    String getApplicationIdentifier() {
        return this.mApplicationIdentifier;
    }
    
    String getInstallUuid() {
        return this.mInstallUuid;
    }
    
    MyoGatt getMyoGatt() {
        return this.mMyoGatt;
    }
    
    public Scanner getScanner() {
        return this.mScanner;
    }
    
    Myo getDevice(final String address) {
        return this.mKnownDevices.get(address);
    }
    
    public ArrayList<Myo> getConnectedDevices() {
        final ArrayList<Myo> connectedMyos = new ArrayList<Myo>();
        for (final Myo myo : this.mKnownDevices.values()) {
            if (myo.isConnected()) {
                connectedMyos.add(myo);
            }
        }
        return connectedMyos;
    }
    
    public void addListener(final DeviceListener listener) {
        if (this.mListeners.contains(listener)) {
            throw new IllegalArgumentException("Trying to add a listener that is already registered.");
        }
        this.mListeners.add(listener);
        this.mHandler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                final long timestamp = Hub.this.now();
                for (final Myo myo : Hub.this.mKnownDevices.values()) {
                    if (myo.isAttached()) {
                        listener.onAttach(myo, timestamp);
                        if (myo.getConnectionState() != Myo.ConnectionState.CONNECTED) {
                            continue;
                        }
                        listener.onConnect(myo, timestamp);
                    }
                }
            }
        });
    }
    
    public void removeListener(final DeviceListener listener) {
        this.mListeners.remove(listener);
    }
    
    public void attachToAdjacentMyo() {
        this.attachToAdjacentMyos(1);
    }
    
    public void attachToAdjacentMyos(final int count) {
        if (count < 1) {
            throw new IllegalArgumentException("The number of Myos to attach must be greater than 0.");
        }
        final int numAttachedDevices = this.getMyoAttachCount();
        if (numAttachedDevices + count > this.mMyoAttachAllowance) {
            Log.w("Hub", String.format("Myo attach allowance is set to %d. There are currently %d attached Myos. Ignoring attach request.", this.mMyoAttachAllowance, numAttachedDevices));
            return;
        }
        this.mScanListener.attachToAdjacent(count);
    }
    
    public void attachByMacAddress(final String macAddress) {
        final int numAttachedDevices = this.getMyoAttachCount();
        if (numAttachedDevices >= this.mMyoAttachAllowance) {
            Log.w("Hub", String.format("Myo attach allowance is set to %d. There are currently %dattached Myo. Ignoring attach request.", this.mMyoAttachAllowance, numAttachedDevices));
            return;
        }
        final Myo myo = this.getDevice(macAddress);
        if (myo != null && myo.isConnected()) {
            Log.w("Hub", "Already attached to the Myo at address=" + macAddress + ". Ignoring attach request.");
        }
        else {
            this.mScanListener.attachByMacAddress(macAddress);
        }
    }
    
    public void detach(final String macAddress) {
        final Myo myo = this.getDevice(macAddress);
        if (myo != null && myo.isAttached()) {
            this.mMyoGatt.disconnect(myo.getMacAddress());
        }
        else {
            Log.w("Hub", "No attached Myo at address=" + macAddress + ". Nothing to detach.");
        }
    }
    
    public long now() {
        return SystemClock.elapsedRealtime();
    }
    
    public void setLockingPolicy(final LockingPolicy lockingPolicy) {
        this.mLockingPolicy = lockingPolicy;
    }
    
    public LockingPolicy getLockingPolicy() {
        return this.mLockingPolicy;
    }
    
    public int getMyoAttachAllowance() {
        return this.mMyoAttachAllowance;
    }
    
    public void setMyoAttachAllowance(final int myoAttachAllowance) {
        this.mMyoAttachAllowance = myoAttachAllowance;
    }
    
    int getMyoAttachCount() {
        int count = 0;
        for (final Myo myo : this.mKnownDevices.values()) {
            if (myo.isAttached()) {
                ++count;
            }
        }
        return count;
    }
    
    boolean isInitialized() {
        return this.mBleManager != null;
    }
    
    void addGattValueListener(final GattCallback.ValueListener listener) {
        this.mGattCallback.addValueListener(listener);
    }
    
    Myo addKnownDevice(final Address address) {
        Myo myo = this.mKnownDevices.get(address.toString());
        if (myo == null) {
            myo = new Myo(this, address);
            this.mKnownDevices.put(myo.getMacAddress(), myo);
        }
        return myo;
    }
    
    ArrayList<Myo> getKnownDevices() {
        return new ArrayList<Myo>(this.mKnownDevices.values());
    }
    
    static boolean allowedToConnectToMyo(final Hub hub, final String address) {
        final Myo myo = hub.getDevice(address);
        if (myo != null && myo.isAttached()) {
            return true;
        }
        final int numAttachedDevices = hub.getMyoAttachCount();
        final int attachAllowance = hub.getMyoAttachAllowance();
        if (numAttachedDevices >= attachAllowance) {
            Log.w("Hub", String.format("Myo attach allowance is set to %d. There are currently %d attached Myos.", attachAllowance, numAttachedDevices));
            return false;
        }
        return true;
    }
    
    void connectToScannedMyo(final String address) {
        if (!allowedToConnectToMyo(this, address)) {
            return;
        }
        final boolean connecting = this.mMyoGatt.connect(address);
        if (connecting) {
            this.mScanner.getScanListAdapter().notifyDeviceChanged();
        }
    }
    
    void disconnectFromScannedMyo(final String address) {
        this.mMyoGatt.disconnect(address);
        this.mScanner.getScanListAdapter().notifyDeviceChanged();
    }
    
    private boolean isValidApplicationIdentifier(final String applicationIdentifier) {
        if (applicationIdentifier == null) {
            return false;
        }
        if (applicationIdentifier.isEmpty()) {
            return true;
        }
        if (applicationIdentifier.length() > 255) {
            return false;
        }
        char prevChar = '.';
        int fullStopCount = 0;
        for (int i = 0; i < applicationIdentifier.length(); ++i) {
            final char c = applicationIdentifier.charAt(i);
            if ((prevChar == '.' || i == applicationIdentifier.length() - 1) && (c == '-' || c == '_' || c == '.')) {
                return false;
            }
            if (c == '.') {
                if (prevChar == '-' || prevChar == '_' || prevChar == '.' || i < 2) {
                    return false;
                }
                ++fullStopCount;
            }
            if (fullStopCount == 0 && !Character.isLetter(c)) {
                return false;
            }
            if (!Character.isLetterOrDigit(c) && c != '-' && c != '_' && c != '.') {
                return false;
            }
            prevChar = c;
        }
        return fullStopCount >= 2;
    }
    
    public enum LockingPolicy
    {
        NONE, 
        STANDARD;
    }
    
    private static class InstanceHolder
    {
        private static final Hub INSTANCE;
        
        static {
            INSTANCE = new Hub();
        }
    }
    
    private class ScanItemClickListener implements Scanner.OnMyoClickedListener
    {
        @Override
        public void onMyoClicked(final Myo myo) {
            switch (myo.getConnectionState()) {
                case CONNECTED:
                case CONNECTING: {
                    Hub.this.disconnectFromScannedMyo(myo.getMacAddress());
                    break;
                }
                case DISCONNECTED: {
                    Hub.this.connectToScannedMyo(myo.getMacAddress());
                    break;
                }
            }
        }
    }
}
