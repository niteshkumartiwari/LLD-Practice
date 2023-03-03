package com.booking.recruitment.scoring;

import com.booking.recruitment.scoring.extensions.RESTExtension;
import com.booking.recruitment.scoring.fixtures.rest.InventoryFixtures;
import com.booking.recruitment.scoring.model.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;
import java.util.stream.Stream;

import static io.restassured.RestAssured.delete;
import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith({RESTExtension.class})
class HotelInventoryTest {
  @Test
  void whenHotelRequestedThenCorrectOneIsReturned() {
    Hotel hotel = InventoryFixtures.createAnyHotel();
    get("/hotel/" + hotel.getId())
        .then()
        .statusCode(SC_OK)
        .body("id", equalTo(hotel.getId().intValue()));
  }

  @Test
  void statusCode404WhenNonExistentHotelRequested() {
    whenHotelRequestedThenCorrectOneIsReturned();
    get("/hotel/-1").then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void whenRequestingDeletionHotelIsSoftDeleted() {
    Hotel newHotel = InventoryFixtures.createAnyHotel();
    Optional<Hotel> optionalApiHotel = getHotelById(newHotel.getId());
    assertThat(optionalApiHotel.isPresent(), is(true));

    newHotel = optionalApiHotel.get();
    assertThat(newHotel.isDeleted(), is(false));

    delete("/hotel/" + newHotel.getId());

    // It's possible that the hotel is returned with property deleted = true, or that it now returns
    // a 404 - no hotel
    getHotelById(newHotel.getId()).ifPresent(hotel -> assertThat(hotel.isDeleted(), is(true)));
  }

  @Test
  void deletedHotelNotPresentWhenRequestingAllHotels() {
    whenRequestingDeletionHotelIsSoftDeleted();
    Hotel newHotel = InventoryFixtures.createAnyHotel();
    assertHotelPresent(true, newHotel);

    delete("/hotel/" + newHotel.getId());

    assertHotelPresent(false, newHotel);
  }

  @Test
  void statusCodeSuccessWhenDeletingHotel() {
    Hotel newHotel = InventoryFixtures.createAnyHotel();
    delete("/hotel/" + newHotel.getId()).then().statusCode(anyOf(is(SC_OK), is(SC_NO_CONTENT)));
  }

  @Test
  void statusCode404WhenTryingToDeleteNonExistentHotel() {
    whenRequestingDeletionHotelIsSoftDeleted();
    delete("/hotel/-1").then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void whenRequestingADeletedHotelThen404IsReturned() {
    Hotel newHotel = InventoryFixtures.createAnyHotel();
    get("/hotel/" + newHotel.getId()).then().statusCode(SC_OK);

    delete("/hotel/" + newHotel.getId());

    get("/hotel/" + newHotel.getId()).then().statusCode(SC_NOT_FOUND);
  }

  private Optional<Hotel> getHotelById(Long id) {
    Hotel hotel = get("/hotel/" + id).thenReturn().as(Hotel.class);
    if (hotel.getId() == null) {
      return Optional.empty();
    } else {
      return Optional.of(hotel);
    }
  }

  private void assertHotelPresent(boolean expected, Hotel newHotel) {
    Hotel[] allHotels = get("/hotel").andReturn().as(Hotel[].class);
    assertThat(
        Stream.of(allHotels).anyMatch(hotel -> hotel.getId().equals(newHotel.getId())),
        is(expected));
  }
}
