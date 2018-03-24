package com.bizsoft.fmcgv2.signalr.pojo;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
public class UnderAccountGroup{

	@JsonProperty("GroupName")
	private String groupName;

	@JsonProperty("Company")
	private Company company;

	@JsonProperty("UnderAccountGroup")
	private UnderAccountGroup underAccountGroup;

	@JsonProperty("CompanyId")
	private int companyId;

	@JsonProperty("IsEnabled")
	private boolean isEnabled;

	@JsonProperty("GroupCode")
	private String groupCode;

	@JsonProperty("AccountPath")
	private String accountPath;

	@JsonProperty("Id")
	private int id;

	@JsonProperty("GroupNameWithCode")
	private String groupNameWithCode;

	@JsonProperty("IsReadOnly")
	private boolean isReadOnly;

	@JsonProperty("UnderGroupId")
	private int underGroupId;

	public void setGroupName(String groupName){
		this.groupName = groupName;
	}

	public String getGroupName(){
		return groupName;
	}

	public void setCompany(Company company){
		this.company = company;
	}

	public Company getCompany(){
		return company;
	}

	public void setUnderAccountGroup(UnderAccountGroup underAccountGroup){
		this.underAccountGroup = underAccountGroup;
	}

	public UnderAccountGroup getUnderAccountGroup(){
		return underAccountGroup;
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

	public void setGroupCode(String groupCode){
		this.groupCode = groupCode;
	}

	public String getGroupCode(){
		return groupCode;
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

	public void setGroupNameWithCode(String groupNameWithCode){
		this.groupNameWithCode = groupNameWithCode;
	}

	public String getGroupNameWithCode(){
		return groupNameWithCode;
	}

	public void setIsReadOnly(boolean isReadOnly){
		this.isReadOnly = isReadOnly;
	}

	public boolean isIsReadOnly(){
		return isReadOnly;
	}

	public void setUnderGroupId(int underGroupId){
		this.underGroupId = underGroupId;
	}

	public int getUnderGroupId(){
		return underGroupId;
	}

	@Override
 	public String toString(){
		return 
			"UnderAccountGroup{" + 
			"groupName = '" + groupName + '\'' + 
			",company = '" + company + '\'' + 
			",underAccountGroup = '" + underAccountGroup + '\'' + 
			",companyId = '" + companyId + '\'' + 
			",isEnabled = '" + isEnabled + '\'' + 
			",groupCode = '" + groupCode + '\'' + 
			",accountPath = '" + accountPath + '\'' + 
			",id = '" + id + '\'' + 
			",groupNameWithCode = '" + groupNameWithCode + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			",underGroupId = '" + underGroupId + '\'' + 
			"}";
		}
}