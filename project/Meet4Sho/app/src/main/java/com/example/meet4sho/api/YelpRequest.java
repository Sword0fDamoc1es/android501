package com.example.meet4sho.api;

import android.os.AsyncTask;
import android.util.Log;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class calls the Yelp API and parses all the info into YelpRestaurant Objects
 */

public class YelpRequest extends AsyncTask<SearchFilter, Void, Void> {

    /**
     * Yelp API constants
     */
    private final String YELP_API_KEY = "88RR4WnZJtAJeY_z4UA6bR-NhnlmhFk5zYFqxD30lDTrpfa543apNhHmDzum2ElLWI4od6pGjBfMjIoG7T9ReRU8vYaQejY57KwBwKwoj-r9ID9ahOJG4D1d5TRDYnYx";
    private RequestListener listener;

    public YelpRequest(RequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(SearchFilter... filter) {
        String urlS = "https://api.yelp.com/v3/businesses/search?";    // base url
        urlS += filter[0].toString();
        Log.i("------>", urlS);

        URL url = null;
        HttpURLConnection urlConnection = null;
        String httpResponse = null;
        try {
            url = new URL(urlS);                                                          // create a new URL object
            urlConnection = (HttpURLConnection) url.openConnection();                     // open a new HTTP url connection
            urlConnection.setRequestProperty("Authorization", "Bearer " + YELP_API_KEY);  // set request header
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());     // get response result from GET request
            httpResponse = IOUtil.toString(in);
            JSONObject result = null;
            result = new JSONObject(httpResponse);

            JSONArray restaurants = result.getJSONArray("businesses");
            List<YelpRestaurant> allRestaurants = new ArrayList<>();
            for (int i=0; i < restaurants.length(); i++) {
                JSONObject event = JSONUtils.getJSONObject(restaurants, i);
                String id = JSONUtils.getString(event,"id");
                String name = JSONUtils.getString(event,"name");
                String image_url = JSONUtils.getString(event,"image_url");
                String is_closed = JSONUtils.getString(event,"is_closed");
                String url_received = JSONUtils.getString(event,"url");
                String review_count = JSONUtils.getString(event,"review_count");
                JSONArray categories = JSONUtils.getJSONAray(event, "categories");
                String rating = JSONUtils.getString(event,"rating");
                String latitude = JSONUtils.getJSONObject(event,"coordinates").getString("latitude");
                String longitude = JSONUtils.getJSONObject(event,"coordinates").getString("longitude");
                JSONArray transactions = JSONUtils.getJSONAray(event, "transactions");
                String price = JSONUtils.getString(event,"price");
                JSONObject location = JSONUtils.getJSONObject(event,"location");
                String address1 = location.getString("address1");
                String address2 = location.getString("address2");
                String address3 = location.getString("address3");
                String city = location.getString("city");
                String zip_code = location.getString("zip_code");
                String country = location.getString("country");
                String state = location.getString("state");
                JSONArray disp_address = location.getJSONArray("display_address");
                String display_address = disp_address.getString(0) + disp_address.getString(1);
                String phone = JSONUtils.getString(event,"phone");
                String display_phone = JSONUtils.getString(event,"display_phone");
                String distance = JSONUtils.getString(event,"distance");
                YelpRestaurant yelpRes = new YelpRestaurant(id, name, image_url, is_closed, url_received, review_count
                        , categories, rating, latitude, longitude, transactions,
                        price, address1, address2, address3, city, zip_code,
                        country, state, display_address, display_phone, phone, distance);
                allRestaurants.add(yelpRes);

            }
            listener.updateViews(allRestaurants);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();            // IMPORTANT: disconnect url connection
        }
        return null;
    }

}