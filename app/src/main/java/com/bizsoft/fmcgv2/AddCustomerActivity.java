package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Ledger;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.Network;

import static com.bizsoft.fmcgv2.dataobject.Store.currentAccGrpId;
import static com.bizsoft.fmcgv2.dataobject.Store.currentAccGrpName;

public class AddCustomerActivity extends AppCompatActivity {

    Button save,clear;
    EditText customerName,personIncharge,addressLine1,addressLine2,city,mobileNumber,gstNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer);
        getSupportActionBar().setTitle("Add Customer");

        customerName = (EditText) findViewById(R.id.dealer_name);
        personIncharge = (EditText) findViewById(R.id.person_incharge);
        addressLine1 = (EditText) findViewById(R.id.address_line_one);
        addressLine2 = (EditText) findViewById(R.id.address_line_two);
        city = (EditText) findViewById(R.id.state);
        mobileNumber = (EditText) findViewById(R.id.mobile_number);
        gstNumber = (EditText) findViewById(R.id.gst_number);

        save = (Button) findViewById(R.id.save);
        clear = (Button) findViewById(R.id.clear);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                customerName.setText("");
                personIncharge.setText("");
                addressLine1.setText("");
                addressLine2.setText("");
                city.setText("");
                mobileNumber.setText("");
                gstNumber.setText("");
            }
        });




    }
    public  void validate()
    {
        boolean status = true;

        if(TextUtils.isEmpty(customerName.getText().toString()))
        {
            customerName.setError("Field cannot be empty");
            status = false;
        }
        if(TextUtils.isEmpty(personIncharge.getText().toString()))
        {
            status = false;
            personIncharge.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(addressLine1.getText().toString()))
        {
            status = false;
            addressLine1.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(addressLine2.getText().toString()))
        {
            status = false;
            addressLine2.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(city.getText().toString()))
        {
            status = false;
            city.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(mobileNumber.getText().toString()))
        {
            status = false;
            mobileNumber.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(gstNumber.getText().toString()))
        {
            status = false;
            gstNumber.setError("Field cannot be empty");
        }
        else
        {
            if(gstNumber.getText().toString().length()!=15)
            {
                status = false;
                gstNumber.setError("Not a valid GST number - 15 Digits Required..");
            }
        }
        if(status)
        {
    Network network = Network.getInstance(AddCustomerActivity.this);
            System.out.println("===="+network.isOnline());

            if(!network.isOnline())
            {
                Toast.makeText(this, "No network..saved offline", Toast.LENGTH_SHORT).show();

                createCustomer();
             //   Network.Check_Internet(AddCustomerActivity.this,"Network Offline","Please enable data connection");
            }
            else
            {
                Toast.makeText(this, "saved offline", Toast.LENGTH_SHORT).show();
                //new SaveCustomer(AddCustomerActivity.this).execute();
                createCustomer();
            }


           // new SaveCustomer(AddCustomerActivity.this).execute();
        }
        else
        {
            //do nothing

            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }

    }

    public void createCustomer()
    {
        Customer customer = new Customer();
        customer.setLedgerName(customerName.getText().toString());
        customer.setPersonIncharge(personIncharge.getText().toString());
        customer.setAddressLine1(addressLine1.getText().toString());
        customer.setAddressLine2(addressLine2.getText().toString());
        customer.setCityName(city.getText().toString());
        customer.setMobileNo(mobileNumber.getText().toString());
        customer.setGSTNo(gstNumber.getText().toString());


        Ledger ledger = new Ledger();
        ledger.setLedgerName(customerName.getText().toString());
        ledger.setPersonIncharge(personIncharge.getText().toString());
        ledger.setAddressLine1(addressLine1.getText().toString());
        ledger.setAddressLine2(addressLine2.getText().toString());
        ledger.setCityName(city.getText().toString());
        ledger.setMobileNo(mobileNumber.getText().toString());
        ledger.setGSTNo(gstNumber.getText().toString());



        System.out.println("Account group Id "+currentAccGrpId);
        System.out.println("Account group name "+currentAccGrpName);
        ledger.setAccountGroupId(currentAccGrpId);
        ledger.setAccountName(currentAccGrpName);


        customer.setLedger(ledger);
        Store.getInstance().customerList.add(customer);



        Intent intent = new Intent(AddCustomerActivity.this,CustomerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
