/*
 * Created By Shri Hari
 *
 * Copyright (c) 2018.All Rights Reserved
 */

package com.bizsoft.fmcgv2.dataobject;

import java.io.File;

/**
 * Created by GopiKing on 31-08-2017.
 */

public class BizFile {
    String fileName;
    double fileSize;
    String date;
    File details;

    public File getDetails() {
        return details;
    }

    public void setDetails(File details) {
        this.details = details;
    }

    public String getFileName() {
        return fileName;

    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public double getFileSize() {
        return fileSize;
    }

    public void setFileSize(double fileSize) {
        this.fileSize = fileSize;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
