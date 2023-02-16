package com.cub.demo.dto.innerobj;

import java.util.Arrays;
import java.util.List;

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
	
	public List<Currency> getAllCurrency(){
		return Arrays.asList(this.usd,this.gbp,this.eur);
	}

}
