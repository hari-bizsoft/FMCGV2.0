package com.bizsoft.fmcgv2.dataobject;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * Created by shri on 9/8/17.
 */

@JsonIgnoreProperties(ignoreUnknown = true,value = {"UnderStockGroup"})
public class StockGroup {

            String AccountPath;
    @JsonProperty("Id")
            Long Id;
    @JsonProperty("StockGroupName")
            String StockGroupName;
    @JsonProperty("GroupCode")
            String GroupCode;
            String StockGroupNameWithCode;
    @JsonProperty("CompanyId")
            Long CompanyId;
    @JsonProperty("UnderGroupId")
            Long UnderGroupId;
    @JsonProperty("UnderStockGroup")
            String UnderStockGroup;

    public String getAccountPath() {
        return AccountPath;
    }

    public void setAccountPath(String accountPath) {
        AccountPath = accountPath;
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }


    public String getStockGroupName() {
        return StockGroupName;
    }

    public void setStockGroupName(String stockGroupName) {
        StockGroupName = stockGroupName;
    }

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String groupCode) {
        GroupCode = groupCode;
    }

    public String getStockGroupNameWithCode() {
        return StockGroupNameWithCode;
    }

    public void setStockGroupNameWithCode(String stockGroupNameWithCode) {
        StockGroupNameWithCode = stockGroupNameWithCode;
    }

    public Long getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(Long companyId) {
        CompanyId = companyId;
    }

    public Long getUnderGroupId() {
        return UnderGroupId;
    }

    public void setUnderGroupId(Long underGroupId) {
        UnderGroupId = underGroupId;
    }

    public String getUnderStockGroup() {
        return UnderStockGroup;
    }

    public void setUnderStockGroup(String underStockGroup) {
        UnderStockGroup = underStockGroup;
    }

    public String getSubStockGroup() {
        return SubStockGroup;
    }

    public void setSubStockGroup(String subStockGroup) {
        SubStockGroup = subStockGroup;
    }

    public String getUnderStockGroupName() {
        return underStockGroupName;
    }

    public void setUnderStockGroupName(String underStockGroupName) {
        this.underStockGroupName = underStockGroupName;
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

    public boolean isPurchase() {
        return IsPurchase;
    }

    public void setPurchase(boolean purchase) {
        IsPurchase = purchase;
    }

    public boolean isSale() {
        return IsSale;
    }

    public void setSale(boolean sale) {
        IsSale = sale;
    }
    @JsonProperty("SubStockGroup")
    String SubStockGroup;
    @JsonProperty("Company")
            Company Company;
    @JsonProperty("underStockGroupName")
            String underStockGroupName;
    @JsonProperty("IsReadOnly")
            boolean IsReadOnly;
    @JsonProperty("IsEnabled")
            boolean IsEnabled;
    @JsonProperty("IsPurchase")
            boolean IsPurchase;

    public com.bizsoft.fmcgv2.dataobject.Company getCompany() {
        return Company;
    }

    public void setCompany(com.bizsoft.fmcgv2.dataobject.Company company) {
        Company = company;
    }

    @JsonProperty("IsSale")
    boolean IsSale;


    public static ArrayList<String> getNames()
    {
        ArrayList<StockGroup> stockGroup = Store.getInstance().stockGroupList;
        ArrayList<String> strings = new ArrayList<>();
        for (int i=0;i<stockGroup.size();i++)
        {


            strings.add(stockGroup.get(i).getStockGroupName());


        }
        return strings;
    }
    public static ArrayList<String> getNamesWithID()
    {
        ArrayList<StockGroup> stockGroup = Store.getInstance().stockGroupList;
        ArrayList<String> strings = new ArrayList<>();
        for (int i=0;i<stockGroup.size();i++)
        {


            strings.add(stockGroup.get(i).getStockGroupName()+"-"+stockGroup.get(i).getId());


        }
        return strings;
    }


}
