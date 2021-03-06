/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.BTLib.BTDeviceList;
import com.bizsoft.fmcgv2.BTLib.BTDeviceListActivity;
import com.bizsoft.fmcgv2.BTLib.BTPrint;
import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.adapter.SalesAdapter;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Sale;
import com.bizsoft.fmcgv2.dataobject.SaleOrder;
import com.bizsoft.fmcgv2.dataobject.SaleReturn;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.PrinterService;
import com.bizsoft.fmcgv2.service.UIUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import static com.bizsoft.fmcgv2.DashboardActivity.BLUETOOTH_FLAG;
import static com.bizsoft.fmcgv2.service.BizUtils.getTransactionType;

public class ReprintActivity extends AppCompatActivity {


    Spinner customerSpinner,saleType,printItem;
    private String currentSaleType;
    private String customer;
    private int customerPosition;
    private Long customerId;
    private Customer currentCustomer;
    ListView listView;
    ArrayList<Product> products = new ArrayList<Product>();
    private SalesAdapter adapter;
    TextView subTotalT,gstT,grandTotalT;
    Button print;
    private double tenderAmountValue = 0;
    private  double fromCustomerValue = 0;
    TextView balance,received;
    Spinner billID;
    private String billIDValue;
    Sale currentSales;
    SaleOrder currentSaleOrder;
    SaleReturn currentSaleReturn;
    DecimalFormat df ;
    private double subTotal,gst,grandTotal;
    BizUtils bizUtils;

