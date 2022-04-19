package com.example.meet4sho.model;

import android.os.AsyncTask;
import android.util.Log;

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
}
