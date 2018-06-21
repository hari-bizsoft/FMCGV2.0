/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.bizsoft.fmcgv2.dataobject.ReceiverResponse;
import com.bizsoft.fmcgv2.dataobject.Store;

import com.bizsoft.fmcgv2.service.BlockPage;

import com.bizsoft.fmcgv2.service.Network;
import com.bizsoft.fmcgv2.service.SignalRService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

import microsoft.aspnet.signalr.client.Action;
import microsoft.aspnet.signalr.client.ConnectionState;
import microsoft.aspnet.signalr.client.MessageReceivedHandler;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.StateChangedCallback;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.HubResult;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.NegotiationException;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

import static com.bizsoft.fmcgv2.dataobject.Store.SERVER_HUB_CHAT;

public class MainActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 1000;
    static String domain;
    private static HubConnection mHubConnectionCaller;
    private static HubConnection mHubConnectionReceiver;
    static  Handler mHandlerCaller;
    static  Handler mHandlerReceiver;
    public static HubProxy mHubProxyCaller;
    static HubProxy mHubProxyReceiver;
    SharedPreferences sharedpreferences ;
    SharedPreferences sharedpreferencesDomain ;
    private String MyPREFERENCES = "ACTIVATION_KEY";
    private String DOMAIN = "DOMAIN";
    SharedPreferences.Editor editor;
    SharedPreferences.Editor editorDomain;
    private String AppId = "AppId";
    private static String android_id;
    private static String email;
    private String AppIDValue;
    private String RConnectionId;
    private String TAG = "SIGNAL IR";
    static String fromClassName;
    static TextView message;
    Activity activity;
    static Context context;
    private TextView mainUrl;
    private Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        message = (TextView) findViewById(R.id.message);
        mainUrl = (TextView) findViewById(R.id.url);
        Store.getInstance().mainActvity = MainActivity.this;

        mainUrl.setText("Trying...");

        sharedpreferencesDomain = getSharedPreferences(DOMAIN, Context.MODE_PRIVATE);


        String domain= sharedpreferencesDomain.getString("domain",Store.getInstance().domain);

        Log.d("Domain ",domain);
        Store.getInstance().domain = domain;

         if(Network.getInstance(MainActivity.this).isOnline())
         {
             new Handler().postDelayed(new Runnable(){
                 @Override
                 public void run() {
                     /* Create an Intent that will start the Menu-Activity. */
                     Activity activity = MainActivity.this;
                     if(Store.getInstance().mHubConnectionCaller !=null && Store.getInstance().mHubConnectionReceiver !=null  ) {
                         Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                         intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                         startActivity(intent);
                     }
                     else
                     {
                         setURL(MainActivity.this);

                         // init(MainActivity.this, activity);
                     }
                 }
             }, SPLASH_DISPLAY_LENGTH);
         }
         else
         {

             mainUrl.setText("No Network Connection");





         }





        mainUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Network.getInstance(MainActivity.this).isOnline())
                {
                    setURL(MainActivity.this);
                }
                else
                {
                    mainUrl.setText("No Network Connection");
                }

            }
        });


    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("Touch","listened");
        if(Network.getInstance(MainActivity.this).isOnline())
        {
            try {
                if (!dialog.isShowing()) {
                    setURL(MainActivity.this);
                }
            }catch (Exception e)
            {
                Log.e("Errr","dialog");
            }
        }
        else
        {
            mainUrl.setText("No Network Connection");
        }
        return super.onTouchEvent(event);

    }
    @Override
    public void onBackPressed() {

        // check if modal is open #&ui-state=dialog

        if(dialog!=null)
        {
        if(!dialog.isShowing()) {
            dialog.show();

        }        }
    }
    private void setURL(final Context context) {
       dialog = new Dialog(context);
        dialog.setContentView(R.layout.set_url_dialog);

        final EditText url = (EditText) dialog.findViewById(R.id.url);
        Button submit = (Button) dialog.findViewById(R.id.submit);
        url.setText(Store.getInstance().domain);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean s = Patterns.WEB_URL.matcher(url.getText()).matches();
                if(s)
                {

                    Store.getInstance().domain = url.getText().toString();

                    editorDomain = sharedpreferencesDomain.edit();
                    editorDomain.putString("domain",Store.getInstance().domain);
                    editorDomain.commit();
                    Log.d("Domain Stored",sharedpreferencesDomain.getString("domain","not stored"));

                    dialog.dismiss();
                    init(MainActivity.this, activity);
                    mainUrl.setText(Store.getInstance().domain);


                }
                else
                {
                    Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }


    public void connect()
    {

        boolean status = true;
        domain = Store.getInstance().domain;



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
                Toast.makeText(MainActivity.this, "Not a valid url", Toast.LENGTH_SHORT).show();
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
            if(status) {
                // new GetCompanyDetails(MainActivity.this).execute();

                SignalRService.getCompanyDetails(MainActivity.this);

                System.out.println("trying to log in ..");
                boolean loginStat = SignalRService.login(MainActivity.this, null, null, null);

                if (loginStat)
                {
                    if (Store.getInstance().user.getId() == null) {
                        Toast.makeText(context, "Invalid Username or Password..", Toast.LENGTH_SHORT).show();
                    } else {

                        try {

                           // Store.getInstance().user.SalRefCode = "0000A";
                          //  Store.getInstance().user.SRRefCode = "0000B";
                           // Store.getInstance().user.SORefCode = "0000C";

                        } catch (Exception e) {
                            System.out.println("Exception e :" + e);
                        }



                        finish();
                        Intent intent = new Intent(context, DownloadDataActivity.class);
                        startActivity(intent);

                        MainActivity.this.finish();


                    }
            }
               // SignalRService.getCompanyDetails(MainActivity.this);
            }
            else
            {
                Toast.makeText(MainActivity.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
            }

        }

    //------------------------------------------------------BLOCKER
    public void init(Context context, Activity activity)
    {
        this.activity = activity;
        this.context = context;
        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        editor = sharedpreferences.edit();
        mHandlerCaller = new Handler(Looper.getMainLooper());
        mHandlerReceiver = new Handler(Looper.getMainLooper());


        new ConnectCaller(context).execute();
    }

    class ConnectCaller extends AsyncTask
    {
        String host =  null;
        Context context;
        ProgressDialog progressDialog;

        public ConnectCaller(Context context) {

            if(Store.getInstance().urlType.contains("full")) {
                Store.getInstance().baseUrl = "http://" + Store.getInstance().domain + "/";
            }
            else
            {
                Store.getInstance().baseUrl = "http://" + Store.getInstance().domain + "/";

            }
            this.host = Store.getInstance().baseUrl;
            this.context = context;
            this.progressDialog    = new ProgressDialog(this.context);
            this.progressDialog.setTitle("Connecting...");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.show();

        }
        @Override
        protected Object doInBackground(Object[] objects) {
            System.out.println("Intializing connection");
            Platform.loadPlatformComponent( new AndroidPlatformComponent() );
            // Change to the IP address and matching port of your SignalR server.

            Log.d("Host: ",host);

            mHubConnectionCaller = new HubConnection(host);

           SERVER_HUB_CHAT = "ABServerHub";

            mHubProxyCaller = mHubConnectionCaller.createHubProxy(SERVER_HUB_CHAT);
            mHubProxyCaller.on("Display",
                    new SubscriptionHandler1<String>() {
                        @Override
                        public void run(final String msg) {
                            final String finalMsg = msg;
                            // display Toast message
                            mHandlerCaller.post(new Runnable() {
                                @Override
                                public void run() {
                                    // Toast.makeText(getApplicationContext(), finalMsg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    , String.class);


            ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnectionCaller.getLogger());
            SignalRFuture<Void> signalRFuture = mHubConnectionCaller.start(clientTransport);

            Store.getInstance().signalRFutureCaller = signalRFuture;



            try {
                Log.d("Connection:","trying...");

                try {
                    signalRFuture.get(60, TimeUnit.SECONDS);
                    Log.d("Connection:"," ok");
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Log.d("Connection:","not ok");
                    ( (Activity) context ).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mainUrl.setText("Server unreachable...");
                        }
                    });
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();

                ( (Activity) context ).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainUrl.setText("Server unreachable...");
                    }
                });




            }

            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            new ConnectReceiver(context).execute();


            progressDialog.dismiss();
           // Toast.makeText(context, "Caller Connected....", Toast.LENGTH_SHORT).show();
            recieveAllMsg("Caller");

            // getnynyDetails();


        }
    }
    class ConnectReceiver extends AsyncTask
    {
        String host =  null;
        Context context;
        String result ="";
        public ConnectReceiver(Context context) {
            if(Store.getInstance().urlType.contains("full")) {
                Store.getInstance().baseUrl = "http://" + Store.getInstance().domain + "/";
            }
            else
            {
                Store.getInstance().baseUrl = "http://" + Store.getInstance().domain + "/";
            }
            this.host =Store.getInstance().baseUrl;
            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }
        @Override
        protected Object doInBackground(Object[] objects) {
            System.out.println("Intializing connection - receiver");
            Platform.loadPlatformComponent( new AndroidPlatformComponent() );
            // Change to the IP address and matching port of your SignalR server.


            mHubConnectionReceiver = new HubConnection(host);

            String SERVER_HUB_CHAT = "ABServerHub";

            mHubProxyReceiver = mHubConnectionReceiver.createHubProxy(SERVER_HUB_CHAT);
            mHubProxyReceiver.on("Display",
                    new SubscriptionHandler1<String>() {
                        @Override
                        public void run(final String msg) {
                            final String finalMsg = msg;
                            // display Toast message
                            mHandlerReceiver.post(new Runnable() {
                                @Override
                                public void run() {
                                   // Toast.makeText(context, finalMsg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    , String.class);
            mHubProxyReceiver.on("Company_Save",
                    new SubscriptionHandler1<Object>() {
                        @Override
                        public void run(final Object msg) {
                            final String finalMsg = msg.toString();
                            System.out.println("customer_save"+finalMsg);
                            // display Toast message
                            mHandlerReceiver.post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(context, finalMsg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    , Object.class);
            mHubProxyReceiver.on("AppApproved_Changed",
                    new SubscriptionHandler1<String>() {
                        @Override
                        public void run(final String response) {

                            Log.d("App change ->",response);
                            // display Toast message
                            mHandlerReceiver.post(new Runnable() {
                                @Override
                                public void run() {


                                    Log.d("App change", String.valueOf(response));
                                    Intent intent = null;
                                    if(!Boolean.valueOf(response))
                                    {
                                        activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                                                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                                        intent = new Intent(context,BlockPage.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        context.startActivity(intent);

                                    }
                                    else
                                    {

                                        Activity activity = BlockPage.getActivity();

                                        if(activity==null)
                                        {
                                           connect();

                                        }
                                        else {

                                            try {
                                                activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                                activity.finish();
                                            } catch (Exception e) {

                                                Log.e("Error", e.toString());
                                            }
                                        }

                                        mHandlerReceiver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                // Toast.makeText(getApplicationContext(), finalMsg, Toast.LENGTH_SHORT).show();

                                                Toast.makeText(context, "Event --- "+response, Toast.LENGTH_SHORT).show();

                                            }
                                        });

                                    }

                                }
                            });
                        }
                    }
                    , String.class);

            ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnectionReceiver.getLogger());
            SignalRFuture<Void> signalRFuture = mHubConnectionReceiver.start(clientTransport);

            try {
                Log.d("Connection:","trying...");

                try {
                    signalRFuture.get(60*10,TimeUnit.SECONDS);
                    Log.d("Connection:","ok");
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Log.d("Connection:","not ok");
                    ( (Activity) context ).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mainUrl.setText("Server time out...");
                        }
                    });
                }


            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                Log.d("Connection:","server unreachable");
                ( (Activity) context ).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainUrl.setText("Server unreachable...");
                    }
                });

            }

            //Toast.makeText(context, "Receiver Connected....", Toast.LENGTH_SHORT).show();
            recieveAllMsgFromReceiver("Receiver");


            RConnectionId = mHubConnectionReceiver.getConnectionId();

             result = null;
            try {

                try {
                    Log.d("RecConnIdToCaller","Trying...");
                    result = mHubProxyCaller.invoke(String.class, "SetReceiverConnectionIdToCaller",RConnectionId).get(60*3, TimeUnit.SECONDS);
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    ( (Activity) context ).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            mainUrl.setText("Server time out...");
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                ( (Activity) context ).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainUrl.setText("Server unreachable...");
                    }
                });
            } catch (ExecutionException e) {
                e.printStackTrace();
                ( (Activity) context ).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        mainUrl.setText("Server unreachable...");
                    }
                });
            }

            try {
                Log.d("REG RID TO SERVER", result);
            }catch (Exception e)
            {
                Log.d("REG RID TO SERVER", "result is null");
            }

            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            if(Boolean.valueOf(result))
            {
                String s = sharedpreferences.getString(AppId,"");


                Log.d("Shared Pref - Value ",s);


                AppIDValue = null;

                if(TextUtils.isEmpty(s))
                {
                    Log.d("Shared Pref","New");
                    String conStr =  getConnectionId();

                    AppIDValue = conStr;
                    editor.putString(AppId, conStr);
                    editor.apply();
                }
                else
                {
                    Log.d("Shared Pref","Old");
                    String conStr = sharedpreferences.getString(AppId,"");
                    AppIDValue = conStr;

                }

                Store.getInstance().appId = AppIDValue;
                generateUID(AppIDValue);
            }






            System.out.println("Conn ID ----"+ mHubConnectionReceiver.getConnectionId());
            if(mHubConnectionReceiver !=null)
            {

                mHubConnectionReceiver.stateChanged(new StateChangedCallback() {
                    @Override
                    public void stateChanged(ConnectionState connectionState, ConnectionState connectionState1) {

                        System.out.println("State listener ---State :"+connectionState.name());
                        System.out.println("State listener ---State :"+connectionState1.name());


                    }
                });

            }

        }



    }
    public void recieveAllMsg(final String from)
    {

        mHubConnectionCaller.received(new MessageReceivedHandler() {

            @Override
            public void onMessageReceived(final JsonElement json) {
                Log.e("onMessageReceived -"+from, json.toString());

                mHandlerCaller.post(new Runnable() {
                    @Override
                    public void run() {
                       // Toast.makeText(context, json.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



    }
    public String getConnectionId()
    {
        String s = null;

        try {
            s = mHubProxyCaller.invoke(String.class,"GetNewAppID").get();
            System.out.println("new App Id______"+s);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return s ;

    }
    private void recieveAllMsgFromReceiver(final String from) {
        mHubConnectionReceiver.received(new MessageReceivedHandler() {


            @Override
            public void onMessageReceived(final JsonElement json) {
                Log.e("onMessageReceived |- > "+from, json.toString());


                   mHandlerReceiver.post(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            GsonBuilder gsonBuilder = new GsonBuilder();
                            gsonBuilder.setDateFormat("M/d/yy hh:mm a");
                            final Gson gson = gsonBuilder.create();
                            final Type collectionType = new TypeToken<ReceiverResponse>() {
                            }.getType();
                            ReceiverResponse response = gson.fromJson(json, collectionType);
                            System.out.println("AppApproved Change --- " + Boolean.valueOf(String.valueOf(response.isA())));
                            if(Boolean.valueOf(String.valueOf(response.isA())))
                            {
                                //Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                //Toast.makeText(context, "Not Ok", Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e)
                        {
                            System.out.println("Exception  --- " + e);
                        }



                    }
                });

            }
        });
    }
    public void generateUID(String appIdValue) {


        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        email = null;
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                Log.d("EMAIL : ",possibleEmail);

                if(email==null) {
                    email = possibleEmail;
                }

            }
        }



        String deviceid = getLocalBluetoothName() + " " + email + " "+android_id;


        Store.getInstance().deviceName = getLocalBluetoothName();




       new Activate(appIdValue,getLocalBluetoothName(),email,android_id).execute();

    }
    public static String getLocalBluetoothName(){
        BluetoothAdapter mBluetoothAdapter = null;
        if(mBluetoothAdapter == null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        String name = "Emulator";
        try {
            name = mBluetoothAdapter.getName();
        }
        catch (Exception e)
        {

        }
        if(name == null){
            System.out.println("Name is null!");
            name = mBluetoothAdapter.getName();


        }
        return name;
    }

    class Activate extends AsyncTask
    {
        String appIdValue;
        String localBluetoothName;
        String email;
        String android_id;
        String result;
        private String result1;

        public Activate(String appIdValue, String localBluetoothName, String email, String android_id) {
            this.appIdValue = appIdValue;
            this.localBluetoothName = localBluetoothName;
            this.email = email;
            this.android_id = android_id;
        }
        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                HashMap<String,String> params = new HashMap<String, String>();
                result = mHubProxyCaller.invoke(String.class,"LoginHubCaller",appIdValue,localBluetoothName,email,android_id).get();

            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);


            System.out.println("login stat______"+result);
            Store.getInstance().mHubProxyCaller =  mHubProxyCaller;
            Store.getInstance().mHubProxyReceiver =  mHubProxyReceiver;


            Store.getInstance().mHubConnectionReceiver =  mHubConnectionReceiver;
            Store.getInstance().mHubConnectionCaller =  mHubConnectionCaller;

            Store.getInstance().mHandlerCaller = mHandlerCaller;
            Store.getInstance().mHandlerReceiver = mHandlerReceiver;


            System.out.println("mHubProxyCaller = "+ Store.getInstance().mHubProxyCaller);

            if(result!=null) {
                if (Boolean.valueOf(result)) {

                    connect();
                } else {
                    message.setText("Waiting for approval..");
                    Toast.makeText(context, "Waiting for approval...", Toast.LENGTH_SHORT).show();

                }
            }
            else {
                message.setText("Server unreachable");
                Toast.makeText(context, "Server unreachable...", Toast.LENGTH_SHORT).show();
            }



        }
    }


}
