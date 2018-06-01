package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Product;

import java.util.ArrayList;

import static com.bizsoft.fmcgv2.ReportActivity.reportUrl;

/**
 * Created by shri on 16/8/17.
 */

public class ReportAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater= null;
    public ArrayList<Product> productReports = new ArrayList<Product>();

    public ReportAdapter(Context context, ArrayList<Product> productReports) {
        this.context = context;
        this.productReports = productReports;
        this.layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {
        return this.productReports.size();
    }

    @Override
    public Object getItem(int position) {
        return this.productReports.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class Holder
    {

        TextView productName;
        TextView amount;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        Product product = (Product) getItem(position);
        convertView = layoutInflater.inflate(R.layout.product_report_single_item, null);
        holder.productName = (TextView) convertView.findViewById(R.id.dealer_name);
        holder.amount = (TextView) convertView.findViewById(R.id.amount);



        holder.amount.setText(String.valueOf(String.format("%.2f",product.getAmount())));

        System.out.println("=============="+ reportUrl);
        System.out.println("=============="+reportUrl.contains("customer"));
        if(reportUrl.toLowerCase().contains("customer"))
        {

            holder.productName.setText(String.valueOf(product.getLedgerName()));
        }
        else
        {

            holder.productName.setText(String.valueOf(product.getProductName()));
        }




        return convertView;
    }
    public void removeAll()
    {
        for(int i=0;i<productReports.size();i++)
        {
            productReports.remove(i);
        }

    }
}
