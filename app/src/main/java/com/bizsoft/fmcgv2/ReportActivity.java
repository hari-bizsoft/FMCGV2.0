/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.adapter.ReportAdapter;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.ReportData;
import com.bizsoft.fmcgv2.dataobject.Sale;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.HttpHandler;
import com.bizsoft.fmcgv2.service.SignalRService;
import com.bizsoft.fmcgv2.service.UIUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReportActivity extends AppCompatActivity {

    Spinner reportType;
    Spinner reportFor;
     public static String reportForValue = "Customer" ;
     String reportTypeValue;
    ListView listview;
    String FLAG_DATE;
    private String fromDateValue,toDateValue;
    ArrayList<Product> productReportList = new ArrayList<Product>();
    private int year,month,day;
    BizUtils bizUtils;
    public static  String reportUrl="sale/productReport";
    private ReportAdapter reportAdapter;
    private HashMap<String, String> map = new HashMap<String, String>();
    TextView grandTotal;
    ImageButton fromDateButton,toDateButton;
    EditText fromDate,toDate;
    Button customSearch;
    EditText moneyFromCustomer;
    DecimalFormat df ;
    Spinner saleType;

    private String reportBaseUrl = "sale";
    TextView label;
    TextView customInputLabel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        UIUtil.setActionBarMenu(ReportActivity.this,getSupportActionBar(),"Reports");
        bizUtils = new BizUtils();


        df = new DecimalFormat("#.####");
        df.setRoundingMode(RoundingMode.CEILING);





        reportFor = (Spinner) findViewById(R.id.report_for);
        reportType = (Spinner) findViewById(R.id.report_type);
        listview = (ListView) findViewById(R.id.listview);
        grandTotal = (TextView) findViewById(R.id.grandTotal);
        fromDateButton = (ImageButton) findViewById(R.id.fromDateButton);
        toDateButton = (ImageButton) findViewById(R.id.toDateButton);
        fromDate = (EditText) findViewById(R.id.from_date);
        toDate= (EditText) findViewById(R.id.to_date);
        customSearch = (Button) findViewById(R.id.custom_search);
        saleType = (Spinner) findViewById(R.id.sale_type);
        label = (TextView) findViewById(R.id.name);
        customInputLabel = (TextView) findViewById(R.id.textView33);





        map = bizUtils.setTodayDate();

        getSupportActionBar().setTitle("Report view");


        setSaleType();
        setReportFor();
        setReportType();
        bizUtils.setTodayDate();



        reportAdapter =  new ReportAdapter(ReportActivity.this,productReportList);
        listview.setAdapter(reportAdapter);




        fromDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FLAG_DATE = "fromdate";
                showDialog(999);
            }
        });
        toDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FLAG_DATE = "todate";
                showDialog(999);

            }
        });
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FLAG_DATE = "fromdate";
                showDialog(999);
            }
        });
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FLAG_DATE = "todate";
                showDialog(999);
            }
        });

        customSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reportAdapter.removeAll();
                System.out.println("=============>"+productReportList.size());
                new GetReport(ReportActivity.this,reportUrl,fromDateValue+" 00:00:00",toDateValue+" 23:59:59").execute();
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
            String date = arg3+"/"+(arg2+1)+"/"+arg1;
            if(FLAG_DATE.compareToIgnoreCase("fromdate")==0)
            {
                fromDate.setText(String.valueOf(date));

                fromDateValue = String.valueOf(date);

                map.put("fromDate",fromDateValue);
            }
            else
            {
                toDate.setText(String.valueOf(date));
                toDateValue = String.valueOf(date);
                map.put("toDate",toDateValue);
            }
            Toast.makeText(ReportActivity.this, date, Toast.LENGTH_SHORT).show();
        }
    };
    public void setSaleType()
    {
        final List<String> genderList = new ArrayList<String>();
        genderList.add("Sale");
        genderList.add("Sale Return");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(ReportActivity.this, android.R.layout.simple_spinner_item, genderList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        saleType.setAdapter(dataAdapter);
        saleType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),genderList.get(position),Toast.LENGTH_SHORT).show();
                reportForValue = genderList.get(position);

                if(position==0)
                {
                    reportBaseUrl = "sale";
                }
                else
                {
                    reportBaseUrl = "salesReturn";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                reportForValue = genderList.get(0);

                reportBaseUrl = "sale";
            }


        });



    }
    public void setReportFor()
    {
        final List<String> genderList = new ArrayList<String>();
        genderList.add("Customer");
        genderList.add("Product");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(ReportActivity.this, android.R.layout.simple_spinner_item, genderList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportFor.setAdapter(dataAdapter);
        reportFor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),genderList.get(position),Toast.LENGTH_SHORT).show();
                reportForValue = genderList.get(position);

                if(position==0)
                {
                    reportUrl = reportBaseUrl+"/customerReport";
                }
                else
                {
                    reportUrl = reportBaseUrl+"/productReport";
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                reportForValue = genderList.get(0);

                reportUrl = reportBaseUrl+"/customerReport";
            }


        });



    }

    public void setReportType()
    {
        final List<String> genderList = new ArrayList<String>();
        genderList.add("Daily");
        genderList.add("Weekly");
        genderList.add("Monthly");
        genderList.add("Custom Date");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter <String>(ReportActivity.this, android.R.layout.simple_spinner_item, genderList);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reportType.setAdapter(dataAdapter);
        reportType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                reportTypeValue = genderList.get(position);
                if(position==0)
                {
                    map = bizUtils.setTodayDate();
                    fromDate.setVisibility(View.GONE);
                    fromDateButton.setVisibility(View.GONE);
                    toDate.setVisibility(View.GONE);
                    toDateButton.setVisibility(View.GONE);
                    customSearch.setVisibility(View.GONE);
                    customInputLabel.setVisibility(View.GONE);

                }
                else
                    if(position==1)
                    {
                      map =   bizUtils.setCurrentWeek();
                        fromDate.setVisibility(View.GONE);
                        fromDateButton.setVisibility(View.GONE);
                        toDate.setVisibility(View.GONE);
                        toDateButton.setVisibility(View.GONE);
                        customSearch.setVisibility(View.GONE);
                        customInputLabel.setVisibility(View.GONE);

                    }
                    else if(position==2)
                    {
                        map = bizUtils.setCurrentMonth();
                        fromDate.setVisibility(View.GONE);
                        fromDateButton.setVisibility(View.GONE);
                        toDate.setVisibility(View.GONE);
                        toDateButton.setVisibility(View.GONE);
                        customSearch.setVisibility(View.GONE);
                        customInputLabel.setVisibility(View.GONE);

                    }
                    else if(position ==3)
                    {
                        fromDate.setVisibility(View.VISIBLE);
                        fromDateButton.setVisibility(View.VISIBLE);
                        toDate.setVisibility(View.VISIBLE);
                        toDateButton.setVisibility(View.VISIBLE);
                        customSearch.setVisibility(View.VISIBLE);
                        customInputLabel.setVisibility(View.VISIBLE);


                    }
                productReportList.clear();
                fromDate.setText("");
                toDate.setText("");
               new GetReport(ReportActivity.this,reportUrl, Store.getInstance().fromDate, Store.getInstance().toDate).execute();





            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                reportTypeValue = genderList.get(0);

                bizUtils.setTodayDate();




            }


        });



    }

    class GetReport extends AsyncTask
    {
        Context context;
        String url;
        String response;
        HashMap<String,String> params = new HashMap<>();
        String fromDate;
        String toDate;
      //  private ProgressDialog pdia;

        public GetReport(Context context, String url, String fromDate,String toDate) {
            this.context = context;
            this.url = url;
            this.response = null;
            this.params = new HashMap<>();
            this.fromDate = fromDate;
            this.toDate = toDate;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.params.put("DealerId", String.valueOf(Store.getInstance().dealerId));
            this.params.put("DateFrom", String.valueOf(map.get("fromDate")));
            this.params.put("DateTo", String.valueOf(map.get("toDate")));
           // pdia = new ProgressDialog(context);
         //   pdia.setMessage("Generating report...");
         //   pdia.show();

        }
        @Override
        protected Object doInBackground(Object[] params) {
            HttpHandler httpHandler = new HttpHandler();


            response = httpHandler.makeServiceCall(this.url,this.params);

            String fromDateStr = String.valueOf(map.get("fromDate")) +" 00:00:00";;

            String toDateStr =   String.valueOf(map.get("toDate"))+" 23:59:59";;


            DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
            Date fromDate = null;
            Date toDate = null;
            try {
                 fromDate = dateFormat.parse(fromDateStr);
                 toDate = dateFormat.parse(toDateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            System.out.println("SD ==================="+fromDate);
            System.out.println("ED ==================="+toDate);


            if(reportBaseUrl.contains("return")) {

            }
            else if(reportBaseUrl.contains("sale")) {

                Store.getInstance().saleList.clear();
                SignalRService.saleList(null, null, fromDate, toDate, null, 0, 9999999);
            }

       //  new GetReport(ReportActivity.this,reportUrl, Store.getInstance().fromDate, Store.getInstance().toDate).execute();



            return true;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("My REPORT = "+response );



           // pdia.dismiss();

            if(response!=null)
            {
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                Gson gson = gsonBuilder.create();



                Type collectionType = new TypeToken<ReportData>() {
                }.getType();

                ReportData collection = gson.fromJson(response, collectionType);



                Store.getInstance().reportData = (ReportData) collection;
                double total = 0;
                if(Store.getInstance().reportData.Datas!=null) {
                    productReportList.clear();
                    productReportList.addAll(Store.getInstance().reportData.Datas);

                    for(int i=0;i<productReportList.size();i++)
                    {
                        total = total +productReportList.get(i).getAmount();
                    }
                }

                System.out.println("Size === "+ Store.getInstance().saleList.size());

                if(Store.getInstance().saleList!=null)
                {
                    if(Store.getInstance().saleList.size()>0)
                    {
                        if(reportUrl.toLowerCase().contains("customer"))
                        {
                           HashMap<Long,ArrayList<com.bizsoft.fmcgv2.Tables.Sale>> customerReport = new HashMap<Long,ArrayList<com.bizsoft.fmcgv2.Tables.Sale>>();
                            HashMap<Long,ArrayList<com.bizsoft.fmcgv2.Tables.Sale>> productReport = new HashMap<Long,ArrayList<com.bizsoft.fmcgv2.Tables.Sale>>();

                           for(int i=0;i<Store.getInstance().saleList.size();i++)
                           {
                               for(int j=0;j<Store.getInstance().customerList.size();j++)
                               {
                                   if(Store.getInstance().customerList.get(j).getLedger().getId() == Store.getInstance().saleList.get(i).getLedgerId())
                                   {
                                       if(customerReport.containsKey(Store.getInstance().customerList.get(j).getLedger().getId()))
                                       {
                                            customerReport.get(Store.getInstance().customerList.get(j).getLedger().getId()).add(Store.getInstance().saleList.get(i));
                                       }
                                       else
                                       {
                                           ArrayList<com.bizsoft.fmcgv2.Tables.Sale> sales = new ArrayList<com.bizsoft.fmcgv2.Tables.Sale>();
                                           sales.add(Store.getInstance().saleList.get(i));
                                           customerReport.put(Store.getInstance().customerList.get(j).getLedger().getId(),sales);
                                       }

                                   }

                               }

                           }
                            System.out.println("Customer Size ===="+customerReport.size());
                        }
                        else
                        {

                        }



                    }
                    else
                    {
                       // Toast.makeText(context, "No Data found...", Toast.LENGTH_SHORT).show();

                        reportAdapter.notifyDataSetChanged();
                    }
                }





                if(reportUrl.toLowerCase().contains("customer"))
                {
                    label.setText(String.valueOf("Customer Name"));
                }
                else
                {
                    label.setText(String.valueOf("Product Name"));
                }
                grandTotal.setText(String.valueOf(String.format("%.2f",total)));
                reportAdapter.notifyDataSetChanged();

            }


        }


    }
}
