package com.bizsoft.fmcgv2.Tables;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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

}
