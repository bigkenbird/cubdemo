package com.cub.demo.service;

import java.text.ParseException;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.cub.demo.dto.CoinDeskDto;
import com.cub.demo.exception.CurrencyServiceException;
import com.cub.demo.exception.HttpStatusException;
import com.cub.demo.servcie.CurrencyService;
import com.cub.demo.vo.CurrencyVo;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CurrencyServiceTest {
	
	Logger logger = LoggerFactory.getLogger(CurrencyServiceTest.class);
	
	@Autowired
	CurrencyService  currencyService;
	
	@Test
	@Order(0)
	void  getCoinDeskDto() {
		try {
			logger.info("start getCoinDeskDto Test.........................................");
			 CoinDeskDto coinDeskDto  =  currencyService.getCoinDeskDto();
			 Assert.notNull(coinDeskDto,"coinDeskDto can not be null");
			 logger.info(coinDeskDto.toString());
			 logger.info("end getCoinDeskDto Test.........................................");
		} 	catch (HttpStatusException e) {
			logger.error("HttpStatusException:",e);
		} catch (Exception e){
			logger.error("Exception:",e);
		}
		
	}

	
	@Test
	@Order(1)
	void getDataByCurrencyCode() {
		try {
			logger.info("start getDataByCurrencyCode Test.........................................");
			List<CurrencyVo> currencyVos = currencyService.getDataByCurrencyCode("USD");
			Assert.notEmpty(currencyVos,"currencyVo list can not be empty");
			currencyVos.stream().forEach(e->{
				logger.info("currencyVo:"+e);
			});
			logger.info("end getDataByCurrencyCode Test.........................................");
		} catch (CurrencyServiceException e) {
			logger.error("CurrencyServiceException:",e);
		}
	}
	
	
	@Test
	@Order(2)
	void modifyData() {
		CurrencyVo currencyVo = new CurrencyVo();
		currencyVo.setId(1);
		currencyVo.setCurrencyCode("USD");
		currencyVo.setCurrencyChinese("美國貨幣"); //美金改美國貨幣
		currencyVo.setUpdateBy("Ken");
		try {
			logger.info("start modifyData Test.........................................");
			CurrencyVo modifiedData = currencyService.modifyData(currencyVo);
			logger.info("currencyVo:"+currencyVo);
			logger.info("modifiedData:"+modifiedData);
			
			Assert.notNull(modifiedData," modified CurrencyVo can not be null");
			Assert.notNull(modifiedData.getUpdateDate(),"modified CurrencyVo updateDate can not be null");
			Assert.isTrue(currencyVo.getId().equals(modifiedData.getId()), "modified CurrencyVo id is not equal "+currencyVo.getId());
			Assert.isTrue(currencyVo.getCurrencyCode().equals(modifiedData.getCurrencyCode()), "modified CurrencyVo CurrencyCode is not equal "+currencyVo.getCurrencyCode());
			Assert.isTrue(currencyVo.getCurrencyChinese().equals(modifiedData.getCurrencyChinese()), "modified CurrencyVo CurrencyChinese is not equal "+currencyVo.getCurrencyChinese());
			Assert.isTrue( "Ken".equals(modifiedData.getUpdateBy()), "modified CurrencyVo UpdateBy is not equal "+"Ken");
			logger.info("end modifyData Test.........................................");
		} catch (CurrencyServiceException e) {
			logger.error("CurrencyServiceException:",e);
		}
	}
	
	
	@Test
	@Order(3)
	void createCurrency() {
		CurrencyVo currencyVo = new CurrencyVo();
		currencyVo.setCurrencyCode("AFN");
		currencyVo.setCurrencyChinese("阿富汗幣");
		currencyVo.setCreateBy("Ken");
		try {
			logger.info("start createCurrency Test.........................................");
			CurrencyVo createData = currencyService.createCurrency(currencyVo);
			Assert.notNull(createData," created CurrencyVo can not be null");
			Assert.notNull(createData.getId(),"created CurrencyVo id can not be null");
			Assert.notNull(createData.getCreateDate(),"created CurrencyVo updateDate can not be null");
			logger.info("currencyVo.getCurrencyCode():"+currencyVo.getCurrencyCode());
			logger.info("createData.getCurrencyCode():"+createData.getCurrencyCode());
			logger.info("judge:"+currencyVo.getCurrencyCode().equals(createData.getCurrencyCode()));
			Assert.isTrue(currencyVo.getCurrencyCode().equals(createData.getCurrencyCode().trim()), "created CurrencyVo CurrencyCode is not equal "+currencyVo.getCurrencyCode());
			Assert.isTrue(currencyVo.getCurrencyChinese().equals(createData.getCurrencyChinese()), "created CurrencyVo CurrencyChinese is not equal "+currencyVo.getCurrencyChinese());
			Assert.isTrue("Ken".equals(createData.getCreateBy()), "created CurrencyVo CreateBy is not equal "+"Ken");
			logger.info("createData:"+createData.toString());
			logger.info("end createCurrency Test.........................................");
		} catch (CurrencyServiceException e) {
			logger.error("CurrencyServiceException:",e);
		}
	}
	
	
	
	@Test
	@Order(4)
	void transferCoinDesk() throws HttpStatusException, CurrencyServiceException, ParseException {
		logger.info("start transferCoinDesk Test.........................................");
		List<CurrencyVo> currencyVos = currencyService.transferCoinDesk();
		Assert.notEmpty(currencyVos,"currencyVos can not be empty");
		logger.info("end transferCoinDesk Test.........................................");
	}
	
	
	@Test
	@Order(5)
	void deleteCurrency() {
		try {
			logger.info("start deleteCurrency Test.........................................");
			currencyService.deleteCurrency(4);
			List<CurrencyVo> vertifiedDatas = currencyService.getDataByCurrencyCode("GBP");
			Assert.isNull(vertifiedDatas,"vertified CurrencyVo list must be null");
			logger.info("vertifiedDatas:"+vertifiedDatas);
			logger.info("end deleteCurrency Test.........................................");
		} catch (CurrencyServiceException e) {
			logger.error("CurrencyServiceException:",e);
		}
	}
	
	

}
