/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bizsoft.fmcgv2.ProductListActivity;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Product;

import java.util.ArrayList;

/**
 * Created by GopiKing on 09-04-2018.
 */

public class ProductListAdapter extends BaseAdapter{
    Context context;
    public ArrayList<Product> productList;
    LayoutInflater layoutInflater= null;

    public ProductListAdapter(Context context, ArrayList<Product> allProductList) {
        this.context = context;
        this.productList = allProductList;
        this.layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return this.productList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.productList.get(position).getId();
    }
    class Holder
    {
        TextView id,name,stock;




    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.productlist_single_item, null);
        final Product product = (Product) getItem(position);

        holder.id = (TextView) convertView.findViewById(R.id.id);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.stock = (TextView) convertView.findViewById(R.id.stock_left);

        holder.id.setText(String.valueOf(product.getId()));
        holder.name.setText(String.valueOf(product.getProductName()));
        holder.stock.setText(String.valueOf(product.getAvailableStock()));


        return convertView;
    }
}
