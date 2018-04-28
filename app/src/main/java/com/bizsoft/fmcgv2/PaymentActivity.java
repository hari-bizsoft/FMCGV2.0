package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.BTLib.BTDeviceList;
import com.bizsoft.fmcgv2.BTLib.BTPrint;
import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Payment;
import com.bizsoft.fmcgv2.dataobject.Sale;
import com.bizsoft.fmcgv2.dataobject.SaleReturn;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.bizsoft.fmcgv2.DashboardActivity.BLUETOOTH_FLAG;

public class PaymentActivity extends AppCompatActivity {

    Spinner customerSpinner,paymentModeSpinner;
    TextView openingBalance,outStandingAmount;
    Button print;
    private String customer;
    private int customerPosition;
    private Long customerId;
    private Customer currentCustomer;
    Double openingBalanceValue;
    private String paymentModeValue;
    private double outStandingAmountValue;
    EditText toCustomer;
    Button save;
    double x =0;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private static final int REQUEST_PERMISSION_SETTING1 = 102;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        customerSpinner = (Spinner) findViewById(R.id.customer_spinner);
        paymentModeSpinner = (Spinner) findViewById(R.id.payment_mode_spinner);
        openingBalance = (TextView) findViewById(R.id.opening_balance);
        outStandingAmount = (TextView) findViewById(R.id.outstanding_amount);
        print = (Button) findViewById(R.id.dc_print);
        toCustomer = (EditText) findViewById(R.id.to_customer);
        save = (Button) findViewById(R.id.save);
        outStandingAmount.setText(String.valueOf(String.format("%.2f", openingBalanceValue)));
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Payment payment = new Payment();

                String temp = toCustomer.getText().toString();

                x = 0;
                if (TextUtils.isEmpty(temp)) {
                    x = 0;
                }
                else {
                    x = Double.parseDouble(toCustomer.getText().toString());
                }

                payment.setAmount(x);
                if (currentCustomer.getId() != null) {
                    payment.setLegerId(currentCustomer.getId());
                }
                payment.setPaymentMode(paymentModeValue);


                Store.getInstance().payments.add(payment);
                currentCustomer.getPayments().add(payment);

                Toast.makeText(PaymentActivity.this, "Saved...", Toast.LENGTH_SHORT).show();
            }
        });

        toCustomer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("----------"+s.toString());



                if(toCustomer.getText()!= null)
                {
                    double balance =0;
                    if(!toCustomer.getText().toString().equals("") || TextUtils.isEmpty(toCustomer.getText().toString()))
                    {

                        double gt = openingBalanceValue;
                        double fc = 0;
                        try {
                            fc = Double.parseDouble(toCustomer.getText().toString());
                            balance = gt+fc;
                        }
                        catch (Exception e)
                        {
                            fc = 0;
                            balance = 0;
                        }



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
                        Intent intent = new Intent(PaymentActivity.this, BTDeviceList.class);

                        startActivityForResult(intent, BLUETOOTH_FLAG);

                        Toast.makeText(PaymentActivity.this, "new socket", Toast.LENGTH_SHORT).show();


                    } else {
                        print(currentCustomer);
                    }

                } catch (Exception e) {

                }
            }
        });
    }
    public void setCustomerSpinner()
    {
        final List<String> genderList = new ArrayList<String>();


        final ArrayList<Customer> customerList = Store.getInstance().customerList;


        for(int i=0;i<customerList.size();i++)
        {
            genderList.add(customerList.get(i).getId()+" - "+customerList.get(i).getLedgerName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(PaymentActivity.this, android.R.layout.simple_spinner_item, genderList);

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
                openingBalanceValue = currentCustomer.getOPBal();

                openingBalance.setText(String.valueOf(openingBalanceValue));
                outStandingAmount.setText(String.valueOf(calculateOutStanding()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                customer = genderList.get(0);
                customerPosition  = 0;
                customerId = customerList.get(0).getId();
                currentCustomer = customerList.get(0);


                openingBalanceValue = currentCustomer.getOPBal();

                openingBalance.setText(String.valueOf(openingBalanceValue));
                outStandingAmount.setText(String.valueOf(calculateOutStanding()));
            }


        });



    }
    public void setPaymentMode()
    {


        final ArrayList<String> genderList = new ArrayList<String>();
        genderList.add("Cash");
        genderList.add("Cheque");




        // Drop down layout style - list view with radio button
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(PaymentActivity.this,genderList);
        paymentModeSpinner.setAdapter(customSpinnerAdapter);

        paymentModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),genderList.get(position),Toast.LENGTH_SHORT).show();
                paymentModeValue = genderList.get(position);

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


        if(customer1.getId()==null)
        {
            BTPrint.PrintTextLine("Customer ID :"+"Unregistered");
        }
        else
        {
            BTPrint.PrintTextLine("Customer ID :"+customer1.getId());
        }

        BTPrint.PrintTextLine("Customer Name :"+customer1.getLedgerName());
        BTPrint.PrintTextLine("Person In Charge :"+customer1.getPersonIncharge());
        BTPrint.PrintTextLine("GST No :"+customer1.getGSTNo());


        BTPrint.PrintTextLine("------------------------------");


        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***Payment DETAILS***");
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine("Payment Mode :"+paymentModeValue);
        BTPrint.PrintTextLine("Given   : RM "+x);
        BTPrint.PrintTextLine("Outstanding: RM "+outStandingAmountValue);






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


}
