/*
 * Copyright (C) 2021  Radix IoT Inc. All rights reserved.
 */
package com.radixiot.example;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class SimpleEntityNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(SimpleEntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String simpleEntityNotFoundHandler(SimpleEntityNotFoundException ex) {
        return ex.getMessage();
    }

}
