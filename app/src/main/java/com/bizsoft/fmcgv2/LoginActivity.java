package com.bizsoft.fmcgv2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.adapter.CustomSpinnerAdapter;
import com.bizsoft.fmcgv2.dataobject.Company;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.dataobject.User;
import com.bizsoft.fmcgv2.service.ConnectivityChangeReceiver;
import com.bizsoft.fmcgv2.service.HttpHandler;
import com.bizsoft.fmcgv2.service.SignalRService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {


    ImageButton reload;
    EditText serverName;
    Spinner companyName,dealerName;
    EditText username,password;
    Button login,connect;
    String domain;
    SharedPreferences sharedpreferences ;
    private String MyPREFERENCES= "loginDetails";
    SharedPreferences.Editor editor;
    public static TextView serverStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        reload = (ImageButton) findViewById(R.id.reload);
        serverName = (EditText) findViewById(R.id.server_url);
        companyName = (Spinner) findViewById(R.id.company_spinner);
        dealerName = (Spinner) findViewById(R.id.dealer_spinner);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        connect = (Button) findViewById(R.id.connect);
        serverStatus = (TextView) findViewById(R.id.status);

        registerReceiver(
                new ConnectivityChangeReceiver(),
                new IntentFilter(
                        ConnectivityManager.CONNECTIVITY_ACTION));



        serverName.setText(String.valueOf(Store.getInstance().domain));


        System.out.println("Called login activity...");

        if(Store.getInstance().serverStatus==null)
        {
            Store.getInstance().serverStatus = "offline";
        }
        serverStatus.setText(String.valueOf( Store.getInstance().serverStatus));
        if(Store.getInstance().serverStatus.toLowerCase().contains("offline"))
        {
            serverStatus.setTextColor(Color.RED);
        }
        else if(Store.getInstance().serverStatus.toLowerCase().contains("online"))
        {
            serverStatus.setTextColor(Color.GREEN);
        }
        else if(Store.getInstance().serverStatus.toLowerCase().contains("conn"))
        {
            serverStatus.setTextColor(Color.BLUE);
        }

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
         editor = sharedpreferences.edit();


        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);



        String companyId = prefs.getString(getString(R.string.companyID), "0");
        String dealerId = prefs.getString(getString(R.string.dealerID), "0");


        System.out.println("Company Id = "+companyId);
        System.out.println("Dealer Id = "+dealerId);




        boolean isStored = false;
        if(Long.parseLong(companyId)!=0 && Long.parseLong(dealerId)!=0)
        {
            Store.getInstance().companyID = Long.parseLong(companyId);
            Store.getInstance().dealerId = Long.parseLong(dealerId);

            isStored = true;
        }



        password.setLongClickable(false);
        password.setCustomSelectionActionModeCallback(new ActionMode.Callback() {

            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public void onDestroyActionMode(ActionMode mode) {
            }

            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }
        });
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serverName.setText(String.valueOf(Store.getInstance().domain));


            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
            }
        });
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 boolean status = true;
                 domain = serverName.getText().toString();
                if(TextUtils.isEmpty(domain))
                {
                    serverName.setError("Cant be empty");
                    status = false;
                }
                else
                {
                    String baseUrl = "";
                    if(Store.getInstance().urlType.contains("full")) {
                        baseUrl = "http://" + domain + "/";
                    }
                    else
                    {
                        baseUrl = "http://" + domain + "/";
                    }
                    System.out.println("Base URL = "+baseUrl);
                    boolean s = Patterns.WEB_URL.matcher(baseUrl).matches();
                    if(!s)
                    {
                        status = false;
                        Toast.makeText(LoginActivity.this, "Not a valid url", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        System.out.println("Domain b= "+ Store.getInstance().domain);
                        Store.getInstance().domain = domain;
                        System.out.println("Domain a= "+ Store.getInstance().domain);

                        if(Store.getInstance().urlType.contains("full")) {
                            Store.getInstance().baseUrl = "http://" + domain + "/";
                        }
                        else
                        {
                            Store.getInstance().baseUrl = "http://" + domain + "/";
                        }
                        System.out.println("Base URL= "+ Store.getInstance().baseUrl);
                    }

                }
                if(status)
                {
                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);

                    String companyId = prefs.getString(getString(R.string.companyID), "0");
                    String dealerId = prefs.getString(getString(R.string.dealerID), "0");

                    System.out.println("Company Id = "+companyId);
                    System.out.println("Dealer Id = "+dealerId);

                    boolean isStored = false;
                    if(Long.parseLong(companyId)!=0 && Long.parseLong(dealerId)!=0)
                    {
                        Store.getInstance().companyID = Long.parseLong(companyId);
                        Store.getInstance().dealerId = Long.parseLong(dealerId);
                        isStored = true;
                    }

                    new GetCompanyDetails(LoginActivity.this).execute();

                    System.out.println("Calling company list");
                   // ArrayList<Company> result = SignalRService.getCompanyDetails(LoginActivity.this);
                }



            }
        });

      //  companyName.setEnabled(false);
      //  dealerName.setEnabled(false);
       // username.setEnabled(false);
      //  password.setEnabled(false);




        setCompanyList();

    }

    public void setCompanyList()
    {
        final ArrayList<String> companyList = new ArrayList<String>();

        final ArrayList<Company> companyL = Store.getInstance().companyList;
        final ArrayList<Company> companyTemp = new ArrayList<Company>();

        System.out.println("&&&&&&&&&&&&&&&&&&&&&&& :"+companyL.size());
        for(int i=0;i<companyL.size();i++)
        {
            System.out.println("&&&&&&&&&&&&&&&&&&&&&&& :"+companyL.get(i).getCompanyType());
            if(companyL.get(i).getCompanyType().toLowerCase().equals("company"))
            {
                companyTemp.add(companyL.get(i));
                System.out.println("ID :"+companyL.get(i).getId());
                System.out.println("Name :"+companyL.get(i).getCompanyName());
                System.out.println("GST :"+companyL.get(i).getGSTNo());

            }


        }
        Store.getInstance().pureCompanyList = companyTemp;

        for(int j=0;j<companyTemp.size();j++)
        {
            System.out.println("Company Details = "+companyTemp.get(j).getId()+" "+companyTemp.get(j).getGSTNo()+" "+companyTemp.get(j).getCompanyName()+" "+companyTemp.get(j).getAddressLine1()+" ");
            companyList.add(companyTemp.get(j).getCompanyName()+"-"+companyTemp.get(j).getId());
        }

        System.out.println("Company List Size = "+companyList.size());
        System.out.println("Company List temp size = "+companyTemp.size());



        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(LoginActivity.this,companyList);
        companyName.setAdapter(customSpinnerAdapter);


        companyName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Store.getInstance().companyName = companyList.get(position);
                System.out.println("Test size = "+companyTemp.size());
                Store.getInstance().companyID = companyTemp.get(position).getId();
                Store.getInstance().companyPosition = position;
                Store.getInstance().company = companyTemp.get(position);


                Toast.makeText(LoginActivity.this, "Company name :"+ Store.getInstance().companyName, Toast.LENGTH_SHORT).show();
                System.out.println("========================ADD======================="+companyTemp.get(0).getGSTNo());
                dealerName.setEnabled(true);




                setDealerList();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Store.getInstance().companyName = companyList.get(0);
                System.out.println("Test size = "+companyTemp.size());
                Store.getInstance().companyID = companyTemp.get(0).getId();
                Store.getInstance().companyPosition = 0;
                Store.getInstance().company = companyTemp.get(0);

                System.out.println("========================ADD======================="+companyTemp.get(0).getGSTNo());

                Toast.makeText(LoginActivity.this, "Company name :"+ Store.getInstance().companyName, Toast.LENGTH_SHORT).show();

                dealerName.setEnabled(true);
            }
        });

    }
    public void setDealerList()
    {
        final ArrayList<String> dealerList = new ArrayList<String>();


        final ArrayList<Company> companyTemp = new ArrayList<Company>();
        Long id = Store.getInstance().companyID;




        for(int i=0;i<Store.getInstance().dealerList.size();i++)
        {

            Double temp = Double.valueOf(Store.getInstance().dealerList.get(i).getUnderCompanyId());
            Long dId = (new Double(temp)).longValue();;


            System.out.println("Dealer under ccompany Id ---"+dId+"___Company Id"+id);
            if (dId.compareTo(id)==0) {
                    companyTemp.add(Store.getInstance().dealerList.get(i));
                    System.out.println("ID :" + Store.getInstance().dealerList.get(i).getId());

                }
        }


        System.out.println("Dealer size = "+companyTemp.size());
        for(int j=0;j<companyTemp.size();j++)
        {
            dealerList.add(companyTemp.get(j).getCompanyName());
        }
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(LoginActivity.this,dealerList);
        dealerName.setAdapter(customSpinnerAdapter);

        dealerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Store.getInstance().dealerName = dealerList.get(position);
                Store.getInstance().dealerId = companyTemp.get(position).getId();
                System.out.println("Dealer ID :"+companyTemp.get(position).getId());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        username.setEnabled(true);
        password.setEnabled(true);



    }
    public void login()
    {

        boolean status = true;
        String passWord = password.getText().toString();
        String userName = username.getText().toString();

        if(TextUtils.isEmpty(userName))
        {
            username.setError("Username cannot be empty");
            status = false;
        }
        if (TextUtils.isEmpty(passWord))
        {
            password.setError("Password cannot be empty");
            status = false;
        }

        if(status)
        {


            new LoginTask(LoginActivity.this,userName,passWord).execute();



        }

    }
    public void validate()
    {

        if(Store.getInstance().companyName==null)
        {
            companyName.setEnabled(true);
        }

    }
    class GetCompanyDetails extends AsyncTask
    {
        String jsonStr = null;
        String url = "";
        Context context;
        private ProgressDialog pdia;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pdia = new ProgressDialog(context);
            pdia.setMessage("Loading...");
            pdia.show();
        }

        public GetCompanyDetails(Context context) {

            this.url = "company/tolist";
            this.context = context;
        }

        @Override

        protected Object doInBackground(Object[] params) {
            HttpHandler httpHandler = new HttpHandler();

            ArrayList<Company> result = SignalRService.getCompanyDetails(LoginActivity.this);

            System.out.println("company List...."+result);



            return true;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);



            if(Store.getInstance().companyList!=null) {
                Toast.makeText(context, "Connected...", Toast.LENGTH_SHORT).show();
                Store.getInstance().serverStatus = "Connected";
                serverStatus.setText(String.valueOf( Store.getInstance().serverStatus));
                serverStatus.setTextColor(Color.BLUE);

                companyName.setEnabled(true);

                System.out.println("Setting company List....");
                setCompanyList();
                username.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(username, InputMethodManager.SHOW_IMPLICIT);

            }
            else {
                Store.getInstance().serverStatus = "Offline";
                serverStatus.setText(String.valueOf( Store.getInstance().serverStatus));
                serverStatus.setTextColor(Color.RED);
                Toast.makeText(context, "Not connected..", Toast.LENGTH_SHORT).show();
            }
            pdia.dismiss();
        }


    }
    class LoginTask extends AsyncTask
    {



        Context context;
        String userName, passWord;
        boolean status;

        HashMap<String,String> params = new HashMap<String, String>();

        public LoginTask(Context context, String userName, String passWord) {


            this.context = context;
            this.userName = userName;
            this.passWord = passWord;
            this.status = false;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            System.out.println("Dealer ID = "+ Store.getInstance().dealerId);
            params.put("CompanyName", Store.getInstance().dealerName);
            params.put("LoginId",this.userName);
            params.put("Password",this.passWord);
        }
        @Override
        protected Object doInBackground(Object[] params) {
            status =  SignalRService.newLogin(LoginActivity.this,Store.getInstance().dealerName,userName,passWord);

            System.out.print("Login status --- "+status);
            return true;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            System.out.println("USER ID >< = "+Store.getInstance().user.getUserId());
            if(status) {

                if(Store.getInstance().user.getId()==null)
                {
                    Toast.makeText(context, "Invalid Username or Password..", Toast.LENGTH_SHORT).show();
                }
                else
                {

                    editor.putString(getString(R.string.companyID), String.valueOf(Store.getInstance().companyID));
                    editor.putString(getString(R.string.companyPosition), String.valueOf(Store.getInstance().companyPosition));

                    System.out.println("======================ADD====================="+ Store.getInstance().company.getAddressLine1());
                    editor.putString(getString(R.string.companyName), String.valueOf(Store.getInstance().company.getCompanyName()));
                    editor.putString(getString(R.string.companyAddressLine1), String.valueOf(Store.getInstance().company.getAddressLine1()));
                    editor.putString(getString(R.string.companyAddressLine2), String.valueOf(Store.getInstance().company.getAddressLine2()));
                    editor.putString(getString(R.string.phoneNumber), String.valueOf(Store.getInstance().company.getTelephoneNo()));
                    editor.putString(getString(R.string.gstNo), String.valueOf(Store.getInstance().company.getGSTNo()));
                    editor.putString(getString(R.string.email), String.valueOf(Store.getInstance().company.getEMailId()));
                    editor.putString(getString(R.string.postal_code), String.valueOf(Store.getInstance().company.getPostalCode()));


                    editor.putString(getString(R.string.dealerID), String.valueOf(Store.getInstance().dealerId));
                    editor.putBoolean(getString(R.string.isLogged), true);
                    editor.putString(getString(R.string.username), this.userName);
                    editor.putString(getString(R.string.password), this.passWord);
                    editor.putString(getString(R.string.dealerName), String.valueOf(Store.getInstance().dealerName));



                    editor.commit();

                    System.out.println("S = "+ Store.getInstance().companyID);
                    System.out.println("S = "+ Store.getInstance().dealerId);
                    System.out.println("S = "+ Store.getInstance().dealerName);

                    Toast.makeText(context, "Login Successfully..", Toast.LENGTH_SHORT).show();



                    SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);



                    String companyId = prefs.getString(getString(R.string.companyID), "0");
                    String dealerId = prefs.getString(getString(R.string.dealerID), "0");
                    String dealerName = prefs.getString(getString(R.string.dealerName), "0");

                    System.out.println("Company Id = "+companyId);
                    System.out.println("Dealer Id = "+dealerId);
                    System.out.println("Dealer Name = "+dealerName);
                    System.out.println("Dealer Name = "+dealerName);

                    username.setText("");
                    password.setText("");
                    try
                    {

                      //  Store.getInstance().user.SalRefCode ="0000A";
                      //  Store.getInstance().user.SRRefCode="0000B";
                      //  Store.getInstance().user.SORefCode="0000C";

                    }
                    catch (Exception e)
                    {
                        System.out.println("Exception e :"+e);
                    }
                    finish();
                    Intent intent = new Intent(context,DownloadDataActivity.class);
                    startActivity(intent);



                }



            }
            else {
                Toast.makeText(context, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        username.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(username, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        username.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(username, InputMethodManager.SHOW_IMPLICIT);
    }
}
