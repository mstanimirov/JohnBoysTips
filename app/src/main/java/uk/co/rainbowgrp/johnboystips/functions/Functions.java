package uk.co.rainbowgrp.johnboystips.functions;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Martin on 9/1/2016.
 */
public class Functions {

    Context mContext;

    public Functions(Context context) {

        this.mContext = context;

    }

    public static void disableSroll(View v, int ScrollView) {

        android.widget.ScrollView sv = (android.widget.ScrollView) v.findViewById(ScrollView);

        sv.setVerticalScrollBarEnabled(false);
        sv.setHorizontalScrollBarEnabled(false);

    }

    public static int getIndex(ArrayAdapter<CharSequence> spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItem(i).equals(myString)) {
                index = i;
            }
        }
        return index;

    }

    public static void requestFocus(EditText id) {

        id.clearFocus();
        id.requestFocus();

    }

    public static void requestFocus2(EditText id) {

        id.setFocusable(true);
        id.setFocusableInTouchMode(true);
        id.clearFocus();
        id.requestFocus();
        id.setFocusable(false);
        id.setFocusableInTouchMode(false);

    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showToast(String str) {

        Toast.makeText(mContext.getApplicationContext(), str, Toast.LENGTH_SHORT).show();

    }

}
