/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.signalr.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PDetail{

	@JsonProperty("SNo")
	private double sNo;

	@JsonProperty("Product_Spec_Id")
	private double productSpecId;

	@JsonProperty("RQty")
	private double qty;

	@JsonProperty("ProductId")
	private double productId;

	@JsonProperty("Available")
	private double available;

	@JsonProperty("Id")
	private double id;

	public void setSNo(double sNo){
		this.sNo = sNo;
	}

	public double getSNo(){
		return sNo;
	}

	public void setProductSpecId(double productSpecId){
		this.productSpecId = productSpecId;
	}

	public double getProductSpecId(){
		return productSpecId;
	}

	public void setQty(double qty){
		this.qty = qty;
	}

	public double getQty(){
		return qty;
	}

	public void setProductId(double productId){
		this.productId = productId;
	}

	public double getProductId(){
		return productId;
	}

	public void setAvailable(double available){
		this.available = available;
	}

	public double getAvailable(){
		return available;
	}

	public void setId(double id){
		this.id = id;
	}

	public double getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"PDetail{" + 
			"sNo = '" + sNo + '\'' + 
			",product_Spec_Id = '" + productSpecId + '\'' + 
			",qty = '" + qty + '\'' + 
			",productId = '" + productId + '\'' + 
			",available = '" + available + '\'' + 
			",id = '" + id + '\'' + 
			"}";
		}
}