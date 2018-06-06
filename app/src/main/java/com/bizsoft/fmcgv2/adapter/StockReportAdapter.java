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
import com.bizsoft.fmcgv2.dataobject.StockReport;

import java.util.ArrayList;

/**
 * Created by GopiKing on 04-05-2018.
 */
public class StockReportAdapter extends BaseAdapter {

    public ArrayList<StockReport> stockList = new ArrayList<StockReport>();
    LayoutInflater layoutInflater= null;
    Context context;

    public StockReportAdapter(ArrayList<StockReport> stockList, Context context) {
        this.stockList = stockList;
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return this.stockList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.stockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    class  Holder
    {
        TextView productName;

        TextView sIn;
        TextView SOut;
        TextView Sal;
        TextView saleR;
        TextView available;
        TextView SRforSale;
        TextView SRNotForSale;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.stock_report_single_item, null);


        final StockReport stockReport= (StockReport) getItem(position);
        holder.productName = (TextView) convertView.findViewById(R.id.product_name);
        //holder.sIn = (TextView) convertView.findViewById(R.id.sin);
       // holder.SOut= (TextView) convertView.findViewById(R.id.sout);
        holder.Sal = (TextView) convertView.findViewById(R.id.sale);
        holder.saleR = (TextView) convertView.findViewById(R.id.sale_return);
        holder.available= (TextView) convertView.findViewById(R.id.available_stock);
        holder.SRforSale= (TextView) convertView.findViewById(R.id.sale_return_for_sale);
        holder.SRNotForSale= (TextView) convertView.findViewById(R.id.sale_return_not_for_sale);
        holder.productName.setText(String.valueOf(stockReport.getProductName()));

      //  holder.sIn.setText(String.valueOf(stockReport.getSIn()));
      //  holder.SOut.setText(String.valueOf(stockReport.getSOut()));

        holder.Sal.setText(String.valueOf(stockReport.getSal()));
        holder.saleR.setText(String.valueOf(stockReport.getSR()));
        holder.available.setText(String.valueOf(stockReport.getAvailable()));
        holder.SRforSale.setText(String.valueOf(stockReport.getSRQtyForSales()));
        holder.SRNotForSale.setText(String.valueOf(stockReport.getSRQtyNForSales()));





        return convertView;
    }
}
