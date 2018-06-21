/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.PrintPreview;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.BizFile;
import com.bizsoft.fmcgv2.dataobject.Company;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Created by GopiKing on 31-08-2017.
 */

public class FileMangerAdpater extends BaseAdapter{

    Context context;
    LayoutInflater layoutInflater= null;
    public ArrayList<BizFile> fileList = new ArrayList<BizFile>();
    private Dialog dialog;


    public FileMangerAdpater(Context context,ArrayList<BizFile> fileList) {
        this.context = context;
        this.layoutInflater = (LayoutInflater.from(context));
        this.fileList = fileList;
    }
    @Override
    public int getCount() {
        return this.fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return this.fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    class  Holder
    {
        TextView fileName;
        Button sendMail,view;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        convertView = layoutInflater.inflate(R.layout.file_list_single_item, null);

        holder.fileName = (TextView) convertView.findViewById(R.id.file_name);
        holder.sendMail= (Button) convertView.findViewById(R.id.send_mail);
        holder.view= (Button) convertView.findViewById(R.id.view);
        holder.fileName.setText(String.valueOf(fileList.get(position).getFileName()));

        holder.sendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    sendMail(fileList.get(position).getDetails().getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                try {
                    openPDF(fileList.get(position).getDetails());
                } catch (ActivityNotFoundException ex)
                {
                    Toast.makeText(context, "No Suitable App found to open this type of file..\nOpening playstore", Toast.LENGTH_LONG).show();
                    final String appPackageName = "com.adobe.reader"; // getPackageName() from Context or Activity object
                    try {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        return convertView;
    }
    public void sendMail(final String absolutePath) throws Exception {


        dialog = new Dialog(context);
        dialog.setContentView(R.layout.email_prompt);
        final EditText mail_id = (EditText) dialog.findViewById(R.id.mail_id);
        final Button send = (Button) dialog.findViewById(R.id.send);
        send.setEnabled(false);
        send.setBackgroundColor(context.getResources().getColor(R.color.grey));
        dialog.show();

        final String[] receipientMail = {"hari.bizsoft@gmail.com"};
        mail_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(mail_id.getText()!=null) {
                    if(isValid(mail_id.getText().toString()))
                    {
                        send.setEnabled(true);
                        send.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryDark));
                        receipientMail[0] = mail_id.getText().toString();
                    }
                    else
                    {
                        send.setEnabled(false);
                        send.setBackgroundColor(context.getResources().getColor(R.color.grey));
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {


            }
        });

        final Company company = BizUtils.getCompany();




        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundMail.newBuilder(context)
                        .withUsername("naveen.bizsoft@gmail.com")
                        .withPassword("navin0330")
                        .withMailto(receipientMail[0])
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject("Invoice copy")
                        .withBody("This is a digitally generated invoice copy from ."+ company.getCompanyName())
                        .withAttachments(absolutePath)
                        .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                            @Override
                            public void onSuccess() {
                                //do some magic
                                Toast.makeText(context, "Success..", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        })
                        .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                            @Override
                            public void onFail() {
                                //do some magic
                            }
                        })
                        .send();


            }
        });


    }
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public  void openPDF(File file)
    {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
// Just example, you should parse file name for extension
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".pdf");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mime);
        ((Activity) context).startActivityForResult(intent, 10);

    }
}
