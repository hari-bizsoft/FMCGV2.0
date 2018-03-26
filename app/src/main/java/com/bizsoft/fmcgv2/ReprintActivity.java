package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.adapter.SalesAdapter;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Sale;
import com.bizsoft.fmcgv2.dataobject.SaleOrder;
import com.bizsoft.fmcgv2.dataobject.SaleReturn;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.bizsoft.fmcgv2.DashboardActivity.BLUETOOTH_FLAG;

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
    FloatingActionButton menu;
    TextView discountValue;
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
        print = (Button) findViewById(R.id.print);
        balance = (TextView) findViewById(R.id.balance_amount);
        received = (TextView) findViewById(R.id.received_amount);
        billID = (Spinner) findViewById(R.id.bill_id);
        menu  = (FloatingActionButton) findViewById(R.id.menu);
        discountValue = (TextView) findViewById(R.id.discount_value);
        bizUtils = new BizUtils();

        getSupportActionBar().setTitle("Reprint Bills");
        df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);
        setCustomerSpinner();
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bizUtils.showMenu(ReprintActivity.this);
            }
        });

            adapter = new SalesAdapter(ReprintActivity.this,products);
            listView.setAdapter(adapter);
        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    if(BTPrint.btsocket==null)
                    {
                        Intent intent = new Intent(ReprintActivity.this,BTDeviceList.class);

                        startActivityForResult(intent,BLUETOOTH_FLAG);

                        Toast.makeText(ReprintActivity.this, "new connection", Toast.LENGTH_SHORT).show();


                    }
                    else
                    {


                        Customer customer = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);


                        System.out.println("Sales List "+customer.getSale().size());
                        System.out.println("Sales Order List "+customer.getSaleOrder().size());

                        if(currentSaleType.toLowerCase().contains("order"))
                        {
                            if( (customer.getSaleOrder().size()>0))
                            {
                                Toast.makeText(ReprintActivity.this, "Printing", Toast.LENGTH_SHORT).show();
                                print(currentCustomer, "Sale Order Bill", customer.getSaleOrder(),currentSaleOrder.getPaymentMode());
                            }
                        }
                        else
                        if(currentSaleType.toLowerCase().contains("return"))
                        {
                            if( (customer.getSaleReturn().size()>0))
                            {
                                Toast.makeText(ReprintActivity.this, "Printing", Toast.LENGTH_SHORT).show();
                                print(currentCustomer, "Sale return Order Bill", customer.getSaleReturn(),currentSaleReturn.getPaymentMode());
                            }
                        }
                        else  {
                            if( (customer.getSale().size()>0)) {
                                Toast.makeText(ReprintActivity.this, "Printing", Toast.LENGTH_SHORT).show();
                                print(currentCustomer, "Sale Bill", customer.getSale(),currentSales.getPaymentMode());
                            }
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
                currentSaleType = genderList.get(0);
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
                genderList.add(customerList.get(i).getId()+" - "+customerList.get(i).getLedger().getLedgerName());
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

                genderList.add(currentCustomer.getSaleOrdersOfCustomer().get(i).getTempId());
            }

        }
        else
        if(currentSaleType.toLowerCase().contains("return")) {
            saleReturn = new ArrayList<SaleReturn>();
            saleReturn = currentCustomer.getSaleReturnOfCustomer();
            size = saleReturn.size();

            for(int i=0;i<size;i++) {

                genderList.add(currentCustomer.getSaleReturnOfCustomer().get(i).getTempId());
            }

        }
        else {
            sale = new ArrayList<Sale>();
            sale = currentCustomer.getSalesOfCustomer();
            size = sale.size();

            for(int i=0;i<size;i++) {
                genderList.add(currentCustomer.getSalesOfCustomer().get(i).getTempId());
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
                    currentSaleOrder = finalSaleOrder.get(position);
                    products.clear();
                    products.addAll(currentSaleOrder.getProducts());
                    adapter.notifyDataSetChanged();
                    System.out.println("Sale order Size"+products.size());

                    generateBill();
                }
                else
                if(currentSaleType.toLowerCase().contains("return")) {

                    currentSaleReturn = finalSaleReturn.get(position);
                    products.clear();
                    products.addAll(currentSaleReturn.getProducts());
                    adapter.notifyDataSetChanged();
                    generateBill();
                    System.out.println("Sale  Return Size"+products.size());
                }
                else
                {
                    currentSales = finalSale.get(position);
                    products.clear();
                    products.addAll(currentSales.getProducts());
                    adapter.notifyDataSetChanged();
                    generateBill();
                    System.out.println("Sale  Size"+products.size());

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                billIDValue = genderList.get(0);


                if(currentSaleType.toLowerCase().contains("order"))
                {
                    currentSaleOrder = finalSaleOrder.get(0);
                    products.clear();
                    products.addAll(currentSaleOrder.getProducts());
                    adapter.notifyDataSetChanged();
                    generateBill();
                    System.out.println("Sale order Size"+products.size());
                }
                else
                if(currentSaleType.toLowerCase().contains("return")) {

                    currentSaleReturn = finalSaleReturn.get(0);
                    products.clear();
                    products.addAll(currentSaleReturn.getProducts());
                    adapter.notifyDataSetChanged();
                    generateBill();
                    System.out.println("Sale  Return Size"+products.size());
                }
                else
                {
                    currentSales = finalSale.get(0);
                    products.clear();
                    products.addAll(currentSales.getProducts());
                    adapter.notifyDataSetChanged();
                    generateBill();
                    System.out.println("Sale  Size"+products.size()+"=========");

                }

            }


        });





    }
    public void generateBill()
    {
         subTotal = 0;
         gst = 0;
         grandTotal = 0;
         fromCustomerValue = 0;
        tenderAmountValue = 0;



        if(currentSaleType.toLowerCase().contains("order"))
        {

            subTotal = bizRound(currentSaleOrder.getReceivedAmount(),2);
            gst = bizRound(currentSaleOrder.getGst(),2);
            grandTotal = bizRound(currentSaleOrder.getGrandTotal(),2);
            fromCustomerValue = bizRound(currentSaleOrder.getReceivedAmount(),2);
            tenderAmountValue = bizRound(currentSaleOrder.getBalance(),2);




            subTotalT.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getSubTotal())));
            gstT.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getGst())));
            grandTotalT.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getGrandTotal())));
            discountValue.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getDiscountValue())));
            received.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getReceivedAmount())));

            balance.setText(String.valueOf(String.format("%.2f",currentSaleOrder.getBalance())));

        }
        else
        if(currentSaleType.toLowerCase().contains("return"))
        {
            received.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getReceivedAmount())));

            balance.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getBalance())));
            subTotalT.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getSubTotal())));
            gstT.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getGst())));
            grandTotalT.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getGrandTotal())));
            discountValue.setText(String.valueOf(String.format("%.2f",currentSaleReturn.getDiscountValue())));
        }
        else
        {

            received.setText(String.valueOf(String.format("%.2f",currentSales.getReceivedAmount())));

            balance.setText(String.valueOf(String.format("%.2f",currentSales.getBalance())));
            subTotalT.setText(String.valueOf(String.format("%.2f",currentSales.getSubTotal())));
            gstT.setText(String.valueOf(String.format("%.2f",currentSales.getGst())));
            grandTotalT.setText(String.valueOf(String.format("%.2f",currentSales.getGrandTotal())));
            discountValue.setText(String.valueOf(String.format("%.2f",currentSales.getDiscountValue())));
        }


    }
    public void print(Customer customer, String type, ArrayList<Product> products, String paymentMode)
    {
        SharedPreferences prefs = getSharedPreferences(Store.getInstance().MyPREFERENCES, MODE_PRIVATE);
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine(prefs.getString(getString(R.string.companyName), "Aboorvass"));
        BTPrint.SetAlign(Paint.Align.CENTER);


        System.out.println("company address line 1 =============================="+prefs.getString(getString(R.string.companyAddressLine1), "0"));
        System.out.println("company address line 2 =============================="+prefs.getString(getString(R.string.companyAddressLine2), "0"));
        System.out.println("company gst =============================="+prefs.getString(getString(R.string.gstNo), "0"));
        System.out.println("company mail =============================="+prefs.getString(getString(R.string.email), "0"));
        System.out.println("company postalcode =============================="+prefs.getString(getString(R.string.postal_code), "0"));

        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine(prefs.getString(getString(R.string.companyAddressLine1), "0")+","+prefs.getString(getString(R.string.companyAddressLine2), "0")+","+prefs.getString(getString(R.string.postal_code),"+1234556789"));

        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("E-Mail: "+prefs.getString(getString(R.string.email),"+1234556789"));
        BTPrint.PrintTextLine("GST No: "+prefs.getString(getString(R.string.gstNo),"+1234556789"));
        BTPrint.PrintTextLine("Ph No: "+prefs.getString(getString(R.string.phoneNumber),"+1234556789"));
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("***Bill Details***");
        BizUtils bizUtils = new BizUtils();
        BTPrint.PrintTextLine("Bill ID :"+String.valueOf(Store.getInstance().companyID+"-"+ Store.getInstance().dealerId+"-"+customer.getId()));
        BTPrint.PrintTextLine("Bill Date :"+bizUtils.getCurrentTime());
        BTPrint.PrintTextLine("------------------------------");
        Customer customer1 = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);

        BTPrint.PrintTextLine("Customer ID :"+customer1.getId());
        BTPrint.PrintTextLine("Customer Name :"+customer1.getLedgerName());
        BTPrint.PrintTextLine("Person In Charge :"+customer1.getPersonIncharge());
        BTPrint.PrintTextLine("GST No :"+customer1.getGSTNo());


        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Sale/Sale Order:"+type);
        BTPrint.PrintTextLine("Payment Mode :"+paymentMode);
        BTPrint.PrintTextLine("------------------------------");

        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***ITEM DETAILS***");
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine("NAME     QTY    PRICE   AMOUNT ");

        customer = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);


        String gstSpace = "";

        System.out.println("Sale list"+customer.getSale().size());
        double mainSubTotal =0;
        double mainGst = 0;
        double mainGrantTotal = 0;

        for(int i=0;i<products.size();i++) {
            Product item = products.get(i);
            String in;
            String iq = "";
            String ip = "";
            String ir = "";

            String itemnameSpace = "";
            String itemquantitySpace = "";
            String itempriceSpace = "";
            String itemrateSpace = "";
            int spaceLength = 0;
            int itemQspaceL = 0;
            int itemPspaceL = 0;
            int itemRspaceL = 0;


            if(item.getProductName().length()>=10)
            {
                in = item.getProductName().substring(0,9);
                itemnameSpace = " ";
            }
            else
            {
                int total = item.getProductName().length();
                spaceLength = 10 - total;

                for(int x=0;x<spaceLength;x++)
                {
                    itemnameSpace = itemnameSpace + " ";
                }



                in = item.getProductName();

            }

            if(String.valueOf(item.getQty()).length()>=6)
            {
                iq = String.valueOf(item.getQty()).substring(0,4);
                iq = iq + "..";
            }
            else
            {
                int total = String.valueOf(item.getQty()).length();
                itemQspaceL = 6 - total;

                for(int x=0;x<itemQspaceL;x++)
                {
                    itemquantitySpace = itemquantitySpace+ " ";
                }

                iq = String.valueOf(item.getQty());
            }
            String itemP = String.valueOf(item.getMRP());


            if(itemP.length()>=8)
            {
                in = itemP.substring(0,7);
                itemrateSpace = " ";

            }
            else
            {
                int total =itemP.length();
                itemPspaceL = 8 - total;

                for(int x=0;x<itemPspaceL;x++)
                {
                    itempriceSpace = itempriceSpace + " ";
                }



                ip = itemP;

            }
            double subTotalx =0;
            double gstx = 0;
            if(String.valueOf(item.getQty()*item.getMRP()).length()>=6)
            {
                ir = String.valueOf(item.getQty()*item.getMRP()).substring(0,4);
                ir = ir + "..";

                subTotalx = item.getQty()*item.getMRP();
            }
            else
            {
                int total = String.valueOf(item.getQty()*item.getMRP()).length();
                itemRspaceL = 6 - total;

                for(int x=0;x<itemRspaceL;x++)
                {
                    itemrateSpace = itemrateSpace+ " ";
                }

                ir = String.valueOf(item.getQty()*item.getMRP());
                subTotalx = subTotalx  + item.getQty()*item.getMRP();
            }





            mainSubTotal = mainSubTotal +  subTotalx;


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

            for(int f=0;f<c;f++)
            {
                gstSpace = gstSpace + " ";
            }


            double tt = subTotalx;

            mainGst = bizRound( mainGst,2);

            BTPrint.PrintTextLine(in+itemnameSpace+iq+itemquantitySpace+ip+itempriceSpace+ir);
        }


        String mgt = String.valueOf(String.format("%.2f", mainGrantTotal));
        String ra = String.valueOf(String.format("%.2f",fromCustomerValue));
        String ba = String.valueOf(String.format("%.2f",tenderAmountValue));

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

        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.RIGHT);
        BTPrint.PrintTextLine("Sub total = RM "+String.format("%.2f",subTotal));
        BTPrint.PrintTextLine("GST = RM "+gstSpace+String.format("%.2f",gst));
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Grand total = RM "+String.format("%.2f",grandTotal));
        BTPrint.PrintTextLine("Received amount =" + raSpace + " RM " + String.format("%.2f",fromCustomerValue));
        BTPrint.PrintTextLine("Balance amount =" + baSpace + " RM " + String.format("%.2f",tenderAmountValue));
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("*****THANK YOU*****");
        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Dealer Name:"+ Store.getInstance().dealerName);
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("Powered By Denariu Soft SDN BHD");

        BTPrint.printLineFeed();



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("Result code === "+resultCode);
        System.out.println("requestCode === "+requestCode);
        System.out.println("data === "+data);



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
