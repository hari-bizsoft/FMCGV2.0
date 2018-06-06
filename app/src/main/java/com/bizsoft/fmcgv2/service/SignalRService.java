/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Toast;

import com.bizsoft.fmcgv2.DownloadDataActivity;
import com.bizsoft.fmcgv2.LoginActivity;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.Tables.Bank;
import com.bizsoft.fmcgv2.Tables.Receipt;
import com.bizsoft.fmcgv2.Tables.SOPending;
import com.bizsoft.fmcgv2.Tables.SalesOrder;
import com.bizsoft.fmcgv2.Tables.SalesOrderDetails;
import com.bizsoft.fmcgv2.Tables.SalesReturn;
import com.bizsoft.fmcgv2.Tables.TransactionType;
import com.bizsoft.fmcgv2.dataobject.AccountGroup;
import com.bizsoft.fmcgv2.dataobject.Company;
import com.bizsoft.fmcgv2.dataobject.CompanyDetails;
import com.bizsoft.fmcgv2.dataobject.CreditLimitType;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Ledger;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.Tables.Sale;
import com.bizsoft.fmcgv2.dataobject.ProductSpecProcess;
import com.bizsoft.fmcgv2.dataobject.ReceiverResponse;
import com.bizsoft.fmcgv2.dataobject.StockGroup;
import com.bizsoft.fmcgv2.dataobject.StockReport;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.dataobject.TaxMaster;
import com.bizsoft.fmcgv2.dataobject.UOM;
import com.bizsoft.fmcgv2.dataobject.User;
import com.bizsoft.fmcgv2.signalr.pojo.ProductSpec;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.StateChangedCallback;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

import static android.content.Context.MODE_PRIVATE;
import static com.bizsoft.fmcgv2.MainActivity.getLocalBluetoothName;
import static com.bizsoft.fmcgv2.MainActivity.mHubProxyCaller;
import static com.bizsoft.fmcgv2.dataobject.Store.SERVER_HUB_CHAT;
import static com.bizsoft.fmcgv2.service.BizUtils.prettyJson;

/**
 * Created by GopiKing on 22-12-2017.
 */

public class SignalRService {
    static SharedPreferences sharedpreferences ;
    private static String MyPREFERENCES = "ACTIVATION_KEY";
    static SharedPreferences.Editor editor;
    private static String AppId = "AppId";

    static final String TAG = "SIGNAL R SERVICE";
    private static Object RConnectionId;

