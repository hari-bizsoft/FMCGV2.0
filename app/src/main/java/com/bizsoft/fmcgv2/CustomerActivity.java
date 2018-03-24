package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.bizsoft.fmcgv2.adapter.CustomerAdapter;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;

import java.util.ArrayList;

public class CustomerActivity extends AppCompatActivity {

    ListView customerListView;
    FloatingActionButton add,menu;
    private ImageView sync;
    private ImageButton sales;
    private ImageButton salesOrder;
    private ImageButton home;
    EditText searchBar;
    ArrayList<Customer> CustomerList;
    ArrayList<Customer> AllCustomerList = new ArrayList<Customer>();
    private CustomerAdapter customerAdapter;
    BizUtils bizUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        AllCustomerList.addAll(Store.getInstance().customerList);


        getSupportActionBar().setTitle("Customer List");
        customerListView = (ListView) findViewById(R.id.customer_listview);
        add = (FloatingActionButton) findViewById(R.id.add);
        menu = (FloatingActionButton) findViewById(R.id.menu);
        searchBar = (EditText) findViewById(R.id.search_bar);
        bizUtils = new BizUtils();

        add.bringToFront();
        menu.bringToFront();

        customerAdapter = new CustomerAdapter(CustomerActivity.this, AllCustomerList);
        customerListView.setAdapter(customerAdapter);
        searchBar.addTextChangedListener(new TextWatcher() {

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

                        if (Store.getInstance().customerList.get(i).getLedgerName().toLowerCase().contains(s)) {
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
        });




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(CustomerActivity.this,AddCustomerActivity.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bizUtils.showMenu(CustomerActivity.this);
            }
        });
    }

}
