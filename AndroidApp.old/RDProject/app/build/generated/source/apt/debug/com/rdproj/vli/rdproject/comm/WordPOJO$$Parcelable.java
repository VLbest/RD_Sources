
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
public class WordPOJO$$Parcelable
    implements Parcelable, ParcelWrapper<com.rdproj.vli.rdproject.comm.WordPOJO>
{

    private com.rdproj.vli.rdproject.comm.WordPOJO wordPOJO$$0;
    @SuppressWarnings("UnusedDeclaration")
    public final static Creator<WordPOJO$$Parcelable>CREATOR = new Creator<WordPOJO$$Parcelable>() {


        @Override
        public WordPOJO$$Parcelable createFromParcel(android.os.Parcel parcel$$2) {
            return new WordPOJO$$Parcelable(read(parcel$$2, new IdentityCollection()));
        }

        @Override
        public WordPOJO$$Parcelable[] newArray(int size) {
            return new WordPOJO$$Parcelable[size] ;
        }

    }
    ;

    public WordPOJO$$Parcelable(com.rdproj.vli.rdproject.comm.WordPOJO wordPOJO$$2) {
        wordPOJO$$0 = wordPOJO$$2;
    }

    @Override
    public void writeToParcel(android.os.Parcel parcel$$0, int flags) {
        write(wordPOJO$$0, parcel$$0, flags, new IdentityCollection());
    }

    public static void write(com.rdproj.vli.rdproject.comm.WordPOJO wordPOJO$$1, android.os.Parcel parcel$$1, int flags$$0, IdentityCollection identityMap$$0) {
        int identity$$0 = identityMap$$0 .getKey(wordPOJO$$1);
        if (identity$$0 != -1) {
            parcel$$1 .writeInt(identity$$0);
        } else {
            parcel$$1 .writeInt(identityMap$$0 .put(wordPOJO$$1));
            parcel$$1 .writeString(wordPOJO$$1 .id);
            parcel$$1 .writeString(wordPOJO$$1 .word);
        }
    }

    @Override
    public int describeContents() {
        return  0;
    }

    @Override
    public com.rdproj.vli.rdproject.comm.WordPOJO getParcel() {
        return wordPOJO$$0;
    }

    public static com.rdproj.vli.rdproject.comm.WordPOJO read(android.os.Parcel parcel$$3, IdentityCollection identityMap$$1) {
        int identity$$1 = parcel$$3 .readInt();
        if (identityMap$$1 .containsKey(identity$$1)) {
            if (identityMap$$1 .isReserved(identity$$1)) {
                throw new ParcelerRuntimeException("An instance loop was detected whild building Parcelable and deseralization cannot continue.  This error is most likely due to using @ParcelConstructor or @ParcelFactory.");
            }
            return identityMap$$1 .get(identity$$1);
        } else {
            com.rdproj.vli.rdproject.comm.WordPOJO wordPOJO$$4;
            int reservation$$0 = identityMap$$1 .reserve();
            wordPOJO$$4 = new com.rdproj.vli.rdproject.comm.WordPOJO();
            identityMap$$1 .put(reservation$$0, wordPOJO$$4);
            wordPOJO$$4 .id = parcel$$3 .readString();
            wordPOJO$$4 .word = parcel$$3 .readString();
            com.rdproj.vli.rdproject.comm.WordPOJO wordPOJO$$3 = wordPOJO$$4;
            identityMap$$1 .put(identity$$1, wordPOJO$$3);
            return wordPOJO$$3;
        }
    }

}
