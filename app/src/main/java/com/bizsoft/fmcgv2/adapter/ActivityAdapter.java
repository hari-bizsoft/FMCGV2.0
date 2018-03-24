package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Notification;
import com.bizsoft.fmcgv2.dataobject.Store;

import java.util.ArrayList;

/**
 * Created by GopiKing on 13-03-2018.
 */

public class ActivityAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater= null;
    public ArrayList<Notification> notificationList  = new ArrayList<Notification>();

    public ActivityAdapter(Context context, ArrayList<Notification> notificationList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.notificationList = notificationList;
    }

    @Override
    public int getCount() {
        return this.notificationList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.notificationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    class Holder
    {
        TextView message;
        TextView time;

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        Notification notification = (Notification) getItem(position);


        convertView = layoutInflater.inflate(R.layout.activity_single_item, null);
        holder.message = (TextView) convertView.findViewById(R.id.message);
        holder.time= (TextView) convertView.findViewById(R.id.time);

        holder.message.setText(String.valueOf(notification.getMessage()));
        holder.time.setText(String.valueOf(notification.getTime()));
        return convertView;

    }
}
