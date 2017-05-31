//
// Decompiled by Procyon v0.5.30
//

package com.thalmic.myo.scanner;

import android.os.Bundle;
import android.app.Activity;
import android.support.v7.app.AppCompatActivity;

import com.rdproj.vli.rdproject.R;

public class ScanActivity extends AppCompatActivity
{
    protected void onCreate(final Bundle savedInstanceState) {

        final int width = this.getResources().getDimensionPixelSize(R.dimen.myosdk__fragment_scan_window_width);
        final int height = this.getResources().getDimensionPixelSize(R.dimen.myosdk__fragment_scan_window_height);

        if (width > 0 && height > 0) {
            this.getWindow().setLayout(width, height);
        }

        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.myosdk__activity_scan);
        //this.getActionBar().setDisplayOptions(0, 2);

    }
}
