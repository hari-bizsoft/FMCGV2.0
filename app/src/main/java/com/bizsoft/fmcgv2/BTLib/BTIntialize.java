/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

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




            }
        }
        catch (Exception e)
        {
            System.out.println("Printer Exception "+e);

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
            System.out.println("Unable to connect...");

        }

    }

}
