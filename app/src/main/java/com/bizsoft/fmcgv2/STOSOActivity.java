/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.BTLib.BTDeviceList;
import com.bizsoft.fmcgv2.BTLib.BTPrint;
import com.bizsoft.fmcgv2.Tables.SOPending;
import com.bizsoft.fmcgv2.Tables.SalesOrder;
import com.bizsoft.fmcgv2.Tables.SalesOrderDetails;
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

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import static com.bizsoft.fmcgv2.ReprintActivity.bizRound;
import static com.bizsoft.fmcgv2.service.BizUtils.getTransactionType;
import static com.bizsoft.fmcgv2.service.BizUtils.syncStockProcessProductList;

public class STOSOActivity extends AppCompatActivity {
    Spinner customerSpinner,saleType,printItem;
    private String currentSaleType = "order";
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

    Spinner billID;
    private String billIDValue;
    Sale currentSales;
    SOPending currentSaleOrder;
    SaleReturn currentSaleReturn;
    DecimalFormat df ;
    private double subTotal,gst,grandTotal;
    BizUtils bizUtils;

    TextView discountValue;
    private Button delete;
    TextView discountLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stoso);

        UIUtil.setActionBarMenu(STOSOActivity.this,getSupportActionBar(),"Sales Order To Sale");


        customerSpinner = (Spinner) findViewById(R.id.customer_spinner);
        saleType = (Spinner) findViewById(R.id.sale_type_spinner);
        listView = (ListView) findViewById(R.id.listview);
        subTotalT = (TextView) findViewById(R.id.sub_total);
        gstT = (TextView) findViewById(R.id.GST);
        grandTotalT = (TextView) findViewById(R.id.grand_total);
        print = (Button) findViewById(R.id.dc_print);

        billID = (Spinner) findViewById(R.id.bill_id);

        discountValue = (TextView) findViewById(R.id.discount_value);
        bizUtils = new BizUtils();
        delete = (Button) findViewById(R.id.delete);
        discountLabel = (TextView) findViewById(R.id.discount_label);





        getSupportActionBar().setTitle("Covert To Sale");




        df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);

        setCustomerSpinner();



        adapter = new SalesAdapter(STOSOActivity.this,products);
        listView.setAdapter(adapter);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(STOSOActivity.this);
                alertDialog.setMessage("Are you sure you want to delete ?");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        for(  int i=0;i<Store.getInstance().customerList.size();i++) {
                            System.out.println(Store.getInstance().customerList.get(i).getLedger().getId() + "=====================" + currentCustomer.getLedger().getId());
                            System.out.println(Store.getInstance().customerList.get(i).getLedger().getId().compareTo(currentCustomer.getLedger().getId()));
                            if (Store.getInstance().customerList.get(i).getLedger().getId().compareTo(currentCustomer.getLedger().getId()) == 0) {

                                System.out.println("item delete");
                                Store.getInstance().customerList.get(i).getSOPListDelete().add(currentSaleOrder);
                                Toast.makeText(STOSOActivity.this, "Sale Order Deleted", Toast.LENGTH_SHORT).show();

                                try {
                                    BizUtils.storeAsJSON("customerList", BizUtils.getJSON("customer", Store.getInstance().customerList));
                                    System.out.println("DB Updated..on local storage");


                                } catch (ClassNotFoundException e) {
                                    System.err.println("Unable to write to DB");
                                }

                                clear();
                            }
                        }



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






            }
        });

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(currentSaleOrder!=null)
                    {

                        SalesOrder salesOrder = new SalesOrder();
                        salesOrder.setSODate(currentSaleOrder.getSODate());

                        salesOrder.setId(((long) currentSaleOrder.getId()));
                        int invce = 0;
                        for(int i=0;i<Store.getInstance().customerList.size();i++)
                        {
                            ArrayList<Sale> invoice = Store.getInstance().customerList.get(i).getSalesOfCustomer();

                            if(invoice.size()>0) {
                                invce = invce + invoice.size();
                            }

                        }
                        salesOrder.setRefNo(currentSaleOrder.getRefNo());


                       // salesOrder.setRefNo(currentSaleOrder.getRefNo());
                        salesOrder.setRefCode(currentSaleOrder.getRefCode());
                        salesOrder.setLedgerId((int) currentSaleOrder.getLedgerId());
                        salesOrder.setItemAmount(currentSaleOrder.getItemAmount());
                        salesOrder.setDiscountAmount(currentSaleOrder.getDiscountAmount());
                        salesOrder.setGSTAmount(currentSaleOrder.getGSTAmount());
                        salesOrder.setTotalAmount(currentSaleOrder.getTotalAmount());
                        salesOrder.setNarration(currentSaleOrder.getNarration());
                        ArrayList<SalesOrderDetails> sodetails = currentSaleOrder.getSODetails();
                        for(int i=0;i<sodetails.size();i++)
                        {
                            sodetails.get(i).setSODId(salesOrder.getId());
                        }
                        salesOrder.setSODetails(sodetails);

                        for(  int i=0;i<Store.getInstance().customerList.size();i++)
                        {

                            System.out.println(Store.getInstance().customerList.get(i).getLedger().getId()+"====================="+currentCustomer.getLedger().getId());
                            System.out.println(Store.getInstance().customerList.get(i).getLedger().getId().compareTo(currentCustomer.getLedger().getId()));
                            if(Store.getInstance().customerList.get(i).getLedger().getId().compareTo(currentCustomer.getLedger().getId())==0)
                            {

                                    System.out.println("new entry");
                                    Store.getInstance().customerList.get(i).getsOPendingList().add(salesOrder);
                                    Toast.makeText(STOSOActivity.this, "Converted to Sale", Toast.LENGTH_SHORT).show();

                                try {
                                    BizUtils.storeAsJSON("customerList",BizUtils.getJSON("customer",Store.getInstance().customerList));
                                    System.out.println("DB Updated..on local storage");




                                } catch (ClassNotFoundException e) {
                                    System.err.println("Unable to write to DB");
                                }
                                    makeSales(salesOrder,Store.getInstance().customerList.get(i));
                                    clear();


                                System.out.println("--SO P Size = "+Store.getInstance().customerList.get(i).getsOPendingList().size());
                             /*   for(int x=0;x<Store.getInstance().customerList.get(i).getsOPendingList().size();x++)
                                {
                                    if(Store.getInstance().customerList.get(i).getsOPendingList().get(x).getRefNo().equals(salesOrder.getRefNo()))
                                    {
                                        System.out.println("Duplicate Entry");
                                    }
                                    else
                                    {
                                        System.out.println("new entry");
                                        Store.getInstance().customerList.get(i).getsOPendingList().add(salesOrder);


                                        Toast.makeText(STOSOActivity.this, "Converted to Sale", Toast.LENGTH_SHORT).show();
                                        clear();

                                        //Saving to local storage as JSON
                                        try {
                                            BizUtils.storeAsJSON("customerList",BizUtils.getJSON("customer",Store.getInstance().customerList));
                                            System.out.println("DB Updated..on local storage");
                                        } catch (ClassNotFoundException e) {

                                            System.err.println("Unable to write to DB");
                                        }


                                    }

                                }

                                */



                            }

                        }








                    }



            }
        });


    }

    private void makeSales(SalesOrder salesOrder, Customer customer) {

        try {
            Sale sale = new Sale();
            sale.setId(salesOrder.getId());
            sale.setBalance(0.00);
            sale.setDiscountValue(salesOrder.getDiscountAmount());
            sale.setGrandTotal(salesOrder.getTotalAmount());
            sale.setPaymentMode("Cash");
            sale.setSubTotal(salesOrder.getItemAmount());
            sale.setGst(salesOrder.getItemAmount() * 0.06);
            sale.setGstType("GST");
            sale.setReceivedAmount(0.00);
            sale.setRefCode(salesOrder.getRefNo());
            sale.setSaleType("sale");
            sale.setPaymentMode("none");
            sale.setDiscountType("grand total");
            sale.setGrandTotalDiscountType(Store.getInstance().discountTypePercentage);

            ArrayList<Product> products = new ArrayList<Product>();
            for (int i = 0; i < salesOrder.getSODetails().size(); i++) {
                ArrayList<SalesOrderDetails> salesOrderDetails = (ArrayList<SalesOrderDetails>) salesOrder.getSODetails();

                Product product = new Product();
                product.setId(salesOrderDetails.get(i).getId());
                product.setFinalPrice(salesOrderDetails.get(i).getUnitPrice() * salesOrderDetails.get(i).getQuantity());
                product.setMRP(salesOrderDetails.get(i).getUnitPrice());
                product.setSellingRate(salesOrderDetails.get(i).getUnitPrice());
                product.setDiscountAmount(salesOrderDetails.get(i).getDiscountAmount());

                product.setQty((long) salesOrderDetails.get(i).getQuantity());
                product.setUOM("");
                product.setSODId(salesOrder.getId());
                product.setProductName(salesOrderDetails.get(i).getProductName());
                double dp = (product.getDiscountAmount() * 100) / product.getDiscountAmount() + product.getFinalPrice();
                product.setDiscountPercentage(dp);


                products.add(product);

            }
            double a = (sale.getDiscountValue() * 100);
            double b = sale.getGrandTotal();
            double dp = a / b;
            sale.setDiscountPercentage(dp);

            sale.setProducts(products);


            // BizUtils.prettyJson("product json",products);

            for (int k = 0; k < Store.getInstance().productList.size(); k++) {
                Product actualProduct = Store.getInstance().productList.get(k);
                ArrayList<SalesOrderDetails> sodetails = (ArrayList<SalesOrderDetails>) salesOrder.getSODetails();


                for (int i = 0; i < sodetails.size(); i++) {

                    if (actualProduct.getId() == sodetails.get(i).getProductId()) {
                        actualProduct.setAvailableStock((long) (actualProduct.getAvailableStock() - sodetails.get(i).getQuantity()));
                        synchronized (actualProduct) {
                            actualProduct.notify();
                        }
                    }
                }

            }
            syncStockProcessProductList();


            PrinterService.print(STOSOActivity.this, customer, "Sale", products, sale, null, null, "Customer Copy");

            DashboardActivity dashboardActivity = new DashboardActivity();


            //DashboardActivity.lastSavedBillType = "Sale";
        }
        catch (Exception e)
        {
            System.out.println("Exception = "+e);
        }

    }

    private void clear() {
        products.clear();
        adapter.notifyDataSetChanged();

        subTotalT.setText("0.00");
        gstT.setText("0.00");
        grandTotalT.setText("0.00");
        discountValue.setText("0.00");


        for(int i=0;i<Store.getInstance().SOPendingList.size();i++)
        {
            if(Store.getInstance().SOPendingList.get(i).getId() == currentSaleOrder.getId())
            {  System.out.println("Removed SO Id "+Store.getInstance().SOPendingList.get(i).getId());
                Store.getInstance().SOPendingList.remove(i);

                setBillIdSpinner();

            }
        }

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
                genderList.add(customerList.get(i).getId()+" - "+customerList.get(i).getLedger().getLedgerName());
            }
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(STOSOActivity.this, android.R.layout.simple_spinner_item, genderList);

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
                System.out.println("-------------------------"+currentCustomer.getLedger().getId());

                setBillIdSpinner();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                customer = genderList.get(0);
                customerPosition  = 0;
                customerId = customerList.get(0).getId();
                currentCustomer = customerList.get(0);
                setBillIdSpinner();
            }


        });



    }
    public void setBillIdSpinner()
    {


        currentSaleType = "order";
        final ArrayList<String> BillIdLabel = new ArrayList<String>();
        int size =0;
        final ArrayList<SOPending> soPendings = new ArrayList<SOPending>();
        for(int i=0;i<Store.getInstance().SOPendingList.size();i++)
        {
            if(Store.getInstance().SOPendingList.get(i).getLedgerId() == currentCustomer.getLedger().getId())
            {
                System.out.println("-------------------------"+Store.getInstance().SOPendingList.get(i).getDiscountAmount());
                soPendings.add(Store.getInstance().SOPendingList.get(i));
                BillIdLabel.add(Store.getInstance().SOPendingList.get(i).getRefNo());
            }
        }
        size = soPendings.size();

        if(size == 0)
        {
            products.clear();
            products.addAll(new ArrayList<Product>());
            adapter.notifyDataSetChanged();

            subTotalT.setText(String.valueOf("0.00"));
            gstT.setText(String.valueOf("0.00"));
            grandTotalT.setText(String.valueOf("0.00"));

            discountValue.setText(String.valueOf("0.00"));

        }




        // Drop down layout style - list view with radio button
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(STOSOActivity.this,BillIdLabel);
        Store.getInstance().reprintSpinnerText.setTextSize(20);
        billID.setAdapter(customSpinnerAdapter);


        ArrayList<SOPending> finalSaleOrder = new ArrayList<SOPending>();

        finalSaleOrder = soPendings;





        billID.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),BillIdLabel.get(position),Toast.LENGTH_SHORT).show();
                billIDValue = BillIdLabel.get(position);


                    currentSaleOrder = soPendings.get(position);

                    System.out.println("========="+soPendings.get(position).getDiscountAmount());
                System.out.println("========="+soPendings.get(position).getDiscountAmount());

                    products.clear();
                    ArrayList<SalesOrderDetails> list = (ArrayList<SalesOrderDetails>) currentSaleOrder.getSODetails();




                    for(int i=0;i<list.size();i++)
                    {

                        Product product = new Product();
                        product.setId(list.get(i).getId());

                        for(int x=0;x<Store.getInstance().productList.size();x++)
                        {
                            if(list.get(i).getProductId() == Store.getInstance().productList.get(x).getId())
                            {
                                Log.d("SO PRODUCT UOM ID", String.valueOf(list.get(i).getUOMId()));

                            }
                        }
                        product.setProductName(list.get(i).getProductName());

                        if(product.getProductName()!=null)
                        {
                            product.setProductName(list.get(i).getProductName());
                        }
                        else {
                            product.setProductName("GHEE 400M");
                        }
                        product.setMRP(list.get(i).getUnitPrice());
                        product.setSellingRate(list.get(i).getUnitPrice());
                        product.setAmount(list.get(i).getUnitPrice());
                        product.setDiscountAmount(list.get(i).getDiscountAmount());
                        product.setQty((long) list.get(i).getQuantity());
                        product.setUOM(String.valueOf(list.get(i).getUOMId()));
                        products.add(product);

                    }

                    adapter.notifyDataSetChanged();
                    System.out.println("Sale order Size"+products.size());


                    generateBill();



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                billIDValue = BillIdLabel.get(0);


                if(soPendings.size()>0) {
                    currentSaleOrder = soPendings.get(0);
                    products.clear();
                    ArrayList<SalesOrderDetails> list = (ArrayList<SalesOrderDetails>) currentSaleOrder.getSODetails();

                    for (int i = 0; i < list.size(); i++) {


                        Product product = new Product();
                        product.setId(list.get(i).getId());
                        if(product.getProductName()!=null)
                        {
                            product.setProductName(list.get(i).getProductName());
                        }
                        else {
                            product.setProductName("GHEE 400M");
                        }

                        product.MRP = list.get(i).getAmount();


                        product.setMRP(list.get(i).getAmount());
                        product.setSellingRate(list.get(i).getAmount());
                        product.setAmount(list.get(i).getAmount());
                        product.setDiscountAmount(list.get(i).getDiscountAmount());
                        product.setQty((long) list.get(i).getQuantity());
                        product.setUOM(String.valueOf(list.get(i).getUOMId()));
                        products.add(product);



                    }

                    adapter.notifyDataSetChanged();
                    System.out.println("Sale order Size" + products.size());

                    generateBill();
                }

            }


        });





    }
    public void generateBill()
    {
        try {
            subTotal = 0;
            gst = 0;
            grandTotal = 0;
            fromCustomerValue = 0;
            tenderAmountValue = 0;


            double st = currentSaleOrder.getItemAmount() + currentSaleOrder.getGSTAmount() - currentSaleOrder.getDiscountAmount();

            double rec = currentSaleOrder.getTotalAmount() - currentSaleOrder.getExtraAmount();

            subTotal = bizRound(currentSaleOrder.getItemAmount(), 2);
            gst = bizRound(currentSaleOrder.getGSTAmount(), 2);
            grandTotal = bizRound(currentSaleOrder.getTotalAmount(), 2);
            fromCustomerValue = bizRound(rec, 2);
            tenderAmountValue = bizRound(currentSaleOrder.getExtraAmount(), 2);


            double gst = currentSaleOrder.getItemAmount() * 0.06;
            subTotalT.setText(String.valueOf(String.format("%.2f", currentSaleOrder.getItemAmount())));
            gstT.setText(String.valueOf(String.format("%.2f", gst)));
            grandTotalT.setText(String.valueOf(String.format("%.2f", currentSaleOrder.getTotalAmount())));
            discountValue.setText(String.valueOf(String.format("%.2f", currentSaleOrder.getDiscountAmount())));

            double a = (currentSaleOrder.getDiscountAmount() * 100);
            double b = currentSaleOrder.getTotalAmount();
            double dp = a / b;

            discountLabel.setText("Discount(" + String.valueOf(String.format("%.2f", dp)) + "%) =");
        }catch (Exception e)
        {
            System.out.println("Exception =="+e);
        }

    }
    /*public void print(Customer customer, String type, ArrayList<Product> products, Sale sale, SaleOrder saleOrder, SaleReturn saleReturn) {
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
        Customer customer1 = currentCustomer;


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

        customer = currentCustomer;


        String gstSpace = "";


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
            String itemP = String.valueOf(String.format("%.2f",item.getSellingRate()));


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
        double disAmt = 0.0;
        if(sale!=null)
        {
            mainGrantTotal = sale.getGrandTotal();
            ba = String.valueOf(sale.getBalance());
            discountPer = String.format("%.2f",sale.getDiscountPercentage());
            disType = sale.getDiscountType();
            disAmt = sale.getDiscountValue();

        }
        if(saleOrder!=null)
        {
            mainGrantTotal = saleOrder.getGrandTotal();
            ba = String.valueOf(saleOrder.getBalance());
            discountPer = String.format("%.2f",saleOrder.getDiscountPercentage());
            disType = saleOrder.getDiscountType();
            disAmt = saleOrder.getDiscountValue();
        }
        if(saleReturn!=null)
        {
            mainGrantTotal = saleReturn.getGrandTotal();
            ba = String.valueOf(saleReturn.getBalance());
            disAmt = saleReturn.getDiscountValue();
        }
        if(disAmt>0)
        {

            BTPrint.PrintTextLine("Discount("+discountPer+"%) RM " + discountSpace + String.format("%.2f",disV));
        }
        ba = String.format("%.2f",Double.parseDouble(ba));
        if(roundOffValue>0) {
            BTPrint.PrintTextLine("Round Off RM " + roundOffSpace + String.format("%.2f", roundOffValue));
        }
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



    }*/
}

