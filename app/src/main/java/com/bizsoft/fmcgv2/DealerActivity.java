package com.bizsoft.fmcgv2;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.dataobject.Company;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.Network;
import com.bizsoft.fmcgv2.service.SignalRService;

public class DealerActivity extends AppCompatActivity {
    Button save,clear;
    EditText dealerName,addressLine1,addressLine2,city,mobileNumber,gstNumber,postalCode,telephone,email,bankName;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer);

        getSupportActionBar().setTitle("Dealer Profile");

        logo = (ImageView) findViewById(R.id.logo);
        dealerName = (EditText) findViewById(R.id.dealer_name);

        addressLine1 = (EditText) findViewById(R.id.address_line_one);
        addressLine2 = (EditText) findViewById(R.id.address_line_two);

        city = (EditText) findViewById(R.id.state);
        mobileNumber = (EditText) findViewById(R.id.mobile_number);
        gstNumber = (EditText) findViewById(R.id.gst_number);
        postalCode = (EditText) findViewById(R.id.postal_code);
        telephone = (EditText) findViewById(R.id.telephone);
        email = (EditText) findViewById(R.id.email);
        bankName = (EditText) findViewById(R.id.bank_name);

        save = (Button) findViewById(R.id.save);
        clear = (Button) findViewById(R.id.clear);

        Bitmap bmp= BizUtils.StringToBitMap(Store.getInstance().dealerLogo);

        logo.setImageResource(R.drawable.denariusoft64);

        logo.setImageBitmap(bmp);



        dealerName.setText(String.valueOf(Store.getInstance().dealer.getCompanyName()));
        addressLine1.setText(String.valueOf(Store.getInstance().dealer.getAddressLine1()));
        addressLine2.setText(String.valueOf(Store.getInstance().dealer.getAddressLine2()));
        city.setText(String.valueOf(Store.getInstance().dealer.getCityName()));
        mobileNumber.setText(String.valueOf(Store.getInstance().dealer.getMobileNo()));
        gstNumber.setText(String.valueOf(Store.getInstance().dealer.getGSTNo()));
        postalCode.setText(String.valueOf(Store.getInstance().dealer.getPostalCode()));
        email.setText(String.valueOf(Store.getInstance().dealer.getEMailId()));

        bankName.setEnabled(false);

        telephone.setText(String.valueOf(Store.getInstance().dealer.getTelephoneNo()));
        if(Store.getInstance().bankList!=null) {
            if(Store.getInstance().bankList.size()>0) {
                bankName.setText(String.valueOf(Store.getInstance().bankList.get(0).getLedger().getLedgerName()));
        }
            else
            {
                Toast.makeText(this, "No Bank Found..", Toast.LENGTH_SHORT).show();
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();
            }
        });

    }
    public  void validate()
    {
        boolean status = true;

        if(TextUtils.isEmpty(dealerName.getText().toString()))
        {
            dealerName.setError("Field cannot be empty");
            status = false;
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

        if(TextUtils.isEmpty(telephone.getText().toString()))
        {
            status = false;
            telephone.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(email.getText().toString()))
        {
            status = false;
            email.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(postalCode.getText().toString()))
        {
            status = false;
            postalCode.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(bankName.getText().toString()))
        {
            status = false;
            bankName.setError("Field cannot be empty");
        }
        if(status)
        {


           createDealer();
        }
        else
        {

            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }

    }

    private void createDealer() {

        Company company = new Company();
        company = Store.getInstance().dealer;
        company.setCompanyName(String.valueOf(dealerName.getText().toString()));
        company.setAddressLine1(String.valueOf(addressLine1.getText().toString()));
        company.setAddressLine2(String.valueOf(addressLine2.getText().toString()));
        company.setCityName(String.valueOf(city.getText().toString()));
        company.setPostalCode(String.valueOf(postalCode.getText().toString()));
        company.setMobileNo(String.valueOf(mobileNumber.getText().toString()));
        company.setTelephoneNo(String.valueOf(telephone.getText().toString()));
        company.setEMailId(String.valueOf(email.getText().toString()));
        company.setGSTNo(String.valueOf(gstNumber.getText().toString()));


        Toast.makeText(this, "Dealer profile updated offline", Toast.LENGTH_SHORT).show();


        SignalRService.saveCompany(company);

    }
}
