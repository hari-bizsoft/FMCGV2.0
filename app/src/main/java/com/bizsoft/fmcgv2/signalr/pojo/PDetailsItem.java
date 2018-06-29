/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.signalr.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PDetailsItem{

	@JsonProperty("ProductName")
	private String ProductName;

	@JsonProperty("SNo")
	private double SNo;

	@JsonProperty("Product_Spec_Id")
	private double Product_Spec_Id;

	@JsonProperty("Qty")
	private double Qty;

	@JsonProperty("Product")
	private Product Product;

	@JsonProperty("ProductId")
	private double ProductId;

	@JsonProperty("Available")
	private double Available;

	@JsonProperty("Id")
	private double Id;

	private double refQty;
	private double refAvailable;

    public double getRefQty() {
        return refQty;
    }

    public void setRefQty(double refQty) {
        this.refQty = refQty;
    }

    public double getRefAvailable() {
        return refAvailable;
    }

    public void setRefAvailable(double refAvailable) {
        this.refAvailable = refAvailable;
    }

    public void setProductName(String productName){
		this.ProductName = productName;
	}

	public String getProductName(){
		return ProductName;
	}

	public void setSNo(double sNo){
		this.SNo = sNo;
	}

	public double getSNo(){
		return SNo;
	}

	public void setProduct_Spec_Id(double product_Spec_Id){
		this.Product_Spec_Id = product_Spec_Id;
	}

	public double getProduct_Spec_Id(){
		return Product_Spec_Id;
	}

	public void setQty(double qty){
		this.Qty = qty;
	}

	public double getQty(){
		return Qty;
	}

	public void setProduct(Product product){
		this.Product = product;
	}

	public Product getProduct(){
		return Product;
	}

	public void setProductId(double productId){
		this.ProductId = productId;
	}

	public double getProductId(){
		return ProductId;
	}

	public void setAvailable(double available){
		this.Available = available;
	}

	public double getAvailable(){
		return Available;
	}

	public void setId(double id){
		this.Id = id;
	}

	public double getId(){
		return Id;
	}

	@Override
 	public String toString(){
		return 
			"PDetailsItem{" + 
			"ProductName = '" + ProductName + '\'' +
			",SNo = '" + SNo + '\'' +
			",product_Spec_Id = '" + Product_Spec_Id + '\'' +
			",RQty = '" + Qty + '\'' +
			",Product = '" + Product + '\'' +
			",ProductId = '" + ProductId + '\'' +
			",Available = '" + Available + '\'' +
			",Id = '" + Id + '\'' +
			"}";
		}
}