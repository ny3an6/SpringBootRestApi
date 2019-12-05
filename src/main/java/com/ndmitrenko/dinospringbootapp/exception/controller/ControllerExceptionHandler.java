package com.ndmitrenko.dinospringbootapp.exception.controller;

import com.ndmitrenko.dinospringbootapp.dto.response.exception.DefaultExceptionResponse;
import com.ndmitrenko.dinospringbootapp.exception.DefaultException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler { // Exception handler
    @ExceptionHandler(DefaultException.class)
    public ResponseEntity<Object> handleProblemWithShowingCompany(DefaultException ex) {
        return new ResponseEntity<>(new DefaultExceptionResponse(ex.getCode(), ex.getMessage() ,ex.getApiResult()), ex.getCode());
    }
}
