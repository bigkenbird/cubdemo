package com.cub.demo.servcie;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cub.demo.dto.CoinDeskDto;
import com.cub.demo.exception.HttpStatusException;

@Service
public class CoinDeskService {
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Value("${coindesk.api.url}")
	private String coindeskApiUrl;
	
	public CoinDeskDto getCoinDeskDto() throws HttpStatusException {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(new MediaType("application", "javascript")));
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		ResponseEntity<CoinDeskDto> coinDeskDtoRsp = restTemplate.exchange(
				coindeskApiUrl, 
				HttpMethod.GET, 
				entity, 
				CoinDeskDto.class);
		
		//回傳4XXor5XX錯誤
		if(coinDeskDtoRsp.getStatusCode().isError()) {
			HttpStatus errorStatus = coinDeskDtoRsp.getStatusCode();
			throw new HttpStatusException(String.valueOf(errorStatus),errorStatus.getReasonPhrase());
		}
		return coinDeskDtoRsp.getBody();
		
	}

}
