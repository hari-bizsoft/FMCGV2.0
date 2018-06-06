/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.bizsoft.fmcgv2.adapter.CustomerAdapter;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.UIUtil;

import java.util.ArrayList;
import java.util.Collections;

public class CustomerActivity extends AppCompatActivity {

    static ListView customerListView;
    FloatingActionButton add;

    static EditText searchBar;
    static ArrayList<Customer> CustomerList;
    static ArrayList<Customer> AllCustomerList = new ArrayList<Customer>();
    public static CustomerAdapter customerAdapter;
    BizUtils bizUtils;
    private TextWatcher namesearchlistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        UIUtil.setActionBarMenu(CustomerActivity.this,getSupportActionBar(),"Customer List");

        AllCustomerList = new ArrayList<>();
        AllCustomerList.addAll(Store.getInstance().customerList);


        //getSupportActionBar().setTitle("Customer List");
        customerListView = (ListView) findViewById(R.id.customer_listview);
        add = (FloatingActionButton) findViewById(R.id.add);

        searchBar = (EditText) findViewById(R.id.search_bar);
        bizUtils = new BizUtils();

        add.bringToFront();



        customerAdapter = new CustomerAdapter(CustomerActivity.this, AllCustomerList);
        customerListView.setAdapter(customerAdapter);
        customerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(CustomerActivity.this,AddCustomerActivity.class);
                intent.putExtra("myAction","edit");
                intent.putExtra("position",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
         namesearchlistener = new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                CustomerList = new ArrayList<Customer>();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Char Sequence = "+s);
                CustomerList.clear();
                if(TextUtils.isEmpty(s) | s.equals("") | s==null)
                {
                    System.out.println("Adding all the customers");

                    CustomerList.addAll(Store.getInstance().customerList);
                }
                else {

                    for (int i = 0; i < Store.getInstance().customerList.size(); i++) {

                        Log.d("Search term",s.toString().toLowerCase());
                        Log.d("Actual name term",Store.getInstance().customerList.get(i).getLedgerName().toLowerCase());
                        if (Store.getInstance().customerList.get(i).getLedger().getLedgerName().toLowerCase().contains(s.toString().toLowerCase())) {
                            Log.d("Matched term",Store.getInstance().customerList.get(i).getLedgerName().toLowerCase());
                            CustomerList.add(Store.getInstance().customerList.get(i));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Adding customer list size"+CustomerList.size());

                System.out.println("Adding customer list size"+customerAdapter.customerList.size());
                customerAdapter.customerList.clear();
                customerAdapter.customerList.addAll(CustomerList);
                System.out.println("Adding customer list size"+customerAdapter.customerList.size());

                customerAdapter.notifyDataSetChanged();
            }
        };
        searchBar.addTextChangedListener(namesearchlistener);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomerActivity.this,AddCustomerActivity.class);
                intent.putExtra("myAction","add");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("ACTIVITY","resume");
        searchBar.addTextChangedListener(namesearchlistener);
    }
}
