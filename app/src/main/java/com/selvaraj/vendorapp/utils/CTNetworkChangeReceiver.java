package com.selvaraj.vendorapp.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.selvaraj.vendorapp.Interface.CTNetworkChangeListener;
import com.selvaraj.vendorapp.R;

/**
 * {@link BroadcastReceiver} class to check network state on activities.
 * The onReceive method this called when there is a connectivity change.
 */
public class CTNetworkChangeReceiver extends BroadcastReceiver {
    private CTNetworkChangeListener listener;

    public CTNetworkChangeReceiver(CTNetworkChangeListener listener) {
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object systemServiceObj = context.getSystemService(Context.CONNECTIVITY_SERVICE);
        ConnectivityManager connectivityManager = (ConnectivityManager) systemServiceObj;
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean networkIsAvailable = false;
        if (networkInfo != null) {
            if (networkInfo.isAvailable()) {
                networkIsAvailable = true;
            }
        }
        String networkMessage;
        if (networkIsAvailable) {
            networkMessage = context.getString(R.string.network_state_on);
        } else {
            networkMessage = context.getString(R.string.network_state_off);
        }
        listener.onNetworkChange(networkMessage);
    }
}
