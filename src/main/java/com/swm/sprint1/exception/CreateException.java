package com.swm.sprint1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CreateException extends RuntimeException {

    private String errorCode;

    public CreateException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
