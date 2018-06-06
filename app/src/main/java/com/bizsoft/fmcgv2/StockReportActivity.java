/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.adapter.StockReportAdapter;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.StockGroup;
import com.bizsoft.fmcgv2.dataobject.StockReport;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.SignalRService;
import com.bizsoft.fmcgv2.service.UIUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class StockReportActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<StockReport> stockReports = new ArrayList<>();
    private int currentIndex = 0;
    private StockReportAdapter stockReportadapter;
    private String FLAG_DATE;
    private int year,month,day;
    private EditText from,to;
    private AutoCompleteTextView productName;
    private String fromDateValue,toDateValue;
    private HashMap<String,String> map = new HashMap<String, String>();
    private Product choosedProduct;
    private Button generate;
    private ArrayList<String> allProductList;
    AutoCompleteTextView searchBar;
    private Product searchProduct;
    private HashMap<String, StockReport> stockReportMap;
    ImageButton clear;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_report);
        UIUtil.setActionStockMenu(StockReportActivity.this,getSupportActionBar(),dialog,"Stock Report");


        listView = (ListView) findViewById(R.id.listview);
        searchBar = (AutoCompleteTextView) findViewById(R.id.search_bar);
        clear = (ImageButton) findViewById(R.id.clear);

        stockReportadapter = new StockReportAdapter(stockReports,StockReportActivity.this);
        listView.setAdapter(stockReportadapter);


        try {

            loadStockReport();
            allProductList = Product.getNames();
            loadProductNameForSearch(searchBar);
            clear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    searchBar.setText("");
                    stockReports.clear();
                    stockReportadapter.notifyDataSetChanged();
                    loadStockReport();

                }
            });
        }
        catch (Exception e)
        {

        }


    }



    public void showDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setTitle("Filter");
        dialog.setContentView(R.layout.filter_dialog);


        dialog.getWindow().setLayout(Store.getInstance().width, ConstraintLayout.LayoutParams.WRAP_CONTENT);
         productName = (AutoCompleteTextView) dialog.findViewById(R.id.product_name);
         from = (EditText) dialog.findViewById(R.id.from_date_chooser_text_box);
         to= (EditText) dialog.findViewById(R.id.to_date_chooser_text_box2);
         generate = (Button) dialog.findViewById(R.id.generate);

        choosedProduct = null;
        fromDateValue = null;
        toDateValue = null;

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FLAG_DATE = "fromdate";
                showDialog(999);
            }
        });
        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FLAG_DATE = "todate";
                showDialog(999);
            }
        });

        loadProductName(productName,context);


        generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate())
                {
                    dialog.dismiss();
                    new GetStockReport().execute();
                }
            }
        });

        dialog.show();


    }

    private boolean validate() {
        boolean status = true;

        if(TextUtils.isEmpty(from.getText().toString()))
        {
            from.setError("Field cannot be empty");
            status = false;
        }
        else
        {
            from.setError(null);
        }
        if(TextUtils.isEmpty(to.getText().toString()))
        {
            to.setError("Field cannot be empty");
            status = false;
        }
        else
        {
            to.setError(null);
        }
        if(!BizUtils.isNetworkConnected(StockReportActivity.this))
        {
            status = false;
            Toast.makeText(this, "No Network Connection..", Toast.LENGTH_SHORT).show();
        }




        return status;
    }

    private void loadProductName(AutoCompleteTextView productName,Context context) {
        ArrayAdapter<String> StockGroupAdapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_dropdown_item_1line, allProductList);
        productName.setThreshold(1);
        productName.setAdapter(StockGroupAdapter);
        productName.setThreshold(1);
        productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                Toast.makeText(StockReportActivity.this, "se"+selected, Toast.LENGTH_SHORT).show();
                for(int i=0;i<allProductList.size();i++)
                {
                    if(Store.getInstance().allProductList.get(i).getProductName().equals(selected))
                    {
                        choosedProduct = new Product();
                          choosedProduct =       Store.getInstance().allProductList.get(i);
                    }
                }



            }
        });
    }
    private void loadProductNameForSearch(AutoCompleteTextView productName) {
        ArrayAdapter<String> StockGroupAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, allProductList);
        productName.setThreshold(1);
        productName.setAdapter(StockGroupAdapter);
        productName.setThreshold(1);
        productName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if(TextUtils.isEmpty(searchBar.getText()))
                {
                    stockReports.clear();
                    stockReportadapter.notifyDataSetChanged();
                    loadStockReport();

                }
            }
        });
        productName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selected = (String) parent.getItemAtPosition(position);
                Toast.makeText(StockReportActivity.this, ""+selected, Toast.LENGTH_SHORT).show();
                for(int i=0;i<allProductList.size();i++)
                {
                    if(Store.getInstance().allProductList.get(i).getProductName().equals(selected))
                    {
                        searchProduct = new Product();
                        searchProduct =  Store.getInstance().allProductList.get(i);

                        if(stockReportMap.containsKey(searchProduct.getProductName())) {
                            stockReports.clear();
                            stockReports.add(stockReportMap.get(searchProduct.getProductName()));
                            stockReportadapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(StockReportActivity.this, "Not found in report..", Toast.LENGTH_SHORT).show();
                        }



                    }
                }



            }
        });
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(this, myDateListener, year, month, day);
        }
        return null;
    }



    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // arg1 = year
            // arg2 = month
            // arg3 = day
            String date = (arg2+1)+"-"+arg3+"-"+arg1;
            if(FLAG_DATE.compareToIgnoreCase("fromdate")==0)
            {
                from.setText(String.valueOf(date));

                fromDateValue = String.valueOf(date);

                map.put("fromDate",fromDateValue);
            }
            else
            {
                to.setText(String.valueOf(date));
                toDateValue = String.valueOf(date);
                map.put("toDate",toDateValue);
            }
            Toast.makeText(StockReportActivity.this, date, Toast.LENGTH_SHORT).show();
        }
    };
