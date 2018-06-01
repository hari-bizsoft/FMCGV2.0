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
import android.widget.ListView;

import com.bizsoft.fmcgv2.BTLib.BTIntialize;
import com.bizsoft.fmcgv2.adapter.SalesAdapter;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.UIUtil;

import java.util.ArrayList;

public class SalesActivity extends AppCompatActivity  {

    ListView listView;
    EditText searchBar;
    ArrayList<Product> SalesList;
    ArrayList<Product> AllSalesList = new ArrayList<Product>();
    private SalesAdapter salesAdapter;

    BizUtils bizUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        UIUtil.setActionBarMenu(SalesActivity.this,getSupportActionBar(),"Sales Products List");
        bizUtils = new BizUtils();
        listView = (ListView) findViewById(R.id.listview);
        searchBar = (EditText) findViewById(R.id.search_bar);



        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                SalesList = new ArrayList<Product>();


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Char Sequence = "+s);
                SalesList.clear();
                if(TextUtils.isEmpty(s) | s.equals("") | s==null)
                {
                    System.out.println("Adding all the customers");

                    SalesList.addAll(Store.getInstance().addedProductForSales);
                }
                else {

                    for (int i = 0; i < Store.getInstance().addedProductForSales.size(); i++) {

                        if (Store.getInstance().addedProductForSales.get(i).getProductName().toLowerCase().contains(s)) {
                            SalesList.add(Store.getInstance().addedProductForSales.get(i));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Adding customer list size"+SalesList.size());

                System.out.println("Adding customer list size"+salesAdapter.products.size());
                salesAdapter.products.clear();
                salesAdapter.products.addAll(SalesList);
                System.out.println("Adding customer list size"+salesAdapter.products.size());

                salesAdapter.notifyDataSetChanged();
            }
        });
        AllSalesList.addAll(Store.getInstance().addedProductForSales);
        salesAdapter = new SalesAdapter(SalesActivity.this,AllSalesList);
        listView.setAdapter(salesAdapter);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        BTIntialize.onActivityResult(requestCode, resultCode, data);
    }
}
