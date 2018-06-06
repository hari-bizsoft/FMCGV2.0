/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.Manifest;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.BTLib.BTDeviceList;
import com.bizsoft.fmcgv2.BTLib.BTPrint;
import com.bizsoft.fmcgv2.Tables.Bank;
import com.bizsoft.fmcgv2.adapter.AddedProductAdapter;
import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.adapter.CustomerAdapter;
import com.bizsoft.fmcgv2.adapter.ProductAdapter;
import com.bizsoft.fmcgv2.adapter.StockGroupAdapter;
import com.bizsoft.fmcgv2.dataobject.AddCustomerResponse;
import com.bizsoft.fmcgv2.dataobject.Company;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Payment;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.ProductModel;
import com.bizsoft.fmcgv2.dataobject.ProductSaveResponse;
import com.bizsoft.fmcgv2.dataobject.Receipt;
import com.bizsoft.fmcgv2.dataobject.Sale;
import com.bizsoft.fmcgv2.dataobject.SaleOrder;
import com.bizsoft.fmcgv2.dataobject.SaleReturn;
import com.bizsoft.fmcgv2.dataobject.StockGroup;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.ConnectivityChangeReceiver;
import com.bizsoft.fmcgv2.service.HttpHandler;
import com.bizsoft.fmcgv2.service.PrinterService;
import com.bizsoft.fmcgv2.service.SignalRService;
import com.bizsoft.fmcgv2.service.UIUtil;
import com.bizsoft.fmcgv2.service.Waiter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Formatter;
import java.util.HashMap;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.bizsoft.fmcgv2.service.BizUtils.decimalFormatter;
import static com.bizsoft.fmcgv2.service.BizUtils.prettyJson;

public class DashboardActivity extends AppCompatActivity {
    private static final int LOCATION_PERMISSION_CONSTANT = 777;
    public static ArrayAdapter productAdapter;
    String[] perms = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
    int permsRequestCode = 200;

    Spinner saleType;
    ImageButton productSearch, customerSearch, stockHomeSearch;
    ArrayList<String> saleTypeList = new ArrayList<String>();
    public static String currentSaleType;
    ListView customerListView;
    public static AutoCompleteTextView customerNameKey;
    TextView customerName;
    AutoCompleteTextView stockGroupKey;
    TextView stockGroupName;
    private ListView stockHomeListView;
    EditText productNameKey;
    public static TextView productName;
    public static ListView productListView;
    public static ListView addedProductListView;
    ImageView sync;
    Button save;
    ImageButton sales, salesOrder, salesReturn;
    private ImageButton home;
    final static int BLUETOOTH_FLAG = 619;
    Button print;
    private double mainSubTotal = 0;
    ArrayList<Product> myaddedProduct = new ArrayList<Product>();
    private Long voi = Long.valueOf(1);
    ImageButton reports, reprint;
    private SharedPreferences permissionStatus;
    Button preview;
    BizUtils bizUtils;
    TextView cfc,ta,cdl,bnl;
    ImageButton dateChooser;
    TextView clearCategory;
    TextView discountLabel;
    TextView paymentLabel;

    public static EditText discountValue;

    Spinner isGst;
    public static String isGstValue;
    public static EditText fromCustomer;
    public static TextView tenderAmount;
    public static TextView subTotal;
    public TextView GST;
    public static TextView grandTotal;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static final int REQUEST_PERMISSION_SETTING1 = 102;
    private boolean sentToSettings = false;
    private String fromCustomerValue = String.valueOf(0);
    private String tenderAmountValue = String.valueOf(0);
    private boolean newcustomer = false;
    private ImageButton invoiceList;
    Button clearData;
    public static Spinner paymentMode;
    public static String paymentModeValue;
    boolean savedCurrentTransaction = false;
    private boolean syncStatus;
    private ImageButton receipt;
    private ImageButton payment;
    private static final String TAG= DashboardActivity.class.getName();
    TextView cn;
    AutoCompleteTextView bankName;
    EditText chequeDate;
    static EditText chequeNo;
    View view17;
    TextView note;
    Spinner discount;
    TextView discountSpinnerLabel;
    public static Spinner grandDiscountTypeSpinner;

    public static TextView appDiscount;

    private Waiter waiter;  //Thread which controls idle time
    private int year,month,day;
    public static String discountType;
    private double tempGt=0;
    private boolean storeGT = false;
    private ArrayList<Product> prodListToShow;
    private Date chequeDateValue = new Date();
    private ProductAdapter Padapter;
    private TextWatcher fromCustomerWatcher;
    public static View customActionBar;
    public static String lastSavedBillType = "none";
    private ArrayList<String> gstTypeList;
    private ArrayList<String> discountTypeList;
    private Button printCC;
    public  static  TextView roundOffText;
   // public  static   EditText roundOffValue;
    public  static   TextView roundOffLabel;
    public static ArrayAdapter<String> customerNameListAdapter;
    private ArrayList<String> grandDiscountTypeList;
    private String grandDiscountType;


    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Activity:","Resume");

        Store.getInstance().addedProductAdapter.setFrom("dashboard");
    }
    public Application getApp()
    {
        return  this.getApplication();
    }
    public void touch()
    {
        waiter.touch();


    }

    @Override
    public void onUserInteraction()
    {
        super.onUserInteraction();
        touch();
        Log.d(TAG, "User interaction to "+this.toString());
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);

        saleType = (Spinner) findViewById(R.id.sale_type_spinner);
        customerSearch = (ImageButton) findViewById(R.id.customer_search);
        productSearch = (ImageButton) findViewById(R.id.product_search);
        customerNameKey = (AutoCompleteTextView) findViewById(R.id.customer_name_key);
        customerName = (TextView) findViewById(R.id.dealer_name);
        stockGroupKey = (AutoCompleteTextView) findViewById(R.id.stock_group_key);
        stockGroupName = (TextView) findViewById(R.id.stock_group_name);
        stockHomeSearch = (ImageButton) findViewById(R.id.stock_group_search);
        productNameKey = (EditText) findViewById(R.id.product_key);
        clearCategory = (TextView) findViewById(R.id.clear_category);
        cfc = (TextView) findViewById(R.id.cfc);
        ta = (TextView) findViewById(R.id.ta);
        cn = (TextView) findViewById(R.id.cn);
        chequeNo = (EditText) findViewById(R.id.cheque_no);
        appDiscount = (TextView) findViewById(R.id.app_discount);
        discountLabel = (TextView) findViewById(R.id.discount_label);
        discountSpinnerLabel = (TextView) findViewById(R.id.discount_spinner_label);
        paymentLabel = (TextView) findViewById(R.id.paymode_label);
        roundOffLabel = (TextView) findViewById(R.id.round_of_label);
        roundOffText = (TextView) findViewById(R.id.round_off_text);
       // roundOffValue = (EditText) findViewById(R.id.round_off_value);


        cdl = (TextView) findViewById(R.id.cdl);
        bnl = (TextView) findViewById(R.id.bnl);
        chequeDate = (EditText) findViewById(R.id.cheque_date);
        bankName = (AutoCompleteTextView) findViewById(R.id.bank_name);
        clearData = (Button) findViewById(R.id.clear_data);
        addedProductListView = (ListView) findViewById(R.id.product_list);
        save = (Button) findViewById(R.id.save);
        print = (Button) findViewById(R.id.dc_print);
        preview = (Button) findViewById(R.id.preview);
        isGst = (Spinner) findViewById(R.id.is_gst);
        fromCustomer = (EditText) findViewById(R.id.money_from_customer);
        tenderAmount = (TextView) findViewById(R.id.tender_amount);
        subTotal = (TextView) findViewById(R.id.sub_total);
        GST = (TextView) findViewById(R.id.GST);
        grandTotal = (TextView) findViewById(R.id.grand_total);
        paymentMode = (Spinner) findViewById(R.id.payment_mode_spinner);
        dateChooser = (ImageButton) findViewById(R.id.date_chooser);
        view17 = findViewById(R.id.view17);
        note = (TextView) findViewById(R.id.note);
        discount = (Spinner) findViewById(R.id.discount);
        printCC = (Button) findViewById(R.id.print_cc);
        grandDiscountTypeSpinner = (Spinner) findViewById(R.id.discount_type);

        discountValue = (EditText) findViewById(R.id.discount_value);
        cn.setVisibility(View.GONE);
        chequeNo.setVisibility(View.GONE);
        save.setEnabled(false);
        save.setBackgroundColor(getResources().getColor(R.color.grey));
        preview.setEnabled(false);
        Log.d("Preview", "Disabled 1");
        preview.setBackgroundColor(getResources().getColor(R.color.grey));
        stockGroupName.setText("All");
        BizUtils.getCurrentDatAndTimeInDF();



        UIUtil.setActionBarDesign(DashboardActivity.this,getSupportActionBar());

        ArrayAdapter<String> bankAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Bank.getNames());
        prettyJson("bank names",Bank.getNames());

        bankName.setThreshold(1);
        bankName.setAdapter(bankAdapter);
        registerReceiver(
                new ConnectivityChangeReceiver(),
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));


        chequeDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int inType = chequeDate.getInputType(); // backup the input type
                chequeDate.setInputType(InputType.TYPE_NULL); // disable soft input
                chequeDate.onTouchEvent(event); // call native handler
                chequeDate.setInputType(inType); // restore input type
                return true; // consume touch even
            }
        });
        chequeDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(999);
            }
        });


        BizUtils.windowDetails(DashboardActivity.this);
        //clearCategory.setVisibility(View.INVISIBLE);
        clearCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Choosed all category", Toast.LENGTH_SHORT).show();

                Store.getInstance().currentStockGroup = null;

                stockGroupName.setText("All");
            }
        });

        bizUtils = new BizUtils();

        Store.getInstance().fromCustomer = fromCustomer;
        Store.getInstance().tenderAmount = tenderAmount;
        Store.getInstance().subTotal = subTotal;
        Store.getInstance().GST = GST;
        Store.getInstance().grandTotal = grandTotal;

        permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
        Log.d(TAG, "Starting application" + this.toString());
        waiter = new Waiter(Store.getInstance().idleTimeLimit * 60 * 1000, DashboardActivity.this);
        waiter.start();

        Store.getInstance().waiter = waiter;





        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (permissionStatus.getBoolean("ACCESS_FINE_LOCATION", false)) {
                getLocationPermission();
            }
            getPermission();

        }
        if (paymentModeValue==null)
            {

                fromCustomerWatcher = new TextWatcher() {
                    String before;
                    boolean status;

                    @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        before = s.toString();

                if (! (currentSaleType.toLowerCase().contains("order") || currentSaleType.toLowerCase().contains("return") )) {
                    if (!(paymentModeValue.contains("PNT") | paymentModeValue.contains("Cheque"))) {
                        if (!currentSaleType.toLowerCase().contains("order")) {
                            save.setEnabled(false);
                            save.setBackgroundColor(getResources().getColor(R.color.grey));
                            preview.setEnabled(false);
                            Log.d("Preview", "Disabled 2");
                            preview.setBackgroundColor(getResources().getColor(R.color.grey));
                        }
                    } else {

                        preview.setEnabled(true);
                        Log.d("Preview", "Enabled 2.0");
                        save.setEnabled(true);
                        save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        preview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                        double fc = 0;
                        if(!TextUtils.isEmpty(s))
                        {
                            fc = Double.parseDouble(s.toString());
                        }

                status = decimalFormatter(fc);
            }

            @Override
            public void afterTextChanged(Editable s) {

                if(!status)
                {
                    fromCustomer.setText(before);
                    fromCustomer.post(new Runnable() {
                        @Override
                        public void run() {
                            fromCustomer.setSelection(fromCustomer.getText().toString().trim().length());
                        }
                    });
                }

                if (!(paymentModeValue.contains("PNT") | paymentModeValue.contains("Cheque") )) {

                    if (! (currentSaleType.toLowerCase().contains("order") || currentSaleType.toLowerCase().contains("return") ))
                    {
                        if (Store.getInstance().fromCustomer.getText() != null) {
                            double balance = 0;
                            if (!Store.getInstance().fromCustomer.getText().toString().equals("") || TextUtils.isEmpty(Store.getInstance().fromCustomer.getText().toString())) {

                                double gt = Double.parseDouble(grandTotal.getText().toString());
                                double fc = 0;
                                try {
                                    fc = Double.parseDouble(fromCustomer.getText().toString());
                                    balance = fc - gt;
                                } catch (Exception e) {
                                    fc = 0;
                                    balance = 0;
                                }


                                if (balance < 0) {
                                    //Toast.makeText(DashboardActivity.this, "Invalid amount received from customer", Toast.LENGTH_SHORT).show();
                                    //fromCustomer.setError("Invalid Amount");
                                    //fromCustomer.setSelection(fromCustomer.getText().length());
                                    note.setText("Get amount greater than "+String.valueOf(String.format("%.2f", gt)));
                                    note.setTextColor(Color.RED);

                                    save.setEnabled(false);
                                    save.setBackgroundColor(getResources().getColor(R.color.grey));
                                    preview.setEnabled(false);
                                    Log.d("Preview", "Disabled 3");
                                    preview.setBackgroundColor(getResources().getColor(R.color.grey));

                                } else {
                                    note.setText(getString(R.string.cfc_note));
                                    note.setTextColor(Color.BLUE);

                                    save.setEnabled(true);
                                    save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                    tenderAmount.setText(String.valueOf(String.format("%.2f", balance)));
                                    if(balance<0)
                                    {
                                        tenderAmount.setText(String.valueOf(String.format("%.2f", 0.00)));
                                    }
                                    preview.setEnabled(true);
                                    Log.d("Preview", "Enabled 4");
                                    preview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                }
                            }
                        }
                    }

                } else {

                    Log.d("Balance Calc", "Not applicable for this method");
                }

            }
        };
        fromCustomer.addTextChangedListener(fromCustomerWatcher);

    }
    else

            if(!(paymentModeValue.contains("PNT") | paymentModeValue.contains("Cheque")))
            {
                save.setEnabled(true);
                save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                preview.setEnabled(true);
                Log.d("Preview", "Enabled 4");
                preview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));



            }


