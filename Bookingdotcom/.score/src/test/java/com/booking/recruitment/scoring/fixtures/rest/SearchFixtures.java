package com.booking.recruitment.scoring.fixtures.rest;

import com.booking.recruitment.scoring.model.City;
import com.booking.recruitment.scoring.model.Hotel;
import com.booking.recruitment.scoring.util.HotelRecommendationAdvisor;
import com.booking.recruitment.scoring.util.SearchDistanceAdvisor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class SearchFixtures {
  private SearchFixtures() {}

  public static List<Hotel> getHotelSample() {
    final City city =
        InventoryFixtures.createCity(
            City.builder()
                .name("Search Test City")
                .cityCentreLatitude(52.368780)
                .cityCentreLongitude(4.903303)
                .build());

    return Arrays.asList(
        InventoryFixtures.createHotel(
            Hotel.builder()
                .latitude(52.364799)
                .longitude(4.908971)
                .city(city)
                .rating(9.2)
                .build()), // distanceCityCentre: 0.5867252880626888, recommendedRating:
        // 0.9106637355968655
        InventoryFixtures.createHotel(
            Hotel.builder()
                .latitude(52.3681563)
                .longitude(4.9010029)
                .city(city)
                .rating(6.3)
                .build()), // distanceCityCentre: 0.17091744408958798, recommendedRating:
        // 0.7139541277955206
        InventoryFixtures.createHotel(
            Hotel.builder()
                .latitude(52.379577)
                .longitude(4.633547)
                .city(city)
                .rating(9.8)
                .build()), // distanceCityCentre: 18.356804007050286, recommendedRating:
        // 0.7350000000000001
        InventoryFixtures.createHotel(
            Hotel.builder()
                .latitude(52.3756755)
                .longitude(4.8668628)
                .city(city)
                .rating(8.7)
                .build()), // distanceCityCentre: 2.590673000142123, recommendedRating:
        // 0.7729663499928937
        InventoryFixtures.createHotel(
            Hotel.builder()
                .latitude(52.380936)
                .longitude(4.8708297)
                .city(city)
                .rating(null)
                .build()), // distanceCityCentre: 2.586550936703864, recommendedRating:
        // 0.12067245316480679
        InventoryFixtures.createHotel(
            Hotel.builder()
                .latitude(52.3773989)
                .longitude(4.8846443)
                .city(city)
                .rating(null)
                .build()) // distanceCityCentre: 1.588827489021465, recommendedRating:
        // 0.17055862554892676
        );
  }

  public static List<Hotel> getTwoHotelSample() {
    final City city =
        InventoryFixtures.createCity(
            City.builder()
                .name("Search Test City")
                .cityCentreLatitude(52.368780)
                .cityCentreLongitude(4.903303)
                .build());

    return Arrays.asList(
        InventoryFixtures.createHotel(
            Hotel.builder()
                .latitude(52.364799)
                .longitude(4.908971)
                .city(city)
                .rating(9.2)
                .build()), // distanceCityCentre: 0.5867252880626888, recommendedRating:
        // 0.9106637355968655
        InventoryFixtures.createHotel(
            Hotel.builder()
                .latitude(52.3681563)
                .longitude(4.9010029)
                .city(city)
                .rating(6.3)
                .build()) // distanceCityCentre: 0.17091744408958798, recommendedRating:
        // 0.7139541277955206
        );
  }

  public static List<Hotel> getListOrderedByCityCentreDistance(List<Hotel> source, int itemLimit) {
    // Expected order for sample above: 2,1,6,5,4,3
    List<Hotel> copyOfSource = new ArrayList<>(source);
    copyOfSource.sort(
        Comparator.comparingDouble(SearchDistanceAdvisor::distanceFromHotelToCityCentre));
    return copyOfSource.subList(0, Math.min(itemLimit, source.size()));
  }

  public static List<Hotel> getListOrderedByBest(List<Hotel> source, int itemLimit) {
    // Expected order for sample above: 1,4,3,2,6,5
    List<Hotel> copyOfSource = new ArrayList<>(source);
    copyOfSource.sort(
        Comparator.comparingDouble(
            (hotel -> 1 - HotelRecommendationAdvisor.getRecommendedScore(hotel))));
    return copyOfSource.subList(0, Math.min(itemLimit, source.size()));
  }
}
