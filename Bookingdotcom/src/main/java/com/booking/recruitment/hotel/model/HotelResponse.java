package com.booking.recruitment.hotel.model;

import javax.persistence.Entity;
import java.io.Serializable;


public class HotelResponse implements Serializable {
    private Long id;

    private String name;
    private Double rating;

    private String address;
    private double latitude;
    private double longitude;

    private City city;

    public static HotelResponse getHotelResponse(Hotel hotel) {
        HotelResponse hotelResponse=new HotelResponse(hotel.getId(),
                hotel.getName(), hotel.getRating(), hotel.getAddress(), hotel.getLatitude(), hotel.getLongitude(), hotel.getCity());
        return hotelResponse;
    }

    public HotelResponse(Long id, String name, Double rating, String address, double latitude, double longitude, City city) {
        this.id = id;
        this.name = name;
        this.rating = rating;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }
}
