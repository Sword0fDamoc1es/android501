package com.example.fandango0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

import com.example.fandango0.model.APIresponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public Button button;
    public Button button2;
    public Button button3;
    public TextView textView;
    public TextView textView2;
    public TextView textView3;
    public EditText edittext;
////    public String zip = "?query=town center";
////    public String zip = "66062"
//    public String country_abbrev = "US";
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        button = findViewById(R.id.button);
//        textView = findViewById(R.id.textView);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ResponseManager mg = new ResponseManager(MainActivity.this);
//                mg.getReplyByZip(listener,country_abbrev);
//            }
//        });
//    }
    private TextView tvSearchResult;
    // marve's key
    private String API_KEY = "c5a45837e9632229e51e8455548838d7";
    private List<String> ids = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView =(TextView) findViewById(R.id.textView);
        textView2 =(TextView) findViewById(R.id.textView2);
        textView3 =(TextView) findViewById(R.id.textView3);
        edittext = (EditText) findViewById(R.id.edittext);

    }
    public void onClick1(View view){
        new TMDBService().execute();
    }
    public void onClick2(View view) { new MovieGluTheatureSearch().execute(); }
    public void onClick3(View view) { new MovieGluTheatureSearchID().execute(); }
    public void onClick4(View view) { new MovieGluCinemaID2Film().execute(); }
    private class TMDBService extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            // curl "https://api-gate2.movieglu.com/filmsComingSoon/?n=1" -H "api-version: [VERSION]" -H "Authorization: [BASIC AUTHENTICATION]" -H "x-api-key: [API-KEY]" -H "device-datetime: [DATE-TIME]" -H "territory: [TERRITORY]" -H "client: [USERNAME]"
