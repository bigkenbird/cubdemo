package com.cub.demo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class HttpStatusException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public HttpStatusException(String errorStatus,String message) {
		super(message);
		this.errorStatus = errorStatus;
	}
	
	private String errorStatus;

}
