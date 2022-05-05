package com.example.meet4sho.api;

import java.util.HashMap;
import java.util.Map;

/**
 * A class that helps with putting certain filters on our API calls
 * Ex: Yelp requires a location via a filter in the URL "location=Boston"
 *     In order to add this to the end of the Yelp's URL endpoint
 *     all we do is create a SearchFilter and call add(location, Boston)
 *     Then the YelpRequest class adds this filter to the URL endpoint when it
 *     calls the Yelp API.
 */

public class SearchFilter {
    private Map<String, String> filters = new HashMap<>();

    public void add(String key, String value){
        filters.put(key, value);
    }
    
    public String get(String key){
        return filters.get(key);
    }

    @Override
    public String toString() {
        String result = "";
        for (String key: filters.keySet()) {
            result += String.format("&%s=%s", key, filters.get(key));
        }
//        if (result != "") result = result.substring(1);
        return result;
    }
}
