/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2;

/**
 * Created by shri on 23/8/17.
 */

import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class ReadFirstPdf
{
    static String mPath = Environment.getExternalStorageDirectory().toString() + "/" + "mynew" + ".pdf";
    private static String INPUTFILE = mPath;
    private static String OUTPUTFILE = mPath;

    public static void main(String[] args) throws DocumentException,
            IOException {
        Document document = new Document();

        PdfWriter writer = PdfWriter.getInstance(document,
                new FileOutputStream(OUTPUTFILE));
        document.open();
        PdfReader reader = new PdfReader(INPUTFILE);
        int n = reader.getNumberOfPages();
        PdfImportedPage page;
        // Go through all pages
        for (int i = 1; i <= n; i++) {
            // only page number 2 will be included
            if (i == 2) {
                page = writer.getImportedPage(reader, i);
                Image instance = Image.getInstance(page);
                document.add(instance);
            }
        }
        document.close();

    }

}