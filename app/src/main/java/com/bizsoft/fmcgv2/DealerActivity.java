package com.bizsoft.fmcgv2;

import android.*;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.dataobject.Company;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.Network;
import com.bizsoft.fmcgv2.service.SignalRService;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DealerActivity extends AppCompatActivity {
    private static int SELECT_FILE = 1;
    Button save,clear;
    EditText dealerName,addressLine1,addressLine2,city,mobileNumber,gstNumber,postalCode,telephone,email,bankName;
    ImageView logo;
    private String userChoosenTask;

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    private int REQUEST_CAMERA;
    private boolean customImage;
    private String imageBase64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealer);

        getSupportActionBar().setTitle("Dealer Profile");

        logo = (ImageView) findViewById(R.id.logo);
        dealerName = (EditText) findViewById(R.id.dealer_name);

        addressLine1 = (EditText) findViewById(R.id.address_line_one);
        addressLine2 = (EditText) findViewById(R.id.address_line_two);

        city = (EditText) findViewById(R.id.state);
        mobileNumber = (EditText) findViewById(R.id.mobile_number);
        gstNumber = (EditText) findViewById(R.id.gst_number);
        postalCode = (EditText) findViewById(R.id.postal_code);
        telephone = (EditText) findViewById(R.id.telephone);
        email = (EditText) findViewById(R.id.email);
        bankName = (EditText) findViewById(R.id.bank_name);

        save = (Button) findViewById(R.id.save);
        clear = (Button) findViewById(R.id.clear);

        Bitmap bmp= BizUtils.StringToBitMap(Store.getInstance().dealerLogo);

        logo.setImageResource(R.drawable.denariusoft64);

        logo.setImageBitmap(bmp);

        logo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


               // selectImage();

            }
        });



        dealerName.setText(String.valueOf(Store.getInstance().dealer.getCompanyName()));
        addressLine1.setText(String.valueOf(Store.getInstance().dealer.getAddressLine1()));
        addressLine2.setText(String.valueOf(Store.getInstance().dealer.getAddressLine2()));
        city.setText(String.valueOf(Store.getInstance().dealer.getCityName()));
        mobileNumber.setText(String.valueOf(Store.getInstance().dealer.getMobileNo()));
        gstNumber.setText(String.valueOf(Store.getInstance().dealer.getGSTNo()));
        postalCode.setText(String.valueOf(Store.getInstance().dealer.getPostalCode()));
        email.setText(String.valueOf(Store.getInstance().dealer.getEMailId()));

        bankName.setEnabled(false);

        telephone.setText(String.valueOf(Store.getInstance().dealer.getTelephoneNo()));
        if(Store.getInstance().bankList!=null) {
            if(Store.getInstance().bankList.size()>0) {
                bankName.setText(String.valueOf(Store.getInstance().bankList.get(0).getLedger().getLedgerName()));
        }
            else
            {
                Toast.makeText(this, "No Bank Found..", Toast.LENGTH_SHORT).show();
            }
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validate();
            }
        });

    }
    public void selectImage()
    {

        final CharSequence[] items = { "Take Photo", "Choose from Library","Set Default",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(DealerActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= checkPermission(DealerActivity.this);
                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result)
                        cameraIntent();
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result)
                        galleryIntent();
                }
                else if (items[item].equals("Set Default")) {
                    logo.setImageResource(R.drawable.denariusoft64);
                    customImage = false;
                    dialog.dismiss();
                }
                else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }
    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        customImage = true;
        logo.setImageBitmap(bm);
    }
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>=android.os.Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    public  void validate()
    {
        boolean status = true;

        if(TextUtils.isEmpty(dealerName.getText().toString()))
        {
            dealerName.setError("Field cannot be empty");
            status = false;
        }

        if(TextUtils.isEmpty(addressLine1.getText().toString()))
        {
            status = false;
            addressLine1.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(addressLine2.getText().toString()))
        {
            status = false;
            addressLine2.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(city.getText().toString()))
        {
            status = false;
            city.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(mobileNumber.getText().toString()))
        {
            status = false;
            mobileNumber.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(gstNumber.getText().toString()))
        {
            status = false;
            gstNumber.setError("Field cannot be empty");
        }

        if(TextUtils.isEmpty(telephone.getText().toString()))
        {
            status = false;
            telephone.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(email.getText().toString()))
        {
            status = false;
            email.setError("Field cannot be empty");
        }
        if(TextUtils.isEmpty(postalCode.getText().toString()))
        {
            status = false;
            postalCode.setError("Field cannot be empty");
        }

        if(status)
        {


           createDealer();
        }
        else
        {

            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
        }

    }

    private void createDealer() {

        Company company = new Company();
        company = Store.getInstance().dealer;
        company.setCompanyName(String.valueOf(dealerName.getText().toString()));
        company.setAddressLine1(String.valueOf(addressLine1.getText().toString()));
        company.setAddressLine2(String.valueOf(addressLine2.getText().toString()));
        company.setCityName(String.valueOf(city.getText().toString()));
        company.setPostalCode(String.valueOf(postalCode.getText().toString()));
        company.setMobileNo(String.valueOf(mobileNumber.getText().toString()));
        company.setTelephoneNo(String.valueOf(telephone.getText().toString()));
        company.setEMailId(String.valueOf(email.getText().toString()));
        company.setGSTNo(String.valueOf(gstNumber.getText().toString()));


        Toast.makeText(this, "Dealer profile updated offline", Toast.LENGTH_SHORT).show();


        Store.getInstance().dealer.setSynced(false);

      //  Store.getInstance().dealerLogo = encodeImage();





    }
    public String encodeImage()
    {
        System.out.println("Update image");
        BitmapDrawable drawable = (BitmapDrawable) logo.getDrawable();
        Bitmap bitmap = drawable.getBitmap();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream .toByteArray();

        String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        Log.d("BITMAP", String.valueOf(encodedImage));
        imageBase64 = encodedImage;

        return imageBase64 ;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            System.out.println("Result code = "+resultCode);
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        customImage = true;
        logo.setImageBitmap(thumbnail);
    }

}
