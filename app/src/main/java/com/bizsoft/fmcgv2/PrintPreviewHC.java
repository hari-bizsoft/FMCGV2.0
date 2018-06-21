/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.bizsoft.fmcgv2.service.PrinterService;

public class PrintPreviewHC extends AppCompatActivity {

    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_preview_hc);

        text = (TextView) findViewById(R.id.printer_area);

        text.setText(String.valueOf(PrinterService.printPreview(PrintPreviewHC.this)));

    }
}
