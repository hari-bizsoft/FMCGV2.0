/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.BTDevice;

import java.util.ArrayList;

/**
 * Created by vs on 6/20/2018.
 */

public class BluetoothAdapter extends BaseAdapter {


    ArrayList<BTDevice> deviceList;
    Context context;
    LayoutInflater layoutInflater= null;

    public BluetoothAdapter(Context context, ArrayList<BTDevice> deviceList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.deviceList = deviceList;
    }
    @Override
    public int getCount() {
        return deviceList.size();
    }

    @Override
    public Object getItem(int position) {
        return deviceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class Holder
    {
        TextView name;
        TextView address;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.btdevice_single_item, null);
        BTDevice btDevice = (BTDevice) getItem(position);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.address = (TextView) convertView.findViewById(R.id.address);
        holder.name.setText(String.valueOf(btDevice.name));
        holder.address.setText(String.valueOf(btDevice.address));

        return convertView;
    }
}
