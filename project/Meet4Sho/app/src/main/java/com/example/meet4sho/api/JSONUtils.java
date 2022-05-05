package com.example.meet4sho.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A Util Class that contains methods for getting JSON elements and catching JSON exceptions
 */
public class JSONUtils {
    public static JSONObject getJSONObject(JSONArray jsonArray, int index) {
        JSONObject result = new JSONObject();
        try {
            result = jsonArray.getJSONObject(index);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static JSONObject getJSONObject(JSONObject jsonObject, String name) {
        JSONObject result = new JSONObject();
        try {
            result = jsonObject.getJSONObject(name);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static String getString(JSONObject jsonObject, String name) {
        String result = "";
        try {
            result = jsonObject.getString(name);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static JSONArray getJSONAray(JSONObject jsonObject, String name) {
        JSONArray result = new JSONArray();
        try {
            result = jsonObject.getJSONArray(name);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static int getInt (JSONObject jsonObject, String name) {
        int result = 0;
        try {
            result = jsonObject.getInt(name);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return result;
    }

    public static boolean getBoolean(JSONObject jsonObject, String name) {
        boolean result = false;
        try {
            result = jsonObject.getBoolean(name);
        } catch (JSONException e) {
            //e.printStackTrace();
        }
        return result;
    }
}
