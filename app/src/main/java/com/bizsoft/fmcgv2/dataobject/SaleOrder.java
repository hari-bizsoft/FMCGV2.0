package com.bizsoft.fmcgv2.dataobject;

import java.util.ArrayList;

/**
 * Created by GopiKing on 30-08-2017.
 */

public class SaleOrder {
    Long id;

    ArrayList<Product> products = new ArrayList<>();
    double receivedAmount;
    double balance;
    String gstType;
    String saleType;
    String tempId;
    double subTotal;
    double gst;
    String refCode;
    boolean synced;
    double discountPercentage;
    double roundOffValue;
    String grandTotalDiscountType;

    public String getGrandTotalDiscountType() {
        return grandTotalDiscountType;
    }

    public void setGrandTotalDiscountType(String grandTotalDiscountType) {
        this.grandTotalDiscountType = grandTotalDiscountType;
    }

    public double getRoundOffValue() {
        return roundOffValue;
    }

    public void setRoundOffValue(double roundOffValue) {
        this.roundOffValue = roundOffValue;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    String discountType;

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public double getDiscountValue() {

        return discountValue;
    }

    public void setDiscountValue(double discountValue) {
        this.discountValue = discountValue;
    }
    double discountValue;
    double grandTotal;


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

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public void setChequeBankName(String chequeBankName) {
        this.chequeBankName = chequeBankName;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    String chequeNo;
    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    String paymentMode;




    public Long getId() {
        return id;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public void setId(Long id) {
        this.id = id;

    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getGstType() {
        return gstType;
    }

    public void setGstType(String gstType) {
        this.gstType = gstType;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        this.grandTotal = grandTotal;
    }




}
