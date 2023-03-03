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
public class City implements Serializable {
  private static final long serialVersionUID = -6934933601993223786L;

  Long id;
  String name;
  double cityCentreLatitude;
  double cityCentreLongitude;
}
