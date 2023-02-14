package com.cub.demo.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cub.demo.model.CurrencySurface;


public interface CurrencySurfaceDao extends JpaRepository<CurrencySurface, Integer>{
	
	public List<CurrencySurface> findByCurrencyCode(String currencyCode);
	
	public Optional<CurrencySurface> findById(Integer id);
	
	public CurrencySurface  save(CurrencySurface currencySurface);
	
	public void deleteById(Integer id);

}
