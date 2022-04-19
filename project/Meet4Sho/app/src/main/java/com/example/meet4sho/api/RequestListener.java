package com.example.meet4sho.api;

import java.util.List;

public interface RequestListener<T> {
    public void updateViews(List<T> events);
}
