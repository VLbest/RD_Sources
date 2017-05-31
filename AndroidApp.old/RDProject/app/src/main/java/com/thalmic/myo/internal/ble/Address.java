// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.internal.ble;

import android.bluetooth.BluetoothAdapter;

public class Address
{
    private String mAddress;
    
    public Address(final String address) {
        if (!BluetoothAdapter.checkBluetoothAddress(address)) {
            throw new IllegalArgumentException("Cannot create Address for invalid address " + address);
        }
        this.mAddress = address;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Address address = (Address)o;
        return this.mAddress.equals(address.mAddress);
    }
    
    @Override
    public int hashCode() {
        return this.mAddress.hashCode();
    }
    
    @Override
    public String toString() {
        return this.mAddress;
    }
}
