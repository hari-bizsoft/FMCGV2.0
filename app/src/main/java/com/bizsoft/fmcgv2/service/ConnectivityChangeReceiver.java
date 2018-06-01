package com.bizsoft.fmcgv2.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.bizsoft.fmcgv2.LoginActivity;
import com.bizsoft.fmcgv2.dataobject.Store;

/**
 * Created by GopiKing on 31-05-2018.
 */
public class ConnectivityChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        debugIntent(intent, "Network State");



        try
        {
            boolean stat = Network.getInstance(context).isOnline();
            Log.d("test network", String.valueOf(stat));
            if(stat) {
                LoginActivity.serverStatus.setText("online");
                Store.getInstance().serverStatus = "online";
                LoginActivity.serverStatus.setTextColor(Color.GREEN);
            }
            else
            {
                LoginActivity.serverStatus.setText("offline");
                Store.getInstance().serverStatus = "offline";
                LoginActivity.serverStatus.setTextColor(Color.RED);
            }
        }
        catch (Exception e)
        {

        }

    }
    private void debugIntent(Intent intent, String tag) {
        Log.v(tag, "action: " + intent.getAction());
        Log.v(tag, "component: " + intent.getComponent());

        Bundle extras = intent.getExtras();
        if (extras != null) {
            for (String key: extras.keySet()) {
                Log.v(tag, "key [" + key + "]: " +
                        extras.get(key));
            }
        }
        else {
            Log.v(tag, "no extras");
        }


    }
}
