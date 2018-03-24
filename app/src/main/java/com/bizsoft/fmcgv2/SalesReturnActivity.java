package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.bizsoft.fmcgv2.BTLib.BTIntialize;
import com.bizsoft.fmcgv2.adapter.SalesAdapter;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;

import java.util.ArrayList;

public class SalesReturnActivity extends AppCompatActivity {

    ListView listView;
    EditText searchBar;
    ArrayList<Product> SalesReturnList;
    ArrayList<Product> AllSalesReturnList = new ArrayList<Product>();
    private SalesAdapter salesReturnAdapter;
    FloatingActionButton menu;
    BizUtils bizUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_return);
        getSupportActionBar().setTitle("Sales Return List");

        menu  = (FloatingActionButton) findViewById(R.id.menu);
        bizUtils = new BizUtils();

        listView = (ListView) findViewById(R.id.listview);
        searchBar = (EditText) findViewById(R.id.search_bar);

        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                SalesReturnList = new ArrayList<Product>();


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Char Sequence = "+s);
                SalesReturnList.clear();
                if(TextUtils.isEmpty(s) | s.equals("") | s==null)
                {
                    System.out.println("Adding all the customers");

                    SalesReturnList.addAll(Store.getInstance().addedProductForSalesReturn);
                }
                else {

                    for (int i = 0; i < Store.getInstance().addedProductForSalesReturn.size(); i++) {

                        if (Store.getInstance().addedProductForSalesReturn.get(i).getProductName().toLowerCase().contains(s)) {
                            SalesReturnList.add(Store.getInstance().addedProductForSalesReturn.get(i));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Adding customer list size"+SalesReturnList.size());

                System.out.println("Adding customer list size"+salesReturnAdapter.products.size());
                salesReturnAdapter.products.clear();
                salesReturnAdapter.products.addAll(SalesReturnList);
                System.out.println("Adding customer list size"+salesReturnAdapter.products.size());

                salesReturnAdapter.notifyDataSetChanged();
            }
        });
        AllSalesReturnList.addAll(Store.getInstance().addedProductForSalesReturn);
        salesReturnAdapter = new SalesAdapter(SalesReturnActivity.this,AllSalesReturnList);
        listView.setAdapter(salesReturnAdapter);


        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bizUtils.showMenu(SalesReturnActivity.this);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BTIntialize.onActivityResult(requestCode, resultCode, data);
    }
}
