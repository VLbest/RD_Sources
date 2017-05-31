package com.thalmic.myo;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ByteReader {
    private byte[] byteData;
    private ByteBuffer bbf;

    public void setByteData(byte[] data){
        this.byteData = data;
        this.bbf = ByteBuffer.wrap(this.byteData);
        bbf.order(ByteOrder.LITTLE_ENDIAN);
    }

    public byte[] getByteData() {
        return byteData;
    }

    public short getShort() {
        return this.bbf.getShort();
    }

    public byte getByte(){
        return this.bbf.get();
    }

    public int getInt(){
        return this.bbf.getInt();
    }

    public String getByteDataString() {
        final StringBuilder stringBuilder = new StringBuilder(byteData.length);
        for (byte byteChar : byteData) {
            stringBuilder.append(String.format("%02X ", byteChar));
        }
        return stringBuilder.toString();
    }

    public String getIntDataString() {
        final StringBuilder stringBuilder = new StringBuilder(byteData.length);
        for (byte byteChar : byteData) {
            stringBuilder.append(String.format("%5d,", byteChar));
        }
        return stringBuilder.toString();
    }
}