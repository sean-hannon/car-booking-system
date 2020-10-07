package com.seanhannon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalSearchException extends Exception {

  static final long serialVersionUID = 6356518085667879112L;

  public IllegalSearchException(String message) {
    super(message);
  }
}
