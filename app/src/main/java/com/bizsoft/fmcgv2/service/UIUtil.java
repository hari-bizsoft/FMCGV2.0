package com.bizsoft.fmcgv2.service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizsoft.fmcgv2.DashboardActivity;
import com.bizsoft.fmcgv2.DealerActivity;
import com.bizsoft.fmcgv2.R;
import com.bizsoft.fmcgv2.dataobject.Store;

/**
 * Created by GopiKing on 18-04-2018.
 */
public class UIUtil {


    public static void setActionBarDesign(final Context context, ActionBar actionBar) {
        try {

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.custom_action_bar);
            View customActionBar = actionBar.getCustomView();
            Toolbar toolbar = (Toolbar) customActionBar.getParent();
            toolbar.setContentInsetsAbsolute(0, 0);


            ImageButton imageButton = (ImageButton) customActionBar.findViewById(R.id.menu);
            ImageView logo = (ImageView) customActionBar.findViewById(R.id.dealer_logo);
            TextView dealerName = (TextView) customActionBar.findViewById(R.id.dealer_name);


            dealerName.setText(String.valueOf(Store.getInstance().dealer.getCompanyName()));




            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    BizUtils bizUtils = new BizUtils();
                    bizUtils.bizMenu(context);

                }
            });


            Bitmap bmp = BizUtils.StringToBitMap(Store.getInstance().companyLogo);

            if (bmp == null) {

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    logo.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.user));
                } else {
                    logo.setBackground(ContextCompat.getDrawable(context, R.drawable.user));
                }
            } else {
                logo.setImageBitmap(bmp);
            }


        } catch (Exception e) {

            System.out.println("Company Hearder Exception = "+e);
        }
    }
}
