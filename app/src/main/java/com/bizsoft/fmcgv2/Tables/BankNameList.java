package com.bizsoft.fmcgv2.Tables;

import com.bizsoft.fmcgv2.dataobject.Ledger;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GopiKing on 01-03-2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankNameList {
    @JsonProperty("Id")
    double Id;
    @JsonProperty("LedgerId")
    double LedgerId;
    @JsonProperty("BankAccountName")
    String BankAccountName;
    @JsonProperty("Ledger")
    Ledger Ledger;

    public com.bizsoft.fmcgv2.dataobject.Ledger getLedger() {
        return Ledger;
    }

    public void setLedger(com.bizsoft.fmcgv2.dataobject.Ledger ledger) {
        Ledger = ledger;
    }

    public double getId() {
        return Id;
    }

    public void setId(double id) {
        Id = id;
    }

    public double getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(double ledgerId) {
        LedgerId = ledgerId;
    }

    public String getBankAccountName() {
        return BankAccountName;
    }

    public void setBankAccountName(String bankAccountName) {
        BankAccountName = bankAccountName;
    }
}
