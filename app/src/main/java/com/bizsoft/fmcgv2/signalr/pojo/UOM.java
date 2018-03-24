package com.bizsoft.fmcgv2.signalr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UOM{

	@JsonProperty("FormalName")
	private String formalName;

	@JsonProperty("Company")
	private Company company;

	@JsonProperty("CompanyId")
	private int companyId;

	@JsonProperty("IsEnabled")
	private boolean isEnabled;

	@JsonProperty("Symbol")
	private String symbol;

	@JsonProperty("Id")
	private int id;

	@JsonProperty("IsReadOnly")
	private boolean isReadOnly;

	public void setFormalName(String formalName){
		this.formalName = formalName;
	}

	public String getFormalName(){
		return formalName;
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

	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}

	public boolean isIsEnabled(){
		return isEnabled;
	}

	public void setSymbol(String symbol){
		this.symbol = symbol;
	}

	public String getSymbol(){
		return symbol;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
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
			"UOM{" + 
			"formalName = '" + formalName + '\'' + 
			",company = '" + company + '\'' + 
			",companyId = '" + companyId + '\'' + 
			",isEnabled = '" + isEnabled + '\'' + 
			",symbol = '" + symbol + '\'' + 
			",id = '" + id + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			"}";
		}
}