/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bizsoft.fmcgv2.Tables.Bank;
import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.dataobject.Ledger;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.SignalRService;

import java.util.ArrayList;
import java.util.regex.Matcher;

import static com.bizsoft.fmcgv2.dataobject.Store.VALID_EMAIL_ADDRESS_REGEX;
import static com.bizsoft.fmcgv2.service.BizUtils.prettyJson;

public class CreateBankActivity extends AppCompatActivity {


    Button save,clear;
    EditText bankName,personIncharge,addressLine1,addressLine2,city,mobileNumber,telephoneNumber,email,opBalance,accNumber,accName;
    Spinner accType;
    //
    String chooosedAcType;
    private String myAction;
    private int position;
    private Bank bank;
    private ArrayList<String> accTypeList;
    private CustomSpinnerAdapter accTypeListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_bank);
        getSupportActionBar().setTitle("Add Bank");

        linkWithLayout();
        setAccTypeList();

        try {
            findAction();
        }
        catch (Exception e)
        {
            Log.e("findAction",e.toString());
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


                bankName.setText("");
                accName.setText("");
                personIncharge.setText("");
                addressLine1.setText("");
                addressLine2.setText("");
                city.setText("");
                mobileNumber.setText("");
                telephoneNumber.setText("");
                accNumber.setText("");
                email.setText("");
                opBalance.setText("0");
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

                bank =    BankActivity.adapter.bankLists.get(position);

                setValues(bank);
                getSupportActionBar().setTitle("Edit Bank");
            }
            else  if(myAction.equals("add"))
            {
                getSupportActionBar().setTitle("Add Bank");
            }

        }
    }

    private void setValues(Bank bank) {

        bankName.setText(String.valueOf(bank.getLedger().getLedgerName()));
        accNumber.setText(String.valueOf(bank.getAccountNo()));
        accName.setText(String.valueOf(bank.getBankAccountName()));
        personIncharge.setText(String.valueOf(bank.getLedger().getLedgerName()));
        //not compulsary
        addressLine1.setText(String.valueOf(bank.getLedger().getAddressLine1()));
        addressLine2.setText(String.valueOf(bank.getLedger().getAddressLine2()));
        city.setText(String.valueOf(bank.getLedger().getCityName()));
        mobileNumber.setText(String.valueOf(bank.getLedger().getMobileNo()));
        telephoneNumber.setText(String.valueOf(bank.getLedger().getTelephoneNo()));
        email.setText(String.valueOf(bank.getLedger().getEMailId()));
        opBalance.setText(String.valueOf(bank.getLedger().getOPBal()));


        if(bank.getLedger().getACType()!=null) {
            for (int i = 0; i < accTypeList.size(); i++) {
                if (bank.getLedger().getACType().equals(accTypeList.get(i))) {
                    accType.setSelection(i);
                }

            }
        }


    }

    private void validate() {

        boolean status = true;

        if(TextUtils.isEmpty(bankName.getText()))
        {
           bankName.setError("Please Fill..!");
           status = false;
        }
        if(TextUtils.isEmpty(personIncharge.getText()))
        {
            personIncharge.setError("Please Fill..!");
            status = false;
        }
        if(TextUtils.isEmpty(accNumber.getText()))
        {
            accNumber.setError("Please Fill..!");
            status = false;
        }

        if(TextUtils.isEmpty(accName.getText()))
        {
            accName.setError("Please Fill..!");
            status = false;
        }

        String regexStr = "^[0-9]*$";
        if(!accNumber.getText().toString().trim().matches(regexStr))
        {
            accNumber.setError("Please Enter a valid account number only..!");
            status = false;
        }
        if(!TextUtils.isEmpty(email.getText()))
        {
            if(!validateEmail(email.getText().toString()))
            {
                email.setError("Please Enter a valid mail id");
                status = false;
            }

        }



        if(status)
        {
            modelData();

        }



    }

    private void modelData() {


        Bank bank = null;

        if(myAction.equals("edit"))
        {
            bank = BankActivity.adapter.bankLists.get(position);

        }
        else
        {
             bank = new Bank();
        }
        bank.setAccountNo(accNumber.getText().toString());
        bank.setBankAccountName(accName.getText().toString());
        Ledger ledger = new Ledger();
        ledger.setLedgerName(bankName.getText().toString());

        ledger.setPersonIncharge(personIncharge.getText().toString());
        //not compulsary
        ledger.setAddressLine1(addressLine1.getText().toString());
        ledger.setAddressLine2(addressLine2.getText().toString());
        ledger.setCityName(city.getText().toString());
        ledger.setMobileNo(mobileNumber.getText().toString());
        ledger.setTelephoneNo(telephoneNumber.getText().toString());
        ledger.setEMailId(email.getText().toString());
        if(!TextUtils.isEmpty(opBalance.getText()))
        {
            ledger.setOPBal(Double.valueOf(opBalance.getText().toString()));

        }


        ledger.setAccountGroupId(Store.getInstance().bankAccountGroupId);

        try
        {
            double op = Double.parseDouble(opBalance.getText().toString());
            ledger.setOPBal(Double.valueOf(op));

        }
        catch (Exception e)
        {

        }


        ledger.setACType(chooosedAcType);
        bank.setLedger(ledger);
        bank.setSynced(false);

        prettyJson("",bank);

        Store.getInstance().newBankList.add(bank);


        try {
            BizUtils.storeAsJSON("NewBankList",BizUtils.getJSON("bankList",Store.getInstance().newBankList));
            System.out.println("DB 'NewBankList' Updated..on local storage");
            Toast.makeText(this, "Bank Added..", Toast.LENGTH_SHORT).show();
            clear.performClick();
        } catch (ClassNotFoundException e) {

            System.err.println("Unable to write to DB 'NewBankList'");
        }


    }


    private void setAccTypeList() {


        accTypeList = new ArrayList<String>();
        accTypeList.add("");
        accTypeList.add("Credit");
        accTypeList.add("Debit");

        chooosedAcType = accTypeList.get(0);


         accTypeListAdapter = new CustomSpinnerAdapter(CreateBankActivity.this,accTypeList);
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

    private void linkWithLayout() {

        bankName= (EditText) findViewById(R.id.bank_name);
        personIncharge = (EditText) findViewById(R.id.person_incharge);
        addressLine1 = (EditText) findViewById(R.id.address_line_one);
        addressLine2 = (EditText) findViewById(R.id.address_line_two);
        city = (EditText) findViewById(R.id.state);
        mobileNumber = (EditText) findViewById(R.id.mobile_number);
        telephoneNumber = (EditText) findViewById(R.id.telephone);
        email = (EditText) findViewById(R.id.email);
        opBalance = (EditText) findViewById(R.id.op_bal);
        accType = (Spinner) findViewById(R.id.acc_type);
        accNumber = (EditText) findViewById(R.id.account_number);
        accName= (EditText) findViewById(R.id.acc_name);


        save = (Button) findViewById(R.id.save);
        clear = (Button) findViewById(R.id.clear);


    }
    public static boolean validateEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }
}
