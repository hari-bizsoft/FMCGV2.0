package com.bizsoft.fmcgv2.dataobject;

import com.bizsoft.fmcgv2.BankActivity;
import com.bizsoft.fmcgv2.CustomerActivity;
import com.bizsoft.fmcgv2.DashboardActivity;
import com.bizsoft.fmcgv2.DealerActivity;
import com.bizsoft.fmcgv2.InvoiceListActivity;
import com.bizsoft.fmcgv2.ProductListActivity;
import com.bizsoft.fmcgv2.ProductSpecActivity;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.ReceiptActivity;
import com.bizsoft.fmcgv2.ReportActivity;
import com.bizsoft.fmcgv2.ReprintActivity;
import com.bizsoft.fmcgv2.STOSOActivity;
import com.bizsoft.fmcgv2.SalesActivity;
import com.bizsoft.fmcgv2.SalesOrderActivity;
import com.bizsoft.fmcgv2.SalesReturnActivity;
import com.bizsoft.fmcgv2.StockReportActivity;

import java.util.ArrayList;

/**
 * Created by GopiKing on 18-04-2018.
 */
public class MenuItems {

    String name;
    int imageResId;
    public  Object classToCall;

    public MenuItems() {

    }

    public Object getClassToCall() {
        return classToCall;
    }

    public void setClassToCall(Object classToCall) {
        this.classToCall = classToCall;
    }

    public MenuItems(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public static void init()
    {
        MenuItems sales = new MenuItems("Sales", R.drawable.sale);
        MenuItems salesOrder = new MenuItems("Sale Order", R.drawable.choices);
        MenuItems salesReturn = new MenuItems("Sale Return", R.drawable.sale_return);
        MenuItems sotosa = new MenuItems("SO to SA", R.drawable.sotos);
        MenuItems customer = new MenuItems("Customer", R.drawable.user);
        MenuItems receipt = new MenuItems("Receipt", R.drawable.receipt);
        MenuItems reports = new MenuItems("Reports", R.drawable.analytics);
        MenuItems invoice = new MenuItems("Invoice", R.drawable.invoice);
        MenuItems reprint = new MenuItems("Re-Print", R.drawable.printer);
        MenuItems sync = new MenuItems("Sync", R.drawable.ic_sync_black_24dp);
        MenuItems storage = new MenuItems("Load Storage", R.drawable.database);
        MenuItems bank = new MenuItems("Bank", R.drawable.bank);
        MenuItems product = new MenuItems("Products", R.drawable.product_bag);
        MenuItems dealer = new MenuItems("Dealer", R.drawable.handshake);
        MenuItems productSpec = new MenuItems("Product Spec", R.drawable.measuring);
        MenuItems stockReport = new MenuItems("Stock Report", R.drawable.stock);
        MenuItems home = new MenuItems("Home", R.drawable.ic_home_black_24dp);
        MenuItems logout = new MenuItems("Logout", R.drawable.logout);

        sales.setClassToCall(SalesActivity.class);
        salesOrder.setClassToCall(SalesOrderActivity.class);
        salesReturn.setClassToCall(SalesReturnActivity.class);
        sotosa.setClassToCall(STOSOActivity.class);
        customer.setClassToCall(CustomerActivity.class);
        receipt.setClassToCall(ReceiptActivity.class);
        reports.setClassToCall(ReportActivity.class);
        invoice.setClassToCall(InvoiceListActivity.class);
        reprint.setClassToCall(ReprintActivity.class);
        sync.setClassToCall(DashboardActivity.class);
        storage.setClassToCall(DashboardActivity.class);
        bank.setClassToCall(BankActivity.class);
        product.setClassToCall(ProductListActivity.class);
        dealer.setClassToCall(DealerActivity.class);
        productSpec.setClassToCall(ProductSpecActivity.class);
        stockReport.setClassToCall(StockReportActivity.class);
        home.setClassToCall(DashboardActivity.class);
        logout.setClassToCall(DashboardActivity.class);




        ArrayList<MenuItems> menuItems = new ArrayList<MenuItems>();
        menuItems.add(sales);
        menuItems.add(salesOrder);
        menuItems.add(salesReturn);
        menuItems.add(sotosa);
        menuItems.add(customer);
        menuItems.add(bank);
        menuItems.add(product);
        menuItems.add(productSpec);
        menuItems.add(dealer);
        menuItems.add(receipt);
        menuItems.add(reports);
        menuItems.add(invoice);
        menuItems.add(reprint);
        menuItems.add(stockReport);
        menuItems.add(storage);
        menuItems.add(sync);
        menuItems.add(home);
        menuItems.add(logout);

        Store.getInstance().menuItems = menuItems;


        Store.getInstance().menuNameList = getNames();





    }
    public static ArrayList<String> getNames()
    {
        ArrayList<String>  strings = new ArrayList<>();
        for(int i=0;i<Store.getInstance().menuItems.size();i++)
        {
            strings.add(Store.getInstance().menuItems.get(i).getName());
        }
        return strings;

    }
}


