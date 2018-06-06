/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.Tables;

/**
 * Created by GopiKing on 28-12-2017.
 */


public class SaleDetail {

    Long Id;
    Long SalesId;
    Long SODId;
    int ProductId;
    int UOMId;
    float Quantity;
    Double UnitPrice;
    Double DiscountAmount;
    Double GSTAmount;
    Double Amount;
    boolean IsResale;

    public boolean isResale() {
        return IsResale;
    }

    public void setResale(boolean resale) {
        IsResale = resale;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Long getSalesId() {
        return SalesId;
    }

    public void setSalesId(Long salesId) {
        SalesId = salesId;
    }

    public Long getSODId() {
        return SODId;
    }

    public void setSODId(Long SODId) {
        this.SODId = SODId;
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
}
