package com.cub.demo.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

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
	
	@Column(name="create_date",updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date  createDate;
	
	@Column(name="create_by",updatable = false)
	private String createBy;
	
	@Column(name="update_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateDate;
	
	@Column(name="update_by")
	private String updateBy;
	
	

}
