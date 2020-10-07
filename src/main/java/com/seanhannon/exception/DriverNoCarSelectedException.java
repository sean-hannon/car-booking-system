package com.seanhannon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Driver has not previously selected a car")
public class DriverNoCarSelectedException extends Exception {

  private static final long serialVersionUID = -5068077633222024853L;

  public DriverNoCarSelectedException(String message) {
    super(message);
  }
}
