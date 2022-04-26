package com.example.meet4sho.api;

import java.util.HashMap;
import java.util.Map;

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
