package com.cub.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cub.demo.servcie.CoinDeskService;
import com.cub.demo.vo.CurrencyVo;

@Controller
@RequestMapping("currency")
public class CurrencyController {
	
	@Autowired
	private CoinDeskService coinDeskService;
	
	
	 @GetMapping("search/{currencyCode}")
	 public ResponseEntity<CurrencyVo> getCurrency(@PathVariable String currencyCode){
		return null;
		 
	 }
	

}
