package com.cub.demo.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
public class CurrencyServiceException extends Exception{

	private static final long serialVersionUID = 1L;
	
	private String errorStatus;
	
	public CurrencyServiceException(String errorStatus,String message) {
		super(message);
		this.errorStatus = errorStatus;
	}

}