/*       roundOffValue.addTextChangedListener(new TextWatcher() {
           double gt =0;
           double rov=0;
           String regex = "^(\\d*\\.)?\\d+$";
           double finalAmount = 0;
           boolean status = false;

           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

               gt = Double.parseDouble(grandTotal.getText().toString());
               rov =0;

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

               Log.d("Round OFF char",s.toString());
               if(!s.toString().trim().matches(regex))
               {
                   Log.d("Round OFF char","Invalid Format");
                  // roundOffText.setText(grandTotal.getText().toString());


               }
               else
               {
                   rov =  Double.parseDouble(s.toString());
               }


           }

           @Override
           public void afterTextChanged(Editable s) {


               if(rov<=gt)
               {
                   finalAmount = gt - rov;

                   roundOffText.setText(String.valueOf(String.format("%.2f",rov)));
                   double x = roundOff(Double.parseDouble(subTotal.getText().toString()) + Double.parseDouble(GST.getText().toString()) -  Double.parseDouble(appDiscount.getText().toString()));

                   grandTotal.setText(String.valueOf(String.format("%.2f",x-rov)));

               }
               else
               {
                   Toast.makeText(DashboardActivity.this, "Invalid Amount Entered", Toast.LENGTH_SHORT).show();
               }


           }
       });*/

        discountValue.addTextChangedListener(new TextWatcher() {


            double gt =0;
            double dc  =0;
            double da =0;
            public String before;
            boolean status;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                tempGt = Store.getInstance().addedProductAdapter.grandTotal;

                try {
                    fromCustomer.setText("0");
                    fromCustomerValue = "0";
                }
                catch (Exception e)
                {
                    Log.e("from customer","error");
                }
                before = s.toString();
                Log.d("decimal before",s.toString());
                }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try
                {
                    if(TextUtils.isEmpty(discountValue.getText()))
                    {
                        dc =0;
                    }
                    else
                    {
                        dc = Double.parseDouble(discountValue.getText().toString());
                    }

                    Log.d("decimalvalidator",String.valueOf(decimalFormatter(dc))+" "+ String.valueOf(dc));
                    status = decimalFormatter(dc);


                }
                catch (Exception e)
                {
                    System.out.print("Ex =>>>>> "+e);
                }



                System.out.println("tgt = <><><>"+tempGt);
                System.out.println("dc = <><><>"+dc);


                if(Store.getInstance().discountTypePercentage.equals(grandDiscountType)) {
                    if (dc >= 100) {
                        if (tempGt != 0) {
                            Toast.makeText(DashboardActivity.this, "Discount % not applicable..", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        da = tempGt * (dc / 100);
                        gt = tempGt - (tempGt * (dc / 100));

                    }
                }
                else if(Store.getInstance().discountTypeRM.equals(grandDiscountType))
                {
                  if(dc<=tempGt)
                  {
                      gt =  tempGt-dc;
                      da = dc;
                  }
                  else
                  {
                      Toast.makeText(DashboardActivity.this, "Discount RM not applicable..", Toast.LENGTH_SHORT).show();
                  }


                }
                    System.out.println("Added Prod Adap GT <?>= " + Store.getInstance().addedProductAdapter.grandTotal);
                    System.out.println("GT <?>= " + gt);
                    System.out.println("DP <?>= " + dc);

                    if (Store.getInstance().addedProductAdapter.grandTotal == 0) {
                        grandTotal.setText(String.valueOf("0"));
                        appDiscount.setText(String.valueOf("0"));
                    } else {

                        grandTotal.setText(String.valueOf(String.format("%.2f", roundOff(gt))));
                        appDiscount.setText(String.valueOf(String.format("%.2f", da)));

                    }




            }

            @Override
            public void afterTextChanged(Editable s) {

                Log.d("decimal after",s.toString());

                if(!status)
                {
                    discountValue.setText(before);
                    discountValue.post(new Runnable() {
                        @Override
                        public void run() {
                            discountValue.setSelection(discountValue.getText().toString().trim().length());
                        }
                    });
                }

                grandTotal.setText(String.valueOf(roundOff(gt)));
            }
        });

        setIsGst();

        setDiscountType();
        setGrandDiscountType();
        grandDiscountTypeSpinner.setSelection(0);


        dateChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(999);
            }
        });
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            printLastSaved("Dealer Copy");


            }
        });
        printCC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printLastSaved("Customer Copy");

            }
        });


        preview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (getCurrentProducts().size() > 0) {

                    Intent intent = new Intent(DashboardActivity.this, PrintPreview.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(DashboardActivity.this, "Please add some product to sale/sale order", Toast.LENGTH_SHORT).show();
                }
            }
        });

        clearData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            clearData();
            }
        });


        Store.getInstance().addedProductAdapter = new AddedProductAdapter(DashboardActivity.this, Store.getInstance().addedProductList);
        Store.getInstance().addedProductAdapter.setFrom("dashboard");
        addedProductListView.setAdapter(Store.getInstance().addedProductAdapter);


        setSaleType();
        //new CustomerDetails(DashboardActivity.this).execute();

           //new TransactionList().execute();




         customerNameListAdapter= new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, Customer.getCustomerNameList());


        customerNameKey.setAdapter(customerNameListAdapter);
        customerNameKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);

                new StoreCurrentCusDetails(selected).execute();
                //Toast.makeText(DashboardActivity.this, ""+Customer.getCustomerList().get(position), Toast.LENGTH_SHORT).show();

            }
        });


      customerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerNameKey.setText("");

                String key = customerNameKey.getText().toString();
                showCustomerList(key);
            }
        });

        ArrayAdapter<String> StockGroupAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, StockGroup.getNamesWithID());
        stockGroupKey.setThreshold(1);
        stockGroupKey.setAdapter(StockGroupAdapter);
        stockGroupKey.setThreshold(1);
        stockGroupKey.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);

                new StoreCurrentStockDetails(selected).execute();
                //Toast.makeText(DashboardActivity.this, ""+Customer.getCustomerList().get(position), Toast.LENGTH_SHORT).show();

            }
        });
        stockHomeSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Store.getInstance().currentCustomer != null) {
                    String key = stockGroupKey.getText().toString();

                    showStockHome(key);
                } else {
                    Toast.makeText(DashboardActivity.this, "Please choose customer first", Toast.LENGTH_SHORT).show();
                }


            }
        });
        productSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("Stock Group Id-------"+Store.getInstance().currentStockGroupId);
                if (Store.getInstance().currentStockGroup != null && Store.getInstance().currentStockGroupId!=null ) {
                    String key = productNameKey.getText().toString().toLowerCase();


                    showProductListNew(key, Store.getInstance().currentStockGroupId);
                } else {
                    Toast.makeText(DashboardActivity.this, "Please choose category..", Toast.LENGTH_SHORT).show();
                    // String key = productNameKey.getText().toString();
                    // showProductList(key, null);
                }


            }
        });


        save.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {


                if(validate())
                {

                    System.out.println("Size -----------");


                    Product product = new Product();

                    product.setQty(voi);
                    myaddedProduct.add(product);

                    for (int x = 0; x < myaddedProduct.size(); x++) {
                        System.out.println("My added prod === " + myaddedProduct.get(x).getQty());


                    }
                    if (Store.getInstance().addedProductList.size() > 0) {


                        System.out.print("Sale Type " + currentSaleType);
                        if (currentSaleType.toLowerCase().contains("order")) {
                            System.out.println("Size -----------" + Store.getInstance().addedProductList.size());


                            for (int i = 0; i < Store.getInstance().customerList.size(); i++) {
                                System.out.println(i + "====" + Store.getInstance().currentCustomerPosition);
                                if (Store.getInstance().currentCustomerPosition == i) {
                                    fromCustomerValue = fromCustomer.getText().toString();
                                    tenderAmountValue = tenderAmount.getText().toString();

                                    Log.d("received amount", fromCustomerValue);
                                    Log.d("balance amount", tenderAmountValue);

                                    double r = Store.getInstance().customerList.get(i).getSaleOrderReceivedAmount();
                                    double b = Store.getInstance().customerList.get(i).getSaleOrderBalance();


                                    if(TextUtils.isEmpty(fromCustomerValue))
                                    {
                                        fromCustomerValue = String.valueOf(0);
                                    }

                                    r = r + Double.parseDouble(fromCustomerValue);
                                    b = b + Double.parseDouble(tenderAmountValue);

                                    Store.getInstance().customerList.get(i).setSaleOrderReceivedAmount(r);
                                    Store.getInstance().customerList.get(i).setSaleOrderBalance(b);


                                    System.out.println("Adding to customer " + Store.getInstance().customerList.get(i).getLedgerName());
                                    Store.getInstance().customerList.get(i).saleOrder.addAll(getCurrentProducts());
                                    Store.getInstance().addedProductForSalesOrder.addAll(getCurrentProducts());


                                    if (storeOffline(Store.getInstance().customerList.get(i))) {
                                        System.out.println("Sale order Added to offline list of customer");

                                        clearWOConfirmation();
                                    }

                                    lastSavedBillType = currentSaleType;
                                    print(currentSaleType);
                                    clearList();

                                    Toast.makeText(DashboardActivity.this, "Sale order saved offline", Toast.LENGTH_SHORT).show();
                                    System.out.println("Sales Order  Size --------------------->" + Store.getInstance().customerList.get(i).getSale());


                                }


                            }
                            Store.getInstance().currentStockGroup = null;
                            Store.getInstance().currentCustomer = null;


                        }  if (currentSaleType.toLowerCase().contains("return")) {
                            System.out.println("Sales return Size -----------" + Store.getInstance().addedProductList.size());


                            for (int i = 0; i < Store.getInstance().customerList.size(); i++) {
                                System.out.println(i + "====" + Store.getInstance().currentCustomerPosition);
                                if (Store.getInstance().currentCustomerPosition == i) {
                                    fromCustomerValue = fromCustomer.getText().toString();
                                    tenderAmountValue = tenderAmount.getText().toString();

                                    Log.d("received amount", fromCustomerValue);
                                    Log.d("balance amount", tenderAmountValue);

                                    double r = Store.getInstance().customerList.get(i).getSaleOrderReceivedAmount();
                                    double b = Store.getInstance().customerList.get(i).getSaleOrderBalance();


                                    if(TextUtils.isEmpty(fromCustomerValue))
                                    {
                                        fromCustomerValue = String.valueOf(0);
                                    }


                                    r = r + Double.parseDouble(fromCustomerValue);
                                    b = b + Double.parseDouble(tenderAmountValue);

                                    Store.getInstance().customerList.get(i).setSaleOrderReceivedAmount(r);
                                    Store.getInstance().customerList.get(i).setSaleOrderBalance(b);


                                    System.out.println("Adding to customer " + Store.getInstance().customerList.get(i).getLedgerName());
                                    Store.getInstance().customerList.get(i).saleReturn.addAll(getCurrentProducts());
                                    Store.getInstance().addedProductForSalesReturn.addAll(getCurrentProducts());


                                    if (storeOffline(Store.getInstance().customerList.get(i))) {
                                        clearWOConfirmation();
                                        System.out.println("Sale  return  Added to offline list of customer");
                                    }
                                    lastSavedBillType = currentSaleType;
                                    print(currentSaleType);
                                    clearList();
                                    Toast.makeText(DashboardActivity.this, "Sale return saved offline", Toast.LENGTH_SHORT).show();
                                    System.out.println("Sales return   Size --------------------->" + Store.getInstance().customerList.get(i).getSaleReturn().size());
                                }
                            }
                            Store.getInstance().currentStockGroup = null;
                            Store.getInstance().currentCustomer = null;


                        }
                        if(! (currentSaleType.toLowerCase().contains("return") || currentSaleType.toLowerCase().contains("order")) )
                        {
                            System.out.println("Size -----------" + Store.getInstance().addedProductList.size());
                            for (int i = 0; i < Store.getInstance().customerList.size(); i++) {
                                if (Store.getInstance().currentCustomerPosition == i) {


                                    double r =0;
                                    double b =0;


                                    if(paymentModeValue.toLowerCase().contains("cash")) {
                                        fromCustomerValue = fromCustomer.getText().toString();
                                        tenderAmountValue = tenderAmount.getText().toString();
                                        Log.d("recived amount", fromCustomerValue);
                                        Log.d("balance amount", tenderAmountValue);
                                        r = Store.getInstance().customerList.get(i).getReceivedAmount();
                                        b = Store.getInstance().customerList.get(i).getBalance();


                                        if(TextUtils.isEmpty(fromCustomerValue))
                                        {
                                            fromCustomerValue = String.valueOf(0);
                                        }

                                        r = r + Double.parseDouble(fromCustomerValue);
                                        b = b + Double.parseDouble(tenderAmountValue);

                                    }
                                    Store.getInstance().customerList.get(i).setReceivedAmount(r);
                                    Store.getInstance().customerList.get(i).setBalance(b);


                                    System.out.println("Adding to customer " + Store.getInstance().customerList.get(i).getLedgerName());
                                    System.out.println("Adding to customer " + Store.getInstance().customerList.get(i).getLedgerName());
                                    Store.getInstance().customerList.get(i).sale.addAll(getCurrentProducts());
                                    Store.getInstance().addedProductForSales.addAll(getCurrentProducts());
                                    if (storeOffline(Store.getInstance().customerList.get(i))) {
                                        clearWOConfirmation();
                                        System.out.println("Sales Added to offline list of customer");
                                     }
                                    lastSavedBillType = currentSaleType;
                                    print(currentSaleType);
                                    clearList();
                                       System.out.println("Sales Size --------------------->" + Store.getInstance().customerList.get(i).getSale().size());

                                    Toast.makeText(DashboardActivity.this, "Sale saved offline", Toast.LENGTH_SHORT).show();
                                    save.setEnabled(false);
                                    save.setBackgroundColor(getResources().getColor(R.color.grey));
                                    preview.setEnabled(false);
                                    Log.d("Preview","Disabled 5");
                                    preview.setBackgroundColor(getResources().getColor(R.color.grey));
                                }

                            }


                            Toast.makeText(DashboardActivity.this, "Sale saved offline", Toast.LENGTH_SHORT).show();

                            Store.getInstance().currentStockGroup = null;
                            Store.getInstance().currentCustomer = null;


                        }
                    } else {
                        Toast.makeText(DashboardActivity.this, "Please add product", Toast.LENGTH_SHORT).show();
                    }






                    runOnUiThread(new Runnable() {
                        public void run() {
                            //Do something on UiThread

                            if(!currentSaleType.toLowerCase().contains("order")) {
                                save.setEnabled(false);
                                save.setBackgroundColor(getResources().getColor(R.color.grey));
                                preview.setEnabled(false);
                                Log.d("Preview", "Disabled 6");
                                preview.setBackgroundColor(getResources().getColor(R.color.grey));
                            }
                            customerName.setText(String.valueOf(""));
                            stockGroupName.setText(String.valueOf(""));

                            fromCustomer.setText(String.valueOf(""));
                            grandTotal.setText(String.valueOf("0.00"));
                            tenderAmount.setText(String.valueOf("0.00"));
                            discountValue.setText(String.valueOf("0"));
                            appDiscount.setText(String.valueOf("0"));
                            chequeNo.setText(String.valueOf(""));
                            chequeDate.setText(String.valueOf(""));
                            bankName.setText(String.valueOf(""));
                        }
                    });

                }






            }
        });




    }

    public static double roundOff(double finalAmount) {
        Log.d("Input", String.valueOf(finalAmount));
        double x = finalAmount - (long) finalAmount;
        x =  Double.parseDouble(String.format("%.2f",x));

        int bd = (int) finalAmount;

        x = x * 100;
        int y = (int) x;
        int r  = y%10;
        int fd = 0;
        String action = "";
        double roundOffVal = 0.00;
        if(r>5)
        {
            if(r<8)
            {
                if(r==8)
                {
                    fd = y + (10 - r);
                    action = "+";
                    roundOffVal = 10 -r;
                }
                else {
                    fd = y - (r -5);
                    action = "-";
                    roundOffVal = r -5;
                }

            }
            else {
                fd = y + (10 - r);
                action = "+";
                roundOffVal = 10 -r;
            }
        }
        else
        {
            if(r<3)
            {
                fd = y - r;
                action = "-";
                roundOffVal = r;
            }
            else
            {
                fd = y + (5-r);
                action = "+";
                roundOffVal = 5-r;
            }

        }



        double result = bd + (fd*0.01);

        double rem =  roundOffVal/100;
        if(rem==0)
        {
        action = "";
        }

        roundOffText.setText(action+String.valueOf(String.format("%.2f",rem)));
        Log.d("Round off ", String.valueOf(result));
        return result;
    }


    private void printLastSaved(String dc) {
        ArrayList<Product> temp = Store.getInstance().addedProductList;

        System.out.println("Added product in cart ==== " + temp.size());




        try {
            if (BTPrint.btsocket == null) {


                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(DashboardActivity.this);
                alertDialog.setMessage("Please connect to the bluetooth to initiate printing");
                alertDialog.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(DashboardActivity.this, BTDeviceList.class);

                        startActivityForResult(intent, BLUETOOTH_FLAG);

                        Toast.makeText(DashboardActivity.this, "Initializing bluetooth connection...", Toast.LENGTH_SHORT).show();




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



            } else {


                Store.getInstance().printerContext = DashboardActivity.this;

                Customer customer = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);

                if(lastSavedBillType.toLowerCase().contains("return"))
                {
                    temp =   customer.getSaleReturnOfCustomer().get(customer.getSaleReturnOfCustomer().size()-1).getProducts();
                    PrinterService.print(DashboardActivity.this,customer, "Sale Return", temp, null, null, customer.getSaleReturnOfCustomer().get(customer.getSaleReturnOfCustomer().size()-1),dc);

                }
                if(lastSavedBillType.toLowerCase().contains("order"))
                {
                    temp =   customer.getSaleOrdersOfCustomer().get(customer.getSaleOrdersOfCustomer().size()-1).getProducts();
                    PrinterService.print(DashboardActivity.this,customer, "Sale Order", temp, null,customer.getSaleOrdersOfCustomer().get(customer.getSaleOrdersOfCustomer().size()-1), null,dc);

                }
                if(! (lastSavedBillType.toLowerCase().contains("return") || lastSavedBillType.toLowerCase().contains("order")) )
                {
                    temp =   customer.getSalesOfCustomer().get(customer.getSalesOfCustomer().size()-1).getProducts();
                    PrinterService.print(DashboardActivity.this,customer, "Sale", temp,customer.getSalesOfCustomer().get(customer.getSalesOfCustomer().size()-1), null, null,dc);

                }
                if(lastSavedBillType.toLowerCase().contains("none"))
                {
                    Toast.makeText(DashboardActivity.this, "No last bills found..", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            System.out.println("Exception " + e);

        }
    }

    private void checkBluetoothConnection() {

        if (BTPrint.btsocket == null) {
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(DashboardActivity.this);
            alertDialog.setMessage("Please connect to the bluetooth to initiate printing");
            alertDialog.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent intent = new Intent(DashboardActivity.this, BTDeviceList.class);

                    startActivityForResult(intent, BLUETOOTH_FLAG);

                    Toast.makeText(DashboardActivity.this, "Initializing bluetooth connection...", Toast.LENGTH_SHORT).show();





                }
            });
            alertDialog.setNegativeButton(R.string.noReloadMsg, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.out.println("Do nothing....");
                }
            });

            alertDialog.show();
        }
    }

    private void writeToFile(Sale sale, SaleOrder saleOrder, SaleReturn saleReturn) {

        Company company = bizUtils.getCompany();
        Customer customer  = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);
            Boolean status = false;

        String refNo = "";
        String paymentModeValue = "";
        if(sale!=null) {
            refNo = sale.getRefCode();
            paymentModeValue = sale.getPaymentMode();

        }
        if(saleOrder !=null)
        {
            refNo = saleOrder.getRefCode();

            paymentModeValue = saleOrder.getPaymentMode();
        }
        if(saleReturn !=null)
        {
            refNo = saleReturn.getRefCode();
            paymentModeValue = saleReturn.getPaymentMode();

        }
        status = bizUtils.write(DashboardActivity.this,customer.getLedgerName()+" | "+ refNo +"|"+bizUtils.getCurrentTime()+" | ( "+company.getCompanyName()+" )" +" | "+Store.getInstance().dealerId,company,customer,Store.getInstance().addedProductList,refNo, sale,saleOrder,saleReturn);

        if (status)
        {
            Toast.makeText(DashboardActivity.this, "Pdf Saved", Toast.LENGTH_SHORT).show();

            saleType.setSelection(0);
        }
        else {
            Toast.makeText(DashboardActivity.this, "Pdf not Saved", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean storeOffline(Customer customer) {
        boolean status = false;

        System.out.println("currentSaleType = "+currentSaleType);


        if (currentSaleType.toLowerCase().contains("order")) {
            int beforeAdd = customer.getSaleOrdersOfCustomer().size();


            System.out.println("Before add = " + beforeAdd);
            SaleOrder saleOrder = new SaleOrder();
            saleOrder.setTempId(Store.getInstance().companyID + "-" + Store.getInstance().dealerName + "-" + bizUtils.getCurrentTime());
            saleOrder.setProducts(getCurrentProducts());
            saleOrder.setSaleType(currentSaleType);
            saleOrder.setGstType(isGstValue);

            saleOrder.setGst(Double.parseDouble(String.format("%.2f", Double.parseDouble(GST.getText().toString()))));
            saleOrder.setSubTotal(Double.parseDouble(subTotal.getText().toString()));
            saleOrder.setGrandTotal(Double.parseDouble(grandTotal.getText().toString()));
            saleOrder.setBalance(Double.parseDouble(tenderAmountValue));
            saleOrder.setReceivedAmount(Double.parseDouble(fromCustomerValue));
            saleOrder.setReceivedAmount(Double.parseDouble(fromCustomerValue));
            saleOrder.setPaymentMode(paymentModeValue);
           saleOrder.setRoundOffValue(Double.parseDouble(roundOffText.getText().toString()));
           saleOrder.setGrandTotalDiscountType(grandDiscountType);







            double dc = 0;
            if(!TextUtils.isEmpty(discountValue.getText()) )
            {

                    dc = Double.parseDouble(appDiscount.getText().toString());
                    saleOrder.setDiscountPercentage(Double.parseDouble(discountValue.getText().toString()));

            }
            saleOrder.setDiscountType(discountType);
            saleOrder.setDiscountValue(dc);

            System.out.println("===============PAYMODE=========="+paymentModeValue);


            if(paymentModeValue.toLowerCase().contains("cheque")) {
                saleOrder.setChequeNo(chequeNo.getText().toString());
                saleOrder.setChequeDate(chequeDate.getText().toString());
                saleOrder.setChequeBankName(bankName.getText().toString());
            }
            else
            {
                saleOrder.setChequeNo("null");
                saleOrder.setChequeDate("null");
                saleOrder.setChequeBankName("null");
            }


            int invce = 0;
            for(int i=0;i<Store.getInstance().customerList.size();i++)
            {
                    ArrayList<SaleOrder> invoice = Store.getInstance().customerList.get(i).getSaleOrdersOfCustomer();
                if(invoice.size()>0) {
                    invce = invce + invoice.size();
                }

            }

            System.out.println("Ref Code Sale -----"+saleOrder.getRefCode());

            saleOrder.setRefCode(String.valueOf(BizUtils.calculateShortCode(DashboardActivity.currentSaleType)+DashboardActivity.calculateRefCode(Store.getInstance().user.getSORefCode(),invce)));



            customer.getSaleOrdersOfCustomer().add(saleOrder);
            if (customer.getSaleOrdersOfCustomer().size() > beforeAdd) {
                status = true;
             }

            writeToFile(null,saleOrder,null);



        }
        if (currentSaleType.toLowerCase().contains("return")) {
            int beforeAdd = customer.getSaleReturnOfCustomer().size();

            SaleReturn saleReturn = new SaleReturn();
            saleReturn.setTempId(Store.getInstance().companyID + "-" + Store.getInstance().dealerName + "-" + bizUtils.getCurrentTime());
            saleReturn.setProducts(getCurrentProducts());
            saleReturn.setSaleType(currentSaleType);
            saleReturn.setGstType(isGstValue);
            saleReturn.setGst(Double.parseDouble(String.format("%.2f", Double.parseDouble(GST.getText().toString()))));
            saleReturn.setSubTotal(Double.parseDouble(subTotal.getText().toString()));
            saleReturn.setGrandTotal(Double.parseDouble(grandTotal.getText().toString()));
            saleReturn.setBalance(Double.parseDouble(tenderAmountValue));
            saleReturn.setReceivedAmount(Double.parseDouble(fromCustomerValue));
            saleReturn.setPaymentMode(paymentModeValue);
            saleReturn.setRoundOffValue(Double.parseDouble(roundOffText.getText().toString()));
            saleReturn.setGrandTotalDiscountType(grandDiscountType);

            int invce = 0;
            for(int i=0;i<Store.getInstance().customerList.size();i++)
            {
                ArrayList<SaleReturn> invoice = Store.getInstance().customerList.get(i).getSaleReturnOfCustomer();
                if(invoice.size()>0) {
                    invce = invce + invoice.size();
                }

            }
            saleReturn.setRefCode(String.valueOf(BizUtils.calculateShortCode(DashboardActivity.currentSaleType)+DashboardActivity.calculateRefCode(Store.getInstance().user.getSORefCode(),invce)));
            System.out.println("===============PAYMODE=========="+paymentModeValue);

            double dc = 0;
            if(!TextUtils.isEmpty(discountValue.getText()) )
            {

                    dc = Double.parseDouble(appDiscount.getText().toString());
                saleReturn.setDiscountPercentage(Double.parseDouble(discountValue.getText().toString()));


            }

            saleReturn.setDiscountType(discountType);
            saleReturn.setDiscountValue(dc);
            if(paymentModeValue.toLowerCase().contains("cheque")) {
                saleReturn.setChequeNo(chequeNo.getText().toString());
                saleReturn.setChequeDate(chequeDate.getText().toString());
                saleReturn.setChequeBankName(bankName.getText().toString());
            }
            else
            {
                saleReturn.setChequeNo("null");
                saleReturn.setChequeDate("null");
                saleReturn.setChequeBankName("null");
            }
            customer.getSaleReturnOfCustomer().add(saleReturn);


            if (customer.getSaleReturnOfCustomer().size() > beforeAdd) {

                bizUtils.updateStock(saleReturn);
                status = true;
            }
            writeToFile(null,null,saleReturn);
        }  if( ! (currentSaleType.toLowerCase().contains("return") || currentSaleType.toLowerCase().contains("order")) ) {

            int beforeAdd = customer.getSalesOfCustomer().size();

            Sale sale = new Sale();
            sale.setTempId(Store.getInstance().companyID + "-" + Store.getInstance().dealerName + "-" + bizUtils.getCurrentTime());
            sale.setProducts(getCurrentProducts());
            sale.setSaleType(currentSaleType);
            sale.setGstType(isGstValue);


            sale.setGst(Double.parseDouble(String.format("%.2f", Double.parseDouble(GST.getText().toString()))));
            sale.setSubTotal(Double.parseDouble(subTotal.getText().toString()));
            sale.setGrandTotal(Double.parseDouble(grandTotal.getText().toString()));
            sale.setBalance(Double.parseDouble(tenderAmountValue));
            sale.setReceivedAmount(Double.parseDouble(fromCustomerValue));
            sale.setPaymentMode(paymentModeValue);
            sale.setRoundOffValue(Double.parseDouble(roundOffText.getText().toString()));
            sale.setGrandTotalDiscountType(grandDiscountType);
            System.out.println("===============PAYMODE=========="+paymentModeValue);
            int invce = 0;
            for(int i=0;i<Store.getInstance().customerList.size();i++)
            {
                ArrayList<Sale> invoice = Store.getInstance().customerList.get(i).getSalesOfCustomer();

                if(invoice.size()>0) {
                    invce = invce + invoice.size();
                }

            }
            sale.setRefCode(String.valueOf(BizUtils.calculateShortCode(DashboardActivity.currentSaleType)+DashboardActivity.calculateRefCode(Store.getInstance().user.getSalRefCode(),invce)));

            double dc = 0;
            if(!TextUtils.isEmpty(discountValue.getText()))
            {

                dc = Double.parseDouble(appDiscount.getText().toString());
                sale.setDiscountPercentage(Double.parseDouble(discountValue.getText().toString()));
                sale.setDiscountType(discountType);
            }
            System.out.println( "DC VALUE = "+dc);
            sale.setDiscountValue(dc);

            if(paymentModeValue.toLowerCase().contains("cheque")) {
                sale.setChequeNo(chequeNo.getText().toString());

                sale.setChequeDate(chequeDate.getText().toString());
                sale.setChequeBankName(bankName.getText().toString());
            }
            else
            {
                sale.setChequeNo("null");
                sale.setChequeDate("null");
                sale.setChequeBankName("null");
            }
            customer.getSalesOfCustomer().add(sale);
            if (customer.getSalesOfCustomer().size() > beforeAdd) {

                status = true;
                bizUtils.updateStock(sale);
                bizUtils.updateOPBal(customer,sale);

            }
             writeToFile(sale,null,null);

        }

        System.out.println("SALE ORDER = " + customer.getSalesOfCustomer().size());
        System.out.println("SALE  = " + customer.getSalesOfCustomer().size());
        System.out.println("SALE  RETURN = " + customer.getSaleReturnOfCustomer().size());

        try {
            BizUtils.storeAsJSON("customerList",BizUtils.getJSON("customer",Store.getInstance().customerList));
            System.out.println("DB Updated..on local storage");


            BizUtils.syncStockProcessProductList();

        } catch (ClassNotFoundException e) {

            System.err.println("Unable to write to DB");
        }


        return status;

    }

    public void getPermission() {

        if (ActivityCompat.checkSelfPermission(DashboardActivity.this, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(WRITE_EXTERNAL_STORAGE, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(WRITE_EXTERNAL_STORAGE, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission(EXTERNAL_STORAGE_PERMISSION_CONSTANT);
        }


    }

    public static String calculateRefCode(String refcode, int count)
    {
      if(refcode.contains("SA"))
        {
            refcode = Store.getInstance().user.getSalRefCode().substring(5,10);
        }
        if(refcode.contains("SO"))
        {
            refcode = Store.getInstance().user.getSORefCode().substring(5,10);;
        }
        if(refcode.contains("SR"))
        {
            refcode = Store.getInstance().user.getSRRefCode().substring(5,10);
        }
        if(refcode.contains("RT"))
        {
            refcode = Store.getInstance().user.getRTRefCode().substring(5,10);
        }

        System.out.println("Hexa ref code = "+refcode);
        String result = new String();

        Long temp1 = Long.parseLong(refcode, 16);

        System.out.println("Decimal ref code = "+temp1);

        temp1 = temp1 + count;

       String hexStr =  Long.toHexString(temp1);


        int length = String.valueOf(hexStr).length();

        length = 5 - length;

        String partner = "";
        for(int i=0;i<5;i++)
        {
            partner = partner + "0";

            if(i==length-1)
            {
                result = partner + hexStr;
            }

        }

        System.out.println("Ref code = "+result);
    return  result;

    }

    public void print(String cSaleType) {

        ArrayList<Product> temp = Store.getInstance().addedProductList;

        System.out.println("Added product in cart ==== " + temp.size());


        try {
            if (BTPrint.btsocket == null) {
                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(DashboardActivity.this);
                alertDialog.setMessage("Please connect to the bluetooth to initiate printing");
                alertDialog.setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(DashboardActivity.this, BTDeviceList.class);

                        startActivityForResult(intent, BLUETOOTH_FLAG);

                        Toast.makeText(DashboardActivity.this, "Initializing bluetooth connection...", Toast.LENGTH_SHORT).show();




                    }
                });
                alertDialog.setNegativeButton(R.string.noReloadMsg, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Do nothing....");
                    }
                });
                alertDialog.show();

            } else {
                Store.getInstance().printerContext = DashboardActivity.this;

                Customer customer = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);


                System.out.println("Sales List " + customer.getSale().size());
                System.out.println("Sales Order List " + customer.getSaleOrder().size());
                System.out.println("Sale Type : "+cSaleType);
                System.out.println("Sale  : "+! (cSaleType.toLowerCase().contains("return") || cSaleType.toLowerCase().contains("order")));
                System.out.println("Sale Order: "+cSaleType.toLowerCase().contains("order"));
                System.out.println("Sale Return  : "+cSaleType.toLowerCase().contains("return"));


                if(cSaleType.toLowerCase().contains("return"))
                {

                    temp =   customer.getSaleReturnOfCustomer().get(customer.getSaleReturnOfCustomer().size()-1).getProducts();
                    PrinterService.print(DashboardActivity.this,customer, "Sale Return", temp, null, null, customer.getSaleReturnOfCustomer().get(customer.getSaleReturnOfCustomer().size()-1),"Customer Copy");
                    lastSavedBillType = cSaleType;



                }
                if(cSaleType.toLowerCase().contains("order"))
                {
                    temp =   customer.getSaleOrdersOfCustomer().get(customer.getSaleOrdersOfCustomer().size()-1).getProducts();
                    PrinterService.print(DashboardActivity.this,customer, "Sale Order", temp, null,customer.getSaleOrdersOfCustomer().get(customer.getSaleOrdersOfCustomer().size()-1), null,"Customer Copy");
                    lastSavedBillType = cSaleType;

                }
                if(! (cSaleType.toLowerCase().contains("return") || cSaleType.toLowerCase().contains("order")) )
                {
                    temp =   customer.getSalesOfCustomer().get(customer.getSalesOfCustomer().size()-1).getProducts();
                    PrinterService.print(DashboardActivity.this,customer, "Sale", temp,customer.getSalesOfCustomer().get(customer.getSalesOfCustomer().size()-1), null, null,"Customer Copy");
                    lastSavedBillType = cSaleType;

                }



            }
        } catch (Exception e) {
            System.out.println("Exception " + e);

        }
    }

    public void getLocationPermission() {

        if (ActivityCompat.checkSelfPermission(DashboardActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, ACCESS_FINE_LOCATION)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Need location Permission");
                builder.setMessage("This app needs location permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(ACCESS_FINE_LOCATION, false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Need Location Permission");
                builder.setMessage("This app needs location permission.");
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Location", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.cancel();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }


            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(ACCESS_FINE_LOCATION, true);
            editor.commit();


        } else {
            //You already have the permission, just go ahead.
            proceedAfterPermission(LOCATION_PERMISSION_CONSTANT);
        }


    }

    public ArrayList<Product> getCurrentProducts() {

        ArrayList<Product> products = Store.getInstance().addedProductList;

        ArrayList<Product> newList = new ArrayList<Product>();

        for (int i = 0; i < products.size(); i++) {
            Product currentProduct = products.get(i);
            Product product = new Product();
            product.setId(products.get(i).getId());
            product.setQty(products.get(i).getQty());
            product.setProductName(products.get(i).getProductName());
            product.setAvailableStock(currentProduct.getAvailableStock());
            product.setMRP(currentProduct.getMRP());
            product.setSellingRate(currentProduct.getSellingRate());
            product.setUOMId(currentProduct.getUOMId());
            product.setUOM(currentProduct.getUOM());
            product.setDiscountAmount(currentProduct.getDiscountAmount());
            product.setUOMName(currentProduct.getUOMName());
            product.setParticulars(currentProduct.getParticulars());
            product.setResale(currentProduct.isResale());
            product.setDiscountAmount(currentProduct.getDiscountAmount());
            product.setDiscountPercentage(currentProduct.getDiscountPercentage());

            newList.add(product);
        }

        return newList;
    }

    public void clearList() {
        System.out.println("Size of prod list" + Store.getInstance().addedProductList.size());
       // roundOffValue.setText(String.valueOf("0"));
        roundOffText.setText(String.valueOf("0.00"));
        for (int i = 0; i < Store.getInstance().addedProductList.size(); i++) {
            Store.getInstance().addedProductList.get(i).setQty(null);
            Store.getInstance().addedProductList.get(i).setDiscountAmount(0.0);
            Store.getInstance().addedProductList.get(i).setDiscountPercentage(0.0);
            Store.getInstance().addedProductList.get(i).setFinalPrice(0);
            Store.getInstance().addedProductList.get(i).setResale(false);
            Store.getInstance().addedProductList.get(i).setParticulars("");
            Store.getInstance().addedProductList.get(i).setSellingRate(Store.getInstance().addedProductList.get(i).getSellingRateRef());
        }

        for(int i=0;i<Store.getInstance().productList.size();i++)
        {
            Store.getInstance().productList.get(i).setQty(null);
            Store.getInstance().productList.get(i).setDiscountAmount(0.0);
            Store.getInstance().productList.get(i).setDiscountPercentage(0.0);
            Store.getInstance().productList.get(i).setFinalPrice(0);
            Store.getInstance().productList.get(i).setResale(false);
            Store.getInstance().productList.get(i).setParticulars("");
            Store.getInstance().productList.get(i).setSellingRate(Store.getInstance().productList.get(i).getSellingRateRef());

        }

        Store.getInstance().addedProductList.clear();
        Store.getInstance().addedProductAdapter.notifyDataSetChanged();
        fromCustomer.setText("0");
        chequeNo.setText("");
        chequeDate.setText("");
        bankName.setText("");
        stockGroupKey.setText("");
        customerNameKey.setText("");
        savedCurrentTransaction = true;

        Log.d("clear","list");
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }


    public void showCustomerList(String key) {


        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.setTitle("Customer List");
        ArrayList<Customer> tempCus = Store.getInstance().customerList;
        final ArrayList<Customer> tempCus1 = new ArrayList<Customer>();
        System.out.println("Size of customer list = "+tempCus.size());
        for (int i = 0; i < tempCus.size(); i++) {
            System.out.println(tempCus.get(i).getId() + "_____Cus___" + tempCus.get(i).getLedger().getLedgerName());
            if (tempCus.get(i).getLedger().getLedgerName().toLowerCase().contains(key) || String.valueOf(tempCus.get(i).getId()).toLowerCase().contains(key)) {

                tempCus1.add(tempCus.get(i));


            }
        }
        if(tempCus1.size()==0)
        {
            dialog.setContentView(R.layout.no_result);
        }
        else
        {


            dialog.setContentView(R.layout.show_customer_list_dialog);
            customerListView = (ListView) dialog.findViewById(R.id.customer_listview);




            customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    customerName.setText(String.valueOf(tempCus1.get(position).getLedgerName().toUpperCase())+"--"+tempCus1.get(position).getLedger().getId());


                    Store.getInstance().currentCustomer = tempCus1.get(position).getLedgerName();

                    Store.getInstance().currentCustomerId = tempCus1.get(position).getLedger().getId();

                    Store.getInstance().currentLedgerId = tempCus1.get(position).getLedger().getId();

                    //Store.getInstance().currentCustomerId = tempCus1.get(position).getId();

                    Store.getInstance().currentCustomerPosition = position;

                    customerName.setText(String.valueOf(tempCus1.get(position).getLedger().getLedgerName()).toUpperCase());
                    dialog.dismiss();

                    lockAllUIComponents();
                    Toast.makeText(DashboardActivity.this, "Customer Name : " + Store.getInstance().currentCustomer, Toast.LENGTH_SHORT).show();
                }
            });


            customerListView.setAdapter(new CustomerAdapter(DashboardActivity.this, tempCus1));
        }





        dialog.show();

    }

    private void lockAllUIComponents() {
        lockUIComponent(customerSearch);
        lockUIComponent(customerNameKey);
        lockUIComponent(saleType);
        lockUIComponent(isGst);
        lockUIComponent(paymentMode);
        lockUIComponent(discount);
    }

    public void showStockHome(String key) {
        final Dialog dialog = new Dialog(DashboardActivity.this);
        dialog.setTitle("Stock Home List");


        ArrayList<StockGroup> tempStock = Store.getInstance().stockGroupList;
        final ArrayList<StockGroup> tempStock1 = new ArrayList<StockGroup>();

        System.out.println("Stock Group Size = " + Store.getInstance().stockGroupList.size());
        for (int i = 0; i < tempStock.size(); i++) {
            if (tempStock.get(i).getStockGroupName().toLowerCase().contains(key) || String.valueOf(tempStock.get(i).getId()).contains(key) || String.valueOf(tempStock.get(i).getCompanyId()).contains(key)) {
                tempStock1.add(tempStock.get(i));
            }
        }

        if(tempStock1.size()==0)
        {
            dialog.setContentView(R.layout.no_result);
        }
        else
        {
            dialog.setContentView(R.layout.show_stock_home_list_dialog);
            stockHomeListView = (ListView) dialog.findViewById(R.id.stockhome_list);


            stockHomeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    stockGroupName.setText(String.valueOf(tempStock1.get(position).getStockGroupName()).toUpperCase());


                    Store.getInstance().currentStockGroup = tempStock1.get(position).getStockGroupName();
                    Store.getInstance().currentStockGroupId = tempStock1.get(position).getId();


                    System.out.println("---currentStockGroupId=="+ Store.getInstance().currentStockGroupId );
                    stockGroupName.setText(String.valueOf(Store.getInstance().currentStockGroup).toUpperCase());


                    dialog.dismiss();
                    //Toast.makeText(DashboardActivity.this, "--id--"+tempStock1.get(position).getId(), Toast.LENGTH_SHORT).show();
                }
            });


            stockHomeListView.setAdapter(new StockGroupAdapter(DashboardActivity.this, tempStock1));
        }


        dialog.show();

    }




    public void showProductListNew(String key, Long currentStockGroupId) {


        final Dialog dialog = new Dialog(DashboardActivity.this);
        final ArrayList<Product> tempProduct1 = new ArrayList<Product>();
        ArrayList<Product> tempProduct = new ArrayList<Product>();
        dialog.setTitle("Product List");


        ArrayList<Product> p = new ArrayList<>();
        if(BizUtils.getTransactionType(DashboardActivity.currentSaleType)==Store.getInstance().SALE) {
            for (int i = 0; i < Store.getInstance().productList.size(); i++) {
                if (Store.getInstance().productList.get(i).getAvailableStock() > 0) {
                    p.add(Store.getInstance().productList.get(i));
                }
            }
        }
        else
        {
            p.addAll(Store.getInstance().productList);
        }
        tempProduct.addAll(p);
        System.out.println("product size -=-----" + p.size());


        System.out.println("tempProduct Group Size = " + Store.getInstance().stockGroupList.size());

        System.out.println("Size of sale = " + Store.getInstance().addedProductForSales.size());
        for (int c = 0; c < Store.getInstance().addedProductForSales.size(); c++) {
            System.out.println("Test 1 - Sale item ======= " + Store.getInstance().addedProductForSales.get(c).getProductName() + "=====" + Store.getInstance().addedProductForSales.get(c).getQty());
        }

        for (int i = 0; i < tempProduct.size(); i++) {
            if (tempProduct.get(i).getProductName().toLowerCase().contains(key) || String.valueOf(tempProduct.get(i).getId()).contains(key) || String.valueOf(tempProduct.get(i).getItemCode()).contains(key)) {


                if (currentStockGroupId == null) {

                    Product prod = new Product();

                    prod = tempProduct.get(i);

                    tempProduct1.add(prod);
                }

                System.out.println(tempProduct.get(i).getStockGroupId() + "*************************" + tempProduct.get(i).getStockGroupId());

                if (currentStockGroupId != null) {
                    if (tempProduct.get(i).getStockGroupId().compareTo(currentStockGroupId) == 0) {

                        boolean stat = true;
                        for(int a=0;a<Store.getInstance().addedProductList.size();a++)
                        {
                            if(tempProduct.get(i).getId()== Store.getInstance().addedProductList.get(a).getId())
                            {
                                stat = false;
                            }
                        }
                        if(stat) {
                            System.out.println("Stock Group ID ---from Item--- A" + tempProduct.get(i).getStockGroupId());

                            Product prod = new Product();

                            prod = tempProduct.get(i);

                            tempProduct1.add(prod);
                        }
                    }
                } else {
                    System.out.println("Stock Group ID ---from Item--- NA" + tempProduct.get(i).getStockGroupId());
                }
            }
        }


        System.out.println("Temp product -  current stock group id=== " + currentStockGroupId);
        System.out.println("Prod Size === " + tempProduct1.size());
        if (tempProduct1.size() == 0) {
            dialog.setContentView(R.layout.no_result);
            dialog.show();
        } else {
            dialog.setContentView(R.layout.show_product_list_dialog);
            productListView = (ListView) dialog.findViewById(R.id.listview);



            final Button add = (Button) dialog.findViewById(R.id.button);
            final Button loadMore = (Button) dialog.findViewById(R.id.load_more);


            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int newitems =0;

                    if (Store.getInstance().addedProductList.size() == 0) {
                        for (int i = 0; i < tempProduct1.size(); i++) {
                            System.out.println("Product Quantity  ===== " + tempProduct1.get(i).getQty());
                            if (tempProduct1.get(i).getQty() != null && tempProduct1.get(i).getQty() != 0) {
                                newitems++;
                                Product product = new Product();
                                product = tempProduct1.get(i);
                                System.out.println("Id === " + product.getId());
                                System.out.println("Reason === " + product.getParticulars());
                                System.out.println("Selling rate === " + product.getSellingRate());
                                //Validating reason field for sale return alone
                                if(currentSaleType.toLowerCase().contains("return")) {
                                    if (TextUtils.isEmpty(product.getParticulars())) {
                                        View showProductListview = Padapter.getViewByPosition(i, productListView);
                                        TextView reason = (TextView) showProductListview.findViewById(R.id.particulars);
                                        reason.setError("Please Fill");

                                    } else {
                                        Store.getInstance().addedProductList.add(product);
                                        dialog.dismiss();

                                    }
                                }
                                else
                                {
                                    Store.getInstance().addedProductList.add(product);
                                    dialog.dismiss();
                                }
                                //--------------------------------------




                            }

                        }


                        Store.getInstance().addedProductAdapter.notifyDataSetChanged();
                    } else {
                        for (int i = 0; i < tempProduct1.size(); i++) {
                            System.out.println("ID====================" + tempProduct1.get(i).getId());
                            System.out.println("QTY====================" + tempProduct1.get(i).getQty());
                            System.out.println("Selling rate === " + tempProduct1.get(i).getSellingRate());
                            if (tempProduct1.get(i).getQty() != null && tempProduct1.get(i).getQty()!= 0) {

                                newitems++;

                                System.out.println("Size === " + Store.getInstance().addedProductList.size());
                                boolean status = true;
                                for (int j = 0; j < Store.getInstance().addedProductList.size(); j++) {

                                    System.out.println(tempProduct1.get(i).getId() + "<==========>" + Store.getInstance().addedProductList.get(j).getId());
                                    if (tempProduct1.get(i).getId() == Store.getInstance().addedProductList.get(j).getId()) {
                                        status = false;
                                    }


                                }
                                if (status) {
                                    Product product = new Product();
                                    product = tempProduct1.get(i);
                                    System.out.println("Id === " + product.getId());
                                    System.out.println("Reason === " + product.getParticulars());
                                    System.out.println("SP === " + product.getSellingRate());

                                    if(currentSaleType.toLowerCase().contains("return")) {
                                        if (TextUtils.isEmpty(product.getParticulars())) {
                                            View showProductListview = Padapter.getViewByPosition(i, productListView);
                                            TextView reason = (TextView) showProductListview.findViewById(R.id.particulars);
                                            reason.setError("Please Fill");

                                        } else {
                                            Store.getInstance().addedProductList.add(product);
                                            dialog.dismiss();

                                        }
                                    }
                                    else
                                    {
                                        Store.getInstance().addedProductList.add(product);
                                        dialog.dismiss();
                                    }
                                    Store.getInstance().addedProductAdapter.notifyDataSetChanged();
                                    System.out.println("Product does not exist..");
                                }


                                for (int j = 0; j < Store.getInstance().addedProductList.size(); j++) {

                                    System.out.println("Added Product === " + Store.getInstance().addedProductList.get(j).getId());

                                }


                            }

                        }

                    }


                    if(newitems==0)
                    {
                        Toast.makeText(DashboardActivity.this, "Choose atleast one product or press back", Toast.LENGTH_SHORT).show();
                    }


                }
            });

            final ArrayList<ArrayList<Product>> productPageList = new ArrayList<ArrayList<Product>>();

            System.out.println("product under cat size = "+tempProduct1.size());


            int offSetValue = 15;


            if (tempProduct1.size() > offSetValue) {

                loadMore.setVisibility(View.VISIBLE);
                int pageSize = tempProduct1.size() / offSetValue + tempProduct1.size() % offSetValue;
                int pageNum = tempProduct1.size()/offSetValue;
                int pageLast = tempProduct1.size() % offSetValue;
                int pageOffSet = 0;

                if(pageLast!=0)
                {
                    pageNum++;
                }


                System.out.println("Product index size----------------"+tempProduct1.size());
                for (int i = 1; i <= pageNum; i++) {
                    int pageOffSetCount =0;
                    if(i==pageNum)
                    {
                        pageOffSetCount = (i-1)*offSetValue+pageLast;

                    }
                    else
                    {
                        pageOffSetCount  = i*offSetValue;
                    }
                    System.out.println("Product index page num ----------------"+i);
                    ArrayList<Product> products = new ArrayList<Product>();
                    for(int j=(i-1)*offSetValue;j<pageOffSetCount;j++)
                    {
                        products.add(tempProduct1.get(j));

                        System.out.println("Product index ----------------"+j);
                    }
                    productPageList.add(products);

                }


                System.out.println("Product index ----------------"+productPageList.size());



                tempProduct1.clear();
                tempProduct1.addAll(productPageList.get(0));

                Padapter = new ProductAdapter(DashboardActivity.this, tempProduct1);

                productListView.setAdapter(Padapter);


                final int[] pageIndex = {1};
                final int finalPageNum = pageNum;

                final int prevPageIndex = productPageList.get(pageIndex[0]).size();


                loadMore.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if(pageIndex[0]< finalPageNum) {

                            ArrayList<Product> currentPageProducts = productPageList.get(pageIndex[0]);
                            tempProduct1.addAll(productPageList.get(pageIndex[0]));
                            pageIndex[0]++;
                            Padapter.notifyDataSetChanged();
                            loadMore.setVisibility(View.GONE);
                            add.setVisibility(View.GONE);

                            loadMore.setText(String.valueOf("Load ("+pageIndex[0]+") of "+(finalPageNum)));

                            Toast.makeText(DashboardActivity.this, "Loaded..", Toast.LENGTH_SHORT).show();

                            loadMore.setVisibility(View.VISIBLE);
                            add.setVisibility(View.VISIBLE);



                            productListView.setSelection(tempProduct1.size()-currentPageProducts.size());
                        }
                        else
                        {
                            Toast.makeText(DashboardActivity.this, "End of page..", Toast.LENGTH_SHORT).show();

                        }


                    }
                });

            }
            else
            {
                Padapter = new ProductAdapter(DashboardActivity.this, tempProduct1);

                productListView.setAdapter(Padapter);

                loadMore.setVisibility(View.GONE);

            }


            dialog.show();

        }
    }

    class LoadProductList extends  AsyncTask
    {
        Context context;
        ProductAdapter productAdapter;
        Product tempProduct1;

        public LoadProductList(Context context, ProductAdapter productAdapter,Product tempProduct1) {
            this.context = context;
            this.productAdapter = productAdapter;
            this.tempProduct1 = tempProduct1;
        }

        @Override
        protected Object doInBackground(Object[] objects) {


            System.out.println("__________adding__"+tempProduct1.getProductName());
            prodListToShow.add(tempProduct1);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            System.out.println("__________Size___"+prodListToShow.size());

            productAdapter.notifyDataSetChanged();
        }
    }




    public void setSaleType() {
        final ArrayList<String> genderList = new ArrayList<String>();
        genderList.add("Sale");
        genderList.add("Sale Order");
        genderList.add("Sale Return");


        // Drop down layout style - list view with radio button
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DashboardActivity.this, genderList);
        saleType.setAdapter(customSpinnerAdapter);

        saleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();
                currentSaleType = genderList.get(position);

                //Sale Order
                if(position==1)
                {
                    paymentMode.setEnabled(false); //payment mode Disabled
                    paymentMode.setVisibility(View.GONE);
                    paymentLabel.setVisibility(View.GONE);
                    discountSpinnerLabel.setVisibility(View.VISIBLE);
                    discount.setVisibility(View.VISIBLE);


                    isGst.setEnabled(true); //GST mode Disabled
                    Toast.makeText(DashboardActivity.this, "Payment option disabled..", Toast.LENGTH_SHORT).show();
                    fromCustomer.setEnabled(false); //From Customer mode Disabled
                    tenderAmount.setEnabled(false); //Tender/Balance Disabled
                    discount.setEnabled(true);//Discount : Enabled
                    save.setEnabled(true);
                    save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    preview.setEnabled(true);
                    Log.d("Preview","Enaabled 7");
                    preview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                    IntializeDashboardVariables();
                    hideCashLayout();
                    hideChequeLayout();
                    setPaymentMode();



                }
                //Sale Return
                else  if(position==2)
                {
                    paymentMode.setEnabled(false); //payment mode Disabled
                    paymentMode.setVisibility(View.GONE);
                    paymentLabel.setVisibility(View.GONE);
                    discountSpinnerLabel.setVisibility(View.GONE);
                    discount.setVisibility(View.GONE);



                    Toast.makeText(DashboardActivity.this, "Payment option disabled..", Toast.LENGTH_SHORT).show();
                    fromCustomer.setEnabled(false);//From Customer mode Disabled
                    tenderAmount.setEnabled(false);//Balance : Disabled
                    discount.setEnabled(false);
                    save.setEnabled(true);
                    isGst.setEnabled(true); //GST mode Enabled
                    save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    preview.setEnabled(true);
                    Log.d("Preview","Enaabled 7-2");
                    preview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));


                    IntializeDashboardVariables();
                    hideCashLayout();
                    hideChequeLayout();
                    noDiscount();

                }
                else
                {
                    //Sale
                    Log.d("Preview","Disabled 8");

                    paymentMode.setVisibility(View.VISIBLE);
                    paymentLabel.setVisibility(View.VISIBLE);
                    discountSpinnerLabel.setVisibility(View.VISIBLE);
                    discount.setVisibility(View.VISIBLE);


                    save.setEnabled(false);
                    save.setBackgroundColor(getResources().getColor(R.color.grey));
                    preview.setEnabled(false);
                    preview.setBackgroundColor(getResources().getColor(R.color.grey));
                    paymentMode.setEnabled(true);
                    isGst.setEnabled(true);
                    fromCustomer.setEnabled(true);
                    Toast.makeText(DashboardActivity.this, "Payment option enabled..", Toast.LENGTH_SHORT).show();
                    tenderAmount.setEnabled(true);


                    discount.setEnabled(true);

                    IntializeDashboardVariables();

                    showCashLayout();

                    setPaymentMode();



                }


            }



            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                currentSaleType = genderList.get(0);


            }


        });


    }

    private void IntializeDashboardVariables() {
        isGst.setSelection(0);
        isGstValue = gstTypeList.get(0);
        paymentMode.setSelection(0);
        paymentModeValue = Store.getInstance().paymentModeTypeList.get(0);
        discount.setSelection(0);
        discountType = discountTypeList.get(0);
    }

    public void setIsGst() {

         gstTypeList = new ArrayList<String>();
        gstTypeList.add("GST");
        gstTypeList.add("No GST");

        // Drop down layout style - list view with radio button

        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DashboardActivity.this, gstTypeList);
        isGst.setAdapter(customSpinnerAdapter);

        isGst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              //  Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();
                isGstValue = gstTypeList.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                isGstValue = gstTypeList.get(0);
            }


        });


    }
    public void setDiscountType() {

        discountTypeList = new ArrayList<String>();
        discountTypeList.add("No Discount");
        discountTypeList.add("Grand Total & Individual Products");



        // Drop down layout style - list view with radio button
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DashboardActivity.this, discountTypeList);
        discount.setAdapter(customSpinnerAdapter);

        discount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();
                discountType = discountTypeList.get(position);

                if(discountType.toLowerCase().contains("no"))
                {
                   noDiscount();


                }
                else
                {
                    discountValue.setEnabled(true);
                    if(discountType.toLowerCase().contains("total"))
                    {
                        grandTotalDiscount();
                        induvidualDiscount();


                    }
                }

            }




            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                discountType= discountTypeList.get(0);

                discountValue.setEnabled(false);
            }


        });


    }
    public void setGrandDiscountType() {

        grandDiscountTypeList = new ArrayList<String>();
        grandDiscountTypeList .add(Store.getInstance().discountTypePercentage);
        grandDiscountTypeList .add(Store.getInstance().discountTypeRM);



        // Drop down layout style - list view with radio button


        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DashboardActivity.this, grandDiscountTypeList );
        grandDiscountTypeSpinner.setAdapter(customSpinnerAdapter);


        grandDiscountTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //  Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();
                grandDiscountType = grandDiscountTypeList.get(position);
                discountValue.setText("0");
                discountValue.requestFocus();
                fromCustomer.setText("0");




            }




            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                grandDiscountType = grandDiscountTypeList.get(0);
            }


        });





    }

    public void setPaymentMode() {





        // Drop down layout style - list view with radio button
        CustomSpinnerAdapter customSpinnerAdapter = new CustomSpinnerAdapter(DashboardActivity.this, Store.getInstance().paymentModeTypeList);
        paymentMode.setAdapter(customSpinnerAdapter);

        paymentMode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(getApplicationContext(), genderList.get(position), Toast.LENGTH_SHORT).show();
                paymentModeValue = Store.getInstance().paymentModeTypeList.get(position);

                if(paymentModeValue.contains("PNT") || paymentModeValue.toLowerCase().contains("cheque") )
                {
                    preview.setEnabled(true);
                    Log.d("Preview","Enabled 9");
                    save.setEnabled(true);
                    save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    preview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                    fromCustomer.setEnabled(false);
                    tenderAmount.setEnabled(false);

                    fromCustomer.setVisibility(View.GONE);
                    tenderAmount.setVisibility(View.GONE);
                    cfc.setVisibility(View.GONE);
                    ta.setVisibility(View.GONE);
                    note.setVisibility(View.GONE);



                    if(paymentModeValue.toLowerCase().contains("cheque"))
                    {
                        System.out.println("showing chq no ...");
                        cn.setVisibility(View.VISIBLE);
                        chequeNo.setVisibility(View.VISIBLE);
                        bnl.setVisibility(View.VISIBLE);
                        cdl.setVisibility(View.VISIBLE);
                        bankName.setVisibility(View.VISIBLE);
                        chequeDate.setVisibility(View.VISIBLE);
                        dateChooser.setVisibility(View.VISIBLE);


                        Resources r = DashboardActivity.this.getResources();
                        int px = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                80,
                                r.getDisplayMetrics()
                        );


                    }
                    else
                    {
                        System.out.println("Hiding chq no ...");
                        cn.setVisibility(View.GONE);
                        chequeNo.setVisibility(View.GONE);
                        bnl.setVisibility(View.GONE);
                        cdl.setVisibility(View.GONE);
                        bankName.setVisibility(View.GONE);
                        chequeDate.setVisibility(View.GONE);
                        dateChooser.setVisibility(View.GONE);
                  }
                }
                else
                {

                    if(! (currentSaleType.toLowerCase().contains("order") || currentSaleType.toLowerCase().contains("return") )) {
                        preview.setEnabled(false);
                        Log.d("Preview", "Disabled 10");
                        save.setEnabled(false);
                        save.setBackgroundColor(getResources().getColor(R.color.grey));
                        preview.setBackgroundColor(getResources().getColor(R.color.grey));


                        System.out.println("cash mode hiding chq no ...");
                        fromCustomer.setEnabled(true);
                        tenderAmount.setEnabled(true);
                        fromCustomer.setVisibility(View.VISIBLE);
                        tenderAmount.setVisibility(View.VISIBLE);
                        cfc.setVisibility(View.VISIBLE);
                        ta.setVisibility(View.VISIBLE);
                        note.setVisibility(View.VISIBLE);
                        cn.setVisibility(View.GONE);

                        chequeNo.setVisibility(View.GONE);
                        bnl.setVisibility(View.GONE);
                        cdl.setVisibility(View.GONE);
                        bankName.setVisibility(View.GONE);
                        chequeDate.setVisibility(View.GONE);
                        dateChooser.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Log.d("Sale Type","Noting selected");
                paymentModeValue = Store.getInstance().paymentModeTypeList.get(0);

                if(paymentModeValue.contains("PNT") || paymentModeValue.toLowerCase().contains("cheque") )
                {
                    fromCustomer.setEnabled(false);
                    tenderAmount.setEnabled(false);

                    fromCustomer.setVisibility(View.GONE);
                    tenderAmount.setVisibility(View.GONE);
                    cfc.setVisibility(View.GONE);
                    ta.setVisibility(View.GONE);
                    if(paymentModeValue.toLowerCase().contains("cheque"))
                    {
                        cn.setVisibility(View.VISIBLE);
                        chequeNo.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        cn.setVisibility(View.GONE);
                        chequeNo.setVisibility(View.GONE);

                    }


                }
                else
                {
                    fromCustomer.setEnabled(true);
                    tenderAmount.setEnabled(true);
                    fromCustomer.setVisibility(View.VISIBLE);
                    tenderAmount.setVisibility(View.VISIBLE);
                    cfc.setVisibility(View.VISIBLE);
                    ta.setVisibility(View.VISIBLE);
                }
            }


        });


    }

    class CustomerDetails extends AsyncTask {

        Context context;
        String jsonStr = null;
        String url;
        HashMap<String, String> params;

        public CustomerDetails(Context context) {
            this.context = context;
            this.url = "customer/tolist";
            params = new HashMap<String, String>();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.params.put("CompanyId", String.valueOf(Store.getInstance().dealerId));


        }

        @Override
        protected Object doInBackground(Object[] params) {
            HttpHandler httpHandler = new HttpHandler();
            SignalRService.customerList(DashboardActivity.this);
            jsonStr = httpHandler.makeServiceCall(this.url, this.params);
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("JSON :" + jsonStr);
            if (jsonStr != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Collection<Customer>>() {
                }.getType();

                Collection<Customer> customerCollection = gson.fromJson(jsonStr, collectionType);


                Store.getInstance().customerList = (ArrayList<Customer>) customerCollection;



                for(int i=0;i<Store.getInstance().customerList.size();i++)
                {
                    System.out.println("=============>"+Store.getInstance().customerList.get(i).getLedgerName());

                    System.out.println  ("=============>"+Store.getInstance().customerList.get(i).getPersonIncharge());

                }


                System.out.println  ("<====Sorted===================>");

                Collections.sort(Store.getInstance().customerList, Customer.COMPARE_BY_LEDGER_NAME);

                for(int i=0;i<Store.getInstance().customerList.size();i++)
                {
                    System.out.println("=============>"+Store.getInstance().customerList.get(i).getLedgerName());

                    System.out.println("=============>"+Store.getInstance().customerList.get(i).getPersonIncharge());

                }
                System.out.println  ("<=======================>");

                new StockDetails(context).execute();


            } else {
                Toast.makeText(context, "Unable to retrieve customer list...", Toast.LENGTH_SHORT).show();
            }
        }


    }

    class StockDetails extends AsyncTask {

        Context context;
        String jsonStr = null;
        String url;
        HashMap<String, String> params;

        public StockDetails(Context context) {
            this.context = context;
            this.url = "stockgroup/tolist";
            params = new HashMap<String, String>();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            this.params.put("CompanyId", String.valueOf(Store.getInstance().companyID));


        }

        @Override
        protected Object doInBackground(Object[] params) {
            HttpHandler httpHandler = new HttpHandler();
            jsonStr = httpHandler.makeServiceCall(this.url, this.params);
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("JSON :" + jsonStr);
            if (jsonStr != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Collection<StockGroup>>() {
                }.getType();

                Collection<StockGroup> customerCollection = gson.fromJson(jsonStr, collectionType);


                Store.getInstance().stockGroupList = (ArrayList<StockGroup>) customerCollection;



                new ProductDetails(context).execute();


            } else {
                Toast.makeText(context, "Unable to retrieve stock list...", Toast.LENGTH_SHORT).show();
            }
        }


    }

    class ProductDetails extends AsyncTask {

        Context context;
        String jsonStr = null;
        String url;
        HashMap<String, String> params;

        public ProductDetails(Context context) {
            this.context = context;
            this.url = "Product/tolist";
            params = new HashMap<String, String>();
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            this.params.put("DealerId", String.valueOf(Store.getInstance().dealerId));


        }

        @Override
        protected Object doInBackground(Object[] params) {
            HttpHandler httpHandler = new HttpHandler();
            jsonStr = httpHandler.makeServiceCall(this.url, this.params);
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("JSON :" + jsonStr);
            if (jsonStr != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<Collection<Product>>() {
                }.getType();

                Collection<Product> productCollection = gson.fromJson(jsonStr, collectionType);


                Store.getInstance().productList = (ArrayList<Product>) productCollection;




            } else {
                Toast.makeText(context, "Unable to retrieve product list...", Toast.LENGTH_SHORT).show();
            }
        }


    }

