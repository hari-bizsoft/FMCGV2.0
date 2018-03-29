package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Product;

import java.util.ArrayList;

/**
 * Created by GopiKing on 28-03-2018.
 */

public class ProductSpecAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater= null;
    public ArrayList<Product> productList= new ArrayList<Product>();


    public ProductSpecAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.productList= productList;
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
    class  Holder
    {
        TextView id, name, price;


        TextView quantity;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.product_spec_single_item, null);

        final Product product = (Product) getItem(position);

        holder.id = (TextView) convertView.findViewById(R.id.sale_id);
        holder.name = (TextView) convertView.findViewById(R.id.dealer_name);
        holder.price = (TextView) convertView.findViewById(R.id.price);


        holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
        holder.id.setText(String.valueOf(product.getId()));
        holder.name.setText(String.valueOf(product.getProductName()));
        holder.price.setText(String.valueOf(String.format("%.2f",product.getSellingRate())));
        holder.quantity.setText(String.valueOf(product.getQty()+" * "));


        return convertView;

    }
}
