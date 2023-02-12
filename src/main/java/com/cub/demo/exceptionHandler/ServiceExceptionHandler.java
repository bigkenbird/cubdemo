package com.cub.demo.exceptionHandler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cub.demo.exception.HttpStatusException;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler{
	
	 @ExceptionHandler(HttpStatusException.class)
	 public ResponseEntity<Object> handleHttpStatusException(HttpStatusException httpStatusException){
		 System.out.println(httpStatusException.getErrorStatus());
		 System.out.println(httpStatusException.getMessage());
		 return null;
		 
	 }

}
