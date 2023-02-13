package com.cub.demo.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cub.demo.model.CurrencySurface;


public interface CurrencySurfaceDao extends JpaRepository<CurrencySurface, Integer>{

}
