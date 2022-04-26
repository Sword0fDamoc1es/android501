package com.example.meet4sho.api;

import java.io.Serializable;
import java.util.List;

public class TMEvent extends ApiEvent implements Serializable {
    private String id;
    private String name;
    private String description;
    private String info;
    private String url;
    private List<TMEventImage> images;
    private List<TMEventClassification> classifications;
    private TMEventTime time;
    private TMEventVenue venue;
    // private TMEventSales sales;
    // private String locale;
    // private String seatmapUrl;
    // private String accessibility;


    public TMEvent(String id, String name, String description, String info, String url, List<TMEventImage> images, List<TMEventClassification> classifications, TMEventTime time, TMEventVenue venue) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.info = info;
        this.url = url;
        this.images = images;
        this.classifications = classifications;
        this.time = time;
        this.venue = venue;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<TMEventImage> getImages() {
        return images;
    }

    public void setImages(List<TMEventImage> images) {
        this.images = images;
    }

    public List<TMEventClassification> getClassifications() {
        return classifications;
    }

    public void setClassifications(List<TMEventClassification> classifications) {
        this.classifications = classifications;
    }

    public TMEventTime getTime() {
        return time;
    }

    public void setTime(TMEventTime time) {
        this.time = time;
    }

    public TMEventVenue getVenue() {
        return venue;
    }

    public void setVenue(TMEventVenue venue) {
        this.venue = venue;
    }
}
