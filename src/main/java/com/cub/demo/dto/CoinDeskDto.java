package com.cub.demo.dto;

import com.cub.demo.dto.innerobj.Bpi;
import com.cub.demo.dto.innerobj.TimeInfo;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/*
 *從coindesk.api.url 接收回來的原始資料
 */
@Data
public class CoinDeskDto {
	
	@JsonProperty("time")
	private TimeInfo timeInfo;
	
	@JsonProperty("disclaimer")
	private String disclaimer;
	
	@JsonProperty("chartName")
	private String chartName;
	
	@JsonProperty("bpi")
	private Bpi bpi;
	
}
