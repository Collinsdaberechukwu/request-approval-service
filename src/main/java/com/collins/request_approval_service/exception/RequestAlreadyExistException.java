package com.collins.request_approval_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RequestAlreadyExistException extends RuntimeException{

    public RequestAlreadyExistException(String msg){
        super(msg);
    }
}
