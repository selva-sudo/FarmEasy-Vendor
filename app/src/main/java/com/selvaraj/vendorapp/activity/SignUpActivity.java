package com.selvaraj.vendorapp.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.selvaraj.vendorapp.BuildConfig;
import com.selvaraj.vendorapp.Interface.LoginListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.base.BaseActivity;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.model.SaveVendor;
import com.selvaraj.vendorapp.utils.LocationUtils;
import com.selvaraj.vendorapp.utils.Utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class SignUpActivity extends BaseActivity implements View.OnClickListener, LoginListener, LocationUtils.LocationCallBack {

    private static final String PACKAGE = "package:";
    private String lastKnownLocation;
    private Handler handler;
    private EditText etFirstName, etLastName, etEmail, etPhone, etPassword;
    private String firstName;
    private String lastName;
    private String password;
    private String email;
    private String phone;
    private ConstraintLayout constraintLayoutSignUp;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handler = new Handler();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationUtils locationUtils = new LocationUtils(fusedLocationProviderClient, this, this);
        locationUtils.getLastLocation();
        setContentView(R.layout.activity_signup);
        etFirstName = findViewById(R.id.et_first_name);
        etLastName = findViewById(R.id.et_last_name);
        etEmail = findViewById(R.id.et_sign_up_email);
        etPassword = findViewById(R.id.et_signup_password);
        etPhone = findViewById(R.id.et_phone_number);
        setHomeInSupportActionBar(true);
        requestPermission();
        Button btnSignUp = findViewById(R.id.btn_sign_up);
        constraintLayoutSignUp = findViewById(R.id.constraint_layout_sign_up);
        btnSignUp.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermission() {
        if ((ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED)) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        SignUpActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @NeedsPermission({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void getLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        LocationUtils locationUtils = new LocationUtils(fusedLocationProviderClient, this, this);
        locationUtils.getLastLocation();
    }

    /**
     * Once permission denied the show toast message corresponding to the permission.
     */
    @OnPermissionDenied({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onPermissionDenied() {
        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
    }

    /**
     * Method displays the need of permission and create request if allow button is clicked.
     *
     * @param request contains list of requests.
     */
    @OnShowRationale({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void showRationaleForCameraAndStorage(PermissionRequest request) {
        showRationaleDialog(R.string.permission_camera_rationale, request);
    }

    /**
     * Navigate to settings when user checks never ask again.
     */
    @OnNeverAskAgain({Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
    void onPermissionNeverAskAgain() {
        Snackbar.make(constraintLayoutSignUp, R.string.snack_bar_title, Snackbar.LENGTH_LONG).
                setAction(R.string.snack_bar_btn, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse(PACKAGE + BuildConfig.APPLICATION_ID));
                        startActivity(intent);
                    }
                }).show();
    }

    /**
     * Method to create alert dialog which contains the need of permission
     *
     * @param messageResId represents particular string resource to show message.
     * @param request      used to proceed or cancel based on user selection.
     */
    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@androidx.annotation.NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@androidx.annotation.NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_up:
                if (checkForValidInputs()) {
                    hideKeyboard(this);
                    saveFarmer();
                }
        }
    }

    private void saveFarmer() {
        if (!Utilities.checkNetworkConnection()) {
            showNoNetworkToast();
            return;
        }
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
        String strDate = dateFormat.format(date);
        Location location = BaseApplication.getInstance().getUserManager().getUserLocation();
        double lat,lng;
        lat=location.getLatitude();
        lng=location.getLongitude();
        final SaveVendor saveVendor = new SaveVendor(email, firstName, lastName, phone, BaseApplication.getInstance().getUserManager().getLastKnownLocation(), strDate,lat,lng);
        BaseApplication.getInstance().getFireBaseUtils().createAccount(saveVendor, this, password, this);
    }

    private boolean checkForValidInputs() {
        firstName = etFirstName.getText().toString().trim();
        lastName = etLastName.getText().toString().trim();
        email = etEmail.getText().toString().trim();
        phone = etPhone.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if (firstName.isEmpty() || firstName.length() > 20) {
            etFirstName.setError(getString(R.string.enter_valid_name));
            return false;
        }
        if (lastName.isEmpty() || lastName.length() > 20) {
            etLastName.setError(getString(R.string.enter_valid_name));
            return false;
        }
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.enter_valid_email));
            return false;
        }
        if (phone.isEmpty() || phone.length() != 10 || !(phone.matches("[0-9]+"))) {
            etPhone.setError(getString(R.string.enter_valid_phone));
            return false;
        }
        if (password.isEmpty() || password.length() < 6 || password.length() > 20) {
            etPassword.setError(getString(R.string.enter_valid_password));
            return false;
        }
        return true;
    }

    @Override
    public void onLoginSuccess(boolean result) {
        if (result) {
            Toast.makeText(getApplicationContext(), getString(R.string.account_creation_success), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Snackbar.make(constraintLayoutSignUp, getString(R.string.auth_fail), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onLocationReceived(final String location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        lastKnownLocation = location;
                        BaseApplication.getInstance().getUserManager().setLastKnownLocation(lastKnownLocation);
                    }
                });
            }
        }).start();
    }
}
