package com.bizsoft.fmcgv2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bizsoft.fmcgv2.dataobject.Company;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Product;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.bizsoft.fmcgv2.DashboardActivity.appDiscount;
import static com.bizsoft.fmcgv2.DashboardActivity.currentSaleType;


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
            paymentMode = (TextView) findViewById(R.id.payment_mode);
            saveAsPdf = (Button) findViewById(R.id.save_as_pdf);
            print = (Button) findViewById(R.id.print);
            print.setVisibility(View.GONE);

            companyPostal= (TextView) findViewById(R.id.postalcode);


            customerId = (TextView) findViewById(R.id.customer_id);
            customerName = (TextView) findViewById(R.id.dealer_name);
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
            customerName.setText(String.valueOf(customer.getLedgerName()));


            customerPhoneNumber.setText(String.valueOf(customer.getMobileNo()));
            customerAddLine1.setText(String.valueOf(customer.getAddressLine1()));
            customerAddLine2.setText(String.valueOf(customer.getAddressLine1()));
            customerGst.setText(String.valueOf(customer.getGSTNo()));

            customerInCharge.setText(String.valueOf(customer.getPersonIncharge()));


            if(TextUtils.isEmpty(Store.getInstance().fromCustomer.getText()))
            {
                Store.getInstance().fromCustomer.setText("0.00");
            }

            receivedRM.setText(String.valueOf(String.format("%.2f",Double.parseDouble(Store.getInstance().fromCustomer.getText().toString()))));
            balanceRM.setText(String.valueOf(String.format("%.2f",Double.parseDouble(Store.getInstance().tenderAmount.getText().toString()))));
            dealerName.setText(String.valueOf(Store.getInstance().dealerName));
            poweredBy.setText(String.valueOf("Denariu Soft SDN BHD"));
            billIdValue = company.getId()+"-"+ Store.getInstance().dealerId+"-"+customer.getId();

            billDateValue = bizUtils.getCurrentTime();

            billDate.setText(billDateValue);



            System.out.println("Size of temp prod in cart ="+ Store.getInstance().addedProductList.size());
            listView.setAdapter(Store.getInstance().addedProductAdapter);

            Store.getInstance().addedProductAdapter.setFrom("Preview");


            paymentMode.setText(String.valueOf(DashboardActivity.paymentModeValue));


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
            grantTotal.setText(String.valueOf(String.format("%.2f",grandTotal1)));
            discountLabel.setText(String.valueOf("Discount ( "+dp+" %)= "));
            discountValue.setText(String.valueOf(String.format("%.2f",discount)));


            saveAsPdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean status = write(String.valueOf(customer.getLedgerName()+" "+bizUtils.getCurrentTime())+" ( "+company.getCompanyName()+")", "test");

                    if (status)
                    {
                        Toast.makeText(PrintPreview.this, "Saved", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(PrintPreview.this, "not Saved", Toast.LENGTH_SHORT).show();
                    }
                }
            });




            if(paymentMode.getText().toString().contains("PNT") || paymentMode.getText().toString().contains("cheque"))
            {


                brm.setVisibility(View.GONE);
                rrm.setVisibility(View.GONE);
                balanceRM.setVisibility(View.GONE);
                receivedRM.setVisibility(View.GONE);

            }
            else
            {

                brm.setVisibility(View.VISIBLE);
                rrm.setVisibility(View.VISIBLE);
                balanceRM.setVisibility(View.VISIBLE);
                receivedRM.setVisibility(View.VISIBLE);
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





    public Boolean write(String fname, String fcontent) {
        try {

            BizUtils bizUtils = new BizUtils();










            File folder = new File(Environment.getExternalStorageDirectory() +
                    File.separator + "fmcg");
            boolean success = true;
            if (!folder.exists()) {
                success = folder.mkdirs();
            }
            if (success) {
                // Do something on success
                Log.d("FOLDER CREATED","TRUE");
            } else {
                // Do something else on failure
                Log.d("FOLDER CREATED","FALSE");
            }
            folder.getAbsolutePath();
            Log.d("ABS PATH",folder.getAbsolutePath());

            //Create file path for Pdf
            fpath = folder.getAbsolutePath()+"/" + fname + ".pdf";

            File file = new File(fpath);

            System.out.println("fpath = "+fpath);

            if (!file.exists()) {
                file.createNewFile();
            }
            // To customise the text of the pdf
            // we can use FontFamily
            Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12, Font.BOLD, new BaseColor(0, 0, 0));
            Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN,
                    12);
            // create an instance of itext document
            Document document = new Document();

            PdfWriter.getInstance(document,
                    new FileOutputStream(file.getAbsoluteFile()));
            document.open();







            try {
                // get receipt stream
                InputStream ims = getAssets().open("fmcglogo64.png");
                Bitmap bmp = BitmapFactory.decodeStream(ims);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                Image image = Image.getInstance(stream.toByteArray());


                PdfPTable logo = new PdfPTable(1);
                logo.setWidthPercentage(15);
                image.setAlignment(Image.ALIGN_CENTER);
                logo.addCell(image);
                document.add(logo);


            }
            catch(IOException ex)
            {
                ex.getStackTrace();
            }

            //using add method in document to insert a paragraph
            PdfPTable thank = new PdfPTable(1);
            thank.setWidthPercentage(100);
            thank.addCell(getCell(getString(R.string.app_name), PdfPCell.ALIGN_CENTER));
            document.add(thank);
            document.add(new Paragraph("_____________________________________________________________________________"));
            document.add(new Paragraph("                                                                             "));

            PdfPTable cn = new PdfPTable(1);
            cn.setWidthPercentage(100);

            cn.addCell(getCell("Company Name :"+company.getCompanyName(), PdfPCell.ALIGN_LEFT));

            document.add(cn);



            document.add(new Paragraph("Address :"+company.getAddressLine1()+","+company.getAddressLine2()+","+company.getPostalCode()));
            document.add(new Paragraph("Ph No:"+company.getTelephoneNo()));
            document.add(new Paragraph("Email :"+company.getEMailId()));
            document.add(new Paragraph("GST No:"+company.getGSTNo()));
            document.add(new Paragraph("_____________________________________________________________________________"));
            document.add(new Paragraph("Bill Id :"+billIdValue));
            document.add(new Paragraph("Bill Date :"+billDateValue));


            PdfPTable cn1 = new PdfPTable(1);
            cn1.setWidthPercentage(100);
            cn1.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            cn1.addCell(getCell("                   ", PdfPCell.ALIGN_LEFT));
            cn1.addCell(getCell("Customer Details", PdfPCell.ALIGN_CENTER));


            document.add(cn1);

            document.add(new Paragraph("ID: "+customer.getId()));
            document.add(new Paragraph("Name: "+customer.getLedgerName()));
            document.add(new Paragraph("Person In Charge: "+customer.getLedgerName()));

            document.add(new Paragraph("Ph No: "+customer.getMobileNo()));
            document.add(new Paragraph("Address : "+customer.getAddressLine1()+","+customer.getAddressLine2()));


            document.add(new Paragraph("GST No: "+customer.getGSTNo()));
            document.add(new Paragraph("_____________________________________________________________________________"));
            PdfPTable cn2 = new PdfPTable(1);
            cn2.setWidthPercentage(100);
            cn2.addCell(getCell("                   ", PdfPCell.ALIGN_LEFT));
            cn2.addCell(getCell(currentSaleType, PdfPCell.ALIGN_CENTER));

            document.add(cn2);

            PdfPTable pm = new PdfPTable(2);
            pm.setWidthPercentage(100);

            pm.addCell(getCell("Sale/Sale Order/Sale Return: "+ currentSaleType,PdfPTable.ALIGN_LEFT));
            pm.addCell(getCell("Payment Mode: "+ DashboardActivity.paymentModeValue,PdfPTable.ALIGN_RIGHT));
            document.add(pm);

            PdfPTable cn3 = new PdfPTable(1);
            cn3.setWidthPercentage(100);
            cn3.addCell(getCell("Item Details", PdfPCell.ALIGN_CENTER));
            cn2.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(cn3);



            PdfPTable line = new PdfPTable(1);
            line.setWidthPercentage(100);
            line.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line);


            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);

            table.addCell(getCell("S.No", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("ID", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Name", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Qty", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Price", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Discount", PdfPCell.ALIGN_LEFT));
            table.addCell(getCell("Amount", PdfPCell.ALIGN_RIGHT));

            document.add(table);
            PdfPTable line1 = new PdfPTable(1);
            line1.setWidthPercentage(100);
            line1.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line1);

            for(int i=0;i<productList.size();i++)
            {
                PdfPTable table1 = new PdfPTable(7);
                table1.setWidthPercentage(100);

                table1.addCell(getCell(String.valueOf(i+1), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(productList.get(i).getId()),PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(productList.get(i).getProductName(), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(+productList.get(i).getQty()), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(+productList.get(i).getMRP()), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(+productList.get(i).getDiscountAmount()), PdfPCell.ALIGN_LEFT));
                table1.addCell(getCell(String.valueOf(productList.get(i).getFinalPrice()), PdfPCell.ALIGN_RIGHT));

                document.add(table1);

            }


            String gstSpace = "";

            String st= String.valueOf(subTotal.getText().toString());
            int subTotalLength = st.length();

            String gstx = String.valueOf( Store.getInstance().GST.getText().toString());
            int gstLength = gstx.length();




            int c = subTotalLength - gstLength;

            for (int f = 0; f < c; f++) {
                gstSpace = gstSpace + " ";
            }

            double s = Double.parseDouble(subTotal.getText().toString());
            double rrm = Double.parseDouble(receivedRM.getText().toString());
            double brm = Double.parseDouble(balanceRM.getText().toString());

            String mgt = String.valueOf(String.format("%.2f",s ));
            String ra = String.valueOf(String.format("%.2f",rrm));
            String ba = String.valueOf(String.format("%.2f",brm));

            int mgtL = mgt.length();
            int raL = ra.length();
            int baL = ba.length();

            String mgSpace = "";
            String raSpace = "";
            String baSpace = "";


            int x = 0;
            int y = 0;
            if (mgtL > raL) {
                x = mgtL - raL;
                y = mgtL - baL;
                for (int i = 0; i < x; i++) {
                    raSpace = raSpace + " ";

                }
                for (int i = 0; i < y; i++) {
                    baSpace = baSpace + " ";

                }
            } else if (raL > mgtL) {
                x = raL - mgtL;
                y = raL - baL;
                for (int i = 0; i < x; i++) {
                    mgSpace = mgSpace + " ";
                }
                for (int i = 0; i < y; i++) {
                    baSpace = baSpace + " ";

                }
            }



            PdfPTable line3 = new PdfPTable(1);
            line3.setWidthPercentage(100);
            line3.addCell(getCell("_____________________________________________________________________________", PdfPCell.ALIGN_LEFT));
            document.add(line3);
            document.add(new Paragraph("                                                  "));

            PdfPTable stx = new PdfPTable(1);
            stx.setWidthPercentage(97);

            stx.addCell(getCell("Sub Total RM "+subTotal.getText().toString()+"  ",PdfPCell.ALIGN_RIGHT));
            stx.addCell(getCell("  ",PdfPCell.ALIGN_LEFT));
            document.add(stx);
            PdfPTable gst = new PdfPTable(1);
            gst.setWidthPercentage(97);
            gst.addCell(getCell("GST RM "+gstSpace+ Store.getInstance().GST.getText().toString()+"  ",PdfPCell.ALIGN_RIGHT));
            document.add(gst);


            PdfPTable gt = new PdfPTable(1);
            gt.setWidthPercentage(97);


            gt.addCell(getCell("Discount ( "+DashboardActivity.discountValue.getText().toString()+") % "+gstSpace+appDiscount.getText().toString()+"  ",PdfPCell.ALIGN_RIGHT));

            gt.addCell(getCell("      ",PdfPCell.ALIGN_RIGHT));

            gt.addCell(getCell("Grand Total RM "+mgSpace+grantTotal.getText().toString()+"  ",PdfPCell.ALIGN_RIGHT));
            gt.addCell(getCell("____________________________________________________________________________",PdfPCell.ALIGN_LEFT));

            gt.addCell(getCell("  ",PdfPCell.ALIGN_RIGHT));
            System.out.println("Pay Mode ===="+paymentMode.getText().toString());
            if(paymentMode.getText().toString().contains("PNT") || paymentMode.getText().toString().contains("cheque")) {
                    }
                    else
            {
                gt.addCell(getCell("Received RM "+raSpace+receivedRM.getText().toString()+"  ",PdfPCell.ALIGN_RIGHT));
                gt.addCell(getCell("  ",PdfPCell.ALIGN_LEFT));



                gt.addCell(getCell("Balance RM " + baSpace + balanceRM.getText().toString() + "  ", PdfPCell.ALIGN_RIGHT));
                gt.addCell(getCell("___________________________________________________________________________", PdfPCell.ALIGN_LEFT));

            }
                gt.addCell(getCell("  ", PdfPCell.ALIGN_LEFT));
                gt.addCell(getCell("Dealer Name = " + Store.getInstance().dealerName, PdfPCell.ALIGN_LEFT));

            document.add(gt);



            PdfPTable thank1 = new PdfPTable(1);
            thank1.setWidthPercentage(100);
            thank1.addCell(getCell(" ", PdfPCell.ALIGN_CENTER));
            thank1.addCell(getCell("***Thank You***", PdfPCell.ALIGN_CENTER));
            document.add(new Paragraph("_____________________________________________________________________________"));
            document.add(thank1);



            // close document
            document.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (DocumentException e) {
            e.printStackTrace();
            return false;
        }
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
