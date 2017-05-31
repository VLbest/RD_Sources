package com.rdproj.vli.rdproject.comm;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.lang.ref.SoftReference;

@Parcel
public class WordPOJO {

    @SerializedName("_id")
    public String id;

    @SerializedName("word")
    public String word;

    public WordPOJO(){

    }

    public WordPOJO(String key, String value) {
        id = key;
        word = value;
    }
}
