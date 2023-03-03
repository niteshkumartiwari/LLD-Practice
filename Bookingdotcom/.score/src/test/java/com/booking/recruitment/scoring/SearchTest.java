package com.booking.recruitment.scoring;

import com.booking.recruitment.scoring.extensions.RESTExtension;
import com.booking.recruitment.scoring.fixtures.rest.InventoryFixtures;
import com.booking.recruitment.scoring.fixtures.rest.SearchFixtures;
import com.booking.recruitment.scoring.model.City;
import com.booking.recruitment.scoring.model.Hotel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.Matchers.emptyIterable;

@ExtendWith({RESTExtension.class})
class SearchTest {
  @Test
  void whenRequestingHotelsClosestToCityThenExpectRightOrder() {
    List<Hotel> hotelSamples = SearchFixtures.getHotelSample();
    List<Hotel> expectedList = SearchFixtures.getListOrderedByCityCentreDistance(hotelSamples, 3);

    List<Hotel> resultList =
        Arrays.asList(
            get("/search/" + hotelSamples.get(0).getCity().getId() + "?sortBy=distance")
                .then()
                .statusCode(SC_OK)
                .extract()
                .response()
                .as(Hotel[].class));

    assertThat(
        resultList.stream().map(Hotel::getId).toArray(),
        arrayContaining(expectedList.stream().map(Hotel::getId).toArray()));
  }

  @Test
  void whenRequestingHotelsClosestToCityWithNoHotelsExpectEmptyList() {
    whenRequestingHotelsClosestToCityThenExpectRightOrder();
    City city = InventoryFixtures.createAnyCity();

    get("/search/" + city.getId() + "?sortBy=distance")
        .then()
        .statusCode(SC_OK)
        .body("$", emptyIterable());
  }

  @Test
  void statusCode404WhenSearchingNonExistentCity() {
    whenRequestingHotelsClosestToCityThenExpectRightOrder();
    get("/search/-1?sortBy=distance").then().statusCode(SC_NOT_FOUND);
  }

  @Test
  void statusCode400WhenSortByInvalid() {
    City city = InventoryFixtures.createAnyCity();

    get("/search/" + city.getId() + "?sortBy=invalid").then().statusCode(SC_BAD_REQUEST);
  }

  @Test
  void whenRequestingHotelsClosestToCityWithLessThan3HotelsThenExpectRightOrder() {
    List<Hotel> hotelSamples = SearchFixtures.getTwoHotelSample();
    List<Hotel> expectedList = SearchFixtures.getListOrderedByCityCentreDistance(hotelSamples, 3);

    List<Hotel> resultList =
        Arrays.asList(
            get("/search/" + hotelSamples.get(0).getCity().getId() + "?sortBy=distance")
                .then()
                .statusCode(SC_OK)
                .extract()
                .response()
                .as(Hotel[].class));

    assertThat(
        resultList.stream().map(Hotel::getId).toArray(),
        arrayContaining(expectedList.stream().map(Hotel::getId).toArray()));
  }

  //  @Test
  //  @Disabled("Disabled so exercise takes <60 minutes to complete")
  //  void whenRequestingBestHotelsThenExpectRightOrder() {
  //    List<Hotel> hotelSamples = SearchFixtures.getHotelSample();
  //    List<Hotel> expectedList = SearchFixtures.getListOrderedByBest(hotelSamples, 3);
  //
  //    List<Hotel> resultList =
  //        Arrays.asList(
  //            get("/search/" + hotelSamples.get(0).getCity().getId() + "?sortBy=best")
  //                .then()
  //                .statusCode(SC_OK)
  //                .extract()
  //                .response()
  //                .as(Hotel[].class));
  //
  //    assertThat(
  //        resultList.stream().map(Hotel::getId).toArray(),
  //        arrayContaining(expectedList.stream().map(Hotel::getId).toArray()));
  //  }
}
