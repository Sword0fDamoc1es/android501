package com.example.meet4sho.api;

import java.io.Serializable;
import java.util.List;

/**
 * This class creates an Object called TMEvent that saves all the info pertaining
 * to a specific event when we call for all the events near a location via Ticketmaster
 * Methods: Just set and get methods
 *
 * id: An event's ID as assigned by Ticketmaster
 * name: Name of the event
 * description: A description of the event
 * info: Info about the event
 * url: URL to the event's webpage
 * images: An ArrayList of TMEventImage Objects (Which contain a URL to the poster, and the width and height of the poster)
 * classifications: An ArrayList of TMEventClassification Objects (Which contain an event's segment, genre, and subgenre)
 * time: A TMEventTime Object (Which contains an events start time, end time, and whether it spans multiple days)
 * venue: A TMEventVenue Object (Which contains a venue's (name, address, latitude, and longitude)
 *
 */

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
