package com.rdproj.vli.rdproject.comm;

import com.google.gson.JsonObject;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @GET("status")
    Call<Boolean> getApiStatus();

    @POST("data/insert")
    Call<ResponseBody> sendGestureUnitData(@Body GestureUnit gestureUnit);

    @POST("wordlist")
    Call<ResponseBody> addToWordlist(@Body WordPOJO word);

    @GET("wordlist")
    Call<HashMap<String,String>> getKnownWords();

}
