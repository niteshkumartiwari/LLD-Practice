package com.booking.recruitment.scoring.util;

import com.booking.recruitment.scoring.model.City;
import com.booking.recruitment.scoring.model.Hotel;

public final class SearchDistanceAdvisor {
  private SearchDistanceAdvisor() {}

  public static double distanceFromHotelToCityCentre(Hotel hotel) {
    City city = hotel.getCity();
    return Haversine.haversine(
        hotel.getLatitude(),
        hotel.getLongitude(),
        city.getCityCentreLatitude(),
        city.getCityCentreLongitude());
  }
}
