package com.example.meet4sho.api;

import android.util.Log;

import org.apache.commons.io.IOUtil;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class HttpUtils {
    /**
     * This method sends an HTTP GET request and returns a JSON object
     * @param url   use this url to send an HTTP GET request
     * @return      a JSON object that embodies all the data from response
     */
    public static JSONObject sendRequest(String url) {
        JSONObject root = sendRequest(url, null);
        return root;
    }

    /**
     * This method sends an HTTP GET request and returns a JSON object
     * @param url       request url
     * @param props     request properties
     * @return          a JSON object that embodies all the data from response
     */
    public static JSONObject sendRequest(String url, Map<String, String> props) {
        URL reqURL = null;
        HttpURLConnection urlConnection = null;
        JSONObject root = null;
        try {
            reqURL = new URL(url);                                                      // Step 1: create a URL instance
            urlConnection = (HttpURLConnection) reqURL.openConnection();                // Step 2: open an http connection
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());   // Step 3: get the response
            //JSONParser parser = new JSONParser();                                       // Step 4: create a JSON parser
            //root = (JSONObject) parser.parse(new InputStreamReader(in));                // Step 5: convert the input stream into a big json object
            String responseString = IOUtil.toString(in);
            Log.i("----->", responseString);
            root = new JSONObject(responseString);
        } catch (IOException | JSONException e) {      // handle exceptions
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();                 // IMPORTANT!!!: disconnect http connection
        }
        return root;
    }

    /**
     * This method is used to build a complete url path
     * @param baseURL
     * @param filter
     * @return
     */
    public static  String buildURL(String baseURL, SearchFilter filter) {
        return baseURL + filter.toString();
    }




    // --------------------------------------------------------------------------------------------------------------------------------------------
    public static String sendRequestTest(String url) {
        String result = sendRequestTest(url, null);
        return result;
    }

    public static String sendRequestTest(String url, Map<String, String> props) {
        URL reqURL = null;
        HttpURLConnection urlConnection = null;
        String result = null;
        try {
            reqURL = new URL(url);                                                      // Step 1: create a URL instance
            urlConnection = (HttpURLConnection) reqURL.openConnection();                // Step 2: open an http connection
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());   // Step 3: get the response
            result = IOUtil.toString(in);
        } catch (IOException e) {      // handle exceptions
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();                 // IMPORTANT!!!: disconnect http connection
        }
        return result;
    }
}
