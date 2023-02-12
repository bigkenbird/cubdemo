package com.cub.demo.dto.innerobj;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/*
 * inner object in Dto
 * DTO:CoinDeskDto
 */

@Data
public class TimeInfo {
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM d, yyyy HH:mm:ss z",timezone = "UTC",locale ="en" )
	private Date updated;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssX" )
	private Date updatedISO;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMM d, yyyy 'at' HH:mm z",timezone = "GMT",locale ="en" )
	private Date updateduk;
	
	
	public static void main(String[] args) {
		String pattern= "MMM d, yyyy HH:mm:ss z";
		DateFormat a = new SimpleDateFormat(pattern);
		a.setTimeZone(TimeZone.getTimeZone("UTC"));
		String date = a.format(new Date());
		System.out.println(date);
		
	}

}
