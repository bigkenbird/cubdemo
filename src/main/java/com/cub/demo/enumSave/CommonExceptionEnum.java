package com.cub.demo.enumSave;

public enum CommonExceptionEnum {
	UNEXPECT_EXCEPTION("無預期錯誤");

	private String message;
	
	CommonExceptionEnum(String message){
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	
}
