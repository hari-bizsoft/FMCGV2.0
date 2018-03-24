package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.BTLib.BTIntialize;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Product;

import java.util.ArrayList;

/**
 * Created by shri on 11/8/17.
 */

public class SalesAdapter extends BaseAdapter {

    Context context;
    public ArrayList<Product> products = new ArrayList<Product>();
    LayoutInflater layoutInflater= null;
    final static  int BLUETOOTH_FLAG = 619;
    public SalesAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
        this.layoutInflater = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {

        return products.size();
    }

    @Override
    public Product getItem(int position) {
        return products.get(position);
    }

    @Override
    public long getItemId(int position) {
        return products.get(position).getId();
    }

    class  Holder
    {
        TextView id, name, price;

        TextView calculatedAmount;
        TextView quantity;
        TextView discountAmount,finalPrice;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.sales_product_single_item, null);

        final Product product = getItem(position);

        holder.id = (TextView) convertView.findViewById(R.id.sale_id);
        holder.name = (TextView) convertView.findViewById(R.id.customer_name);
        holder.price = (TextView) convertView.findViewById(R.id.price);

        holder.discountAmount = (TextView) convertView.findViewById(R.id.discount);
        holder.finalPrice= (TextView) convertView.findViewById(R.id.final_price);

        holder.calculatedAmount = (TextView) convertView.findViewById(R.id.calculated_amount);
        holder.quantity = (TextView) convertView.findViewById(R.id.quantity);









        holder.id.setText(String.valueOf(product.getId()));
        holder.name.setText(String.valueOf(product.getProductName()));
        holder.price.setText(String.valueOf(String.format("%.2f",product.getSellingRate())));
        holder.quantity.setText(String.valueOf(product.getQty()+" * "));

        double amount = product.getQty() * product.getSellingRate();

        holder.calculatedAmount.setText(" = "+String.valueOf(String.format("%.2f",amount)));

        holder.discountAmount.setText(String.valueOf(String.format("%.2f",product.getDiscountAmount())));


        double fp = amount - product.getDiscountAmount();

        holder.finalPrice.setText(String.valueOf(String.format("%.2f",fp)));





        return convertView;
    }
}
