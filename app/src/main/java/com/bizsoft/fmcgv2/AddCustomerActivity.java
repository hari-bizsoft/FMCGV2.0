package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bizsoft.fmcgv2.Tables.Bank;
import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.adapter.CustomerAdapter;
import com.bizsoft.fmcgv2.dataobject.CreditLimitType;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Ledger;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.Network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static com.bizsoft.fmcgv2.CustomerActivity.AllCustomerList;
import static com.bizsoft.fmcgv2.CustomerActivity.CustomerList;
import static com.bizsoft.fmcgv2.CustomerActivity.customerAdapter;
import static com.bizsoft.fmcgv2.DashboardActivity.customerNameListAdapter;
import static com.bizsoft.fmcgv2.dataobject.Store.currentAccGrpId;
import static com.bizsoft.fmcgv2.dataobject.Store.currentAccGrpName;
import static com.bizsoft.fmcgv2.service.BizUtils.isValidEmail;

public class AddCustomerActivity extends AppCompatActivity {

    Button save,clear;
    EditText customerName,personIncharge,addressLine1,addressLine2,city,mobileNumber,gstNumber;
    EditText telephoneNumber,email,opBalance;
    Spinner accType;
    String chooosedAcType;
    public static ArrayList<String> accTypeList;

    private String myAction;
    int position;
    private Customer customer;
    private CustomSpinnerAdapter accTypeListAdapter;

    EditText creditAmount,creditLimit;
    Spinner creditType;
    private static ArrayList<String> typeList;
    private String chooosedCreditType;
    private CustomSpinnerAdapter CreditTypeListAdapter;
    private CreditLimitType chooosedCreditTypeObj = null;
    Handler mainHandler ;
    private boolean customerNameValidator;

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
        telephoneNumber = (EditText) findViewById(R.id.telephone);
        email = (EditText) findViewById(R.id.email);
        opBalance = (EditText) findViewById(R.id.op_bal);
        accType = (Spinner) findViewById(R.id.acc_type);
        creditAmount = (EditText) findViewById(R.id.amount);
        creditLimit = (EditText) findViewById(R.id.credit_limit);
        creditType = (Spinner) findViewById(R.id.creditLimitType);

         mainHandler = new Handler(Looper.getMainLooper());

        save = (Button) findViewById(R.id.save);
        clear = (Button) findViewById(R.id.clear);

         setAccTypeList();

         setCreditTypeList();


