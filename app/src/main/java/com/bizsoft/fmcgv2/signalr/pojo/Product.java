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
public class Product{

	@JsonProperty("StockLeftForSales")
	private int stockLeftForSales;

	@JsonProperty("IsReOrderLevel")
	private boolean isReOrderLevel;

	@JsonProperty("ItemCode")
	private String itemCode;

	@JsonProperty("SellingRate")
	private int sellingRate;

	@JsonProperty("ProductName")
	private String productName;

	@JsonProperty("SQty")
	private int sQty;

	@JsonProperty("MinSellingRate")
	private int minSellingRate;

	@JsonProperty("JOQty")
	private int jOQty;

	@JsonProperty("MRP")
	private int mRP;

	@JsonProperty("SOQty")
	private int sOQty;

	@JsonProperty("PSpecP")
	private int pSpecP;

	@JsonProperty("SSQty")
	private int sSQty;

	@JsonProperty("SRQtyNotForSales")
	private int sRQtyNotForSales;

	@JsonProperty("StockGroup")
	private StockGroup stockGroup;

	@JsonProperty("PRQty")
	private int pRQty;

	@JsonProperty("AvailableStock")
	private int availableStock;

	@JsonProperty("IsReadOnly")
	private boolean isReadOnly;

	@JsonProperty("SOutQty")
	private int sOutQty;

	@JsonProperty("PQty")
	private int pQty;

	@JsonProperty("SInQty")
	private int sInQty;

	@JsonProperty("IsEnabled")
	private boolean isEnabled;

	@JsonProperty("PSpec")
	private int pSpec;

	@JsonProperty("OpeningStock")
	private int openingStock;

	@JsonProperty("SPQty")
	private int sPQty;

	@JsonProperty("DiscountAmount")
	private int discountAmount;

	@JsonProperty("SRQty")
	private int sRQty;

	@JsonProperty("UOM")
	private UOM uOM;

	@JsonProperty("SRQtyForSales")
	private int sRQtyForSales;

	@JsonProperty("JRQty")
	private int jRQty;

	@JsonProperty("StockGroupId")
	private int stockGroupId;

	@JsonProperty("POQty")
	private int pOQty;

	@JsonProperty("Id")
	private int id;

	@JsonProperty("UOMId")
	private int uOMId;

	@JsonProperty("StockLeftNotForSales")
	private int stockLeftNotForSales;

	@JsonProperty("PurchaseRate")
	private int purchaseRate;

	@JsonProperty("MaxSellingRate")
	private int maxSellingRate;

	@JsonProperty("ReOrderLevel")
	private int reOrderLevel;

	public void setStockLeftForSales(int stockLeftForSales){
		this.stockLeftForSales = stockLeftForSales;
	}

	public int getStockLeftForSales(){
		return stockLeftForSales;
	}

	public void setIsReOrderLevel(boolean isReOrderLevel){
		this.isReOrderLevel = isReOrderLevel;
	}

	public boolean isIsReOrderLevel(){
		return isReOrderLevel;
	}

	public void setItemCode(String itemCode){
		this.itemCode = itemCode;
	}

	public String getItemCode(){
		return itemCode;
	}

	public void setSellingRate(int sellingRate){
		this.sellingRate = sellingRate;
	}

	public int getSellingRate(){
		return sellingRate;
	}

	public void setProductName(String productName){
		this.productName = productName;
	}

	public String getProductName(){
		return productName;
	}

	public void setSQty(int sQty){
		this.sQty = sQty;
	}

	public int getSQty(){
		return sQty;
	}

	public void setMinSellingRate(int minSellingRate){
		this.minSellingRate = minSellingRate;
	}

	public int getMinSellingRate(){
		return minSellingRate;
	}

	public void setJOQty(int jOQty){
		this.jOQty = jOQty;
	}

	public int getJOQty(){
		return jOQty;
	}

	public void setMRP(int mRP){
		this.mRP = mRP;
	}

	public int getMRP(){
		return mRP;
	}

	public void setSOQty(int sOQty){
		this.sOQty = sOQty;
	}

	public int getSOQty(){
		return sOQty;
	}

	public void setPSpecP(int pSpecP){
		this.pSpecP = pSpecP;
	}

	public int getPSpecP(){
		return pSpecP;
	}

	public void setSSQty(int sSQty){
		this.sSQty = sSQty;
	}

	public int getSSQty(){
		return sSQty;
	}

	public void setSRQtyNotForSales(int sRQtyNotForSales){
		this.sRQtyNotForSales = sRQtyNotForSales;
	}

	public int getSRQtyNotForSales(){
		return sRQtyNotForSales;
	}

	public void setStockGroup(StockGroup stockGroup){
		this.stockGroup = stockGroup;
	}

	public StockGroup getStockGroup(){
		return stockGroup;
	}

	public void setPRQty(int pRQty){
		this.pRQty = pRQty;
	}

	public int getPRQty(){
		return pRQty;
	}

	public void setAvailableStock(int availableStock){
		this.availableStock = availableStock;
	}

	public int getAvailableStock(){
		return availableStock;
	}

	public void setIsReadOnly(boolean isReadOnly){
		this.isReadOnly = isReadOnly;
	}

