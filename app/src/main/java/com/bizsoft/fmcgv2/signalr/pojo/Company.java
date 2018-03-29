package com.bizsoft.fmcgv2.signalr.pojo;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class Company{

	@JsonProperty("CompanyName")
	private String companyName;
	@JsonProperty("LoginAccYear")
	private int loginAccYear;

	@JsonProperty("IsEnabled")
	private boolean isEnabled;
	@JsonProperty("IsActive")
	private boolean isActive;
	@JsonProperty("CompanyType")
	private String companyType;
	@JsonProperty("Id")
	private int id;
	@JsonProperty("IsReadOnly")
	private boolean isReadOnly;

	public void setCompanyName(String companyName){
		this.companyName = companyName;
	}

	public String getCompanyName(){
		return companyName;
	}

	public void setLoginAccYear(int loginAccYear){
		this.loginAccYear = loginAccYear;
	}

	public int getLoginAccYear(){
		return loginAccYear;
	}



	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}

	public boolean isIsEnabled(){
		return isEnabled;
	}

	public void setIsActive(boolean isActive){
		this.isActive = isActive;
	}

	public boolean isIsActive(){
		return isActive;
	}


	public void setCompanyType(String companyType){
		this.companyType = companyType;
	}

	public String getCompanyType(){
		return companyType;
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
			"Company{" + 
			"companyName = '" + companyName + '\'' + 
			",loginAccYear = '" + loginAccYear + '\'' + 

			",isEnabled = '" + isEnabled + '\'' + 
			",isActive = '" + isActive + '\'' + 

			",companyType = '" + companyType + '\'' + 
			",id = '" + id + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			"}";
		}
}