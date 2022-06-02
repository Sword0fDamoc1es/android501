package com.example.meet4sho.api;

/**
 * An object that contains a Ticketmaster event's start time, end time, and whether it spans multiple days
 */
public class TMEventTime {
    private String startDateTime;
    private String endDateTime;
    private boolean spanMultipleDays;

    public TMEventTime(String startDateTime, String endDateTime, boolean spanMultipleDays) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.spanMultipleDays = spanMultipleDays;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(String startDateTime) {
        this.startDateTime = startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(String endDateTime) {
        this.endDateTime = endDateTime;
    }

    public boolean isSpanMultipleDays() {
        return spanMultipleDays;
    }

    public void setSpanMultipleDays(boolean spanMultipleDays) {
        this.spanMultipleDays = spanMultipleDays;
    }
}
