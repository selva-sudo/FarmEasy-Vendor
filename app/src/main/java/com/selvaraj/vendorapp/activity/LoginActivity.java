package com.selvaraj.vendorapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.selvaraj.vendorapp.Interface.LoginListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.base.BaseActivity;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.utils.Utilities;

public class LoginActivity extends BaseActivity implements LoginListener {
    private Button btnLogin;
    private EditText etEmail, etPassword;
    private TextView tvSignUp;
    private ConstraintLayout constraintLayout;
    private String email, password;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etEmail = findViewById(R.id.et_login_email);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login);
        tvSignUp = findViewById(R.id.tv_sign_up_link);
        progressBar = findViewById(R.id.progress_bar_login);
        constraintLayout = findViewById(R.id.constraint_layout_login);
        progressBar.setVisibility(View.INVISIBLE);
        FirebaseApp.initializeApp(this);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = etEmail.getText().toString();
                password = etPassword.getText().toString().trim();
                hideKeyboard(LoginActivity.this);
                if (validateInputs(email, password)) {
                    progressBar.setVisibility(View.VISIBLE);
                    checkLogin();
                }
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkLogin() {
        if (!Utilities.checkNetworkConnection()) {
            showNoNetworkToast();
            progressBar.setVisibility(View.INVISIBLE);
            return;
        }
        BaseApplication.getInstance().getFireBaseUtils().checkLogin(this, email, password, this);
    }

    private boolean validateInputs(String email, String password) {
        if (email.isEmpty()) {
            etEmail.setError(getString(R.string.error_enter_email_id));
            return false;
        }
        if (password.isEmpty() || password.length() < 6) {
            etPassword.setError(getString(R.string.error_enter_password));
            return false;
        }
        return true;
    }

    @Override
    public void onLoginSuccess(boolean result) {
        if (result) {
            BaseApplication.getInstance().getPreferenceManager().saveLogin(email, password);
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
        } else {
            Snackbar.make(constraintLayout, getString(R.string.input_error), Snackbar.LENGTH_LONG).show();
        }
        progressBar.setVisibility(View.INVISIBLE);
    }
}
