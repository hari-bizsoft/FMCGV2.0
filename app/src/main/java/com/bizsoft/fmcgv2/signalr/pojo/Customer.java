package com.bizsoft.fmcgv2.signalr.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {

	@JsonProperty("IsEnabled")
	private boolean isEnabled;

	@JsonProperty("Ledger")
	private Ledger ledger;

	@JsonProperty("LedgerId")
	private int ledgerId;

	@JsonProperty("Id")
	private int id;

	@JsonProperty("IsReadOnly")
	private boolean isReadOnly;

	public void setIsEnabled(boolean isEnabled){
		this.isEnabled = isEnabled;
	}

	public boolean isIsEnabled(){
		return isEnabled;
	}

	public void setLedger(Ledger ledger){
		this.ledger = ledger;
	}

	public Ledger getLedger(){
		return ledger;
	}

	public void setLedgerId(int ledgerId){
		this.ledgerId = ledgerId;
	}

	public int getLedgerId(){
		return ledgerId;
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
			"Customer{" +
			"isEnabled = '" + isEnabled + '\'' + 
			",ledger = '" + ledger + '\'' + 
			",ledgerId = '" + ledgerId + '\'' + 
			",id = '" + id + '\'' + 
			",isReadOnly = '" + isReadOnly + '\'' + 
			"}";
		}
}