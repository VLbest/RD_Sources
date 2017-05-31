// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

import java.nio.ByteOrder;
import java.nio.ByteBuffer;

class FirmwareInfo
{
    static final int EXPECTED_BYTE_LENGTH = 8;
    public Pose unlockPose;
    
    FirmwareInfo(final byte[] array) {
        if (array.length < 8) {
            throw new IllegalArgumentException("Unexpected length=" + array.length + " of array. Expecting length of " + 8);
        }
        final ByteBuffer buffer = ByteBuffer.wrap(array);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        final int unlockPoseValue = buffer.getShort(6) & 0xFFFF;
        this.unlockPose = ClassifierEvent.poseFromClassifierPose(unlockPoseValue);
    }
    
    FirmwareInfo(final Pose pose) {
        if (pose == null) {
            throw new IllegalArgumentException("pose cannot be null");
        }
        this.unlockPose = pose;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final FirmwareInfo that = (FirmwareInfo)o;
        return this.unlockPose == that.unlockPose;
    }
    
    @Override
    public int hashCode() {
        return this.unlockPose.hashCode();
    }
}
