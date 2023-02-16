package com.cub.demo.enumSave;

public enum CurrencyServiceExceptionEnum {
	
	INPUT_PARAM_IS_EMPTY("1000","input parameter is empty"),
	SEARCH_DATA_NOT_FOUND("1001","data not found"),
	
	COINDESK_BPI_LOSS("2001","loss bpi object");
	
	private String code;
	private String message;
	
	CurrencyServiceExceptionEnum(String code, String message) {
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
