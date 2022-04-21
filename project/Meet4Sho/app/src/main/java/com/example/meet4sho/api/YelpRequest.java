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

public class YelpRequest extends AsyncTask<SearchFilter, Void, Void> {
    private final String YELP_API_KEY = "88RR4WnZJtAJeY_z4UA6bR-NhnlmhFk5zYFqxD30lDTrpfa543apNhHmDzum2ElLWI4od6pGjBfMjIoG7T9ReRU8vYaQejY57KwBwKwoj-r9ID9ahOJG4D1d5TRDYnYx";
    private RequestListener listener;

    public YelpRequest(RequestListener listener) {
        this.listener = listener;
    }

    @Override
    protected Void doInBackground(SearchFilter... filter) {
        String urlS = "https://api.yelp.com/v3/businesses/search?";                                        // base url
        urlS += filter[0].toString();
        Log.i("------>", urlS);

//        // HTTP Request Testing
//         String result = HttpUtils.sendRequestTest(url);
//         listener.updateViews(result);
//
//        JSONObject result = HttpUtils.sendRequest(url);
        URL url = null;
        HttpURLConnection urlConnection = null;
        String httpResponse = null;

        try {
            url = new URL(urlS);                                                                           // create a new URL object
            urlConnection = (HttpURLConnection) url.openConnection();                                               // open a new HTTP url connection
            urlConnection.setRequestProperty("Authorization", "Bearer " + YELP_API_KEY);                                 // set request header
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());                               // get response result from GET request
            httpResponse = IOUtil.toString(in);
            JSONObject result = null;
            result = new JSONObject(httpResponse);
//            JSONObject result = HttpUtils.sendRequest(httpResponse);

//            JSONArray events = result.getJSONObject("_embedded").getJSONArray("events");
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


//                JSONObject event_location = JSONUtils.getJSONObject(event, "place");
//                JSONObject event_address = JSONUtils.getJSONObject(event_location, "address");
//                String event_location_address1 = JSONUtils.getString(event_address, "line1");
//                String event_location_address2 = JSONUtils.getString(event_address, "line2");
//                String event_location_address3 = JSONUtils.getString(event_address, "line3");
//                String event_location_city = JSONUtils.getString(JSONUtils.getJSONObject(event_location, "city"), "name");
//                JSONObject event_location_state = JSONUtils.getJSONObject(event_location, "state");
//                String event_location_stateName = JSONUtils.getString(event_location_state, "name");
//                String event_location_stateCode = JSONUtils.getString(event_location_state, "stateCode");
//                JSONObject event_location_country = JSONUtils.getJSONObject(event_location, "country");
//                String event_locaiton_countryName = JSONUtils.getString(event_location_country, "name");
//                String event_locaiton_countryCode = JSONUtils.getString(event_location_country, "countryCode");
//                String event_location_zipcode = JSONUtils.getString(event_location, "postalCode");
//                JSONObject event_location_ll = JSONUtils.getJSONObject(event_location, "location");
//                String event_location_longitude = JSONUtils.getString(event_location_ll, "longitude");
//                String event_location_latitude = JSONUtils.getString(event_location_ll, "latitude");

//                JSONObject event = events.getJSONObject(i);
//                String event_id = event.getString("id");
//                String event_name = event.getString("name");
//                String event_description = event.getString("description");
//                String event_info = event.getString("info");
//                String event_url = event.getString("url");
//                JSONArray event_images = event.getJSONArray("images");
//                for (int j = 0; j < event_images.length(); j++) {
//                    JSONObject event_image = event_images.getJSONObject(j);
//                    String event_image_url = event_image.getString("url");
//                    int event_image_width = event_image.getInt("width");
//                    int event_image_height = event_image.getInt("height");
//                }
//                JSONObject event_dates = event.getJSONObject("dates");
//                String event_start_dateTime = event_dates.getJSONObject("start").getString("dateTime");
//                //String event_startDate = event_dates.getJSONObject("start").getString("localDate");
//                String event_end_dateTime = event_dates.getJSONObject("end").getString("dateTime");
//                boolean event_span = event_dates.getBoolean("spanMultipleDays");
//                String event_status = event_dates.getJSONObject("status").getString("code");
//                JSONArray event_classifications = event.getJSONArray("classifications");
//                for (int k = 0; k < event_classifications.length(); k++) {
//                    JSONObject event_classification = event_classifications.getJSONObject(k);
//                    String event_classification_segment_id = event_classification.getJSONObject("segment").getString("id");
//                    String event_classification_segment_name = event_classification.getJSONObject("segment").getString("name");
//                    String event_classification_genre_id = event_classification.getJSONObject("genre").getString("id");
//                    String event_classification_genre_name = event_classification.getJSONObject("genre").getString("name");
//                    String event_classification_subgenre_id = event_classification.getJSONObject("subGenre").getString("id");
//                    String event_classification_subgenre_name = event_classification.getJSONObject("subGenre").getString("name");
//                }
//                JSONObject event_location = event.getJSONObject("place");
//                String event_location_address1 = event_location.getJSONObject("address").getString("line1");
//                String event_location_address2 = event_location.getJSONObject("address").getString("line2");
//                String event_location_address3 = event_location.getJSONObject("address").getString("line3");
//                String event_location_city = event_location.getJSONObject("city").getString("name");
//                String event_location_stateName = event_location.getJSONObject("state").getString("name");
//                String event_location_stateCode = event_location.getJSONObject("state").getString("stateCode");
//                String event_locaiton_countryName = event_location.getJSONObject("country").getString("name");
//                String event_locaiton_countryCode = event_location.getJSONObject("country").getString("countryCode");
//                String event_location_zipcode = event_location.getString("postalCode");
//                String event_location_longitude = event_location.getJSONObject("location").getString("longitude");
//                String event_location_latitude = event_location.getJSONObject("location").getString("latitude");

                // TESTING
//                String output =   "event_id:                " + event_id + "\n"
//                                + "event_name:              " + event_name + "\n"
//                                + "event_description:       " + event_description + "\n"
//                                + "event_info:              " + event_info + "\n"
//                                + "event_url:               " + event_url + "\n"
//                                + "event_start_dateTime:    " + event_start_dateTime + "\n"
//                                + "event_end_dateTime:      " + event_end_dateTime + "\n"
//                                + "event_span:              " + event_span + "\n"
//                                + "event_status_code:       " + event_status_code + "\n"
//                                + "event_venue_address1:    " + event_venue_address1 + "\n"
//                                + "event_venue_address2:    " + event_venue_address2 + "\n"
//                                + "event_venue_address3:    " + event_venue_address3 + "\n"
//                                + "event_venue_city:        " + event_venue_city + "\n"
//                                + "event_venue_stateName:   " + event_venue_stateName + "\n"
//                                + "event_venue_stateCode:   " + event_venue_stateCode + "\n"
//                                + "event_venue_countryName: " + event_venue_countryName + "\n"
//                                + "event_venue_countryCode: " + event_venue_countryCode + "\n"
//                                + "event_venue_zipcode:     " + event_venue_zipcode + "\n"
//                                + "event_venue_longitude:   " + event_venue_longitude + "\n"
//                                + "event_venue_latitude:    " + event_venue_latitude + "\n";
            }
            listener.updateViews(allRestaurants);
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();                                                                             // IMPORTANT: disconnect url connection
        }
        return null;
    }

}