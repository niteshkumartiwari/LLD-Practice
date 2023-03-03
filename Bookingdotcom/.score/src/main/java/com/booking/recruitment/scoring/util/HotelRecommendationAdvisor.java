package com.booking.recruitment.scoring.util;

import com.booking.recruitment.scoring.model.Hotel;

import static java.lang.Math.max;
import static java.lang.Math.min;

public final class HotelRecommendationAdvisor {
  private HotelRecommendationAdvisor() {}

  public static final double MAX_DISTANCE_VALUE = 5.0;

  /**
   * Calculates a hotel "recommended score" based on distance and rating
   *
   * @param hotel
   * @return A value between 0 and 1, 0=not recommended at all and 1=max recommendation
   */
  public static double getRecommendedScore(Hotel hotel) {
    double distanceScore =
        max(
            1 - SearchDistanceAdvisor.distanceFromHotelToCityCentre(hotel) / MAX_DISTANCE_VALUE,
            0.0);
    double ratingScore = (hotel.getRating() != null ? hotel.getRating() : 0) / 10.0;

    return min(distanceScore * 0.25 + ratingScore * 0.75, 1.0);
  }
}
