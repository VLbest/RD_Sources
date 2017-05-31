
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
public class GestureUnit$$Parcelable
    implements Parcelable, ParcelWrapper<com.rdproj.vli.rdproject.comm.GestureUnit>
{

    private com.rdproj.vli.rdproject.comm.GestureUnit gestureUnit$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<GestureUnit$$Parcelable>CREATOR = new Creator<GestureUnit$$Parcelable>() {


        @Override
        public GestureUnit$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new GestureUnit$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public GestureUnit$$Parcelable[] newArray(int size) {
            return new GestureUnit$$Parcelable[size] ;
        }

    }
    ;

    public GestureUnit$$Parcelable(com.rdproj.vli.rdproject.comm.GestureUnit gestureUnit$$2) {
        gestureUnit$$0 = gestureUnit$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(gestureUnit$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.rdproj.vli.rdproject.comm.GestureUnit gestureUnit$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(gestureUnit$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(gestureUnit$$1));
            com.rdproj.vli.rdproject.comm.MyoData$$Parcelable.write(gestureUnit$$1 .leftMyo, parcel$$1, flags$$0, identityMap$$0);
            com.rdproj.vli.rdproject.comm.MyoData$$Parcelable.write(gestureUnit$$1 .rightMyo, parcel$$1, flags$$0, identityMap$$0);
            parcel$$1 .writeString(gestureUnit$$1 .gestureName);
            parcel$$1 .writeLong(gestureUnit$$1 .timestamp);
            parcel$$1 .writeInt(gestureUnit$$1 .sampleNumber);
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.rdproj.vli.rdproject.comm.GestureUnit getParcel() {
        return gestureUnit$$0;
    }

    public static com.rdproj.vli.rdproject.comm.GestureUnit read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.rdproj.vli.rdproject.comm.GestureUnit gestureUnit$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            gestureUnit$$4 = new com.rdproj.vli.rdproject.comm.GestureUnit();
            identityMap$$1 .put(reservation$$0, gestureUnit$$4);
            com.rdproj.vli.rdproject.comm.MyoData myoData$$0 = com.rdproj.vli.rdproject.comm.MyoData$$Parcelable.read(parcel$$3, identityMap$$1);
            gestureUnit$$4 .leftMyo = myoData$$0;
            com.rdproj.vli.rdproject.comm.MyoData myoData$$1 = com.rdproj.vli.rdproject.comm.MyoData$$Parcelable.read(parcel$$3, identityMap$$1);
            gestureUnit$$4 .rightMyo = myoData$$1;
            gestureUnit$$4 .gestureName = parcel$$3 .readString();
            gestureUnit$$4 .timestamp = parcel$$3 .readLong();
            gestureUnit$$4 .sampleNumber = parcel$$3 .readInt();
            com.rdproj.vli.rdproject.comm.GestureUnit gestureUnit$$3 = gestureUnit$$4;
            identityMap$$1 .put(identity$$1, gestureUnit$$3);
            return gestureUnit$$3;
        }
    }

}
