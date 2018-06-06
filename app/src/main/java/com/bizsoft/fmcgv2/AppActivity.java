/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.bizsoft.fmcgv2.adapter.ActivityAdapter;
import com.bizsoft.fmcgv2.dataobject.Store;

public class AppActivity extends AppCompatActivity {

    ActivityAdapter activityAdapter;
    ListView activityList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        activityList = (ListView) findViewById(R.id.listview);


        activityAdapter = new ActivityAdapter(AppActivity.this, Store.getInstance().notificationList);
        activityList.setAdapter(activityAdapter);





    }
}
