package com.selvaraj.vendorapp.base;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.selvaraj.vendorapp.Interface.CTNetworkChangeListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.utils.CTNetworkChangeReceiver;

public class BaseActivity extends AppCompatActivity implements CTNetworkChangeListener {
    private IntentFilter intentFilter = new IntentFilter();
    private CTNetworkChangeReceiver networkChangeReceiver;

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.setPriority(100);
        networkChangeReceiver = new CTNetworkChangeReceiver(this);
    }

    /**
     * Method to set home menu in action bar.
     *
     * @param shouldSet contains boolean value.
     */
    public void setHomeInSupportActionBar(boolean shouldSet) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(shouldSet); // Set common home menu.
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(networkChangeReceiver);
    }

    @Override
    public void onNetworkChange(String networkState) {
        showToast(networkState);
    }

    /**
     * This method will show no network toast
     * It get called when  calls onNoNetwork callback.
     */
    public void showNoNetworkToast() {
        Toast.makeText(this, getString(R.string.no_network_toast), Toast.LENGTH_LONG).show();
    }

    /**
     * Method called from any activity that extents this.
     * To show the toast message.
     *
     * @param msg contains message that should displayed on toast.
     */
    public void showToast(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

}
