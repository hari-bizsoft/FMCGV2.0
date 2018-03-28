package com.bizsoft.fmcgv2.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Store;

import java.util.ArrayList;

/**
 * Created by shri on 22/8/17.
 */
public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private final Context activity;
    private ArrayList<String> asr;
    LayoutInflater layoutInflater= null;
    public Holder holder = null;

    public CustomSpinnerAdapter(Context context,ArrayList<String> asr) {
        this.asr=asr;
        activity = context;
        this.layoutInflater = (LayoutInflater.from(context));
    }



    public int getCount()
    {
        return asr.size();
    }

    public Object getItem(int i)
    {
        return asr.get(i);
    }

    public long getItemId(int i)
    {
        return (long)i;
    }



    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView txt = new TextView(this.activity);
        txt.setPadding(16, 16, 16, 16);
        txt.setTextSize(18);
        txt.setGravity(Gravity.CENTER_VERTICAL);
        txt.setText(asr.get(position));
        txt.setTextColor(Color.parseColor("#000000"));


        return  txt;
    }

    public class  Holder
    {
        public TextView name;
    }
    public View getView(int i, View view, ViewGroup viewgroup) {

         holder = new Holder();
        view = layoutInflater.inflate(R.layout.dropdown_style, null);

        holder.name = (TextView) view.findViewById(R.id.dealer_name);
        Store.getInstance().reprintSpinnerText = holder.name;

        holder.name.setText(String.valueOf(asr.get(i)));
        return  view;

    }


}
