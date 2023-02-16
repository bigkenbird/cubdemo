package com.cub.demo.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cub.demo.dto.CoinDeskDto;
import com.cub.demo.enumSave.CurrencyControllerEnum;
import com.cub.demo.exception.CurrencyServiceException;
import com.cub.demo.exception.HttpStatusException;
import com.cub.demo.servcie.CurrencyService;
import com.cub.demo.vo.CurrencyVo;

@RestController
@RequestMapping("currency")
public class CurrencyController {
	
	@Autowired
	private CurrencyService coinDeskService;
	
	 /*
	  * 取得coinDesk API 資料
	  * 
	  * OutPut: ResponseEntity<CoinDeskDto>
	  */
	@GetMapping(value="searchCoindesk",consumes = {"application/*; charset=UTF-8"},produces="application/json;charset=UTF-8")
	public ResponseEntity<CoinDeskDto> getCoinDesk() throws HttpStatusException{
		CoinDeskDto coinDeskDto = coinDeskService.getCoinDeskDto();
		return new ResponseEntity<CoinDeskDto>(coinDeskDto,HttpStatus.OK);
	}
	
	
	 /*
	  * 根據幣名(currencyCode) 查詢 API
	  * Input: string 幣名(currencyCode)
	  * OutPut: ResponseEntity<List<CurrencyVo>>
	  */
	
	 @GetMapping(value="search/{currencyCode}",consumes = {"application/*; charset=UTF-8"},produces="application/json;charset=UTF-8")
	 public ResponseEntity<List<CurrencyVo>> getCurrency(@PathVariable String currencyCode) throws CurrencyServiceException{
		List<CurrencyVo> results = coinDeskService.getDataByCurrencyCode(currencyCode);
		return new ResponseEntity<List<CurrencyVo>>(results,HttpStatus.OK);
	 }
	 
	 /*
	  * 新增幣別對照表資料
	  * Input:Json 格式
	  * example:	
	  * 	{
	  * 	"currencyCode":"AFN",
	  * 	"currencyChinese":"阿富汗幣",
	  * 	"createBy":"Ken"
	  * 	}
	  * OutPut:ResponseEntity<CurrencyVo>
	  */
	 
	 @PostMapping(value="create",consumes = {"application/*; charset=UTF-8"},produces="application/json;charset=UTF-8")
	 public ResponseEntity<CurrencyVo> createCurrency(@RequestBody CurrencyVo createData) throws CurrencyServiceException{
		 System.out.println("createData:"+createData);
		 CurrencyVo result = coinDeskService.createCurrency(createData);
		 System.out.println("result:"+result);
		 return new ResponseEntity<CurrencyVo>(result, HttpStatus.OK);
	 }
	 
	 /*
	  * 更新幣別對照表資料，需指定id
	  * Input:Json 格式
	  * example:	
	  * 	{
	  * 	"id":1,
	  * 	"currencyCode":"AFN",
	  * 	"currencyChinese":"阿富汗幣"
	  * 	}
	  * OutPut:ResponseEntity<CurrencyVo>
	  */
	 @PatchMapping(value="update",consumes = {"application/*; charset=UTF-8"},produces="application/json;charset=UTF-8")
	 public ResponseEntity<CurrencyVo> updateCurrency(@RequestBody CurrencyVo updateData) throws CurrencyServiceException{
		 CurrencyVo result = coinDeskService.modifyData(updateData);
		 return new ResponseEntity<CurrencyVo>(result, HttpStatus.OK);
	 }
	 
	 /*
	  * 刪除幣別對照表資料，需指定id
	  * Input:int id
	  * 
	  * OutPut:ResponseEntity<String>
	  * 
	  */
	 @DeleteMapping(value="delete/{id}",consumes = {"application/*; charset=UTF-8"},produces="application/json;charset=UTF-8")
	 public ResponseEntity<String> deleteCurrency(@PathVariable Integer id) throws CurrencyServiceException{
		coinDeskService.deleteCurrency(id);
		return new ResponseEntity<String>(CurrencyControllerEnum.DELETE_SUCCESS.getMessage(), HttpStatus.OK);
	 }
	 
	 /*
	  * 轉換API
	  * 
	  * OutPut:ResponseEntity<List<CurrencyVo>>
	  * 
	  */
	 @GetMapping(value="transfer",consumes = {"application/*; charset=UTF-8"},produces="application/json;charset=UTF-8")
	 public ResponseEntity<List<CurrencyVo>> getTransferCurrency() throws HttpStatusException, CurrencyServiceException, ParseException{
		 List<CurrencyVo>  results = coinDeskService.transferCoinDesk();
		 return new ResponseEntity<List<CurrencyVo>>(results, HttpStatus.OK);
	 }
	

}
