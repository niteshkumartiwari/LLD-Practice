package com.booking.recruitment.hotel.controller;

import com.booking.recruitment.hotel.repository.HotelRepository;
import com.booking.testing.SlowTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Sql(executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD, scripts = "classpath:data.sql")
@SlowTest
class RatingControllerTest {
  @Autowired private MockMvc mockMvc;

  @Autowired private HotelRepository repository;

  @Test
  @DisplayName("When the average rating is requested then it's correctly calculated")
  void averageRatingCorrect() throws Exception {
    mockMvc
        .perform(get("/rating/city/2"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("numberOfRatings", equalTo(4)))
        .andExpect(jsonPath("averageRating", closeTo(6.05, 0.01)));
  }
}
