package com.bizsoft.fmcgv2.service;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Store;

public class BlockPage extends AppCompatActivity {

    TextView deviceName,appId;
    static Activity activity = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_page);

        getSupportActionBar().setTitle("App Status");

        deviceName = (TextView) findViewById(R.id.device_name);
        appId = (TextView) findViewById(R.id.app_id);

        deviceName.setText("Device Name: "+String.valueOf(Store.getInstance().deviceName));
        appId.setText("App ID: "+String.valueOf(Store.getInstance().appId));

        activity = this;


    }
    public static Activity getActivity()
    {

        return activity;
    }    @Override
    public void onBackPressed() {





    }

}
