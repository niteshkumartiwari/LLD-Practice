package com.booking.recruitment.hotel.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ElementWithSameIDAlreadyExistsException extends RuntimeException {
  private static final long serialVersionUID = 7246983447306271525L;

  public ElementWithSameIDAlreadyExistsException(String message) {
    super(message);
  }
}
