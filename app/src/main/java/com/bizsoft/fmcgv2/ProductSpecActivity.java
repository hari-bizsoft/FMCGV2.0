package com.bizsoft.fmcgv2;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.adapter.ProductAdapter;
import com.bizsoft.fmcgv2.adapter.ProductSpecAdapter;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.ProductSpecProcess;
import com.bizsoft.fmcgv2.dataobject.ProductSpecProcessDetails;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.bizsoft.fmcgv2.service.SignalRService;
import com.bizsoft.fmcgv2.signalr.pojo.PDetailsItem;
import com.bizsoft.fmcgv2.signalr.pojo.ProductSpec;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductSpecActivity extends AppCompatActivity {

    EditText date_chooser;
    EditText productName;
    EditText quatity;
    ListView productList;
    Button save,clear;
    ImageButton datePicker;
    ImageButton productSearch;
    ImageButton quantityPicker;
    private int year,month,day;
    ArrayList<ProductSpec> productSpecList;

    private String toDateValue;
    private ProductSpec choosedProduct;
    ProductSpecAdapter productAdapter;
    private ArrayList<PDetailsItem> choosedOutputProd = new ArrayList<PDetailsItem>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_spec);

        getSupportActionBar().setTitle("Product Specification");

        date_chooser = (EditText) findViewById(R.id.date_chooser_text_box);
        productName = (EditText) findViewById(R.id.product_name);
        quatity = (EditText) findViewById(R.id.quantity_text);

        datePicker = (ImageButton) findViewById(R.id.date_chooser);
        productSearch = (ImageButton) findViewById(R.id.search_button);
        quantityPicker = (ImageButton) findViewById(R.id.number_picker_button);

        productList = (ListView) findViewById(R.id.listView);
        clear = (Button) findViewById(R.id.clear);
        save= (Button) findViewById(R.id.save);


        init();


       productAdapter = new ProductSpecAdapter(ProductSpecActivity.this,choosedOutputProd);
        productList.setAdapter(productAdapter);



      /*  date_chooser.setEnabled(false);
        date_chooser.setClickable(true);
        productName.setEnabled(false);
        productName.setClickable(true);
        quatity.setEnabled(false);
        quatity.setClickable(true);
*/

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);

            }
        });
        date_chooser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(999);

            }
        });
        productSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clear();
                showProducts();

            }
        });
        productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                clear();
                showProducts();

            }
        });
        quantityPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(choosedProduct!=null)
                {
                    chooeseQuantity(choosedProduct);
                }

            }
        });

        quatity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(choosedProduct!=null)
                {
                    chooeseQuantity(choosedProduct);
                }

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    validate();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    private void validate() throws ParseException {

        boolean status = true;

        if(TextUtils.isEmpty(date_chooser.getText().toString()))
        {
            date_chooser.setError("Field cannot be empty");
            status = false;
        }
        if(TextUtils.isEmpty(productName.getText().toString()))
        {
            productName.setError("Field cannot be empty");
            status = false;
        }
        if(TextUtils.isEmpty(quatity.getText().toString()))
        {
            quatity.setError("Field cannot be empty");
            status = false;
        }
        if(status)
        {


            updateProductSpec();

        }
        else
        {

        }

    }

    private void updateProductSpec() throws ParseException {

        int masterQuantity = ((int) choosedProduct.getAvailable());

        int fromUser = Integer.parseInt(quatity.getText().toString());

        int newQuantityofMaster = masterQuantity - fromUser;

        choosedProduct.setAvailable(newQuantityofMaster);
        choosedProduct.setRQty(fromUser);





        for(int i=0;i<choosedOutputProd.size();i++)
        {

            int detailAvailableStock = ((int) choosedOutputProd.get(i).getAvailable());
            int detailQuantity = ((int)choosedOutputProd.get(i).getQty());
            int newDetailsStock = detailAvailableStock + detailQuantity;
            choosedOutputProd.get(i).setAvailable(newDetailsStock);
        }



        createProductSpecProcess();





        choosedOutputProd = (ArrayList<PDetailsItem>) choosedProduct.getPDetails();
        System.out.println("Product details size == "+choosedProduct.getPDetails().size());
        productAdapter.productList =choosedOutputProd;
        productAdapter.notifyDataSetChanged();
        clear();






        Toast.makeText(this, "Product Saved..", Toast.LENGTH_SHORT).show();










    }

    private void createProductSpecProcess() throws ParseException {

        ProductSpecProcess productSpecProcess = new ProductSpecProcess();
        productSpecProcess.setAvailable(choosedProduct.getAvailable());


        Date date = BizUtils.getDateFromString(date_chooser.getText().toString(),"dd/MM/yyyy");
        productSpecProcess.setDate(date);
        productSpecProcess.setId((long) choosedProduct.getId());
        // productSpecProcess.setPDetails(choosedOutputProd);
        for(int i=0;i<choosedOutputProd.size();i++)
        {

            ProductSpecProcessDetails productSpecProcessDetails = new ProductSpecProcessDetails();
            productSpecProcessDetails.setId((long) choosedOutputProd.get(i).getId());
            productSpecProcessDetails.setProductId((int) choosedOutputProd.get(i).getProductId());
            productSpecProcessDetails.setPSId(2);
            productSpecProcessDetails.setQty(choosedOutputProd.get(i).getQty());
            productSpecProcess.getPDetails().add(productSpecProcessDetails);
        }



        productSpecProcess.setProductId((int) choosedProduct.getProductId());
        productSpecProcess.setProductName(choosedProduct.getProductName());
        productSpecProcess.setPSPTypeId(2);
        productSpecProcess.setQty(choosedProduct.getRQty());



        BizUtils.prettyJson("Prod Spec",productSpecProcess);


        productSpecProcess.setSynced(false);
        Store.getInstance().prodcutSpecProcess.add(productSpecProcess);


        try {
            BizUtils.storeAsJSON("ProductSpecProcessList",BizUtils.getJSON("productspecprocess",Store.getInstance().dealer));
            System.out.println("DB 'ProductSpecProcessList' Updated..on local storage");
        } catch (ClassNotFoundException e) {

            System.err.println("Unable to write to DB");
        }




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
            String date = (arg2+1)+"/"+arg3+"/"+arg1;

                date_chooser.setText(String.valueOf(date));
                toDateValue = String.valueOf(date);


            Toast.makeText(ProductSpecActivity.this, date, Toast.LENGTH_SHORT).show();
        }
    };
    public void chooeseQuantity(final ProductSpec prod)
    {
        final Dialog dialog = new Dialog(ProductSpecActivity.this);
        dialog.setTitle("Choose Quantity");
        dialog.setContentView(R.layout.number_picker);
        NumberPicker numberPicker = (NumberPicker) dialog.findViewById(R.id.np);
        final TextView q = (TextView) dialog.findViewById(R.id.tv);
        q.setText("Choose Quantity");




        numberPicker.setMinValue(1);
        //Specify the maximum value/number of NumberPicker


        System.out.println("available = "+prod.getAvailable());
        numberPicker.setMaxValue(Integer.parseInt(String.valueOf(prod.getAvailable())));

        //Gets whether the selector wheel wraps when reaching the min/max value.
        numberPicker.setWrapSelectorWheel(true);

        //Set a value change listener for NumberPicker
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                //Display the newly selected number from picker

                System.out.println("ON Change = "+newVal);
                int x = 0;

                try {

                    x = newVal;
                    if (x >= 1) {

                        x = newVal;


                        quatity.setText(String.valueOf(x));

                        for(int i=0;i<prod.getPDetails().size();i++)
                        {
                            prod.getPDetails().get(i).setQty(x*prod.getPDetails().get(i).getRefQty());



                        }
                        productAdapter.productList = (ArrayList<PDetailsItem>) prod.getPDetails();
                        productAdapter.notifyDataSetChanged();


                    } else {
                        Toast.makeText(ProductSpecActivity.this, "No Stock", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(ProductSpecActivity.this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }
    public  void showProducts()
    {
        productSpecList  = new ArrayList<ProductSpec>();
        productSpecList = Store.getInstance().productSpecList;
        final ArrayList<String> strings = new ArrayList<String>();
        for(int i=0;i<productSpecList.size();i++)
        {
            strings.add(productSpecList.get(i).getProductName());
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter(ProductSpecActivity.this, R.layout.support_simple_spinner_dropdown_item , strings);



        final Dialog dialog = new Dialog(ProductSpecActivity.this);
        dialog.setTitle("Choose Product");
        dialog.setContentView(R.layout.show_stock_home_list_dialog);
        ListView listView = (ListView) dialog.findViewById(R.id.stockhome_list);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(ProductSpecActivity.this, strings.get(position), Toast.LENGTH_SHORT).show();
                choosedProduct  = productSpecList.get(position);
                productName.setText(String.valueOf(choosedProduct.getProductName()));


              //  BizUtils.prettyJson("Product details ",choosedProduct.getPDetails());




                choosedOutputProd = (ArrayList<PDetailsItem>) choosedProduct.getPDetails();
                System.out.println("Product details size == "+choosedProduct.getPDetails().size());
                productAdapter.productList =choosedOutputProd;
                productAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
    public void init()
    {
        ArrayList<ProductSpec> productSpecs = Store.getInstance().productSpecList;

        for(int i=0;i<productSpecs.size();i++)
        {
            ArrayList<PDetailsItem> pDetails = (ArrayList<PDetailsItem>) productSpecs.get(i).getPDetails();

             for(int x = 0;x<pDetails.size();x++)
            {
                pDetails.get(x).setRefQty(pDetails.get(x).getQty());
                pDetails.get(x).setRefAvailable(pDetails.get(x).getAvailable());

            }





        }

        quatity.setText("1");
        date_chooser.setText(String.valueOf(BizUtils.getCurrentDate()));
    }

    public void clear()
    {
        ArrayList<ProductSpec> productSpecs = Store.getInstance().productSpecList;

        for(int i=0;i<productSpecs.size();i++)
        {
            ArrayList<PDetailsItem> pDetails = (ArrayList<PDetailsItem>) productSpecs.get(i).getPDetails();

            for(int x = 0;x<pDetails.size();x++)
            {
                pDetails.get(x).setQty(pDetails.get(x).getRefQty());


            }
            quatity.setText("1");
            productName.setText("");
            date_chooser.setText(String.valueOf(BizUtils.getCurrentDate()));

            productAdapter.productList = new ArrayList<PDetailsItem>();
            productAdapter.notifyDataSetChanged();




        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        clear();
    }
}
