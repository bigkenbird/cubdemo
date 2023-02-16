package com.cub.demo.vo;

import lombok.Data;

@Data
public class CurrencyVo {
	
	private Integer id;
	
	private String currencyCode; //幣別
	
	private String currencyChinese; //幣別中文名
	
	private String createDate;
	
	private String createBy;
	
	private String updateDate;
	
	private String updateBy;
	
	private Double rate_float;

}
