/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.signalr.pojo;

import com.bizsoft.fmcgv2.dataobject.CreditLimitType;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Ledger{

	@JsonProperty("MobileNo")
	private String mobileNo;

	@JsonProperty("AccountGroupId")
	private int accountGroupId;

	@JsonProperty("IsEnabled")
	private boolean isEnabled;

	@JsonProperty("GSTNo")
	private String gSTNo;

	@JsonProperty("CreditLimit")
	private int creditLimit;

	@JsonProperty("CreditAmount")
	private int creditAmount;

	@JsonProperty("OPDr")
	private int oPDr;

	@JsonProperty("CityName")
	private String cityName;

	@JsonProperty("AddressLine2")
	private String addressLine2;

	@JsonProperty("OPBal")
	private int oPBal;

	@JsonProperty("AddressLine1")
	private String addressLine1;

	@JsonProperty("AccountGroup")
	private AccountGroup accountGroup;

	@JsonProperty("CreditLimitType")
	private CreditLimitType creditLimitType;

	@JsonProperty("Id")
	private int id;

	@JsonProperty("PersonIncharge")
	private String personIncharge;

	@JsonProperty("IsReadOnly")
	private boolean isReadOnly;

	@JsonProperty("LedgerName")
	private String ledgerName;

	@JsonProperty("AccountName")
	private String accountName;

	@JsonProperty("ACType")
	private String aCType;

	public void setMobileNo(String mobileNo){
		this.mobileNo = mobileNo;
	}

	public String getMobileNo(){
		return mobileNo;
	}

	public void setAccountGroupId(int accountGroupId){
		this.accountGroupId = accountGroupId;
	}

	public int getAccountGroupId(){
		return accountGroupId;
	}

	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}

	public boolean isIsEnabled(){
		return isEnabled;
	}

	public void setGSTNo(String gSTNo){
		this.gSTNo = gSTNo;
	}

	public String getGSTNo(){
		return gSTNo;
	}

	public void setCreditLimit(int creditLimit){
		this.creditLimit = creditLimit;
	}

	public int getCreditLimit(){
		return creditLimit;
	}

	public void setCreditAmount(int creditAmount){
		this.creditAmount = creditAmount;
	}

	public int getCreditAmount(){
		return creditAmount;
	}

	public void setOPDr(int oPDr){
		this.oPDr = oPDr;
	}

	public int getOPDr(){
		return oPDr;
	}

	public void setCityName(String cityName){
		this.cityName = cityName;
	}

	public String getCityName(){
		return cityName;
	}

	public void setAddressLine2(String addressLine2){
		this.addressLine2 = addressLine2;
	}

	public String getAddressLine2(){
		return addressLine2;
	}

	public void setOPBal(int oPBal){
		this.oPBal = oPBal;
	}

	public int getOPBal(){
		return oPBal;
	}

	public void setAddressLine1(String addressLine1){
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine1(){
		return addressLine1;
	}

	public void setAccountGroup(AccountGroup accountGroup){
		this.accountGroup = accountGroup;
	}

	public AccountGroup getAccountGroup(){
		return accountGroup;
	}

	public void setCreditLimitType(CreditLimitType creditLimitType){
		this.creditLimitType = creditLimitType;
	}

	public CreditLimitType getCreditLimitType(){
		return creditLimitType;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setPersonIncharge(String personIncharge){
		this.personIncharge = personIncharge;
	}

	public String getPersonIncharge(){
		return personIncharge;
	}

	public void setIsReadOnly(boolean isReadOnly){
		this.isReadOnly = isReadOnly;
	}

	public boolean isIsReadOnly(){
		return isReadOnly;
	}

	public void setLedgerName(String ledgerName){
		this.ledgerName = ledgerName;
	}

	public String getLedgerName(){
		return ledgerName;
	}

	public void setAccountName(String accountName){
		this.accountName = accountName;
	}

	public String getAccountName(){
		return accountName;
	}

	public void setACType(String aCType){
		this.aCType = aCType;
	}

	public String getACType(){
		return aCType;
	}

	@Override
 	public String toString(){
		return 
			"Ledger{" + 
			"mobileNo = '" + mobileNo + '\'' + 
			",accountGroupId = '" + accountGroupId + '\'' + 
			",isEnabled = '" + isEnabled + '\'' + 
			",gSTNo = '" + gSTNo + '\'' + 
			",creditLimit = '" + creditLimit + '\'' + 
			",creditAmount = '" + creditAmount + '\'' + 
			",oPDr = '" + oPDr + '\'' + 
			",cityName = '" + cityName + '\'' + 
			",addressLine2 = '" + addressLine2 + '\'' + 
			",oPBal = '" + oPBal + '\'' + 
			",addressLine1 = '" + addressLine1 + '\'' + 
			",accountGroup = '" + accountGroup + '\'' + 
			",creditLimitType = '" + creditLimitType + '\'' + 
			",id = '" + id + '\'' + 
			",personIncharge = '" + personIncharge + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			",ledgerName = '" + ledgerName + '\'' + 
			",accountName = '" + accountName + '\'' + 
			",aCType = '" + aCType + '\'' + 
			"}";
		}
}