package com.booking.recruitment.scoring.fixtures.rest;

import com.booking.recruitment.scoring.model.City;
import com.booking.recruitment.scoring.model.Hotel;

import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;
import static org.apache.http.HttpStatus.SC_CREATED;

public class InventoryFixtures {
  private InventoryFixtures() {}

  public static City createAnyCity() {
    return createCity(City.builder().name("Test City").build());
  }

  public static City createCity(City city) {
    return given()
        .request()
        .contentType(JSON)
        .body(city)
        .post("/city")
        .then()
        .statusCode(SC_CREATED)
        .extract()
        .response()
        .as(City.class);
  }

  public static Hotel createAnyHotel(City city) {
    return createHotel(Hotel.builder().name("Test Hotel").city(city).build());
  }

  public static Hotel createAnyHotel() {
    return createAnyHotel(createAnyCity());
  }

  public static Hotel createHotel(Hotel hotel) {
    return given()
        .request()
        .contentType(JSON)
        .body(hotel)
        .post("/hotel")
        .then()
        .statusCode(SC_CREATED)
        .extract()
        .response()
        .as(Hotel.class);
  }
}
