package com.example.fandango0;

import android.content.Context;

import com.example.fandango0.model.APIresponse;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class ResponseManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.amctheatres.com/v2/theatres/42")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ResponseManager(Context context) {
        this.context = context;
    }

    public interface AMC_locationSuggestion_zip{
        @GET("location-suggestions/?query={77429}")
        Call<List<String>> repos(
               @Path("77429") String word
        );


    }

}
