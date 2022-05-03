package com.example.meet4sho.api;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class TMDBRequest extends AsyncTask<SearchFilter, Void, Void> {
    // API constants
    private final String API_ENDPOINT = "https://api.themoviedb.org/3/";
    private final String API_KEY = "06394bbe96d1ac41eded2b7f5aad470a";
    // member variables
    private RequestListener listener;
    private double latitude = 42.350444;
    private double longitude = -71.105377;
    private String deviceDatetime = Instant.now().toString();
    //private String deviceDatetime = "2022-04-25T21:23:51.027Z";

    public MGRequest(RequestListener listener, Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(SearchFilter... filter) {
        // set request headers
        String url = String.format("https://api.themoviedb.org/3/search/movie?api_key=%s",API_KEY);
        url += filter[0].toString();

        JSONObject result = HttpUtils.sendRequest(url);
        try {
            JSONObject film = result.getJSONObject("film");
            JSONArray cinemas = result.getJSONArray("cinemas");
            String movie_id = film.getString("film_id");

            String filmInfoURL = API_ENDPOINT + "filmDetails/?film_id=" + movie_id;
            JSONObject filmInfoResult = HttpUtils.sendRequest(filmInfoURL, props);
            String movie_info = filmInfoResult.getString("synopsis_long");

            String movie_name = film.getString("film_name");
            String movie_img = film.getJSONObject("images").getJSONObject("poster").getJSONObject("1").getJSONObject("medium").getString("film_image");
            movie_img = movie_img.replace("\\", "");
            for (int i = 0 ; i < cinemas.length(); i++) {
                JSONObject cinema = cinemas.getJSONObject(i);
                String cinema_id = cinema.getString("cinema_id");
                String cinema_name = cinema.getString("cinema_name");
                Double cinema_distance = cinema.getDouble("distance");

                String cinemaInfoURL = API_ENDPOINT + "cinemaDetails/?cinema_id=" + cinema_id;
                JSONObject resultCinemaInfo = HttpUtils.sendRequest(cinemaInfoURL, props);
                double cinema_lat = resultCinemaInfo.getDouble("lat");
                double cinema_lng = resultCinemaInfo.getDouble("lng");


                MGCinema cinema_i = new MGCinema(cinema_id, cinema_name, cinema_lat, cinema_lng, cinema_distance, movie_id, movie_name, movie_img, movie_info, new ArrayList<>());
                JSONArray showings = cinema.getJSONObject("showings").getJSONObject("Standard").getJSONArray("times");
                for (int j = 0; j < showings.length(); j++) {
                    JSONObject time = showings.getJSONObject(j);
                    String start_time = time.getString("start_time");
                    String end_time = time.getString("end_time");
                    cinema_i.addTime(new MGTime(start_time, end_time));
                }
                allEvents.add(cinema_i);
            }
            listener.updateViews(allEvents);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
