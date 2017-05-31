// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.scanner;

import android.view.MenuItem;
import android.view.MenuInflater;
import android.view.Menu;

import com.rdproj.vli.rdproject.R;
import com.thalmic.myo.Myo;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.content.Intent;
import android.bluetooth.BluetoothAdapter;
import com.thalmic.myo.Hub;
import android.os.Bundle;
import android.widget.ListView;
import android.app.Fragment;

public class ScanFragment extends Fragment implements Scanner.OnScanningStartedListener
{
    private static final int REQUEST_ENABLE_BT = 1;
    private Scanner mScanner;
    private ListView mListView;
    
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setHasOptionsMenu(true);
        this.mScanner = Hub.getInstance().getScanner();
        this.mScanner.addOnScanningStartedListener(this);
    }
    
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                final Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                this.startActivityForResult(enableBtIntent, 1);
            }
            else {
                this.mScanner.startScanning();
            }
        }
    }
    
    public void onDestroy() {
        super.onDestroy();
        this.mScanner.removeOnScanningStartedListener(this);
    }
    
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.myosdk__fragment_scan, container, false);
        this.mListView = (ListView)view.findViewById(R.id.myosdk__scan_result_view);
        final MyoDeviceListAdapter adapter = this.mScanner.getAdapter();
        this.mListView.setAdapter((ListAdapter)adapter);
        this.mListView.setOnItemClickListener((AdapterView.OnItemClickListener)new AdapterView.OnItemClickListener() {
            public void onItemClick(final AdapterView<?> parent, final View view, final int position, final long id) {
                if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
                    final Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
                    ScanFragment.this.startActivityForResult(enableBtIntent, 1);
                    return;
                }
                final Scanner.OnMyoClickedListener listener = ScanFragment.this.mScanner.getOnMyoClickedListener();
                if (listener != null) {
                    final Myo myo = adapter.getMyo(position);
                    listener.onMyoClicked(myo);
                }
            }
        });

        return view;
    }
    
    public void onDestroyView() {
        super.onDestroyView();
        this.mListView.setAdapter((ListAdapter)null);
    }
    
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.myosdk__fragment_scan, menu);
    }
    
    public void onPrepareOptionsMenu(final Menu menu) {
        super.onPrepareOptionsMenu(menu);
        final MenuItem scanItem = menu.findItem(R.id.myosdk__action_scan);
        scanItem.setTitle(this.mScanner.isScanning() ? R.string.myosdk__action_stop_scan : R.string.myosdk__action_scan);
    }
    
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        if (R.id.myosdk__action_scan == id) {
            this.toggleScanning();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == 1) {
            if (resultCode == -1) {
                this.mScanner.startScanning();
            }
            else {
                this.onBluetoothNotEnabled();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    public void onScanningStarted() {
        this.getActivity().invalidateOptionsMenu();
    }
    
    public void onScanningStopped() {
        this.getActivity().invalidateOptionsMenu();
    }
    
    protected void onBluetoothNotEnabled() {
        this.getActivity().finish();
    }
    
    private void toggleScanning() {
        if (!BluetoothAdapter.getDefaultAdapter().isEnabled()) {
            final Intent enableBtIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
            this.startActivityForResult(enableBtIntent, 1);
            return;
        }
        if (this.mScanner.isScanning()) {
            this.mScanner.stopScanning();
        }
        else {
            this.mScanner.startScanning();
        }
    }
}
