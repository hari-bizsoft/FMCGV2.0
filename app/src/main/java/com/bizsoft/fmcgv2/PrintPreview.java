/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.bizsoft.fmcgv2.dataobject.Company;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import static com.bizsoft.fmcgv2.DashboardActivity.appDiscount;
import static com.bizsoft.fmcgv2.DashboardActivity.currentSaleType;
import static com.bizsoft.fmcgv2.DashboardActivity.fromCustomer;
import static com.bizsoft.fmcgv2.DashboardActivity.paymentModeValue;
import static com.bizsoft.fmcgv2.DashboardActivity.tenderAmount;


public class PrintPreview extends AppCompatActivity {

    TextView companyId,companyName,companyPhoneNumber,companyAddLine1,companyAddLine2,companyGst,companyMail,companyPostal;
    TextView customerId,customerName,customerPhoneNumber,customerAddLine1,customerAddLine2,customerGst,customerInCharge,customerPostal;
    Customer customer;
    static Company company = new Company();
    ListView listView;
    TextView saleId,saleType,saleIdLabel;
    TextView subTotal,gst,grantTotal;
    private Bitmap bm;
    private ArrayList<Product> productList;
    private String fpath;
    TextView balanceRM,receivedRM,dealerName,poweredBy,billId,billDate;
    private String billIdValue;
    private String billDateValue;
    Button saveAsPdf,print;
    TextView paymentMode;
    private TextView brm,rrm;
    TextView discountValue;
    TextView discountLabel;

