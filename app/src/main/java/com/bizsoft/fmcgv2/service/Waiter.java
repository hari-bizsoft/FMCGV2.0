/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.service;

import android.content.Context;
import android.util.Log;

import com.bizsoft.fmcgv2.dataobject.Store;

public class Waiter extends Thread
{
    private static final String TAG=Waiter.class.getName();
    private long lastUsed;
    private long period;
    private boolean stop;
    private BizUtils bizUtils;
    Context context;
    public  long idle;

    public Waiter(long period,Context context)
    {
        this.period=period;
        stop=false;
        this.context = context;
    }

    public void run()
    {
         idle=0;
        this.touch();
        do
        {
            idle=System.currentTimeMillis()-lastUsed;
            Log.d(TAG, "Application is idle for "+idle +" ms");




            System.out.println("Network State 1 >>>"+Network.isNetworkAvailable(context));
            if(Network.isNetworkAvailable(context)) {
                if (Store.getInstance().mHubConnectionReceiver != null) {
                    if (Store.getInstance().mHubConnectionReceiver.getState().toString().toLowerCase().contains("disconnected")) {
                        System.out.println("----re login attempt");
                        SignalRService.reconnect(context);
                    }
                }
            }
            else
            {
                Log.d("Network Info","No connection");
            }


            try
            {
                Thread.sleep(5000); //check every 5 seconds



            }
            catch (Exception e)
            {
                Log.d(TAG, "com.bizsoft.fmcgv2.service.Waiter interrupted!");
            }
            if(idle > period)
            {

                bizUtils = new BizUtils();

                if(bizUtils.isNetworkConnected(context))
                {
                    try {
                        System.out.println("Ready to sync now...");
                        idle = 0;
                        bizUtils.sync(context, "auto");
                    }
                    catch (Exception e)
                    {
                        Log.e("Sync auto","error");
                        System.err.println(e);
                    }




                }
                else
                {

                    System.out.println("OFFLINE - Unable to sync now...");

                }

                idle=0;
                //do something here - e.g. call popup or so
            }
        }
        while(!stop);
        Log.d(TAG, "Finishing com.bizsoft.fmcgv2.service.Waiter thread");
    }

    public synchronized void touch()
    {
        lastUsed=System.currentTimeMillis();
    }


    public synchronized void forceInterrupt()
    {
        this.interrupt();
    }

    //soft stopping of thread
//soft stopping of thread

    public synchronized void setPeriod(long period)
    {
        this.period=period;
    }

}
