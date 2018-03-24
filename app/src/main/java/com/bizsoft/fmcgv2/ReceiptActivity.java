package com.bizsoft.fmcgv2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.BTLib.BTDeviceList;
import com.bizsoft.fmcgv2.BTLib.BTPrint;
import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Receipt;
import com.bizsoft.fmcgv2.dataobject.Sale;
import com.bizsoft.fmcgv2.dataobject.SaleReturn;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import static com.bizsoft.fmcgv2.DashboardActivity.BLUETOOTH_FLAG;

public class ReceiptActivity extends AppCompatActivity {

    Spinner customerSpinner,paymentModeSpinner;
    TextView  openingBalance,outStandingAmount;
    Button print;
    private String customer;
    private int customerPosition;
    private Long customerId;
    private Customer currentCustomer;
    Double openingBalanceValue;
    private String paymentModeValue;
    private double outStandingAmountValue;
    EditText fromCustomer;
    Button save;
    double x =0;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static final int REQUEST_PERMISSION_SETTING1 = 102;
    FloatingActionButton menu;
    private BizUtils bizUtils;
    private TextView cn;
    private TextView chequeNo,cdl,bnl;
    EditText chequeDate,bankName;
    ImageButton dateChooser;
    private int year,month,day;
    final static int BLUETOOTH_FLAG = 619;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        getSupportActionBar().setTitle("Receipt");
        menu  = (FloatingActionButton) findViewById(R.id.menu);
        bizUtils = new BizUtils();


        customerSpinner = (Spinner) findViewById(R.id.customer_spinner);
        paymentModeSpinner = (Spinner) findViewById(R.id.payment_mode_spinner);
        openingBalance = (TextView) findViewById(R.id.opening_balance);
        outStandingAmount = (TextView) findViewById(R.id.outstanding_amount);
        print = (Button) findViewById(R.id.print);
        fromCustomer = (EditText) findViewById(R.id.to_customer);
        save = (Button) findViewById(R.id.save);
        cn= (TextView) findViewById(R.id.cn);
        chequeNo = (EditText) findViewById(R.id.cheque_no);
        dateChooser = (ImageButton) findViewById(R.id.date_chooser);
        cdl= (TextView) findViewById(R.id.cdl);
        bnl = (TextView) findViewById(R.id.bnl);
        chequeDate = (EditText) findViewById(R.id.cheque_date);
        bankName = (EditText) findViewById(R.id.bank_name);







        dateChooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog(999);
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bizUtils.showMenu(ReceiptActivity.this);
            }
        });
        print.setVisibility(View.INVISIBLE);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate())
                {

                Receipt receipt = new Receipt();

                String temp = fromCustomer.getText().toString();

                x = 0;
                if (TextUtils.isEmpty(temp)) {
                    x = 0;
                }
                else {
                    x = Double.parseDouble(fromCustomer.getText().toString());
                }

                receipt.setAmount(x);
                if (currentCustomer.getId() != null) {
                    receipt.setLegerId(currentCustomer.getLedger().getId());
                }
                receipt.setPaymentMode(paymentModeValue);

                    if(paymentModeValue.toLowerCase().contains("cheque"))
                    {
                        receipt.setChequeNo(chequeNo.getText().toString());
                        receipt.setChequeDate(chequeDate.getText().toString());
                        receipt.setChequeBankName(bankName.getText().toString());
                    }
                    else
                    {
                        receipt.setChequeNo("null");
                        receipt.setChequeDate("null");
                        receipt.setChequeBankName("null");
                    }




                Store.getInstance().receipts.add(receipt);

                currentCustomer.getReceipts().add(receipt);

                //Saving to local storage as JSON
                    try {
                        BizUtils.storeAsJSON("customerList",BizUtils.getJSON("customer",Store.getInstance().customerList));
                        System.out.println("DB Updated..on local storage");
                    } catch (ClassNotFoundException e) {

                        System.err.println("Unable to write to DB");
                    }

                Toast.makeText(ReceiptActivity.this, "Saved...", Toast.LENGTH_SHORT).show();

                clearData();


                }

            }
        });

        System.out.println("OPBAL = "+openingBalanceValue);


        fromCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {





            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                outStandingAmount.setText(String.valueOf(String.format("%.2f", openingBalanceValue)));
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("----------"+s.toString());



                if(fromCustomer.getText()!= null)
                {
                    double balance =0;
                    if(!fromCustomer.getText().toString().equals("") || TextUtils.isEmpty(fromCustomer.getText().toString()))
                    {

                        double gt = openingBalanceValue;
                        double fc = 0;
                        try {

                            fc = Double.parseDouble(fromCustomer.getText().toString());

                            if(fc <= openingBalanceValue)
                            {
                                balance = gt-fc;
                            }






                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception =="+e);

                            balance = 0;
                        }


                        System.out.println("S----------------------->"+balance);

                        outStandingAmount.setText(String.valueOf(String.format("%.2f", balance)));


                    }
                    else
                    {
                        outStandingAmount.setText(String.valueOf(String.format("%.2f", openingBalanceValue)));
                    }

                }
                else
                {
                    outStandingAmount.setText(String.valueOf(String.format("%.2f", openingBalanceValue)));
                }
                if(TextUtils.isEmpty(s.toString()))
                {
                    outStandingAmount.setText(String.valueOf(String.format("%.2f", openingBalanceValue)));
                }

            }
        });



        setCustomerSpinner();
        setPaymentMode();

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    if (BTPrint.btsocket == null) {
                        Intent intent = new Intent(ReceiptActivity.this, BTDeviceList.class);

                        startActivityForResult(intent, BLUETOOTH_FLAG);

                        Toast.makeText(ReceiptActivity.this, "new socket", Toast.LENGTH_SHORT).show();


                    } else {
                        print(currentCustomer);
                    }

                } catch (Exception e) {

                }
            }
        });

        System.out.println("");
    }

    public  void print()
    {

        try {
            if (BTPrint.btsocket == null) {
                Intent intent = new Intent(ReceiptActivity.this, BTDeviceList.class);

                startActivityForResult(intent, BLUETOOTH_FLAG);

                Toast.makeText(ReceiptActivity.this, "new socket", Toast.LENGTH_SHORT).show();


            } else {
                print(currentCustomer);
            }

        } catch (Exception e) {

        }
    }

    private void clearData() {

        //openingBalance.setText("0.00");
        fromCustomer.setText("0.00");
        outStandingAmount.setText("0.00");

        chequeNo.setText("");
        chequeDate.setText("");
        bankName.setText("");

        Toast.makeText(this, "Receipt Saved.", Toast.LENGTH_SHORT).show();

        print();


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

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(ReceiptActivity.this, android.R.layout.simple_spinner_item, genderList);

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


                System.out.println("Ope");
                double tmp = 0;
                System.out.println("currentcustomer.getLedger().getOPBal()"+currentCustomer.getLedger().getOPBal());
                if(currentCustomer.getLedger().getOPBal()==null)
                {
                    tmp = 0;
                }
                else
                {
                    tmp= currentCustomer.getLedger().getOPBal();
                }

                openingBalanceValue = tmp;

                System.out.print("OP BAL = "+openingBalanceValue);
                openingBalance.setText(String.valueOf(String.format("%.2f",openingBalanceValue)));
                outStandingAmount.setText(String.valueOf(String.format("%.2f",openingBalanceValue)));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                customer = genderList.get(0);
                customerPosition  = 0;
                customerId = customerList.get(0).getId();
                currentCustomer = customerList.get(0);


                double tmp = 0;
                System.out.println("currentcustomer.getLedger().getOPBal()"+currentCustomer.getLedger().getOPBal());
                if(currentCustomer.getLedger().getOPBal()==null)
                {
                    tmp = 0;
                }
                else
                {
                    tmp= currentCustomer.getLedger().getOPBal();
                }

                openingBalanceValue = tmp;



                openingBalance.setText(String.valueOf(String.format("%.2f",openingBalanceValue)));
                outStandingAmount.setText(String.valueOf(String.format("%.2f",openingBalanceValue)));
            }

        });

    }
    public void setPaymentMode()
    {

        final ArrayList<String> genderList = new ArrayList<String>();
        genderList.add("Cash");
        genderList.add("Cheque");
     // Drop down layout style - list view with radio button
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(ReceiptActivity.this,genderList);
        paymentModeSpinner.setAdapter(customSpinnerAdapter);

        paymentModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),genderList.get(position),Toast.LENGTH_SHORT).show();
                paymentModeValue = genderList.get(position);
                if(paymentModeValue.contains("PNT") || paymentModeValue.toLowerCase().contains("cheque") )
                {
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
                    System.out.println("cash mode hiding chq no ...");
                    cn.setVisibility(View.GONE);
                    chequeNo.setVisibility(View.GONE);
                    bnl.setVisibility(View.GONE);
                    cdl.setVisibility(View.GONE);
                    bankName.setVisibility(View.GONE);
                    chequeDate.setVisibility(View.GONE);
                    dateChooser.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                paymentModeValue = genderList.get(0);


            }


        });




    }
    public double calculateOutStanding()
    {
        outStandingAmountValue =0;
        ArrayList<Sale> sales = currentCustomer.getSalesOfCustomer();
        ArrayList<SaleReturn> salesReturn = currentCustomer.getSaleReturnOfCustomer();

        Iterator<Sale> iterator = sales.iterator();
        while (iterator.hasNext()) {
            Sale sale = iterator.next();
            if(sale.getPaymentMode().contains("PNT"))
            {
                outStandingAmountValue = outStandingAmountValue + sale.getGrandTotal();
                System.out.println("PNT Exist"+currentCustomer.getId()+currentCustomer.getLedgerName());
            }

        }
        Iterator<SaleReturn> iterator1 = salesReturn.iterator();
        while (iterator1.hasNext()) {
            SaleReturn saleReturn = iterator1.next();
            if(saleReturn.getPaymentMode().contains("PNT"))
            {
                outStandingAmountValue = outStandingAmountValue + saleReturn.getGrandTotal();
                System.out.println("PNT Exist"+currentCustomer.getId()+currentCustomer.getLedgerName());
            }

        }
        return outStandingAmountValue;

    }

    public void print(Customer customer)
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

        Receipt receipt = customer1.getReceipts().get(customer1.getReceipts().size()-1);
        if(customer1.getId()==null)
        {
            BTPrint.PrintTextLine("Customer ID :"+"Unregistered");
        }
        else
        {
            BTPrint.PrintTextLine("Customer ID :"+customer1.getId());
        }


        BTPrint.PrintTextLine("Customer Name :"+customer1.getLedger().getLedgerName());
        BTPrint.PrintTextLine("Person In Charge :"+customer1.getLedger().getPersonIncharge());
        BTPrint.PrintTextLine("GST No :"+customer1.getLedger().getGSTNo());
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***RECEIPT DETAILS***");
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine("Payment Mode :"+receipt.getPaymentMode());


        BTPrint.PrintTextLine("Received     : RM "+String.format("%.2f",customer1.getLedger().getOPBal()));
        BTPrint.PrintTextLine("Received     : RM "+String.format("%.2f",receipt.getAmount()));

        BTPrint.PrintTextLine("Outstanding  : RM "+ String.format("%.2f",(customer1.getLedger().getOPBal()- receipt.getAmount())));
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
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
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

                chequeDate.setText(String.valueOf(date));
            Toast.makeText(ReceiptActivity.this, date, Toast.LENGTH_SHORT).show();
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
                chequeDate.setText(String.valueOf("Unspecified"));
            }

            if(TextUtils.isEmpty(bankName.getText().toString()) )
            {
                bankName.setText(String.valueOf("Unspecified"));
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

        String s  = fromCustomer.getText().toString();
        System.out.println("S-----"+s);
        if(TextUtils.isEmpty(s))
        {
            System.out.println("S-----"+"Empty");
            status = false;
            Toast.makeText(this, "Receive RM greater than 0 from Customer", Toast.LENGTH_SHORT).show();
        }
        else
        {
            System.out.println("S-----"+"non empty");
            if(Double.parseDouble(s)==0)
            {
                System.out.println("S-----"+"zero value");
                status = false;
                Toast.makeText(this, "Receive RM greater than 0 from Customer", Toast.LENGTH_SHORT).show();
            }

        }
        
        return  status;


    }


}
