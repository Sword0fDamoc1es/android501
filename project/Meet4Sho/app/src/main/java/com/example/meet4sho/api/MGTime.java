package com.example.meet4sho.api;

import java.io.Serializable;

/**
 * This class creates an Object called MGTime that saves all the info we
 * get back from MovieGlu when we request for a specific movie's showtime at a specific cinema
 * Methods: Just set and get methods
 *
 * startTime: Start time of the movie at a specific cinema
 * endTime: End time of a movie at a specific cinema
 * date: Date of the showing of the movie at a specific cinema
 */

public class MGTime implements Serializable {
    private String startTime;
    private String endTime;
    private String date;

    public MGTime(String startTime, String endTime, String date) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
