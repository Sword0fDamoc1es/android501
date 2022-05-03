package com.example.meet4sho.api;

import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.meet4sho.RestaurantRecyclerAdapter;
import com.example.meet4sho.RestaurantsFragment;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MGRequest extends AsyncTask<SearchFilter, Void, Void> {

    private final String YELP_API_KEY = "88RR4WnZJtAJeY_z4UA6bR-NhnlmhFk5zYFqxD30lDTrpfa543apNhHmDzum2ElLWI4od6pGjBfMjIoG7T9ReRU8vYaQejY57KwBwKwoj-r9ID9ahOJG4D1d5TRDYnYx";

//    client:	BOST_0
//    x-api-key:	fms66r7H056EXLIIZhgcg8pquCOwXbCo4EpfVJhl
//    authorization:	Basic Qk9TVF8wX1hYOmFoUGVtdGFzSTIxQQ==
//    territory:	XX
//    api-version:	v200
//    geolocation:	-22.0;14.0 (Recommended location, note initial minus character)
//    device-datetime:	yyyy-mm-ddThh:mm:ss.sssZ (ISO 8601 format, e.g. 2018-09-14T08:30:17.360Z)

    // API constants
    private final String API_ENDPOINT = "https://api-gate2.movieglu.com/";
    private final String API_CLIENT = "NYU";
    private final String API_KEY = "5H78ZTGLPI8j1e24ewsfh5lrita93T3S2GPxQkNQ";
    private final String API_AUTH = "Basic TIIVOmgyVjVKZUdqTU9IQw==";
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
        props.put("api-version", "v200");
        props.put("territory", "US");
        props.put("authorization", "Basic VU5JVl81OTpPR2lBeng4b1E3UWQ=");
        props.put("x-api-key", "NaNAgqb9cgPRncNlw1vm8estEInA4B17e7sn5rLb\n");
        props.put("client", "UNIV_59");
        props.put("device-datetime", deviceDatetime);
        props.put("geolocation", Double.toString(latitude) + ";" + Double.toString(longitude));
        Log.d("MRRequest",deviceDatetime);
        // search film name using MovieGlu's filmLiveSearch and get a film id
//        String query = filter[0].toString().replaceAll(" ", "+").substring(1);
//        if (query.charAt(query.length()-1) == '+') query = query.substring(0,query.length()-1);
//        String url = API_ENDPOINT + "filmLiveSearch/?" + query + "&n=1";
//        Log.i("------>", url);
        String name = filter[0].get("query").replace(' ', '+');
        if (name.charAt(name.length()-1) == '+') name = name.substring(0, name.length()-1);
//        Log.d("MGRequest",name);

        String url = API_ENDPOINT + "filmLiveSearch/?query=" + name + "&n=1";
        Log.i("------>", url);
        JSONObject result = HttpUtils.sendRequest(url, props);
        String film_id = null;
        try {
            film_id = result.getJSONArray("films").getJSONObject(0).getString("film_id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (film_id == null) return null;


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

            String movie_name = film.getString("film_name");
            String movie_img = film.getJSONObject("images").getJSONObject("poster").getJSONObject("1").getJSONObject("medium").getString("film_image");
            movie_img = movie_img.replace("\\", "");
            for (int i = 0 ; i < cinemas.length(); i++) {
                JSONObject cinema = cinemas.getJSONObject(i);
                String cinema_id = cinema.getString("cinema_id");
                String cinema_name = cinema.getString("cinema_name");
                Double cinema_distance = cinema.getDouble("distance");

                SearchFilter filterForMovie = new SearchFilter();
                String urlS = "https://api.yelp.com/v3/businesses/search?";
                filterForMovie.add("longitude", String.valueOf(longitude));
                filterForMovie.add("latitude", String.valueOf(latitude));
                filterForMovie.add("term", cinema_name);
                urlS += "longitude="+longitude+"&latitude="+latitude+"&term="+cinema_name;

                URL YelpURL = new URL(urlS);                                                                           // create a new URL object
                HttpURLConnection urlConnection = (HttpURLConnection) YelpURL.openConnection();                                               // open a new HTTP url connection
                urlConnection.setRequestProperty("Authorization", "Bearer " + YELP_API_KEY);                                 // set request header
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());                               // get response result from GET request
                String httpResponse = IOUtil.toString(in);
                JSONObject YelpResult = new JSONObject(httpResponse);
                JSONArray movieTheaters = YelpResult.getJSONArray("businesses");
                Log.d("Yelp Movies", movieTheaters.toString());
                JSONObject movieTheater = JSONUtils.getJSONObject(movieTheaters, 0);
                String latToProvide = JSONUtils.getJSONObject(movieTheater,"coordinates").getString("latitude");
                String lngToProvide = JSONUtils.getJSONObject(movieTheater,"coordinates").getString("longitude");


                double cinema_lat = Double.parseDouble(latToProvide);
                double cinema_lng = Double.parseDouble(lngToProvide);


                MGCinema cinema_i = new MGCinema(cinema_id, cinema_name, cinema_lat, cinema_lng, cinema_distance, movie_id, movie_name, movie_img, new ArrayList<>());
                JSONArray showings = cinema.getJSONObject("showings").getJSONObject("Standard").getJSONArray("times");
                for (int j = 0; j < showings.length(); j++) {
                    JSONObject time = showings.getJSONObject(j);
                    String start_time = time.getString("start_time");
                    String end_time = time.getString("end_time");
                    cinema_i.addTime(new MGTime(start_time, end_time, date));
                }
                allEvents.add(cinema_i);
            }
            listener.updateViews(allEvents);
        } catch (JSONException | MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