/*    class Save extends AsyncTask {

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
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            productModels = new ArrayList<ProductModel>();


            Long gst = Long.valueOf(0);
            double totalAmount = Long.valueOf(0);
            Long extraAmount = Long.valueOf(0);


            for (int i = 0; i < input.size(); i++) {

                if (input.get(i).isStatus()) {
                    System.out.println("No sync required for " + url);
                } else {
                    System.out.println("adding sync required for " + url);


                    ProductModel productModel = new ProductModel();

                    System.out.println("Product  id = " + input.get(i).getId());
                    productModel.setProductId(input.get(i).getId());
                    productModel.setQty(input.get(i).getQty());
                    productModel.setRate((long) input.get(i).getMRP());


                    double amount = (input.get(i).getMRP() * input.get(i).getQty()) -input.get(i).getDiscountAmount()  ;

                    productModel.setAmount(amount);

                    productModel.setUOMId(input.get(i).getUOMId());
                    System.out.println("REASON="+input.get(i).getParticulars());
                    System.out.println("Resale="+input.get(i).isResale());
                    productModel.setReason(input.get(i).getParticulars());
                    productModel.setResale(input.get(i).isResale());
                    productModel.setDiscountAmount(input.get(i).getDiscountAmount());
                    productModels.add(productModel);
                }


            }


            Gson gson = new Gson();


            String json = gson.toJson(productModels);
            System.out.println("JSON : " + json);


            double dc =0;
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
                dc = saleOrder.getDiscountValue();


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
                dc = saleReturn.getDiscountValue();
            } else {

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
                dc = sale.getDiscountValue();
            }

            this.params.put("IsGST", String.valueOf(IsGst));
            this.params.put("LedgerId", String.valueOf(this.cusId));
            this.params.put("DiscountAmount", String.valueOf(dc));
            this.params.put("PayMode", String.valueOf(payMode));
            this.params.put(this.paramKey, json);
        }

        @Override
        protected Object doInBackground(Object[] params) {

            if (productModels.size() != 0) {
                HttpHandler httpHandler = new HttpHandler();
                jsonStr = httpHandler.makeServiceCall(this.url, this.params);
            } else {
                System.out.println("No sync required.....");
            }
            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            System.out.println("JSON RESPONSE" + jsonStr);

            if (jsonStr != null) {


                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<ProductSaveResponse>() {
                }.getType();

                ProductSaveResponse response = gson.fromJson(jsonStr, collectionType);

                Store.getInstance().productSaveRes = response;


                if (response.isHasError()) {
                    syncStatus = false;
                    Toast.makeText(context, "Not saved", Toast.LENGTH_SHORT).show();

                } else {


                    for (int i = 0; i < input.size(); i++) {

                        input.get(i).setStatus(true);


                    }


                    System.out.println("URL saved to " + this.url);
                    System.out.println("payMode ="+ payMode);
                    if (url.toLowerCase().contains("order")) {
                        Toast.makeText(context, "Sale order saved", Toast.LENGTH_SHORT).show();


                        if(payMode.contains("PNT"))
                        {


                            double x =0;
                            x = customer.getLedger().getOPBal() + grandTotal;
                            customer.setOPBal(x);

                            System.out.println("OP ="+ x);
                        }
                        syncStatus = true;

                    } else if (url.toLowerCase().contains("return")) {
                        Toast.makeText(context, "Sale return saved", Toast.LENGTH_SHORT).show();



                        syncStatus = true;
                    } else {
                        Toast.makeText(context, "sale  saved", Toast.LENGTH_SHORT).show();
                        System.out.println("payMode ="+ payMode);
                        if(payMode.contains("PNT"))
                        {


                            double x =0;

                            System.out.println("GT ==="+grandTotal);

                            x = customer.getLedger().getOPBal() +grandTotal;

                            System.out.println("OP ="+ x);
                            customer.setOPBal(x);
                        }
                        syncStatus = true;
                    }
                }


            }
        }


    }*/

