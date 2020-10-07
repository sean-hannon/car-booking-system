package com.seanhannon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Car has already been selected")
public class CarAlreadyInUseException extends Exception {

    private static final long serialVersionUID = 1195424351657867436L;

    public CarAlreadyInUseException(String message){
        super(message);
    }
}
