package com.example.meet4sho.api;

import java.util.ArrayList;
import java.util.List;


/**
 * This class creates an Object called MGCinema that saves all the info we
 * get back from MovieGlu when we request for a specific movie at a specific cinema
 * Methods: Just set and get methods
 *
 * cinema_id: A cinema's ID as assigned by MovieGlu
 * cinema_name: The name of the cinema
 * cinema_lat: Latitude of the cinema
 * cinema_lng: Longitude of the cinema
 * film_id: The ID of the film we requested as assigned by MovieGlu
 * film_name: The name of the film
 * film_img: URL to the film's movie poster
 * times: An ArrayList of MGTime objects (MGTime is an object containing a movie's start time,
 *      end time, and date of showing at this specfic cinema)
 *
 */

public class MGCinema {
    private String cinema_id;
    private String cinema_name;
    private double cinema_lat;
    private double cinema_lng;
    private double cinema_distance;
    private String film_id;
    private String film_name;
    private String film_img;
    private List<MGTime> times = new ArrayList<>();

    public MGCinema(String cinema_id, String cinema_name, double cinema_lat, double cinema_lng, double cinema_distance, String film_id, String film_name, String film_img, List<MGTime> times) {
        this.cinema_id = cinema_id;
        this.cinema_name = cinema_name;
        this.cinema_lat = cinema_lat;
        this.cinema_lng = cinema_lng;
        this.cinema_distance = cinema_distance;
        this.film_id = film_id;
        this.film_name = film_name;
        this.film_img = film_img;
        this.times = times;
    }

    public void addTime(MGTime time) {
        this.times.add(time);
    }

    public String getCinema_id() {
        return cinema_id;
    }

    public void setCinema_id(String cinema_id) {
        this.cinema_id = cinema_id;
    }

    public String getCinema_name() {
        return cinema_name;
    }

    public void setCinema_name(String cinema_name) {
        this.cinema_name = cinema_name;
    }

    public double getCinema_distance() {
        return cinema_distance;
    }

    public void setCinema_distance(double cinema_distance) {
        this.cinema_distance = cinema_distance;
    }

    public List<MGTime> getTimes() {
        return times;
    }

    public void setTimes(List<MGTime> times) {
        this.times = times;
    }

    public String getFilm_id() {
        return film_id;
    }

    public void setFilm_id(String film_id) {
        this.film_id = film_id;
    }

    public String getFilm_name() {
        return film_name;
    }

    public void setFilm_name(String film_name) {
        this.film_name = film_name;
    }

    public String getFilm_img() {
        return film_img;
    }

    public void setFilm_img(String film_img) {
        this.film_img = film_img;
    }

    public double getCinema_lat() {
        return cinema_lat;
    }

    public void setCinema_lat(double cinema_lat) {
        this.cinema_lat = cinema_lat;
    }

    public double getCinema_lng() {
        return cinema_lng;
    }

    public void setCinema_lng(double cinema_lng) {
        this.cinema_lng = cinema_lng;
    }

}
