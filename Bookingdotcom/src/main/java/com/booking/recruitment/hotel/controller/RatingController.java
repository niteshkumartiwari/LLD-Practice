package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.dto.RatingReportDto;
import com.booking.recruitment.hotel.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rating")
public class RatingController {
  private final RatingService ratingService;

  @Autowired
  public RatingController(RatingService ratingService) {
    this.ratingService = ratingService;
  }

  @GetMapping(value = "/city/{cityId}")
  @ResponseStatus(HttpStatus.OK)
  public RatingReportDto getRatingByCity(@PathVariable Long cityId) {
    return ratingService.getRatingAverage(cityId);
  }
}
