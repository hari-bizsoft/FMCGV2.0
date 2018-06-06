/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.Tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by GopiKing on 28-02-2018.
 */

public class SalesOrder {
    Long Id;
    Date SODate;
    String RefNo;
    String RefCode;

    int LedgerId;

    boolean synced;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    Double ItemAmount;
    Double DiscountAmount;
    Double GSTAmount;
    Double ExtraAmount;
    Double TotalAmount;

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String narration) {
        Narration = narration;
    }

    String Narration;
    public String getRefCode() {
        return RefCode;
    }

    public void setRefCode(String refCode) {
        RefCode = refCode;
    }

    public Date getSODate() {
        return SODate;
    }

    public void setSODate(Date SODate) {
        this.SODate = SODate;
    }


    public int getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(int ledgerId) {
        LedgerId = ledgerId;
    }



    public Collection<SalesOrderDetails> SODetails = new ArrayList<SalesOrderDetails>();

    public Collection<SalesOrderDetails> getSODetails() {
        return SODetails;
    }

    public void setSODetails(Collection<SalesOrderDetails> SODetails) {
        this.SODetails = SODetails;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }



    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public Double getItemAmount() {
        return ItemAmount;
    }

    public void setItemAmount(Double itemAmount) {
        ItemAmount = itemAmount;
    }

    public Double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(Double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public Double getGSTAmount() {
        return GSTAmount;
    }

    public void setGSTAmount(Double GSTAmount) {
        this.GSTAmount = GSTAmount;
    }

    public Double getExtraAmount() {
        return ExtraAmount;
    }

    public void setExtraAmount(Double extraAmount) {
        ExtraAmount = extraAmount;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }


}
