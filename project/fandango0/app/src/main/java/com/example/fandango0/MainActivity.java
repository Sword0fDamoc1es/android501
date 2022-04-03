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

        new TMDBService().execute();
    }
    private class TMDBService extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            String url_formatted = String.format("https://api.themoviedb.org/3/movie/%s?api_key=%s","popular",API_KEY);
            URL url = null;
            HttpURLConnection urlConnection = null;
            String responseString = null;
            try {
                url = new URL(url_formatted);
                urlConnection = (HttpURLConnection) url.openConnection();
                // the word : popular is the search keyword.
                // change it using other keyword for other functions.
                urlConnection.setRequestProperty("Authorization", "Bearer " + API_KEY);
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
            try {
                jsonObj = new JSONObject(result);
                if (jsonObj != null) {
                    JSONArray jsonArray_results = jsonObj.getJSONArray("results");
                    for (int i=0; i < jsonArray_results.length(); i++) {
                        ids.add(jsonArray_results.getJSONObject(i).getString("id"));
                    }
//                    tvSearchResult.setText(ids);
                }
                new TMDBResultsDetailAPI().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        private class TMDBResultsDetailAPI extends AsyncTask<String, String, String> {
            @Override
            protected String doInBackground(String... strings) {
                String url_formatted = String.format("https://api.themoviedb.org/3/movie/%s?api_key=c5a45837e9632229e51e8455548838d7",ids.get(0));
                URL url = null;
                HttpURLConnection urlConnection = null;
                String responseString = null;
                try {
                    url = new URL(url_formatted);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Authorization", "Bearer " + API_KEY);
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
                        String id = jsonObj.getString("id");
                        String original_title = jsonObj.getString("original_title");
                        String popularity = jsonObj.getString("popularity");
                        String release_date = jsonObj.getString("release_date");
                        String tvDisplay = String.format("id:%s\noriginal_title:%s\npopularity:%s\nrelease_date:%s", id,original_title,popularity,release_date);
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