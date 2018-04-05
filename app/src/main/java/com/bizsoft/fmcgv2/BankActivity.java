package com.bizsoft.fmcgv2;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.bizsoft.fmcgv2.Tables.BankList;
import com.bizsoft.fmcgv2.adapter.BankAdapter;
import com.bizsoft.fmcgv2.dataobject.Customer;
import com.bizsoft.fmcgv2.dataobject.Store;
import com.bizsoft.fmcgv2.service.BizUtils;

import java.util.ArrayList;

public class BankActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton add,menu;
    BizUtils bizUtils;
    private EditText searchBar;
    private ArrayList<BankList> bankList;
    private BankAdapter adapter;
    private ArrayList<BankList> filterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank);
        getSupportActionBar().setTitle("Banks");
        searchBar = (EditText) findViewById(R.id.search_bar);
        listView = (ListView) findViewById(R.id.listview);
        add = (FloatingActionButton) findViewById(R.id.add);
        menu = (FloatingActionButton) findViewById(R.id.menu);

        bizUtils = new BizUtils();

        add.bringToFront();
        menu.bringToFront();

        filterList.addAll(Store.getInstance().bankList);
        adapter = new BankAdapter(BankActivity.this, filterList);
        listView.setAdapter(adapter);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(BankActivity.this,CreateBankActivity.class);
                startActivity(intent);
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                bizUtils.showMenu(BankActivity.this);
            }
        });
        enableSearch();

    }

    private void enableSearch() {
        searchBar.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                bankList = new ArrayList<BankList>();


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                System.out.println("Char Sequence = "+s);
                bankList.clear();
                if(TextUtils.isEmpty(s) | s.equals("") | s==null)
                {
                    System.out.println("Adding all the customers");

                    bankList.addAll(Store.getInstance().bankList);
                }
                else {

                    for (int i = 0; i < Store.getInstance().bankList.size(); i++) {

                        if (Store.getInstance().bankList.get(i).getLedger().getLedgerName().toLowerCase().contains(s.toString().toLowerCase())) {
                            bankList.add(Store.getInstance().bankList.get(i));
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                System.out.println("Adding bank list size"+bankList.size());

                System.out.println("Adding bank list size"+adapter.bankLists.size());
                adapter.bankLists.clear();
                adapter.bankLists.addAll(bankList);
                System.out.println("Adding customer list size"+adapter.bankLists.size());

                adapter.notifyDataSetChanged();
            }
        });
    }
}