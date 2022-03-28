package com.example.fandango0;

import android.content.Context;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class ResponseManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.amctheatres.com/v2/theatres/42")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ResponseManager(Context context) {
        this.context = context;
    }

    public interface AMC_locationSuggestion_zip{
        @GET("location-suggestions/?query={zip}")


    }

    @Override

}
