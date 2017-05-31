// 
// Decompiled by Procyon v0.5.30
// 

package com.thalmic.myo;

public class Vector3
{
    private double mX;
    private double mY;
    private double mZ;
    
    public Vector3() {
        final double mx = 0.0;
        this.mZ = mx;
        this.mY = mx;
        this.mX = mx;
    }
    
    public Vector3(final Vector3 other) {
        this.set(other);
    }
    
    public Vector3(final double x, final double y, final double z) {
        this.mX = x;
        this.mY = y;
        this.mZ = z;
    }
    
    public void set(final Vector3 src) {
        this.mX = src.mX;
        this.mY = src.mY;
        this.mZ = src.mZ;
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
    
    public double length() {
        return Math.sqrt(this.mX * this.mX + this.mY * this.mY + this.mZ * this.mZ);
    }
    
    public void add(final Vector3 other) {
        this.mX += other.mX;
        this.mY += other.mY;
        this.mZ += other.mZ;
    }
    
    public void subtract(final Vector3 other) {
        this.mX -= other.mX;
        this.mY -= other.mY;
        this.mZ -= other.mZ;
    }
    
    public void multiply(final double n) {
        this.mX *= n;
        this.mY *= n;
        this.mZ *= n;
    }
    
    public void divide(final double n) {
        this.mX /= n;
        this.mY /= n;
        this.mZ /= n;
    }
    
    public void normalize() {
        final double scale = 1.0 / this.length();
        this.multiply(scale);
    }
    
    public double dot(final Vector3 other) {
        return this.mX * other.mX + this.mY * other.mY + this.mZ * other.mZ;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final Vector3 vector = (Vector3)o;
        return Double.compare(vector.mX, this.mX) == 0 && Double.compare(vector.mY, this.mY) == 0 && Double.compare(vector.mZ, this.mZ) == 0;
    }
    
    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(this.mX);
        int result = (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.mY);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        temp = Double.doubleToLongBits(this.mZ);
        result = 31 * result + (int)(temp ^ temp >>> 32);
        return result;
    }
    
    @Override
    public String toString() {
        return "Vector3{mX=" + this.mX + ", mY=" + this.mY + ", mZ=" + this.mZ + '}';
    }
}
