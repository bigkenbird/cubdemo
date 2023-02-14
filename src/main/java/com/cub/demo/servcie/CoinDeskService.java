package com.cub.demo.servcie;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.coyote.http11.filters.VoidInputFilter;
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
import com.cub.demo.enumSave.CoinDeskServiceExceptionEnum;
import com.cub.demo.exception.CoinDeskServiceException;
import com.cub.demo.exception.HttpStatusException;
import com.cub.demo.model.CurrencySurface;
import com.cub.demo.vo.CurrencyVo;

@Service
public class CoinDeskService {
	
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
	 * 根據 幣名(currencyCode) 從 幣別對應表(CurrencySurface) 資料庫取得資料
	 * @Param:幣名(currencyCode)
	 */
	
	public List<CurrencyVo> getDataByCurrencyCode(String currencyCode) throws CoinDeskServiceException {
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
	public CurrencyVo modifyData(CurrencyVo source,String updateBy) throws CoinDeskServiceException {
		//驗證 幣別vo
		if(source==null) {
			throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"all "+CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		if(source.getId()!=null) {
			throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"id "+CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		else if(!StringUtils.hasText(source.getCurrencyCode())) {
			throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"currencyCode "+CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		else if(!StringUtils.hasText(source.getCurrencyChinese())) {
			throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"currencyChinese "+CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		//根據 幣別id 取出 幣別對應表 資料
	  Optional<CurrencySurface> currencySurface = currencySurfaceDao.findById(source.getId());
	  
	  if(currencySurface.isEmpty()) {
		  throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.SEARCH_DATA_NOT_FOUND.getCode(),
					CoinDeskServiceExceptionEnum.SEARCH_DATA_NOT_FOUND.getMessage()
			);
	  }
	  
	  currencySurface.ifPresent(
			  target->{
				  BeanUtils.copyProperties(source, target);
				  target.setUpdateDate(new Date( new java.util.Date().getTime()));
				  target.setUpdateBy(updateBy);
			  });
	  	CurrencySurface result = currencySurfaceDao.save(currencySurface.get());
	  	CurrencyVo currencyVo = new CurrencyVo();
	  	BeanUtils.copyProperties(result, currencyVo);
		
		return currencyVo;
	}
	
	/*
	 * 根據 幣別vo 從 幣別對應表(CurrencySurface) 資料庫新增資料
	 * 驗證 幣別vo 的 幣名(currencyCode) 、 幣別中文對應名(currencyChinese) 是否不為空
	 * @Param:幣別vo(CurrencyVo)
	 * 
	 */
	
	public CurrencyVo createCurrency(CurrencyVo source,String createBy) throws CoinDeskServiceException {
		if(source==null) {
			throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"all "+CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
	
		
		if(!StringUtils.hasText(source.getCurrencyCode())) {
			throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"currencyCode "+CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		else if(!StringUtils.hasText(source.getCurrencyChinese())) {
			throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"currencyChinese "+CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		CurrencySurface target = new CurrencySurface();
		BeanUtils.copyProperties(source, target);
		target.setCreateDate(new Date( new java.util.Date().getTime()));
		target.setCreateBy(createBy);
		
		CurrencySurface result = currencySurfaceDao.save(target); //寫入資料庫
		
	  	CurrencyVo currencyVo = new CurrencyVo();
	  	BeanUtils.copyProperties(result, currencyVo);
		return currencyVo;
	}
	
	/*
	 * 根據 幣別id 從 幣別對應表(CurrencySurface) 資料庫刪除資料
	 * 驗證 幣別id 是否不為空
	 * @Param:幣別id(id)
	 * 
	 */
	public void deleteCurrency(Integer id) throws CoinDeskServiceException {
		if(id==null) {
			throw new CoinDeskServiceException(
					CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getCode(),
					"id "+CoinDeskServiceExceptionEnum.INPUT_PARAM_IS_EMPTY.getMessage()
					);
		}
		
		currencySurfaceDao.deleteById(id);
	}
}
