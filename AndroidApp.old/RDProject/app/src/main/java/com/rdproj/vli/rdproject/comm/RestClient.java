package com.rdproj.vli.rdproject.comm;

import android.os.Handler;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.Streams;
import com.thalmic.myo.Myo;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Retrofit;

public class RestClient {

    private static final String SERVICE_URL = "http://104.155.73.191:5000/";
    private ApiService apiService;

    private Callback<ResponseBody> dataCallback = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Log.d("d", "d");
        }
        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            Log.d("d", "d");
        }
    };


    public RestClient(){
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        Retrofit retrofit = new Retrofit.Builder().baseUrl(SERVICE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();

        apiService = retrofit.create(ApiService.class);

    }

    public void setGestureUnitData(GestureUnit gestureUnit){
        apiService.sendGestureUnitData(gestureUnit).enqueue(dataCallback);
    }

    public void addToWordlist(WordPOJO word){
        apiService.addToWordlist(word).enqueue(dataCallback);
    }

    public void getKnownWords(Callback<HashMap<String, String>> callback){
        Call<HashMap<String,String>> words = apiService.getKnownWords();
        words.enqueue(callback);
    }

    public void getApiStatus(Callback<Boolean> callback){
        Call<Boolean> status = apiService.getApiStatus();
        status.enqueue(callback);
    }

}
