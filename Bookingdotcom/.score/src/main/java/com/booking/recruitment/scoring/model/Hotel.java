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
public class Hotel implements Serializable {
  private static final long serialVersionUID = -4259538876372899909L;

  Long id;

  String name;
  Double rating;
  City city;

  String address;
  double latitude;
  double longitude;
  boolean deleted = false;
}