    public static ArrayList<Company>  getCompanyDetails(Context context)
    {
        ArrayList<LinkedTreeMap> companyCollection = new ArrayList<LinkedTreeMap>();
        try {
            companyCollection = mHubProxyCaller.invoke(companyCollection.getClass(),"CompanyDetail_List").get();

            System.out.println("Obj ---------"+companyCollection.size());

            ArrayList<Company> companyArrayList = new ArrayList<Company>();

            for(int i=0;i<companyCollection.size();i++)
            {
                Company company = new Company();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                company  = mapper.convertValue(companyCollection.get(i),Company.class);

                BizUtils.prettyJson("company i th = ",companyCollection.get(i));

                System.out.println("Company Id"+company.getId());


                if(company.isActive()) {
                    System.out.println("adding---------" + "----" + i);
                    companyArrayList.add(company);
                }else
                {
                    System.out.println("removed---------" + "----" + i);

                }
            }

            Store.getInstance().companyList = companyArrayList;
            BizLogger.generateNoteOnSD( "Company Size : "+String.valueOf(Store.getInstance().companyList.size()));

            if (Store.getInstance().companyList.size() != 0) {
                Store.getInstance().serverStatus = "Online";

            } else {
                Store.getInstance().serverStatus = "Offline";
                Log.d(TAG, "No Company list found");

                Toast.makeText(context, "No company List Found", Toast.LENGTH_SHORT).show();
                //  Intent intent = new Intent(context,LoginActivity.class);
                //context.startActivity(intent);
            }



            for(int i=0;i<Store.getInstance().companyList.size();i++)
            {
                SharedPreferences prefs = context.getSharedPreferences(Store.getInstance().MyPREFERENCES, MODE_PRIVATE);

                String dealerId = prefs.getString(context.getString(R.string.dealerID), "0");



                if(Long.valueOf(dealerId).compareTo(Store.getInstance().companyList.get(i).getId())==0)
                {

                    Store.getInstance().dealer = Store.getInstance().companyList.get(i);

                    try {

                        Store.getInstance().dealerLogo = companyLogo();
                    }
                    catch (Exception e)
                    {
                        System.err.println(e);
                    }

                    try {
                        System.out.println("Under Company Id = " + Store.getInstance().companyList.get(i).getUnderCompanyId());
                        Store.getInstance().companyLogo = companyLogo(Store.getInstance().companyList.get(i).getUnderCompanyId());
                    }
                    catch (Exception e)
                    {
                        System.err.println(e);
                    }





                }
            }




    }
    catch (Exception e) {
        e.printStackTrace();
    }
        System.out.println("____________________"+Store.getInstance().companyList.size());
        try {
            BizUtils.storeAsJSON("Company",BizUtils.getJSON("company",Store.getInstance().companyList));
        } catch (ClassNotFoundException e) {
            System.out.println("Exception in writing to DB :Customer "+e);
        }
        Store.getInstance().dealerList.clear();
        for(int i=0;i<Store.getInstance().companyList.size();i++)
        {
            if(Store.getInstance().companyList.get(i).getUnderCompanyId()!=null && Store.getInstance().companyList.get(i).getCompanyType().toLowerCase().contains("dealer"))
            {
                System.out.println("UnderCompanyId = "+Store.getInstance().companyList.get(i).getUnderCompanyId());
                System.out.println("Dealer Name = "+Store.getInstance().companyList.get(i).getCompanyName());
                Store.getInstance().dealerList.add(Store.getInstance().companyList.get(i));

            }

        }
        System.out.println("Dealer Size ----"+Store.getInstance().dealerList.size());

        int activeCount = 0;
        for(int j=0;j<Store.getInstance().dealerList.size();j++)
        {
        if(Store.getInstance().dealerList.get(j).isActive())
        {
            activeCount++;
        }
        }
        System.out.println("active count----"+activeCount);
        return Store.getInstance().companyList;


    }
    public static boolean login(Context context,String dlrName,String usrName,String passWord)
    {
        boolean status = false;
        //-----------------------------------------------
        SharedPreferences prefs = context.getSharedPreferences(Store.getInstance().MyPREFERENCES, MODE_PRIVATE);
        boolean isLogged = prefs.getBoolean(context.getString(R.string.isLogged), false);
        String username = prefs.getString(context.getString(R.string.username), "");
        String password = prefs.getString(context.getString(R.string.password), "");
        String dealerName = prefs.getString(context.getString(R.string.dealerName), "0");
        String dealerId = prefs.getString(context.getString(R.string.dealerID), "0");
        String companyId = prefs.getString(context.getString(R.string.companyID), "0");
        String companyGSTnO = prefs.getString(context.getString(R.string.gstNo), "0");


        System.out.println("company name =============================="+prefs.getString(context.getString(R.string.phoneNumber), "0"));
        System.out.println("company address line 1 =============================="+prefs.getString(context.getString(R.string.companyAddressLine1), "0"));
        System.out.println("company address line 2 =============================="+prefs.getString(context.getString(R.string.companyAddressLine2), "0"));
        System.out.println("company gst =============================="+prefs.getString(context.getString(R.string.gstNo), "0"));
        System.out.println("company mail =============================="+prefs.getString(context.getString(R.string.email), "0"));
        System.out.println("company postalcode =============================="+prefs.getString(context.getString(R.string.postal_code), "0"));


        Store.getInstance().companyPosition = Integer.parseInt(prefs.getString(context.getString(R.string.companyPosition), "0"));

        String year = BizUtils.getCurrentYear();
        System.out.println("Acc year  =============================="+year);
        if(dealerName!=null && !dealerName.equals(""))
        {

            Store.getInstance().dealerName = dealerName;
            System.out.println("dealerName = "+ Store.getInstance().dealerName);
            Store.getInstance().dealerId = Long.valueOf(dealerId);
            Store.getInstance().companyID = Long.valueOf(companyId);

        }

        //-----------------------------------------------------------SL

        System.out.println("isLogged = "+isLogged);
        System.out.println("mHubProxyCaller = "+ Store.getInstance().mHubProxyCaller);
        if(isLogged)
        {
            try {
                if(usrName!=null && passWord!=null & dlrName !=null)
                {
                    System.out.println("Login details____NEWLOGIN"+Store.getInstance().user);

                    Store.getInstance().user = Store.getInstance().mHubProxyCaller.invoke(User.class,"userAccount_Login",year,dlrName,usrName,passWord).get();
                    if(Store.getInstance().user!=null)
                    {
                        if(Store.getInstance().user.getId()!=0) {
                            status = true;
                        }
                        else
                        {
                            status = false;
                            Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        status = false;
                        Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Store.getInstance().user = Store.getInstance().mHubProxyCaller.invoke(User.class,"userAccount_Login",year,dealerName,username,password).get();
                    System.out.println("Login details____OLDLOGIN"+Store.getInstance().user);
                    if(Store.getInstance().user!=null)
                    {
                        if(Store.getInstance().user.getId()!=null) {


                            if(Store.getInstance().user.getId()==0.0)
                            {
                                status = false;
                            }
                            else
                            {
                                status = true;
                            }
                        }
                        else {
                            status = false;
                        }
                    }
                    else
                    {
                        status = false;
                        Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                    }
                }

                prettyJson("User ",Store.getInstance().user);



            } catch (InterruptedException e) {
                e.printStackTrace();
                status = false;
            } catch (ExecutionException e) {
                e.printStackTrace();
                status = false;
            }

        }
        else
        {
            ArrayList<Company> s = getCompanyDetails(context);
            System.out.print("--Test--"+s.size());
            Store.getInstance().serverStatus = "Online";
            Intent mainIntent = new Intent(context,LoginActivity.class);
            context.startActivity(mainIntent);
            Store.getInstance().mainActvity.finish();
        }

        return  status;
        //---------------------------------------------------------------------------
    }

    public static boolean newLogin(Context context, String dlrName, String usrName, String passWord)
    {
        boolean status = false;
        String year = BizUtils.getCurrentYear();

        if(usrName!=null && passWord!=null & dlrName !=null)
        {

            User obj = new User();
            try {
                obj = Store.getInstance().mHubProxyCaller.invoke(obj.getClass(),"userAccount_Login",year,dlrName,usrName,passWord).get();

                System.out.println("---user info---"+obj);


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if(obj!=null)
            {
                Store.getInstance().user = (User) obj;

                prettyJson("User",Store.getInstance().user);

                if(Store.getInstance().user.getId()!=null) {
                    status = true;
                    System.out.println("Login details____NEWLOGIN"+Store.getInstance().user);
                    if(Store.getInstance().user.getId()==0.0)
                    {
                        status = false;
                    }
                    else
                    {
                        status = true;
                    }
                }
                else {
                    status = false;
                }
            }
        }


        return  status;

    }

    public static void customerList(Context context)
    {

        ArrayList<LinkedTreeMap> customreCollection = new ArrayList<LinkedTreeMap>();
        try {

            customreCollection = Store.getInstance().mHubProxyCaller.invoke(customreCollection.getClass(),"Customer_List").get();


            System.out.println("customertCollection---"+customreCollection);

            Store.getInstance().customerList.clear();

            for(int i=0;i<customreCollection.size();i++)
            {

                Customer customer = new Customer();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                customer = mapper.convertValue(customreCollection.get(i),Customer.class);
                System.out.println("Customer Id"+customer.getId());
                System.out.println("Customer ledger"+customer.getLedger());
                System.out.println("Customer ledger name"+customer.getLedgerName());
              //  BizUtils.prettyJson("Customer",customreCollection.get(i));


                //BizUtils.prettyJson("Customer ",customer);
                customer.setSynced(true);


                Store.getInstance().customerList.add(customer);

                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run()
                    {
                        DownloadDataActivity.customers.setText(String.valueOf(Store.getInstance().customerList.size()));
                    }
                });





            }


        } catch (InterruptedException e) {


        } catch (ExecutionException e) {
            e.printStackTrace();
        }

            System.out.println("customer_list----"+customreCollection.size());
    }

    public static Customer getCustomer(Long id)
    {

        ArrayList<LinkedTreeMap> customreCollection = new ArrayList<LinkedTreeMap>();
        try {

            customreCollection = Store.getInstance().mHubProxyCaller.invoke(customreCollection.getClass(),"Customer_List").get();


            System.out.println("customertCollection---"+customreCollection);


            Customer customer1 = new Customer();

            for(int i=0;i<customreCollection.size();i++)
            {

                Customer customer = new Customer();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                customer = mapper.convertValue(customreCollection.get(i),Customer.class);
                System.out.println("Customer Id"+customer.getId() + "======"+ id);
                System.out.println("Customer ledger"+customer.getLedger());
                System.out.println("Customer ledger name"+customer.getLedgerName());
                //  BizUtils.prettyJson("Customer",customreCollection.get(i));

                if(customer.getId().compareTo(id)==0)
                {
                    System.out.println("Customer Id  matched "+customer.getId());
                    customer1 = customer;
                }

                //BizUtils.prettyJson("Customer ",customer);
            }

            return customer1;


        } catch (InterruptedException e) {


        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("customer_list----"+customreCollection.size());
        return null;
    }

    public static void productList(Context context)
    {

        ArrayList<LinkedTreeMap> companyCollection = new ArrayList<LinkedTreeMap>();

        try {
            Log.d("Connection State", String.valueOf(Store.getInstance().mHubConnectionCaller.getState()));
            companyCollection = Store.getInstance().mHubProxyCaller.invoke(companyCollection.getClass(),"Product_List").get();
          //  BizUtils.prettyJson("Products List",companyCollection);
            Store.getInstance().productList.clear();
            Store.getInstance().allProductList.clear();
            for(int i=0;i<companyCollection.size();i++)
            {
                Product product = new Product();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                product  = mapper.convertValue(companyCollection.get(i),Product.class);
                System.out.println("Product Id"+product.getId());


                BizUtils.prettyJson("product ind item",product);
                product.setQty(Long.valueOf(0));
                Store.getInstance().allProductList.add(product);
                if(product.getStockGroup().isSale() && product.isDealer()) {
                    product.setSynced(true);
                    product.setQty(Long.valueOf(0));
                    product.sellingRateRef = product.getSellingRate();
                    Store.getInstance().productList.add(product);
                }
                ((Activity)context).runOnUiThread(new Runnable() {
                    public void run()
                    {
                        DownloadDataActivity.products.setText(String.valueOf(Store.getInstance().productList.size()));
                    }
                });



            }
            for(int c=0;c<Store.getInstance().productList.size();c++)
            {

                System.out.println("Item id"+Store.getInstance().productList.get(c).getId());
                System.out.println("Item code"+Store.getInstance().productList.get(c).getItemCode());
                System.out.println("item uom code"+Store.getInstance().productList.get(c).getUOMId());
            }
            for(int c=0;c<Store.getInstance().productList.size();c++)
            {
                for(int d = 0;d<Store.getInstance().SOPendingList.size();d++) {

                    for(int i=0;i<Store.getInstance().SOPendingList.get(d).getSODetails().size();i++)
                    {
                        ArrayList<SalesOrderDetails> saleOrderDetails = (ArrayList<SalesOrderDetails>) Store.getInstance().SOPendingList.get(d).getSODetails();
                        if(saleOrderDetails.get(i).getProductId() == Store.getInstance().productList.get(c).getId())
                        {
                            Log.d("Product","Matched ");
                            Log.d("Product Name",Store.getInstance().productList.get(c).getProductName());
                            Store.getInstance().SOPendingList.get(d).getSODetails().get(i).setProductName(Store.getInstance().productList.get(c).getProductName());



                        }
                    }

                }

            }

         try {
             for (int d = 0; d < Store.getInstance().SOPendingList.size(); d++) {
                 Log.d("SO REF CODE", Store.getInstance().SOPendingList.get(d).getRefNo());
                 ArrayList<SalesOrderDetails> saleOrderDetails = (ArrayList<SalesOrderDetails>) Store.getInstance().SOPendingList.get(d).getSODetails();

                 for (int c = 0; c < saleOrderDetails.size(); c++) {
                     Log.d("SO PRODUCT NAME", saleOrderDetails.get(c).getProductName());

                 }


             }
         }catch (Exception e)
         {

         }

        } catch (InterruptedException e) {


        } catch (ExecutionException e) {

            e.printStackTrace();
        }

    }
    public static void stockHomeList()
    {
        ArrayList<LinkedTreeMap> companyCollection = new ArrayList<LinkedTreeMap>();
        try {
            companyCollection = Store.getInstance().mHubProxyCaller.invoke(companyCollection.getClass(),"StockGroup_List").get();
           // BizUtils.prettyJson("Stock Group",companyCollection);
            Store.getInstance().stockGroupList.clear();
            for(int i=0;i<companyCollection.size();i++)
            {
                StockGroup stockGroup = new StockGroup();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                stockGroup  = mapper.convertValue(companyCollection.get(i),StockGroup.class);
                System.out.println("stockGroup Id"+stockGroup.getId());

                if(stockGroup.isSale()) {
                    Store.getInstance().stockGroupList.add(stockGroup);
                }

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("customertList----"+Store.getInstance().stockGroupList.size());
    }
    public static void productSpecList()
    {
        ArrayList<LinkedTreeMap> companyCollection = new ArrayList<LinkedTreeMap>();
        try {
            companyCollection = Store.getInstance().mHubProxyCaller.invoke(companyCollection.getClass(),"Product_Spec_Dispatch_List").get();

            Store.getInstance().productSpecList.clear();
            for(int i=0;i<companyCollection.size();i++)
            {
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

                ProductSpec productSpec = new ProductSpec();

                  productSpec  = mapper.convertValue(companyCollection.get(i),ProductSpec.class);
                  System.out.println("stockGroup Id"+productSpec.getId());

                prettyJson("productSpec item",productSpec);


                Store.getInstance().productSpecList.add(productSpec);

            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("productSpecList----"+Store.getInstance().productSpecList.size());
    }

    public static Customer saveCustomer(Customer customer) {

        Object o = new Object();



        Customer customerResponse = new Customer();
        Gson gson = new Gson();
        try {
            BizUtils.prettyJson("Customer",customer);
            System.out.println("---------------saving--"+customer.getLedger().getLedgerName());

            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Customer_Save",customer).get();
            System.out.println("---------------class type--"+o.getClass());



            final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            customerResponse  = mapper.convertValue(o,Customer.class);
            System.out.println("customer Id"+customerResponse.getId());

            BizLogger.generateNoteOnSD( BizUtils.getCurrentDatAndTimeInDF()+" | Customer saved Id :  "+customerResponse.getId());




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return customerResponse;

    }
    public static boolean saveCompany(Company company) {

        Object o = new Object();
        boolean status = false;
        Company customerResponse = new Company();
        Gson gson = new Gson();
        try {
            CompanyDetails companyDetails = new CompanyDetails();
            companyDetails.Id = company.getId().intValue();
            prettyJson("company details",company);
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"CompanyDetail_Save",company).get();

            if((Double)o != 0)
            {
                System.out.println("---------------dealer saved --"+o);
                status = true;
                BizLogger.generateNoteOnSD( BizUtils.getCurrentDatAndTimeInDF()+" | company saved Id :  "+customerResponse.getId());
            }
            else
            {
                System.out.println("---------------dealer not saved --"+o);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return status;

    }
    public static boolean saveBank(Bank bank) {

        Object o = new Object();
        boolean status = false;

        Gson gson = new Gson();

        try {

            if(bank.getId()==0)
            {
                System.out.println("---------------bank save--");
            }
            else
            {
                System.out.println("---------------bank updated--");
            }


            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Bank_Save",bank).get();
            prettyJson("bank save response",o);

            try {


                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                bank = mapper.convertValue(o,Bank.class);
                System.out.println("bank Id"+bank.getId());
                if(bank.getId()!=0)
                {
                    status = true;
                }
            }
            catch (Exception e)
            {
                System.out.println("Exception=="+e);

            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return status;

    }

    public static boolean saveProductSpec(ProductSpecProcess productSpecProcess) {

        Object o = new Object();


        boolean status = false;

        ProductSpecProcess specProcess = new ProductSpecProcess();

        try {

            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Product_Spec_Process_Save",productSpecProcess).get();

            System.out.println("---------------class type--"+o.getClass());
            System.out.println("---------------class value --"+o);

            prettyJson("prod spec before save",productSpecProcess);
            if((boolean)o)
            {
                System.out.println("---------------productSpecProcess saved --"+o);

                status = true;
                BizLogger.generateNoteOnSD( BizUtils.getCurrentDatAndTimeInDF()+" | prod spec  saved Id :  "+productSpecProcess.getId());
            }
            else
            {
                System.out.println("---------------productSpecProcess not saved --"+o);
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


        return status;

    }
    public static void   accountGroupList()
    {

        try {
            ArrayList<LinkedTreeMap> collections = new ArrayList<LinkedTreeMap>();
            try {
                collections = Store.getInstance().mHubProxyCaller.invoke(collections.getClass(), "AccountGroup_List").get();

                // BizUtils.prettyJson("Accounts Group",collections);

                Store.getInstance().accountsGroupList.clear();
                for (int i = 0; i < collections.size(); i++) {
                    AccountGroup accountGroup = new AccountGroup();
                    final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                    accountGroup = mapper.convertValue(collections.get(i), AccountGroup.class);
                    System.out.println("accountGroup Id-" + accountGroup.getId());
                    System.out.println("accountGroup Name-" + accountGroup.getGroupName());
                    System.out.println("accountGroup company Id-" + accountGroup.getCompany().getId());

                    if (accountGroup.getGroupName() != null) {

                        if (accountGroup.getGroupName().equals("Sundry Debtors")) {
                            Store.getInstance().currentAccGrpId = accountGroup.getId();
                            Store.getInstance().currentAccGrpName = accountGroup.getGroupName();
                            System.out.println("Current - accountGroup Name-" + accountGroup.getGroupName());

                        }
                        if (accountGroup.getGroupName().equals("Bank Accounts") && accountGroup.getCompany().getId().compareTo(Store.getInstance().dealer.getId()) == 0) {
                            Store.getInstance().bankAccountGroupId = accountGroup.getId();
                            System.out.println("Bank Accounts - Group Id" + accountGroup.getGroupName());
                        }
                    }
                    Store.getInstance().accountsGroupList.add(accountGroup);

                }


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            System.out.println("accountGroup----" + Store.getInstance().accountsGroupList.size());
        }
        catch (Exception e)
        {
            System.err.println("Accounts group error");
        }
    }
    public static void   taxMasterList()
    {

        ArrayList<LinkedTreeMap> collections = new ArrayList<LinkedTreeMap>();
        try {
            collections = Store.getInstance().mHubProxyCaller.invoke(collections.getClass(),"TaxMaster_List").get();

            Store.getInstance().taxMasterList.clear();
            for(int i=0;i<collections.size();i++)
            {
                 TaxMaster taxMaster = new TaxMaster();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                taxMaster  = mapper.convertValue(collections.get(i),TaxMaster.class);
                System.out.println("taxmaster Id-"+taxMaster.getId());
               // System.out.println("accountGroup Name-"+taxMaster.getGroupName());

                Store.getInstance().taxMasterList.add(taxMaster);

            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        System.out.println("taxmaster----"+Store.getInstance().taxMasterList.size());
    }
    public static void   UOMList()
    {

        ArrayList<LinkedTreeMap> collections = new ArrayList<LinkedTreeMap>();
        try {
            collections = Store.getInstance().mHubProxyCaller.invoke(collections.getClass(),"UOM_List").get();

            Store.getInstance().UOM.clear();

            prettyJson("UOM LIst",collections);
            for(int i=0;i<collections.size();i++)
            {
                UOM uom = new UOM();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                uom  = mapper.convertValue(collections.get(i),UOM.class);
                System.out.println("uom Id-"+uom.getId());
                Store.getInstance().UOM.add(uom);
                // System.out.println("accountGroup Name-"+taxMaster.getGroupName());

            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("uom----"+Store.getInstance().UOM.size());
    }

    public static boolean salesSave(Sale sale)
    {
        Object o = new Object();

        boolean status = false;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(sale);
            System.out.println("JSON : " + json);


            //System.out.println(" ---------Sales  details size "+sale.getSDetail().size());
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Sales_Save",sale).get();

            System.out.println(" ---------Sale save"+o.getClass());

            status = (boolean) o;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;

    }

    public static double cashLedgerId()
    {
        Object o = new Object();
        double cusId = 0;
        try {
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Cash_Ledger_Id").get();
            System.out.println(" ---------Cash_Ledger_Id "+o.getClass()+"===="+o);
            cusId = (double) o;
            Store.getInstance().cashLedgerId = cusId;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return cusId;

    }
    public static double bankLedgerId()
    {
        Object o = new Object();
        double bankId = 0;
        try {
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Bank_Ledger_Id").get();
            System.out.println(" ---------bankLedgerId "+o.getClass());
            bankId = (double) o;

            Store.getInstance().bankLedgerId = bankId;
            System.out.println(" ---------bankLedgerId "+o);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return bankId;

    }

    public static void  bankList()
    {

        ArrayList<LinkedTreeMap> collections = new ArrayList<LinkedTreeMap>();
        try {
                collections = Store.getInstance().mHubProxyCaller.invoke(collections.getClass(),"Bank_List").get();

            System.out.println("Collection = "+collections);

            Store.getInstance().bankList.clear();
            for(int i=0;i<collections.size();i++)
            {
                Bank bankList = new Bank();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                bankList  = mapper.convertValue(collections.get(i),Bank.class);
                System.out.println("Bank_Name_List-"+bankList);

                // System.out.println("accountGroup Name-"+taxMaster.getGroupName());
                Store.getInstance().bankList.add(bankList);

            }

            prettyJson("Bank List",Store.getInstance().bankList);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    public static String companyLogo()
    {

        Object o = new Object();
        try {
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Company_Logo").get();

            System.out.println("Dealer Logo (RAW) = "+o.getClass() + " = "+o);

            Store.getInstance().dealerLogo = String.valueOf(o);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Store.getInstance().dealerLogo;

    }
    public static String companyLogo(Long underCompanyId)
    {

        Object o = new Object();
        try {
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Company_Logo",underCompanyId).get();

            System.out.println("Company Logo (RAW) = "+o.getClass() + " = "+o);

            Store.getInstance().companyLogo = String.valueOf(o);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return Store.getInstance().companyLogo;

    }
    public static void  SOPendingList()
    {

        ArrayList<LinkedTreeMap> collections = new ArrayList<LinkedTreeMap>();
        try {
            collections = Store.getInstance().mHubProxyCaller.invoke(collections.getClass(),"SalesOrder_SOPendingList").get();

            Store.getInstance().SOPendingList.clear();

            System.out.println("SO P Collection = "+collections);

            ArrayList<SOPending> soPendings = new ArrayList<SOPending>();
            for(int i=0;i<collections.size();i++)
            {
                SOPending soPending = new SOPending();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                soPending = mapper.convertValue(collections.get(i),SOPending.class);
                System.out.println("sOPending_List-"+soPending.getStatus() + "   "+soPending.getRefNo());

                for(int j=0;j<soPending.getSODetails().size();j++)
                {
                    System.out.println("sOPending_List-Prod ID"+soPending.getSODetails().get(j).getProductId());
                    System.out.println("sOPending_List-Prod ID"+soPending.getSODetails().get(j).getQuantity());
                }




                // System.out.println("accountGroup Name-"+taxMaster.getGroupName());

                if(soPending.getStatus().equals("Pending")) {

                    soPendings.add(soPending);
                }
            }
            Store.getInstance().SOPendingList = soPendings;

           // BizUtils.prettyJson("SO PENDING LIST",Store.getInstance().SOPendingList);




        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }
    public static boolean salesReturn(SalesReturn sale)
    {
        Object o = new Object();

        boolean status = false;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(sale);
            System.out.println("JSON : " + json);

            prettyJson("SalesReturn",sale);
            Dummy  temp = new Dummy();

            Dummydetails dummydetails = new Dummydetails();
            dummydetails.ProductId = 123;
            dummydetails.Quantity= 54;
            dummydetails.UOMId =7987;
            dummydetails.DiscountAmount = Double.valueOf(456);
            dummydetails.UnitPrice = Double.valueOf(566);
            dummydetails.GSTAmount = Double.valueOf(82);
            dummydetails.Amount = 0.0;

            temp.SRDetails.add(dummydetails);

            prettyJson("dummy details",temp);

            //System.out.println(" ---------Sales  details size "+sale.getSDetail().size());
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"SalesReturn_Save",sale).get();

            System.out.println(" ---------Sales return save"+o.getClass());

            status = (boolean) o;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;

    }

    public static boolean receiptSave(Receipt receipt)
    {
        Object o = new Object();

        boolean status = false;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(receipt);
            System.out.println("JSON : " + json);


            //System.out.println(" ---------Sales  details size "+sale.getSDetail().size());
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Receipt_Save",receipt).get();

            System.out.println(" ---------Receipt_Save save"+o.getClass());

            status = (boolean) o;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;

    }
    public static boolean productSave(Product product)
    {
        Object o = new Object();

        boolean status = false;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(product);
            System.out.println("JSON : " + json);


            //System.out.println(" ---------Sales  details size "+sale.getSDetail().size());
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Product_Save",product).get();

            System.out.println(" ---------Product_Save save"+o.getClass());

            Product productResult = new Product();
            final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
            productResult  = mapper.convertValue(o,Product.class);
            System.out.println("Product Id"+productResult.getId());

            if(productResult.getId()!=0)
            {
                status = true;
            }


        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;

    }
    public static boolean salesOrder(SalesOrder sale)
    {
        Object o = new Object();

        boolean status = false;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(sale);
            System.out.println("JSON : " + json);


            //System.out.println(" ---------Sales  details size "+sale.getSDetail().size());
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"SalesOrder_Save",sale).get();

            System.out.println(" ---------Sales order save"+o.getClass());

            status = (boolean) o;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;

    }

    public static void  getTransactionType()
    {

        ArrayList<LinkedTreeMap> collections = new ArrayList<LinkedTreeMap>();



        try {

            collections = Store.getInstance().mHubProxyCaller.invoke(collections.getClass(),"TransactionType_List").get();

            System.out.println("-------collection--"+collections);
            Store.getInstance().transactionTypeList.clear();
            for(int i=0;i<collections.size();i++)
            {
                System.out.println("-------collection--obj----"+i+"-------"+collections.get(i));
                TransactionType transactionType = new TransactionType();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                transactionType  = mapper.convertValue(collections.get(i),TransactionType.class);
                System.out.println("-------collection--obj----id------"+collections.get(i).get("Type").getClass());


                transactionType.setId((double)collections.get(i).get("Id"));
                transactionType.setType(String.valueOf(collections.get(i).get("Type")));
                System.out.println("transactionType Id"+transactionType.getId());
                System.out.println("transactionType name"+transactionType.getType());
                Store.getInstance().transactionTypeList.add(transactionType);




            }
            TransactionType.getAll();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("Trans type list size "+Store.getInstance().transactionTypeList.size());

    }


    public static boolean saleOrderMakeSales(SalesOrder salesOrder) {

        Object o = new Object();

        boolean status = false;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(salesOrder);
            System.out.println("JSON : " + json);


            //System.out.println(" ---------Sales  details size "+sale.getSDetail().size());
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"SalesOrder_MakeSales",salesOrder).get();

            System.out.println(" ---------SalesReturn_MakeSales  save"+o.getClass());

            status = (boolean) o;
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;
    }


    public static void Sales_getNewRefNo() {
        Object o = new Object();
        boolean status = false;
        try {
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Sales_NewRefNo").get();
            System.out.println(" ---------Sales_NewRefNo "+o.getClass()+"-----------"+o);
            Store.getInstance().user.setSalRefCode(String.valueOf(o));

            Store.getInstance().saleRefCode  = Store.getInstance().user.getSalRefCode();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void SalesOrder_getNewRefNo() {
        Object o = new Object();
        boolean status = false;
        try {

            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"SalesOrder_NewRefNo").get();
            System.out.println(" ---------SalesOrder_getNewRefNo "+o.getClass()+"-----------"+o);
            Store.getInstance().user.setSORefCode(String.valueOf(o));

            Store.getInstance().saleOrderRefCode  = Store.getInstance().user.getSORefCode();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void SalesReturn_getNewRefNo() {
        Object o = new Object();
        boolean status = false;
        try {
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"SalesReturn_NewRefNo").get();
            System.out.println(" ---------SalesReturn_getNewRefNo "+o.getClass()+"-----------"+o);
             Store.getInstance().user.setSRRefCode(String.valueOf(o));

             Store.getInstance().saleReturnRefCode  = Store.getInstance().user.getSRRefCode();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void Receipt_getNewRefNo() {
        Object o = new Object();
        boolean status = false;
        try {
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"Receipt_NewRefNo").get();
            System.out.println(" ---------Receipt_NewRefNo "+o.getClass()+"-----------"+o);
                Store.getInstance().user.setRTRefCode(String.valueOf(o));

                Store.getInstance().saleReceiptRefCode = Store.getInstance().user.getRTRefCode();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
    public static void saleList(Integer LedgerId, Integer TType, Date dtFrom, Date dtTo, String BillNo,float amtFrom, float amtTo)
    {

        ArrayList<Sale>  sale = new ArrayList<Sale>();
        try {

            sale = Store.getInstance().mHubProxyCaller.invoke(sale.getClass(),"Sale_List",LedgerId,TType,dtFrom,dtTo,BillNo,amtFrom,amtTo).get();

            System.out.println("sale List ---"+sale);

            Store.getInstance().saleList.clear();

            for(int i=0;i<sale.size();i++)
            {
                Sale sale1 = new Sale();
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper
                sale1 = mapper.convertValue(sale.get(i),Sale.class);
                System.out.println("Sale Id"+sale1.getSDetails().size());
                Store.getInstance().saleList.add(sale1);
            }


        } catch (InterruptedException e) {


        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("sale list----"+sale.size());
    }

    public static void reconnect(Context context)
    {
        SERVER_HUB_CHAT = "ABServerHub";

        if(Store.getInstance().urlType.contains("full")) {
            Store.getInstance().baseUrl = "http://" + Store.getInstance().domain + "/";
        }
        else
        {
            Store.getInstance().baseUrl = "http://" + Store.getInstance().domain + "/";

        }
        String host = Store.getInstance().baseUrl;



        Platform.loadPlatformComponent( new AndroidPlatformComponent() );

        Store.getInstance().mHubConnectionCaller = new HubConnection(host);

        Store.getInstance().mHubProxyCaller = Store.getInstance().mHubConnectionCaller.createHubProxy(SERVER_HUB_CHAT);

        Store.getInstance().mHubProxyCaller.on("Display",
                new SubscriptionHandler1<String>() {
                    @Override
                    public void run(final String msg) {
                        final String finalMsg = msg;
                        // display Toast message
                        Store.getInstance().mHandlerCaller.post(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(getApplicationContext(), finalMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                , String.class);
        ClientTransport clientTransport = new ServerSentEventsTransport(Store.getInstance().mHubConnectionCaller.getLogger());
        SignalRFuture<Void> signalRFuture = Store.getInstance().mHubConnectionCaller.start(clientTransport);

        try {
            Log.d("Connection:","trying...");

            try {
                signalRFuture.get(60, TimeUnit.SECONDS);
                recieveAllMsg("Caller");
                Log.d("Connection:"," ok");
                reConnectReceiver(context);
            } catch (TimeoutException e) {
                e.printStackTrace();
                Log.d("Connection:","not ok");
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();

        }

    }
    public static void reConnectReceiver(final Context context)
    {

        Store.getInstance().mHubConnectionReceiver = new HubConnection(Store.getInstance().getHost());

        Store.getInstance().mHubProxyReceiver = Store.getInstance().mHubConnectionReceiver.createHubProxy(SERVER_HUB_CHAT);
        Store.getInstance().mHubProxyReceiver.on("Display",
                new SubscriptionHandler1<String>() {
                    @Override
                    public void run(final String msg) {
                        final String finalMsg = msg;
                        // display Toast message
                        Store.getInstance().mHandlerReceiver.post(new Runnable() {
                            @Override
                            public void run() {
                                // Toast.makeText(context, finalMsg, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
                , String.class);

        Class<String> params = null;
        Store.getInstance().mHubProxyReceiver.on("AppApproved_Changed",
                new SubscriptionHandler1<String>() {
                    @Override
                    public void run(final String response) {

                        Log.d("App change ->",response);
                        // display Toast message
                        Store.getInstance().mHandlerReceiver.post(new Runnable() {
                            @Override
                            public void run() {


                                Log.d("App change", String.valueOf(response));
                                Intent intent = null;
                                if(!Boolean.valueOf(response))
                                {
                                    ((Activity)context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                    intent = new Intent(context,BlockPage.class);
                                   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);

                                }
                                else
                                {

                                    Activity activity = BlockPage.getActivity();

                                    if(activity==null)
                                    {
                                        connect(context);

                                    }
                                    else {

                                        try {
                                            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            activity.finish();
                                        } catch (Exception e) {

                                            Log.e("Error", e.toString());
                                        }
                                    }

                                    Store.getInstance().mHandlerReceiver.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            // Toast.makeText(getApplicationContext(), finalMsg, Toast.LENGTH_SHORT).show();

                                            Toast.makeText(context, "Event --- "+response, Toast.LENGTH_SHORT).show();

                                        }
                                    });

                                }

                            }
                        });
                    }
                }
                , String.class);
        ClientTransport clientTransport = new ServerSentEventsTransport(Store.getInstance().mHubConnectionReceiver.getLogger());
        SignalRFuture<Void> signalRFuture = Store.getInstance().mHubConnectionReceiver.start(clientTransport);

        try {
            Log.d("Connection:","trying...");

            try {
                signalRFuture.get(60*10,TimeUnit.SECONDS);
                Log.d("Connection:","ok");
            } catch (TimeoutException e) {
                e.printStackTrace();
                Log.d("Connection:","not ok");
            }


        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            Log.d("Connection:","server unreachable");

        }

        //Toast.makeText(context, "Receiver Connected....", Toast.LENGTH_SHORT).show();
        recieveAllMsgFromReceiver("Receiver");



        RConnectionId = Store.getInstance().mHubConnectionReceiver.getConnectionId();

        String result = null;
        try {

            try {
                Log.d("RecConnIdToCaller","Trying...");

                result = Store.getInstance().mHubProxyCaller.invoke(String.class, "SetReceiverConnectionIdToCaller",RConnectionId).get(60*3, TimeUnit.SECONDS);


            } catch (TimeoutException e) {
                e.printStackTrace();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        try {
            Log.d("REG RID TO SERVER", result);
        }catch (Exception e)
        {
            Log.d("REG RID TO SERVER", "result is null");
        }

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = sharedpreferences.edit();

        if(Boolean.valueOf(result))
        {
            String s = sharedpreferences.getString(AppId,"");


            Log.d("Shared Pref - Value ",s);


            String AppIDValue = null;

            if(TextUtils.isEmpty(s))
            {
                Log.d("Shared Pref","New");
                String conStr =  getConnectionId();
                AppIDValue = conStr;
                editor.putString(AppId, conStr);
                editor.apply();
            }
            else
            {
                Log.d("Shared Pref","Old");
                String conStr = sharedpreferences.getString(AppId,"");
                AppIDValue = conStr;

            }

            Store.getInstance().appId = AppIDValue;


            Store.getInstance().appId = AppIDValue;
            generateUID(AppIDValue,context);

        }
        System.out.println("Conn ID ----"+ Store.getInstance().mHubConnectionReceiver.getConnectionId());
        if(Store.getInstance().mHubConnectionReceiver !=null)
        {

            Store.getInstance().mHubConnectionReceiver.stateChanged(new StateChangedCallback() {
                @Override
                public void stateChanged(ConnectionState connectionState, ConnectionState connectionState1) {

                    System.out.println("State listener ---State :"+connectionState.name());
                    System.out.println("State listener ---State :"+connectionState1.name());


                }
            });

        }


    }
    public static void recieveAllMsg(final String from)
    {

        Store.getInstance().mHubConnectionCaller.received(new MessageReceivedHandler() {

            @Override
            public void onMessageReceived(final JsonElement json) {
                Log.e("onMessageReceived -"+from, json.toString());

                Store.getInstance().mHandlerCaller.post(new Runnable() {
                    @Override
                    public void run() {
                        // Toast.makeText(context, json.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
    private static void recieveAllMsgFromReceiver(final String from) {
        Store.getInstance().mHubConnectionReceiver.received(new MessageReceivedHandler() {


            @Override
            public void onMessageReceived(final JsonElement json) {
                Log.e("onMessageReceived |- > "+from, json.toString());


                Store.getInstance().mHandlerReceiver.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                            final Gson gson = gsonBuilder.create();
                            final Type collectionType = new TypeToken<ReceiverResponse>() {
                            }.getType();
                            ReceiverResponse response = gson.fromJson(json, collectionType);


                            System.out.println("AppApproved Change --- " + Boolean.valueOf(String.valueOf(response.isA())));

                            if(Boolean.valueOf(String.valueOf(response.isA())))
                            {
                                //Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                //Toast.makeText(context, "Not Ok", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception  --- " + e);
                        }



                    }
                });

            }
        });
    }
    public static String getConnectionId()
    {
        String s = null;



        try {
            s = Store.getInstance().mHubProxyCaller.invoke(String.class,"GetNewAppID").get();

            System.out.println("new App Id______"+s);



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return s ;

    }
    public static void generateUID(String appIdValue, Context context) {
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String email = null;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                Log.d("EMAIL : ",possibleEmail);

                if(email==null) {
                    email = possibleEmail;
                }

            }
        }



        String deviceid = getLocalBluetoothName() + " " + email + " "+android_id;


        Store.getInstance().deviceName = getLocalBluetoothName();




        new Activate(appIdValue,getLocalBluetoothName(),email,android_id,context).execute();

    }

    public static boolean salesOrderDelete(SOPending salesOrder) {
        Object o = new Object();

        boolean status = false;
        try {
            Gson gson = new Gson();
            String json = gson.toJson(salesOrder);
            System.out.println("JSON : " + json);


            //System.out.println(" ---------Sales  details size "+sale.getSDetail().size());
            o = Store.getInstance().mHubProxyCaller.invoke(o.getClass(),"salesOrder_Delete",salesOrder.getId()).get();


            System.out.println(" ---------Sales order  delete"+o.getClass());


            status = (boolean) o;

            System.out.println(" ---------Sales order  delete status" +status);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return status;
    }

    public static void productSpecMasterList() {

        ArrayList<LinkedTreeMap> companyCollection = new ArrayList<LinkedTreeMap>();
        try {
            companyCollection = Store.getInstance().mHubProxyCaller.invoke(companyCollection.getClass(),"Product_Spec_Master_List").get();

            Store.getInstance().productSpecMasterList.clear();

            for(int i=0;i<companyCollection.size();i++)
            {
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

                ProductSpec productSpec = new ProductSpec();

                productSpec  = mapper.convertValue(companyCollection.get(i),ProductSpec.class);
                System.out.println("stockGroup Id"+productSpec.getId());

                prettyJson("productSpec master item",productSpec);


                Store.getInstance().productSpecMasterList.add(productSpec);


            }



        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println("productSpecMasterList----"+Store.getInstance().productSpecList.size());
    }

    public static void stockReportList(Integer PID, Integer LedgerId, String fd, String td) {

        ArrayList<LinkedTreeMap> reportList = new ArrayList<LinkedTreeMap>();
        try {
            reportList = Store.getInstance().mHubProxyCaller.invoke(reportList.getClass(),"StockBalance_Individual",PID,LedgerId,fd,td).get();

            Store.getInstance().stockReportList.clear();
            for(int i=0;i<reportList.size();i++)
            {
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

                StockReport stockReport = new StockReport();
                stockReport  = mapper.convertValue(reportList.get(i),StockReport.class);
                prettyJson("stockReport  item",stockReport);
                Store.getInstance().stockReportList.add(stockReport);

            }
            Log.d("Stock report Size", String.valueOf(Store.getInstance().stockReportList.size()));

        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void creditLimitTypeList() {
        ArrayList<LinkedTreeMap> list = new ArrayList<LinkedTreeMap>();
        try {
            list = Store.getInstance().mHubProxyCaller.invoke(list.getClass(),"CreditLimitType_List").get();

            Store.getInstance().customerCreditLimitTypeList.clear();
            for(int i=0;i<list.size();i++)
            {
                final ObjectMapper mapper = new ObjectMapper(); // jackson's objectmapper

                CreditLimitType creditLimitType = new CreditLimitType();
                creditLimitType  = mapper.convertValue(list.get(i),CreditLimitType.class);
                prettyJson("creditLimitType  ",creditLimitType);
                Store.getInstance().customerCreditLimitTypeList.add(creditLimitType);

            }
            Log.d("cctypeList size", String.valueOf(Store.getInstance().customerCreditLimitTypeList.size()));

        }catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    static class Activate extends AsyncTask
    {
        String appIdValue;
        String localBluetoothName;
        String email;
        String android_id;
        String result;
        private String result1;
        Context context;

        public Activate(String appIdValue, String localBluetoothName, String email, String android_id, Context context) {
            this.appIdValue = appIdValue;
            this.localBluetoothName = localBluetoothName;
            this.email = email;
            this.android_id = android_id;
            this.context = context;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HashMap<String,String> params = new HashMap<String, String>();
                result = Store.getInstance().mHubProxyCaller.invoke(String.class,"LoginHubCaller",appIdValue,localBluetoothName,email,android_id).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            System.out.println("login stat______"+result);
            Store.getInstance().mHubProxyCaller =  Store.getInstance().mHubProxyCaller;
            Store.getInstance().mHubProxyReceiver =  Store.getInstance().mHubProxyReceiver;


            Store.getInstance().mHubConnectionReceiver =  Store.getInstance().mHubConnectionReceiver;
            Store.getInstance().mHubConnectionCaller =  Store.getInstance().mHubConnectionCaller;

            Store.getInstance().mHandlerCaller = Store.getInstance().mHandlerCaller;
            Store.getInstance().mHandlerReceiver = Store.getInstance().mHandlerReceiver;


            System.out.println("mHubProxyCaller = "+ Store.getInstance().mHubProxyCaller);
            if(Boolean.valueOf(result)) {

                Toast.makeText(context, "Connecting...", Toast.LENGTH_SHORT).show();
                connect(context);
            }
            else
            {

                Toast.makeText(context, "Waiting for approval...", Toast.LENGTH_SHORT).show();

            }



        }
    }
    public static void connect(final Context context)
    {

        boolean status = true;
        String domain = Store.getInstance().domain;



        String baseUrl = "";
        if(Store.getInstance().urlType.contains("full")) {
            baseUrl = "http://" + domain + "/";
        }
        else
        {
            baseUrl = "http://" + domain + "/";
        }


        System.out.println("Base URL = "+baseUrl);

        boolean s = Patterns.WEB_URL.matcher(baseUrl).matches();

        if(!s)
        {
            status = false;
            Toast.makeText(context, "Not a valid url", Toast.LENGTH_SHORT).show();
        }
        else
        {
            System.out.println("Domain b= "+ Store.getInstance().domain);
            Store.getInstance().domain = domain;

            System.out.println("Domain a= "+ Store.getInstance().domain);

            if(Store.getInstance().urlType.contains("full")) {
                Store.getInstance().baseUrl = "http://" + domain + "/";
            }
            else
            {
                Store.getInstance().baseUrl = "http://" + domain + "/";
            }
            System.out.println("Base URL= "+ Store.getInstance().baseUrl);


        }
        if(status) {
            // new GetCompanyDetails(MainActivity.this).execute();

           // SignalRService.getCompanyDetails(context);

            System.out.println("trying to log in ..");
            boolean loginStat = SignalRService.login(context, null, null, null);

            if (loginStat)
            {
                if (Store.getInstance().user.getId() == null) {
                    Toast.makeText(context, "Invalid Username or Password..", Toast.LENGTH_SHORT).show();
                } else {

                    try {
                        Log.d("Login Stat","Logged in..");

                        Store.getInstance().user.setSORefCode(Store.getInstance().saleOrderRefCode);
                        Store.getInstance().user.setSalRefCode(Store.getInstance().saleRefCode);
                        Store.getInstance().user.setSRRefCode(Store.getInstance().saleReturnRefCode);
                        Store.getInstance().user.setRTRefCode(Store.getInstance().saleReceiptRefCode);


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                        alertDialog.setPositiveButton(R.string.reloadMsg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                System.out.println("Download Data ....");
                                new DownloaddData(context).execute();

                            }
                        });
                        alertDialog.setNegativeButton(R.string.noReloadMsg, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                System.out.println("Do nothing....");
                            }
                        });

                        alertDialog.create();
                        alertDialog.show();


                        // Store.getInstance().user.SalRefCode = "0000A";
                        //  Store.getInstance().user.SRRefCode = "0000B";
                        // Store.getInstance().user.SORefCode = "0000C";

                    } catch (Exception e) {
                        System.out.println("Exception e :" + e);
                    }



                }
            }
            // SignalRService.getCompanyDetails(MainActivity.this);
        }

    }
    private static class DownloaddData extends AsyncTask<Integer,Integer,String>
    {
        ProgressDialog progressDialog;
        Context context;

        public DownloaddData(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {


            progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Sync status");
            progressDialog.setMessage("Downloading data..");
            // progressDialog.show();

        }


        @Override
        protected String doInBackground(Integer... integers) {

            SignalRService.customerList(context);
            SignalRService.cashLedgerId();

            try {
                SignalRService.bankLedgerId();
                SignalRService.bankList();
                SignalRService.SOPendingList();
               // SignalRService.Sales_getNewRefNo();
                //SignalRService.SalesOrder_getNewRefNo();
                //SignalRService.SalesReturn_getNewRefNo();
               // SignalRService.Receipt_getNewRefNo();
            }
            catch (Exception e)
            {
                System.out.println("catch---"+e);
            }



            SignalRService.productList(context);
            SignalRService.stockHomeList();


            SignalRService.accountGroupList();


              SignalRService.taxMasterList();
            //   progressBar.setProgress(100);

            return null;
        }
        @Override
        protected void onPostExecute(String result) {

            // progressDialog.dismiss();

            SignalRService.getCompanyDetails(context);

            ((Activity)context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Downloading data complete", Toast.LENGTH_SHORT).show();
                }
            });
            System.out.println("Downloading details complete");


        }

        @Override
        protected void onProgressUpdate(Integer... values) {

            System.out.println("----------Status-----------------------"+values[0]);

        }
    }
    static class BankNew
    {
        int Id;
        String AccountNo;
        String BankAccountName;
        Ledger Ledger;

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getAccountNo() {
            return AccountNo;
        }

        public void setAccountNo(String accountNo) {
            AccountNo = accountNo;
        }

        public String getBankAccountName() {
            return BankAccountName;
        }

        public void setBankAccountName(String bankAccountName) {
            BankAccountName = bankAccountName;
        }

        public com.bizsoft.fmcgv2.dataobject.Ledger getLedger() {
            return Ledger;
        }

        public void setLedger(com.bizsoft.fmcgv2.dataobject.Ledger ledger) {
            Ledger = ledger;
        }
    }

    public static class Dummydetails
    {
        long Id;
        int ProductId;
        int UOMId;
        float Quantity;
        Double UnitPrice;
        Double DiscountAmount;
        Double GSTAmount;
        Double Amount;

    }
    public static class Dummy
    {
        int Id;
        Collection<Dummydetails> SRDetails = new ArrayList<Dummydetails>();


    }

}
