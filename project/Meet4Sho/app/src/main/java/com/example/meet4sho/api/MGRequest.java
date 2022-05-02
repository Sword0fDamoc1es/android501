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
public class MGRequest extends AsyncTask<SearchFilter, Void, Void> {
    // API constants
    private final String API_ENDPOINT = "https://api-gate2.movieglu.com/";
    private final String API_CLIENT = "MEET_0";
    private final String API_KEY = "y2bw2c1y0d9MKeUZRDrRC2lJWccKZ8yn67OW5FEg";
    private final String API_AUTH = "Basic TUVFVF8wOjR5VnJSZnVQMUhEYw==";
    private final String API_TERRITORY = "US";
    private final String API_VERSION = "v200";
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
        Map<String, String> props = new HashMap<>();
        props.put("api-version", API_VERSION);
        props.put("territory", API_TERRITORY);
        props.put("authorization", API_AUTH);
        props.put("x-api-key", API_KEY);
        props.put("client", API_CLIENT);
        props.put("device-datetime", deviceDatetime);
        props.put("geolocation", Double.toString(latitude) + ";" + Double.toString(longitude));

        // search film name using MovieGlu's filmLiveSearch and get a film id
//        String query = filter[0].toString().replaceAll(" ", "+").substring(1);
//        if (query.charAt(query.length()-1) == '+') query = query.substring(0,query.length()-1);
//        String url = API_ENDPOINT + "filmLiveSearch/?" + query + "&n=1";
//        Log.i("------>", url);
        String name = filter[0].get("query").replace(' ', '+');
        if (name.charAt(name.length()-1) == '+') name = name.substring(0, name.length()-1);
        String url = API_ENDPOINT + "filmLiveSearch/?query=" + name + "&n=1";
        Log.i("------>", url);
        JSONObject result = HttpUtils.sendRequest(url, props);
        String film_id = null;
        try {
            film_id = result.getJSONArray("films").getJSONObject(0).getString("film_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // search film id using MovieGlu's filmShowTimes and get showtimes for selected film at nearest cinemas
        Log.i("------>", film_id);
        String date = filter[0].get("date");
        url = API_ENDPOINT + "filmShowTimes/?film_id=" + film_id + "&date=" + date;
        Log.i("------>", url);
        result = HttpUtils.sendRequest(url, props);
        List<MGCinema> allEvents = new ArrayList<>();
        try {
            JSONObject film = result.getJSONObject("film");
            JSONArray cinemas = result.getJSONArray("cinemas");
            String movie_id = film.getString("film_id");
            /*
            Part I added
             */
            String filmInfoURL = API_ENDPOINT + "filmDetails/?film_id=" + movie_id;
            JSONObject filmInfoResult = HttpUtils.sendRequest(filmInfoURL, props);
            String movie_info = filmInfoResult.getString("synopsis_long");

            /*
             */
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
