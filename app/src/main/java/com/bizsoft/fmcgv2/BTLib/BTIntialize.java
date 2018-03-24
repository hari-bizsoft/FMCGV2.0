package com.bizsoft.fmcgv2.BTLib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.bizsoft.fmcgv2.DashboardActivity;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Store;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by shri on 16/8/17.
 */

public class BTIntialize {

    private static final int BLUETOOTH_FLAG = 619;

    public static void start(Context context)
    {
        DashboardActivity dashboardActivity = new DashboardActivity();
        try
        {
            if(BTPrint.btsocket==null)
            {
                Intent intent = new Intent(context,BTDeviceList.class);

                ((Activity) context).startActivityForResult(intent,BLUETOOTH_FLAG);


                Toast.makeText(context, "new socket", Toast.LENGTH_SHORT).show();


            }
            else
            {


                Customer customer = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);


                System.out.println("Sales List "+customer.getSale().size());
                System.out.println("Sales Order List "+customer.getSaleOrder().size());

                if(customer.getSale().size()>0)
                {

                    dashboardActivity.print(customer,"Sale Bill",customer.getSale(), customer.getSalesOfCustomer().get(customer.getSalesOfCustomer().size() - 1), customer.getSaleOrdersOfCustomer().get(customer.getSaleOrdersOfCustomer().size() - 1), customer.getSaleReturnOfCustomer().get(customer.getSaleReturnOfCustomer().size() - 1));
                }
                else if (customer.getSaleOrder().size()>0) {
                    dashboardActivity.print(customer,"Sale Order Bill",customer.getSaleOrder(), customer.getSalesOfCustomer().get(customer.getSalesOfCustomer().size() - 1), customer.getSaleOrdersOfCustomer().get(customer.getSaleOrdersOfCustomer().size() - 1), customer.getSaleReturnOfCustomer().get(customer.getSaleReturnOfCustomer().size() - 1));
                }


            }
        }
        catch (Exception e)
        {
            System.out.println("Exception "+e);

        }
    }


    public static void onActivityResult(int requestCode, int resultCode, Intent data) {

        System.out.println("Result code === "+resultCode);
        System.out.println("requestCode === "+requestCode);
        System.out.println("data === "+data);



        try {


            BTPrint.btsocket = BTDeviceList.getSocket();
            if (BTPrint.btsocket != null) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                OutputStream opstream = null;
                try {
                    opstream = BTPrint.btsocket.getOutputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                BTPrint.btoutputstream = opstream;
            }

        }
        catch (Exception e)
        {

        }

    }

}
