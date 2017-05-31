package com.rdproj.vli.rdproject.comm;

import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordPojoController implements Callback<List<WordPOJO>>{

    private List<WordPOJO> wordlist;

    public WordPojoController(){
        wordlist = new LinkedList<>();
    }

    @Override
    public void onResponse(Call<List<WordPOJO>> call, Response<List<WordPOJO>> response) {
        Log.d("d", "d");
    }

    @Override
    public void onFailure(Call<List<WordPOJO>> call, Throwable t) {
        Log.d("d", "d");
    }

    public List<WordPOJO> getWordlist() {
        return wordlist;
    }
}
