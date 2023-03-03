package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.model.Hotel;
import com.booking.recruitment.hotel.model.HotelResponse;
import com.booking.recruitment.hotel.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotel")
public class HotelController {
  private final HotelService hotelService;

  @Autowired
  public HotelController(HotelService hotelService) {
    this.hotelService = hotelService;
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public List<Hotel> getAllHotels() {
    return hotelService.getAllHotels();
  }


  /**
   * Get API to return Hotel By id
   *
   * @param id
   * @return com.booking.recruitment.hotel.model.HotelResponse
   */
  @GetMapping("/{id}")
  public ResponseEntity<HotelResponse> getHotelById(@PathVariable final Long id){
    HotelResponse result= HotelResponse.getHotelResponse(hotelService.getHotelById(id));
    return ResponseEntity
            .status(HttpStatus.OK)
            .body(result);
  }

  /**
   * Delete API to logically mark the hotel as deleted
   *
   * @param id
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteHotelById(@PathVariable Long id){
    hotelService.deleteHotelById(id);

    return ResponseEntity
            .status(HttpStatus.OK)
            .body(null);
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Hotel createHotel(@RequestBody Hotel hotel) {
    return hotelService.createNewHotel(hotel);
  }
}
