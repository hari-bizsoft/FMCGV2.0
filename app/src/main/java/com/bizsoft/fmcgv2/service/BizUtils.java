/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.DashboardActivity;
import com.bizsoft.fmcgv2.MainActivity;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.Tables.Bank;
import com.bizsoft.fmcgv2.Tables.ReceiptDetail;
import com.bizsoft.fmcgv2.Tables.SaleDetail;
import com.bizsoft.fmcgv2.Tables.SalesOrderDetails;
import com.bizsoft.fmcgv2.Tables.SalesReturnDetails;
import com.bizsoft.fmcgv2.adapter.MenuAdapter;
import com.bizsoft.fmcgv2.dataobject.Company;

import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Ledger;
import com.bizsoft.fmcgv2.dataobject.MenuItems;
import com.bizsoft.fmcgv2.dataobject.Notification;
import com.bizsoft.fmcgv2.dataobject.Payment;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.ProductModel;
import com.bizsoft.fmcgv2.dataobject.ProductSaveResponse;
import com.bizsoft.fmcgv2.dataobject.ProductSpecProcess;
import com.bizsoft.fmcgv2.dataobject.Receipt;
import com.bizsoft.fmcgv2.dataobject.Sale;
import com.bizsoft.fmcgv2.dataobject.SaleOrder;
import com.bizsoft.fmcgv2.dataobject.SaleReturn;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.signalr.pojo.PDetailsItem;
import com.bizsoft.fmcgv2.signalr.pojo.ProductSpec;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;
import static com.bizsoft.fmcgv2.DashboardActivity.appDiscount;
import static com.bizsoft.fmcgv2.DashboardActivity.customerNameListAdapter;
import static com.bizsoft.fmcgv2.DashboardActivity.discountType;
import static com.bizsoft.fmcgv2.DashboardActivity.fromCustomer;
import static com.bizsoft.fmcgv2.DashboardActivity.paymentModeValue;
import static com.bizsoft.fmcgv2.DashboardActivity.subTotal;
import static com.bizsoft.fmcgv2.dataobject.Store.currentAccGrpId;
import static com.bizsoft.fmcgv2.dataobject.Store.currentAccGrpName;

/**
 * Created by shri on 9/8/17.
 */

public class BizUtils {
    public boolean newcustomer;
    private boolean syncStatus;
    private String fpath;
    private String billIdValue;
    private String billDateValue;
    private MenuAdapter menuAdapter;
    private ArrayList<MenuItems> menuItems = new ArrayList<MenuItems>();
    private Dialog dialog;


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        boolean status = false;
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            // notify user you are online
            Log.d("NET STAT", "ONLINE");
            status = true;
        } else if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.DISCONNECTED) {

            // notify user you are not online
            Log.d("NET STAT", "OFFLINE");
            status = false;
        }
        return status;
    }

    public HashMap<String, String> setTodayDate() {
        HashMap<String, String> map = new HashMap<>();
        String fromDate, toDate;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        fromDate = dateFormat.format(date) + " 00:00:00";
        toDate = dateFormat.format(date) + " 23:59:59";


        Store.getInstance().fromDate = fromDate;
        Store.getInstance().toDate = toDate;

        System.out.println("From = " + fromDate + " To :" + toDate);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);

        return map;


    }

    public String getCurrentTime() {

        String fromDate, toDate;
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
        Date date = new Date();
        fromDate = dateFormat.format(date);

        return fromDate;
    }

    public static String getCurrentYearAndMonth() {
        String fromDate, toDate;
        DateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
        Date date = new Date();
        fromDate = dateFormat.format(date);

        return fromDate;
    }

    public static String getCurrentDate() {
        String fromDate, toDate;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        fromDate = dateFormat.format(date);

        return fromDate;
    }

    public static Date getCurrentDatAndTimeInDF() {
        String fromDate, toDate;
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy-HH-mm-ss");
        Date date = new Date();
        fromDate = dateFormat.format(date);

        System.out.println("Date format ----" + date);
        return date;
    }

    public static String getCurrentYear() {

        String fromDate, toDate;
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        fromDate = dateFormat.format(date);

        return fromDate;
    }
    public static String getCurrentMonth() {

        String fromDate, toDate;
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        fromDate = dateFormat.format(date);

        return fromDate;
    }

    public HashMap<String, String> setCurrentMonth() {
        HashMap<String, String> map = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();   // this takes current date
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date fromDate = c.getTime();


        Calendar calendar = getCalendarForNow();
        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        Date toDate = calendar.getTime();

        System.out.println("Date ==========" + dateFormat.format(calendar.getTime()));

        calendar.set(Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        System.out.println("Date ==========" + dateFormat.format(calendar.getTime()));


        String fd = dateFormat.format(fromDate) + " 00:00:00";
        String ld = dateFormat.format(toDate) + " 23:59:59";


        Store.getInstance().fromDate = fd;
        Store.getInstance().toDate = ld;


        System.out.println("From = " + fd + " To :" + ld);


        map.put("fromDate", fd);
        map.put("toDate", ld);


        return map;

    }

    public void setCurrentYear() {

        String fromDate, toDate;
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        fromDate = "01/01/" + dateFormat.format(date) + " 00:00:00";
        toDate = "31/12/" + dateFormat.format(date) + " 23:59:59";


        Store.getInstance().fromDate = fromDate;
        Store.getInstance().toDate = toDate;

        System.out.println("From = " + fromDate + " To :" + toDate);
    }

    private static Calendar getCalendarForNow() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.setTime(new Date());
        return calendar;
    }

    public HashMap<String, String> setCurrentWeek() {
        HashMap<String, String> map = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();   // this takes current date

        Date fromDate = c.getTime();
        System.out.println("Date ==========" + dateFormat.format(c.getTime()));


        String fd = dateFormat.format(fromDate) + " 00:00:00";


        c.add(Calendar.DATE, -7);
        Date toDate = c.getTime();

        String ld = dateFormat.format(toDate) + " 23:59:59";


        Store.getInstance().fromDate = fd;
        Store.getInstance().toDate = ld;


        System.out.println("From = " + fd + " To :" + ld);


        map.put("fromDate", ld);
        map.put("toDate", fd);

        return map;
    }

