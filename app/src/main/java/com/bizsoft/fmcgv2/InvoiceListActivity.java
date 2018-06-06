/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.adapter.FileMangerAdpater;
import com.bizsoft.fmcgv2.dataobject.BizFile;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.UIUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class InvoiceListActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<BizFile> fileList = new ArrayList<BizFile>();
    ArrayList<BizFile> allFileList = new ArrayList<BizFile>();
    BizUtils bizUtils;

    EditText searchBar;
    ArrayList<BizFile> filterFileList;
    FileMangerAdpater fa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_list);
        UIUtil.setActionBarMenu(InvoiceListActivity.this,getSupportActionBar(),"Invoice List");
        listView = (ListView) findViewById(R.id.listview);

        bizUtils = new BizUtils();

        searchBar = (EditText) findViewById(R.id.search_bar);


        getSupportActionBar().setTitle("Invoice list");
        String path = Environment.getExternalStorageDirectory() +
                File.separator + "fmcg";
        Log.d("Files", "Path: " + path);
        File directory = new File(path);
        final File[] files = directory.listFiles();

        if(files!=null) {

            Log.d("Files", "Size: " + files.length);


            for (int i = 0; i < files.length; i++) {
                BizFile bizFile = new BizFile();
                bizFile.setFileName(files[i].getName());
                bizFile.setFileSize(files[i].getUsableSpace());
                bizFile.setDetails(files[i]);
                bizFile.setDate(String.valueOf(files[i].lastModified()));

                if(bizFile.getFileName().contains("| "+ Store.getInstance().dealerId)) {
                    fileList.add(bizFile);
                    allFileList.add(bizFile);
                }
                Log.d("Files", "FileName:" + files[i].getName());
            }
        }
        else
        {
            Toast.makeText(this, "No record found...", Toast.LENGTH_SHORT).show();
        }

        Collections.reverse(fileList);

       fa = new FileMangerAdpater(InvoiceListActivity.this, fileList);
        listView.setAdapter(fa);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {



                openPDF(fileList.get(position).getDetails());
            }
        });
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                filterFileList = new ArrayList<BizFile>();
                System.out.println("Adding all the files  "+allFileList.size());

                filterFileList.addAll(allFileList);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Char Sequence = "+s);
                filterFileList.clear();
                if(TextUtils.isEmpty(s) | s.equals("") | s==null)
                {
                    System.out.println("Adding all the files  "+fileList.size());


                    filterFileList.addAll(allFileList);
                }
                else {

                    for (int i = 0; i < fileList.size(); i++) {

                        if (fileList.get(i).getFileName().toLowerCase().contains(s.toString().toLowerCase())) {
                            filterFileList.add(fileList.get(i));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Adding customer list size"+ filterFileList.size());

                System.out.println("Adding customer list size"+fa.fileList.size());
                fa.fileList.clear();
                Collections.reverse(filterFileList);
                fa.fileList.addAll(filterFileList);

                System.out.println("Adding customer list size"+fa.fileList.size());

                fa.notifyDataSetChanged();
            }
        });

    }
    public  void  openPDF(File file)
    {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
// Just example, you should parse file name for extension
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(".pdf");

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), mime);
        startActivityForResult(intent, 10);
    }
}
