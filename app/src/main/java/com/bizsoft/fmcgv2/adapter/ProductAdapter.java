package com.bizsoft.fmcgv2.adapter;

import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.DashboardActivity;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Store;

import java.util.ArrayList;

import static com.bizsoft.fmcgv2.DashboardActivity.discountType;

/**
 * Created by shri on 10/8/17.
 */

public class ProductAdapter extends BaseAdapter   {

    Context context;
    public ArrayList<Product> productList = new ArrayList<Product>();
    LayoutInflater layoutInflater= null;
    private int x = 0;
    private int Object = 1;
    private String nQuantity;

    public ProductAdapter(Context context,ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
        this.layoutInflater = (LayoutInflater.from(context));
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

        Long id = Long.valueOf(0);
        try {
            id = this.productList.get(position).getId();
        }
        catch (Exception e)
        {
            System.out.println("Exception in getting prod id  ");
        }

        return id ;
    }

    public void add(Product product) {

        this.productList.clear();
        this.productList  = new ArrayList<Product>();
        this.productList.add(product);
    }

    class Holder
    {
        TextView id,name,price,company,stock;
        TextView quantity;
        ImageButton plus,minus;
        TextView calculatedAmount;
        Button add;
        TextView reason;
        CheckBox isResale;
        TextView discountLabel,finalPrice;
        EditText discount;
        TextView productCode;



    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.product_single_item, null);


        final Product product = getItem(position);

