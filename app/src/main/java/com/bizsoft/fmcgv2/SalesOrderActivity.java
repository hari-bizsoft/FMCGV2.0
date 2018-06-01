package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.BTLib.BTIntialize;
import com.bizsoft.fmcgv2.adapter.SalesAdapter;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.UIUtil;

import java.util.ArrayList;

public class SalesOrderActivity extends AppCompatActivity {

    ListView listView;
    EditText searchBar;
    ArrayList<Product> SalesOrderList;
    ArrayList<Product> AllSalesOrderList = new ArrayList<Product>();
    private SalesAdapter salesOrderAdapter;

    BizUtils bizUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_order);
        getSupportActionBar().setTitle("Sales Order List");
        UIUtil.setActionBarMenu(SalesOrderActivity.this,getSupportActionBar(),"Sales Order Products List");

        bizUtils = new BizUtils();
        listView = (ListView) findViewById(R.id.listview);
        searchBar = (EditText) findViewById(R.id.search_bar);



        AllSalesOrderList.addAll(Store.getInstance().addedProductForSalesOrder);
        salesOrderAdapter = new SalesAdapter(SalesOrderActivity.this,AllSalesOrderList);
        listView.setAdapter(salesOrderAdapter);

        



        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                SalesOrderList = new ArrayList<Product>();


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Char Sequence = "+s);
                SalesOrderList.clear();
                if(TextUtils.isEmpty(s) | s.equals("") | s==null)
                {
                    System.out.println("Adding all the customers");

                    SalesOrderList.addAll(Store.getInstance().addedProductForSalesOrder);
                }
                else {

                    for (int i = 0; i < Store.getInstance().addedProductForSalesOrder.size(); i++) {

                        if (Store.getInstance().addedProductForSalesOrder.get(i).getProductName().toLowerCase().contains(s)) {
                            SalesOrderList.add(Store.getInstance().addedProductForSalesOrder.get(i));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Adding customer list size"+SalesOrderList.size());

                System.out.println("Adding customer list size"+salesOrderAdapter.products.size());
                salesOrderAdapter.products.clear();
                salesOrderAdapter.products.addAll(SalesOrderList);
                System.out.println("Adding customer list size"+salesOrderAdapter.products.size());

                salesOrderAdapter.notifyDataSetChanged();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BTIntialize.onActivityResult(requestCode, resultCode, data);
    }
}
