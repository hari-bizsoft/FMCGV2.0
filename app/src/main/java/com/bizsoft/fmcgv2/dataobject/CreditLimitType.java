/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreditLimitType{

	@JsonProperty("Id")
	private int id;
	@JsonProperty("LimitType")
	String LimitType;

    public String getLimitType() {
        return LimitType;
    }

    public void setLimitType(String limitType) {
        LimitType = limitType;
    }

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

    public static CreditLimitType getByName(String chooosedCreditType) {

        CreditLimitType result = new CreditLimitType();
        for(int i=0;i<Store.getInstance().customerCreditLimitTypeList.size();i++)
        {
            if(chooosedCreditType.equals(Store.getInstance().customerCreditLimitTypeList.get(i).getLimitType()))
            {
                result = Store.getInstance().customerCreditLimitTypeList.get(i);
            }
        }
        return result;
    }
}