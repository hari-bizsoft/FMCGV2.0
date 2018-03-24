package com.bizsoft.fmcgv2.Tables;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by GopiKing on 28-12-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Sale {

    Long Id;
    Date SalesDate;
    Date SODate;
    String RefNo;
    Double ItemAmount;
    Double DiscountAmount;
    Double GSTAmount;
    Double ExtraAmount;
    Double TotalAmount;
    String ChequeNo;
    Date ChequeDate;
    String BankName;
    int LedgerId;
    boolean IsGST;
    int TransactionTypeId;



    public Date getSODate() {
        return SODate;
    }

    public void setSODate(Date SODate) {
        this.SODate = SODate;
    }



    public boolean isGST() {
        return IsGST;
    }

    public void setGST(boolean GST) {
        IsGST = GST;
    }

    public int getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(int ledgerId) {
        LedgerId = ledgerId;
    }

    public int getTransactionTypeId() {
        return TransactionTypeId;
    }

    public void setTransactionTypeId(int transactionTypeId) {
        TransactionTypeId = transactionTypeId;
    }

    public Collection<SaleDetail> SDetails = new ArrayList<SaleDetail>();

    public Collection<SaleDetail> getSDetails() {
        return SDetails;
    }

    public void setSDetails(Collection<SaleDetail> SDetails) {
        this.SDetails = SDetails;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Date getSalesDate() {
        return SalesDate;
    }

    public void setSalesDate(Date salesDate) {
        this.SalesDate = salesDate;
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

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }
}
