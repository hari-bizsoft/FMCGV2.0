/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.Tables;

import com.bizsoft.fmcgv2.dataobject.Store;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;

/**
 * Created by GopiKing on 28-12-2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransactionType {

    double Id;

    public double getId() {
        return Id;
    }

    public void setId(double id) {
        Id = id;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    java.lang.String  Type;

    public static void getAll() {
        ArrayList<String> paymentModeTypeList = new ArrayList<String>();


        for (int i = 0; i < Store.getInstance().transactionTypeList.size(); i++) {


            if (Store.getInstance().transactionTypeList.get(i).getType().toLowerCase().contains("credit")) {
                paymentModeTypeList.add("PNT (Payment Next Trip)");
            } else {
                paymentModeTypeList.add(Store.getInstance().transactionTypeList.get(i).getType());
            }

        }
        Store.getInstance().paymentModeTypeList = paymentModeTypeList;
    }
}
