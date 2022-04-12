package com.example.meet4sho;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class TM_MainActivity extends AppCompatActivity {
    // views
    private EditText etCity, etKeyword;
    private Button btSubmit, btnMore;
    private TextView tvSearchResult;
    private RecyclerView rvEvents;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;

    // constants
    private final String TM_API_KEY = "PqFUIagPA9p1AMWB2SeEF9QFyhGnG0P7";
    private final String LOG_TAG = "[MainActivity]";

    private int pageNum = 1;

    private List<String> ids = new ArrayList<>();
    private List<String> names = new ArrayList<>();
    private List<String> descriptions = new ArrayList<>();
    private List<String> imageURLs = new ArrayList<>();

    private TM_RecyclerAdapter ra;

    private int startingIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tm_mainscreen);

        // init views
        etCity = (EditText) findViewById(R.id.etCity);
        etKeyword = (EditText) findViewById(R.id.etKeyword);
        btSubmit = (Button) findViewById(R.id.btSubmit);
        btnMore = (Button) findViewById(R.id.btnMore);
        rvEvents = (RecyclerView) findViewById(R.id.rvEvents);

        if(getIntent().getExtras()!=null){
            Bundle extras = getIntent().getExtras();
            names = extras.getStringArrayList("names");
            descriptions = extras.getStringArrayList("descriptions");
            imageURLs = extras.getStringArrayList("urls");

            ra = new TM_RecyclerAdapter(TM_MainActivity.this, names, descriptions, imageURLs);
            rvEvents.setAdapter(ra);
            rvEvents.setLayoutManager(new LinearLayoutManager(TM_MainActivity.this));

        }else{
            ra = new TM_RecyclerAdapter(TM_MainActivity.this, names, descriptions, imageURLs);
            rvEvents.setAdapter(ra);
            rvEvents.setLayoutManager(new LinearLayoutManager(TM_MainActivity.this));
        }


        // view actions
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
                names.clear();
                descriptions.clear();
                imageURLs.clear();
                pageNum = 1;
                String city = etCity.getText().toString();
                String keyword = etKeyword.getText().toString();
                new TicketMaster().execute(city, keyword);
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pageNum++;
               String city = etCity.getText().toString();
               String keyword = etKeyword.getText().toString();
               new TicketMaster().execute(city, keyword);
            }
        });


    }

    /**
     * TicketMaster is the class that creates a URL connection so
     * that we may send and retrieve requests from TicketMaster
     */
    private class TicketMaster extends AsyncTask<String, String, String> {

        /**
         * 1.) Create a formatted URL string with the keyword now_playing
         *     and the passed in API key and region code
         * 2.) Create a URL connection from the formatted URL string
         */
        @Override
        protected String doInBackground(String... params) {
            String city = params[0];
            String keyword = params[1];
            String page = Integer.toString(pageNum);
            String size = Integer.toString(8);
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
                                jsonArray_events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("city").getString("name") + ", " +
                                jsonArray_events.getJSONObject(i).getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0).getJSONObject("state").getString("stateCode"));
                        imageURLs.add(jsonArray_events.getJSONObject(i).getJSONArray("images").getJSONObject(0).getString("url"));
                    }
                }

                ra.notifyData(names, descriptions, imageURLs);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                TM_MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                TM_MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                double lat = locationGPS.getLatitude();
                double longi = locationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                Toast.makeText(this, latitude + "," + longitude, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
