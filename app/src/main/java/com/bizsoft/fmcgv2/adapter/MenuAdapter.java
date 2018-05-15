package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.MenuItems;
import com.bizsoft.fmcgv2.dataobject.Store;

import java.util.ArrayList;

/**
 * Created by GopiKing on 18-04-2018.
 */
public class MenuAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater= null;
    ArrayList<MenuItems> menuItems;

    public MenuAdapter(Context context,ArrayList<MenuItems> menuItems) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.menuItems = menuItems;
    }

    @Override
    public int getCount() {
        return this.menuItems.size();
    }

    @Override
    public Object getItem(int position) {
        return this.menuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    class Holder
    {

        TextView name;
        ImageView icon;


    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        MenuItems menuItems = (MenuItems) getItem(position);
        convertView = layoutInflater.inflate(R.layout.menu_single_item, null);
        holder.name = (TextView) convertView.findViewById(R.id.name);
        holder.icon= (ImageView) convertView.findViewById(R.id.icon);
        holder.name.setText(String.valueOf(menuItems.getName()));
        holder.icon.setImageResource(menuItems.getImageResId());
        return convertView;
    }
}
