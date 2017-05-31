// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.scanner;

import java.util.Comparator;
import java.util.List;
import java.util.Collections;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.TextView;

import android.view.ViewGroup;
import android.view.View;

import com.rdproj.vli.rdproject.R;
import com.thalmic.myo.Myo;
import java.util.ArrayList;
import android.widget.BaseAdapter;

class MyoDeviceListAdapter extends BaseAdapter implements Scanner.ScanListAdapter
{
    private ArrayList<Item> mItems;
    private RssiComparator mComparator;
    
    public MyoDeviceListAdapter() {
        this.mItems = new ArrayList<Item>();
        this.mComparator = new RssiComparator();
    }
    
    public void addDevice(final Myo myo, final int rssi) {
        if (myo == null) {
            throw new IllegalArgumentException("Myo cannot be null.");
        }
        Item item = this.getItem(myo);
        if (item != null) {
            item.rssi = rssi;
        }
        else {
            item = new Item(myo, rssi);
            this.mItems.add(item);
        }
        this.notifyDataSetChanged();
    }
    
    public Myo getMyo(final int position) {
        return this.mItems.get(position).myo;
    }
    
    public void clear() {
        this.mItems.clear();
        this.notifyDataSetChanged();
    }
    
    public void notifyDeviceChanged() {
        this.notifyDataSetChanged();
    }
    
    public void notifyDataSetChanged() {
        this.sortByRssi();
        super.notifyDataSetChanged();
    }
    
    public int getCount() {
        return this.mItems.size();
    }
    
    public Object getItem(final int i) {
        return this.mItems.get(i);
    }
    
    public long getItemId(final int i) {
        return i;
    }
    
    public View getView(final int i, View view, final ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();
        ViewHolder viewHolder;
        if (view == null) {

            view = View.inflate(context, R.layout.myosdk__device_list_item, (ViewGroup)null);
            viewHolder = new ViewHolder();
            viewHolder.deviceName = (TextView)view.findViewById(16908308);
            viewHolder.deviceVersion = (TextView)view.findViewById(16908309);
            viewHolder.requiredDeviceVersion = (TextView)view.findViewById(R.id.myosdk__required_firmware_version_text);
            viewHolder.progressBar = (ProgressBar)view.findViewById(R.id.myosdk__progress);
            viewHolder.connectionStateDot = view.findViewById(R.id.myosdk__connection_state_dot);
            view.setTag((Object)viewHolder);

        }
        else {
            viewHolder = (ViewHolder)view.getTag();
        }
        final Myo myo = this.mItems.get(i).myo;
        viewHolder.deviceVersion.setText((CharSequence)"");
        viewHolder.requiredDeviceVersion.setText((CharSequence)"");

        viewHolder.deviceVersion.setVisibility(8);
        viewHolder.requiredDeviceVersion.setVisibility(8);
        viewHolder.connectionStateDot.setVisibility(8);
        viewHolder.progressBar.setVisibility(8);

        if (myo.getConnectionState() == Myo.ConnectionState.DISCONNECTED) {
            if (!myo.isFirmwareVersionSupported()) {

                viewHolder.deviceVersion.setVisibility(0);
                viewHolder.requiredDeviceVersion.setVisibility(0);
                viewHolder.connectionStateDot.setVisibility(0);
                viewHolder.connectionStateDot.setBackgroundResource(R.drawable.myosdk__firmware_incompatible_dot);
                final String version = myo.getFirmwareVersion().toDisplayString();
                final String requiredVersion = "1.1.0";
                final String versionString = String.format(context.getString(R.string.myosdk__firmware_version_format), version);
                final String requiredVersionString = String.format(context.getString(R.string.myosdk__firmware_required_format), requiredVersion);
                viewHolder.deviceVersion.setText((CharSequence)versionString);
                viewHolder.requiredDeviceVersion.setText((CharSequence)requiredVersionString);

            }
        }
        else if (myo.getConnectionState() == Myo.ConnectionState.CONNECTING) {
            viewHolder.progressBar.setVisibility(0);
        }
        else if (myo.getConnectionState() == Myo.ConnectionState.CONNECTED) {

            viewHolder.deviceVersion.setVisibility(0);
            viewHolder.connectionStateDot.setVisibility(0);
            viewHolder.connectionStateDot.setBackgroundResource(R.drawable.myosdk__connected_dot);
            final String version = myo.getFirmwareVersion().toDisplayString();
            final String versionString2 = String.format(context.getString(R.string.myosdk__firmware_version_format), version);
            viewHolder.deviceVersion.setText((CharSequence)versionString2);

        }
        String deviceName = myo.getName();
        if (TextUtils.isEmpty((CharSequence)deviceName)) {
            deviceName = context.getString(R.string.myosdk__unknown_myo);
        }
        viewHolder.deviceName.setText((CharSequence)deviceName);
        return view;
    }
    
    private Item getItem(final Myo myo) {
        for (int i = 0, size = this.mItems.size(); i < size; ++i) {
            if (this.mItems.get(i).myo.equals(myo)) {
                return this.mItems.get(i);
            }
        }
        return null;
    }
    
    private void sortByRssi() {
        Collections.sort(this.mItems, this.mComparator);
    }
    
    private class Item
    {
        final Myo myo;
        int rssi;
        
        public Item(final Myo myo, final int rssi) {
            this.myo = myo;
            this.rssi = rssi;
        }
    }
    
    private static class ViewHolder
    {
        TextView deviceName;
        TextView deviceVersion;
        TextView requiredDeviceVersion;
        ProgressBar progressBar;
        View connectionStateDot;
    }
    
    private static class RssiComparator implements Comparator<Item>
    {
        @Override
        public int compare(final Item lhs, final Item rhs) {
            if (lhs.myo.equals(rhs.myo)) {
                return 0;
            }
            return rhs.rssi - lhs.rssi;
        }
        
        @Override
        public boolean equals(final Object object) {
            return super.equals(object);
        }
    }
}
