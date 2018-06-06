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
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.signalr.pojo.PDetailsItem;

import java.util.ArrayList;

/**
 * Created by GopiKing on 28-03-2018.
 */

public class ProductSpecAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater= null;
    public ArrayList<PDetailsItem> productList= new ArrayList<PDetailsItem>();


    public ProductSpecAdapter(Context context, ArrayList<PDetailsItem> productList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.productList= productList;

        System.out.println("Called comtructor");
    }

    @Override
    public int getCount() {
        System.out.println("Called getcount @ "+this.productList.size());
        return this.productList.size();

    }

    @Override
    public Object getItem(int position) {

        System.out.println("Called getitem @ "+this.productList.get(position));
        return this.productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long) this.productList.get(position).getId();
    }
    class  Holder
    {
        TextView id, name, stock;


        TextView quantity;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        System.out.println("Called product spec holder");
        final Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.product_spec_single_item, null);

        final PDetailsItem product = (PDetailsItem) getItem(position);

        holder.id = (TextView) convertView.findViewById(R.id.sale_id);
        holder.name = (TextView) convertView.findViewById(R.id.dealer_name);
        holder.stock = (TextView) convertView.findViewById(R.id.stock);


        holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
        holder.id.setText(String.valueOf(((int) product.getId())));
        holder.name.setText(String.valueOf(product.getProductName()));
        holder.stock.setText(String.valueOf(((int) product.getAvailable())));
        holder.quantity.setText(String.valueOf(((int) product.getQty())));


        return convertView;

    }
}
