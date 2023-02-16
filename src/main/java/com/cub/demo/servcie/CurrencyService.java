package com.cub.demo.servcie;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.cub.demo.dao.CurrencySurfaceDao;
import com.cub.demo.dto.CoinDeskDto;
import com.cub.demo.dto.innerobj.Bpi;
import com.cub.demo.dto.innerobj.Currency;
import com.cub.demo.enumSave.CurrencyServiceExceptionEnum;
import com.cub.demo.exception.CurrencyServiceException;
import com.cub.demo.exception.HttpStatusException;
import com.cub.demo.model.CurrencySurface;
import com.cub.demo.vo.CurrencyVo;

@Service
public class CurrencyService {
	
	private static SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
	private CurrencySurfaceDao currencySurfaceDao;
	
	@Value("${coindesk.api.url}")
	private String coindeskApiUrl;
	
	/*
	 * 取得貨幣匯率資訊 by coindeskApiUrl
	 * 
	 */
	
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
	
	/*
	 * 根據coindesk進行api轉換
	 * 
	 */
	public List<CurrencyVo> transferCoinDesk() throws HttpStatusException, CurrencyServiceException, ParseException {
		CoinDeskDto coinDeskDto = getCoinDeskDto();
		Bpi bpi = coinDeskDto.getBpi();
		if(bpi==null) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.COINDESK_BPI_LOSS.getCode(),
					CurrencyServiceExceptionEnum.COINDESK_BPI_LOSS.getMessage()
					); 
		}
		
		//封裝List<CurrencyVo> 
		/*
		1.更新時間(時間格式範例:1990/01/01 00:00:00)。 
		2.幣別相關資訊(幣別,幣別中文名稱,以及匯率)。
		*/
		List<Currency> currencys =  bpi.getAllCurrency();
		List<CurrencyVo> targets = new ArrayList<>();
		
		Date updateDate = coinDeskDto.getTimeInfo().getUpdated();
		for(Currency currency:currencys) {
			List<CurrencyVo> currencyVos = getDataByCurrencyCode(currency.getCode());
			for(CurrencyVo currencyVo:currencyVos) {
				currencyVo.setRate_float(currency.getRate_float());
				Timestamp updateTimestamp = new Timestamp( sdFormat.parse(sdFormat.format(updateDate)).getTime());
				currencyVo.setUpdateDate(sdFormat.format(updateTimestamp));
			}
			targets.addAll(currencyVos);
		}
		return targets;
	}
	
	
	/*
	 * 根據 幣名(currencyCode) 從 幣別對應表(CurrencySurface) 資料庫取得資料
	 * @Param:幣名(currencyCode)
	 */
	
	public List<CurrencyVo> getDataByCurrencyCode(String currencyCode) throws CurrencyServiceException {
		List<CurrencySurface>  resultList =  currencySurfaceDao.findByCurrencyCode(currencyCode);
		
		//根據取出資料數量進行檢核
		if(CollectionUtils.isEmpty(resultList)) {
			return null;
		}
		
		//進行資料封裝
		List<CurrencyVo> targets =  
			resultList.stream().map(source->{
			CurrencyVo target = new CurrencyVo();
			BeanUtils.copyProperties(source, target);
			target.setCreateDate(sdFormat.format(source.getCreateDate()));
			return target;
		}).collect(Collectors.toList());
		
		return targets;
		
	}
	
	
	/*
	 * 根據 幣別vo 從 幣別對應表(CurrencySurface) 資料庫更新資料
	 * @Param:幣別vo(CurrencyVo)
	 * 更新邏輯:
	 * 1.驗證  幣別vo 的 幣別id 、 幣名(currencyCode) 、 幣別中文對應名(currencyChinese) 是否不為空
	 * 2.根據 幣別id 取出 幣別對應表 資料
	 * 3.將 幣別中文對應名(currencyChinese)、更新時間(updateDate)、更新人(updateBy) 更新回 幣別對應表(CurrencySurface) 資料庫
	 */
	public CurrencyVo modifyData(CurrencyVo source) throws CurrencyServiceException {
		//驗證 幣別vo
		if(source==null) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"all "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		if(source.getId()==null) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"id "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		else if(!StringUtils.hasText(source.getCurrencyCode())) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"currencyCode "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		else if(!StringUtils.hasText(source.getCurrencyChinese())) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"currencyChinese "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		else if(!StringUtils.hasText(source.getUpdateBy())) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"updateBy "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		//根據 幣別id 取出 幣別對應表 資料
	  Optional<CurrencySurface> currencySurface = currencySurfaceDao.findById(source.getId());
	  
	  if(currencySurface.isEmpty()) {
		  throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.SEARCH_DATA_NOT_FOUND.getCode(),
					CurrencyServiceExceptionEnum.SEARCH_DATA_NOT_FOUND.getMessage()
			);
	  }
	  
	  currencySurface.ifPresent(
			  target->{
				  BeanUtils.copyProperties(source, target);
				  target.setUpdateDate(new Date(System.currentTimeMillis()));
				  target.setUpdateBy(source.getUpdateBy());
			  });
	  	currencySurfaceDao.save(currencySurface.get());
	  	CurrencySurface searchresult = currencySurfaceDao.findById(source.getId()).get();
	  	CurrencyVo currencyVo = new CurrencyVo();
	  	BeanUtils.copyProperties(searchresult, currencyVo);
	  	currencyVo.setUpdateDate(sdFormat.format(searchresult.getUpdateDate()));
		return currencyVo;
	}
	
	/*
	 * 根據 幣別vo 從 幣別對應表(CurrencySurface) 資料庫新增資料
	 * 驗證 幣別vo 的 幣名(currencyCode) 、 幣別中文對應名(currencyChinese) 是否不為空
	 * @Param:幣別vo(CurrencyVo)
	 * 
	 */
	
	public CurrencyVo createCurrency(CurrencyVo source) throws CurrencyServiceException {
		if(source==null) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"all "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		if(!StringUtils.hasText(source.getCurrencyCode())) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"currencyCode "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		else if(!StringUtils.hasText(source.getCurrencyChinese())) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"currencyChinese "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		else if(!StringUtils.hasText(source.getCreateBy())) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"createBy "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		CurrencySurface target = new CurrencySurface();
		BeanUtils.copyProperties(source, target);
		target.setCreateDate(new Date(System.currentTimeMillis()));
		target.setCreateBy(source.getCreateBy());
		currencySurfaceDao.save(target);//寫入資料庫
		
		List<CurrencySurface> results = currencySurfaceDao.findByCurrencyCode(source.getCurrencyCode());
		
		if(CollectionUtils.isEmpty(results)) {
			return null;
		}
		target = results.get(0);
	  	CurrencyVo currencyVo = new CurrencyVo();
	  	BeanUtils.copyProperties(target, currencyVo);
	  	currencyVo.setCreateDate(sdFormat.format(target.getCreateDate()));
		return currencyVo;
	}
	
	/*
	 * 根據 幣別id 從 幣別對應表(CurrencySurface) 資料庫刪除資料
	 * 驗證 幣別id 是否不為空
	 * @Param:幣別id(id)
	 * 
	 */
	public void deleteCurrency(Integer id) throws CurrencyServiceException {
		if(id==null) {
			throw new CurrencyServiceException(
					CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"id "+CurrencyServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		currencySurfaceDao.deleteById(id);
	}
	
	
	
}
