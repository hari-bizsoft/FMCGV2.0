package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by GopiKing on 17-02-2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxMaster {

    @JsonProperty("Id")
    private int Id;
    @JsonProperty("LedgerId")
    private int LedgerId;
    @JsonProperty("TaxPercentage")
    private double TaxPercentage;
    @JsonProperty("TaxAmount")
    private double TaxAmount;
    @JsonProperty("Ledger")
    private Ledger Ledger;

    @JsonProperty("IsReadOnly")
    private boolean IsReadOnly;

    @JsonProperty("IsEnabled")
    private boolean IsEnabled;

    @JsonProperty("Status")
    private boolean Status;

    @JsonProperty("TaxName")
    private String TaxName;

    @JsonProperty("CreatedBy")
    private int CreatedBy;

    @JsonProperty("DeleteBy")
    private int DeleteBy;

    @JsonProperty("ModifyBy")
    private int ModifyBy;

    @JsonProperty("CreatedAt")
    private String CreatedAt;

    @JsonProperty("DeletedAt")
    private String DeletedAt;

    @JsonProperty("ModifiedAt")
    private String ModifiedAt;

    public int getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(int ledgerId) {
        LedgerId = ledgerId;
    }

    public double getTaxPercentage() {
        return TaxPercentage;
    }

    public void setTaxPercentage(double taxPercentage) {
        TaxPercentage = taxPercentage;
    }

    public double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        TaxAmount = taxAmount;
    }

    public com.bizsoft.fmcgv2.dataobject.Ledger getLedger() {
        return Ledger;
    }

    public void setLedger(com.bizsoft.fmcgv2.dataobject.Ledger ledger) {
        Ledger = ledger;
    }

    public boolean isReadOnly() {
        return IsReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        IsReadOnly = readOnly;
    }

    public boolean isEnabled() {
        return IsEnabled;
    }

    public void setEnabled(boolean enabled) {
        IsEnabled = enabled;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public String getTaxName() {
        return TaxName;
    }

    public void setTaxName(String taxName) {
        TaxName = taxName;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public int getDeleteBy() {
        return DeleteBy;
    }

    public void setDeleteBy(int deleteBy) {
        DeleteBy = deleteBy;
    }

    public int getModifyBy() {
        return ModifyBy;
    }

    public void setModifyBy(int modifyBy) {
        ModifyBy = modifyBy;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getDeletedAt() {
        return DeletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        DeletedAt = deletedAt;
    }

    public String getModifiedAt() {
        return ModifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        ModifiedAt = modifiedAt;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
