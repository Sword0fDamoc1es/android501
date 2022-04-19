package com.example.meet4sho;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.meet4sho.AppActivity;
import com.example.meet4sho.TM_RecyclerAdapter;

import org.apache.commons.io.IOUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class API_Fetch {
    private static final String TM_API_KEY = "PqFUIagPA9p1AMWB2SeEF9QFyhGnG0P7";
    private static final String LOG_TAG = "[MainActivity]";

    private static TM_RecyclerAdapter ra;
    private static List<String> names;
    private static List<String> descriptions;
    private static List<String> imgUrls;



    public static void Search(TM_RecyclerAdapter r, List<String> nms, List<String> des, List<String> urls, int pn, String city, String keyword){
        ra = r;
        names = nms;
        descriptions = des;
        imgUrls = urls;

        new TicketMaster().execute(Integer.toString(pn), city, keyword);
    }

    /**
     * TicketMaster is the class that creates a URL connection so
     * that we may send and retrieve requests from TicketMaster
     */
    private static class TicketMaster extends AsyncTask<String, String, String> {

        /**
         * 1.) Create a formatted URL string with the keyword now_playing
         *     and the passed in API key and region code
         * 2.) Create a URL connection from the formatted URL string
         */
        @Override
        protected String doInBackground(String... params) {
            String page = params[0];
            String city = params[1];
            String keyword = params[2];
            String size = Integer.toString(10);
            String url_formatted = String.format("https://app.ticketmaster.com/discovery/v2/events.json?keyword=%s&city=%s&size=%s&page=%s&apikey=%s",keyword,city,size,page,TM_API_KEY);
            Log.i(LOG_TAG, "Formatted URL: " + url_formatted);
            URL url = null;
            HttpURLConnection urlConnection = null;
            String responseString = null;
            try {
                url = new URL(url_formatted);
                urlConnection = (HttpURLConnection) url.openConnection();
                Log.i(LOG_TAG, urlConnection.getContentType());
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = IOUtil.toString(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return responseString;
        }

        /**
         * Parse through the JSON object and retrieve all the info that we need
         * i.e. Name of the event, venue, images, state
         */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("API RESPONSE:", result);
            JSONObject jsonObj = null;
            String id = "";
            try {
                jsonObj = new JSONObject(result);
                if (jsonObj != null) {
                    JSONArray jsonArray_events = jsonObj.getJSONObject("_embedded").getJSONArray("events");
                    for (int i=0; i < jsonArray_events.length(); i++) {
                        names.add(jsonArray_events.getJSONObject(i).getString("name"));
                        descriptions.add(jsonArray_events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("address").getString("line1") + ", " +
                                jsonArray_events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name"));
                        imgUrls.add(jsonArray_events.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url"));
                    }
                }
//                issue retrieving state code when searching only with key word
//                + ", " + jsonArray_events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("stateCode")

                ra.notifyData(names, descriptions, imgUrls);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static class YelpApiBusinessSearch extends AsyncTask<String, Void, String> {                                       // AsyncTask for handling network operations
        @Override
        protected String doInBackground(String... params) {
            String category = params[0];
            String price = params[1];
            String radius = params[2] + "000";
            String sort = params[3];
            String latitude = Double.toString(42.350498);
            String longitude = Double.toString(-71.105400);
            // build complete url
            String url_formatted = "https://api.yelp.com/v3/businesses/search?";                                        // base url
            url_formatted = url_formatted.concat(String.format("latitude=%s&longitude=%s", latitude, longitude));       // add latitude and longitude endpoint
            if (category != null && !category.isEmpty()) {                                                              // add category endpoint
                String endpoint = String.format("&categories=%s", category);
                url_formatted = url_formatted.concat(endpoint);
            }
            if (price != null && !price.isEmpty()) {                                                                    // add price endpoint
                String endpoint = String.format("&price=%s", price);
                url_formatted = url_formatted.concat(endpoint);
            }
            if (radius != null && !radius.isEmpty()) {                                                                  // add radius endpoint
                String endpoint = String.format("&radius=%s", radius);
                url_formatted = url_formatted.concat(endpoint);
            }
            if (sort != null && !sort.isEmpty()) {                                                                      // add sort_by endpoint
                String endpoint = String.format("&sort_by=%s", sort);
                url_formatted = url_formatted.concat(endpoint);
            }
            Log.i("--------->", url_formatted);
            //url_formatted = "https://api.yelp.com/v3/businesses/search?latitude=42.350498&longitude=-71.1054";
            //url_formatted = "https://api.yelp.com/v3/businesses/search?location=Boston";
            // send HTTP GET request
            URL url = null;
            HttpURLConnection urlConnection = null;
            String httpResponse = null;
            try {
                url = new URL(url_formatted);                                                                           // create a new URL object
                urlConnection = (HttpURLConnection) url.openConnection();                                               // open a new HTTP url connection
                urlConnection.setRequestProperty("Authorization", "Bearer " + API_KEY);                                 // set request header
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());                               // get response result from GET request
                httpResponse = IOUtil.toString(in);                                                                     // turn the result into a string for parsing
                Log.i("--------->", httpResponse);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();                                                                             // IMPORTANT: disconnect url connection
            }
            return httpResponse;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);
            JSONObject jsonObject = null;
            // parse json string
            try {
                jsonObject = new JSONObject(result);
                if (jsonObject != null) {
                    JSONArray restaurants = jsonObject.getJSONArray("businesses");
                    for (int i = 0; i < restaurants.length(); i++) {
                        // parse json data
                        JSONObject resto = restaurants.getJSONObject(i);
                        String id = resto.getString("id");
                        String name = resto.getString("name");
                        String image_url = resto.getString("image_url");
                        String is_closed = resto.getString("is_closed");
                        String url = resto.getString("url");
                        String review_count = resto.getString("review_count");
                        JSONArray categories = resto.getJSONArray("categories");
                        String rating  = resto.getString("rating");
                        String latitude = resto.getJSONObject("coordinates").getString("latitude");
                        String longitude = resto.getJSONObject("coordinates").getString("longitude");
                        JSONArray transactions = resto.getJSONArray("transactions");
                        String price = resto.getString("price");
                        JSONObject location = resto.getJSONObject("location");
                        String address1 = location.getString("address1");
                        String address2 = location.getString("address2");
                        String address3 = location.getString("address3");
                        String city = location.getString("city");
                        String zip_code = location.getString("zip_code");
                        String country = location.getString("country");
                        String state = location.getString("state");
                        JSONArray disp_address = location.getJSONArray("display_address");
                        String display_address = disp_address.getString(0) + disp_address.getString(1);
                        String phone = resto.getString("phone");
                        String display_phone = resto.getString("display_phone");
                        String distance = resto.getString("distance");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
