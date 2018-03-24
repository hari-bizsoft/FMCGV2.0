package com.bizsoft.fmcgv2.Tables;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by GopiKing on 01-03-2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SOPending {


    @JsonProperty("Id")
    double Id;
    @JsonProperty("SODate")
    Date SODate ;
    @JsonProperty("RefNo")
    String RefNo ;
    @JsonProperty("RefCode")
    String RefCode;
    @JsonProperty("LedgerId")
    double LedgerId ;
    @JsonProperty("ItemAmount")
    double ItemAmount ;
    @JsonProperty("DiscountAmount")
    double DiscountAmount ;
    @JsonProperty("GSTAmount")
    double GSTAmount ;
    @JsonProperty("ExtraAmount")
    double ExtraAmount;
    @JsonProperty("TotalAmount")
    double TotalAmount ;
    @JsonProperty("Narration")
    String Narration ;
    @JsonProperty("Status")
    String Status ;
    @JsonProperty("AmountInwords")
   String  AmountInwords ;
    @JsonProperty("synced")
   boolean synced;

    public double getId() {
        return Id;
    }

    public void setId(double id) {
        Id = id;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

   ArrayList<SalesOrderDetails> SODetails = new ArrayList<>();

    public Date getSODate() {
        return SODate;
    }

    public void setSODate(Date SODate) {
        this.SODate = SODate;
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

    public double getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(double ledgerId) {
        LedgerId = ledgerId;
    }

    public double getItemAmount() {
        return ItemAmount;
    }

    public void setItemAmount(double itemAmount) {
        ItemAmount = itemAmount;
    }

    public double getDiscountAmount() {
        return DiscountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        DiscountAmount = discountAmount;
    }

    public double getGSTAmount() {
        return GSTAmount;
    }

    public void setGSTAmount(double GSTAmount) {
        this.GSTAmount = GSTAmount;
    }

    public double getExtraAmount() {
        return ExtraAmount;
    }

    public void setExtraAmount(double extraAmount) {
        ExtraAmount = extraAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getNarration() {
        return Narration;
    }

    public void setNarration(String narration) {
        Narration = narration;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getAmountInwords() {
        return AmountInwords;
    }

    public void setAmountInwords(String amountInwords) {
        AmountInwords = amountInwords;
    }

    public ArrayList<SalesOrderDetails> getSODetails() {
        return SODetails;
    }

    public void setSODetails(ArrayList<SalesOrderDetails> SODetails) {
        this.SODetails = SODetails;
    }
}
