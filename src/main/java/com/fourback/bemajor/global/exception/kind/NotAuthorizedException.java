package com.fourback.bemajor.global.exception.kind;

import com.fourback.bemajor.global.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

public class NotAuthorizedException extends CustomException {
    public NotAuthorizedException(String message) {
        super(3, message, HttpStatus.FORBIDDEN);
    }
}
