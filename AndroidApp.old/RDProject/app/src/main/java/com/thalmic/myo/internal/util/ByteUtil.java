// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo.internal.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class ByteUtil
{
    private static final char[] hexArray;
    
    public static String bytesToHex(final byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; ++j) {
            final int v = bytes[j] & 0xFF;
            hexChars[j * 2] = ByteUtil.hexArray[v >>> 4];
            hexChars[j * 2 + 1] = ByteUtil.hexArray[v & 0xF];
        }
        return new String(hexChars);
    }
    
    public static UUID getUuidFromBytes(final byte[] array, final int index) {
        final ByteBuffer buffer = ByteBuffer.wrap(array);
        final long msb = buffer.getLong(index);
        final long lsb = buffer.getLong(index + 8);
        return new UUID(msb, lsb);
    }
    
    public static String getString(final byte[] array, final int offset) {
        if (offset > array.length) {
            return null;
        }
        final byte[] strBytes = new byte[array.length - offset];
        for (int i = 0; i != array.length - offset; ++i) {
            strBytes[i] = array[offset + i];
        }
        return new String(strBytes);
    }
    
    static {
        hexArray = "0123456789ABCDEF".toCharArray();
    }
}
