package com.example.scrollinglist.video;


import com.example.scrollinglist.pojorec.RecommandationResponse;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by belangek on 9/22/16.
 */

public class RetrofitRec implements Callback<RecommandationResponse> {

    private static final String BASE_URL = "http://search.cc-d.mtvi.com";
    private RecommandationListener listener;
    private EpisodesServices service;
    private String TAG = RetrofitRec.class.getSimpleName();

    public RetrofitRec(RecommandationListener listener) {
        this.listener = listener;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        service = retrofit.create(EpisodesServices.class);//slow ... it hasnt loaded y
        this.listener = listener;
    }
    public void getRecommandation(String q) {
        Call<RecommandationResponse> call = null;
        call = service.getRecommandation(q);
        call.enqueue(this);
    }
    @Override
    public void onResponse(Call<RecommandationResponse> call, Response<RecommandationResponse> response) {
        int statusCode = response.code();

        RecommandationResponse responseObj = response.body();
        listener.onSuccess(responseObj);
    }

    @Override
    public void onFailure(Call<RecommandationResponse> call, Throwable t) {
        listener.onFailure(t.getLocalizedMessage());
    }

}
