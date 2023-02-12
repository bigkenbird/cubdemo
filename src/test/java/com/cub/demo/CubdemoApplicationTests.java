package com.cub.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cub.demo.dto.CoinDeskDto;
import com.cub.demo.exception.HttpStatusException;
import com.cub.demo.servcie.CoinDeskService;

@SpringBootTest
class CubdemoApplicationTests {
	
	Logger logger = LoggerFactory.getLogger(CubdemoApplicationTests.class);
	
	@Autowired
	CoinDeskService coinDeskService;

	/*
	 * 呼叫 coindesk 的 API。
	 */
	@Test
	void contextLoads() {
		try {
			CoinDeskDto coinDeskDto = coinDeskService.getCoinDeskDto();
			logger.info(coinDeskDto.toString());
		} catch (HttpStatusException e) {
			logger.error(e.getMessage());
			
		}
	}

}