    TextView discountValue;
    private int currentPrintPosition = 0;
    public static String lastSavedBillType = "none";
    private TextView discountLabel;
    private TextView balanceLabel,receivedLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reprint);


        customerSpinner = (Spinner) findViewById(R.id.customer_spinner);
        saleType = (Spinner) findViewById(R.id.sale_type_spinner);
        listView = (ListView) findViewById(R.id.listview);
        subTotalT = (TextView) findViewById(R.id.sub_total);
        gstT = (TextView) findViewById(R.id.GST);
        grandTotalT = (TextView) findViewById(R.id.grand_total);
        print = (Button) findViewById(R.id.dc_print);
        balance = (TextView) findViewById(R.id.balance_amount);
        received = (TextView) findViewById(R.id.received_amount);
        billID = (Spinner) findViewById(R.id.bill_id);

        discountValue = (TextView) findViewById(R.id.discount_value);
        discountLabel = (TextView) findViewById(R.id.discount_label);
        receivedLabel = (TextView) findViewById(R.id.received_label);
        balanceLabel = (TextView) findViewById(R.id.balance_label);

        bizUtils = new BizUtils();

        UIUtil.setActionBarMenu(ReprintActivity.this,getSupportActionBar(),"Reprint Bills");


        df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        setCustomerSpinner();


            adapter = new SalesAdapter(ReprintActivity.this,products);
            listView.setAdapter(adapter);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(BTPrint.btsocket==null)
                    {
                        Intent intent = new Intent(ReprintActivity.this,BTDeviceListActivity.class);

                        startActivityForResult(intent,BLUETOOTH_FLAG);

                        Toast.makeText(ReprintActivity.this, "new connection", Toast.LENGTH_SHORT).show();


                    }
                    else
                    {



                        Customer customer = Store.getInstance().customerList.get(customerPosition);
                        ArrayList<Product> temp = Store.getInstance().addedProductList;

                        if(currentSaleType .toLowerCase().contains("return"))
                        {
                            temp =   currentSaleReturn.getProducts();
                            PrinterService.print(ReprintActivity.this,customer, "Sale Return", temp, null, null, currentSaleReturn,"Customer Copy");

                        }
                        if(currentSaleType .toLowerCase().contains("order"))
                        {
                            temp =   currentSaleOrder.getProducts();
                            PrinterService.print(ReprintActivity.this,customer, "Sale Order", temp, null,currentSaleOrder, null,"Customer Copy");

                        }
                        if(! (currentSaleType .toLowerCase().contains("return") || currentSaleType .toLowerCase().contains("order")) )
                        {
                            temp =   currentSales.getProducts();
                            PrinterService.print(ReprintActivity.this,customer, "Sale",temp, currentSales, null, null,"Customer Copy");

                        }
                        if(currentSaleType .toLowerCase().contains("none"))
                        {
                            Toast.makeText(ReprintActivity.this, "No last bills found..", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                catch (Exception e)
                {
                    System.out.println("Exception "+e);

                }
            }
        });

    }
    public void setSaleType()
    {
        final List<String> genderList = new ArrayList<String>();
        genderList.add("Sale");
        genderList.add("Sale Order");
        genderList.add("Sale Return");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(ReprintActivity.this, android.R.layout.simple_spinner_item, genderList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saleType.setAdapter(dataAdapter);
        saleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



                Toast.makeText(getApplicationContext(),genderList.get(position),Toast.LENGTH_SHORT).show();
                currentSaleType = genderList.get(position);
                if(currentSaleType.toLowerCase().contains("order"))
                {



                    setBillIdSpinner();
                }
                else
                {

                    setBillIdSpinner();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                System.out.println("Sale type order -----"+currentSaleType);
                setBillIdSpinner();




            }


        });



    }
    public void setCustomerSpinner()
    {
        final List<String> genderList = new ArrayList<String>();


        final ArrayList<Customer> customerList = Store.getInstance().customerList;

        for(int i=0;i<customerList.size();i++)
        {
            if(customerList.get(i).getId()==null)
            {
                genderList.add("Unsaved"+" - "+customerList.get(i).getLedger().getLedgerName());
            }
            else
            {
                if(customerList.get(i).getId()==0)
                {
                    genderList.add(customerList.get(i).getLedger().getLedgerName());
                }
                else {
                    genderList.add(customerList.get(i).getId() + " - " + customerList.get(i).getLedger().getLedgerName());
                }
            }

        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(ReprintActivity.this, android.R.layout.simple_spinner_item, genderList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerSpinner.setAdapter(dataAdapter);
        customerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),genderList.get(position),Toast.LENGTH_SHORT).show();
                customer  = genderList.get(position);
                customerPosition  = position;
                customerId = customerList.get(position).getId();
                currentCustomer = customerList.get(position);
                setSaleType();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                customer = genderList.get(0);
                customerPosition  = 0;
                customerId = customerList.get(0).getId();
                currentCustomer = customerList.get(0);
                setSaleType();





            }


        });



    }
    public void setBillIdSpinner()
    {
        ArrayList<Sale> sale = null;
        ArrayList<SaleOrder> saleOrder = null;
        ArrayList<SaleReturn> saleReturn = null;
        final ArrayList<String> genderList = new ArrayList<String>();
        int size =0;


        System.out.println("Sale order Size======= " +currentCustomer.getSaleOrdersOfCustomer().size() );
        System.out.println("Sale Size ==============" +currentCustomer.getSalesOfCustomer().size() );

        if(currentSaleType.toLowerCase().contains("order")) {
            saleOrder = new ArrayList<SaleOrder>();
            saleOrder = currentCustomer.getSaleOrdersOfCustomer();
            size = saleOrder.size();

            for(int i=0;i<size;i++) {

                genderList.add(currentCustomer.getSaleOrdersOfCustomer().get(i).getRefCode());
            }

        }
        else
        if(currentSaleType.toLowerCase().contains("return")) {
            saleReturn = new ArrayList<SaleReturn>();
            saleReturn = currentCustomer.getSaleReturnOfCustomer();
            size = saleReturn.size();

            for(int i=0;i<size;i++) {

                genderList.add(currentCustomer.getSaleReturnOfCustomer().get(i).getRefCode());
            }

        }
        else {
            sale = new ArrayList<Sale>();
            sale = currentCustomer.getSalesOfCustomer();
            size = sale.size();

            for(int i=0;i<size;i++) {
                genderList.add(currentCustomer.getSalesOfCustomer().get(i).getRefCode());
            }
        }

        if(size == 0)
        {
            products.clear();
            products.addAll(new ArrayList<Product>());
            adapter.notifyDataSetChanged();

            subTotalT.setText(String.valueOf("0.00"));
            gstT.setText(String.valueOf("0.00"));
            grandTotalT.setText(String.valueOf("0.00"));
            received.setText(String.valueOf("0.00"));
            balance.setText(String.valueOf("0.00"));
            discountValue.setText(String.valueOf("0.00"));

        }




        // Drop down layout style - list view with radio button
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(ReprintActivity.this,genderList);
        Store.getInstance().reprintSpinnerText.setTextSize(20);
        billID.setAdapter(customSpinnerAdapter);

        final ArrayList<Sale> finalSale = sale;
        final ArrayList<SaleOrder> finalSaleOrder = saleOrder;
        final ArrayList<SaleReturn> finalSaleReturn = saleReturn;
        billID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),genderList.get(position),Toast.LENGTH_SHORT).show();
                billIDValue = genderList.get(position);


                if(currentSaleType.toLowerCase().contains("order"))
                {
                    ArrayList<SaleOrder> solist = currentCustomer.getSaleOrdersOfCustomer();
                    for(int i=0;i< solist.size();i++)
                    {
                        if(solist.get(i).getRefCode().equals(billIDValue))
                        {
                            currentSaleOrder = solist.get(i);
                            products.clear();
                            products.addAll(currentSaleOrder.getProducts());
                            adapter.notifyDataSetChanged();
                            System.out.println("Sale order Size"+products.size());

                            generateBill(position);
                        }

                    }



                }
                else
                if(currentSaleType.toLowerCase().contains("return")) {

                    ArrayList<SaleReturn> srlist = currentCustomer.getSaleReturnOfCustomer();
                    for(int i=0;i< srlist.size();i++) {
                        if (srlist.get(i).getRefCode().equals(billIDValue)) {
                            currentSaleReturn =srlist.get(i);
                            products.clear();
                            products.addAll(currentSaleReturn.getProducts());
                            adapter.notifyDataSetChanged();
                            generateBill(position);
                            System.out.println("Sale  Return Size" + products.size());
                        }

                    }
                }
                else
                {
                    ArrayList<Sale> salist = currentCustomer.getSalesOfCustomer();
                    for(int i=0;i< salist.size();i++) {
                        if (salist.get(i).getRefCode().equals(billIDValue)) {
                            currentSales = salist.get(i);
                            products.clear();
                            products.addAll(currentSales.getProducts());
                            adapter.notifyDataSetChanged();
                            generateBill(position);
                            System.out.println("Sale  Size" + products.size());
                        }
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                billIDValue = genderList.get(0);



                if(currentSaleType.toLowerCase().contains("order"))
                {
                    ArrayList<SaleOrder> solist = currentCustomer.getSaleOrdersOfCustomer();
                    for(int i=0;i< solist.size();i++)
                    {
                        if(solist.get(i).getRefCode().equals(billIDValue))
                        {
                            currentSaleOrder = solist.get(i);
                            products.clear();
                            products.addAll(currentSaleOrder.getProducts());
                            adapter.notifyDataSetChanged();
                            System.out.println("Sale order Size"+products.size());

                            generateBill(0);
                        }

                    }



                }
                else
                if(currentSaleType.toLowerCase().contains("return")) {

                    ArrayList<SaleReturn> srlist = currentCustomer.getSaleReturnOfCustomer();
                    for(int i=0;i< srlist.size();i++) {
                        if (srlist.get(i).getRefCode().equals(billIDValue)) {
                            currentSaleReturn =srlist.get(i);
                            products.clear();
                            products.addAll(currentSaleReturn.getProducts());
                            adapter.notifyDataSetChanged();
                            generateBill(0);
                            System.out.println("Sale  Return Size" + products.size());
                        }

                    }
                }
                else
                {
                    ArrayList<Sale> salist = currentCustomer.getSalesOfCustomer();
                    for(int i=0;i< salist.size();i++) {
                        if (salist.get(i).getRefCode().equals(billIDValue)) {
                            currentSales = salist.get(i);
                            products.clear();
                            products.addAll(currentSales.getProducts());
                            adapter.notifyDataSetChanged();
                            generateBill(0);
                            System.out.println("Sale  Size" + products.size());
                        }
                    }

                }


            }


        });





    }
    public void generateBill(int position)
    {
         subTotal = 0;
         gst = 0;
         grandTotal = 0;
         fromCustomerValue = 0;
        tenderAmountValue = 0;

        currentPrintPosition = position;




        discountLabel.setVisibility(View.GONE);
        discountValue.setVisibility(View.GONE);
        balanceLabel.setVisibility(View.GONE);
        balance.setVisibility(View.GONE);
        received.setVisibility(View.GONE);
        receivedLabel.setVisibility(View.GONE);



        if(currentSaleType.toLowerCase().contains("order"))
        {

            subTotal = bizRound(currentSaleOrder.getReceivedAmount(),2);
            gst = bizRound(currentSaleOrder.getGst(),2);
            grandTotal = bizRound(currentSaleOrder.getGrandTotal(),2);
            fromCustomerValue = bizRound(currentSaleOrder.getReceivedAmount(),2);
            tenderAmountValue = bizRound(currentSaleOrder.getBalance(),2);

            if(BizUtils.getDiscountType(currentSaleOrder.getDiscountType())==Store.getInstance().DISCOUNT_FOR_GRABD_TOTAL)
            {

                discountLabel.setVisibility(View.VISIBLE);
                discountValue.setVisibility(View.VISIBLE);

            }



            subTotalT.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getSubTotal())));
            gstT.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getGst())));
            grandTotalT.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getGrandTotal())));
            discountValue.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getDiscountValue())));
            received.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getReceivedAmount())));

            balance.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getBalance())));

            if(currentSaleOrder.equals(Store.getInstance().discountTypePercentage)) {
                discountLabel.setText(String.valueOf("Discount ( " + currentSaleOrder.getDiscountPercentage() + " " + currentSaleOrder.getGrandTotalDiscountType() + ")= "));
            }else
            {
                discountLabel.setText(String.valueOf("Discount ( " + currentSaleOrder.getGrandTotalDiscountType() + " )= "));
            }
        }
        else
        if(currentSaleType.toLowerCase().contains("return"))
        {
            if(BizUtils.getDiscountType(currentSaleReturn.getDiscountType())==Store.getInstance().DISCOUNT_FOR_GRABD_TOTAL)
            {

                discountLabel.setVisibility(View.VISIBLE);
                discountValue.setVisibility(View.VISIBLE);

            }

            received.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getReceivedAmount())));

            balance.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getBalance())));
            subTotalT.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getSubTotal())));
            gstT.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getGst())));
            grandTotalT.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getGrandTotal())));
            discountValue.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getDiscountValue())));
            if(currentSaleReturn.equals(Store.getInstance().discountTypePercentage)) {
                discountLabel.setText(String.valueOf("Discount ( " + currentSaleReturn.getDiscountPercentage() + " " + currentSaleReturn.getGrandTotalDiscountType() + ")= "));
            }else
            {
                discountLabel.setText(String.valueOf("Discount ( " + currentSaleReturn.getGrandTotalDiscountType() + " )= "));
            }
        }
        else
        {

            if(BizUtils.getDiscountType(currentSales.getDiscountType())==Store.getInstance().DISCOUNT_FOR_GRABD_TOTAL)
            {

                discountLabel.setVisibility(View.VISIBLE);
                discountValue.setVisibility(View.VISIBLE);


            }
            if(BizUtils.getTransactionType(DashboardActivity.currentSaleType)==Store.getInstance().SALE) {
                if (currentSales.getPaymentMode().toLowerCase().contains("cash")) {
                    balanceLabel.setVisibility(View.VISIBLE);
                    balance.setVisibility(View.VISIBLE);
                    received.setVisibility(View.VISIBLE);
                    receivedLabel.setVisibility(View.VISIBLE);
                    received.setText(String.valueOf(String.format("%.2f",currentSales.getReceivedAmount())));

                    if(currentSales.equals(Store.getInstance().discountTypePercentage)) {
                        discountLabel.setText(String.valueOf("Discount ( " + currentSales.getDiscountPercentage() + " " + currentSales.getGrandTotalDiscountType() + ")= "));
                    }else
                    {
                        discountLabel.setText(String.valueOf("Discount ( " + currentSales.getGrandTotalDiscountType() + " )= "));
                    }
                    balance.setText(String.valueOf(String.format("%.2f",currentSales.getBalance())));
                }
            }


            subTotalT.setText(String.valueOf(String.format("%.2f",currentSales.getSubTotal())));
            gstT.setText(String.valueOf(String.format("%.2f",currentSales.getGst())));
            grandTotalT.setText(String.valueOf(String.format("%.2f",currentSales.getGrandTotal())));
            discountValue.setText(String.valueOf(String.format("%.2f",currentSales.getDiscountValue())));

            if(currentSales.equals(Store.getInstance().discountTypePercentage)) {
                discountLabel.setText(String.valueOf("Discount ( " + currentSales.getDiscountPercentage() + " " + currentSales.getGrandTotalDiscountType() + ")= "));
            }else
            {
                discountLabel.setText(String.valueOf("Discount ( " + currentSales.getGrandTotalDiscountType() + " )= "));
            }
        }


    }
    public void print2(Context context,Customer customer, String type, ArrayList<Product> products, Sale sale, SaleOrder saleOrder, SaleReturn saleReturn) {
        SharedPreferences prefs = getSharedPreferences(Store.getInstance().MyPREFERENCES, MODE_PRIVATE);
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine(prefs.getString(getString(R.string.companyName), "Aboorvass"));
        BTPrint.SetAlign(Paint.Align.CENTER);


        System.out.println("company address line 1 ==============================" + prefs.getString(getString(R.string.companyAddressLine1), "0"));
        System.out.println("company address line 2 ==============================" + prefs.getString(getString(R.string.companyAddressLine2), "0"));
        System.out.println("company gst ==============================" + prefs.getString(getString(R.string.gstNo), "0"));
        System.out.println("company mail ==============================" + prefs.getString(getString(R.string.email), "0"));
        System.out.println("company postalcode ==============================" + prefs.getString(getString(R.string.postal_code), "0"));

        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine(prefs.getString(getString(R.string.companyAddressLine1), "0") + "," + prefs.getString(getString(R.string.companyAddressLine2), "0") + "," + prefs.getString(getString(R.string.postal_code), "+1234556789"));

        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("E-Mail: " + prefs.getString(getString(R.string.email), "+1234556789"));
        BTPrint.PrintTextLine("GST No: " + prefs.getString(getString(R.string.gstNo), "+1234556789"));
        BTPrint.PrintTextLine("Ph No: " + prefs.getString(getString(R.string.phoneNumber), "+1234556789"));
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("***Bill Details***");
        BizUtils bizUtils = new BizUtils();
        String refNo = "";
        if(sale!=null) {
            refNo = sale.getRefCode();
        }
        if(saleOrder !=null)
        {
            refNo = saleOrder.getRefCode();
        }
        if(saleReturn !=null)
        {
            refNo = saleReturn.getRefCode();
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

        String paymentModeValue = "";
        if(sale!=null) {

            paymentModeValue = sale.getPaymentMode();

        }
        if(saleOrder !=null)
        {
            paymentModeValue = saleOrder.getPaymentMode();
        }
        if(saleReturn !=null)
        {
            paymentModeValue = saleReturn.getPaymentMode();
        }


        BTPrint.PrintTextLine("Payment Mode :" + paymentModeValue);
        BTPrint.PrintTextLine("------------------------------");

        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***ITEM DETAILS***");
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.LEFT);
        //  BTPrint.PrintTextLine("NAME     QTY    PRICE   AMOUNT ");

        customer = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);


        String gstSpace = "";

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
                    id = "     ";
                }

            }


            double subTotalx = 0;
            double gstx = 0;
            if (String.valueOf(item.getQty() * item.getMRP()).length() >= 6) {
                ir = String.valueOf( (item.getQty() * item.getMRP()) - item.getDiscountAmount()).substring(0, 4);
                ir = ir + "..";

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


            System.out.println("on test");
            String subTotal = String.valueOf(subTotalx);
            int subTotalLength = subTotal.length();

            String gst = String.valueOf(gstx);
            int gstLength = gst.length();


            gstSpace = "";

            int c = subTotalLength - gstLength;

            for (int f = 0; f < c; f++) {
                gstSpace = gstSpace + " ";
            }


            double tt = subTotalx;

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


        gstSpace = "";
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
        BTPrint.PrintTextLine("Grand total" + mgSpace + " RM " + String.format("%.2f",mainGrantTotal));

        if(getTransactionType(type)==Store.getInstance().SALE) {
            if(sale.getPaymentMode().toLowerCase().contains("cash")) {
                BTPrint.PrintTextLine("Received amount RM " + String.format("%.2f", Double.parseDouble(ra)));
                BTPrint.PrintTextLine("Balance amount RM " + baSpace +String.format("%.2f", Double.parseDouble(ba)));
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
        BTPrint.PrintTextLine("Powered By Denariusoft SDN BHD");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***"+"Customer Copy"+"***");
        BTPrint.printLineFeed();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Result code === "+resultCode);
        System.out.println("requestCode === "+requestCode);
        System.out.println("data === "+data);



        try {


            BTPrint.btsocket = BTDeviceListActivity.getSocket();
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

        }
        catch (Exception e)
        {

        }

    }
    public static double bizRound(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }



}
