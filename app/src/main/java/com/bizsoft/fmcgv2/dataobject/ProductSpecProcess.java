/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

import com.bizsoft.fmcgv2.signalr.pojo.PDetailsItem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by GopiKing on 29-03-2018.
 */

public class ProductSpecProcess {

    private long Id;
    private Date Date;
    private int ProductId;
    private double Qty;
    private String ProductName;
    private double Available;

    boolean synced;

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }

    private ArrayList<ProductSpecProcessDetails> PDetails = new ArrayList<ProductSpecProcessDetails>();
    private int PSPTypeId = 2;

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public java.util.Date getDate() {
        return Date;
    }

    public void setDate(java.util.Date date) {
        Date = date;
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

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getAvailable() {
        return Available;
    }

    public void setAvailable(double available) {
        Available = available;
    }

    public ArrayList<ProductSpecProcessDetails> getPDetails() {
        return PDetails;
    }

    public void setPDetails(ArrayList<ProductSpecProcessDetails> PDetails) {
        this.PDetails = PDetails;
    }

    public int getPSPTypeId() {
        return PSPTypeId;
    }

    public void setPSPTypeId(int PSPTypeId) {
        this.PSPTypeId = PSPTypeId;
    }
}
