package com.cub.demo.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="CurrencySurface")
@Data
public class CurrencySurface {
	
	@Id
	@Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column(name="currency_code")
	private String currencyCode;
	
	@Column(name="currency_chinese")
	private String currencyChinese;
	
	@Column(name="create_date")
	private Date createDate;
	
	@Column(name="create_by")
	private String createBy;
	
	@Column(name="update_date")
	private Date updateDate;
	
	@Column(name="update_by")
	private String updateBy;
	
	

}
