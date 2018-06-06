/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package signal.gson;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("com.robohorse.robopojogenerator")
public class Customer{

	@SerializedName("IsEnabled")
	private boolean isEnabled;

	@SerializedName("Ledger")
	private Ledger ledger;

	@SerializedName("LedgerId")
	private int ledgerId;

	@SerializedName("Id")
	private int id;

	@SerializedName("IsReadOnly")
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