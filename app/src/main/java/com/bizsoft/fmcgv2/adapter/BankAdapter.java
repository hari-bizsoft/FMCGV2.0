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
import com.bizsoft.fmcgv2.Tables.Bank;

import java.util.ArrayList;

/**
 * Created by GopiKing on 05-04-2018.
 */

public class BankAdapter extends BaseAdapter{
    Context context;
    LayoutInflater layoutInflater= null;
    public ArrayList<Bank> bankLists = new ArrayList<>();



    public BankAdapter(Context context, ArrayList<Bank> bankLists) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.bankLists = bankLists;
    }


    @Override
    public int getCount() {
        return this.bankLists.size();
    }

    @Override
    public Object getItem(int position) {
        return this.bankLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.bankLists.get(position).getLedger().getId();
    }
    class Holder
    {

        TextView bankName;
        TextView accNumber;
        TextView accName;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.bank_single_item, null);
        holder.bankName = (TextView) convertView.findViewById(R.id.bank_name);
        holder.accName = (TextView) convertView.findViewById(R.id.acc_no);
        holder.accNumber = (TextView) convertView.findViewById(R.id.acc_name);


        holder.bankName.setText(String.valueOf(bankLists.get(position).getLedger().getLedgerName()));
        holder.accNumber.setText(String.valueOf(bankLists.get(position).getAccountNo()));
        holder.accName.setText(String.valueOf(bankLists.get(position).getBankAccountName()));

        return convertView;
    }

}
