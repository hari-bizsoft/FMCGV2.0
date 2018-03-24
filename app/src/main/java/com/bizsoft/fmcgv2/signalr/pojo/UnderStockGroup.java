package com.bizsoft.fmcgv2.signalr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UnderStockGroup{

	@JsonProperty("CompanyId")
	private int companyId;

	@JsonProperty("IsEnabled")
	private boolean isEnabled;

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

	public void setCompanyId(int companyId){
		this.companyId = companyId;
	}

	public int getCompanyId(){
		return companyId;
	}

	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}

	public boolean isIsEnabled(){
		return isEnabled;
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
			"UnderStockGroup{" + 
			"companyId = '" + companyId + '\'' + 
			",isEnabled = '" + isEnabled + '\'' + 
			",isPurchase = '" + isPurchase + '\'' + 
			",accountPath = '" + accountPath + '\'' + 
			",id = '" + id + '\'' + 
			",isSale = '" + isSale + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			"}";
		}
}