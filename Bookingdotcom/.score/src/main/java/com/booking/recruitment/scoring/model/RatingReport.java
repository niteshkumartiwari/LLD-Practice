package com.booking.recruitment.scoring.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingReport implements Serializable {
  private static final long serialVersionUID = 7627465845556623173L;

  private int numberOfRatings;
  private Double averageRating;
}
