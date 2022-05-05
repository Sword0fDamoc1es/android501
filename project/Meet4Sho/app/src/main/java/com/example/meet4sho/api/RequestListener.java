package com.example.meet4sho.api;

import java.util.List;

// Listens for data from API calls and updates views in activities / fragments
public interface RequestListener<T> {
    public void updateViews(List<T> events);
}
