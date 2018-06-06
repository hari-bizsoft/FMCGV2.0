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
 * Created by GopiKing on 01-03-2018.
 */

public class Receipt {
    
          Long Id;
          String EntryNo ;
            Date ReceiptDate ;
            Long LedgerId ;
            String ReceiptMode ;
            double Amount ;
            String Particulars ;
            String RefNo ;
            String Status ;
             double Extracharge ;
            String ChequeNo ;
            Date ChequeDate ;
            Date CleareDate ;
            String ReceivedFrom ;
            String VoucherNo ;

            public Collection<ReceiptDetail> RDetails = new ArrayList<ReceiptDetail>();

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getEntryNo() {
        return EntryNo;
    }

    public void setEntryNo(String entryNo) {
        EntryNo = entryNo;
    }

    public Date getReceiptDate() {
        return ReceiptDate;
    }

    public void setReceiptDate(Date receiptDate) {
        ReceiptDate = receiptDate;
    }

    public Long getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(Long ledgerId) {
        LedgerId = ledgerId;
    }

    public String getReceiptMode() {
        return ReceiptMode;
    }

    public void setReceiptMode(String receiptMode) {
        ReceiptMode = receiptMode;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getParticulars() {
        return Particulars;
    }

    public void setParticulars(String particulars) {
        Particulars = particulars;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public double getExtracharge() {
        return Extracharge;
    }

    public void setExtracharge(double extracharge) {
        Extracharge = extracharge;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String chequeNo) {
        ChequeNo = chequeNo;
    }

    public Date getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(Date chequeDate) {
        ChequeDate = chequeDate;
    }

    public Date getCleareDate() {
        return CleareDate;
    }

    public void setCleareDate(Date cleareDate) {
        CleareDate = cleareDate;
    }

    public String getReceivedFrom() {
        return ReceivedFrom;
    }

    public void setReceivedFrom(String receivedFrom) {
        ReceivedFrom = receivedFrom;
    }

    public String getVoucherNo() {
        return VoucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        VoucherNo = voucherNo;
    }

    public Collection<ReceiptDetail> getRDetails() {
        return RDetails;
    }

    public void setRDetails(Collection<ReceiptDetail> RDetails) {
        this.RDetails = RDetails;
    }
}
