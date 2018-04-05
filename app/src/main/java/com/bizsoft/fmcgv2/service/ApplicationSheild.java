package com.bizsoft.fmcgv2.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.MainActivity;
import com.bizsoft.fmcgv2.dataobject.ReceiverResponse;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;

/**
 * Created by GopiKing on 21-12-2017.
 */

public class ApplicationSheild {


    private HubConnection mHubConnectionCaller;
    private HubConnection mHubConnectionReceiver;
    private Handler mHandlerCaller;
    private Handler mHandlerReceiver;
    private HubProxy mHubProxyCaller;
    private HubProxy mHubProxyReceiver;

    SharedPreferences sharedpreferences ;
    private String MyPREFERENCES = "ACTIVATION_KEY";
    SharedPreferences.Editor editor;
    private String AppId = "AppId";
    private String android_id;
    private String email;
    private String AppIDValue;
    private String RConnectionId;
    private String TAG = "SIGNAL IR";
    static String fromClassName;

    Activity activity;
    Context context;
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
            this.host = Store.getInstance().baseUrl;
            this.context = context;
            this.progressDialog    = new ProgressDialog(this.context);
            this.progressDialog.setTitle("Connecting.....");
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


            mHubConnectionCaller = new HubConnection(host);

            String SERVER_HUB_CHAT = "ABServerHub";

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


            Action<HubResult> hr= new Action<HubResult>() {
                @Override
                public void run(HubResult hubResult) throws Exception {

                }
            };



            try {
                Log.d("Connection:","trying...");

                try {
                    signalRFuture.get(60, TimeUnit.SECONDS);
                    Log.d("Connection:","ok");
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Log.d("Connection:","not ok");
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();

            }

            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            new ConnectReceiver(context).execute();


            progressDialog.dismiss();
            //Toast.makeText(context, "Caller Connected....", Toast.LENGTH_SHORT).show();
            recieveAllMsg("Caller");

            // getCompanyDetails();


        }
    }
    class ConnectReceiver extends AsyncTask
    {
        String host =  null;
        Context context;
        ProgressDialog progressDialog;

        public ConnectReceiver(Context context) {
            this.host =Store.getInstance().baseUrl;
            this.context = context;
            this.progressDialog    = new ProgressDialog(this.context);
            this.progressDialog.setTitle("Connecting.....");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            this.progressDialog.show();

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
                                    Toast.makeText(context, finalMsg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                    , String.class);
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
                                    if(Boolean.valueOf(String.valueOf(response)))
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

                                        try {
                                            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                            activity.finish();
                                        }
                                        catch (Exception e)
                                        {
                                            Log.e("Error",e.toString());
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
                    signalRFuture.get(60,TimeUnit.SECONDS);
                    Log.d("Connection:","ok");
                } catch (TimeoutException e) {
                    e.printStackTrace();
                    Log.d("Connection:","not ok");
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();

            }

            return null;
        }
        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);

            progressDialog.dismiss();
            Toast.makeText(context, "Receiver Connected....", Toast.LENGTH_SHORT).show();
            recieveAllMsgFromReceiver("Receiver");


            RConnectionId = mHubConnectionReceiver.getConnectionId();

            String result = null;
            try {
                result = mHubProxyCaller.invoke(String.class, "SetReceiverConnectionIdToCaller",RConnectionId).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            Log.d("REG RID TO SERVER",result);

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
                        Toast.makeText(context, json.toString(), Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(context, "Ok", Toast.LENGTH_SHORT).show();

                            }
                            else
                            {
                                Toast.makeText(context, "Not Ok", Toast.LENGTH_SHORT).show();
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
    private void generateUID(String appIdValue) {


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





        new Activate(appIdValue,getLocalBluetoothName(),email,android_id).execute();

    }
    public String getLocalBluetoothName(){
        BluetoothAdapter mBluetoothAdapter = null;
        if(mBluetoothAdapter == null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        }
        String name = mBluetoothAdapter.getName();
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


            if(Boolean.valueOf(result)) {
                MainActivity m = new MainActivity();
                m.connect();
            }
            else
            {
                Toast.makeText(context, "Waiting for approval...", Toast.LENGTH_SHORT).show();

            }



        }
    }
}
