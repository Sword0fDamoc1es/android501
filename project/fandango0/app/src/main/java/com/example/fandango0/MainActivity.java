package com.example.fandango0;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    public TextView textView;
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

    }
    public void onClick(View view){
        new TMDBService().execute();
    }
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
//    private final onFetchListener listener = new onFetchListener() {
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