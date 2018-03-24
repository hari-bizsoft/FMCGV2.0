package com.bizsoft.fmcgv2.service;

import android.app.Application;
import android.util.Log;

/**
 * Created by GopiKing on 20-09-2017.
 */

public class ControlApplication extends Application
{
    private static final String TAG=ControlApplication.class.getName();
    private Waiter waiter;  //Thread which controls idle time

    // only lazy initializations here!
    @Override
    public void onCreate()
    {
        super.onCreate();
        Log.d(TAG, "Starting application"+this.toString());
        waiter=new Waiter(15*60*1000,this); //15 mins
        waiter.start();
    }

    public void touch()
    {
        waiter.touch();
    }
}
