package com.example.yelpapi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.commons.io.IOUtil;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.*;

public class MainActivity extends AppCompatActivity {
    // views
    private EditText edtCategory;
    private EditText edtPrice;
    private EditText edtRadius;
    private EditText edtSort;
    private Button btnSubmit;

    // variables
    private List<String> ids = new ArrayList<>();

    // constants
    private String API_KEY = "88RR4WnZJtAJeY_z4UA6bR-NhnlmhFk5zYFqxD30lDTrpfa543apNhHmDzum2ElLWI4od6pGjBfMjIoG7T9ReRU8vYaQejY57KwBwKwoj-r9ID9ahOJG4D1d5TRDYnYx";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // instantiate views
        edtCategory = (EditText) findViewById(R.id.edtCategory);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        edtRadius = (EditText) findViewById(R.id.edtRadius);
        edtSort = (EditText) findViewById(R.id.edtSort);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        // view actions
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String category = edtCategory.getText().toString();
                String price = edtPrice.getText().toString();
                String radius = edtRadius.getText().toString();
                String sort = edtSort.getText().toString();
                String[] params = {category, price, radius, sort};
                new YelpApiBusinessSearch().execute(params);
            }
        });
    }

    private class YelpApiBusinessSearch extends AsyncTask<String, Void, String> {                                       // AsyncTask for handling network operations
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
            Intent i = new Intent(getApplicationContext(), SecondActivity.class);                                       // go to a new activity
            i.putExtra("httpResponse", httpResponse);
            startActivity(i);
            return httpResponse;
        }

        //        @Override
//        protected void onPostExecute(String result) {
//            super.onPostExecute(result);
//            //tvSearchResult.setText(result);
//            Log.i("API RESPONSE:", result);
//            JSONObject jsonObj = null;
//            try {
//                jsonObj = new JSONObject(result);
//                if (jsonObj != null) {
//                    JSONArray jsonArray_businesses = jsonObj.getJSONArray("businesses");
//                    for (int i=0; i < jsonArray_businesses.length(); i++) {
//                        ids.add(jsonArray_businesses.getJSONObject(i).getString("id"));
//                    }
////                    tvSearchResult.setText(ids);
//                }
//                new YelpBusinessDetailAPI().execute();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
    }

//    private class YelpBusinessDetailAPI extends AsyncTask<String, String, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            String url_formatted = String.format("https://api.yelp.com/v3/businesses/%s",ids.get(0));
//            URL url = null;
//            HttpURLConnection urlConnection = null;
//            String responseString = null;
//            try {
//                url = new URL(url_formatted);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestProperty("Authorization", "Bearer " + API_KEY);
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                responseString = IOUtil.toString(in);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                urlConnection.disconnect();
//            }
//            return responseString;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            JSONObject jsonObj = null;
//            try {
//                jsonObj = new JSONObject(s);
//                if (jsonObj != null) {
//                    String id = jsonObj.getString("id");
//                    String name = jsonObj.getString("name");
//                    String phone = jsonObj.getString("phone");
//                    String review_count = jsonObj.getString("review_count");
//                    String tvDisplay = String.format("id:%s\nname:%s\nphone:%s\nreview_count:%s", id,name,phone,review_count);
////                    tvSearchResult.setText(tvDisplay);
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}