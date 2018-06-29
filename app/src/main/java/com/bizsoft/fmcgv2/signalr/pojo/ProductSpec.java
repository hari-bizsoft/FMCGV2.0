/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.signalr.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductSpec {

	@JsonProperty("PDetail")
	private PDetail pDetail;

	@JsonProperty("RNo")
	private double rNo;

	public long getAvailable() {
		return Available;
	}

	public void setAvailable(long available) {
		Available = available;
	}

	@JsonProperty("Available")
	long Available;

	@JsonProperty("ProductName")
	private String productName;

	@JsonProperty("Product")
	private Product product;

	@JsonProperty("ProductId")
	private double productId;

	@JsonProperty("Id")
	private double id;

	@JsonProperty("PDetails")
	private List<PDetailsItem> pDetails;


	int RQty;

	public int getRQty() {
		return RQty;
	}

	public void setRQty(int RQty) {
		this.RQty = RQty;
	}

	public void setPDetail(PDetail pDetail){
		this.pDetail = pDetail;
	}

	public PDetail getPDetail(){
		return pDetail;
	}

	public void setRNo(double rNo){
		this.rNo = rNo;
	}

	public double getRNo(){
		return rNo;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setProduct(Product product){
		this.product = product;
	}

	public Product getProduct(){
		return product;
	}

	public void setProductId(double productId){
		this.productId = productId;
	}

	public double getProductId(){
		return productId;
	}

	public void setId(double id){
		this.id = id;
	}

	public double getId(){
		return id;
	}

	public void setPDetails(List<PDetailsItem> pDetails){
		this.pDetails = pDetails;
	}

	public List<PDetailsItem> getPDetails(){
		return pDetails;
	}

	@Override
 	public String toString(){
		return 
			"ProductSpec{" +
			"pDetail = '" + pDetail + '\'' + 
			",rNo = '" + rNo + '\'' + 
			",productName = '" + productName + '\'' + 
			",product = '" + product + '\'' + 
			",productId = '" + productId + '\'' + 
			",id = '" + id + '\'' + 
			",pDetails = '" + pDetails + '\'' + 
			"}";
		}
}