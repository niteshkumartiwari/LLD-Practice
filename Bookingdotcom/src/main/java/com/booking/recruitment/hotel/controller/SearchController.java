package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.model.HotelDistance;
import com.booking.recruitment.hotel.model.HotelResponse;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class SearchController {

    private final HotelService hotelService;

    @Autowired
    public SearchController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    /**
     * Search API to return 3 nearest hotel to the city center
     *
     * @param cityId
     * @param attribute
     * @return List<com.booking.recruitment.hotel.model.HotelResponse>
     */
    @GetMapping("/search/{cityId}")
    public ResponseEntity<List<HotelResponse>> getClosestThreeHotels(@PathVariable final Long cityId,
                                                                     @RequestParam(value = "sortBy", required = false) final String attribute) {
        List<Hotel> hotels = hotelService.getHotelsByCity(cityId);

        double cityCenterLat = hotels.get(0).getCity().getCityCentreLatitude();
        double cityCenterLong = hotels.get(0).getCity().getCityCentreLongitude();

        List<HotelDistance> hotelDistances = new ArrayList<>();

        for (Hotel hotel : hotels) {
            double dist = distance(cityCenterLat, cityCenterLong, hotel.getLatitude(), hotel.getLongitude());
            hotelDistances.add(new HotelDistance(hotel, dist));
        }

        //TODO :: Currently Nlog(N), Can be optimized to Nlog(K) via priority_queue(maxHeap)
        Collections.sort(hotelDistances, new CustomComparator());
        List<HotelResponse> result = new ArrayList<>();

        for (int i = 0; i < Math.min(3, hotelDistances.size()); i++) {
            result.add(HotelResponse.getHotelResponse(hotelDistances.get(i).getHotel()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

    public class CustomComparator implements Comparator<HotelDistance> {
        @Override
        public int compare(HotelDistance h1, HotelDistance h2) {
            return Double.compare(h2.getDistance(), h1.getDistance());
        }
    }

    private double distance(double lat1, double lat2, double lon1, double lon2) {

        // The math module contains a function
        // named toRadians which converts from
        // degrees to radians.
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2)
                + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2), 2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956
        // for miles
        double r = 6371;

        // calculate the result
        return (c * r);
    }
}
