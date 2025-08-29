package com.bekassyl.productstoreapp.exceptions;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ProductGlobalExceptionHandler {
    @ExceptionHandler(ProductNotFoundException.class)
    public String handlerProductNotFoundException(ProductNotFoundException e) {
        return "errors/404";
    }
}
