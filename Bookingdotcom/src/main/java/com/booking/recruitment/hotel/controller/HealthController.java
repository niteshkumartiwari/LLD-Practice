package com.booking.recruitment.hotel.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {
  @GetMapping("/health")
  public ResponseEntity<String> health() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/ready")
  public ResponseEntity<String> ready() {
    return ResponseEntity.ok().build();
  }
}
