// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import android.os.Build;
import org.json.JSONException;
import org.json.JSONArray;
import java.io.IOException;
import java.util.concurrent.Executor;
import org.json.JSONObject;
import android.os.AsyncTask;
import android.util.Log;
import android.text.TextUtils;
import java.util.concurrent.Executors;
import com.thalmic.myo.internal.util.NetworkUtil;
import java.util.concurrent.ExecutorService;

class Reporter
{
    private static final String TAG = "Reporter";
    public static final String EVENT_NAME_ATTACHED_MYO = "AttachedMyo";
    public static final String EVENT_NAME_DETACHED_MYO = "DetachedMyo";
    public static final String EVENT_NAME_SYNCED_MYO = "SyncedMyo";
    public static final String EVENT_NAME_UNSYNCED_MYO = "UnsyncedMyo";
    private static final String EVENT_URL = "http://devices.thalmic.com/event";
    private static final String MYOSDK_PLATFORM;
    private static final String MYOSDK_VERSION = "0.10.0";
    private static final String REPORTING_MAC_ADDRESS_KEY = "macAddress";
    private static final String REPORTING_PLATFORM_KEY = "softwarePlatform";
    private static final String REPORTING_SDK_VERSION_KEY = "softwareVersion";
    private static final String REPORTING_APP_ID_KEY = "appId";
    private static final String REPORTING_UUID_KEY = "uuid";
    private static final String REPORTING_DATA_KEY = "data";
    private static final String REPORTING_EVENT_NAME_KEY = "eventName";
    private static final String REPORTING_TIMESTAMP_KEY = "timestamp";
    private static final String REPORTING_FIRMWARE_VERSION_KEY = "firmwareVersion";
    private ExecutorService mExecutor;
    private NetworkUtil mUtil;
    private boolean mSendUsageData;
    
    public Reporter() {
        this(new NetworkUtil());
    }
    
    public Reporter(final NetworkUtil util) {
        this.mExecutor = Executors.newSingleThreadExecutor();
        this.mSendUsageData = true;
        this.mUtil = util;
    }
    
    public void setSendUsageData(final boolean sendUsageData) {
        this.mSendUsageData = sendUsageData;
    }
    
    public boolean isSendingUsageData() {
        return this.mSendUsageData;
    }
    
    public void sendMyoEvent(final String appId, final String uuid, final String eventName, final Myo myo) {
        if (!this.mSendUsageData) {
            return;
        }
        if (myo == null || TextUtils.isEmpty((CharSequence)myo.getMacAddress())) {
            Log.e("Reporter", "Could not send Myo event. Invalid Myo.");
            return;
        }
        final long timestamp = System.currentTimeMillis() * 1000L;
        new AsyncTask<Void, Void, Boolean>() {
            protected Boolean doInBackground(final Void... params) {
                try {
                    final JSONObject jo = buildEventJsonObject(appId, uuid, eventName, myo, timestamp);
                    return Reporter.this.sendJsonPostRequest(jo.toString(), "http://devices.thalmic.com/event");
                }
                catch (Exception e) {
                    Log.e("Reporter", "Exception in sending event:" + e.toString());
                    return false;
                }
            }
        }.executeOnExecutor((Executor)this.mExecutor, new Void[0]);
    }
    
    private boolean sendJsonPostRequest(final String jsonString, final String urlString) throws IOException {
        final int responseCode = this.mUtil.postJsonToUrl(jsonString, urlString);
        final boolean success = responseCode == 200;
        if (!success) {
            Log.e("Reporter", "Unsuccessful sending post request to " + urlString + ". Received non-200 " + "(" + responseCode + ") response code from server.");
        }
        return success;
    }
    
    private static JSONObject buildEventJsonObject(final String appId, final String uuid, final String eventName, final Myo myo, final long timestamp) throws JSONException {
        final JSONObject jo = new JSONObject();
        jo.put("appId", (Object)appId);
        jo.put("softwarePlatform", (Object)Reporter.MYOSDK_PLATFORM);
        jo.put("softwareVersion", (Object)"0.10.0");
        jo.put("uuid", (Object)uuid);
        final JSONArray eventArray = new JSONArray();
        jo.put("data", (Object)eventArray);
        final JSONObject event = new JSONObject();
        event.put("eventName", (Object)eventName);
        event.put("timestamp", timestamp);
        eventArray.put((Object)event);
        final JSONObject eventData = new JSONObject();
        eventData.put("macAddress", (Object)macAddressForReporting(myo.getMacAddress()));
        eventData.put("firmwareVersion", (Object)firmwareVersionForReporting(myo.getFirmwareVersion()));
        event.put("data", (Object)eventData);
        return jo;
    }
    
    private static String macAddressForReporting(final String address) {
        return address.toLowerCase().replace(':', '-');
    }
    
    private static String firmwareVersionForReporting(final FirmwareVersion v) {
        return "" + v.major + "." + v.minor + "." + v.patch + "." + v.hardwareRev;
    }
    
    static {
        MYOSDK_PLATFORM = "Android_" + Build.VERSION.RELEASE;
    }
}
