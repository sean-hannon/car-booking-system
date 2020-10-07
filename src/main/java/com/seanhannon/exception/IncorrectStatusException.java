package com.seanhannon.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Incorrect driver status")
public class IncorrectStatusException extends Exception {

    private static final long serialVersionUID = 3042668441792263179L;

    public IncorrectStatusException(String message){
        super(message);
    }
}