/*    public class SaveCustomer extends AsyncTask {
        Context context;
        String jsonStr;
        String url;
        HashMap<String, String> params;
        Customer customers = new Customer();

        public SaveCustomer(Context context, Customer customers) {
            this.context = context;
            this.jsonStr = null;
            this.url = "customer/save";
            this.params = new HashMap<String, String>();
            this.customers = customers;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            this.params.put("DealerId", String.valueOf(Store.getInstance().dealerId));
            this.params.put("LedgerName", customers.getLedgerName());
            this.params.put("PersonIncharge", customers.getPersonIncharge());
            this.params.put("AddressLine1", customers.getAddressLine1());
            this.params.put("AddressLine2", customers.getAddressLine2());
            this.params.put("CityName", customers.getCityName());
            this.params.put("MobileNo", customers.getMobileNo());
            this.params.put("GSTNo", customers.getGSTNo());
        }

        @Override
        protected Object doInBackground(Object[] params) {

            HttpHandler httpHandler = new HttpHandler();
            jsonStr = httpHandler.makeServiceCall(this.url, this.params);

            return true;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("JSON :" + jsonStr);
            if (jsonStr != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();
                Type collectionType = new TypeToken<AddCustomerResponse>() {
                }.getType();

                AddCustomerResponse customerRCollection = gson.fromJson(jsonStr, collectionType);
                Store.getInstance().addCustomerResponse = (AddCustomerResponse) customerRCollection;
                if (Store.getInstance().addCustomerResponse.isHasError()) {
                    Toast.makeText(context, "Customer not Saved", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(context, "Customer Saved", Toast.LENGTH_SHORT).show();
                    this.customers.setId(Store.getInstance().addCustomerResponse.getId());
                    int size = Store.getInstance().customerList.size();
                    System.out.println("Received ID " + Store.getInstance().addCustomerResponse.getId());

                    Store.getInstance().customerList.get(size - 1).setId(Store.getInstance().addCustomerResponse.getId());
                    for (int i = 0; i < Store.getInstance().customerList.size(); i++) {
                        System.out.println("cus id : " + Store.getInstance().customerList.get(i).getId());
                    }

                    if (newcustomer = true) {
                        System.out.println("Sale size " + customers.getSale().size());
                        System.out.println("Sale order size " + customers.getSaleOrder().size());
                        if (customers.getSale().size() > 0) {
                            newcustomer = false;

                            for (int y = 0; y < customers.getSalesOfCustomer().size(); y++) {
                                new Save(DashboardActivity.this, "sale/save", "SODetails", customers.getSalesOfCustomer().get(y).getProducts(), customers.getId(), customers.getSalesOfCustomer().get(y), null, null,customers).execute();
                            }


                        }


                        if (customers.getSaleOrder().size() > 0) {
                            newcustomer = false;

                            for (int y = 0; y < customers.getSaleOrdersOfCustomer().size(); y++) {
                                new Save(DashboardActivity.this, "SaleOrder/save", "SaleOrderDetails", customers.getSaleOrdersOfCustomer().get(y).getProducts(), customers.getId(), null, customers.getSaleOrdersOfCustomer().get(y), null, customers).execute();

                            }


                        }
                        if (customers.getSaleReturn().size() > 0) {

                            for (int y = 0; y < customers.getSaleReturnOfCustomer().size(); y++) {
                                newcustomer = false;
                                new Save(DashboardActivity.this, "SalesReturn/save", "SaleReturnDetails", customers.getSaleReturnOfCustomer().get(y).getProducts(), customers.getId(), null, null, customers.getSaleReturnOfCustomer().get(y), customers).execute();

                            }

                        }
                        if (customers.getReceipts().size() > 0) {
                            newcustomer = false;
                            System.out.println("Called save receipt");
                            for (int z = 0; z < customers.getReceipts().size(); z++) {


                                if( customers.getReceipts().get(z).isSynced())
                                {
                                    System.out.println("Receipt already synced");
                                }
                                else
                                {
                                    new SaveReceipt(context, "Sale/Receipt_Save", customers.getReceipts().get(z), customers.getId()).execute();

                                }

                            }
                            System.out.println("Closing save receipt");

                        }
                        if (customers.getPayments().size() > 0) {
                            newcustomer = false;
                            System.out.println("Called save payments");
                            for (int z = 0; z < customers.getPayments().size(); z++) {
                                if( customers.getPayments().get(z).isSynced())
                                {
                                    System.out.println("Payment already synced");
                                }
                                else
                                {
                                    new SaveReceipt(context, "Sale/Payment_Save", customers.getPayments().get(z), customers.getId()).execute();
                                }
                            }
                            System.out.println("Closing save payments");

                        }
                    }


                }
                syncStatus = true;
            } else {

                syncStatus = false;
                Toast.makeText(context, "Error in saving customer", Toast.LENGTH_SHORT).show();
            }

        }


    }*/


