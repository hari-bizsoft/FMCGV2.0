package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GopiKing on 28-03-2018.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class CFiles {

    @JsonProperty("Id")
    private double Id;
    @JsonProperty("CompanyId")
    private double CompanyId;
    @JsonProperty("AttchmentCode")
    private String AttchmentCode;
    @JsonProperty("Image")
    private String Image;


    public double getId() {
        return Id;
    }

    public void setId(double id) {
        Id = id;
    }

    public double getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(double companyId) {
        CompanyId = companyId;
    }

    public String getAttchmentCode() {
        return AttchmentCode;
    }

    public void setAttchmentCode(String attchmentCode) {
        AttchmentCode = attchmentCode;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }
}
