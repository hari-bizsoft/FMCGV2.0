package com.bizsoft.fmcgv2.signalr.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditLimitType{

	@JsonProperty("Id")
	private int id;

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	@Override
 	public String toString(){
		return 
			"CreditLimitType{" + 
			"id = '" + id + '\'' + 
			"}";
		}
}