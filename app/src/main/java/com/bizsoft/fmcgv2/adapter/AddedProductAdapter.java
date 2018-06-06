/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.DashboardActivity;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Store;

import java.util.ArrayList;

/**
 * Created by shri on 10/8/17.
 */

public class AddedProductAdapter extends BaseAdapter {
    Context context;
    public ArrayList<Product> productList = new ArrayList<Product>();
    LayoutInflater layoutInflater = null;
    public Holder holder;
    public String from;
    double tender;
    double subTotal;
    public double gst;
    double fromCustomer;
    public double grandTotal;
    private int currentPosition;
    private int savedListTop,savedPosition;


    public AddedProductAdapter(Context context, ArrayList<Product> productList) {

        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.productList = productList;
        holder = new Holder();


    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    @Override
    public int getCount() {
        return productList.size();

    }

    @Override
    public Product getItem(int position) {
        return productList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productList.get(position).getId();
    }

    public class Holder {
        TextView id, name;
        //TextView price;


      //  TextView calculatedAmount;
        TextView quantity;
        public ImageButton remove;
        TextView resale,reason;
        TextView discountAmount,finalPrice;
        TextView discountLabel;
        //TextView productCode;
        ImageButton plus,minus;


        public TextView sno;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {





        convertView = layoutInflater.inflate(R.layout.added_product_single_item, null);

        final Product product = getItem(position);

        holder.id = (TextView) convertView.findViewById(R.id.sale_id);
        holder.sno = (TextView) convertView.findViewById(R.id.s_no);
        holder.name = (TextView) convertView.findViewById(R.id.dealer_name);
       // holder.price = (TextView) convertView.findViewById(R.id.stock);
        holder.resale = (TextView) convertView.findViewById(R.id.is_resale);
        holder.reason= (TextView) convertView.findViewById(R.id.particulars);

       // holder.calculatedAmount = (TextView) convertView.findViewById(R.id.calculated_amount);
        holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
        holder.remove = (ImageButton) convertView.findViewById(R.id.remove);
        holder.discountAmount = (TextView) convertView.findViewById(R.id.discount);
        holder.finalPrice= (TextView) convertView.findViewById(R.id.final_price);
        holder.discountLabel = (TextView) convertView.findViewById(R.id.discount_label);
        holder.plus = (ImageButton) convertView.findViewById(R.id.plus);
        holder.minus = (ImageButton) convertView.findViewById(R.id.minus);
       // holder.productCode= (TextView) convertView.findViewById(R.id.product_code);

        if(!DashboardActivity.currentSaleType.toLowerCase().contains("return"))
        {
            holder.resale.setVisibility(View.GONE);
            holder.reason.setVisibility(View.GONE);
        }
        holder.resale.setText(String.valueOf("Resale: "+product.isResale()));
        holder.reason.setText(String.valueOf("Reason: "+product.getParticulars()));


        if(getFrom()!=null)
        {
            if(getFrom().toLowerCase().contains("preview"))
            {
                holder.remove.setVisibility(View.INVISIBLE);
                holder.plus.setVisibility(View.INVISIBLE);
                holder.minus.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.remove.setVisibility(View.VISIBLE);
                holder.plus.setVisibility(View.VISIBLE);
                holder.minus.setVisibility(View.VISIBLE);
            }
        }
        holder.id.setText(String.valueOf(product.getId()));
        holder.name.setText(String.valueOf(product.getProductName()));
        holder.sno.setText(String.valueOf(position+1)+")");
       // holder.price.setText(String.valueOf(String.format("%.2f",product.getMRP())));
       // holder.productCode.setText(String.valueOf(product.getItemCode()));

        holder.quantity.setText(String.valueOf(product.getQty()));
        double amount = product.getQty() * product.getMRP();
       // holder.calculatedAmount.setText(" = "+String.valueOf(String.format("%.2f",amount)));
        holder.discountAmount.setText(String.valueOf(String.format("%.2f",product.getDiscountAmount())));

        //final double dp = 100/(amount/product.getDiscountAmount());
        final double dp = product.getDiscountPercentage();


        holder.discountLabel.setText("Discount "+String.valueOf(dp) +" % = ");
        holder.discountLabel.setText("Discount "+String.valueOf(String.format("%.2f",dp))+" % = ");

        if(DashboardActivity.discountType.toLowerCase().contains("no"))
        {
            holder.discountLabel.setVisibility(View.INVISIBLE);
            holder.discountAmount.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.discountLabel.setVisibility(View.VISIBLE);
            holder.discountAmount.setVisibility(View.VISIBLE);

        }


        if(product.getFinalPrice()==0)
        {
            product.setFinalPrice(product.getQty()*product.getMRP());
            notifyDataSetChanged();
        }

        holder.finalPrice.setText(String.valueOf(String.format("%.2f",product.getFinalPrice())));

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<productList.size();i++)
                {
                    if(position==i)
                    {
                        productList.get(i).setDiscountAmount(0);
                        productList.get(i).setFinalPrice(0);
                        productList.get(i).setDiscountPercentage(0.0);
                        productList.get(i).setQty(null);
                        productList.get(i).setResale(false);
                        productList.get(i).setParticulars("");
                        productList.get(i).setSellingRate(productList.get(i).getSellingRateRef());


                        productList.remove(i);
                        notifyDataSetChanged();
                    }
                }

            }
        });
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int x = 0;
                try {
                    x =  product.getQty().intValue();
                    double discountPercent = dp;
                    if(DashboardActivity.currentSaleType.toLowerCase().contains("order") | DashboardActivity.currentSaleType.toLowerCase().contains("return"))
                    {

                        x++;
                        double Price =  x * product.getMRP();
                        double disAmt = Price * (product.getDiscountPercentage()/100);
                        double fPrice = Price - disAmt;

                        product.setDiscountAmount(disAmt);
                        product.setFinalPrice(fPrice);
                        product.setQty(Long.valueOf(x));

                        holder.quantity.setText(String.valueOf(x));
                        holder.finalPrice.setText(String.valueOf(String.format("%.2f",fPrice)));
                        holder.discountAmount.setText(String.valueOf(String.format("%.2f",disAmt)));
                        //enableDiscountField(holder);

                        View firstVisibleView = DashboardActivity.addedProductListView.getChildAt(0);
                        savedListTop = (firstVisibleView == null) ? 0 : firstVisibleView.getTop();

                        currentPosition = position;
                        ((Activity) context).runOnUiThread(new Runnable() {
                            public void run() {
                                notifyDataSetChanged();
                            }
                        });
                        DashboardActivity.addedProductListView.requestFocus();

                    }
                    else {

                        if (x < product.getAvailableStock()) {
                            x++;



                            double Price =  x * product.getMRP();
                            double disAmt = Price * (product.getDiscountPercentage()/100);
                            double fPrice = Price - disAmt;

                            product.setDiscountAmount(disAmt);
                            product.setFinalPrice(fPrice);
                            product.setQty(Long.valueOf(x));

                            holder.quantity.setText(String.valueOf(x));
                            holder.finalPrice.setText(String.valueOf(String.format("%.2f",fPrice)));
                            holder.discountAmount.setText(String.valueOf(String.format("%.2f",disAmt)));

                            View firstVisibleView = DashboardActivity.addedProductListView.getChildAt(0);
                            savedListTop = (firstVisibleView == null) ? 0 : firstVisibleView.getTop();

                            currentPosition =position;

                            ((Activity) context).runOnUiThread(new Runnable() {
                                public void run() {
                                    notifyDataSetChanged();
                                    DashboardActivity.addedProductListView.requestFocus();
                                }
                            });


                         //   enableDiscountField(holder);
                        } else {
                            Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                          //  disableDiscountFields(holder);
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                   // disableDiscountFields(holder);
                }
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int x = 0;

                try {
                     x =  product.getQty().intValue();

                     int c = 0;

                     if(x-1>0) {
                         if (DashboardActivity.currentSaleType.toLowerCase().contains("order") | DashboardActivity.currentSaleType.toLowerCase().contains("return")) {
                             if (x >= 1) {
                                 x--;
                                 double Price = x * product.getMRP();
                                 double disAmt = Price * (product.getDiscountPercentage() / 100);
                                 double fPrice = Price - disAmt;

                                 product.setDiscountAmount(disAmt);
                                 product.setFinalPrice(fPrice);
                                 product.setQty(Long.valueOf(x));

                                 holder.quantity.setText(String.valueOf(x));
                                 holder.finalPrice.setText(String.valueOf(String.format("%.2f", fPrice)));
                                 holder.discountAmount.setText(String.valueOf(String.format("%.2f", disAmt)));

                                 ((Activity) context).runOnUiThread(new Runnable() {
                                     public void run() {
                                         notifyDataSetChanged();
                                         DashboardActivity.addedProductListView.requestFocus();
                                     }
                                 });

                                 //enableDiscountField(holder);
                             } else {
                                 Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                                 // disableDiscountFields(holder);

                             }


                         } else {
                             if (x >= 1) {
                                 x--;
                                 holder.quantity.setText(String.valueOf(x));
                                 double Price = x * product.getMRP();
                                 double disAmt = Price * (product.getDiscountPercentage() / 100);
                                 double fPrice = Price - disAmt;

                                 product.setDiscountAmount(disAmt);
                                 product.setFinalPrice(fPrice);
                                 product.setQty(Long.valueOf(x));

                                 holder.quantity.setText(String.valueOf(x));
                                 holder.finalPrice.setText(String.valueOf(String.format("%.2f", fPrice)));
                                 holder.discountAmount.setText(String.valueOf(String.format("%.2f", disAmt)));

                                 ((Activity) context).runOnUiThread(new Runnable() {
                                     public void run() {
                                         notifyDataSetChanged();
                                         DashboardActivity.addedProductListView.requestFocus();
                                     }
                                 });

                                 // enableDiscountField(holder);
                             } else {
                                 Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                                 //disableDiscountFields(holder);
                             }

                         }

                     }
                     else
                     {
                         Toast.makeText(context, "Product quantity cannot be 0", Toast.LENGTH_SHORT).show();
                     }

                } catch (Exception e) {
                    Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                    //disableDiscountFields(holder);
                }
            }
        });
        return convertView;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        Log.d("added product","notifydatasetchanged");

         tender=0;
         subTotal=0;
         gst=0;
         grandTotal=0;

         fromCustomer = 0;
         double roundOff = 0;


        if(Store.getInstance().fromCustomer.getText()!= null)
        {
            if(!Store.getInstance().fromCustomer.getText().toString().equals("") || TextUtils.isEmpty(Store.getInstance().fromCustomer.getText().toString()))
            {

                try
                {
                    fromCustomer = Double.parseDouble(Store.getInstance().fromCustomer.getText().toString());
                }
                catch (Exception e)
                {

                }



            }

        }

        System.out.println("PROD subtotal = "+subTotal);
        for(int i=0;i<this.productList.size();i++)
        {

            System.out.println("PROD INDEX = "+i);
            System.out.println("PROD ID = "+productList.get(i).getId());

            subTotal =  subTotal +productList.get(i).getFinalPrice();



        }
        System.out.println("PROD subtotal after notify changes= "+subTotal);
        if(DashboardActivity.isGstValue.toLowerCase().contains("no"))
        {
            gst = 0;
        }
        else
        {
            gst = subTotal*(0.06);
        }

        gst = Double.parseDouble(String.format("%.2f",gst));
        subTotal = Double.parseDouble(String.format("%.2f",subTotal));
        grandTotal = gst +subTotal;


        if(fromCustomer==0) {
            tender = 0;
        }
        else
        {
            tender =  fromCustomer  - grandTotal;
        }
        try {
            //roundOff = Double.parseDouble(DashboardActivity.roundOffValue.getText().toString());

        }
        catch (Exception e)
        {
           roundOff =0;
        }
        grandTotal = grandTotal - roundOff;


        Store.getInstance().subTotal.setText(String.valueOf(String.format("%.2f",subTotal)));
        Store.getInstance().GST.setText(String.valueOf( String.format("%.2f", gst)));
        Store.getInstance().grandTotal.setText(String.valueOf( String.format("%.2f", DashboardActivity.roundOff(grandTotal))));
        Store.getInstance().tenderAmount.setText(String.valueOf(String.format("%.2f", 0.00)));
        DashboardActivity.fromCustomer.setText("0");
        DashboardActivity.discountValue.setText("0.00");
        //DashboardActivity.roundOffValue.setText("0");

        Store.getInstance().grandTotalRef = grandTotal;







    }
}
