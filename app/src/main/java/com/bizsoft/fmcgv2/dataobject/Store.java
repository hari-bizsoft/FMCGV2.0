/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import android.widget.EditText;
import android.widget.TextView;

import com.bizsoft.fmcgv2.ProductSpecActivity;
import com.bizsoft.fmcgv2.Tables.*;
import com.bizsoft.fmcgv2.Tables.Sale;
import com.bizsoft.fmcgv2.adapter.AddedProductAdapter;
import com.bizsoft.fmcgv2.service.Waiter;
import com.bizsoft.fmcgv2.signalr.pojo.ProductSpec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;

/**
 * Created by shri on 8/8/17.
 */

public class Store {

    public static final int SALE = 1;
    public static final int SALE_ORDER = 2;
    public static final int SALE_RETURN = 3
            ;
    public static final int NO_DISCOUNT = 1;
    public static final int DISCOUNT_FOR_GRABD_TOTAL = 2;

    public static String SERVER_HUB_CHAT = "ABServerHub";
    private static Store instance =null;
    public ArrayList<Company> companyList = new ArrayList<Company>();
    public ArrayList<Company> pureCompanyList = new ArrayList<Company>();
    public Long companyID;
    public ArrayList<Company> dealerList = new ArrayList<Company>();
    public Long dealerId;
    public User user = new User();
    public ArrayList<Customer> customerList = new ArrayList<Customer>();
    public ArrayList<StockGroup> stockGroupList = new ArrayList<StockGroup>();
    public ArrayList<Product>
            productList = new ArrayList<Product>();
    public AddCustomerResponse addCustomerResponse = new AddCustomerResponse();
    public String currentCustomer;
    public Long currentCustomerId;
    public String currentStockGroup = "0";
    public Long currentStockGroupId;
    public String currentProduct;
    public Long currentProductId;
    public ArrayList<Product> addedProductList = new ArrayList<Product>();
    public AddedProductAdapter addedProductAdapter;
    public ProductSaveResponse productSaveRes;
    public ArrayList<Product> addedProductForSalesOrder = new ArrayList<Product>();
    public ArrayList<Product> addedProductForSales = new ArrayList<Product>();
    public ArrayList<Product> addedProductForSalesReturn = new ArrayList<Product>();
    public int currentCustomerPosition;
    public boolean bluetoothStatus;
    public ArrayList<ProductReport> productReports = new ArrayList<ProductReport>();
    public String fromDate;
    public String toDate;
    public ReportData reportData;
    public TextView reportLabel;
    public String MyPREFERENCES= "loginDetails";
    public int companyPosition;
    public EditText fromCustomer;
    public TextView tenderAmount,subTotal,GST,grandTotal;
    public Company company = new Company();
    public TextView reprintSpinnerText;
    public  ArrayList<Receipt> receipts = new ArrayList<Receipt>();
    public ArrayList<Payment> payments = new ArrayList<>();
    public int idleTimeLimit = 1;
    public String serverStatus;
    public String appId;
    public String deviceName;
    public HubProxy mHubProxyCaller;
    public HubProxy mHubProxyReceiver;
    public HubConnection mHubConnectionReceiver;
    public HubConnection mHubConnectionCaller;
    public Handler mHandlerCaller;
    public Handler mHandlerReceiver;
    public Activity mainActvity;
    public ArrayList<AccountGroup> accountsGroupList = new ArrayList<AccountGroup>();
    public static Long currentAccGrpId;
    public static String currentAccGrpName;
    public Long currentLedgerId;
    public ArrayList<TransactionType> transactionTypeList = new ArrayList<TransactionType>();
    public ArrayList<TaxMaster> taxMasterList = new ArrayList<TaxMaster>();
    public double cashLedgerId;
    public double bankLedgerId;

    public ArrayList<SOPending> SOPendingList = new ArrayList<SOPending>();
    public ArrayList<com.bizsoft.fmcgv2.Tables.Sale> saleList = new ArrayList<Sale>();
    public SignalRFuture<Void> signalRFutureCaller;
    public String saleRefCode;
    public String saleOrderRefCode;
    public String saleReturnRefCode;
    public String saleReceiptRefCode;

    public ArrayList<String> messageList = new ArrayList<String>();
    public ArrayList<Notification> notificationList = new ArrayList<Notification>();
    public String dealerLogo;
    public Bitmap dealerLogoBitmap;
    public Waiter waiter;
    public Company dealer = new Company();
    public ArrayList<Bank> bankList = new ArrayList<Bank>();
    public ArrayList<ProductSpec> productSpecList = new ArrayList<ProductSpec>();
    public ArrayList<ProductSpecProcess> prodcutSpecProcess = new ArrayList<ProductSpecProcess>();
    public ArrayList<Bank> newBankList = new ArrayList<Bank>();
    public Long bankAccountGroupId;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    public ArrayList<UOM> UOM = new ArrayList<UOM>();
    public String companyLogo;
    public ArrayList<String> paymentModeTypeList = new ArrayList<>();
    public BluetoothDevice btDevice;
    public Context printerContext ;
    public double grandTotalRef;
    public  ArrayList<SOPending> SOPListToDelete = new ArrayList<>();
    public ArrayList<ProductSpec> productSpecMasterList = new ArrayList<>();
    public ArrayList<StockReport> stockReportList = new ArrayList<>();
    public int height;
    public int width;
    public ArrayList<Product> allProductList = new ArrayList<>();
    public ArrayList<MenuItems> menuItems = new ArrayList<MenuItems>();
    public HashMap<Long, Product> allProductHashMap = new HashMap<Long, Product>();
    public ArrayList<String> menuNameList = new ArrayList<>();
    public ArrayList<CreditLimitType> customerCreditLimitTypeList = new ArrayList<CreditLimitType>();
    public ArrayList<String> customerNameList = new ArrayList<String>();
    public final String discountTypeRM = "RM";
    public String discountTypePercentage = "% ";


    public static Store getInstance() {

        if(instance==null)
        {
            instance = new Store();
        }


        return instance;
    }
    public String domain = "app.denariusoft.com";
    public  String urlType = "full";
    public  String baseUrl = "http://"+domain+"/";



    public  String companyName=null;
    public String dealerName = null;

    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    public String getHost()
    {
        if(Store.getInstance().urlType.contains("full")) {
            Store.getInstance().baseUrl = "http://" + Store.getInstance().domain + "/";
        }
        else
        {
            Store.getInstance().baseUrl = "http://" + Store.getInstance().domain + "/";

        }
        String host = Store.getInstance().baseUrl;

        return host;
    }
}