        try {
            findAction();
        }
        catch (Exception e)
        {
            Log.e("Customer - findAction",e.toString());
        }
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    customerName.setText("");
                    personIncharge.setText("");
                    addressLine1.setText("");
                    addressLine2.setText("");
                    city.setText("");
                    mobileNumber.setText("");
                    gstNumber.setText("");
                    telephoneNumber.setText("");
                    email.setText("");
                    opBalance.setText("");
                    creditAmount.setText("");
                    creditLimit.setText("");
                    accType.setSelection(0);
                    creditType.setSelection(0);

                }catch (Exception e)
                {
                    Log.e("Clear","ex");
                }



            }
        });


        customerName.addTextChangedListener(new TextWatcher() {
            ArrayList<String> oldCustomerNames;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                oldCustomerNames = Customer.getCustomerNameList();
                customerNameValidator = true;
            }

            @Override
            public void onTextChanged(final CharSequence s, int start, int before, int count) {

                Runnable myRunnable = new Runnable() {
                    @Override
                    public void run() {

                      for(int i=0;i<oldCustomerNames.size();i++)
                      {
                          Log.d("test 1",oldCustomerNames.get(i));
                          Log.d("test 2",s.toString());
                          if(oldCustomerNames.get(i).equals(s.toString()))
                          {
                             customerName.setError("Already Taken..");
                              customerNameValidator = false;
                          }
                      }
                    }
                };
                mainHandler.post(myRunnable);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });




    }

    private void setCreditTypeList() {

       try {
           typeList = new ArrayList<String>();


           typeList.add("");
           for (int i = 0; i < Store.getInstance().customerCreditLimitTypeList.size(); i++) {
               typeList.add(Store.getInstance().customerCreditLimitTypeList.get(i).getLimitType());
           }
           chooosedCreditType = typeList.get(0);
           CreditTypeListAdapter = new CustomSpinnerAdapter(AddCustomerActivity.this,typeList);
           creditType.setAdapter(CreditTypeListAdapter);
           creditType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                   chooosedCreditType = typeList.get(position);

                   chooosedCreditTypeObj =  CreditLimitType.getByName(chooosedCreditType);
               }

               @Override
               public void onNothingSelected(AdapterView<?> parent) {

               }
           });

       }
       catch (Exception e)
       {

       }

    }

    private void setAccTypeList() {


        accTypeList = new ArrayList<String>();
        accTypeList.add("");
        accTypeList.add("Credit");
        accTypeList.add("Debit");

        chooosedAcType = accTypeList.get(0);


        accTypeListAdapter = new CustomSpinnerAdapter(AddCustomerActivity.this,accTypeList);
        accType.setAdapter(accTypeListAdapter);
        accType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                chooosedAcType = accTypeList.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void findAction() {

        myAction = getIntent().getExtras().getString("myAction");
        position = getIntent().getExtras().getInt("position");

        System.out.println("Action : "+myAction);
        if(myAction!=null)
        {
            if(myAction.equals("edit"))
            {

                customer  =    customerAdapter.customerList.get(position);

                setValues(customer);
                    getSupportActionBar().setTitle("Edit Customer");
            }
            else  if(myAction.equals("add"))
            {
                getSupportActionBar().setTitle("Add Customer");
            }

        }
    }

    private void setValues(Customer customer) {
        customerName.setText(String.valueOf(customer.getLedger().getLedgerName()));
        personIncharge.setText(String.valueOf(customer.getLedger().getPersonIncharge()));
        addressLine1.setText(String.valueOf(customer.getLedger().getAddressLine1()));
        addressLine2.setText(String.valueOf(customer.getLedger().getAddressLine2()));
        city.setText(String.valueOf(customer.getLedger().getCityName()));
        mobileNumber.setText(String.valueOf(customer.getLedger().getMobileNo()));
        gstNumber.setText(String.valueOf(customer.getLedger().getGSTNo()));
        telephoneNumber.setText(String.valueOf(customer.getLedger().getTelephoneNo()));
        email.setText(String.valueOf(customer.getLedger().getEMailId()));
        opBalance.setText(String.valueOf(customer.getLedger().getOPBal()));
        creditAmount.setText(String.valueOf(customer.getLedger().getCreditAmount()));
        creditLimit.setText(String.valueOf(customer.getLedger().getCreditLimit()));


        if(customer.getLedger().getACType()!=null) {
            for (int i = 0; i < accTypeList.size(); i++) {
                if (customer.getLedger().getACType().equals(accTypeList.get(i))) {
                    accType.setSelection(i);
                }

            }
        }
        if(customer.getLedger().getCreditLimitType()!=null)
        {
            for (int i = 0; i < Store.getInstance().customerCreditLimitTypeList.size(); i++) {
                if (customer.getLedger().getCreditLimitType().getId() == Store.getInstance().customerCreditLimitTypeList.get(i).getId()) {

                    for (int j = 0; j < typeList.size(); j++) {
                        if (Store.getInstance().customerCreditLimitTypeList.get(i).getLimitType().equals(typeList.get(j))) {
                            creditType.setSelection(j);
                        }
                    }
                }

            }

        }



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
        if(TextUtils.isEmpty(chooosedAcType))
        {
            status = false;
            Toast.makeText(this, "Please choose account type as Credit/Debit", Toast.LENGTH_SHORT).show();
        }
        if(!TextUtils.isEmpty(email.getText()))
        {
            if(!isValidEmail(email.getText()))
            {
                status = false;
                email.setError("Not a valid mail id");
            }

        }
        if(!myAction.equals("edit"))
        {
        if(!customerNameValidator)
        {
            status = false;
            Toast.makeText(this, "Customer name already taken", Toast.LENGTH_SHORT).show();
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
        Customer customer = null;


        if(myAction.equals("edit"))
        {
           Customer temp = customerAdapter.customerList.get(position);

            for(int i=0;i<Store.getInstance().customerList.size();i++)
            {
                if(temp.getId().compareTo(Store.getInstance().customerList.get(i).getId())==0)
                {
                    customer = Store.getInstance().customerList.get(i);
                    System.out.println("Current customer to edit = "+customer.getLedger().getLedgerName());
                }
            }


        }
        else
        {
            customer =  new Customer();
        }

        customer.setLedgerName(customerName.getText().toString());
        customer.setPersonIncharge(personIncharge.getText().toString());
        customer.setAddressLine1(addressLine1.getText().toString());
        customer.setAddressLine2(addressLine2.getText().toString());
        customer.setCityName(city.getText().toString());
        customer.setMobileNo(mobileNumber.getText().toString());
        customer.setGSTNo(gstNumber.getText().toString());
        Ledger ledger = null;
        if(myAction.equals("edit")) {
             ledger = customer.getLedger();
        }
        else
        {
            ledger = new Ledger();
        }
        ledger.setLedgerName(customerName.getText().toString());
        ledger.setPersonIncharge(personIncharge.getText().toString());
        ledger.setAddressLine1(addressLine1.getText().toString());
        ledger.setAddressLine2(addressLine2.getText().toString());
        ledger.setCityName(city.getText().toString());
        ledger.setMobileNo(mobileNumber.getText().toString());
        ledger.setGSTNo(gstNumber.getText().toString());
        ledger.setTelephoneNo(telephoneNumber.getText().toString());
        ledger.setEMailId(email.getText().toString());

        if( !TextUtils.isEmpty(opBalance.getText())) {
            if(opBalance.getText().toString()!=null) {
                ledger.setOPBal(Double.valueOf(opBalance.getText().toString()));
            }
        }
        ledger.setACType(chooosedAcType);



        System.out.println("Account group Id "+currentAccGrpId);
        System.out.println("Account group name "+currentAccGrpName);
        ledger.setAccountGroupId(currentAccGrpId);
        ledger.setAccountName(currentAccGrpName);

        if(chooosedCreditType!=null)
        {
            if(!TextUtils.isEmpty(chooosedCreditType))
            {
                ledger.CreditLimitType = new CreditLimitType();

                ledger.getCreditLimitType().setId(chooosedCreditTypeObj.getId());
                ledger.getCreditLimitType().setLimitType(chooosedCreditTypeObj.getLimitType());
            }
        }
        if(!TextUtils.isEmpty(creditAmount.getText()))
        {
            String d =creditAmount.getText().toString();

            try {
                ledger.setCreditAmount(Double.parseDouble(d));
            }catch (Exception e)
            {
                Log.d("exc","not a double");
            }
        }

        if(!TextUtils.isEmpty(creditLimit.getText()))
        {
            String d =creditLimit.getText().toString();

            try {
                ledger.setCreditLimit(Double.parseDouble(d));
            }catch (Exception e)
            {
                Log.d("exc","not a double");
            }
        }
        customer.setLedger(ledger);
        customer.setSynced(false);
        if(myAction.equals("edit"))
        {


         Log.d("Customer","Update List");

        }
        else {
            Store.getInstance().customerList.add(customer);
        }

        //Saving to local storage as JSON
        try {
            BizUtils.storeAsJSON("customerList",BizUtils.getJSON("customer",Store.getInstance().customerList));
            System.out.println("DB Updated..on local storage");

            Collections.sort(Store.getInstance().customerList, Customer.COMPARE_BY_LEDGER_NAME);

           customerNameListAdapter= new ArrayAdapter<String>(this,
                    android.R.layout.simple_dropdown_item_1line, Customer.getCustomerNameList());
            DashboardActivity.customerNameKey.setAdapter(customerNameListAdapter);
            customerNameListAdapter.notifyDataSetChanged();



            CustomerActivity.customerAdapter.notifyDataSetChanged();
            CustomerActivity.searchBar.setText("test");
            CustomerActivity.searchBar.setText("");




        } catch (ClassNotFoundException e) {

            System.err.println("Unable to write to DB");
        }
        Intent intent = new Intent(AddCustomerActivity.this,CustomerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }


}
