/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

import android.util.Log;

import com.bizsoft.fmcgv2.Tables.SOPending;
import com.bizsoft.fmcgv2.Tables.SalesOrder;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by shri on 9/8/17.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer {


        boolean IsReadOnly;
        boolean IsEnabled;
        String GroupCode;
        String AccountName;
        String ACType;
        Double OPBal;
    @JsonProperty("Logo")
        Byte[] Logo;

    @JsonProperty("Id")
        int Id;



    @JsonProperty("LedgerName")
        String LedgerName;
        String AccountGroup;
        Long AccountGroupId;
        String PersonIncharge;
        String AddressLine1;
        String AddressLine2;
        String CityName;
        String TelephoneNo;
        String MobileNo;
        String GSTNo;
        String EMailId;

        ArrayList<SalesOrder> sOPendingList = new ArrayList<SalesOrder>();



    @JsonProperty("LedgerId")
    int LedgerId;

    public int getLedgerId() {
        return LedgerId;
    }

    public void setLedgerId(int ledgerId) {
        LedgerId = ledgerId;
    }

    public ArrayList<SalesOrder> getsOPendingList() {
        return sOPendingList;
    }

    public void setsOPendingList(ArrayList<SalesOrder> sOPendingList) {
        this.sOPendingList = sOPendingList;
    }

    public boolean isSynced() {
        return synced;
    }

    public void setSynced(boolean synced) {
        this.synced = synced;
    }



    boolean synced;

    public ArrayList<Payment> getPayments() {
        return payments;
    }

    public void setPayments(ArrayList<Payment> payments) {
        this.payments = payments;
    }

    ArrayList<Receipt> receipts = new ArrayList<Receipt>();
        ArrayList<Payment> payments = new ArrayList<Payment>();

    public ArrayList<Receipt> getReceipts() {
        return receipts;
    }

    public void setReceipts(ArrayList<Receipt> receipts) {
        this.receipts = receipts;
    }

    public ArrayList<Product> getSaleReturn() {
        return saleReturn;
    }

    public void setSaleReturn(ArrayList<Product> saleReturn) {
        this.saleReturn = saleReturn;
    }

    double saleOrderReceivedAmount;
        double saleOrderBalance;
        ArrayList<Sale> salesOfCustomer = new ArrayList<Sale>();
        ArrayList<SaleOrder> saleOrdersOfCustomer = new ArrayList<SaleOrder>();
    ArrayList<SaleReturn> saleReturnOfCustomer = new ArrayList<SaleReturn>();

    public ArrayList<Sale> getSalesOfCustomer() {
        return salesOfCustomer;
    }

    public ArrayList<SaleReturn> getSaleReturnOfCustomer() {
        return saleReturnOfCustomer;
    }

    public void setSaleReturnOfCustomer(ArrayList<SaleReturn    > saleReturnOfCustomer) {
        this.saleReturnOfCustomer = saleReturnOfCustomer;
    }

    public void setSalesOfCustomer(ArrayList<Sale> salesOfCustomer) {
        this.salesOfCustomer = salesOfCustomer;
    }

    public ArrayList<SaleOrder> getSaleOrdersOfCustomer() {
        return saleOrdersOfCustomer;
    }


    public void setSaleOrdersOfCustomer(ArrayList<SaleOrder> saleOrdersOfCustomer) {
        this.saleOrdersOfCustomer = saleOrdersOfCustomer;
    }

    public Customer() {


    }

    public double getSaleOrderReceivedAmount() {
        return saleOrderReceivedAmount;
    }

    public void setSaleOrderReceivedAmount(double saleOrderReceivedAmount) {
        this.saleOrderReceivedAmount = saleOrderReceivedAmount;
    }

    public double getSaleOrderBalance() {
        return saleOrderBalance;
    }

    public void setSaleOrderBalance(double saleOrderBalance) {
        this.saleOrderBalance = saleOrderBalance;
    }

    public Customer(double receivedAmount, double balance) {
        this.receivedAmount = 0;
        this.balance = 0;
        this.saleOrderReceivedAmount = 0;
        this.saleOrderBalance = 0;

    }

    Long CreditLimitTypeId;
        String CreditLimitTypeName;

        double receivedAmount;

    public double getReceivedAmount() {
        return receivedAmount;
    }

    public void setReceivedAmount(double receivedAmount) {
        this.receivedAmount = receivedAmount;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    double balance;
    public ArrayList<Product> saleOrder = new ArrayList<Product>();
    public ArrayList<Product> saleReturn = new ArrayList<Product>();

    public ArrayList<Product> getSale() {
        return sale;
    }

    public ArrayList<Product> getSaleOrder() {
        return saleOrder;
    }

    public void setSaleOrder(ArrayList<Product> saleOrder) {
        this.saleOrder = saleOrder;
    }

    public void setSale(ArrayList<Product> sale) {
        this.sale = sale;
    }

    public ArrayList<Product> sale = new ArrayList<>();
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

    public String getGroupCode() {
        return GroupCode;
    }

    public void setGroupCode(String groupCode) {
        GroupCode = groupCode;
    }

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
        return OPBal;
    }

    public void setOPBal(Double OPBal) {
        this.OPBal = OPBal;
    }

    public Long getId() {
        return Long.valueOf(Id);
    }

    public void setId(Long id) {
        Id = id.intValue();
    }

    public String getLedgerName() {

        if(LedgerName==null)
        {
            if(getLedger()!=null) {
                return getLedger().getLedgerName();
            }else
            {
                return  "name unavailable ";
            }
        }
        else
        {
            return  " unavailable";
        }

    }

    public void setLedgerName(String ledgerName) {
        LedgerName = ledgerName;
    }

    public String getAccountGroup() {
        return AccountGroup;
    }

    public void setAccountGroup(String accountGroup) {
        AccountGroup = accountGroup;
    }

    public Long getAccountGroupId() {
        return AccountGroupId;
    }

    public void setAccountGroupId(Long accountGroupId) {
        AccountGroupId = accountGroupId;
    }

    public String getPersonIncharge() {

        if(PersonIncharge == null)
        {
            PersonIncharge = "";
        }


        return PersonIncharge;
    }

    public void setPersonIncharge(String personIncharge) {
        PersonIncharge = personIncharge;
    }

    public String getAddressLine1(

    ) {
        if(AddressLine1 == null)
        {
            AddressLine1 = "";
        }
        return AddressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        AddressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        if(AddressLine2 == null)
        {
            AddressLine2 = "";
        }
        return AddressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        AddressLine2 = addressLine2;
    }

    public String getCityName() {
        if(CityName == null)
        {
            CityName = "";
        }
        return CityName;
    }

    public void setCityName(String cityName) {
        CityName = cityName;
    }

    public String getTelephoneNo() {
        if(TelephoneNo == null)
        {
            TelephoneNo = "";
        }
        return TelephoneNo;
    }

    public void setTelephoneNo(String telephoneNo) {
        TelephoneNo = telephoneNo;
    }

    public String getMobileNo() {
        if(MobileNo == null)
        {
            MobileNo = "";
        }
        return MobileNo;
    }

    public void setMobileNo(String mobileNo) {
        MobileNo = mobileNo;
    }

    public String getGSTNo() {
        if(GSTNo == null)
        {
            GSTNo = "";
        }
        return GSTNo;
    }

    public void setGSTNo(String GSTNo) {
        this.GSTNo = GSTNo;
    }

    public String getEMailId() {
        return EMailId;
    }

    public void setEMailId(String EMailId) {
        this.EMailId = EMailId;
    }


    public Long getCreditLimitTypeId() {
        return CreditLimitTypeId;
    }

    public void setCreditLimitTypeId(Long creditLimitTypeId) {
        CreditLimitTypeId = creditLimitTypeId;
    }

    public String getCreditLimitTypeName() {
        return CreditLimitTypeName;
    }

    public void setCreditLimitTypeName(String creditLimitTypeName) {
        CreditLimitTypeName = creditLimitTypeName;
    }

    public String getOPCr() {
        return OPCr;
    }

    public void setOPCr(String OPCr) {
        this.OPCr = OPCr;
    }

    public String getOPDr() {
        return OPDr;
    }

    public void setOPDr(String OPDr) {
        this.OPDr = OPDr;
    }

    public String getLedgerCode() {
        return LedgerCode;
    }

    public void setLedgerCode(String ledgerCode) {
        LedgerCode = ledgerCode;
    }


    @JsonProperty("Ledger")
    Ledger Ledger;

    public com.bizsoft.fmcgv2.dataobject.Ledger getLedger() {
        return Ledger;
    }

    public void setLedger(com.bizsoft.fmcgv2.dataobject.Ledger ledger) {
        Ledger = ledger;
    }

    String OPCr;
        String OPDr;
        String LedgerCode;




    public static Comparator<Customer> COMPARE_BY_LEDGER_NAME = new Comparator<Customer>() {
        public int compare(Customer one, Customer other) {
            Log.d("comparator 1",one.getLedger().getLedgerName());
            Log.d("comparator 2",other.getLedger().getLedgerName());
            return one.getLedgerName().toLowerCase().compareTo(other.getLedgerName().toLowerCase());
        }
    };
    public static ArrayList<String> getCustomerList()
    {
        ArrayList<String> names = new ArrayList<>();
        for(int i=0;i<Store.getInstance().customerList.size();i++)
        {
            Long id = Store.getInstance().customerList.get(i).getLedger().getId();

            if(id==0)
            {

            }
            names.add(Store.getInstance().customerList.get(i).getLedger().getLedgerName()+" - "+Store.getInstance().customerList.get(i).getLedger().getId());

        }

        return names;
    }
    public static ArrayList<String> getCustomerNameList()
    {
        ArrayList<String> names = new ArrayList<>();
        for(int i=0;i<Store.getInstance().customerList.size();i++)
        {

            names.add(Store.getInstance().customerList.get(i).getLedger().getLedgerName());

        }

        return names;
    }


    public ArrayList<SOPending> SOPListDelete = new ArrayList<SOPending>();

    public ArrayList<SOPending> getSOPListDelete() {
        return SOPListDelete;
    }

    public void setSOPListDelete(ArrayList<SOPending> SOPListDelete) {
        this.SOPListDelete = SOPListDelete;
    }
}

