/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_WORLD_READABLE;

/**
 * Created by GopiKing on 14-03-2018.
 */

public class BizLogger implements Thread.UncaughtExceptionHandler {
    private final static String TAG = BizLogger.class.getSimpleName();
    private final static String ERROR_FILE = BizLogger.class.getSimpleName() + ".error";

    private final Context context;
    private final Thread.UncaughtExceptionHandler rootHandler;

    public BizLogger(Context context) {
        this.context = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        rootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {

        String fullClassName = Thread.currentThread().getStackTrace()[2].getClassName();
        String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
        String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
        int lineNumber = Thread.currentThread().getStackTrace()[2].getLineNumber();
        Log.d(className + "." + methodName + "():" + lineNumber, String.valueOf(ex));
        System.out.println("Exception ----------------"+ex);
        generateNoteOnSD(ex.toString());

    }
    public static void generateNoteOnSD(String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "FMCGLogs");
            if (!root.exists()) {
                root.mkdirs();
            }

            File gpxfile = new File(root, "fmcglog");
            if(!gpxfile.exists())
            {
                gpxfile.createNewFile();
            }
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}