/*    public void showMenu(final Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.menu_layout);



        ImageButton customerIcon = (ImageButton) dialog.findViewById(R.id.customer_icon);

        Button close = (Button) dialog.findViewById(R.id.close_button);
        final ImageView sync = (ImageView) dialog.findViewById(R.id.sync);
        ImageButton sales = (ImageButton) dialog.findViewById(R.id.sales_order_pending_list);
        ImageButton salesOrder = (ImageButton) dialog.findViewById(R.id.sale_order);
        ImageButton salesReturn = (ImageButton) dialog.findViewById(R.id.sale_return);
        ImageButton home = (ImageButton) dialog.findViewById(R.id.home);
        ImageButton reprint = (ImageButton) dialog.findViewById(R.id.reprint);
        ImageButton invoiceList = (ImageButton) dialog.findViewById(R.id.invoice_list);
        ImageButton clearData = (ImageButton) dialog.findViewById(R.id.clear_data);
        ImageButton receipt = (ImageButton) dialog.findViewById(R.id.receipt);
        ImageButton payment = (ImageButton) dialog.findViewById(R.id.payment);
        ImageButton loadOffline = (ImageButton) dialog.findViewById(R.id.load_offline);
        ImageButton dealer = (ImageButton) dialog.findViewById(R.id.dealer);
        ImageButton productSpec = (ImageButton) dialog.findViewById(R.id.product_spec);
        Button activity = (Button) dialog.findViewById(R.id.activity);
        ImageButton bank = (ImageButton) dialog.findViewById(R.id.bank);
        ImageButton product = (ImageButton) dialog.findViewById(R.id.product);
        ImageButton stockReport = (ImageButton) dialog.findViewById(R.id.stock_report);
        TextView productLabel = (TextView) dialog.findViewById(R.id.textView93);
        //  productLabel.setVisibility(View.INVISIBLE);

        // product.setVisibility(View.INVISIBLE);

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AppActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });

        ImageButton reports = (ImageButton) dialog.findViewById(R.id.reports);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });
        reports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });
        reprint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, ReprintActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });
        customerIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CustomerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

                dialog.dismiss();
            }
        });
        dialog.show();

        sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Store.getInstance().waiter != null) {
                    Store.getInstance().waiter.touch();
                }

                sync(context, "manual");


                dialog.dismiss();


            }
        });

        stockReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, StockReportActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                dialog.dismiss();
                context.startActivity(intent);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, SalesActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

                context.startActivity(intent);
            }
        });
        salesOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, SalesOrderActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        salesReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, SalesReturnActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        invoiceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, InvoiceListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

            }
        });
        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = context.getSharedPreferences(Store.getInstance().MyPREFERENCES, MODE_PRIVATE);
                prefs.edit().clear().commit();

                ((Activity) context).finish();
                Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

            }
        });
        receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(context, ReceiptActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

            }
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(context, STOSOActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);

            }
        });

        loadOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BizUtils.readAsJSON("customerList", context);
                dialog.dismiss();

            }
        });
        dealer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(context, DealerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        productSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(context, ProductSpecActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        bank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(context, BankActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });
        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                context.startActivity(intent);
            }
        });


    }*/


    public  void bizMenu(final Context context)
    {
        try {
            dialog = new Dialog(context);
            dialog.setContentView(R.layout.biz_menu);


            dialog.getWindow().setLayout(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);

            GridView menuGrid = (GridView) dialog.findViewById(R.id.menu_grid);
            AutoCompleteTextView searchKey = (AutoCompleteTextView) dialog.findViewById(R.id.search_term);


            loadSearchList(context, searchKey, Store.getInstance().menuNameList);

            ViewGroup.LayoutParams layoutParams = menuGrid.getLayoutParams();
            layoutParams.height = Store.getInstance().width;
            layoutParams.width = Store.getInstance().width;
            //  menuGrid.setLayoutParams(layoutParams);


            menuItems.clear();
            menuItems.addAll(Store.getInstance().menuItems);
            menuAdapter = new MenuAdapter(context, menuItems);
            menuGrid.setAdapter(menuAdapter);
            menuGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                    gotoAction(context, position, dialog);
                }
            });

            addTextListener(searchKey);

            dialog.show();
        }
        catch (Exception e)
        {

        }
    }

    private void gotoAction(final Context context, int position, Dialog dialog) {
        MenuItems menuItem = (MenuItems) menuAdapter.getItem(position);
        dialog.dismiss();
        if(menuItem.getName().toLowerCase().equals("sync"))
        {
            if (Store.getInstance().waiter != null) {
                Store.getInstance().waiter.touch();
            }
            sync(context, "manual");
        }
        else if(menuItem.getName().equals("Load Storage"))
        {
            BizUtils.readAsJSON("customerList", context);

        }
        else  if (menuItem.getName().equals("Logout"))
        {
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
            alertDialog.setMessage("Are you sure you want to logout ?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    logout(context);


                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("Do nothing....");
                }
            });

            alertDialog.create();
            alertDialog.show();

        }

        else {

            Intent intent = new Intent(context, (Class<?>) menuItem.classToCall);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(intent);
        }
    }

    public int getScreenOrientation(Context context)
    {
        Display getOrient = ((Activity)context).getWindowManager().getDefaultDisplay();
        int orientation = Configuration.ORIENTATION_UNDEFINED;
        if(getOrient.getWidth()==getOrient.getHeight()){
            orientation = Configuration.ORIENTATION_SQUARE;
        } else{
            if(getOrient.getWidth() < getOrient.getHeight()){
                orientation = Configuration.ORIENTATION_PORTRAIT;
            }else {
                orientation = Configuration.ORIENTATION_LANDSCAPE;
            }
        }
        return orientation;
    }
    private void addTextListener(final AutoCompleteTextView searchKey) {

        searchKey.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(TextUtils.isEmpty(searchKey.getText()))
                {
                    menuItems.clear();
                    menuItems.addAll(Store.getInstance().menuItems);
                    menuAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void loadSearchList(final Context context, AutoCompleteTextView searchBar, ArrayList<String> searchNameList) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, searchNameList);
        searchBar.setThreshold(1);
        searchBar.setAdapter(adapter);
        searchBar.setThreshold(1);
        searchBar.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                Toast.makeText(context, ""+selected, Toast.LENGTH_SHORT).show();

                returnResult(selected,context);

            }


        });

    }

    private String returnResult(String selected, Context context) {


        for(int i=0;i<Store.getInstance().menuItems.size();i++)
        {
            if(Store.getInstance().menuItems.get(i).getName().equals(selected))
            {
               // menuItems.clear();
               //menuItems.add(Store.getInstance().menuItems.get(i));
               //menuAdapter.notifyDataSetChanged();
               gotoAction(context,i,dialog);


            }
        }

        return selected;
    }


    private void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(Store.getInstance().MyPREFERENCES, MODE_PRIVATE);
        prefs.edit().clear().commit();

        ((Activity) context).finish();
        Intent intent = new Intent(context.getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static void syncStockProcessProductList() {


        for (int k = 0; k < Store.getInstance().productList.size(); k++) {
            Product actualProduct = Store.getInstance().productList.get(k);
            ArrayList<ProductSpec> choosedOutputProd = Store.getInstance().productSpecList;
            for (int i = 0; i < choosedOutputProd.size(); i++) {
                ProductSpec productprocess = choosedOutputProd.get(i);

                if (productprocess.getProductId() == actualProduct.getId()) {
                    productprocess.setAvailable(actualProduct.getAvailableStock());
                    synchronized (productprocess) {
                        productprocess.notify();
                    }
                }
                for (int j = 0; j < productprocess.getPDetails().size(); j++) {
                    PDetailsItem product = productprocess.getPDetails().get(j);
                    if (product.getProductId() == actualProduct.getId()) {
                        product.setAvailable(actualProduct.getAvailableStock());
                        synchronized (product) {
                            product.notify();
                        }

                    }

                }
            }
        }

    }

    public void updateOPBal(Customer customer, Sale sale) {

        if (sale.getPaymentMode().contains("PNT")) {





            if(customer.getLedger().getACType()!=null) {
                if (customer.getLedger().getACType().toLowerCase().contains("debit")) {

                    double x = 0;
                    System.out.println("GT ===" + sale.getGrandTotal());
                    x = customer.getLedger().getOPBal() + sale.getGrandTotal();

                    System.out.println("OP =" + x);
                    customer.getLedger().setOPBal(x);
                }
                else
                {
                    double x = 0;
                    System.out.println("GT ===" + sale.getGrandTotal());
                    x = customer.getLedger().getOPBal() -  sale.getGrandTotal();

                    System.out.println("OP =" + x);
                    customer.getLedger().setOPBal(x);
                }
            }
            else
            {
                double x = 0;
                System.out.println("GT ===" + sale.getGrandTotal());
                x = customer.getLedger().getOPBal() + sale.getGrandTotal();
                System.out.println("OP =" + x);
                customer.getLedger().setOPBal(x);
                customer.getLedger().setACType("Debit");
            }
        }
    }

    public static int getDiscountType(String discountType) {
        int i=0;
        if(discountType.toLowerCase().contains("no"))
        {
            i=1;
        }
        if(discountType.toLowerCase().contains("grand") || discountType.toLowerCase().contains("individual"))
        {
            i=2;
        }


        return i;
    }

    public static boolean createAlertDialog(Context context,String s) {
        final boolean[] status = {false};



        return status[0];
    }


    public class SaveCustomer extends AsyncTask {
        Context context;
        Customer customers = new Customer();
        Customer customerResponse;
        int position;
        boolean status;
        private String from;
        private Customer customer;

        public SaveCustomer(Context context, Customer customers, int position, String from) {
            this.context = context;

            this.customers = customers;
            this.status = false;
            this.customerResponse = new Customer();
            this.position = position;
            this.from = from;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        @Override
        protected Object doInBackground(Object[] params) {


            customerResponse = SignalRService.saveCustomer(customers);
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            try {

                System.out.println("JSON :" + customerResponse.getId());
                if (customerResponse != null) {
                    if (customerResponse.getId() == 0) {
                        // Toast.makeText(context, "Customer not Saved", Toast.LENGTH_SHORT).show();
                        Log.e("Customer save", "not saved");

                    } else {
                        Toast.makeText(context, "Customer Saved", Toast.LENGTH_SHORT).show();
                        this.customers.setId(customerResponse.getId());
                        int size = Store.getInstance().customerList.size();
                        System.out.println("Received ID " + customerResponse.getId());
                        Store.getInstance().customerList.get(position).setId(customerResponse.getId());

                        new getCustomer(customer,customerResponse,position,context,from).execute();

                        System.out.println("Customer has been obtain"+customer.getLedger());



                    }
                    syncStatus = true;
                } else {

                    syncStatus = false;
                    Toast.makeText(context, "Error in saving customer", Toast.LENGTH_SHORT).show();
                }

            }
            catch (Exception e)
            {
                System.out.println("Exception "+e);
            }
        }


    }
    class getCustomer extends AsyncTask
    {

        private Customer customer;
        private Customer customerResponse;
        int position;
        Context context;
        String from;


        public getCustomer(Customer customer, Customer customerResponse, int position, Context context, String from) {
            this.customer = customer;
            this.customerResponse = customerResponse;
            this.position = position;
            this.context = context;
            this.from = from;

        }

        @Override
        protected Object doInBackground(Object[] objects) {
            customer = SignalRService.getCustomer(customerResponse.getId());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("Got Customer with ledg id"+customer.getLedger().getId());




            System.out.println("Size " + Store.getInstance().customerList.size());
            System.out.println("Position " + position);
            for (int i = 0; i < Store.getInstance().customerList.size(); i++) {
                System.out.println("cus id : " + Store.getInstance().customerList.get(i).getId());
                System.out.println("Position " + i);
                if (Store.getInstance().customerList.get(i).getId().compareTo(customer.getId()) == 0) {
                    System.out.println("setting led if for cus id : " + Store.getInstance().customerList.get(i).getId());
                    Store.getInstance().customerList.get(i).getLedger().setId(customer.getLedger().getId());
                    Store.getInstance().customerList.get(i).setSynced(true);
                    //Saving to local storage as JSON
                    try {
                        BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));
                        System.out.println("DB Updated..on local storage");

                        customerNameListAdapter = new ArrayAdapter<String>(context,
                                android.R.layout.simple_dropdown_item_1line, Customer.getCustomerNameList());


                        DashboardActivity.customerNameKey.setAdapter(customerNameListAdapter);
                        customerNameListAdapter.notifyDataSetChanged();

                    } catch (ClassNotFoundException e) {

                        System.err.println("Unable to write to DB");
                    }
                    break;
                }
            }

            BizUtils.prettyJson("customer", customer);


            saveDetails(context, from);
        }
    }


    private void saveDetails(Context context, String from) {

        boolean sync = false;

        ArrayList<Customer> customerList = Store.getInstance().customerList;
        for (int i = 0; i < customerList.size(); i++) {
            //System.out.println("OBJ =" + customerList.get(i).getSale().size());

            if (customerList.get(i).getId() != null) {



                //printSyncData(customerList,i);


                if (customerList.get(i).getSale().size() > 0) {
                    newcustomer = false;

                    for (int y = 0; y < customerList.get(i).getSalesOfCustomer().size(); y++) {

                        if (!customerList.get(i).getSalesOfCustomer().get(y).isSynced()) {
                            new Save(context, "sale/save", "SODetails", customerList.get(i).getSalesOfCustomer().get(y).getProducts(), customerList.get(i).getId(), customerList.get(i).getSalesOfCustomer().get(y), null, null, customerList.get(i)).execute();

                            sync = true;

                        }
                    }

                }
                if (customerList.get(i).getSaleOrder().size() > 0) {
                    newcustomer = false;


                    for (int y = 0; y < customerList.get(i).getSaleOrdersOfCustomer().size(); y++) {
                        if (!customerList.get(i).getSaleOrdersOfCustomer().get(y).isSynced()) {
                            new Save(context, "SaleOrder/save", "SaleOrderDetails", customerList.get(i).getSaleOrdersOfCustomer().get(y).getProducts(), customerList.get(i).getId(), null, customerList.get(i).getSaleOrdersOfCustomer().get(y), null, customerList.get(i)).execute();
                            sync = true;
                        }
                    }


                }
                if (customerList.get(i).getSaleReturn().size() > 0) {

                    for (int y = 0; y < customerList.get(i).getSaleReturnOfCustomer().size(); y++) {
                        if (!customerList.get(i).getSaleReturnOfCustomer().get(y).isSynced()) {
                            newcustomer = false;
                            new Save(context, "SalesReturn/save", "SaleReturnDetails", customerList.get(i).getSaleReturnOfCustomer().get(y).getProducts(), customerList.get(i).getId(), null, null, customerList.get(i).getSaleReturnOfCustomer().get(y), customerList.get(i)).execute();
                            sync = true;
                        }
                    }

                }
                if (customerList.get(i).getReceipts().size() > 0) {
                    newcustomer = false;
                    System.out.println("Called save receipt");
                    for (int z = 0; z < customerList.get(i).getReceipts().size(); z++) {

                        if (customerList.get(i).getReceipts().get(z).isSynced()) {
                            System.out.println("Receipt already synced");
                        } else {
                            System.out.println("Customer Ledger Id " + customerList.get(i).getLedger().getId());
                            new SaveReceipt(context, "Sale/Receipt_Save", customerList.get(i).getReceipts().get(z), customerList.get(i).getId(), customerList.get(i)).execute();

                            sync = true;
                        }


                    }
                    System.out.println("Closing save receipt");

                }

               // System.out.println("SO P list size " + customerList.get(i).getsOPendingList().size());
              //  System.out.println("SO P list size " + customerList.get(i).getsOPendingList().size());
                if (customerList.get(i).getsOPendingList().size() > 0) {
                    for (int x = 0; x < customerList.get(i).getsOPendingList().size(); x++) {
                        if (customerList.get(i).getsOPendingList().get(x).isSynced()) {
                            System.out.println("SO pendling  already synced");
                        } else {
                            boolean status = SignalRService.saleOrderMakeSales(customerList.get(i).getsOPendingList().get(x));
                            if (status) {
                                System.out.println("SO pending saved");
                                customerList.get(i).getsOPendingList().get(x).setSynced(true);
                                sync = true;

                            } else {
                                System.out.println("SO Pending Not  saved ");
                            }
                        }
                    }

                }
                if (customerList.get(i).getPayments().size() > 0) {
                    newcustomer = false;
                    System.out.println("Called save payments");
                    for (int z = 0; z < customerList.get(i).getPayments().size(); z++) {


                        if (customerList.get(i).getPayments().get(z).isSynced()) {
                            System.out.println("Payment already synced");
                        } else {
                            new SaveReceipt(context, "Sale/Payment_Save", customerList.get(i).getPayments().get(z), customerList.get(i).getId()).execute();
                            sync = true;
                        }

                    }
                    System.out.println("Closing save payments");

                }


                //new Save(context,"SaleOrder/save","SaleOrderDetails",Store.getInstance().addedProductForSalesOrder).execute();

                // new Save(context,"sale/save","SODetails",Store.getInstance().addedProductForSales).execute();

            }


        }


        System.out.println("Sync required = " + sync);


        if (!sync) {
            if (from.toLowerCase().contains("auto")) {
                // System.out.print("No Data to sync...");
                Store.getInstance().messageList.add("Auto Sync invoked");
                Notification notification = new Notification();
                notification.setTime(getCurrentTime());
                notification.setMessage("Auto Sync invoked:No data to Sync");
                //  Store.getInstance().notificationList.add(notification);

            } else {
                Notification notification = new Notification();
                notification.setMessage("Manual Sync invoked:No data to Sync");
                notification.setTime(getCurrentTime());
                Store.getInstance().notificationList.add(notification);


                try {
                    Snackbar snackbar = null;

                    View notifier = DashboardActivity.customActionBar.findViewById(R.id.notify);
                    snackbar = Snackbar
                            .make(notifier, "Manual sync: No Data ", Snackbar.LENGTH_LONG);

                    // snackbar.show();
                    Store.getInstance().messageList.add("Manual Sync invoked");

                    showToast("No Data to sync...", context);
                } catch (Exception e) {

                }
            }


        } else {
            try {
                BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));

                Toast.makeText(context, "Saved..", Toast.LENGTH_SHORT).show();
                System.err.println("DB Updated..on local storage");
            } catch (ClassNotFoundException e) {

                System.err.println("Unable to write to DB");
            }
        }
    }

    private void printSyncData(ArrayList<Customer> customerList,int i) {
        System.out.println("Sale size " + customerList.get(i).getSale().size());
        System.out.println("Sale order size " + customerList.get(i).getSaleOrder().size());
        System.out.println("Sale of cus size " + customerList.get(i).getSalesOfCustomer().size());
        System.out.println("Sale order of cus size " + customerList.get(i).getSaleOrdersOfCustomer().size());
        System.out.println("Sale of return size " + customerList.get(i).getSaleReturn().size());
        System.out.println("Sale retutn of cus size " + customerList.get(i).getSaleReturnOfCustomer().size());
        System.out.println("Receipt size " + customerList.get(i).getReceipts().size());
        System.out.println("Payments size " + customerList.get(i).getPayments().size());
        System.out.println("Pending list size " + customerList.get(i).getPayments().size());
        System.out.println("Cus id " + customerList.get(i).getId());
    }

    class Save extends AsyncTask {

        Context context;
        String jsonStr = null;
        String url;
        HashMap<String, String> params;
        String paramKey;
        ArrayList<Product> input = new ArrayList<Product>();
        Long cusId;
        ArrayList<ProductModel> productModels;
        Sale sale;
        SaleOrder saleOrder;
        SaleReturn saleReturn;
        boolean IsGst;
        String LedgerId;
        String payMode;
        Customer customer;
        double grandTotal;
        com.bizsoft.fmcgv2.Tables.Sale salex;
        com.bizsoft.fmcgv2.Tables.SalesOrder salesOrderx;
        com.bizsoft.fmcgv2.Tables.SalesReturn salesReturnx;
        private boolean status;


        public Save(Context context, String url, String paramkey, ArrayList<Product> input, Long id, Sale sale, SaleOrder saleOrder, SaleReturn saleReturn, Customer customers) {
            this.context = context;
            this.url = url;
            params = new HashMap<String, String>();
            this.paramKey = paramkey;
            this.input = input;
            this.cusId = id;
            this.sale = sale;
            this.saleOrder = saleOrder;
            this.saleReturn = saleReturn;
            this.customer = customers;
            this.grandTotal = 0;
            this.salex = new com.bizsoft.fmcgv2.Tables.Sale();
            this.salesOrderx = new com.bizsoft.fmcgv2.Tables.SalesOrder();
            this.salesReturnx = new com.bizsoft.fmcgv2.Tables.SalesReturn();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            productModels = new ArrayList<ProductModel>();

            Long gst = Long.valueOf(0);
            double totalAmount = Long.valueOf(0);
            Long extraAmount = Long.valueOf(0);
            ArrayList<SaleDetail> saleDetails = new ArrayList<SaleDetail>();
            ArrayList<SalesOrderDetails> saleOrderDetails = new ArrayList<SalesOrderDetails>();
            ArrayList<SalesReturnDetails> salesReturnDetails = new ArrayList<SalesReturnDetails>();
            for (int i = 0; i < input.size(); i++) {

                if (input.get(i).isStatus()) {
                    System.out.println("No sync required for " + url);
                } else {
                    System.out.println("adding sync required for " + url);
                    ProductModel productModel = new ProductModel();
                    double amount = (input.get(i).getMRP() * input.get(i).getQty());


                    SaleDetail saleDetail = new SaleDetail();
                    saleDetail.setProductId(input.get(i).getId().intValue());
                    saleDetail.setQuantity(input.get(i).getQty());
                    saleDetail.setAmount(amount);
                    saleDetail.setDiscountAmount(input.get(i).getDiscountAmount());
                    saleDetail.setGSTAmount(Double.valueOf(0));
                    saleDetail.setUnitPrice(input.get(i).getMRP());
                    saleDetail.setUOMId(input.get(i).getUOMId().intValue());
                    saleDetails.add(saleDetail);


                    SalesOrderDetails salesODetails = new SalesOrderDetails();
                    salesODetails.setProductId(input.get(i).getId().intValue());
                    salesODetails.setQuantity(input.get(i).getQty());
                    salesODetails.setAmount(amount);
                    salesODetails.setDiscountAmount(input.get(i).getDiscountAmount());
                    salesODetails.setGSTAmount(Double.valueOf(0));
                    salesODetails.setUnitPrice(input.get(i).getMRP());
                    salesODetails.setUOMId(input.get(i).getUOMId().intValue());
                    saleOrderDetails.add(salesODetails);


                    System.out.println("Product  id = " + input.get(i).getId());
                    productModel.setProductId(input.get(i).getId());
                    productModel.setQty(input.get(i).getQty());
                    productModel.setRate((long) input.get(i).getMRP());
                    productModel.setAmount(amount);
                    productModel.setUOMId(input.get(i).getUOMId());
                    System.out.println("REASON=" + input.get(i).getParticulars());
                    System.out.println("Resale=" + input.get(i).isResale());
                    productModel.setReason(input.get(i).getParticulars());
                    productModel.setResale(input.get(i).isResale());
                    productModel.setDiscountAmount(input.get(i).getDiscountAmount());
                    productModels.add(productModel);

                    SalesReturnDetails saleReturnDetail = new SalesReturnDetails();
                    saleReturnDetail.setProductId(input.get(i).getId().intValue());
                    saleReturnDetail.setQuantity(input.get(i).getQty());
                    saleReturnDetail.setAmount(amount);
                    saleReturnDetail.setDiscountAmount(input.get(i).getDiscountAmount());
                    saleReturnDetail.setGSTAmount(Double.valueOf(0));
                    saleReturnDetail.setUnitPrice(input.get(i).getMRP());
                    saleReturnDetail.setUOMId(input.get(i).getUOMId().intValue());
                    saleReturnDetail.setResale(input.get(i).isResale());
                    saleReturnDetail.setParticulars(input.get(i).getParticulars());
                    salesReturnDetails.add(saleReturnDetail);


                }


            }


            Gson gson = new Gson();


            String json = gson.toJson(productModels);
            // System.out.println("JSON : " + json);


            if (url.toLowerCase().contains("order")) {
                this.params.put("ChqNo", saleOrder.getChequeNo());
                this.params.put("ChqDate", saleOrder.getChequeDate());
                this.params.put("ChqBankName", saleOrder.getChequeBankName());
                if (saleOrder.getGstType().toLowerCase().contains("no")) {
                    IsGst = false;
                } else {
                    IsGst = true;
                }
                payMode = saleOrder.getPaymentMode();
                grandTotal = saleOrder.getGrandTotal();


                this.params.put("DiscountAmount", String.valueOf(saleOrder.getDiscountValue()));
                this.params.put("RefCode", saleOrder.getRefCode());

                //--------------------------Signal R params


                salesOrderx.setId((long) 0);
                salesOrderx.setSODate(BizUtils.getCurrentDatAndTimeInDF());
                salesOrderx.setRefNo(saleOrder.getRefCode());

                salesOrderx.setLedgerId(customer.getLedger().getId().intValue());
                salesOrderx.setItemAmount(saleOrder.getSubTotal());
                salesOrderx.setDiscountAmount(saleOrder.getDiscountValue());
                salesOrderx.setGSTAmount((double) saleOrder.getGst());
                salesOrderx.setExtraAmount(Double.valueOf(0));
                salesOrderx.setTotalAmount(saleOrder.getGrandTotal());


                salesOrderx.SODetails.clear();
                salesOrderx.SODetails.addAll(saleOrderDetails);


            } else if (url.toLowerCase().contains("return")) {

                this.params.put("ChqNo", saleReturn.getChequeNo());
                this.params.put("ChqDate", saleReturn.getChequeDate());
                this.params.put("ChqBankName", saleReturn.getChequeBankName());
                if (saleReturn.getGstType().toLowerCase().contains("no")) {
                    IsGst = false;
                } else {
                    IsGst = true;
                }
                payMode = saleReturn.getPaymentMode();
                grandTotal = saleReturn.getGrandTotal();
                this.params.put("RefCode", saleReturn.getRefCode());
                this.params.put("DiscountAmount", String.valueOf(saleReturn.getDiscountValue()));


                //Signal R params


                //------------cheque details---------------
                salesReturnx.setId((long) 0);
                salesReturnx.setSRDate(BizUtils.getCurrentDatAndTimeInDF());
                salesReturnx.setRefNo(saleReturn.getRefCode());
                salesReturnx.setLedgerId(customer.getLedger().getId().intValue());
                for (int i = 0; i < Store.getInstance().transactionTypeList.size(); i++) {

                    if (Store.getInstance().transactionTypeList.get(i).getType().toLowerCase().contains(saleReturn.getPaymentMode().toLowerCase())) {

                        salesReturnx.setTransactionTypeId((int) Store.getInstance().transactionTypeList.get(i).getId());
                    }
                    if (saleReturn.getPaymentMode().contains("PNT")) {
                        System.out.println("Payment mode value ----" + saleReturn.getPaymentMode());
                        if (Store.getInstance().transactionTypeList.get(i).getType().toLowerCase().contains("credit")) {
                            System.out.println("Payment mode value ----" + saleReturn.getPaymentMode() + "----------" + Store.getInstance().transactionTypeList.get(i).getType() + "----------" + Store.getInstance().transactionTypeList.get(i).getId());
                            salesReturnx.setTransactionTypeId((int) Store.getInstance().transactionTypeList.get(i).getId());
                        }

                    }


                }


                salesReturnx.setChequeNo(saleReturn.getChequeNo());
                String date = saleReturn.getChequeDate() + " 01-01-01";
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH-mm-ss");
                try {
                    Date date1 = format.parse(date);
                    salesReturnx.setChequeDate(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                salesReturnx.setBankName(saleReturn.getChequeBankName());
                salesReturnx.setItemAmount(saleReturn.getSubTotal());
                //-----------------------------
                salesReturnx.setDiscountAmount(saleReturn.getDiscountValue());


                if (saleReturn.getGstType().toLowerCase().contains("no")) {
                    salesReturnx.setGSTAmount(Double.valueOf(0));

                } else {
                    salesReturnx.setGSTAmount(saleReturn.getGst());

                }

                salesReturnx.setExtraAmount(Double.valueOf(0));


                salesReturnx.setTotalAmount(saleReturn.getGrandTotal());


                System.out.println("Payment mode value ----" + saleReturn.getPaymentMode());


                salesReturnx.SRDetails.clear();


                salesReturnx.SRDetails.addAll(salesReturnDetails);


            } else {


                //------------cheque details---------------
                salex.setBankName(sale.getChequeBankName());
                salex.setChequeNo(sale.getChequeNo());

                String date = sale.getChequeDate() + " 01-01-01";
                DateFormat format = new SimpleDateFormat("MM/dd/yyyy HH-mm-ss");
                try {
                    if (sale.getChequeDate() != null) {
                        Date date1 = format.parse(date);
                        salex.setChequeDate(date1);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //-----------------------------
                salex.setLedgerId(customer.getLedger().getId().intValue());
                salex.setDiscountAmount(sale.getDiscountValue());
                salex.setExtraAmount(Double.valueOf(0));

                if (sale.getGstType().toLowerCase().contains("no")) {
                    salex.setGSTAmount(Double.valueOf(0));
                    salex.setGST(false);
                } else {
                    salex.setGSTAmount(sale.getGst());
                    salex.setGST(true);
                }


                salex.setTotalAmount(sale.getGrandTotal());
                salex.setRefNo(sale.getRefCode());
                salex.setItemAmount(sale.getSubTotal());
                salex.setSalesDate(BizUtils.getCurrentDatAndTimeInDF());


                System.out.println("Payment mode value ----" + sale.getPaymentMode());

                for (int i = 0; i < Store.getInstance().transactionTypeList.size(); i++) {

                    if (Store.getInstance().transactionTypeList.get(i).getType().toLowerCase().contains(sale.getPaymentMode().toLowerCase())) {

                        salex.setTransactionTypeId((int) Store.getInstance().transactionTypeList.get(i).getId());
                    }
                    if (sale.getPaymentMode().contains("PNT")) {
                        System.out.println("Payment mode value ----" + sale.getPaymentMode());
                        if (Store.getInstance().transactionTypeList.get(i).getType().toLowerCase().contains("credit")) {
                            System.out.println("Payment mode value ----" + sale.getPaymentMode() + "----------" + Store.getInstance().transactionTypeList.get(i).getType() + "----------" + Store.getInstance().transactionTypeList.get(i).getId());
                            salex.setTransactionTypeId((int) Store.getInstance().transactionTypeList.get(i).getId());
                        }

                    }


                }

                salex.SDetails.clear();
                salex.SDetails.addAll(saleDetails);
                this.params.put("ChqNo", sale.getChequeNo());
                this.params.put("ChqDate", sale.getChequeDate());
                this.params.put("ChqBankName", sale.getChequeBankName());
                if (sale.getGstType().toLowerCase().contains("no")) {
                    IsGst = false;
                } else {
                    IsGst = true;
                }
                payMode = sale.getPaymentMode();
                grandTotal = sale.getGrandTotal();
                this.params.put("RefCode", sale.getRefCode());
                this.params.put("DiscountAmount", String.valueOf(sale.getDiscountValue()));
            }
            this.params.put("IsGST", String.valueOf(IsGst));
            this.params.put("LedgerId", String.valueOf(this.cusId));


//                this.params.put("value", String.valueOf("1"));


            this.params.put("PayMode", String.valueOf(payMode));


            this.params.put(this.paramKey, json);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            if (productModels.size() != 0) {
                HttpHandler httpHandler = new HttpHandler();
                //jsonStr = httpHandler.makeServiceCall(this.url, this.params);
                System.out.println("URL===================" + url);
                if (url.toLowerCase().contains("order")) {
                    status = SignalRService.salesOrder(salesOrderx);
                    //BizUtils.prettyJson("Sale Order === ",salesOrderx);

                    if (status) {
                        saleOrder.setSynced(true);
                        BizLogger.generateNoteOnSD(BizUtils.getCurrentDatAndTimeInDF() + " | Sale saved ref code :  " + salesOrderx.getRefCode());

                    }

                } else if (url.toLowerCase().contains("return")) {
                    status = SignalRService.salesReturn(salesReturnx);
                    saleReturn.setSynced(true);
                } else {
                    status = SignalRService.salesSave(salex);
                    sale.setSynced(true);
                }


            } else {
                System.out.println("No sync required.....");
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            System.out.println("JSON RESPONSE" + jsonStr);

            if (status || jsonStr != null) {

                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();

                Type collectionType = new TypeToken<ProductSaveResponse>() {
                }.getType();

                ProductSaveResponse response = gson.fromJson(jsonStr, collectionType);

                Store.getInstance().productSaveRes = response;


                if (!status) {
                    syncStatus = false;
                    Toast.makeText(context, "Not saved", Toast.LENGTH_SHORT).show();

                } else {


                    for (int i = 0; i < input.size(); i++) {

                        input.get(i).setStatus(true);


                    }


                    System.out.println("URL saved to " + this.url);
                    System.out.println("payMode =" + payMode);
                    if (url.toLowerCase().contains("order")) {
                        Toast.makeText(context, "Sale order saved", Toast.LENGTH_SHORT).show();

                        if (payMode.contains("PNT")) {
                            double x = 0;
                            x = customer.getLedger().getOPBal() + grandTotal;
                            customer.setOPBal(x);

                            System.out.println("OP =" + x);
                        }
                        syncStatus = true;

                    } else if (url.toLowerCase().contains("return")) {
                        Toast.makeText(context, "Sale return saved", Toast.LENGTH_SHORT).show();


                        syncStatus = true;
                    } else {
                        Toast.makeText(context, "sale  saved", Toast.LENGTH_SHORT).show();
                        System.out.println("payMode =" + payMode);
                        if (payMode.contains("PNT")) {


                            double x = 0;

                            System.out.println("GT ===" + grandTotal);
                            x = customer.getLedger().getOPBal() + grandTotal;

                            System.out.println("OP =" + x);
                            customer.setOPBal(x);

                        }
                        syncStatus = true;
                    }
                }
                try {
                    BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));
                    System.err.println("DB Updated..on local storage");
                } catch (ClassNotFoundException e) {

                    System.err.println("Unable to write to DB");
                }

            }
        }


    }

    private class SaveReceipt extends AsyncTask {

        Context context;
        String jsonStr = null;
        String url;
        HashMap<String, String> params;
        String paramKey;
        Receipt receipt_ = new Receipt();
        Payment payment = new Payment();
        Receipt refReceipt;
        Long cusId;
        com.bizsoft.fmcgv2.Tables.Receipt receipt;
        private boolean status;

        Customer customer;

        public SaveReceipt(Context context, String url, Receipt receipt, Long cusId, Customer customer) {
            this.context = context;
            this.url = url;
            this.receipt_ = receipt;
            refReceipt = receipt;
            this.cusId = cusId;
            params = new HashMap<String, String>();
            this.customer = customer;
        }

        public SaveReceipt(Context context, String url, Payment receipt, Long cusId) {
            this.context = context;
            this.url = url;
            this.payment = receipt;
            this.cusId = cusId;
            params = new HashMap<String, String>();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            if (this.url.toLowerCase().contains("payment")) {
                params.put("Amount", String.valueOf(payment.getAmount()));
                params.put("LedgerId", String.valueOf(payment.getLegerId()));
                params.put("PayMode", String.valueOf(payment.getPaymentMode()));

                this.params.put("ChqNo", payment.getChequeNo());
                this.params.put("ChqDate", payment.getChequeDate());
                this.params.put("ChqBankName", payment.getChequeBankName());

            } else {


                params.put("Amount", String.valueOf(receipt_.getAmount()));
                System.out.println("Receipt ledger id : " + customer.getLedger().getId());
                params.put("LedgerId", String.valueOf(customer.getLedger().getId()));
                params.put("PayMode", String.valueOf(receipt_.getPaymentMode()));

                this.params.put("ChqNo", receipt_.getChequeNo());
                this.params.put("ChqDate", receipt_.getChequeDate());
                this.params.put("ChqBankName", receipt_.getChequeBankName());


                receipt = new com.bizsoft.fmcgv2.Tables.Receipt();
                receipt.setId((long) 0);
                System.out.println("ID================" + receipt.getId());
                int invce = 1;
                for (int i = 0; i < Store.getInstance().customerList.size(); i++) {

                    invce = invce + Store.getInstance().customerList.get(i).getReceipts().size();

                }


                receipt.setEntryNo(String.valueOf(BizUtils.calculateShortCode("receipt") + DashboardActivity.calculateRefCode(Store.getInstance().user.getRTRefCode(), invce)));
                System.out.println("ENTRY NO================" + receipt.getEntryNo());
                receipt.setReceiptDate(BizUtils.getCurrentDatAndTimeInDF());
                //Ledger id need to be set
                System.out.println("RDATE================" + receipt.getReceiptDate());
                System.out.println("Pay Mode================" + receipt_.getPaymentMode());
                if (this.receipt_.getPaymentMode().toLowerCase().contains("cash")) {
                    receipt.setLedgerId((long) Store.getInstance().cashLedgerId);
                } else {
                    receipt.setLedgerId((long) Store.getInstance().bankLedgerId);
                }


                System.out.println("LEDGER ID================" + receipt.getLedgerId());
                receipt.setReceiptMode(this.receipt_.getPaymentMode());
                receipt.setAmount(receipt_.getAmount());
                receipt.setChequeNo(this.receipt_.getChequeNo());


                DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Date chqdate = new Date();
                try {
                    chqdate = dateFormat.parse(this.receipt_.getChequeDate());
                    System.out.println("Date format ----" + chqdate);
                    receipt.setChequeDate(chqdate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ReceiptDetail receiptDetail = new ReceiptDetail();
                receiptDetail.setId(Long.valueOf(0));
                receiptDetail.setReceiptId(Long.valueOf(0));
                System.out.println("Receipt ledger Cash/Cheque ----" + receipt_.getLegerId());
                receiptDetail.setLedgerId(customer.getLedger().getId());
                receiptDetail.setAmount(this.receipt_.getAmount());


                receipt.getRDetails().add(receiptDetail);


            }

        }

        @Override
        protected Object doInBackground(Object[] params) {
            HttpHandler httpHandler = new HttpHandler();
            System.out.println("URL ================== " + this.url);

            status = SignalRService.receiptSave(receipt);
            // jsonStr = httpHandler.makeServiceCall(this.url, this.params);
            return true;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("JSON RESPONSE" + jsonStr);

            System.out.println("Status : " + status);


            String method = "";


            if (this.url.toLowerCase().contains("payment")) {
                method = "Payment";
            } else {
                method = "Receipt";
            }
            if (status) {
                Toast.makeText(context, method + " Saved", Toast.LENGTH_SHORT).show();
                if (this.url.toLowerCase().contains("payment")) {

                    payment.setSynced(true);

                } else {

                    receipt_.setSynced(true);
                    refReceipt.setSynced(true);

                    receipt.getAmount();

                }
                try {
                    BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));
                    System.err.println("DB Updated..on local storage");
                } catch (ClassNotFoundException e) {

                    System.err.println("Unable to write to DB");
                }
            } else {
                Toast.makeText(context, method + " not Saved", Toast.LENGTH_SHORT).show();

            }

        }


    }
    public static int getTransactionType(String name)
    {
        int i=0;

        if(name.toLowerCase().contains("return"))
        {
            i=Store.getInstance().SALE_RETURN;
        }
        if(name.toLowerCase().contains("order"))
        {
            i=Store.getInstance().SALE_ORDER;
        }
        if(!(name.toLowerCase().contains("return")|| name.toLowerCase().contains("order") ))
        {

            i = Store.getInstance().SALE;
        }
        return i;
    }

    public void updateStock(SaleReturn saleReturn) {


        ArrayList<Product> allProducts = Store.getInstance().productList;
        for (int i = 0; i < allProducts.size(); i++) {
            System.out.println("qty ->" + allProducts.get(i).getAvailableStock());
        }
        ArrayList<Product> currentProductsInCart = saleReturn.getProducts();
        for (int i = 0; i < allProducts.size(); i++) {
            for (int j = 0; j < currentProductsInCart.size(); j++) {

                if (allProducts.get(i).getId() == currentProductsInCart.get(j).getId()) {

                    if (allProducts.get(i).isResale()) {


                        Long aq = allProducts.get(i).getAvailableStock();

                        Long cq = currentProductsInCart.get(j).getQty();

                        System.out.println(aq + "< ==== >" + cq);

                        aq = aq + cq;


                        allProducts.get(i).setAvailableStock(aq);

                        System.out.println("AQ ====" + aq);
                        System.out.println("CQ ====" + cq);


                        //  allProducts.get(i).setRQty(aq);

                    }

                }

            }
        }
    }

    public void updateStock(Sale sale) {

        ArrayList<Product> allProducts = Store.getInstance().productList;
        for (int i = 0; i < allProducts.size(); i++) {
            System.out.println("qty ->" + allProducts.get(i).getAvailableStock());
        }
        ArrayList<Product> currentProductsInCart = sale.getProducts();
        for (int i = 0; i < allProducts.size(); i++) {
            for (int j = 0; j < currentProductsInCart.size(); j++) {

                if (allProducts.get(i).getId() == currentProductsInCart.get(j).getId()) {


                    Long aq = allProducts.get(i).getAvailableStock();

                    Long cq = currentProductsInCart.get(j).getQty();

                    System.out.println(aq + "< ==== >" + cq);

                    aq = aq - cq;


                    allProducts.get(i).setAvailableStock(aq);

                    System.out.println("AQ ====" + aq);
                    System.out.println("CQ ====" + cq);


                    //  allProducts.get(i).setRQty(aq);


                }

            }
        }
    }

    public void sync(Context context, String from) {


        boolean canSync = false;
            if (Store.getInstance().mHubConnectionReceiver != null) {

                if (Store.getInstance().mHubConnectionReceiver.getState().toString().toLowerCase().contains("disconnected")) {

                  //  Network.Check_Internet(context, "Check network", "Please Turn on Internet connectivity..");

                }
                else
                {
                    System.out.println("Network State in sync >>>"+Network.isNetworkAvailable(context));
                    if(Network.isNetworkAvailable(context)) {
                        canSync = true;
                        if(Store.getInstance().mHubConnectionReceiver==null)
                        {
                            System.out.println("----re login attempt");

                            SignalRService.reconnect(context);

                        }
                    }
                    else
                    {
                        System.out.println("No connection..");
                    }
                }

        }


        if(canSync) {
            boolean sync = false;
            AsyncTask saleStatus = null, saleOrderStatus = null, saleReturnStatus = null, customerStatus = null;


            ArrayList<Customer> customerList = Store.getInstance().customerList;

            System.out.println("Customer Size = " + customerList.size());


            if (customerList.size() == 0) {
                // Toast.makeText(context, "Nothing to sync...", Toast.LENGTH_SHORT).show();
            }
            for (int i = 0; i < customerList.size(); i++) {
                System.out.println("OBJ =" + customerList.get(i));
                System.out.println("cus OBJ =" + customerList.get(i).isSynced());
                if (!customerList.get(i).isSynced()) {
                    sync = true;

                    newcustomer = true;
                    Customer customer = createCustomer(customerList.get(i));
                    //BizUtils.prettyJson("new customer \n",customer);
                    System.out.println("cus OBJ new =" + customer.isSynced());
                   try {
                       customerStatus = new SaveCustomer(context, customer, i, from).execute();
                   }
                   catch (Exception e)
                   {

                   }
                }


            }


            int syncCount = 0;
            for (int i = 0; i < customerList.size(); i++) {
                System.out.println("OBJ =" + customerList.get(i).getSale().size());

                if (customerList.get(i).getId() != null) {

                    // printSyncData(customerList,i);

                    if (customerList.get(i).getSale().size() > 0) {
                        newcustomer = false;

                        for (int y = 0; y < customerList.get(i).getSalesOfCustomer().size(); y++) {

                            if (!customerList.get(i).getSalesOfCustomer().get(y).isSynced()) {
                                saleStatus = new Save(context, "sale/save", "SODetails", customerList.get(i).getSalesOfCustomer().get(y).getProducts(), customerList.get(i).getId(), customerList.get(i).getSalesOfCustomer().get(y), null, null, customerList.get(i)).execute();
                                System.out.println("Status === " + saleStatus.getStatus());
                                sync = true;

                            }

                        }


                    }
                    if (customerList.get(i).getSaleOrder().size() > 0) {
                        newcustomer = false;


                        for (int y = 0; y < customerList.get(i).getSaleOrdersOfCustomer().size(); y++) {
                            if (!customerList.get(i).getSaleOrdersOfCustomer().get(y).isSynced()) {
                                saleOrderStatus = new Save(context, "SaleOrder/save", "SaleOrderDetails", customerList.get(i).getSaleOrdersOfCustomer().get(y).getProducts(), customerList.get(i).getId(), null, customerList.get(i).getSaleOrdersOfCustomer().get(y), null, customerList.get(i)).execute();
                                sync = true;
                            }
                        }


                    }
                    if (customerList.get(i).getSaleReturn().size() > 0) {

                        for (int y = 0; y < customerList.get(i).getSaleReturnOfCustomer().size(); y++) {
                            if (!customerList.get(i).getSaleReturnOfCustomer().get(y).isSynced()) {
                                newcustomer = false;
                                saleReturnStatus = new Save(context, "SalesReturn/save", "SaleReturnDetails", customerList.get(i).getSaleReturnOfCustomer().get(y).getProducts(), customerList.get(i).getId(), null, null, customerList.get(i).getSaleReturnOfCustomer().get(y), customerList.get(i)).execute();
                                sync = true;
                            }
                        }

                    }
                    if (customerList.get(i).getReceipts().size() > 0) {
                        newcustomer = false;
                        System.out.println("Called save receipt");
                        for (int z = 0; z < customerList.get(i).getReceipts().size(); z++) {

                            if (customerList.get(i).getReceipts().get(z).isSynced()) {
                                System.out.println("Receipt already synced");
                            } else {
                                new SaveReceipt(context, "Sale/Receipt_Save", customerList.get(i).getReceipts().get(z), customerList.get(i).getId(), customerList.get(i)).execute();
                                sync = true;
                            }

                        }
                        System.out.println("Closing save receipt");

                    }

                    // System.out.println("SO P list size " + customerList.get(i).getsOPendingList().size());
                    // System.out.println("SO P list size " + customerList.get(i).getsOPendingList().size());
                    if (customerList.get(i).getsOPendingList().size() > 0) {
                        for (int x = 0; x < customerList.get(i).getsOPendingList().size(); x++) {
                            if (customerList.get(i).getsOPendingList().get(x).isSynced()) {
                                System.out.println("SO pendling  already synced");
                            } else {
                                boolean status = SignalRService.saleOrderMakeSales(customerList.get(i).getsOPendingList().get(x));
                                if (status) {
                                    System.out.println("SO pending saved");
                                    customerList.get(i).getsOPendingList().get(x).setSynced(true);
                                    sync = true;
                                    try {
                                        BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));
                                        System.err.println("DB Updated..on local storage");
                                    } catch (ClassNotFoundException e) {

                                        System.err.println("Unable to write to DB");
                                    }
                                } else {
                                    System.out.println("SO Pending Not  saved ");
                                }
                            }
                        }

                    }
                    // System.out.println("SOP Delete List size " + customerList.get(i).getSOPListDelete().size());


                    if (customerList.get(i).getSOPListDelete().size() > 0) {
                        for (int x = 0; x < customerList.get(i).getSOPListDelete().size(); x++) {
                            if (customerList.get(i).getSOPListDelete().get(x).isSynced()) {
                                System.out.println("SO pendling 'delete' already synced");
                            } else {

                                boolean status = SignalRService.salesOrderDelete(customerList.get(i).getSOPListDelete().get(x));
                                if (status) {
                                    System.out.println("SO pending deleted");
                                    customerList.get(i).getSOPListDelete().get(x).setSynced(true);
                                    sync = true;
                                    try {
                                        BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));
                                        System.err.println("DB Updated..on local storage");
                                    } catch (ClassNotFoundException e) {

                                        System.err.println("Unable to write to DB");
                                    }
                                } else {
                                    System.out.println("SO Pending Not  saved ");
                                }
                            }
                        }
                    }


                    if (customerList.get(i).getPayments().size() > 0) {
                        newcustomer = false;
                        System.out.println("Called save payments");
                        for (int z = 0; z < customerList.get(i).getPayments().size(); z++) {


                            if (customerList.get(i).getPayments().get(z).isSynced()) {
                                System.out.println("Payment already synced");
                            } else {
                                new SaveReceipt(context, "Sale/Payment_Save", customerList.get(i).getPayments().get(z), customerList.get(i).getId()).execute();
                                sync = true;
                            }

                        }
                        System.out.println("Closing save payments");

                    }


                    //new Save(context,"SaleOrder/save","SaleOrderDetails",Store.getInstance().addedProductForSalesOrder).execute();

                    // new Save(context,"sale/save","SODetails",Store.getInstance().addedProductForSales).execute();

                }


            }


            System.out.println("Sync dealer = " + Store.getInstance().dealer.isSynced());


            if (!Store.getInstance().dealer.isSynced()) {
                if (SignalRService.saveCompany(Store.getInstance().dealer)) {

                    Toast.makeText(context, "Dealer Updated", Toast.LENGTH_SHORT).show();
                    Store.getInstance().dealer.setSynced(true);
                    sync = true;

                    try {
                        BizUtils.storeAsJSON("Dealer", BizUtils.getJSON("dealer", Store.getInstance().dealer));
                        System.out.println("DB 'Dealer' Updated..on local storage");
                    } catch (ClassNotFoundException e) {

                        System.err.println("Unable to write to DB");
                    }

                } else {
                    Toast.makeText(context, "Dealer not Updated", Toast.LENGTH_SHORT).show();
                }
            } else {
                System.out.println("Dealer already synced");
            }

            for (int i = 0; i < Store.getInstance().prodcutSpecProcess.size(); i++) {
                System.out.println("Sync prod spec = " + Store.getInstance().prodcutSpecProcess.get(i).isSynced());
                if (Store.getInstance().prodcutSpecProcess.get(i).isSynced()) {
                    System.out.println("No sync prod spec");


                } else {
                    sync = true;
                    if (SignalRService.saveProductSpec(Store.getInstance().prodcutSpecProcess.get(i))) {
//                    Toast.makeText(context, "Prod Spec Updated", Toast.LENGTH_SHORT).show();
                        Store.getInstance().prodcutSpecProcess.get(i).setSynced(true);
                        try {
                            BizUtils.storeAsJSON("ProductSpecProcessList", BizUtils.getJSON("productspecprocess", Store.getInstance().prodcutSpecProcess));
                            System.out.println("DB 'ProductSpecProcessList' Updated..on local storage");
                        } catch (ClassNotFoundException e) {

                            System.err.println("Unable to write to DB");
                        }


                    } else {
                        //  Toast.makeText(context, "Prod spec not updated", Toast.LENGTH_SHORT).show();
                        BizUtils.prettyJson("product Spec", Store.getInstance().prodcutSpecProcess.get(i));
                    }

                }


            }
            for (int i = 0; i < Store.getInstance().newBankList.size(); i++) {
                if (Store.getInstance().newBankList.get(i).isSynced()) {
                    System.out.println("Already synced...");
                } else {
                    sync = true;
                    if (SignalRService.saveBank(Store.getInstance().newBankList.get(i))) {
                        Store.getInstance().newBankList.get(i).setSynced(true);
                        try {
                            BizUtils.storeAsJSON("NewBankList", BizUtils.getJSON("bankList", Store.getInstance().newBankList));
                            System.out.println("DB 'NewBankList' Updated..on local storage");
                        } catch (ClassNotFoundException e) {

                            System.err.println("Unable to write to DB 'NewBankList'");
                        }
                    }
                }
            }
            for (int i = 0; i < Store.getInstance().productList.size(); i++) {
                if (Store.getInstance().productList.get(i).isSynced()) {
                    //   System.out.println("Product : Already synced...");
                } else {
                    System.out.println("Product : syncing now...");
                    sync = true;
                    if (SignalRService.productSave(Store.getInstance().productList.get(i))) {
                        Store.getInstance().productList.get(i).setSynced(true);


                        try {
                            BizUtils.storeAsJSON("ProductList", BizUtils.getJSON("productList", Store.getInstance().productList));
                            System.out.println("DB 'ProductList' Updated..on local storage");
                        } catch (ClassNotFoundException e) {

                            System.err.println("Unable to write to DB 'ProductList'");
                        }

                    }
                }
            }


            System.out.println("Sync required = " + sync);


            if (!sync) {
                if (from.toLowerCase().contains("auto")) {
                    // System.out.print("No Data to sync...");
                    Store.getInstance().messageList.add("Auto Sync invoked");
                    Notification notification = new Notification();
                    notification.setTime(getCurrentTime());
                    notification.setMessage("Auto Sync invoked:No data to Sync");
                    //  Store.getInstance().notificationList.add(notification);

                } else {
                    Notification notification = new Notification();
                    notification.setMessage("Manual Sync invoked:No data to Sync");
                    notification.setTime(getCurrentTime());
                    Store.getInstance().notificationList.add(notification);


                    try {
                        Snackbar snackbar = null;

                        View notifier = DashboardActivity.customActionBar.findViewById(R.id.notify);
                        snackbar = Snackbar
                                .make(notifier, "Manual sync: No Data ", Snackbar.LENGTH_LONG);

                        // snackbar.show();
                        Store.getInstance().messageList.add("Manual Sync invoked");

                        showToast("No Data to sync...", context);
                    } catch (Exception e) {

                    }
                }


            } else {
                try {
                    Toast.makeText(context, "Saved...", Toast.LENGTH_SHORT).show();
                    BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));
                    System.err.println("DB Updated..on local storage");
                } catch (ClassNotFoundException e) {
                    System.err.println("Unable to write to DB");
                }
            }
        }
        else
        {
            Toast.makeText(context, "No Network Connection...", Toast.LENGTH_SHORT).show();
        }
    }

    public void showToast(final String toast, final Context context) {
        ((Activity) context).runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static Company getCompany() {

        Company company = null;
        for (Company company1 : Store.getInstance().companyList) {

            if (company1.getId().equals(Store.getInstance().companyID)) {

                company = company1;
                return company;
            }
        }
        return company;
    }

    public static void windowDetails(Context context)
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        Store.getInstance().height = height;
        Store.getInstance().width = width;
    }
    public Boolean write(Context context, String fname, Company company, Customer customer, ArrayList<Product> productList, String refCode,Sale sale, SaleOrder saleOrder, SaleReturn saleReturn) {
        try {


            BizUtils bizUtils = new BizUtils();
            String refNo;
            String currentSaleType = "";
            String roundOffValue = "0.00";
            if(sale!=null) {
                refNo = sale.getRefCode();
                paymentModeValue = sale.getPaymentMode();
                currentSaleType = "sale";
                roundOffValue = String.format("%.2f",sale.getRoundOffValue());

            }
            if(saleOrder !=null)
            {
                refNo = saleOrder.getRefCode();

                paymentModeValue = saleOrder.getPaymentMode();
                currentSaleType = "sale order";
                roundOffValue = String.format("%.2f",saleOrder.getRoundOffValue());
            }
            if(saleReturn !=null)
            {
                refNo = saleReturn.getRefCode();
                paymentModeValue = saleReturn.getPaymentMode();
                currentSaleType = "sale return";
                roundOffValue = String.format("%.2f",saleReturn.getRoundOffValue());
            }


            billIdValue = refCode;
            billDateValue = bizUtils.getCurrentTime();

            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "fmcg");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                // Do something on success
                Log.d("FOLDER CREATED", "TRUE");
            } else {
                // Do something else on failure
                Log.d("FOLDER CREATED", "FALSE");
            }
            folder.getAbsolutePath();
            Log.d("ABS PATH", folder.getAbsolutePath());

            //Create file path for Pdf
            fpath = folder.getAbsolutePath() + "/" + fname + ".pdf";

            File file = new File(fpath);

            System.out.println("fpath = " + fpath);

            if (!file.exists()) {
                file.createNewFile();
            }
            // To customise the text of the pdf
            // we can use FontFamily
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12);
            // create an instance of itext document
            Document document = new Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();


            try {
                // get receipt stream
                Bitmap bmp = null;
                if (Store.getInstance().dealerLogo != null) {
                    bmp = BizUtils.StringToBitMap(Store.getInstance().dealerLogo);
                } else {
                    InputStream ims = context.getAssets().open("fmcglogo64.png");
                    bmp = BitmapFactory.decodeStream(ims);
                }

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());
                image.setBorder(Image.NO_BORDER);

                PdfPTable logo = new PdfPTable(1);
                logo.setWidthPercentage(15);
                image.setAlignment(Image.ALIGN_CENTER);
                logo.addCell(image);

                document.add(logo);


            } catch (IOException ex) {
                System.out.println("Logo Exception = " + ex);
                ex.getStackTrace();
            }

            //using add method in document to insert a paragraph
            PdfPTable thank = new PdfPTable(1);
            thank.setWidthPercentage(100);
            thank.addCell(getCell(context.getString(R.string.app_name), PdfPCell.ALIGN_CENTER));
            document.add(thank);
            document.add(new Paragraph("_____________________________________________________________________________"));


            PdfPTable cn = new PdfPTable(1);
            cn.setWidthPercentage(100);

            cn.addCell(getCell("Company Name :" + company.getCompanyName(), PdfPCell.ALIGN_LEFT));
            document.add(cn);
            document.add(new Paragraph("Address :" + company.getAddressLine1() + "," + company.getAddressLine2() + "," + company.getPostalCode()));
            document.add(new Paragraph("Ph No:" + company.getTelephoneNo()));
            document.add(new Paragraph("Email :" + company.getEMailId()));
            document.add(new Paragraph("GST No:" + company.getGSTNo()));
            document.add(new Paragraph("_____________________________________________________________________________"));
            document.add(new Paragraph("Bill Id :" + billIdValue));
            document.add(new Paragraph("Bill Date :" + billDateValue));


            PdfPTable cn1 = new PdfPTable(1);
            cn1.setWidthPercentage(100);
            cn1.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            /*cn1.addCell(getCell("                   ", PdfPCell.ALIGN_LEFT));*/
            cn1.addCell(getCell("Customer Details", PdfPCell.ALIGN_CENTER));


            document.add(cn1);

            if(customer.getId()!=0) {
                document.add(new Paragraph("ID: " + customer.getId()));
            }
            document.add(new Paragraph("Name: " + customer.getLedger().getLedgerName()));
            document.add(new Paragraph("Person In Charge: " + customer.getLedger().getPersonIncharge()));

            document.add(new Paragraph("Ph No: " + customer.getLedger().getMobileNo()));
            document.add(new Paragraph("Address : " + customer.getLedger().getAddressLine1() + "," + customer.getLedger().getAddressLine2()));


            document.add(new Paragraph("GST No: " + customer.getLedger().getGSTNo()));
            document.add(new Paragraph("_____________________________________________________________________________"));
            PdfPTable cn2 = new PdfPTable(1);
            cn2.setWidthPercentage(100);
            cn2.addCell(getCell("                   ", PdfPCell.ALIGN_LEFT));


           /* document.add(cn2);*/

            PdfPTable pm = new PdfPTable(1);
            pm.setWidthPercentage(100);

            pm.addCell(getCell("Sale/Sale Order/Sale Return: " + currentSaleType, PdfPTable.ALIGN_LEFT));
            document.add(pm);
            if(getTransactionType(currentSaleType)==Store.getInstance().SALE) {
                PdfPTable pm1 = new PdfPTable(1);
                pm1.setWidthPercentage(100);
                pm1.addCell(getCell("Payment Mode: " + paymentModeValue, PdfPTable.ALIGN_LEFT));
                document.add(pm1);
                if(paymentModeValue.toLowerCase().contains("cheque"))
                {
                    PdfPTable pm2 = new PdfPTable(1);
                    pm2.setWidthPercentage(100);
                    pm2.addCell(getCell("Cheque No: " + sale.getChequeNo(), PdfPTable.ALIGN_LEFT));
                    document.add(pm2);
                }
            }




            PdfPTable cn3 = new PdfPTable(1);
            cn3.setWidthPercentage(100);
            cn3.addCell(getCell("Item Details", PdfPCell.ALIGN_CENTER));
            cn2.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(cn3);


            PdfPTable line = new PdfPTable(1);
            line.setWidthPercentage(100);
            line.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line);


            int numberOfCols = 6;
            if(getTransactionType(currentSaleType)==Store.getInstance().SALE_RETURN) {
                numberOfCols = 8;
            }
            PdfPTable table = new PdfPTable(numberOfCols);
            table.setWidthPercentage(100);

            table.addCell(getCell("S.No", PdfPCell.ALIGN_LEFT));
            //table.addCell(getCell("ID", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Name", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Qty", PdfPCell.ALIGN_LEFT));
            if(getTransactionType(currentSaleType)==Store.getInstance().SALE_RETURN) {
                table.addCell(getCell("Reason", PdfPCell.ALIGN_LEFT));
                table.addCell(getCell("Resale", PdfPCell.ALIGN_LEFT));
            }
            table.addCell(getCell("Price", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Discount", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Amount", PdfPCell.ALIGN_RIGHT));

            document.add(table);
            PdfPTable line1 = new PdfPTable(1);
            line1.setWidthPercentage(100);
            line1.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line1);

            for (int i = 0; i < productList.size(); i++) {
                PdfPTable table1 = new PdfPTable(numberOfCols);
                table1.setWidthPercentage(100);

                table1.addCell(getCell(String.valueOf(i + 1), PdfPCell.ALIGN_LEFT));
               // table1.addCell(getCell(String.valueOf(productList.get(i).getId()), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(productList.get(i).getProductName(), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(productList.get(i).getQty()), PdfPCell.ALIGN_LEFT));
                if(getTransactionType(currentSaleType)==Store.getInstance().SALE_RETURN) {
                    table1.addCell(getCell(String.valueOf(productList.get(i).getParticulars()), PdfPCell.ALIGN_LEFT));
                    if(productList.get(i).isResale()) {
                        table1.addCell(getCell("Yes", PdfPCell.ALIGN_LEFT));
                    }
                    else
                    {
                        table1.addCell(getCell("No", PdfPCell.ALIGN_LEFT));
                    }
                }
                table1.addCell(getCell(String.valueOf(String.format("%.2f", productList.get(i).getMRP())), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(String.format("%.2f", productList.get(i).getDiscountAmount())), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(String.format("%.2f", productList.get(i).getFinalPrice())), PdfPCell.ALIGN_RIGHT));

                document.add(table1);

            }


            String gstSpace = "";

            String st = String.valueOf(subTotal.getText().toString());
            int subTotalLength = st.length();

            String gstx = String.valueOf(Store.getInstance().GST.getText().toString());
            int gstLength = gstx.length();


            int c = subTotalLength - gstLength;

            for (int f = 0; f < c; f++) {
                gstSpace = gstSpace + " ";
            }

            double s = Double.parseDouble(subTotal.getText().toString());

            double rrm = 0;
            if (TextUtils.isEmpty(fromCustomer.getText().toString())) {
                rrm = 0;
            } else {
                rrm = Double.parseDouble(fromCustomer.getText().toString());
            }

            double brm = Double.parseDouble(DashboardActivity.tenderAmount.getText().toString());

            String mgt = String.valueOf(String.format("%.2f", s));
            String ra = String.valueOf(String.format("%.2f", rrm));
            String ba = String.valueOf(String.format("%.2f", brm));

            int mgtL = mgt.length();
            int raL = ra.length();
            int baL = ba.length();

            String mgSpace = "";
            String raSpace = "";
            String baSpace = "";


            int x = 0;
            int y = 0;
            if (mgtL > raL) {
                x = mgtL - raL;
                y = mgtL - baL;
                for (int i = 0; i < x; i++) {
                    raSpace = raSpace + " ";

                }
                for (int i = 0; i < y; i++) {
                    baSpace = baSpace + " ";

                }
            } else if (raL > mgtL) {
                x = raL - mgtL;
                y = raL - baL;
                for (int i = 0; i < x; i++) {
                    mgSpace = mgSpace + " ";
                }
                for (int i = 0; i < y; i++) {
                    baSpace = baSpace + " ";

                }
            }


            PdfPTable line3 = new PdfPTable(1);
            line3.setWidthPercentage(100);
            line3.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line3);
            document.add(new Paragraph("                                                  "));

            PdfPTable stx = new PdfPTable(1);
            stx.setWidthPercentage(97);

            stx.addCell(getCell("Sub Total RM = " + String.format("%7.2f",Double.parseDouble( subTotal.getText().toString() ))+  "  ", PdfPCell.ALIGN_RIGHT));
            stx.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));
            document.add(stx);
            PdfPTable gst = new PdfPTable(1);
            gst.setWidthPercentage(97);

            gst.addCell(getCell("GST RM = " + String.format("%7.2f",Double.parseDouble(  Store.getInstance().GST.getText().toString()  ))+ "  ", PdfPCell.ALIGN_RIGHT));
            document.add(gst);


            PdfPTable gt = new PdfPTable(1);
            gt.setWidthPercentage(97);


            double disAmnt = 0;
            if (TextUtils.isEmpty(DashboardActivity.discountValue.getText().toString())) {
                disAmnt = 0;
            } else {
                disAmnt = Double.parseDouble(DashboardActivity.discountValue.getText().toString());
            }
            if(BizUtils.getDiscountType(discountType)==Store.getInstance().DISCOUNT_FOR_GRABD_TOTAL) {
                gt.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));


                if(DashboardActivity.grandDiscountTypeSpinner.getSelectedItem().toString().equals(Store.getInstance().discountTypePercentage)) {

                    gt.addCell(getCell("Discount ( " + disAmnt + " "+DashboardActivity.grandDiscountTypeSpinner.getSelectedItem().toString()+" ) = " + String.format("%7.2f",Double.parseDouble(appDiscount.getText().toString()))  + "  ", PdfPCell.ALIGN_RIGHT));

                }else
                {
                    gt.addCell(getCell("Discount ( "+DashboardActivity.grandDiscountTypeSpinner.getSelectedItem().toString()+" ) = " + String.format("%7.2f",Double.parseDouble(appDiscount.getText().toString()))  + "  ", PdfPCell.ALIGN_RIGHT));
                }

            }
            gt.addCell(getCell("      ", PdfPCell.ALIGN_RIGHT));
            gt.addCell(getCell("Round Off RM = " +String.format("%7.2f",Double.parseDouble(roundOffValue)) + "  ", PdfPCell.ALIGN_RIGHT));

            gt.addCell(getCell("      ", PdfPCell.ALIGN_RIGHT));
            gt.addCell(getCell("Grand Total RM = " + String.format("%7.2f",Double.parseDouble(DashboardActivity.grandTotal.getText().toString() ))+ "  ", PdfPCell.ALIGN_RIGHT));
            gt.addCell(getCell("____________________________________________________________________________", PdfPCell.ALIGN_LEFT));

            gt.addCell(getCell("  ", PdfPCell.ALIGN_RIGHT));
            System.out.println("Pay Mode ====" + paymentModeValue);



                if(getTransactionType(currentSaleType)==Store.getInstance().SALE) {
                    if(sale.getPaymentMode().toLowerCase().contains("cash")) {
                if (TextUtils.isEmpty(fromCustomer.getText().toString())) {
                    gt.addCell(getCell("Received RM = " +  String.format("%7.2f", Double.parseDouble("0")) + "  ", PdfPCell.ALIGN_RIGHT));
                } else {
                    gt.addCell(getCell("Received RM = " +  String.format("%7.2f", Double.parseDouble(fromCustomer.getText().toString())) + "  ", PdfPCell.ALIGN_RIGHT));
                }
                gt.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));
                gt.addCell(getCell("Balance RM = " +  String.format("%7.2f", Double.parseDouble(DashboardActivity.tenderAmount.getText().toString()))  + "  ", PdfPCell.ALIGN_RIGHT));
                gt.addCell(getCell("___________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            }}



            gt.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));
            gt.addCell(getCell("Dealer Name = " + Store.getInstance().dealer.getCompanyName(), PdfPCell.ALIGN_LEFT));

            document.add(gt);


            PdfPTable thank1 = new PdfPTable(1);
            thank1.setWidthPercentage(100);
            thank1.addCell(getCell(" ", PdfPCell.ALIGN_CENTER));
            thank1.addCell(getCell("***Thank You***", PdfPCell.ALIGN_CENTER));
            document.add(new Paragraph("_____________________________________________________________________________"));
            thank1.setWidthPercentage(100);
            thank1.addCell(getCell(" ", PdfPCell.ALIGN_CENTER));
            thank1.addCell(getCell("Powered By Denariusoft SDN BHD", PdfPCell.ALIGN_CENTER));
            document.add(thank1);


            // close document
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
    }

    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public static String calculateShortCode(String saleType) {
        String result = "";

        System.out.print("---Sale type -----" + saleType);
        System.out.print("---current month -----" + getCurrentYearAndMonth());


        String month = Integer.toHexString(Integer.parseInt(getCurrentYearAndMonth().substring(0, 2)));
        String year = getCurrentYearAndMonth().substring(5, 7);

        if (saleType.toLowerCase().contains("return")) {

            result = "SR";


            result = result + year + month.toUpperCase();


        } else if (saleType.toLowerCase().contains("receipt")) {

            result = "RT";


            result = result + year + month.toUpperCase();


        } else if (saleType.toLowerCase().contains("order")) {
            result = "SO";
            result = result + year + month.toUpperCase();


        } else {
            result = "SA";
            result = result + year + month.toUpperCase();

        }

        System.out.println("OUT = >" + result);
        return result;

    }

    public static void prettyJson(String TAG, Object object) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(object);
        System.out.println(TAG + " = " + json);
    }

    public static void storeAsJSON(String DBName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "FMCG_DB");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, DBName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void readAsJSON(String DBName, final Context context) {
        //Customer Type
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();
        Type customerList = null;
        Type dealerType = null;
        Type productSpecList = null;
        Type bankList = null;


        try {
            initDB();
        } catch (IOException e) {
            e.printStackTrace();
        }


        File root = new File(Environment.getExternalStorageDirectory(), "FMCG_DB");
        if (!root.exists()) {

            Toast.makeText(context, "DB-root for " + DBName + " not found", Toast.LENGTH_SHORT).show();
        }
        File file = new File(root, DBName);
        if (!file.exists()) {
            Toast.makeText(context, "DB named " + DBName + " not found", Toast.LENGTH_SHORT).show();
        }

        File file2 = new File(root, "ProductSpecProcessList");
        if (!file2.exists()) {
            Toast.makeText(context, "DB named 'ProductSpecProcessList' not found", Toast.LENGTH_SHORT).show();
        } else {
            try {
                file2.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file3 = new File(root, "Dealer");
        if (!file3.exists()) {
            Toast.makeText(context, "DB named 'Dealer' not found", Toast.LENGTH_SHORT).show();
        } else {
            try {
                file3.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File file4 = new File(root, "NewBankList");
        if (!file4.exists()) {
            Toast.makeText(context, "DB named 'NewBankList' not found", Toast.LENGTH_SHORT).show();
        } else {
            try {
                file4.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        StringBuilder text = new StringBuilder();
        StringBuilder text2 = new StringBuilder();
        StringBuilder text3 = new StringBuilder();
        StringBuilder newBankJsonAsText = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();

            BufferedReader br2 = new BufferedReader(new FileReader(file2));
            String line2;
            while ((line2 = br2.readLine()) != null) {
                text2.append(line2);
            }
            br2.close();


            BufferedReader br3 = new BufferedReader(new FileReader(file3));
            String line3;
            while ((line3 = br3.readLine()) != null) {
                text3.append(line3);
            }
            br3.close();


            BufferedReader br4 = new BufferedReader(new FileReader(file4));
            String line4;
            while ((line4 = br4.readLine()) != null) {
                newBankJsonAsText.append(line4);
            }
            br4.close();


            //reading customer list as json and storing again
            customerList = new TypeToken<Collection<Customer>>() {
            }.getType();
            ArrayList<Customer> customers = new ArrayList<Customer>();
            customers = gson.fromJson(text.toString(), customerList);

            //reading customer list as json and storing again
            productSpecList = new TypeToken<Collection<ProductSpecProcess>>() {
            }.getType();
            ArrayList<ProductSpecProcess> productSpecProcesses = new ArrayList<ProductSpecProcess>();
            productSpecProcesses = gson.fromJson(text2.toString(), productSpecList);


            //reading customer list as json and storing again
            dealerType = new TypeToken<Company>() {
            }.getType();
            Company dealer = new Company();
            dealer = gson.fromJson(text3.toString(), dealerType);


            //reading customer list as json and storing again
            bankList = new TypeToken<Collection<Bank>>() {
            }.getType();
            ArrayList<Bank> bankArrayList = new ArrayList<Bank>();
            bankArrayList = gson.fromJson(newBankJsonAsText.toString(), bankList);


            final Dialog dialog = new Dialog(context);

            dialog.setContentView(R.layout.offline_data);
            TextView customer = (TextView) dialog.findViewById(R.id.customers);
            TextView sale = (TextView) dialog.findViewById(R.id.sale);
            TextView saleOrder = (TextView) dialog.findViewById(R.id.sale_order);
            TextView salesReturn = (TextView) dialog.findViewById(R.id.sale_return);
            TextView salesOrderPendingList = (TextView) dialog.findViewById(R.id.sales_order_pending_list);
            TextView receipt = (TextView) dialog.findViewById(R.id.receipt);
            TextView dealr = (TextView) dialog.findViewById(R.id.dealer);
            TextView prodctSpec = (TextView) dialog.findViewById(R.id.product_spec);
            TextView banks = (TextView) dialog.findViewById(R.id.banks);
            Button load = (Button) dialog.findViewById(R.id.load);


            int c = 0, s = 0, so = 0, sopl = 0, sr = 0, r = 0, d = 0, ps = 0, b = 0,p=0;
            int c_synced = 0, s_synced = 0, so_synced = 0, sopl_synced = 0, sr_synced = 0, r_synced = 0, d_synced = 0, ps_synced = 0, b_synced = 0,p_synced=0;
            for (int i = 0; i < customers.size(); i++) {

                c++;
                if (customers.get(i).isSynced()) {
                    c_synced++;
                }
                s = s + customers.get(i).getSalesOfCustomer().size();
                for (int x = 0; x < customers.get(i).getSalesOfCustomer().size(); x++) {
                    if (!customers.get(i).getSalesOfCustomer().get(x).isSynced()) {
                        Log.d("SO", "Unsaved");
                        // BizUtils.prettyJson("unsaved sale",customers.get(i).getSalesOfCustomer().get(x));
                    } else {
                        s_synced++;

                        BizUtils.prettyJson("saved sale", customers.get(i).getSalesOfCustomer().get(x));

                    }

                }


                so = so + customers.get(i).getSaleOrdersOfCustomer().size();
                for (int x = 0; x < customers.get(i).getSaleOrdersOfCustomer().size(); x++) {
                    if (!customers.get(i).getSaleOrdersOfCustomer().get(x).isSynced()) {
                        Log.d("SO", "Unsaved");
                    } else {
                        so_synced++;
                    }

                }
                sr = sr + customers.get(i).getSaleReturnOfCustomer().size();
                for (int x = 0; x < customers.get(i).getSaleReturnOfCustomer().size(); x++) {
                    if (!customers.get(i).getSaleReturnOfCustomer().get(x).isSynced()) {
                        Log.d("SR", "Unsaved");
                    } else {
                        sr_synced++;
                    }

                }
                sopl = sopl + customers.get(i).getsOPendingList().size();
                for (int x = 0; x < customers.get(i).getsOPendingList().size(); x++) {
                    if (!customers.get(i).getsOPendingList().get(x).isSynced()) {
                        Log.d("SOP", "Unsaved");
                    } else {
                        sopl_synced++;
                    }

                }
                r = r + customers.get(i).getReceipts().size();
                for (int x = 0; x < customers.get(i).getReceipts().size(); x++) {
                    if (!customers.get(i).getReceipts().get(x).isSynced()) {

                    } else {
                        r_synced++;
                    }

                }



            }

            if (dealer.isSynced()) {
                d_synced++;
            }

            for (int i = 0; i < productSpecProcesses.size(); i++) {

                if (productSpecProcesses.get(i).isSynced()) {
                    ps_synced++;
                }
                ps++;
            }
            for (int i = 0; i < bankArrayList.size(); i++) {
                if (bankArrayList.get(i).isSynced()) {
                    b_synced++;
                }
                b++;
            }


            customer.setText(String.valueOf(c_synced + " of " + c + " synced"));
            sale.setText(String.valueOf(s_synced + " of " + s + " synced"));
            saleOrder.setText(String.valueOf(so_synced + " of " + so + " synced"));
            salesReturn.setText(String.valueOf(sr_synced + " of " + sr + " synced"));
            salesOrderPendingList.setText(String.valueOf(sopl_synced + " of " + sopl + " synced"));
            receipt.setText(String.valueOf(r_synced + " of " + r + " synced"));
            if (d_synced == 0) {
                dealr.setText(String.valueOf("not synced   "));
            } else {
                dealr.setText(String.valueOf("synced      "));
            }
            prodctSpec.setText(String.valueOf(ps_synced + " of " + ps + " synced"));
            banks.setText(String.valueOf(b_synced + " of " + b + " synced"));


            final ArrayList<Customer> finalCustomers = customers;
            final Company finalDealer = dealer;
            final ArrayList<ProductSpecProcess> finalProductSpecProcesses = productSpecProcesses;
            final ArrayList<Bank> finalBankArrayList = bankArrayList;
            load.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    Store.getInstance().customerList = finalCustomers;
                    Store.getInstance().dealer = finalDealer;
                    Store.getInstance().prodcutSpecProcess = finalProductSpecProcesses;
                    Store.getInstance().bankList = finalBankArrayList;

                    Toast.makeText(context, "Loaded..", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

            dialog.show();
            BizUtils.prettyJson("FROM DB UPDATED -- ", Store.getInstance().customerList);


        } catch (IOException e) {
            //You'll need to add proper error handling here
        }
    }

    public static String getJSON(String db, final Object o) throws ClassNotFoundException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        Gson gson = gsonBuilder.create();

        Type companyType = null;
        Type customerList = null;
        Type dealer = null;
        Type productSpec = null;
        Type bankList = null;
        Type productList = null;
        String json = "";
        if (db.equals("company")) {
            companyType = new TypeToken<Collection<Company>>() {
            }.getType();

            json = gson.toJson(Store.getInstance().companyList, companyType);
        }
        if (db.equals("customer")) {
            customerList = new TypeToken<Collection<Customer>>() {
            }.getType();
            json = gson.toJson(Store.getInstance().customerList, customerList);


        }
        if (db.equals("dealer")) {
            dealer = new TypeToken<Company>() {
            }.getType();
            json = gson.toJson(Store.getInstance().dealer, dealer);

        }

        if (db.equals("productspecprocess")) {
            productSpec = new TypeToken<Collection<ProductSpecProcess>>() {
            }.getType();
            json = gson.toJson(Store.getInstance().prodcutSpecProcess, productSpec);

        }
        if (db.equals("bankList")) {
            bankList = new TypeToken<Collection<Bank>>() {
            }.getType();
            json = gson.toJson(Store.getInstance().newBankList, bankList);

        }
        if (db.equals("productList")) {
            productList = new TypeToken<Collection<Product>>() {
            }.getType();
            json = gson.toJson(Store.getInstance().productList, productList);

        }


        System.out.println(json);

        return json;


    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    public Customer createCustomer(Customer customer_) {
        Customer customer = new Customer();


        customer.setId(customer_.getId());
        customer.setLedgerId(customer_.getLedgerId());
        customer.setLedgerName(customer_.getLedger().getLedgerName());
        customer.setPersonIncharge(customer_.getLedger().getPersonIncharge());
        customer.setAddressLine1(customer_.getLedger().getAddressLine1());
        customer.setAddressLine2(customer_.getLedger().getAddressLine2());
        customer.setCityName(customer_.getLedger().getCityName());
        customer.setMobileNo(customer_.getLedger().getMobileNo());
        customer.setGSTNo(customer_.getLedger().getGSTNo());
        customer.setSynced(customer_.isSynced());


        Ledger ledger = new Ledger();
        ledger.setId(customer_.getLedger().getId());
        ledger.setLedgerName(customer_.getLedger().getLedgerName());
        ledger.setPersonIncharge(customer_.getLedger().getPersonIncharge());
        ledger.setAddressLine1(customer_.getLedger().getAddressLine1());
        ledger.setAddressLine2(customer_.getLedger().getAddressLine2());
        ledger.setCityName(customer_.getLedger().getCityName());
        ledger.setMobileNo(customer_.getLedger().getMobileNo());
        ledger.setGSTNo(customer_.getLedger().getGSTNo());
        ledger.setTelephoneNo(customer_.getLedger().getTelephoneNo());
        ledger.setEMailId(customer_.getLedger().getEMailId());
        try {
            ledger.setOPBal(Double.valueOf(customer_.getLedger().getOPBal()));
            ledger.setACType(customer_.getLedger().getACType());
        }catch (Exception e)
        {
            System.out.println("Customer ex" + e);
        }

        System.out.println("Account group Id " + currentAccGrpId);
        System.out.println("Account group name " + currentAccGrpName);
        ledger.setAccountGroupId(currentAccGrpId);
        ledger.setAccountName(currentAccGrpName);


        customer.setLedger(ledger);


        return customer;
    }

    public static Date getDateFromString(String d, String format) throws ParseException {


        d = d + " 4:30 pm";
        System.out.println("Input String : " + d);
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

        Date d1 = df.parse(d);
        System.out.println("Date: " + d1);
        System.out.println("Date in MM/dd/yyyy format is: " + df.format(d1));


        return d1;
    }

    public static void initDB() throws IOException {
        File root = new File(Environment.getExternalStorageDirectory(), "FMCG_DB");
        if (!root.exists()) {

            root.mkdirs();
        }
        File file = new File(root, "Dealer");
        if (!file.exists()) {

            file.createNewFile();
            Log.d("DB", "New DB- Dealer");
            try {
                BizUtils.storeAsJSON("Dealer", BizUtils.getJSON("dealer", Store.getInstance().dealer));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        File file2 = new File(root, "ProductSpecProcessList");
        if (!file2.exists()) {
            file2.createNewFile();
            Log.d("DB", "New DB- Prod Spec");
            try {
                BizUtils.storeAsJSON("ProductSpecProcessList", BizUtils.getJSON("productspecprocess", Store.getInstance().prodcutSpecProcess));
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        File file3 = new File(root, "customerList");
        if (!file3.exists()) {
            file3.createNewFile();
            Log.d("DB", "New DB- CustomerList");
            try {
                BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        File file4 = new File(root, "NewBankList");
        if (!file4.exists()) {
            file4.createNewFile();
            Log.d("DB", "New DB- NewBankList");
            try {
                BizUtils.storeAsJSON("NewBankList", BizUtils.getJSON("bankList", Store.getInstance().customerList));

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }



    }
    public static boolean decimalFormatter(double d)
    {
        boolean stat = false;


        String text = Double.toString(Math.abs(d));
        Log.d("decimal points = ",text);
        int integerPlaces = text.indexOf('.');
        int decimalPlaces = text.length() - integerPlaces - 1;


        if(decimalPlaces>2)
        {
            stat = false;
        }
        else
        {
            stat = true;
        }

        return stat;
    }
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }
    public static JSONObject validateChequeDate(int day, int month, int year) throws JSONException {


        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE,day);
        cal.set(Calendar.MONTH,month-1);
        cal.set(Calendar.YEAR,year);

        Log.d("chequedate", String.valueOf(cal.getTime()));

        Calendar afterDate = Calendar.getInstance();
        afterDate.add(Calendar.YEAR,3);
        Log.d("chequedate-after", String.valueOf(afterDate.getTime()));



        Calendar beforeDate = Calendar.getInstance();
        beforeDate.add(Calendar.MONTH,-6);
        Log.d("chequedate-before", String.valueOf(beforeDate.getTime()));


        boolean status;
        String errMsg = "";
        if(cal.before(afterDate) && cal.after(beforeDate))
        {

            status = true;
        }
        else
        {
            if(!cal.before(afterDate))
            {
                errMsg = "The cheque date should not be dated after 3 Years from current date";
            }

            if(!cal.after(beforeDate))
            {
                errMsg = "The cheque date should not be dated before 6 Months from current date";
            }

            status = false;
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("status",status);
        jsonObject.put("errMsg",errMsg);

        return jsonObject;


    }

}
