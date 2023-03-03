package com.booking.recruitment.scoring.fixtures.rest;

import com.booking.recruitment.scoring.model.City;
import com.booking.recruitment.scoring.model.Hotel;

import java.util.List;

public class RatingReportFixtures {
  private RatingReportFixtures() {}

  public static void createHotelSample(City city, List<Double> ratings) {
    for (Double rating : ratings) {
      InventoryFixtures.createHotel(Hotel.builder().city(city).rating(rating).build());
    }
  }
}
