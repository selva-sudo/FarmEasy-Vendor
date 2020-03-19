package com.selvaraj.vendorapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.selvaraj.vendorapp.base.BaseApplication;

public class Utilities {
    /**
     * Method to check network state at the time of api call.
     *
     * @return the state in boolean.
     */
    public static boolean checkNetworkConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) BaseApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}