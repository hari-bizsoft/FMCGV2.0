/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.Tables;

import com.bizsoft.fmcgv2.dataobject.Ledger;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GopiKing on 01-03-2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Bank {


    @JsonProperty("Id")
    int Id;
    @JsonProperty("BankAccountName")
    String BankAccountName;
    @JsonProperty("Ledger")
    Ledger Ledger;
    @JsonProperty("AccountNo")
    String AccountNo;

    public void setId(int id) {
        Id = id;
    }

    boolean synced;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getAccountNo() {
        return AccountNo;
    }

    public void setAccountNo(String accountNo) {
        AccountNo = accountNo;
    }

    public com.bizsoft.fmcgv2.dataobject.Ledger getLedger() {
        return Ledger;
    }

    public void setLedger(com.bizsoft.fmcgv2.dataobject.Ledger ledger) {
        Ledger = ledger;
    }

    public double getId() {
        return Id;
    }


    public String getBankAccountName() {
        return BankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        BankAccountName = bankAccountName;
    }

    public static  String[] getNames()

    {
        String[] names = new String[Store.getInstance().bankList.size()];
        for(int i=0;i< Store.getInstance().bankList.size();i++)
        {

            names[i] = Store.getInstance().bankList.get(i).getLedger().getLedgerName();

        }
        return names;
    }
}
