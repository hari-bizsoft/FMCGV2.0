/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by GopiKing on 04-05-2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StockReport {

    @JsonProperty("LedgerName")
    String LedgerName;
    @JsonProperty("ProductName")
    String ProductName;
    @JsonProperty("SIn")
    long SIn;
    @JsonProperty("SOut")
    long SOut;
    @JsonProperty("Sal")
    long Sal;
    @JsonProperty("Available")
    long Available;
    @JsonProperty("SR")
    long SR;
    @JsonProperty("SRQtyForSales")
    long SRQtyForSales;
    @JsonProperty("SRQtyNForSales")
    long SRQtyNForSales;

    public long getAvailable() {
        return Available;
    }

    public void setAvailable(long available) {
        Available = available;
    }

    public long getSR() {
        return SR;
    }

    public void setSR(long SR) {
        this.SR = SR;
    }

    public long getSRQtyForSales() {
        return SRQtyForSales;
    }

    public void setSRQtyForSales(long SRQtyForSales) {
        this.SRQtyForSales = SRQtyForSales;
    }

    public long getSRQtyNForSales() {
        return SRQtyNForSales;
    }

    public void setSRQtyNForSales(long SRQtyNForSales) {
        this.SRQtyNForSales = SRQtyNForSales;
    }

    public String getLedgerName() {
        return LedgerName;
    }

    public void setLedgerName(String ledgerName) {
        LedgerName = ledgerName;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public long getSIn() {
        return SIn;
    }

    public void setSIn(long SIn) {
        this.SIn = SIn;
    }

    public long getSOut() {
        return SOut;
    }

    public void setSOut(long SOut) {
        this.SOut = SOut;
    }

    public long getSal() {
        return Sal;
    }

    public void setSal(long sal) {
        Sal = sal;
    }

    public static ArrayList<String> getProductNames() {
        ArrayList<String> p = new ArrayList<>();
        for(int i=0;i<Store.getInstance().stockReportList.size();i++)
        {
            p.add(Store.getInstance().stockReportList.get(i).getProductName());
        }
        return p;
    }
    public static HashMap<String,StockReport> getStockReportByName()
    {
        HashMap<String,StockReport> stockReportHashMap = new HashMap<String,StockReport>();

        for(int i=0;i<Store.getInstance().stockReportList.size();i++)
        {
            stockReportHashMap.put(Store.getInstance().stockReportList.get(i).getProductName(),Store.getInstance().stockReportList.get(i));
        }

        return stockReportHashMap;
    }
}
