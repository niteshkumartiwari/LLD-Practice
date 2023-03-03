//package com.booking.recruitment.scoring;
//
//import com.booking.recruitment.scoring.extensions.RESTExtension;
//import com.booking.recruitment.scoring.fixtures.rest.InventoryFixtures;
//import com.booking.recruitment.scoring.fixtures.rest.RatingReportFixtures;
//import com.booking.recruitment.scoring.model.City;
//import com.booking.recruitment.scoring.model.Hotel;
//import com.booking.recruitment.scoring.util.RatingAverageCalculator;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//
//import java.util.Arrays;
//import java.util.List;
//
//import static io.restassured.RestAssured.get;
//import static org.apache.http.HttpStatus.SC_OK;
//import static org.hamcrest.Matchers.is;
//
//@ExtendWith({RESTExtension.class})
//class RatingReportTest {
//  @Test
//  @Disabled("Disabled so exercise takes <60 minutes to complete")
//  void whenHavingAnEmptyRatingThenIsIgnored() {
//    City city = InventoryFixtures.createAnyCity();
//    List<Double> ratings = Arrays.asList(9.2, 6.8, 7.3, null);
//    RatingReportFixtures.createHotelSample(city, ratings);
//
//    double expectedRatingAverage = RatingAverageCalculator.getRatingAverage(ratings);
//
//    get("/rating/report/" + city.getId())
//        .then()
//        .statusCode(SC_OK)
//        .body("numberOfRatings", is(3))
//        .body("averageRating", is((float) expectedRatingAverage));
//  }
//
//  @Test
//  @Disabled("Disabled so exercise takes <60 minutes to complete")
//  void whenHavingDeletedHotelThenItIsIgnoredFromAverageRating() {
//    City city = InventoryFixtures.createAnyCity();
//    List<Double> ratings = Arrays.asList(9.2, 6.8, 7.3);
//    RatingReportFixtures.createHotelSample(city, ratings);
//
//    double expectedRatingAverage = RatingAverageCalculator.getRatingAverage(ratings);
//
//    InventoryFixtures.createHotel(Hotel.builder().city(city).deleted(true).rating(3.2).build());
//
//    get("/rating/report/" + city.getId())
//        .then()
//        .statusCode(SC_OK)
//        .body("numberOfRatings", is(3))
//        .body("averageRating", is((float) expectedRatingAverage));
//  }
//}
