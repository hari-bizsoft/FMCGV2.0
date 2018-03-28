package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.StockGroup;

import java.util.ArrayList;

/**
 * Created by shri on 10/8/17.
 */

public class StockGroupAdapter extends BaseAdapter {

    Context context;
    ArrayList<StockGroup> stockGroupList;
    LayoutInflater layoutInflater= null;

    public StockGroupAdapter(Context context,ArrayList<StockGroup> stockGroupList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.stockGroupList = stockGroupList;

    }

    @Override
    public int getCount() {
        return this.stockGroupList.size();
    }

    @Override
    public StockGroup getItem(int position) {
        return this.stockGroupList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.stockGroupList.get(position).getId();
    }

    class  Holder
    {
        TextView stockGroupName,id,company,groupcode;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        Holder holder = new Holder();
        StockGroup stockGroup = getItem(position);

        convertView = layoutInflater.inflate(R.layout.stock_single_item, null);
        holder.stockGroupName = (TextView) convertView.findViewById(R.id.dealer_name);
        holder.id = (TextView) convertView.findViewById(R.id.sale_id);


        holder.id.setText(String.valueOf(stockGroup.getId()));
        holder.stockGroupName.setText(String.valueOf(stockGroup.getStockGroupName()));

        return convertView;
    }
}
