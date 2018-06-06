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
public class StockGroup{

	@JsonProperty("StockGroupNameWithCode")
	private String stockGroupNameWithCode;

	@JsonProperty("Company")
	private Company company;

	@JsonProperty("CompanyId")
	private int companyId;

	@JsonProperty("UnderStockGroup")
	private UnderStockGroup underStockGroup;

	@JsonProperty("IsEnabled")
	private boolean isEnabled;

	@JsonProperty("StockGroupName")
	private String stockGroupName;

	@JsonProperty("GroupCode")
	private String groupCode;

	@JsonProperty("IsPurchase")
	private boolean isPurchase;

	@JsonProperty("AccountPath")
	private String accountPath;

	@JsonProperty("Id")
	private int id;

	@JsonProperty("IsSale")
	private boolean isSale;

	@JsonProperty("IsReadOnly")
	private boolean isReadOnly;

	public void setStockGroupNameWithCode(String stockGroupNameWithCode){
		this.stockGroupNameWithCode = stockGroupNameWithCode;
	}

	public String getStockGroupNameWithCode(){
		return stockGroupNameWithCode;
	}

	public void setCompany(Company company){
		this.company = company;
	}

	public Company getCompany(){
		return company;
	}

	public void setCompanyId(int companyId){
		this.companyId = companyId;
	}

	public int getCompanyId(){
		return companyId;
	}

	public void setUnderStockGroup(UnderStockGroup underStockGroup){
		this.underStockGroup = underStockGroup;
	}

	public UnderStockGroup getUnderStockGroup(){
		return underStockGroup;
	}

	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}

	public boolean isIsEnabled(){
		return isEnabled;
	}

	public void setStockGroupName(String stockGroupName){
		this.stockGroupName = stockGroupName;
	}

	public String getStockGroupName(){
		return stockGroupName;
	}

	public void setGroupCode(String groupCode){
		this.groupCode = groupCode;
	}

	public String getGroupCode(){
		return groupCode;
	}

	public void setIsPurchase(boolean isPurchase){
		this.isPurchase = isPurchase;
	}

	public boolean isIsPurchase(){
		return isPurchase;
	}

	public void setAccountPath(String accountPath){
		this.accountPath = accountPath;
	}

	public String getAccountPath(){
		return accountPath;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setIsSale(boolean isSale){
		this.isSale = isSale;
	}

	public boolean isIsSale(){
		return isSale;
	}

	public void setIsReadOnly(boolean isReadOnly){
		this.isReadOnly = isReadOnly;
	}

	public boolean isIsReadOnly(){
		return isReadOnly;
	}

	@Override
 	public String toString(){
		return 
			"StockGroup{" + 
			"stockGroupNameWithCode = '" + stockGroupNameWithCode + '\'' + 
			",company = '" + company + '\'' + 
			",companyId = '" + companyId + '\'' + 
			",underStockGroup = '" + underStockGroup + '\'' + 
			",isEnabled = '" + isEnabled + '\'' + 
			",stockGroupName = '" + stockGroupName + '\'' + 
			",groupCode = '" + groupCode + '\'' + 
			",isPurchase = '" + isPurchase + '\'' + 
			",accountPath = '" + accountPath + '\'' + 
			",id = '" + id + '\'' + 
			",isSale = '" + isSale + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			"}";
		}
}