/*    class saveOffline extends AsyncTask {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Object doInBackground(Object[] params) {
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
        }


    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Result code === " + resultCode);
        System.out.println("requestCode === " + requestCode);
        System.out.println("data === " + data);
        if (requestCode == REQUEST_PERMISSION_SETTING) {
            if (ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }
        } else {
            try {


                BTPrint.btsocket = BTDeviceList.getSocket();
                if (BTPrint.btsocket != null) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    OutputStream opstream = null;
                    try {
                        opstream = BTPrint.btsocket.getOutputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    BTPrint.btoutputstream = opstream;
                }

            } catch (Exception e) {

            }
        }


    }

    public void print(Context context,Customer customer, String type, ArrayList<Product> products, Sale sale, SaleOrder saleOrder, SaleReturn saleReturn, String copyName) {

        Store.getInstance().printerContext = context;

        SharedPreferences prefs = context.getSharedPreferences(Store.getInstance().MyPREFERENCES, MODE_PRIVATE);
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine(prefs.getString(context.getString(R.string.companyName), "Aboorvass"));
        BTPrint.SetAlign(Paint.Align.CENTER);


        System.out.println("company address line 1 ==============================" + prefs.getString(context.getString(R.string.companyAddressLine1), "0"));
        System.out.println("company address line 2 ==============================" + prefs.getString(context.getString(R.string.companyAddressLine2), "0"));
        System.out.println("company gst ==============================" + prefs.getString(context.getString(R.string.gstNo), "0"));
        System.out.println("company mail ==============================" + prefs.getString(context.getString(R.string.email), "0"));
        System.out.println("company postalcode ==============================" + prefs.getString(context.getString(R.string.postal_code), "0"));

        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine(prefs.getString(context.getString(R.string.companyAddressLine1), "0") + "," + prefs.getString(context.getString(R.string.companyAddressLine2), "0") + "," + prefs.getString(context.getString(R.string.postal_code), "+1234556789"));

        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("E-Mail: " + prefs.getString(context.getString(R.string.email), "+1234556789"));
        BTPrint.PrintTextLine("GST No: " + prefs.getString(context.getString(R.string.gstNo), "+1234556789"));
        BTPrint.PrintTextLine("Ph No: " + prefs.getString(context.getString(R.string.phoneNumber), "+1234556789"));
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("***Bill Details***");
        BizUtils bizUtils = new BizUtils();

        String refNo = "";
        String paymentModeValue = "";

        if(sale!=null) {
            refNo = sale.getRefCode();
            paymentModeValue = sale.getPaymentMode();



        }
        if(saleOrder !=null)
        {
            refNo = saleOrder.getRefCode();

            paymentModeValue = saleOrder.getPaymentMode();
        }
        if(saleReturn !=null)
        {
            refNo = saleReturn.getRefCode();
            paymentModeValue = saleReturn.getPaymentMode();

        }




        BTPrint.PrintTextLine("Bill Ref No :" + String.valueOf(refNo));
        BTPrint.PrintTextLine("Bill Date :" + bizUtils.getCurrentTime());
        BTPrint.PrintTextLine("------------------------------");
        Customer customer1 = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);
        if (customer1.getId() == null) {
            BTPrint.PrintTextLine("Customer ID :" + "Unregistered");
        } else {
            BTPrint.PrintTextLine("Customer ID :" + customer1.getId());
        }
        BTPrint.PrintTextLine("Customer Name :" + customer1.getLedger().getLedgerName());
        BTPrint.PrintTextLine("Person In Charge :" + customer1.getLedger().getPersonIncharge());
        BTPrint.PrintTextLine("GST No :" + customer1.getLedger().getGSTNo());
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Sale/Order/Return:" + type);

        if(getTransactionType(type)==Store.getInstance().SALE) {
            BTPrint.PrintTextLine("Payment Mode :" + paymentModeValue);
            if(sale.getPaymentMode().toLowerCase().contains("cheque"))
            {
                BTPrint.PrintTextLine("Cheque No :" + sale.getChequeNo());
            }
        }


        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***ITEM DETAILS***");
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.LEFT);
      //  BTPrint.PrintTextLine("NAME     QTY    PRICE   AMOUNT ");

        customer = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);





        System.out.println("Sale list" + customer.getSale().size());
        double mainSubTotal = 0;
        double mainGst = 0;
        double mainGrantTotal = 0;

        for (int i = 0; i < products.size(); i++) {
            Product item = products.get(i);
            String in;
            String iq = "";
            String ip = "";
            String ir = "";
            String id = "";
            String itemnameSpace = "";
            String itemquantitySpace = "";
            String itempriceSpace = "";
            String itemrateSpace = "";
            String itemDisSpace = "";
            int spaceLength = 0;
            int itemQspaceL = 0;
            int itemPspaceL = 0;
            int itemRspaceL = 0;
            int itemDspaceL = 0;


            if (item.getProductName().length() >= 10) {
                in = item.getProductName().substring(0, 9);
                itemnameSpace = " ";
            } else {
                int total = item.getProductName().length();
                spaceLength = 10 - total;

                for (int x = 0; x < spaceLength; x++) {
                    itemnameSpace = itemnameSpace + " ";
                }

                in = item.getProductName();

            }

            if (String.valueOf(item.getQty()).length() >= 6) {
                iq = String.valueOf(item.getQty()).substring(0, 4);
                iq = iq + "..";
            } else {
                int total = String.valueOf(item.getQty()).length();
                itemQspaceL = 6 - total;

                for (int x = 0; x < itemQspaceL -1 ; x++) {
                    itemquantitySpace = itemquantitySpace + " ";
                }

                iq = String.valueOf(item.getQty());
            }
            String itemP = String.valueOf(String.format("%.2f",item.getMRP()));

            if (itemP.length() >= 8) {
                in = itemP.substring(0, 7);
                itemrateSpace = " ";

            } else {
                int total = itemP.length();
                itemPspaceL = 8 - total;

                for (int x = 0; x < itemPspaceL-1; x++) {
                    itempriceSpace = itempriceSpace + " ";
                }


                ip = itemP;

            }

            String itemD = String.valueOf(String.format("%.2f",item.getDiscountAmount()));


            if (itemD.length() >= 8) {
                in = itemD.substring(0, 7);
                itemDisSpace = " ";

            } else {
                int total = itemD.length();
                itemDspaceL = 8 - total;

                for (int x = 0; x < itemDspaceL-1; x++) {
                    itemDisSpace = itemDisSpace + " ";
                }


                id = itemD;

                if(item.getDiscountAmount()==0)
                {
                    id = "    ";
                }

            }


            double subTotalx = 0;
            double gstx = 0;
            if (String.valueOf(item.getQty() * item.getMRP()).length() >= 6) {
              //  ir = String.valueOf( (item.getQty() * item.getMRP()) - item.getDiscountAmount()).substring(0, 4);
                //ir = ir + "..";
                ir = String.valueOf((item.getQty() * item.getMRP()) - item.getDiscountAmount());

                subTotalx = item.getQty() * item.getMRP();
            } else {
                int total = String.valueOf(item.getQty() * item.getMRP()).length();
                itemRspaceL = 6 - total;

                for (int x = 0; x < itemRspaceL; x++) {
                    itemrateSpace = itemrateSpace + " ";
                }

                ir = String.valueOf(String.format("%.2f",item.getQty() * item.getMRP() - item.getDiscountAmount() ));
                subTotalx = subTotalx + item.getQty() * item.getMRP() -item.getDiscountAmount() ;
            }


            mainSubTotal = mainSubTotal + subTotalx;
            gstx = subTotalx * (0.06);
            mainGst = mainGst + gstx;
            mainGrantTotal = mainSubTotal + mainGst;







            mainGst = bizRound(mainGst, 2);

           // BTPrint.PrintTextLine(in + itemnameSpace + iq + itemquantitySpace + ip + itempriceSpace + ir);

            BTPrint.PrintTextLine(i+1+"."+item.getProductName());


            int l = itemquantitySpace.length();
            int ld = l/2;

            String q = itemquantitySpace.substring(0,ld)+ "x" + itemquantitySpace.substring(ld,l);
            String dis ="";
            if(item.getDiscountAmount()!=0)
            {

                int d = itemDisSpace.length();
                int dd = d / 2;

                 dis = itemDisSpace.substring(0, dd) + " -" + itemDisSpace.substring(dd, d);
            }
            else
            {
                int d = itemDisSpace.length();
                int dd = d / 2;

                 dis = itemDisSpace.substring(0, dd) + " " + itemDisSpace.substring(dd, d);
            }

            int p = itemDisSpace.length();
            int pd = p/2;

            String pr = itemDisSpace.substring(0,pd)+ " =" + itemDisSpace.substring(pd,p);




            if(!TextUtils.isEmpty(id.trim())) {
                id = String.valueOf(String.format("%.2f", Double.parseDouble(id)));
            }
            ir = String.valueOf(String.format("%.2f", Double.parseDouble(ir)));

            String print_line =   " "+iq + q + ip + dis + id +pr + ir;
            System.out.println("Print Line = "+print_line);

            Formatter fmt = new Formatter();
            fmt.format("|%6.2f|", Double.parseDouble(ir));

            String oldPrintStmt  =  " "+iq + q + ip + dis + id +pr + ir;

            String qStr = String.format("%4d",  Integer.parseInt(iq));
            qStr = qStr+" ";
            String pStr = String.format("%6.2f",Double.parseDouble(ip));
            pStr = pStr+" ";
            String dStr = "";
            if(TextUtils.isEmpty(id.trim())) {
                 dStr = String.format("%7s", id);
            }else
            {
                 dStr = "-"+String.format("%6s", id);
            }
            dStr = dStr+" ";
            String rStr = "="+String.format("%8.2f",Double.parseDouble(ir));

            String newPrintStmnt = qStr+"X"+pStr+dStr+rStr;

            BTPrint.PrintTextLine( newPrintStmnt);

        }



        String mgt = "0.00";

        String ra="0.00",ba="0.00";
        Double disV = 0.00;
        double roundOffValue = 0.00;
        if(sale!=null) {


             ra = String.valueOf(String.format("%.2f", sale.getReceivedAmount()));
             ba = String.valueOf(String.format("%.2f", sale.getBalance()));
             mgt = String.valueOf(String.format("%.2f", sale.getGrandTotal()));
            mainGst = sale.getGst();
             disV = sale.getDiscountValue();
            roundOffValue =  sale.getRoundOffValue();

        }
         if(saleOrder !=null)
        {
            ra = String.valueOf(String.format("%.2f", saleOrder.getReceivedAmount()));
            ba = String.valueOf(String.format("%.2f", saleOrder.getBalance()));

            disV = saleOrder.getDiscountValue();
            mgt = String.valueOf(String.format("%.2f", saleOrder.getGrandTotal()));
            mainGst = saleOrder.getGst();
            roundOffValue =  saleOrder.getRoundOffValue();
        }
        if(saleReturn !=null)
        {
            ra = String.valueOf(String.format("%.2f", saleReturn.getReceivedAmount()));
            ba = String.valueOf(String.format("%.2f", saleReturn.getBalance()));
            disV = saleReturn.getDiscountValue();
            mgt = String.valueOf(String.format("%.2f", saleReturn.getGrandTotal()));
            mainGst = saleReturn.getGst();
            roundOffValue =  saleReturn.getRoundOffValue();
        }


        String gstSpace = "";
        String discountSpace="";
        String roundOffSpace="";
        System.out.println("on test");
        String subTotal = String.valueOf(String.format("%.2f",mainSubTotal));
        int subTotalLength = subTotal.length();

        String gst = String.valueOf(String.format("%.2f",mainGst));
        int gstLength = gst.length();

        String disString =String.valueOf(String.format("%.2f",disV));
        int disLength = disString.length();

        String roundOffStr = String.valueOf(String.format("%.2f",roundOffValue));
        int roundOFFlenght = roundOffStr.length();





        int c = subTotalLength - gstLength;
        int dl = subTotalLength - disLength;
        int rl = subTotalLength - roundOFFlenght;

        for (int f = 0; f < c; f++) {
            gstSpace = gstSpace + " ";
        }
        for (int f = 0; f < dl; f++) {
            discountSpace = discountSpace+ " ";
        }
        for (int f = 0; f < rl; f++) {
            roundOffSpace = roundOffSpace+ " ";
        }

        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.RIGHT);
        BTPrint.PrintTextLine("Sub total RM " + String.format("%.2f",mainSubTotal));
        BTPrint.PrintTextLine("GST RM " + gstSpace + String.format("%.2f",mainGst));






        String discountPer = "0.00";
        String disType ="";
        if(sale!=null)
        {
            mainGrantTotal = sale.getGrandTotal();
            ba = String.valueOf(sale.getBalance());
            discountPer = String.format("%.2f",sale.getDiscountPercentage());
            disType = sale.getDiscountType();

        }
        if(saleOrder!=null)
        {
            mainGrantTotal = saleOrder.getGrandTotal();
            ba = String.valueOf(saleOrder.getBalance());
            discountPer = String.format("%.2f",saleOrder.getDiscountPercentage());
            disType = saleOrder.getDiscountType();
        }
        if(saleReturn!=null)
        {
            mainGrantTotal = saleReturn.getGrandTotal();
            ba = String.valueOf(saleReturn.getBalance());
        }
        if(BizUtils.getDiscountType(disType)==Store.getInstance().DISCOUNT_FOR_GRABD_TOTAL)
        {

                BTPrint.PrintTextLine("Discount("+discountPer+"%) RM " + discountSpace + String.format("%.2f",disV));
        }
        ba = String.format("%.2f",Double.parseDouble(ba));
        BTPrint.PrintTextLine("Round Off RM " + roundOffSpace + String.format("%.2f",roundOffValue));
        int mgtL =  String.valueOf(String.format("%.2f",mainGrantTotal)).length();
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
        else
        {
            x = mgtL - raL;
            y = mgtL - baL;
            for (int i = 0; i < x; i++) {
                raSpace = raSpace + " ";

            }
            for (int i = 0; i < y; i++) {
                baSpace = baSpace + " ";

            }
        }

        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Grand total" + " RM " + String.format("%7.2f",mainGrantTotal));

        if(getTransactionType(type)==Store.getInstance().SALE) {
            if(sale.getPaymentMode().toLowerCase().contains("cash")) {
                BTPrint.PrintTextLine("Received amount RM " + String.format("%7.2f", Double.parseDouble(ra)));
                BTPrint.PrintTextLine("Balance amount RM " + String.format("%7.2f", Double.parseDouble(ba)));
            }
        }
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("*****THANK YOU*****");
        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Dealer Name:" + Store.getInstance().dealerName);
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("Powered By Denariu Soft SDN BHD");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***"+copyName+"***");
        BTPrint.printLineFeed();
        savedCurrentTransaction = false;


       /* if(currentSaleType.toLowerCase().contains("order"))
        {

            customer.getSaleOrder().removeAll(products);
        }
        else
        if(currentSaleType.toLowerCase().contains("return"))
        {
            customer.getSaleReturn().removeAll(products);
        }
        else
        {
            customer.getSale().removeAll(products);

        }
        System.out.println("Product Size = "+products.size());

*/
    }


    public static double bizRound(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static String fixedLengthString(double string, int length, String type) {
        return String.format("%1$"+length+type, string);
    }

    private void proceedAfterPermission(int v) {
        //We've got the permission, now we can proceed further
        if (v == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            Toast.makeText(getBaseContext(), "We got the Storage Permission", Toast.LENGTH_SHORT).show();
            checkBluetoothConnection();
        } else if (v == LOCATION_PERMISSION_CONSTANT) {
            Toast.makeText(getBaseContext(), "We got the Location Permission", Toast.LENGTH_SHORT).show();
            checkBluetoothConnection();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission(EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
        if (requestCode == LOCATION_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                proceedAfterPermission(LOCATION_PERMISSION_CONSTANT);
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(DashboardActivity.this, ACCESS_FINE_LOCATION)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();


                            ActivityCompat.requestPermissions(DashboardActivity.this, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(), "Unable to get Permission", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        if (sentToSettings) {
            if (ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }
            if (ActivityCompat.checkSelfPermission(DashboardActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                //Got Permission
                proceedAfterPermission(LOCATION_PERMISSION_CONSTANT);
            }
        }
    }

    private class SaveReceipt extends AsyncTask {

        Context context;
        String jsonStr = null;
        String url;
        HashMap<String, String> params;
        String paramKey;
        Receipt receipt = new Receipt();
        Payment payment = new Payment();
        Long cusId;

        public SaveReceipt(Context context, String url, Receipt receipt, Long cusId) {
            this.context = context;
            this.url = url;
            this.receipt = receipt;
            this.cusId = cusId;
            params = new HashMap<String, String>();
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
                params.put("Amount", String.valueOf(receipt.getAmount()));
                params.put("LedgerId", String.valueOf(receipt.getLegerId()));
                params.put("PayMode", String.valueOf(receipt.getPaymentMode()));

                this.params.put("ChqNo", receipt.getChequeNo());
                this.params.put("ChqDate", receipt.getChequeDate());
                this.params.put("ChqBankName", receipt.getChequeBankName());

            }


        }

        @Override
        protected Object doInBackground(Object[] params) {
            HttpHandler httpHandler = new HttpHandler();
            System.out.println("URL ================== " + this.url);

            jsonStr = httpHandler.makeServiceCall(this.url, this.params);
            return true;

        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("JSON RESPONSE" + jsonStr);
            if (jsonStr != null) {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();


                Type collectionType = new TypeToken<AddCustomerResponse>() {
                }.getType();

                AddCustomerResponse customerRCollection = gson.fromJson(jsonStr, collectionType);


                Store.getInstance().addCustomerResponse = (AddCustomerResponse) customerRCollection;

                String method = "";
                if (this.url.toLowerCase().contains("payment")) {
                    method = "Payment";
                } else {
                    method = "Receipt";
                }

                if (Store.getInstance().addCustomerResponse.isHasError()) {
                    Toast.makeText(context, method + " not Saved", Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(context, method + " Saved", Toast.LENGTH_SHORT).show();


                    if (this.url.toLowerCase().contains("payment")) {
                        this.payment.setId(Store.getInstance().addCustomerResponse.getId());
                        payment.setSynced(true);

                    } else {
                        this.receipt.setId(Store.getInstance().addCustomerResponse.getId());
                        receipt.setSynced(true);

                    }


                }

            }

        }


    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);

            chequeDateValue = c.getTime();

            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            String date = (arg2+1)+"/"+arg3+"/"+arg1;
            JSONObject result = null;
            boolean status = false;
            String errMsg = "";
            try {
                 result = validateChequeDate(arg3, arg2 + 1, arg1);
                 status = (boolean) result.get("status");
                 errMsg = (String) result.get("errMsg");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(status) {
                chequeDate.setText(String.valueOf(date));


                Toast.makeText(DashboardActivity.this, date, Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(DashboardActivity.this, errMsg, Toast.LENGTH_LONG).show();
            }
        }

        private JSONObject validateChequeDate(int day, int month, int year) throws JSONException {


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
    };

    public  boolean validate()
    {
        boolean status = true;
        if(paymentModeValue.toLowerCase().contains("cheque"))
        {

            boolean con1 = TextUtils.isEmpty(chequeNo.getText().toString());;
            boolean con2 =  chequeNo.getText().toString().contains("");

            if(TextUtils.isEmpty(chequeDate.getText().toString()) )
            {
                chequeDate.setText(String.valueOf(""));
            }

            if(TextUtils.isEmpty(bankName.getText().toString()) )
            {
                bankName.setText(String.valueOf(""));
            }
            if(con1 || !con2)
            {
                chequeNo.setError("Please fill..");
                chequeNo.requestFocus();

                status = false;
            }
            else
            {
                status = true;

            }

        }

        return  status;


    }

    private class TransactionList extends AsyncTask {


        @Override
        protected Object doInBackground(Object[] objects) {
            SignalRService.getTransactionType();
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            setPaymentMode();

        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Activity:","Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Activity:","Destroy");
        fromCustomer.removeTextChangedListener(fromCustomerWatcher);

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Activity:","Start");

        fromCustomerWatcher = new TextWatcher() {
            String before;
            boolean status;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                before = s.toString();


                if (! (currentSaleType.toLowerCase().contains("order") || currentSaleType.toLowerCase().contains("return") )) {

                    if (!(paymentModeValue.contains("PNT") | paymentModeValue.contains("Cheque"))) {

                        if (!currentSaleType.toLowerCase().contains("order")) {
                            save.setEnabled(false);
                            save.setBackgroundColor(getResources().getColor(R.color.grey));
                            preview.setEnabled(false);
                            Log.d("Preview", "Disabled 2");
                            preview.setBackgroundColor(getResources().getColor(R.color.grey));
                        }
                    } else {

                        preview.setEnabled(true);
                        Log.d("Preview", "Enabled 2.0");
                        save.setEnabled(true);
                        save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                        preview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                double fc = 0;
                if(!TextUtils.isEmpty(s))
                {
                    fc = Double.parseDouble(s.toString());
                }

                status = decimalFormatter(fc);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!status)
                {
                    fromCustomer.setText(before);
                    fromCustomer.post(new Runnable() {
                        @Override
                        public void run() {
                            fromCustomer.setSelection(fromCustomer.getText().toString().trim().length());
                        }
                    });
                }

                if (!(paymentModeValue.contains("PNT") | paymentModeValue.contains("Cheque") )) {

                    if (! (currentSaleType.toLowerCase().contains("order") || currentSaleType.toLowerCase().contains("return") ))
                    {

                        if (Store.getInstance().fromCustomer.getText() != null) {
                            double balance = 0;
                            if (!Store.getInstance().fromCustomer.getText().toString().equals("") || TextUtils.isEmpty(Store.getInstance().fromCustomer.getText().toString())) {

                                double gt = Double.parseDouble(grandTotal.getText().toString());
                                double fc = 0;
                                try {

                                    fc = Double.parseDouble(fromCustomer.getText().toString());
                                    if(TextUtils.isEmpty(fromCustomer.getText()))
                                    {
                                        fc =0;
                                    }
                                    balance = fc - gt;
                                } catch (Exception e) {
                                    fc = 0;
                                    balance = fc - gt;
                                }


                                if (balance < 0) {
                                   // Toast.makeText(DashboardActivity.this, "Invalid amount received from customer", Toast.LENGTH_SHORT).show();
                                    //fromCustomer.setError("Invalid amount");
                                    //fromCustomer.setSelection(fromCustomer.getText().length());
                                    note.setText("Get amount RM "+String.valueOf(String.format("%.2f", gt) +" or above"));
                                    note.setTextColor(Color.RED);
                                    save.setEnabled(false);
                                    save.setBackgroundColor(getResources().getColor(R.color.grey));
                                    preview.setEnabled(false);
                                    Log.d("Preview", "Disabled 3");
                                    preview.setBackgroundColor(getResources().getColor(R.color.grey));

                                } else {
                                    note.setText(getString(R.string.cfc_note));
                                    note.setTextColor(Color.BLUE);
                                    save.setEnabled(true);
                                    save.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                                    tenderAmount.setText(String.valueOf(String.format("%.2f", balance)));
                                    if(balance<0)
                                    {
                                        tenderAmount.setText(String.valueOf(String.format("%.2f", 0.00)));
                                    }
                                    preview.setEnabled(true);
                                    Log.d("Preview", "Enabled 4");
                                    preview.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));

                                }


                            }

                        }
                    }

                } else {

                    Log.d("Balance Calc", "Not applicable for this method");
                }


            }

        };
        fromCustomer.addTextChangedListener(fromCustomerWatcher);
    }
    private void showChequeLayout() {
        System.out.println("showing chq layout ...");
        cn.setVisibility(View.VISIBLE);
        chequeNo.setVisibility(View.VISIBLE);
        bnl.setVisibility(View.VISIBLE);
        cdl.setVisibility(View.VISIBLE);
        bankName.setVisibility(View.VISIBLE);
        chequeDate.setVisibility(View.VISIBLE);
        dateChooser.setVisibility(View.VISIBLE);
    }

    private void hideChequeLayout() {

        System.out.println("Hiding chq layout ...");
        cn.setVisibility(View.GONE);
        chequeNo.setVisibility(View.GONE);
        bnl.setVisibility(View.GONE);
        cdl.setVisibility(View.GONE);
        bankName.setVisibility(View.GONE);
        chequeDate.setVisibility(View.GONE);
        dateChooser.setVisibility(View.GONE);
    }

    private void showCashLayout() {
        fromCustomer.setVisibility(View.VISIBLE);
        tenderAmount.setVisibility(View.VISIBLE);
        cfc.setVisibility(View.VISIBLE);
        ta.setVisibility(View.VISIBLE);
        note.setVisibility(View.GONE);
    }

    private void hideCashLayout() {
        fromCustomer.setVisibility(View.GONE);
        tenderAmount.setVisibility(View.GONE);
        cfc.setVisibility(View.GONE);
        ta.setVisibility(View.GONE);
        note.setVisibility(View.GONE);

    }
    private void induvidualDiscount() {
        discountValue.setText("0");
        discountValue.setEnabled(true);
        discountValue.setVisibility(View.VISIBLE);
        appDiscount.setVisibility(View.VISIBLE);
        discountLabel.setVisibility(View.VISIBLE);
        grandDiscountTypeSpinner.setVisibility(View.VISIBLE);
    }

    private void grandTotalDiscount() {
        discountValue.setEnabled(true);
        discountValue.setVisibility(View.VISIBLE);
        appDiscount.setVisibility(View.VISIBLE);
        discountLabel.setVisibility(View.VISIBLE);
        grandDiscountTypeSpinner.setVisibility(View.VISIBLE);

    }

    private void noDiscount() {
        discountValue.setText("0");
        discountValue.setEnabled(false);
        discountValue.setVisibility(View.GONE);
        appDiscount.setVisibility(View.GONE);
        discountLabel.setVisibility(View.GONE);
        grandDiscountTypeSpinner.setVisibility(View.GONE);
    }
    public int getTransactionType(String name)
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
    private void clearData() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
        builder.setMessage("Do you want to clear ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        realeaseUIComponent(saleType);
                        realeaseUIComponent(isGst);
                        realeaseUIComponent(discount);
                        realeaseUIComponent(paymentMode);
                        realeaseUIComponent(customerSearch);
                        realeaseUIComponent(customerNameKey);

                        clearList();
                        clearCSP();
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();


    }
    public void clearWOConfirmation()
    {
        realeaseUIComponent(saleType);
        realeaseUIComponent(isGst);
        realeaseUIComponent(discount);
        realeaseUIComponent(paymentMode);
        realeaseUIComponent(customerSearch);
        realeaseUIComponent(customerNameKey);

        clearList();
        clearCSP();
    }

    private void clearCSP() {

        runOnUiThread(new Runnable() {
            public void run() {
                //Do something on UiThread

                Store.getInstance().currentStockGroup = null;
                stockGroupName.setText("");

                Store.getInstance().currentCustomer = null;
                customerName.setText(String.valueOf(""));

                if(!currentSaleType.toLowerCase().contains("order")) {
                    save.setEnabled(false);
                    save.setBackgroundColor(getResources().getColor(R.color.grey));
                    preview.setEnabled(false);
                    Log.d("Preview", "Disabled 00");
                    preview.setBackgroundColor(getResources().getColor(R.color.grey));
                }



                fromCustomer.setText(String.valueOf(""));
                grandTotal.setText(String.valueOf("0.00"));
                tenderAmount.setText(String.valueOf("0.00"));
                discountValue.setText(String.valueOf("0"));
                appDiscount.setText(String.valueOf("0"));
                chequeNo.setText(String.valueOf(""));
               // roundOffValue.setText(String.valueOf("0"));
                roundOffText.setText(String.valueOf("0.00"));
                chequeDate.setText(String.valueOf(""));
                bankName.setText(String.valueOf(""));
                productNameKey.setText(String.valueOf(""));
                Log.d("clear","csp");
            }
        });


    }

    public  void lockUIComponent(View v)
    {
        v.setEnabled(false);
        v.setBackgroundResource(R.color.disabled);

    }
    public void realeaseUIComponent(View v)
    {
        v.setEnabled(true);
        v.setBackgroundResource(R.drawable.enable);

        if(v.getId()==R.id.customer_name_key){

            v.setBackgroundResource(R.drawable.rounded_edittext);
        }



    }

    private class StoreCurrentCusDetails extends AsyncTask {
        String name;
        private Customer choosedCustomer;

        public StoreCurrentCusDetails(String name) {

            this.name = name;
        }

        @Override
        protected Object doInBackground(Object[] objects) {




             choosedCustomer = null;
            for(int i=0;i<Store.getInstance().customerList.size();i++)
            {
                System.out.println(name.toLowerCase()+"===="+Store.getInstance().customerList.get(i).getLedger().getLedgerName().toString());
                System.out.println(name.toLowerCase().toString().contains(Store.getInstance().customerList.get(i).getLedger().getId().toString()));
                if(name.toLowerCase().toString().equals(Store.getInstance().customerList.get(i).getLedger().getLedgerName().toString().toLowerCase()))
                {
                    choosedCustomer  = Store.getInstance().customerList.get(i);
                    Store.getInstance().currentCustomer = choosedCustomer.getLedgerName();

                    Store.getInstance().currentCustomerId = choosedCustomer.getLedger().getId();

                    Store.getInstance().currentLedgerId = choosedCustomer.getLedger().getId();


                    Store.getInstance().currentCustomerPosition = i;




                }
            }


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            lockAllUIComponents();
            customerName.setText(String.valueOf(choosedCustomer.getLedger().getLedgerName()).toUpperCase());
            Toast.makeText(DashboardActivity.this, "Customer Name : " + String.valueOf(choosedCustomer.getLedger().getLedgerName()).toUpperCase(), Toast.LENGTH_SHORT).show();

        }
    }

    private class StoreCurrentStockDetails extends AsyncTask {
        String name;
        private StockGroup choosedStockGroup;
        public StoreCurrentStockDetails(String selected) {
            this.name = selected;
        }

        @Override
        protected Object doInBackground(Object[] objects) {

            for(int i=0;i<Store.getInstance().stockGroupList.size();i++) {

                if(name.toLowerCase().toString().contains(Store.getInstance().stockGroupList.get(i).getId().toString()))
                {
                    choosedStockGroup = Store.getInstance().stockGroupList.get(i);



                    Store.getInstance().currentStockGroup = choosedStockGroup.getStockGroupName();
                    Store.getInstance().currentStockGroupId = choosedStockGroup.getId();


                    System.out.println("---currentStockGroupId==" + Store.getInstance().currentStockGroupId);
                }



            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            Store.getInstance().currentStockGroup = choosedStockGroup.getStockGroupName();
            Store.getInstance().currentStockGroupId = choosedStockGroup.getId();


            stockGroupName.setText(String.valueOf(Store.getInstance().currentStockGroup).toUpperCase());

        }
    }
}