/*    class Load extends AsyncTask
    {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            stockReportMap = StockReport.getStockReportByName();

            currentIndex = 0;
            if(Store.getInstance().stockReportList.size()<=50)
            {
                stockReports.addAll(Store.getInstance().stockReportList);
            }
            else
            {
                for(int i=0;i<50;i++)
                {
                    StockReport stockReport = new StockReport();
                    stockReport = Store.getInstance().stockReportList.get(i);
                    stockReports.add(stockReport);
                }
            }



        }

        @Override
        protected Object doInBackground(Object[] objects) {

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            listView.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                    int threshold = 1;
                    int offset = 50;
                    int count = listView.getCount();


                    if (scrollState == SCROLL_STATE_IDLE) {
                        if(Store.getInstance().stockReportList.size()!=stockReports.size())
                        {

                            if (Store.getInstance().stockReportList.size() >= stockReports.size() + offset) {
                                offset = offset;
                            }
                            else
                            {
                                int cOffset = Store.getInstance().stockReportList.size() - currentIndex;
                                offset = cOffset;
                            }
                            if (listView.getLastVisiblePosition() >= count - threshold) {
                                for (int i = currentIndex; i < currentIndex+offset; i++) {
                                    stockReports.add(Store.getInstance().stockReportList.get(i));
                                }
                                currentIndex = stockReports.size();

                                Log.d("End PAGE ",Store.getInstance().stockReportList+"---"+stockReports.size());
                                if(Store.getInstance().stockReportList.size()==stockReports.size())
                                {
                                    Toast.makeText(StockReportActivity.this, "Reached the end of report .."+stockReports.size()+" Records", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(StockReportActivity.this, "Loading.."+stockReports.size()+" Records", Toast.LENGTH_SHORT).show();
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            stockReportadapter.notifyDataSetChanged();
                                        }
                                    });

                                }
                            }

                        }
                        else
                        {
                            Log.d("Listview POS", String.valueOf(stockReportadapter.getCount()));
                        }
                        }
                        else    if (scrollState == SCROLL_STATE_TOUCH_SCROLL)
                    {



                        if (listView.getLastVisiblePosition() >= count - threshold) {
                            if (Store.getInstance().stockReportList.size() == stockReports.size()) {
                                Toast.makeText(StockReportActivity.this, "Reached the end of report .." + stockReports.size() + " Records", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }




                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                }
            });
        }
    }*/
    public  void loadStockReport()
    {
        stockReportMap = StockReport.getStockReportByName();

        currentIndex = 0;
        if(Store.getInstance().stockReportList.size()<=50)
        {
            stockReports.addAll(Store.getInstance().stockReportList);
        }
        else
        {
            for(int i=0;i<50;i++)
            {
                StockReport stockReport = new StockReport();
                stockReport = Store.getInstance().stockReportList.get(i);
                stockReports.add(stockReport);
            }
        }


        ///----------------------------
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int offset = 50;
                int count = listView.getCount();


                if (scrollState == SCROLL_STATE_IDLE) {
                    if(Store.getInstance().stockReportList.size()!=stockReports.size())
                    {

                        if (Store.getInstance().stockReportList.size() >= stockReports.size() + offset) {
                            offset = offset;
                        }
                        else
                        {
                            int cOffset = Store.getInstance().stockReportList.size() - currentIndex;
                            offset = cOffset;
                        }
                        if (listView.getLastVisiblePosition() >= count - threshold) {
                            for (int i = currentIndex; i < currentIndex+offset; i++) {
                                stockReports.add(Store.getInstance().stockReportList.get(i));
                            }
                            currentIndex = stockReports.size();

                            Log.d("End PAGE ",Store.getInstance().stockReportList+"---"+stockReports.size());
                            if(Store.getInstance().stockReportList.size()==stockReports.size())
                            {
                                Toast.makeText(StockReportActivity.this, "Reached the end of report .."+stockReports.size()+" Records", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(StockReportActivity.this, "Loading.."+stockReports.size()+" Records", Toast.LENGTH_SHORT).show();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        stockReportadapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        }

                    }
                    else
                    {
                        Log.d("Listview POS", String.valueOf(stockReportadapter.getCount()));
                    }
                }
                else    if (scrollState == SCROLL_STATE_TOUCH_SCROLL)
                {
                    if (listView.getLastVisiblePosition() >= count - threshold) {
                        if (Store.getInstance().stockReportList.size() == stockReports.size()) {
                            Toast.makeText(StockReportActivity.this, "Reached the end of report .." + stockReports.size() + " Records", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

    }

    private class GetStockReport extends AsyncTask{
        ProgressDialog progressDialog =null;

        public GetStockReport() {
            this.progressDialog = new ProgressDialog(StockReportActivity.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Loading..");
            progressDialog.show();
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            Integer id;
            if(choosedProduct==null)
            {
                id = null;
            }
            else
            {
                id = choosedProduct.getId().intValue();
            }
            //SignalRService.stockReportList(id,null,fromDateValue,toDateValue);
            SignalRService.stockReportList(id,null,fromDateValue,toDateValue);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    progressDialog.setMessage("Loading.."+Store.getInstance().stockReportList.size());

                }
            });
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            Log.d("Stock Size", String.valueOf(Store.getInstance().stockReportList.size()));
            Toast.makeText(StockReportActivity.this, "Filter applied", Toast.LENGTH_SHORT).show();

            progressDialog.dismiss();

            stockReports.clear();
            stockReportadapter.notifyDataSetChanged();
            loadStockReport();
        }
    }
}
