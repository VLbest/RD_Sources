// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;

public class FirmwareVersion
{
    public static final int REQUIRED_FIRMWARE_VERSION_MAJOR = 1;
    public static final int MINIMUM_FIRMWARE_VERSION_MINOR = 1;
    static final int EXPECTED_BYTE_LENGTH = 8;
    public final int major;
    public final int minor;
    public final int patch;
    public final int hardwareRev;
    
    FirmwareVersion(final byte[] array) {
        if (array.length < 8) {
            throw new IllegalArgumentException("Unexpected length=" + array.length + " of array. Expecting length of " + 8);
        }
        final ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.major = buffer.getShort();
        this.minor = buffer.getShort();
        this.patch = buffer.getShort();
        this.hardwareRev = buffer.getShort();
    }
    
    FirmwareVersion(final int major, final int minor, final int patch, final int hardwareRev) {
        this.major = major;
        this.minor = minor;
        this.patch = patch;
        this.hardwareRev = hardwareRev;
    }
    
    FirmwareVersion() {
        this.major = 0;
        this.minor = 0;
        this.patch = 0;
        this.hardwareRev = 0;
    }
    
    public boolean isNotSet() {
        return this.major == 0 && this.minor == 0 && this.patch == 0 && this.hardwareRev == 0;
    }
    
    public String toDisplayString() {
        return "" + this.major + "." + this.minor + "." + this.patch;
    }
    
    @Override
    public String toString() {
        return "{ major : " + this.major + ", " + "minor : " + this.minor + ", " + "patch : " + this.patch + ", " + "hardwareRev : " + this.hardwareRev + " }";
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FirmwareVersion that = (FirmwareVersion)o;
        return this.hardwareRev == that.hardwareRev && this.major == that.major && this.minor == that.minor && this.patch == that.patch;
    }
    
    @Override
    public int hashCode() {
        int result = this.major;
        result = 31 * result + this.minor;
        result = 31 * result + this.patch;
        result = 31 * result + this.hardwareRev;
        return result;
    }
}