        try {


            holder.id = (TextView) convertView.findViewById(R.id.sale_id);
            holder.name = (TextView) convertView.findViewById(R.id.customer_name);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.stock = (TextView) convertView.findViewById(R.id.stock_left);
            holder.reason = (TextView) convertView.findViewById(R.id.particulars);
            holder.isResale = (CheckBox) convertView.findViewById(R.id.is_resale);
            holder.calculatedAmount = (TextView) convertView.findViewById(R.id.calculated_amount);

            holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            holder.discount = (EditText) convertView.findViewById(R.id.discount);
            holder.discountLabel = (TextView) convertView.findViewById(R.id.discount_label);
            holder.finalPrice = (TextView) convertView.findViewById(R.id.final_price);

            holder.plus = (ImageButton) convertView.findViewById(R.id.plus);
            holder.minus = (ImageButton) convertView.findViewById(R.id.minus);
            holder.add = (Button) convertView.findViewById(R.id.add);

            holder.productCode = (TextView) convertView.findViewById(R.id.product_code);
            holder.id.setText(String.valueOf(product.getId()));
            holder.name.setText(String.valueOf(product.getProductName()));
            holder.price.setText(String.valueOf(String.format("%.2f", product.getMRP())));
            holder.finalPrice.setText(String.valueOf(String.format("%.2f", product.getMRP())));
            holder.stock.setText(String.valueOf(product.getAvailableStock()));
            holder.calculatedAmount.setText("0.00");
            holder.productCode.setText(String.valueOf(product.getItemCode()));

            System.out.println("Product Code ====="+product.getItemCode());

            holder.quantity.setText(String.valueOf("0"));
            holder.add.setVisibility(View.GONE);

            holder.quantity.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    showNumberPicker(product, holder);

                }
            });

            holder.price.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    chooeseSellingRatePicker(product, holder);

                }
            });

            if (!DashboardActivity.currentSaleType.toLowerCase().contains("return")) {
                holder.isResale.setVisibility(View.GONE);
                holder.reason.setVisibility(View.GONE);
                product.setResale(true);
                product.setParticulars("Not required");

                System.out.println(" set value");

            }
            System.out.println("Reason ==" + product.getParticulars());
            if (product.getParticulars() == null) {

                product.setResale(false);
                holder.reason.setText(String.valueOf("Unspecified"));
                product.setParticulars("Unspecified");


            } else {
                holder.reason.setText(product.getParticulars());

            }


            if (discountType.toLowerCase().contains("no")) {
                holder.finalPrice.setVisibility(View.GONE);
                holder.discountLabel.setVisibility(View.GONE);
                holder.discount.setVisibility(View.GONE);

            } else {

                if (discountType.toLowerCase().contains("total")) {

                    holder.finalPrice.setVisibility(View.GONE);
                    holder.discountLabel.setVisibility(View.GONE);
                    holder.discount.setVisibility(View.GONE);

                } else {
                    holder.finalPrice.setVisibility(View.VISIBLE);
                    holder.discountLabel.setVisibility(View.VISIBLE);
                    holder.discount.setVisibility(View.VISIBLE);
                }
            }


            holder.discount.addTextChangedListener(new TextWatcher() {
                double gt = 0;
                double dc = 0;
                double tempGt = 0;
                double da = 0;


                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    tempGt = Double.parseDouble(holder.calculatedAmount.getText().toString());
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    try {

                        if (TextUtils.isEmpty(holder.discount.getText())) {
                            dc = 0;
                        } else {
                            dc = Double.parseDouble(holder.discount.getText().toString());
                        }


                    } catch (Exception e) {
                        System.out.print("Ex =>>>>> " + e);
                    }
                    System.out.println("tgt = <><><>" + tempGt);
                    System.out.println("dc = <><><>" + dc);
                    if (dc >= 100) {
                        Toast.makeText(context, "Discount not applicable..", Toast.LENGTH_SHORT).show();
                    } else {
                        if (dc >= 100) {
                            if (tempGt != 0) {
                                Toast.makeText(context, "Discount not applicable..", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            da = tempGt * (dc / 100);
                            gt = tempGt - (tempGt * (dc / 100));

                        }

                    }
                    holder.finalPrice.setText(String.valueOf(String.format("%.2f", gt)));
                    product.setDiscountAmount(da);
                    product.setFinalPrice(gt);

                    System.out.println("GT ====<ON>" + gt);
                    System.out.println("GT ====<ON>" + product.getFinalPrice());


                }

                @Override
                public void afterTextChanged(Editable s) {
                    //  product.setDiscountAmount(dc);
                    //  product.setFinalPrice(gt);
                    //   System.out.println("GT ====<AFR>"+gt);
                    //  System.out.println("GT ====<ON>"+product.getFinalPrice());

                }
            });


            holder.isResale.setEnabled(false);


            holder.reason.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    System.out.println("Reason ==" + product.getParticulars());
                    if (product.getParticulars() == null) {

                        product.setResale(false);


                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    if (TextUtils.isEmpty(holder.reason.getText().toString().trim())) {
                        holder.isResale.setEnabled(false);
                    } else {
                        holder.isResale.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {


                    holder.isResale.setChecked(false);

                    product.setParticulars(holder.reason.getText().toString());
                    product.setResale(false);
                }
            });


            holder.isResale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (TextUtils.isEmpty(holder.reason.getText())) {

                        holder.reason.setError("Set reason first..");

                    } else {
                        if (holder.isResale.isChecked()) {

                            product.setResale(true);

                            System.out.println("Resale Checked...");

                            if (holder.reason.getText() != null) {
                                System.out.println("Checked-Set value from edittext" + (holder.reason.getText().toString()));
                                //  holder.reason.setText((holder.reason.getText().toString()));
                                product.setParticulars(holder.reason.getText().toString());
                            } else {
                                System.out.println("Checked-Set value manually");
                                holder.reason.setText("Not required");
                                product.setParticulars("Not required");
                            }


                        } else {
                            System.out.println("Resale UNChecked...");
                            product.setResale(false);
                            if (holder.reason.getText() != null) {
                                System.out.println("UNChecked-Set value from edittext" + (holder.reason.getText().toString()));

                                product.setParticulars(holder.reason.getText().toString());
                            } else {

                                System.out.println("UNChecked-Set value manually");
                                holder.reason.setText("Other reason");
                                product.setParticulars("Other reason");
                            }

                        }
                    }


                }
            });


            holder.plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    x = 0;
                    try {
                        x = Integer.parseInt(holder.quantity.getText().toString());


                        if(DashboardActivity.currentSaleType.toLowerCase().contains("order") | DashboardActivity.currentSaleType.toLowerCase().contains("return"))
                        {

                            x++;
                            holder.quantity.setText(String.valueOf(x));
                            product.setQty(Long.valueOf(x));
                            holder.calculatedAmount.setText(String.valueOf(String.format("%.2f", x * product.getMRP())));
                            holder.finalPrice.setText(String.valueOf(String.format("%.2f", x * product.getMRP())));

                        }
                        else {

                            if (x < product.getAvailableStock()) {
                                x++;
                                holder.quantity.setText(String.valueOf(x));

                                product.setQty(Long.valueOf(x));


                                holder.calculatedAmount.setText(String.valueOf(String.format("%.2f", x * product.getMRP())));
                                holder.finalPrice.setText(String.valueOf(String.format("%.2f", x * product.getMRP())));
                            } else {
                                Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                            }
                        }
                    } catch (Exception e) {
                        Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    x = 0;

                    try {
                        x = Integer.parseInt(holder.quantity.getText().toString());

                        if(DashboardActivity.currentSaleType.toLowerCase().contains("order") | DashboardActivity.currentSaleType.toLowerCase().contains("return"))
                        {
                            if (x >= 1) {
                                x--;
                                holder.quantity.setText(String.valueOf(x));

                                holder.calculatedAmount.setText(String.valueOf(String.format("%.2f", x * product.getMRP())));
                                holder.finalPrice.setText(String.valueOf(String.format("%.2f", x * product.getMRP())));
                            } else {
                                Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                            }


                        }
                        else {
                            if (x >= 1) {
                                x--;
                                holder.quantity.setText(String.valueOf(x));

                                holder.calculatedAmount.setText(String.valueOf(String.format("%.2f", x * product.getMRP())));
                                holder.finalPrice.setText(String.valueOf(String.format("%.2f", x * product.getMRP())));
                            } else {
                                Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                            }

                        }


                    } catch (Exception e) {
                        Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    System.out.println("Size of cart list" + Store.getInstance().addedProductList.size());

                    if (Store.getInstance().addedProductList.size() > 0) {

                        for (int i = 0; i < Store.getInstance().addedProductList.size(); i++) {
                            if (Store.getInstance().addedProductList.get(i).getId() != product.getId()) {

                                DashboardActivity.productName.setText(String.valueOf(product.getProductName()));

                                if (x == 0) {
                                    x = 1;
                                    System.out.println("X=" + x);
                                }

                                product.setQty(Long.valueOf(x));
                                Store.getInstance().addedProductList.add(product);


                                Store.getInstance().addedProductAdapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(context, "product already added", Toast.LENGTH_SHORT).show();
                            }

                        }
                    } else {


                        if (x == 0) {
                            x = 1;
                            System.out.println("X=" + x);
                        }
                        product.setQty(Long.valueOf(x));
                        Store.getInstance().addedProductList.add(product);

                        DashboardActivity.productName.setText(String.valueOf(product.getProductName()));
                        System.out.println("========" + Store.getInstance().addedProductList.get(0).getQty());
                        Store.getInstance().addedProductAdapter.notifyDataSetChanged();
                    }


                }
            });

        }
        catch (Exception e)
        {
            System.out.println("exception ==="+e);
        }




        return convertView;
    }
    public void showNumberPicker(final Product prod, final Holder holder)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("Choose Quantity");
        dialog.setContentView(R.layout.number_picker);
        NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.np);
        final TextView q = (TextView) dialog.findViewById(R.id.tv);
        q.setText(String.valueOf("Set selling price"));
        numberPicker.setMinValue(0);
        //Specify the maximum value/number of NumberPicker
        String s = String.valueOf(prod.getAvailableStock());
        numberPicker.setMaxValue(Integer.parseInt(s));

        //Gets whether the selector wheel wraps when reaching the min/max value.
        numberPicker.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                x = 0;

                try {
                    x = Integer.parseInt(holder.quantity.getText().toString());
                    if (x >= 1) {

                        x = newVal;
                        prod.setQty(Long.valueOf(x));
                        holder.quantity.setText(String.valueOf(x));

                        holder.calculatedAmount.setText(String.valueOf(String.format("%.2f", x * prod.getMRP())));
                        holder.finalPrice.setText(String.valueOf(String.format("%.2f", x * prod.getMRP())));
                    } else {
                        Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
    public void chooeseSellingRatePicker(final Product prod, final Holder holder)
    {
        final Dialog dialog = new Dialog(context);
        dialog.setTitle("Choose Selling Rate");
        dialog.setContentView(R.layout.number_picker);
        NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.np);
        final TextView q = (TextView) dialog.findViewById(R.id.tv);
        q.setText("Choose Selling Rate");

        String m = String.valueOf(prod.getMinSellingRate());
        numberPicker.setMinValue(Integer.parseInt(m));
        //Specify the maximum value/number of NumberPicker
        String s = String.valueOf(prod.getMaxSellingRate());
        numberPicker.setMaxValue(Integer.parseInt(s));

        //Gets whether the selector wheel wraps when reaching the min/max value.
        numberPicker.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker
                double x = 0;
                int q = 0;
                try {
                      q = Integer.parseInt(holder.quantity.getText().toString());
                    x = newVal;
                    if (x >= 1) {

                        x = newVal;

                        prod.setSellingRate((x));
                        holder.price.setText(String.valueOf(x));

                        holder.calculatedAmount.setText(String.valueOf(String.format("%.2f", q* prod.getMRP())));
                        holder.finalPrice.setText(String.valueOf(String.format("%.2f", q * prod.getMRP())));
                    } else {
                        Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(context, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
}
