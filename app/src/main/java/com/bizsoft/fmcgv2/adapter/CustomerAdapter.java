package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Customer;

import java.util.ArrayList;

/**
 * Created by shri on 9/8/17.
 */

public class CustomerAdapter extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater= null;
    public ArrayList<Customer> customerList  = new ArrayList<>();


    public CustomerAdapter(Context context, ArrayList<Customer> customerList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.customerList = customerList;
    }

    @Override
    public int getCount() {
        return customerList.size();
    }

    @Override
    public Customer getItem(int position) {
        return customerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class Holder
    {

        TextView customerName;
        TextView GSTNumber;
        TextView state;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.customer_single_item, null);
        holder.customerName = (TextView) convertView.findViewById(R.id.customer_name);
        holder.state = (TextView) convertView.findViewById(R.id.state);
        holder.customerName.setText(String.valueOf(customerList.get(position).getLedger().getLedgerName()));
        holder.GSTNumber = (TextView) convertView.findViewById(R.id.gst_no);
        holder.GSTNumber.setText(String.valueOf(customerList.get(position).getLedger().getGSTNo()));
        holder.state.setText(String.valueOf(customerList.get(position).getLedger().getCityName()));

        return convertView;
    }
}
