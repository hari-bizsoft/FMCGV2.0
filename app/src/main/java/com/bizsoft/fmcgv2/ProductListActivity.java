package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.bizsoft.fmcgv2.adapter.CustomerAdapter;
import com.bizsoft.fmcgv2.adapter.ProductAdapter;
import com.bizsoft.fmcgv2.adapter.ProductListAdapter;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.UIUtil;

import java.util.ArrayList;

public class ProductListActivity extends AppCompatActivity {
    ListView productListView;
    FloatingActionButton add;

    EditText searchBar;
    ArrayList<Product> productList;
    ArrayList<Product> allProductList = new ArrayList<Product>();
    public static ProductListAdapter productAdapter;
    BizUtils bizUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        UIUtil.setActionBarMenu(ProductListActivity.this,getSupportActionBar(),"Product List");


        allProductList.addAll(Store.getInstance().productList);
        productListView = (ListView) findViewById(R.id.product_listview);


        searchBar = (EditText) findViewById(R.id.search_bar);
        bizUtils = new BizUtils();






        productAdapter  = new ProductListAdapter(ProductListActivity.this, allProductList);
        productListView.setAdapter(productAdapter);
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(ProductListActivity.this,AddProductActivity.class);
                intent.putExtra("myAction","edit");
                intent.putExtra("position",position);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });


        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                productList = new ArrayList<Product>();


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Char Sequence = "+s);
                productList.clear();
                if(TextUtils.isEmpty(s) | s.equals("") | s==null)
                {
                    System.out.println("Adding all the products");

                    productList.addAll(Store.getInstance().productList);
                }
                else {

                    for (int i = 0; i < Store.getInstance().productList.size(); i++) {

                        if (Store.getInstance().productList.get(i).getProductName().toLowerCase().contains(s.toString().toLowerCase())) {
                            productList.add(Store.getInstance().productList.get(i));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Adding product list list size"+productList.size());
                System.out.println("Adding product list size"+productAdapter.productList.size());
                productAdapter.productList.clear();
                productAdapter.productList.addAll(productList);
                System.out.println("Adding product list size"+productAdapter.productList.size());

                productAdapter.notifyDataSetChanged();
            }
        });
    }


}
