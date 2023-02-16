package com.cub.demo;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.CollectionUtils;

import com.cub.demo.dao.CurrencySurfaceDao;
import com.cub.demo.dto.CoinDeskDto;
import com.cub.demo.exception.HttpStatusException;
import com.cub.demo.model.CurrencySurface;
import com.cub.demo.servcie.CurrencyService;

@SpringBootTest
class CubdemoApplicationTests {
	
	Logger logger = LoggerFactory.getLogger(CubdemoApplicationTests.class);
	
	@Autowired
	CurrencyService coinDeskService;
	
	@Autowired
	CurrencySurfaceDao currencySurfaceDao;

	/*
	 * 呼叫 coindesk 的 API。
	 */
	@Test
	void getCoinDeskDto() {
		try {
			CoinDeskDto coinDeskDto = coinDeskService.getCoinDeskDto();
			logger.info(coinDeskDto.toString());
		} catch (HttpStatusException e) {
			logger.error(e.getMessage());
			
		}
	}
	
	@Test
	void check() {
		List<CurrencySurface> currencySurfaces = currencySurfaceDao.findAll();
		if(CollectionUtils.isEmpty(currencySurfaces)) {
			logger.error("fail");
		}
		else {
			currencySurfaces.stream().forEach(
					e->
			{
				System.out.println(e);
			}
					);
		
		}

}
}