    TextView chequeNumber,chequeLabel;
    TextView paymodeLabel;
    TextView roundOffValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_preview);
      /*  try
        {
        */

        companyId = (TextView) findViewById(R.id.company_id);
        companyName = (TextView) findViewById(R.id.company_name);
        companyPhoneNumber= (TextView) findViewById(R.id.telephone);
        companyAddLine1= (TextView) findViewById(R.id.address_line_1);
        companyAddLine2 = (TextView) findViewById(R.id.address_line_2);
        companyGst = (TextView) findViewById(R.id.gst_no);
        companyMail = (TextView) findViewById(R.id.email);
        paymodeLabel = (TextView) findViewById(R.id.paymode_label);
        chequeNumber  = (TextView) findViewById(R.id.cheque_no);
        chequeLabel = (TextView) findViewById(R.id.cheque_no_label);
            paymentMode = (TextView) findViewById(R.id.payment_mode);
            saveAsPdf = (Button) findViewById(R.id.save_as_pdf);
            print = (Button) findViewById(R.id.dc_print);
            print.setVisibility(View.GONE);

            companyPostal= (TextView) findViewById(R.id.postalcode);


            customerId = (TextView) findViewById(R.id.customer_id);
            customerName = (TextView) findViewById(R.id.customer_name);
            customerPhoneNumber= (TextView) findViewById(R.id.customer_ph);
            customerAddLine1= (TextView) findViewById(R.id.customer_address_line_1);
            customerAddLine2 = (TextView) findViewById(R.id.customer_address_line_2);
            customerGst = (TextView) findViewById(R.id.customer_gst_no);
            discountValue = (TextView) findViewById(R.id.discount_value);

            customerInCharge = (TextView) findViewById(R.id.person_incharge);

            listView = (ListView) findViewById(R.id.listview);

            saleId = (TextView) findViewById(R.id.sale_id);
            saleType = (TextView) findViewById(R.id.sale_type);
            saleIdLabel = (TextView) findViewById(R.id.sale_id_label);

            subTotal = (TextView) findViewById(R.id.sub_total);
            gst = (TextView) findViewById(R.id.GST);
            roundOffValue = (TextView) findViewById(R.id.round_off);
            grantTotal= (TextView) findViewById(R.id.grand_total);
            receivedRM= (TextView) findViewById(R.id.received_rm);
            balanceRM= (TextView) findViewById(R.id.balance_rm);


            dealerName = (TextView) findViewById(R.id.dealer_name);

            poweredBy= (TextView) findViewById(R.id.powered_by);
            billId = (TextView) findViewById(R.id.bill_id);
            billDate= (TextView) findViewById(R.id.bill_date);

            saleId.setVisibility(View.INVISIBLE);
            saleIdLabel.setVisibility(View.INVISIBLE);
            rrm= (TextView) findViewById(R.id.rrm);
            brm= (TextView) findViewById(R.id.brm);
            discountLabel = (TextView) findViewById(R.id.discount_label);

            saveAsPdf.setVisibility(View.INVISIBLE);

            if(BizUtils.getTransactionType(DashboardActivity.currentSaleType) == Store.getInstance().SALE)
            {
                if(!DashboardActivity.paymentModeValue.toLowerCase().contains("cash"))
                {
                    fromCustomer.setVisibility(View.GONE);
                    tenderAmount.setVisibility(View.GONE);
                }
                else
                {
                    fromCustomer.setVisibility(View.VISIBLE);
                    tenderAmount.setVisibility(View.VISIBLE);
                }
            }
            else
            {
                fromCustomer.setVisibility(View.GONE);
                tenderAmount.setVisibility(View.GONE);
            }


            System.out.println("currentSaleType ==="+currentSaleType);

            System.out.println("currentSaleType.toLowerCase().contains(\"order\") ==="+currentSaleType.toLowerCase().contains("order"));

            saleType.setText(String.valueOf(currentSaleType));
            int invce = 1;


                if(currentSaleType.toLowerCase().contains("return"))
                {



                    for(int i=0;i<Store.getInstance().customerList.size();i++) {
                        invce = invce + Store.getInstance().customerList.get(i).getSaleReturnOfCustomer().size();
                    }



                    System.out.println("--------------------><><><><><>SR REF"+DashboardActivity.calculateRefCode(Store.getInstance().user.getSRRefCode(),invce));
                    billId.setText(String.valueOf(BizUtils.calculateShortCode(DashboardActivity.currentSaleType)+DashboardActivity.calculateRefCode(Store.getInstance().user.getSRRefCode(),invce)));

                }
                else
                if(currentSaleType.toLowerCase().contains("order"))
                {
                    System.out.println("--------------------><><><><><>SO REF"+DashboardActivity.calculateRefCode(Store.getInstance().user.getSORefCode(),invce));



                    for(int i=0;i<Store.getInstance().customerList.size();i++) {
                        invce = invce + Store.getInstance().customerList.get(i).getSaleOrdersOfCustomer().size();

                    }

                    billId.setText(String.valueOf(BizUtils.calculateShortCode(DashboardActivity.currentSaleType)+DashboardActivity.calculateRefCode(Store.getInstance().user.getSORefCode(),invce)));
                }
                else
                {

                    System.out.println("--------------------><><><><><>Sal REF"+DashboardActivity.calculateRefCode(Store.getInstance().user.getSalRefCode(),invce));

                    for(int i=0;i<Store.getInstance().customerList.size();i++) {
                        invce = invce + Store.getInstance().customerList.get(i).getSalesOfCustomer().size();

                    }

                        billId.setText(String.valueOf(BizUtils.calculateShortCode(DashboardActivity.currentSaleType)+DashboardActivity.calculateRefCode(Store.getInstance().user.getSalRefCode(),invce)));


                }








            final BizUtils bizUtils = new BizUtils();
            customer = Store.getInstance().customerList.get(Store.getInstance().currentCustomerPosition);
            company = BizUtils.getCompany();
            System.out.println("Customer = "+customer.getLedgerName());
            System.out.println("Company = "+company.getCompanyName());
            System.out.println("sale size = "+customer.getSale().size());
            System.out.println("sale order size = "+customer.getSaleOrder().size());




       companyId.setText(String.valueOf(company.getId()));
       companyName.setText(String.valueOf(company.getCompanyName()));
       companyPhoneNumber.setText(String.valueOf(company.getTelephoneNo()));
       companyAddLine1.setText(String.valueOf(company.getAddressLine1()));
       companyAddLine2.setText(String.valueOf(company.getAddressLine1()));
       companyGst.setText(String.valueOf(company.getGSTNo()));
       companyMail.setText(String.valueOf(company.getEMailId()));
            companyPostal.setText(String.valueOf(company.getPostalCode()));

            if(customer.getId()==null) {
                customerId.setText(String.valueOf("Unregistered"));
            }
            else
            {
                customerId.setText(String.valueOf(customer.getId()));
            }

            customerName.setText(String.valueOf(customer.getLedger().getLedgerName()));


            customerPhoneNumber.setText(String.valueOf(customer.getLedger().getMobileNo()));
            customerAddLine1.setText(String.valueOf(customer.getLedger().getAddressLine1()));
            customerAddLine2.setText(String.valueOf(customer.getLedger().getAddressLine2()));
            customerGst.setText(String.valueOf(customer.getLedger().getGSTNo()));

            customerInCharge.setText(String.valueOf(customer.getLedger().getPersonIncharge()));


            if(TextUtils.isEmpty(Store.getInstance().fromCustomer.getText()))
            {
                Store.getInstance().fromCustomer.setText("0.00");
            }
        receivedRM.setVisibility(View.GONE);
        balanceRM.setVisibility(View.GONE);
        if(BizUtils.getTransactionType(currentSaleType)==Store.getInstance().SALE) {
            if (paymentModeValue.toLowerCase().contains("cash")) {
                receivedRM.setVisibility(View.VISIBLE);
                balanceRM.setVisibility(View.VISIBLE);

                receivedRM.setText(String.valueOf(String.format("%.2f",Double.parseDouble(Store.getInstance().fromCustomer.getText().toString()))));
                balanceRM.setText(String.valueOf(String.format("%.2f",Double.parseDouble(Store.getInstance().tenderAmount.getText().toString()))));

            }

        }

            dealerName.setText(String.valueOf(Store.getInstance().dealerName));
            poweredBy.setText(String.valueOf("Denariu Soft SDN BHD"));
            billIdValue = company.getId()+"-"+ Store.getInstance().dealerId+"-"+customer.getId();

            billDateValue = bizUtils.getCurrentTime();

            billDate.setText(billDateValue);



            System.out.println("Size of temp prod in cart ="+ Store.getInstance().addedProductList.size());
            listView.setAdapter(Store.getInstance().addedProductAdapter);

            Store.getInstance().addedProductAdapter.setFrom("Preview");


            paymentMode.setText(String.valueOf(paymentModeValue));
        chequeNumber.setVisibility(View.INVISIBLE);
        chequeLabel.setVisibility(View.INVISIBLE);
        paymodeLabel.setVisibility(View.INVISIBLE);
        paymentMode.setVisibility(View.INVISIBLE);
        if(BizUtils.getTransactionType(currentSaleType)==Store.getInstance().SALE) {

            paymodeLabel.setVisibility(View.VISIBLE);
            paymentMode.setVisibility(View.VISIBLE);
            paymentMode.setText(String.valueOf(paymentModeValue));
            if(paymentModeValue.toLowerCase().contains("cheque"))
            {
                chequeNumber.setVisibility(View.VISIBLE);
                chequeLabel.setVisibility(View.VISIBLE);
                chequeNumber.setText(String.valueOf(DashboardActivity.chequeNo.getText().toString()));
            }
        }


             productList = new ArrayList<Product>();

            productList.addAll(Store.getInstance().addedProductList);



            double subTotal1 = 0;
            double gst1 = 0;
            double grandTotal1 = 0;
            double discount =0;
            double dp = 0;


            for(int i=0;i<productList.size();i++)
            {
                subTotal1 = subTotal1 + (productList.get(i).getFinalPrice());
            }


            if(TextUtils.isEmpty(DashboardActivity.discountValue.getText()))
            {
                discount =0;
                dp=0;
            }
            else
            {
                dp = Double.parseDouble(DashboardActivity.discountValue.getText().toString());
                discount = Double.parseDouble(appDiscount.getText().toString());
            }
            gst1 = Double.parseDouble(Store.getInstance().GST.getText().toString());

            grandTotal1 =  gst1 + subTotal1 ;

            try
            {
                grandTotal1 = grandTotal1 - discount;
            }
            catch (Exception e)
            {

            }

            subTotal.setText(String.valueOf(String.format("%.2f",subTotal1)));
            gst.setText(String.valueOf(String.format("%.2f",gst1)));
            grantTotal.setText(String.valueOf(String.format("%.2f",DashboardActivity.roundOff(grandTotal1))));

        discountLabel.setVisibility(View.GONE);
        discountValue.setVisibility(View.GONE);
        if(BizUtils.getDiscountType(DashboardActivity.discountType)==Store.getInstance().DISCOUNT_FOR_GRABD_TOTAL)
        {

            discountLabel.setVisibility(View.VISIBLE);
            discountValue.setVisibility(View.VISIBLE);
            if(DashboardActivity.grandDiscountTypeSpinner.getSelectedItem().toString().equals(Store.getInstance().discountTypePercentage)) {
                discountLabel.setText(String.valueOf("Discount ( " + dp + " " + DashboardActivity.grandDiscountTypeSpinner.getSelectedItem().toString() + ")= "));
            }else
            {
                discountLabel.setText(String.valueOf("Discount ( " + DashboardActivity.grandDiscountTypeSpinner.getSelectedItem().toString() + " )= "));
            }
            discountValue.setText(String.valueOf(String.format("%.2f",discount)));
        }
        roundOffValue.setText(String.valueOf(DashboardActivity.roundOffText.getText().toString()));







        if(BizUtils.getTransactionType(DashboardActivity.currentSaleType)==Store.getInstance().SALE) {
            if (DashboardActivity.paymentModeValue.toLowerCase().contains("cash")) {

                brm.setVisibility(View.VISIBLE);
                rrm.setVisibility(View.VISIBLE);
                balanceRM.setVisibility(View.VISIBLE);
                receivedRM.setVisibility(View.VISIBLE);


            } else {


                brm.setVisibility(View.GONE);
                rrm.setVisibility(View.GONE);
                balanceRM.setVisibility(View.GONE);
                receivedRM.setVisibility(View.GONE);
            }
        }
        else
        {
            brm.setVisibility(View.GONE);
            rrm.setVisibility(View.GONE);
            balanceRM.setVisibility(View.GONE);
            receivedRM.setVisibility(View.GONE);
        }
     /*  }
       catch (Exception e)
       {
           System.err.println("Exception = "+e.getMessage());
           System.err.println("Exception = "+e.getStackTrace());
       }
*/
       print.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {


           }
       });

    }
    public static Company getCompany()
    {

        for(Company company1 : Store.getInstance().companyList) {
            if(company1.getId() == Store.getInstance().companyID) {
                company  = company1;
                return company;
            }
        }
        return company;
    }






    public PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }


}
