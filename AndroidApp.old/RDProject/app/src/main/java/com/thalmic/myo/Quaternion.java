// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

public class Quaternion
{
    private double mX;
    private double mY;
    private double mZ;
    private double mW;
    
    public Quaternion() {
        this.set(0.0, 0.0, 0.0, 1.0);
    }
    
    public Quaternion(final Quaternion other) {
        this.set(other);
    }
    
    public Quaternion(final double x, final double y, final double z, final double w) {
        this.set(x, y, z, w);
    }
    
    public void set(final Quaternion other) {
        this.mX = other.mX;
        this.mY = other.mY;
        this.mZ = other.mZ;
        this.mW = other.mW;
    }
    
    private void set(final double x, final double y, final double z, final double w) {
        this.mX = x;
        this.mY = y;
        this.mZ = z;
        this.mW = w;
    }
    
    public double x() {
        return this.mX;
    }
    
    public double y() {
        return this.mY;
    }
    
    public double z() {
        return this.mZ;
    }
    
    public double w() {
        return this.mW;
    }
    
    public double length() {
        return Math.sqrt(this.mW * this.mW + this.mX * this.mX + this.mY * this.mY + this.mZ * this.mZ);
    }
    
    public void multiply(final Quaternion other) {
        final double x = this.mW * other.mX + this.mX * other.mW - this.mY * other.mZ + this.mZ * other.mY;
        final double y = this.mW * other.mY + this.mX * other.mZ + this.mY * other.mW - this.mZ * other.mX;
        final double z = this.mW * other.mZ - this.mX * other.mY + this.mY * other.mX + this.mZ * other.mW;
        final double w = this.mW * other.mW - this.mX * other.mX - this.mY * other.mY - this.mZ * other.mZ;
        this.set(x, y, z, w);
    }
    
    public void inverse() {
        final double d = this.mW * this.mW + this.mX * this.mX + this.mY * this.mY + this.mZ * this.mZ;
        this.set(-this.mX / d, -this.mY / d, -this.mZ / d, this.mW / d);
    }
    
    public Quaternion normalized() {
        final double scale = 1.0 / this.length();
        return new Quaternion(this.mX * scale, this.mY * scale, this.mZ * scale, this.mW * scale);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Quaternion that = (Quaternion)o;
        return Double.compare(that.mX, this.mX) == 0 && Double.compare(that.mY, this.mY) == 0 && Double.compare(that.mZ, this.mZ) == 0 && Double.compare(that.mW, this.mW) == 0;
    }
    
    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(this.mX);
        int result = (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.mY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.mZ);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.mW);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }
    
    @Override
    public String toString() {
        return "Quaternion{mX=" + this.mX + ", mY=" + this.mY + ", mZ=" + this.mZ + ", mW=" + this.mW + '}';
    }
    
    public static double roll(final Quaternion quat) {
        return Math.atan2(2.0 * (quat.mW * quat.mX + quat.mY * quat.mZ), 1.0 - 2.0 * (quat.mX * quat.mX + quat.mY * quat.mY));
    }
    
    public static double pitch(final Quaternion quat) {
        return Math.asin(2.0 * (quat.mW * quat.mY - quat.mZ * quat.mX));
    }
    
    public static double yaw(final Quaternion quat) {
        return Math.atan2(2.0 * (quat.mW * quat.mZ + quat.mX * quat.mY), 1.0 - 2.0 * (quat.mY * quat.mY + quat.mZ * quat.mZ));
    }
}
