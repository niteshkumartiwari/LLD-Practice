package com.booking.recruitment.scoring.util;

import java.util.List;
import java.util.Objects;

public class RatingAverageCalculator {
  private RatingAverageCalculator() {}

  public static double getRatingAverage(List<Double> ratings) {
    return ratings.stream()
        .filter(Objects::nonNull)
        .mapToDouble(Double::doubleValue)
        .average()
        .orElseThrow(() -> new IllegalArgumentException("List of doubles provided is not valid"));
  }
}
