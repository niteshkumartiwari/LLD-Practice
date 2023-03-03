package com.booking.recruitment.hotel.model;

public class HotelDistance {
    private Hotel hotel;
    private double distance;

    public HotelDistance(Hotel hotel, double distance) {
        this.hotel = hotel;
        this.distance = distance;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public double getDistance() {
        return distance;
    }
}
