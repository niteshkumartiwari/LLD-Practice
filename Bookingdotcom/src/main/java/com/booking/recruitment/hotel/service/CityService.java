package com.booking.recruitment.hotel.service;

import com.booking.recruitment.hotel.model.City;

import java.util.List;

public interface CityService {
  List<City> getAllCities();

  City getCityById(Long id);

  City createCity(City city);
}
