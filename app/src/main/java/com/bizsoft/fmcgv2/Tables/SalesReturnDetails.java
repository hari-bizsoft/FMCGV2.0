/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.Tables;

/**
 * Created by GopiKing on 28-02-2018.
 */

public class SalesReturnDetails {

    Long Id;
    int ProductId;
    int UOMId;
    float Quantity;
    Double UnitPrice;
    Double DiscountAmount;
    Double GSTAmount;
    Double Amount;
    boolean IsResale;
    String Particulars;

    public String getParticulars() {
        return Particulars;
    }

    public void setParticulars(String particulars) {
        Particulars = particulars;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }


    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getUOMId() {
        return UOMId;
    }

    public void setUOMId(int UOMId) {
        this.UOMId = UOMId;
    }

    public float getQuantity() {
        return Quantity;
    }

    public void setQuantity(float quantity) {
        Quantity = quantity;
    }

    public Double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        UnitPrice = unitPrice;
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

    public Double getAmount() {
        return Amount;
    }

    public void setAmount(Double amount) {
        Amount = amount;
    }

    public boolean isResale() {
        return IsResale;
    }

    public void setResale(boolean resale) {
        IsResale = resale;
    }
}
