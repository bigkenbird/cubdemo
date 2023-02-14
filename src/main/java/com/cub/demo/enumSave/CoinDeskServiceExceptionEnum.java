package com.cub.demo.enumSave;

public enum CoinDeskServiceExceptionEnum {
	
	INPUT_PARAM_IS_EMPTY("1000","input parameter is empty"),
	SEARCH_DATA_NOT_FOUND("1001","data not found");
	
	private String code;
	private String message;
	
	CoinDeskServiceExceptionEnum(String code, String message) {
		this.code = code;
		this.message =message;
	}
	public String getCode() {
		return code;
	}
	public String getMessage() {
		return message;
	}
	
	

}
