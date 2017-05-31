
package com.rdproj.vli.rdproject.comm;

import android.os.Parcelable;
import android.os.Parcelable.Creator;
import org.parceler.Generated;
import org.parceler.IdentityCollection;
import org.parceler.ParcelWrapper;
import org.parceler.ParcelerRuntimeException;

@Generated(value = "org.parceler.ParcelAnnotationProcessor", date = "2017-05-11T11:46+0200")
@SuppressWarnings({
    "unchecked",
    "deprecation"
})
public class MyoData$$Parcelable
    implements Parcelable, ParcelWrapper<com.rdproj.vli.rdproject.comm.MyoData>
{

    private com.rdproj.vli.rdproject.comm.MyoData myoData$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<MyoData$$Parcelable>CREATOR = new Creator<MyoData$$Parcelable>() {


        @Override
        public MyoData$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new MyoData$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public MyoData$$Parcelable[] newArray(int size) {
            return new MyoData$$Parcelable[size] ;
        }

    }
    ;

    public MyoData$$Parcelable(com.rdproj.vli.rdproject.comm.MyoData myoData$$2) {
        myoData$$0 = myoData$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(myoData$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.rdproj.vli.rdproject.comm.MyoData myoData$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(myoData$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(myoData$$1));
            parcel$$1 .writeDouble(myoData$$1 .oriZ);
            parcel$$1 .writeDouble(myoData$$1 .oriW);
            parcel$$1 .writeDouble(myoData$$1 .oriX);
            parcel$$1 .writeDouble(myoData$$1 .oriY);
            parcel$$1 .writeDouble(myoData$$1 .gyroX);
            parcel$$1 .writeDouble(myoData$$1 .gyroY);
            parcel$$1 .writeDouble(myoData$$1 .gyroZ);
            parcel$$1 .writeDouble(myoData$$1 .accZ);
            parcel$$1 .writeDouble(myoData$$1 .accY);
            parcel$$1 .writeInt(myoData$$1 .emg8);
            parcel$$1 .writeInt(myoData$$1 .emg7);
            parcel$$1 .writeInt(myoData$$1 .emg6);
            parcel$$1 .writeInt(myoData$$1 .emg5);
            parcel$$1 .writeDouble(myoData$$1 .accX);
            parcel$$1 .writeInt(myoData$$1 .emg4);
            parcel$$1 .writeInt(myoData$$1 .emg3);
            parcel$$1 .writeInt(myoData$$1 .emg2);
            parcel$$1 .writeInt(myoData$$1 .emg1);
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.rdproj.vli.rdproject.comm.MyoData getParcel() {
        return myoData$$0;
    }

    public static com.rdproj.vli.rdproject.comm.MyoData read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.rdproj.vli.rdproject.comm.MyoData myoData$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            myoData$$4 = new com.rdproj.vli.rdproject.comm.MyoData();
            identityMap$$1 .put(reservation$$0, myoData$$4);
            myoData$$4 .oriZ = parcel$$3 .readDouble();
            myoData$$4 .oriW = parcel$$3 .readDouble();
            myoData$$4 .oriX = parcel$$3 .readDouble();
            myoData$$4 .oriY = parcel$$3 .readDouble();
            myoData$$4 .gyroX = parcel$$3 .readDouble();
            myoData$$4 .gyroY = parcel$$3 .readDouble();
            myoData$$4 .gyroZ = parcel$$3 .readDouble();
            myoData$$4 .accZ = parcel$$3 .readDouble();
            myoData$$4 .accY = parcel$$3 .readDouble();
            myoData$$4 .emg8 = parcel$$3 .readInt();
            myoData$$4 .emg7 = parcel$$3 .readInt();
            myoData$$4 .emg6 = parcel$$3 .readInt();
            myoData$$4 .emg5 = parcel$$3 .readInt();
            myoData$$4 .accX = parcel$$3 .readDouble();
            myoData$$4 .emg4 = parcel$$3 .readInt();
            myoData$$4 .emg3 = parcel$$3 .readInt();
            myoData$$4 .emg2 = parcel$$3 .readInt();
            myoData$$4 .emg1 = parcel$$3 .readInt();
            com.rdproj.vli.rdproject.comm.MyoData myoData$$3 = myoData$$4;
            identityMap$$1 .put(identity$$1, myoData$$3);
            return myoData$$3;
        }
    }

}
