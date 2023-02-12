package com.cub.demo.dto.innerobj;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.DoubleDeserializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.NumberDeserializer;

import lombok.Data;

/*
 * inner object in Dto
 * DTO:CoinDeskDto
 */

@Data
public class Currency {
	
	
	@JsonProperty("code")
	private String code;
	
	
	@JsonProperty("symbol")
	private String symbol;
	
	@JsonProperty("rate")
	private String rate;
	
	@JsonProperty("description")
	private String description;
	
	@JsonDeserialize(using = NumberDeserializer.class)
	@JsonProperty("rate_float")
	private Double rate_float;
	
}
