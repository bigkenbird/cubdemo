package com.cub.demo.exceptionHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cub.demo.enumSave.CommonExceptionEnum;
import com.cub.demo.exception.CurrencyServiceException;
import com.cub.demo.exception.HttpStatusException;

@ControllerAdvice
public class ServiceExceptionHandler extends ResponseEntityExceptionHandler{
	
	 Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);
	
	 @ExceptionHandler(HttpStatusException.class)
	 public ResponseEntity<Exception> handleHttpStatusException(HttpStatusException httpStatusException){
		 return new ResponseEntity<Exception>(httpStatusException,HttpStatus.EXPECTATION_FAILED);
	 }
	 
	 @ExceptionHandler(CurrencyServiceException.class)
	 public ResponseEntity<Exception> CurrencyServiceException(CurrencyServiceException currencyServiceException){
		 return new ResponseEntity<Exception>(currencyServiceException,HttpStatus.EXPECTATION_FAILED);
	 }
	 
	 @ExceptionHandler(Exception.class)
	 public ResponseEntity<String> UnExpectException(Exception ex){
		 logger.error("UnExpectException",ex);
		 return new ResponseEntity<String>(CommonExceptionEnum.UNEXPECT_EXCEPTION.getMessage(),HttpStatus.SERVICE_UNAVAILABLE);
	 }

}
