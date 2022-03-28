package com.example.fandango0;

import com.example.fandango0.model.APIresponse;

public interface onFetchListener {
    void onFetchData(APIresponse apiresponse, String message);
    void onError(String message);
}
