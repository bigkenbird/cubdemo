package com.cub.demo.dto.innerobj;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Bpi {
	
	@JsonProperty("USD")
	private Currency usd;
	
	@JsonProperty("GBP")
	private Currency gbp;
	
	@JsonProperty("EUR")
	private Currency eur;

}
