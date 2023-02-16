package com.cub.demo.enumSave;

public enum CurrencyControllerEnum {
	DELETE_SUCCESS("delete success");
	
	private String message;
	
	CurrencyControllerEnum(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	
}
