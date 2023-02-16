package com.cub.demo.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.cub.demo.service.CurrencyServiceTest;
import com.cub.demo.vo.CurrencyVo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CurrencyControllerTest {
	
	Logger logger = LoggerFactory.getLogger(CurrencyServiceTest.class);
	
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	ObjectMapper objectMapper;
	
	@Test
	@Order(0)
	void getCurrency() throws Exception {
		MvcResult result =	
		this.mockMvc
		.perform(get("/currency/search/USD"))
		.andExpect(status().isOk())
		.andReturn();
		logger.info("開始 測試呼叫查詢幣別對應表資料 API,並顯示其內容......................................");
		logger.info(result.getResponse().getContentAsString());
		logger.info("結束 測試呼叫查詢幣別對應表資料 API,並顯示其內容......................................");
	}
	
	@Test
	@Order(1)
	void createCurrency() throws Exception {
		CurrencyVo obj = new CurrencyVo();
		obj.setCurrencyCode("AFN");
		obj.setCurrencyChinese("阿富汗幣");
		obj.setCreateBy("Ken");
		MvcResult result = 
		this.mockMvc
		.perform(MockMvcRequestBuilders.post("/currency/create")
				.content(objectMapper.writeValueAsString(obj))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andReturn();
		logger.info("開始 測試呼叫新增幣別對應表資料 API,並顯示其內容。......................................");
		logger.info(result.getResponse().getContentAsString());
		logger.info("結束 測試呼叫新增幣別對應表資料 API,並顯示其內容。......................................");
	}
	
	@Test
	@Order(2)
	void updateCurrency() throws JsonProcessingException, Exception {
		CurrencyVo obj = new CurrencyVo();
		obj.setId(1);
		obj.setCurrencyCode("USD");
		obj.setCurrencyChinese("美國貨幣");
		obj.setUpdateBy("Ken");
		
		MvcResult result = 
				this.mockMvc
				.perform(MockMvcRequestBuilders.patch("/currency/update")
						.content(objectMapper.writeValueAsString(obj))
						.accept(MediaType.APPLICATION_JSON)
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andReturn();
				logger.info("開始 測試呼叫更新幣別對應表資料 API,並顯示其內容。......................................");
				logger.info(result.getResponse().getContentAsString());
				logger.info("結束 測試呼叫更新幣別對應表資料 API,並顯示其內容。......................................");
	}
	
	@Test
	@Order(3)
	void deleteCurrency() throws Exception {
		MvcResult result =	
				this.mockMvc
				.perform(delete("/currency/delete/4"))
				.andExpect(status().isOk())
				.andReturn();
		logger.info("開始 測試呼叫刪除幣別對應表資料 API。......................................");
		logger.info(result.getResponse().getContentAsString());
		logger.info("結束 測試呼叫刪除幣別對應表資料 API。......................................");
		
	}
	
	@Test
	@Order(4)
	void getCoinDesk() throws Exception {
		MvcResult result =	
				this.mockMvc
				.perform(get("/currency/searchCoindesk"))
				.andExpect(status().isOk())
				.andReturn();
				logger.info("開始 測試呼叫 coindesk API,並顯示其內容。......................................");
				logger.info(result.getResponse().getContentAsString());
				logger.info("結束 測試呼叫 coindesk API,並顯示其內容。......................................");
	}
	
	@Test
	@Order(5)
	void getTransferCurrency() throws Exception {
		MvcResult result =	
				this.mockMvc
				.perform(get("/currency/transfer"))
				.andExpect(status().isOk())
				.andReturn();
				logger.info("開始 測試呼叫資料轉換的 API,並顯示其內容。......................................");
				logger.info(result.getResponse().getContentAsString());
				logger.info("結束 測試呼叫資料轉換的 API,並顯示其內容。......................................");
	}

}
