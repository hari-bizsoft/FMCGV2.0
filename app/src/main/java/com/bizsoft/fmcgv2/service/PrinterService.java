/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.text.TextUtils;

import com.bizsoft.fmcgv2.BTLib.BTPrint;
import com.bizsoft.fmcgv2.DashboardActivity;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Sale;
import com.bizsoft.fmcgv2.dataobject.SaleOrder;
import com.bizsoft.fmcgv2.dataobject.SaleReturn;
import com.bizsoft.fmcgv2.dataobject.Store;

import java.util.ArrayList;
import java.util.Formatter;

import static android.content.Context.MODE_PRIVATE;

import static com.bizsoft.fmcgv2.service.BizUtils.getTransactionType;

/**
 * Created by GopiKing on 23-05-2018.
 */
public class PrinterService  {

    public static void print(Context context, Customer customer, String type, ArrayList<Product> products, Sale sale, SaleOrder saleOrder, SaleReturn saleReturn, String copyName) {

        Store.getInstance().printerContext = context;

        double mainSubTotal = 0;
        double mainGst = 0;
        double mainGrantTotal = 0;

        SharedPreferences prefs = context.getSharedPreferences(Store.getInstance().MyPREFERENCES, MODE_PRIVATE);
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine(prefs.getString(context.getString(R.string.companyName), "Aboorvass"));
        BTPrint.SetAlign(Paint.Align.CENTER);


        System.out.println("company address line 1 ==============================" + prefs.getString(context.getString(R.string.companyAddressLine1), "0"));
        System.out.println("company address line 2 ==============================" + prefs.getString(context.getString(R.string.companyAddressLine2), "0"));
        System.out.println("company gst ==============================" + prefs.getString(context.getString(R.string.gstNo), "0"));
        System.out.println("company mail ==============================" + prefs.getString(context.getString(R.string.email), "0"));
        System.out.println("company postalcode ==============================" + prefs.getString(context.getString(R.string.postal_code), "0"));

        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine(prefs.getString(context.getString(R.string.companyAddressLine1), "0") + "," + prefs.getString(context.getString(R.string.companyAddressLine2), "0") + "," + prefs.getString(context.getString(R.string.postal_code), "+1234556789"));

        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("E-Mail: " + prefs.getString(context.getString(R.string.email), "+1234556789"));
        BTPrint.PrintTextLine("GST No: " + prefs.getString(context.getString(R.string.gstNo), "+1234556789"));
        BTPrint.PrintTextLine("Ph No: " + prefs.getString(context.getString(R.string.phoneNumber), "+1234556789"));
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("***Bill Details***");
        BizUtils bizUtils = new BizUtils();
        String refNo = "";
        String paymentModeValue = "";
        String mgt = "0.00";
        String ra="0.00",ba="0.00";
        Double disV = 0.00;
        double roundOffValue = 0.00;
        String discountPer = "0.00";
        String disType ="";
        String grandTotalDiscountType="";

        if(sale!=null) {
            refNo = sale.getRefCode();
            paymentModeValue = sale.getPaymentMode();
            ra = String.valueOf(String.format("%.2f", sale.getReceivedAmount()));
            ba = String.valueOf(String.format("%.2f", sale.getBalance()));

            mainSubTotal = sale.getSubTotal();
            mainGst = sale.getGst();
            disV = sale.getDiscountValue();
            roundOffValue =  sale.getRoundOffValue();

            mainGrantTotal = sale.getGrandTotal();
            ba = String.valueOf(sale.getBalance());
            discountPer = String.format("%.2f",sale.getDiscountPercentage());
            disType = sale.getDiscountType();
            grandTotalDiscountType = sale.getGrandTotalDiscountType();

        }
        if(saleOrder !=null)
        {
            refNo = saleOrder.getRefCode();
            mainSubTotal = saleOrder.getSubTotal();
            paymentModeValue = saleOrder.getPaymentMode();
            ra = String.valueOf(String.format("%.2f", saleOrder.getReceivedAmount()));
            ba = String.valueOf(String.format("%.2f", saleOrder.getBalance()));

            disV = saleOrder.getDiscountValue();

            mainGst = saleOrder.getGst();
            roundOffValue =  saleOrder.getRoundOffValue();
            mainGrantTotal = saleOrder.getGrandTotal();
            ba = String.valueOf(saleOrder.getBalance());
            discountPer = String.format("%.2f",saleOrder.getDiscountPercentage());
            disType = saleOrder.getDiscountType();
            grandTotalDiscountType = saleOrder.getGrandTotalDiscountType();
        }
        if(saleReturn !=null)
        {
            refNo = saleReturn.getRefCode();
            mainSubTotal = saleReturn.getSubTotal();
            paymentModeValue = saleReturn.getPaymentMode();
            ra = String.valueOf(String.format("%.2f", saleReturn.getReceivedAmount()));
            ba = String.valueOf(String.format("%.2f", saleReturn.getBalance()));
            disV = saleReturn.getDiscountValue();
            mainGst = saleReturn.getGst();
            roundOffValue =  saleReturn.getRoundOffValue();
            mainGrantTotal = saleReturn.getGrandTotal();
            ba = String.valueOf(saleReturn.getBalance());
            grandTotalDiscountType = saleReturn.getGrandTotalDiscountType();

        }





        BTPrint.PrintTextLine("Bill Ref No :" + String.valueOf(refNo));
        BTPrint.PrintTextLine("Bill Date :" + bizUtils.getCurrentTime());
        BTPrint.PrintTextLine("------------------------------");
        Customer customer1 = customer;
        if (customer1.getId() == null) {
            BTPrint.PrintTextLine("Customer ID :" + "Unregistered");
        } else {
            BTPrint.PrintTextLine("Customer ID :" + customer1.getId());
        }
        BTPrint.PrintTextLine("Customer Name :" + customer1.getLedger().getLedgerName());
        BTPrint.PrintTextLine("Person In Charge :" + customer1.getLedger().getPersonIncharge());
        BTPrint.PrintTextLine("GST No :" + customer1.getLedger().getGSTNo());
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Sale/Order/Return:" + type);

        if(getTransactionType(type)==Store.getInstance().SALE) {
            if(!paymentModeValue.toLowerCase().contains("none")) {
                BTPrint.PrintTextLine("Payment Mode :" + paymentModeValue);
            }
            if(sale.getPaymentMode().toLowerCase().contains("cheque"))
            {
                BTPrint.PrintTextLine("Cheque No :" + sale.getChequeNo());
            }
        }


        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***ITEM DETAILS***");
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.LEFT);
        for (int i = 0; i < products.size(); i++) {
            Product item = products.get(i);
            String iq = "";
            String ip = "";
            String ir = "";
            String id = "";



            if(item.getDiscountAmount()!=0)
            {
                String itemD = String.valueOf(String.format("%.2f",item.getDiscountAmount()));
                id = itemD;
                if(!TextUtils.isEmpty(id.trim())) {
                    id = String.valueOf(String.format("%.2f", Double.parseDouble(id)));
                }
            }

            ir = String.valueOf(String.format("%.2f",item.getQty() * item.getMRP() - item.getDiscountAmount() ));
            iq = String.valueOf(item.getQty());
            ip = String.valueOf(item.getMRP());


            BTPrint.PrintTextLine(i+1+"."+item.getProductName());

            String qStr = String.format("%4d",  Integer.parseInt(iq));
            qStr = qStr+" ";

            String pStr = String.format("%8.2f",Double.parseDouble(ip));
            pStr = pStr+" ";

            String dStr = "";
            if(TextUtils.isEmpty(id.trim())) {
                dStr = String.format("%7s", id);
            }else
            {
                dStr = "-"+String.format("%6s", id);
            }
            dStr = dStr+" ";

            String rStr = "="+String.format("%8.2f",Double.parseDouble(ir));

            String newPrintStmnt = qStr+"X"+pStr+dStr+rStr;

            BTPrint.PrintTextLine( newPrintStmnt);

        }
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.RIGHT);
        BTPrint.PrintTextLine("Sub total RM " + String.format("%7.2f",mainSubTotal));
        BTPrint.PrintTextLine("GST RM "  + String.format("%7.2f",mainGst));



        if(BizUtils.getDiscountType(disType)==Store.getInstance().DISCOUNT_FOR_GRABD_TOTAL)
        {
            if(grandTotalDiscountType.equals(Store.getInstance().discountTypePercentage)) {
                BTPrint.PrintTextLine("Discount("+discountPer+"%) RM "  + String.format("%7.2f",disV));
            }else if(grandTotalDiscountType.equals(Store.getInstance().discountTypeRM))
            {
                BTPrint.PrintTextLine("Discount("+grandTotalDiscountType+") RM "  + String.format("%7.2f",disV));
            }


        }
        ba = String.format("%.2f",Double.parseDouble(ba));

        BTPrint.PrintTextLine("Round Off RM "  + String.format("%7.2f",roundOffValue));
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Grand total" + " RM " + String.format("%7.2f",mainGrantTotal));
        if(getTransactionType(type)==Store.getInstance().SALE) {
            if(sale.getPaymentMode().toLowerCase().contains("cash")) {
                BTPrint.PrintTextLine("Received amount RM " + String.format("%7.2f", Double.parseDouble(ra)));
                BTPrint.PrintTextLine("Balance amount RM " + String.format("%7.2f", Double.parseDouble(ba)));
            }
        }
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("*****THANK YOU*****");
        BTPrint.SetAlign(Paint.Align.LEFT);
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.PrintTextLine("Dealer Name:" + Store.getInstance().dealerName);
        BTPrint.PrintTextLine("------------------------------");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("Powered By Denariu Soft SDN BHD");
        BTPrint.SetAlign(Paint.Align.CENTER);
        BTPrint.PrintTextLine("***"+copyName+"***");
        BTPrint.printLineFeed();


    }
    public static double bizRound(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
}
