/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.service;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.bizsoft.fmcgv2.R;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;


/**
 * Created by GopiKingMaker on 3/31/2017.
 */

public class Network {

    private static Network instance = new Network();
    static Context context;
    static ConnectivityManager connectivityManager;
    static boolean connected = false;

    static final String TAG = "Network_State";

    public static Network getInstance(Context ctx) {
        context = ctx.getApplicationContext();
        return instance;
    }

    public static  boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


    public static void Check_Internet(final Context context, String title, String message){
        try{

            AlertDialog.Builder iadb = new AlertDialog.Builder(context);
            iadb.setTitle(title);
            iadb.setIcon(R.drawable.fmcglogo64);
            iadb.setMessage(message);
            iadb.setNeutralButton("Enable Mobile Data", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
//                    getMobileDataState();
//                    setMobileDataState(getMobileDataState());
//                    createNetErrorDialog();
                    try {
                        setMobileDataEnabled(context,true);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            });
            iadb.setPositiveButton("Enable WIFI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{

                        WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                        wifi.setWifiEnabled(true);

                    }catch (Exception e){
                        Toast.makeText(context, e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                }
            });
            AlertDialog iad = iadb.create();
            iad.setIcon(R.drawable.fmcglogo64);
            iad.show();

        }catch (Exception ex){}
    }

    public static void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {

        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setDataEnabled ", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        try {
            setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    public void setMobileDataState(Context context,boolean mobileDataEnabled)
    {
        try
        {
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            Method setMobileDataEnabledMethod = telephonyService.getClass().getDeclaredMethod("setDataEnabled", boolean.class);

            if (null != setMobileDataEnabledMethod)
            {
                setMobileDataEnabledMethod.invoke(telephonyService, mobileDataEnabled);
            }
        }
        catch (Exception ex)
        {
            Log.e(TAG, "Error setting mobile data state", ex);
        }
    }
    public static void sendMail(Context context, String subject, String body){
        try{

            Intent email = new Intent(Intent.ACTION_SEND);
            email.setType("text/plain");
            email.putExtra(Intent.EXTRA_SUBJECT,subject);
            email.putExtra(Intent.EXTRA_TEXT, body);
            context.startActivity(Intent.createChooser(email,"Select E-Mail Client"));

        }catch (Exception e){
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


  public static boolean ping(String ip, int port) {

      boolean exists = false;

      try {

          SocketAddress sockaddr = new InetSocketAddress(ip, port);
          // Create an unbound socket
          Socket sock = new Socket();

          // This method will block no more than timeoutMs.
          // If the timeout occurs, SocketTimeoutException is thrown.
          int timeoutMs = 2000;   // 2 seconds
          sock.connect(sockaddr, timeoutMs);
          exists = true;
      } catch (IOException e) {
          // Handle exception
      }
      return exists;
  }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
