/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

/**
 * Created by GopiKing on 29-03-2018.
 */

public class ProductSpecProcessDetails {
    public long Id ;
    public long PSId;
    public int ProductId;
    public double Qty ;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public long getPSId() {
        return PSId;
    }

    public void setPSId(long PSId) {
        this.PSId = PSId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(double qty) {
        Qty = qty;
    }
}