//            String url_formatted = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s","popular",API_KEY);
            String url_formatted = String.format("https://api-gate2.movieglu.com/filmLiveSearch/?n=2&query=Doctor+Strange+in+the+Multiverse+of+Madness");
            URL url = null;
            HttpURLConnection urlConnection = null;
            String responseString = null;
            try {
                url = new URL(url_formatted);
                urlConnection = (HttpURLConnection) url.openConnection();
                // the word : popular is the search keyword.
                // change it using other keyword for other functions.
                urlConnection.setRequestProperty("api-version", "v200");
                urlConnection.setRequestProperty("territory", "US");
                urlConnection.setRequestProperty("authorization", "Basic Qk9TVF8xOjhLQW1UaXVScEt0Qg==");
                urlConnection.setRequestProperty("x-api-key", "usDFRasvcGtTk4dtZsrE9ayZwtwkG9cYV0mqD000");
                urlConnection.setRequestProperty("device-datetime", "2022-04-04T21:23:51.027Z");
                urlConnection.setRequestProperty("client", "BOST_1");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = IOUtil.toString(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //tvSearchResult.setText(result);
            Log.i("API RESPONSE:", result);
            JSONObject jsonObj = null;
            ids.clear();
            String ttt = "";
            try {
                jsonObj = new JSONObject(result);
                if (jsonObj != null) {
                    System.out.println("here1");
//                    JSONArray jsonArray_results = jsonObj.getJSONArray("results");
                    JSONArray jsonArray_results = jsonObj.getJSONArray("films");
                    for (int i=0; i < jsonArray_results.length(); i++) {
//                        if(i == 0){
//                            ttt = jsonArray_results.getJSONObject(i).getString("film_id");
//                        }
                        ids.add(jsonArray_results.getJSONObject(i).getString("film_id"));
                    }
//                    tvSearchResult.setText(ids);
                }
//                textView.setText(ttt);
                 new TMDBResultsDetailAPI().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }



        private class TMDBResultsDetailAPI extends AsyncTask<String, String, String> {
            // filmDetails/?film_id=12345
            @Override
            protected String doInBackground(String... strings) {
//                String url_formatted = String.format("https://api.themoviedb.org/3/movie/%s?api_key=c5a45837e9632229e51e8455548838d7",ids.get(0));
                String url_formatted = String.format("https://api-gate2.movieglu.com/filmDetails/?film_id=%s",ids.get(0));
                URL url = null;
                HttpURLConnection urlConnection = null;
                String responseString = null;
                try {
                    url = new URL(url_formatted);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("api-version", "v200");
                    urlConnection.setRequestProperty("territory", "US");
                    urlConnection.setRequestProperty("authorization", "Basic Qk9TVF8xOjhLQW1UaXVScEt0Qg==");
                    urlConnection.setRequestProperty("x-api-key", "usDFRasvcGtTk4dtZsrE9ayZwtwkG9cYV0mqD000");
                    urlConnection.setRequestProperty("device-datetime", "2022-04-04T21:23:51.027Z");
                    urlConnection.setRequestProperty("client", "BOST_1");
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseString = IOUtil.toString(in);
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                }
                return responseString;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSONObject jsonObj = null;
                try {
                    jsonObj = new JSONObject(s);
                    if (jsonObj != null) {
                        String id = jsonObj.getString("film_id");
                        String original_title = jsonObj.getString("film_name");
//                        String popularity = jsonObj.getString("duration");
                        String release_date = jsonObj.getString("duration_mins");
                        String tvDisplay = String.format("id:%s\noriginal_title:%s\nrelease_date:%s", id,original_title,release_date);
                        textView.setText(tvDisplay);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private class MovieGluTheatureSearch extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // curl "https://api-gate2.movieglu.com/filmsComingSoon/?n=1" -H "api-version: [VERSION]" -H "Authorization: [BASIC AUTHENTICATION]" -H "x-api-key: [API-KEY]" -H "device-datetime: [DATE-TIME]" -H "territory: [TERRITORY]" -H "client: [USERNAME]"
//            String url_formatted = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s","popular",API_KEY);
            String url_formatted = String.format("https://api-gate2.movieglu.com/cinemasNearby/?n=5");
            URL url = null;
            HttpURLConnection urlConnection = null;
            String responseString = null;
            try {
                url = new URL(url_formatted);
                urlConnection = (HttpURLConnection) url.openConnection();
                // the word : popular is the search keyword.
                // change it using other keyword for other functions.
                urlConnection.setRequestProperty("api-version", "v200");
                urlConnection.setRequestProperty("territory", "US");
                urlConnection.setRequestProperty("authorization", "Basic Qk9TVF8xOjhLQW1UaXVScEt0Qg==");
                urlConnection.setRequestProperty("x-api-key", "usDFRasvcGtTk4dtZsrE9ayZwtwkG9cYV0mqD000");
                urlConnection.setRequestProperty("device-datetime", "2022-04-04T21:23:51.027Z");
                // important:
                urlConnection.setRequestProperty("geolocation", "42.3611;-71.0570");
                urlConnection.setRequestProperty("client", "BOST_1");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = IOUtil.toString(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //tvSearchResult.setText(result);
            Log.i("API RESPONSE:", result);
            JSONObject jsonObj = null;
            String ttt = "";
            ArrayList<String> cname = new ArrayList<String>();
            ids.clear();
            try {
                jsonObj = new JSONObject(result);
                if (jsonObj != null) {
                    System.out.println("here1");
//                    JSONArray jsonArray_results = jsonObj.getJSONArray("results");
                    JSONArray jsonArray_results = jsonObj.getJSONArray("cinemas");
                    for (int i = 0; i < jsonArray_results.length(); i++) {
//                        if(i == 0){
//                            ttt = jsonArray_results.getJSONObject(i).getString("film_id");
//                        }
                        cname.add(jsonArray_results.getJSONObject(i).getString("cinema_name"));
                        ids.add(jsonArray_results.getJSONObject(i).getString("cinema_id"));
                    }
//                    tvSearchResult.setText(ids);
                }
                textView2.setText(cname.toString());
//                    new TMDBResultsDetailAPI().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class MovieGluTheatureSearchID extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // curl "https://api-gate2.movieglu.com/filmsComingSoon/?n=1" -H "api-version: [VERSION]" -H "Authorization: [BASIC AUTHENTICATION]" -H "x-api-key: [API-KEY]" -H "device-datetime: [DATE-TIME]" -H "territory: [TERRITORY]" -H "client: [USERNAME]"
//            String url_formatted = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s","popular",API_KEY);
            String url_formatted = String.format("https://api-gate2.movieglu.com/cinemasNearby/?n=5");
            URL url = null;
            HttpURLConnection urlConnection = null;
            String responseString = null;
            try {
                url = new URL(url_formatted);
                urlConnection = (HttpURLConnection) url.openConnection();
                // the word : popular is the search keyword.
                // change it using other keyword for other functions.
                urlConnection.setRequestProperty("api-version", "v200");
                urlConnection.setRequestProperty("territory", "US");
                urlConnection.setRequestProperty("authorization", "Basic Qk9TVF8xOjhLQW1UaXVScEt0Qg==");
                urlConnection.setRequestProperty("x-api-key", "usDFRasvcGtTk4dtZsrE9ayZwtwkG9cYV0mqD000");
                urlConnection.setRequestProperty("device-datetime", "2022-04-04T21:23:51.027Z");
                // important:
                urlConnection.setRequestProperty("geolocation", "42.3611;-71.0570");
                urlConnection.setRequestProperty("client", "BOST_1");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = IOUtil.toString(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //tvSearchResult.setText(result);
            Log.i("API RESPONSE:", result);
            JSONObject jsonObj = null;
            String ttt = "";
            ArrayList<String> cname = new ArrayList<String>();
            try {
                jsonObj = new JSONObject(result);
                if (jsonObj != null) {
                    System.out.println("here1");
//                    JSONArray jsonArray_results = jsonObj.getJSONArray("results");
                    JSONArray jsonArray_results = jsonObj.getJSONArray("cinemas");
                    for (int i = 0; i < jsonArray_results.length(); i++) {
//                        if(i == 0){
//                            ttt = jsonArray_results.getJSONObject(i).getString("film_id");
//                        }
                        cname.add(jsonArray_results.getJSONObject(i).getString("cinema_name"));
                        ids.add(jsonArray_results.getJSONObject(i).getString("cinema_id"));
                    }
//                    tvSearchResult.setText(ids);
                }
                textView3.setText(ids.toString());
//                    new TMDBResultsDetailAPI().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private class MovieGluCinemaID2Film extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // curl "https://api-gate2.movieglu.com/filmsComingSoon/?n=1" -H "api-version: [VERSION]" -H "Authorization: [BASIC AUTHENTICATION]" -H "x-api-key: [API-KEY]" -H "device-datetime: [DATE-TIME]" -H "territory: [TERRITORY]" -H "client: [USERNAME]"
//            String url_formatted = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s","popular",API_KEY);
//            String url_formatted = String.format("https://api-gate2.movieglu.com/cinemasNearby/?n=5");
            String cid = edittext.getText().toString();
            String url_formatted = String.format("https://api-gate2.movieglu.com/cinemaShowTimes/?cinema_id=%s&date=2022-04-05&sort=popularity",cid);
            URL url = null;
            HttpURLConnection urlConnection = null;
            String responseString = null;
            try {
                url = new URL(url_formatted);
                urlConnection = (HttpURLConnection) url.openConnection();
                // the word : popular is the search keyword.
                // change it using other keyword for other functions.
                urlConnection.setRequestProperty("api-version", "v200");
                urlConnection.setRequestProperty("territory", "US");
                urlConnection.setRequestProperty("authorization", "Basic Qk9TVF8xOjhLQW1UaXVScEt0Qg==");
                urlConnection.setRequestProperty("x-api-key", "usDFRasvcGtTk4dtZsrE9ayZwtwkG9cYV0mqD000");
                urlConnection.setRequestProperty("device-datetime", "2022-04-05T21:23:51.027Z");
                // important:
                urlConnection.setRequestProperty("geolocation", "42.3611;-71.0570");
                urlConnection.setRequestProperty("client", "BOST_1");
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                responseString = IOUtil.toString(in);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                urlConnection.disconnect();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //tvSearchResult.setText(result);
            Log.i("API RESPONSE:", result);
            JSONObject jsonObj = null;
            String ttt = "";
            ArrayList<String> cname = new ArrayList<String>();
            try {
                jsonObj = new JSONObject(result);
                if (jsonObj != null) {
                    System.out.println("here1");
//                    JSONArray jsonArray_results = jsonObj.getJSONArray("results");
                    JSONObject jsonArray_results_c = jsonObj.getJSONObject("cinema");
                    String str1 = jsonArray_results_c.getString("cinema_id");
                    String str2 = jsonArray_results_c.getString("cinema_name");
                    textView2.setText(str1+" "+str2);
                    JSONArray jsonArray_results = jsonObj.getJSONArray("films");
                    for (int i = 0; i < jsonArray_results.length(); i++) {
//                        if(i == 0){
//                            ttt = jsonArray_results.getJSONObject(i).getString("film_id");
//                        }
                        cname.add(jsonArray_results.getJSONObject(i).getString("film_name"));
//                        ids.add(jsonArray_results.getJSONObject(i).getString("cinema_id"));
                    }
                    textView.setText(cname.toString());
//                    tvSearchResult.setText(ids);
                }
//                textView3.setText(ids.toString());
//                    new TMDBResultsDetailAPI().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    //    p
//
//
//    rivate final onFetchListener listener = new onFetchListener() {
//        @Override
//        public void onFetchData(APIresponse apiresponse, String message) {
//            if(apiresponse == null){
//                Toast.makeText(MainActivity.this,"nothing",Toast.LENGTH_SHORT).show();
//                return;
//            }
//            showData(apiresponse);
//        }
//
//        @Override
//        public void onError(String message) {
//
//        }
//    };
//    private void showData(APIresponse apiresponse){
//        textView.setText("count: " + apiresponse.getCount());
//    }
}