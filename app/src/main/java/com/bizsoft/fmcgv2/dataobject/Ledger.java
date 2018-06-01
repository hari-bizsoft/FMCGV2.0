package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GopiKing on 27-12-2017.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ledger {


            String AccountName;
            String ACType;
            Double OPBal;

    @JsonProperty("PersonIncharge")
    String PersonIncharge;

    @JsonProperty("AddressLine1")
    String AddressLine1;
    @JsonProperty("AddressLine2")
    String AddressLine2;
    @JsonProperty("CityName")
    String CityName;
    @JsonProperty("TelephoneNo")
    String TelephoneNo;
    @JsonProperty("MobileNo")
    String MobileNo;
    @JsonProperty("GSTNo")
    String GSTNo;
    @JsonProperty("EMailId")
    String EMailId;
    @JsonProperty("CreditAmount")
    double CreditAmount;

    @JsonProperty("CreditLimit")
    double CreditLimit;
    @JsonProperty("CreditLimitType")
    public
    CreditLimitType CreditLimitType;

    public String getEMailId() {
        return EMailId;
    }

    public void setEMailId(String EMailId) {
        this.EMailId = EMailId;
    }

    public String getPersonIncharge()
    {
        if(PersonIncharge==null)
        {
            PersonIncharge = "";
        }
        return PersonIncharge;
    }

    public double getCreditAmount() {
        return CreditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        CreditAmount = creditAmount;
    }

    public double getCreditLimit() {
        return CreditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        CreditLimit = creditLimit;
    }

    public com.bizsoft.fmcgv2.dataobject.CreditLimitType getCreditLimitType() {
        return CreditLimitType;
    }

    public void setCreditLimitType(com.bizsoft.fmcgv2.dataobject.CreditLimitType creditLimitType) {
        CreditLimitType = creditLimitType;
    }

    public void setPersonIncharge(String personIncharge) {
        PersonIncharge = personIncharge;
    }

    public String getAddressLine1() {
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getCityName() {

        if(CityName==null)
        {
            CityName = "";
        }
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getTelephoneNo() {
        return TelephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        TelephoneNo = telephoneNo;
    }

    public String getMobileNo() {
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getGSTNo() {
        if(GSTNo==null)
        {
            GSTNo = "";
        }
        return GSTNo;
    }

    public void setGSTNo(String GSTNo) {
        this.GSTNo = GSTNo;
    }

    public Long getAccountGroupId() {
        return AccountGroupId;
    }

    public void setAccountGroupId(Long accountGroupId) {
        AccountGroupId = accountGroupId;
    }

    Long AccountGroupId;

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getACType() {
        return ACType;
    }

    public void setACType(String ACType) {
        this.ACType = ACType;
    }

    public Double getOPBal() {

        if(OPBal==null)
        {
            OPBal = 0.0;
        }
        return OPBal;
    }

    public void setOPBal(Double OPBal) {
        this.OPBal = OPBal;
    }

    public Long getId() {


        return Long.valueOf(((Integer) Id));
    }

    public void setId(Long id) {
        Id = id.intValue();
    }

    public String getLedgerName() {
        return LedgerName;
    }

    public void setLedgerName(String ledgerName) {
        LedgerName = ledgerName;
    }
    @JsonProperty("Id")
    int Id;
    @JsonProperty("LedgerName")
           String LedgerName;

}
