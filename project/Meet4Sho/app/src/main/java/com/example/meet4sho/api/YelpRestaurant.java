package com.example.meet4sho.api;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

// Class of Yelp restaurant info
public class YelpRestaurant extends ApiEvent implements Serializable {

    private String id;
    private String name;
    private String image_url;
    private String is_closed;
    private String url;
    private String review_count;
    private JSONArray categories;
    private String rating;
    private String latitude;
    private String longitude;
    private JSONArray transactions;
    private String price;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String zip_code;
    private String country;
    private String state;
    private String display_address;
    private String phone;
    private String display_phone;
    private String distance;

    public YelpRestaurant(String id, String name, String image_url, String is_closed, String url, String review_count
            , JSONArray categories, String rating, String latitude, String longitude, JSONArray transactions,
                          String price, String address1, String address2, String address3, String city, String zip_code,
                          String country, String state, String display_address, String display_phone, String phone, String distance) {
        this.id = id;
        this.name = name;
        this.image_url = image_url;
        this.is_closed = is_closed;
        this.url = url;
        this.review_count = review_count;
        this.categories = categories;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.transactions = transactions;
        this.price = price;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.display_address = display_address;
        this.city = city;
        this.zip_code = zip_code;
        this.country = country;
        this.state = state;
        this.phone = phone;
        this.display_phone = display_phone;
        this.distance = distance;

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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getRating() { return rating; }

    public void setRating(String rating) { this.rating = rating; }

    public String getLatitude() { return latitude; }

    public void setLatitude(String latitude) { this.latitude = latitude; }

    public String getLongitude() { return longitude; }

    public void setLongitude(String longitude) { this.longitude = longitude; }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }

    public String getDisplay_address() { return display_address; }

    public void setDisplay_address(String display_address) { this.display_address = display_address; }

    public String getDisplay_phone() { return display_phone; }

    public void setDisplay_phone(String display_phone) { this.display_phone = display_phone; }

    public String getDistance() { return distance; }

    public void setDistance(String distance) { this.distance = distance; }

    public String getIs_closed() { return is_closed; }

    public void setIs_closed(String is_closed) { this.is_closed = is_closed; };

}