	public boolean isIsReadOnly(){
		return isReadOnly;
	}

	public void setSOutQty(int sOutQty){
		this.sOutQty = sOutQty;
	}

	public int getSOutQty(){
		return sOutQty;
	}

	public void setPQty(int pQty){
		this.pQty = pQty;
	}

	public int getPQty(){
		return pQty;
	}

	public void setSInQty(int sInQty){
		this.sInQty = sInQty;
	}

	public int getSInQty(){
		return sInQty;
	}

	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}

	public boolean isIsEnabled(){
		return isEnabled;
	}

	public void setPSpec(int pSpec){
		this.pSpec = pSpec;
	}

	public int getPSpec(){
		return pSpec;
	}

	public void setOpeningStock(int openingStock){
		this.openingStock = openingStock;
	}

	public int getOpeningStock(){
		return openingStock;
	}

	public void setSPQty(int sPQty){
		this.sPQty = sPQty;
	}

	public int getSPQty(){
		return sPQty;
	}

	public void setDiscountAmount(int discountAmount){
		this.discountAmount = discountAmount;
	}

	public int getDiscountAmount(){
		return discountAmount;
	}

	public void setSRQty(int sRQty){
		this.sRQty = sRQty;
	}

	public int getSRQty(){
		return sRQty;
	}

	public void setUOM(UOM uOM){
		this.uOM = uOM;
	}

	public UOM getUOM(){
		return uOM;
	}

	public void setSRQtyForSales(int sRQtyForSales){
		this.sRQtyForSales = sRQtyForSales;
	}

	public int getSRQtyForSales(){
		return sRQtyForSales;
	}

	public void setJRQty(int jRQty){
		this.jRQty = jRQty;
	}

	public int getJRQty(){
		return jRQty;
	}

	public void setStockGroupId(int stockGroupId){
		this.stockGroupId = stockGroupId;
	}

	public int getStockGroupId(){
		return stockGroupId;
	}

	public void setPOQty(int pOQty){
		this.pOQty = pOQty;
	}

	public int getPOQty(){
		return pOQty;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setUOMId(int uOMId){
		this.uOMId = uOMId;
	}

	public int getUOMId(){
		return uOMId;
	}

	public void setStockLeftNotForSales(int stockLeftNotForSales){
		this.stockLeftNotForSales = stockLeftNotForSales;
	}

	public int getStockLeftNotForSales(){
		return stockLeftNotForSales;
	}

	public void setPurchaseRate(int purchaseRate){
		this.purchaseRate = purchaseRate;
	}

	public int getPurchaseRate(){
		return purchaseRate;
	}

	public void setMaxSellingRate(int maxSellingRate){
		this.maxSellingRate = maxSellingRate;
	}

	public int getMaxSellingRate(){
		return maxSellingRate;
	}

	public void setReOrderLevel(int reOrderLevel){
		this.reOrderLevel = reOrderLevel;
	}

	public int getReOrderLevel(){
		return reOrderLevel;
	}

	@Override
 	public String toString(){
		return 
			"Product{" + 
			"stockLeftForSales = '" + stockLeftForSales + '\'' + 
			",isReOrderLevel = '" + isReOrderLevel + '\'' + 
			",itemCode = '" + itemCode + '\'' + 
			",sellingRate = '" + sellingRate + '\'' + 
			",productName = '" + productName + '\'' + 
			",sQty = '" + sQty + '\'' + 
			",minSellingRate = '" + minSellingRate + '\'' + 
			",jOQty = '" + jOQty + '\'' + 
			",mRP = '" + mRP + '\'' + 
			",sOQty = '" + sOQty + '\'' + 
			",pSpecP = '" + pSpecP + '\'' + 
			",sSQty = '" + sSQty + '\'' + 
			",sRQtyNotForSales = '" + sRQtyNotForSales + '\'' + 
			",stockGroup = '" + stockGroup + '\'' + 
			",pRQty = '" + pRQty + '\'' + 
			",availableStock = '" + availableStock + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			",sOutQty = '" + sOutQty + '\'' + 
			",pQty = '" + pQty + '\'' + 
			",sInQty = '" + sInQty + '\'' + 
			",isEnabled = '" + isEnabled + '\'' + 
			",pSpec = '" + pSpec + '\'' + 
			",openingStock = '" + openingStock + '\'' + 
			",sPQty = '" + sPQty + '\'' + 
			",discountAmount = '" + discountAmount + '\'' + 
			",sRQty = '" + sRQty + '\'' + 
			",uOM = '" + uOM + '\'' + 
			",sRQtyForSales = '" + sRQtyForSales + '\'' + 
			",jRQty = '" + jRQty + '\'' + 
			",stockGroupId = '" + stockGroupId + '\'' + 
			",pOQty = '" + pOQty + '\'' + 
			",id = '" + id + '\'' + 
			",uOMId = '" + uOMId + '\'' + 
			",stockLeftNotForSales = '" + stockLeftNotForSales + '\'' + 
			",purchaseRate = '" + purchaseRate + '\'' + 
			",maxSellingRate = '" + maxSellingRate + '\'' + 
			",reOrderLevel = '" + reOrderLevel + '\'' + 
			"}";
		}
}