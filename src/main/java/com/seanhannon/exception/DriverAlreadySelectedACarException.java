package com.seanhannon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Driver has already selected a car")
public class DriverAlreadySelectedACarException extends Exception {

  private static final long serialVersionUID = 7386137842819000947L;

  public DriverAlreadySelectedACarException(String message) {
    super(message);
  }
}
