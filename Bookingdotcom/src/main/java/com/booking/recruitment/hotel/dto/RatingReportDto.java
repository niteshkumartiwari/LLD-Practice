package com.booking.recruitment.hotel.dto;

public class RatingReportDto {
  private int numberOfRatings;
  private Double averageRating;

  public RatingReportDto() {}

  public RatingReportDto(int numberOfRatings, Double averageRating) {
    this.numberOfRatings = numberOfRatings;
    this.averageRating = averageRating;
  }

  public int getNumberOfRatings() {
    return numberOfRatings;
  }

  public void setNumberOfRatings(int numberOfRatings) {
    this.numberOfRatings = numberOfRatings;
  }

  public Double getAverageRating() {
    return averageRating;
  }

  public void setAverageRating(Double averageRating) {
    this.averageRating = averageRating;
  }
}
