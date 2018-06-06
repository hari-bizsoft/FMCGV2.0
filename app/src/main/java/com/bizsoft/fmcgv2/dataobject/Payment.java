/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

/**
 * Created by GopiKing on 14-09-2017.
 */

public class Payment {

    Long id;
    double amount;
    Long LegerId;
    String paymentMode;
    boolean synced;
    String chequeNo;

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public void setChequeBankName(String chequeBankName) {
        this.chequeBankName = chequeBankName;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public String getChequeBankName() {
        return chequeBankName;
    }

    String chequeDate;
    String chequeBankName;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Long getLegerId() {
        return LegerId;
    }

    public void setLegerId(Long legerId) {
        LegerId = legerId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }
}
