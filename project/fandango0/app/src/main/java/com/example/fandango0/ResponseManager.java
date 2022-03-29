package com.example.fandango0;

import android.content.Context;
import android.widget.Toast;

import com.example.fandango0.model.APIresponse;

import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ResponseManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public ResponseManager(Context context) {
        this.context = context;
    }

    public void getReplyByZip(onFetchListener listener, String word){
        AMC_locationSuggestion_zip alz = retrofit.create(AMC_locationSuggestion_zip.class);
        Call<List<APIresponse>> call = alz.repos(word);

        try{
            call.enqueue(new Callback<List<APIresponse>>() {
                @Override
                public void onResponse(Call<List<APIresponse>> call, Response<List<APIresponse>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(context,"Error no response!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    listener.onFetchData(response.body().get(0),response.message());

                }

                @Override
                public void onFailure(Call<List<APIresponse>> call, Throwable throwable) {
                    listener.onError("Request failed!");
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Toast.makeText(context,"ERROR!",Toast.LENGTH_SHORT).show();
        }

    }

    public interface AMC_locationSuggestion_zip{
//        @Query("location-suggestions/?query={word}")
//        @GET("location-suggestions/?query={word}")
        @GET("movie/now_playing?api_key=06394bbe96d1ac41eded2b7f5aad470a&language=en-US&page=1&region={country_abbreviation}")
        Call<List<APIresponse>> repos(
               @Query("country_abbreviation") String country_abbrev
        );


    }

}
