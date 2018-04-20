package com.bizsoft.fmcgv2.dataobject;

import com.bizsoft.fmcgv2.R;

/**
 * Created by GopiKing on 18-04-2018.
 */
public class MenuItems {

    String name;
    int imageResId;

    public MenuItems(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public void init()
    {
        MenuItems home = new MenuItems("Home", R.drawable.ic_home_black_24dp);


    }
}


