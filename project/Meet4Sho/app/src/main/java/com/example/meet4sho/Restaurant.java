package com.example.meet4sho;

import java.io.Serializable;
import java.util.List;

public class Restaurant implements Serializable {

    public String name;
    public String imgURL;
    public String isClosed;
    public String url;
    public String reviewCount;
    public String rating;
    public String latitude;
    public String longitude;
    public String price;
    public String address1;
    public String address2;
    public String address3;
    public String city;
    public String zipCode;
    public String country;
    public String state;
    public String displayAddress;
    public String phone;
    public String displayPhone;
    public String distance;

    public Restaurant(List<String> info){
        this.name = info.get(0);
        this.imgURL = info.get(1);
        this.isClosed = info.get(2);
        this.url = info.get(3);
        this.reviewCount = info.get(4);
        this.rating = info.get(5);
        this.latitude = info.get(6);
        this.longitude = info.get(7);
        this.price = info.get(8);
        this.address1 = info.get(9);
        this.address2 = info.get(10);
        this.address3 = info.get(11);
        this.city = info.get(12);
        this.zipCode = info.get(13);
        this.country = info.get(14);
        this.state = info.get(15);
        this.displayAddress = info.get(16);
        this.phone = info.get(17);
        this.displayPhone = info.get(18);
        this.distance = info.get(19);

    }

}
