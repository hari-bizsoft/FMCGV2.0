package com.bizsoft.fmcgv2.Tables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by GopiKing on 28-02-2018.
 */

public class SalesReturn {

    Long Id;
    Date SRDate;
    String RefNo;
    String RefCode;
    int LedgerId;
    int TransactionTypeId;
    String ChequeNo;
    Date ChequeDate;
    String BankName;
    Double ItemAmount;
    Double DiscountAmount;
    Double GSTAmount;
    Double ExtraAmount;
    Double TotalAmount;

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

    public Collection<SaleDetail> getSRetails() {
        return SRetails;
    }

    public void setSRetails(Collection<SaleDetail> SRetails) {
        this.SRetails = SRetails;
    }

    public Collection<SaleDetail> SRetails = new ArrayList<SaleDetail>();

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Date getSRDate() {
        return SRDate;
    }

    public void setSRDate(Date SRDate) {
        this.SRDate = SRDate;
    }

    public String getRefNo() {
        return RefNo;
    }

    public void setRefNo(String refNo) {
        RefNo = refNo;
    }

    public String getRefCode() {
        return RefCode;
    }

    public void setRefCode(String refCode) {
        RefCode = refCode;
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
}
