package com.cub.demo.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorObj {
	
	private String error;
	
	private String message;
	

}
