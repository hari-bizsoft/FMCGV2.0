package com.bizsoft.fmcgv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizLogger;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.SignalRService;

import java.io.File;
import java.io.IOException;

public class DownloadDataActivity extends AppCompatActivity {

    ProgressBar progressBar;
    public  TextView textView;
    public static TextView customers;
    public static TextView products;
    public TextView categories;
    public TextView accounts;
    public TextView percentage;
    private ImageView companyLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new BizLogger(DownloadDataActivity.this);



        setContentView(R.layout.activity_download_data);

        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);

        customers = (TextView) findViewById(R.id.customers);
        products = (TextView) findViewById(R.id.products);
        categories = (TextView) findViewById(R.id.stock);
        accounts = (TextView) findViewById(R.id.accounts);
        percentage = (TextView) findViewById(R.id.percentage);
        companyLogo = (ImageView) findViewById(R.id.company_logo);




        new DownloaddData().execute();




    }
    private class DownloaddData extends AsyncTask<Integer,Integer,String>
    {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {

            textView.setText("Download Starting...");
            progressDialog = new ProgressDialog(DownloadDataActivity.this);
            progressDialog.setTitle("Sync status");
            progressDialog.setMessage("Downloading data..");
           // progressDialog.show();

        }


        @Override
        protected String doInBackground(Integer... integers) {


            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if(Store.getInstance().dealerLogo!=null)
                    {
                        Bitmap bmp= BizUtils.StringToBitMap(Store.getInstance().dealerLogo);

                        companyLogo.setImageBitmap(bmp);

                        Store.getInstance().dealerLogoBitmap = bmp;
                    }
                    textView.setText("Downloading customers..");
                }
            });

            SignalRService.customerList(DownloadDataActivity.this);
            SignalRService.cashLedgerId();

           try {
               SignalRService.bankLedgerId();
               SignalRService.bankList();
               SignalRService.SOPendingList();
               SignalRService.Sales_getNewRefNo();
               SignalRService.SalesOrder_getNewRefNo();
               SignalRService.SalesReturn_getNewRefNo();
               SignalRService.Receipt_getNewRefNo();
               SignalRService.UOMList();
           }
           catch (Exception e)
           {
               System.out.println("catch---"+e);
           }
            progressBar.setProgress(25);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    percentage.setText("25%");
                    customers.setText(String.valueOf(Store.getInstance().customerList.size()));

//stuff that updates ui
                    textView.setText("Downloading products..");
                }
            });

            SignalRService.productList(DownloadDataActivity.this);
            progressBar.setProgress(50);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    products.setText(String.valueOf(Store.getInstance().productList.size()));
                    percentage.setText("50%");

                    textView.setText("Downloading categories..");
                }
            });

            SignalRService.stockHomeList();
            SignalRService.productSpecList();
            progressBar.setProgress(75);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    percentage.setText("75%");
                    categories.setText(String.valueOf(Store.getInstance().stockGroupList.size()));

                    textView.setText("Downloading accounts group..");
                }
            });

            SignalRService.accountGroupList();
            SignalRService.getCompanyDetails(DownloadDataActivity.this);
            progressBar.setProgress(100);

          //  SignalRService.taxMasterList();
         //   progressBar.setProgress(100);

            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            percentage.setText("100%");




            accounts.setText(String.valueOf(Store.getInstance().accountsGroupList.size()));
            textView.setText(result);
           // progressDialog.dismiss();


            Store.getInstance().dealer.init();
            Store.getInstance().dealer.setSynced(true);



            finish();


            Intent intent = new Intent(DownloadDataActivity.this,DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);


        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        System.out.println("----------Status-----------------------"+values[0]);
        textView.setText("Running..."+ values[0]);
        progressBar.setProgress(values[0]);
    }
    }

}
