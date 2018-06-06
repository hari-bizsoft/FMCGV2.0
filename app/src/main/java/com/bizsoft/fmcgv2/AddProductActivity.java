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
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.StockGroup;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.dataobject.UOM;
import com.bizsoft.fmcgv2.service.SignalRService;

import java.util.ArrayList;

public class AddProductActivity extends AppCompatActivity {

    private String myAction;
    private int position;
    private Product product;

    TextView productName,itemCode;
    EditText purchaseRate,sellingRate,minSellingRate,maxSellingRate,MRP,openingStock,reorderLevel;
    Spinner stockGroup,UOM;
    private ArrayList<String> stockGroupList;
    private String choosedStockGroup;
    private CustomSpinnerAdapter stockGroupListAdapter;
    private Long choosedStockGroupId;
    Button save;
    private ArrayList<String> uomList;
    private String choosedUom;
    private int choosedUOMId;
    private CustomSpinnerAdapter uomListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productName = (TextView) findViewById(R.id.product_name);
        itemCode = (TextView) findViewById(R.id.item_code);
        purchaseRate = (EditText) findViewById(R.id.purchase_rate);
        sellingRate = (EditText) findViewById(R.id.selling_rate);
        minSellingRate = (EditText) findViewById(R.id.min_selling_rate);
        maxSellingRate = (EditText) findViewById(R.id.max_selling_rate);
        MRP = (EditText) findViewById(R.id.mrp);
        openingStock = (EditText) findViewById(R.id.opening_stock);
        reorderLevel = (EditText) findViewById(R.id.re_order_level);

        stockGroup = (Spinner) findViewById(R.id.stock_group);
        UOM = (Spinner) findViewById(R.id.uom);
        save = (Button) findViewById(R.id.save);

        //disabled field
        MRP.setEnabled(false);
        stockGroup.setEnabled(false);
        purchaseRate.setEnabled(false);
        sellingRate.setEnabled(false);
        minSellingRate.setEnabled(false);
        maxSellingRate.setEnabled(false);
        UOM.setEnabled(false);



      try {
          setStockGroupTypeList();
          setUOMList();
          findAction();
      }catch (Exception e){
          Log.e("product Error :", String.valueOf(e));
      }

      save.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {

              validate();
          }
      });

    }

    private void setUOMList() {
        uomList = new ArrayList<String>();

        uomList.addAll(com.bizsoft.fmcgv2.dataobject.UOM.getNameList());
        choosedUom = Store.getInstance().UOM.get(0).getSymbol();
        choosedUOMId = Store.getInstance().UOM.get(0).getId();


        uomListAdapter = new CustomSpinnerAdapter(AddProductActivity.this,uomList);
        UOM.setAdapter(uomListAdapter );
        UOM.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                choosedUom = Store.getInstance().UOM.get(position).getSymbol();
                choosedUOMId = Store.getInstance().UOM.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void validate() {


        boolean status = true;

        if(!TextUtils.isEmpty(openingStock.getText()))
        {

            String regex = "^[1-9]\\d*(\\.\\d+)?$";
            if(!openingStock.getText().toString().trim().matches(regex))
            {
                openingStock.setError("Please Enter a valid opening stock entry..!");
                status = false;
            }
        }
        else
        {
            status = false;
        }
        if(!TextUtils.isEmpty(reorderLevel.getText()))
        {

            String regex = "^[1-9]\\d*(\\.\\d+)?$";
            if(!reorderLevel.getText().toString().trim().matches(regex))
            {
                reorderLevel.setError("Please Enter a valid opening stock entry..!");
                status = false;
            }
        }
        else
        {
            status = false;
        }

        if(status)
        {
            modelDate();
        }

    }

    private void modelDate() {

        product.setOpeningStock(Double.parseDouble(openingStock.getText().toString()));
        product.setReOrderLevel(Double.parseDouble(reorderLevel.getText().toString()));
        product.setSynced(false);

        clearData();



    }

    private void clearData() {

        productName.setText("");
        itemCode.setText("");
        purchaseRate.setText("0.0");
        sellingRate.setText("0.0");
        minSellingRate.setText("0.0");
        maxSellingRate.setText("0.0");
        MRP.setText("");
        openingStock.setText("");
        reorderLevel.setText("");

        stockGroup.setSelection(0);
        UOM.setSelection(0);
        Toast.makeText(this, "Saved..", Toast.LENGTH_SHORT).show();
    }

    private void findAction() {

        myAction = getIntent().getExtras().getString("myAction");
        position = getIntent().getExtras().getInt("position");

        System.out.println("Action : "+myAction);
        if(myAction!=null)
        {
            if(myAction.equals("edit"))
            {

                product =    ProductListActivity.productAdapter.productList.get(position);

                setValues(product);
                getSupportActionBar().setTitle("Edit Product");


            }
            else  if(myAction.equals("add"))
            {
                getSupportActionBar().setTitle("Add Product");
            }

        }
    }
    private void setStockGroupTypeList() {


        stockGroupList = new ArrayList<String>();

        stockGroupList.addAll(StockGroup.getNames());


        choosedStockGroup = stockGroupList.get(0);
        choosedStockGroupId = Store.getInstance().stockGroupList.get(0).getId();


        stockGroupListAdapter = new CustomSpinnerAdapter(AddProductActivity.this,stockGroupList);
        stockGroup.setAdapter(stockGroupListAdapter );
        stockGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                choosedStockGroup = stockGroupList.get(position);
                choosedStockGroupId = Store.getInstance().stockGroupList.get(position).getId();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void setValues(Product product) {

        productName.setText(String.valueOf(product.getProductName()));
        itemCode.setText(String.valueOf(product.getItemCode()));
        purchaseRate.setText(String.valueOf(product.getPurchaseRate()));
        sellingRate.setText(String.valueOf(product.getSellingRate()));
        minSellingRate.setText(String.valueOf(product.getMinSellingRate()));
        maxSellingRate.setText(String.valueOf(product.getMaxSellingRate()));
        MRP.setText(String.valueOf(product.MRP));
        openingStock.setText(String.valueOf(product.getOpeningStock()));
        reorderLevel.setText(String.valueOf(product.getReOrderLevel()));


        if(product.getStockGroup().getStockGroupName()!=null) {
            for (int i = 0; i < stockGroupList.size(); i++) {
                if (product.getStockGroup().getStockGroupName().equals(stockGroupList.get(i))) {
                    stockGroup.setSelection(i);
                    choosedStockGroup = stockGroupList.get(i);
                    choosedStockGroupId = product.getStockGroup().getId();
                }

            }
        }
        if(product.getUOMId()!=null) {
            for (int i = 0; i < Store.getInstance().UOM.size(); i++) {
                if (product.getUOMId().compareTo(Long.valueOf(Store.getInstance().UOM.get(i).getId()))==0) {

                    String currentUOMName = Store.getInstance().UOM.get(i).getSymbol();
                    int currentUOMid = Store.getInstance().UOM.get(i).getId();

                    for(int j=0;j<uomList.size();j++)
                    {
                        if(uomList.get(j).equals(currentUOMName))
                        {

                            Log.d("Choosed UOM",currentUOMName);

                            UOM.setSelection(j);
                            choosedUom = uomList.get(j);
                            choosedUOMId = currentUOMid;
                        }
                    }


                   // choosedUOMId = product.getUOMId();
                }

            }
        }


    }
}
