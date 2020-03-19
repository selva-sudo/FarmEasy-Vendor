package com.selvaraj.vendorapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.selvaraj.vendorapp.Interface.GetUserNameListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.base.BaseActivity;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.fragment.AddProductFragment;
import com.selvaraj.vendorapp.fragment.CartFragment;
import com.selvaraj.vendorapp.fragment.ChatFragment;
import com.selvaraj.vendorapp.fragment.HomeFragment;
import com.selvaraj.vendorapp.fragment.ProfileFragment;

public class HomeActivity extends BaseActivity implements GetUserNameListener {

    HomeFragment homeFragment = HomeFragment.newInstance();
    private TextView tvUserName;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    loadFragment(homeFragment, getString(R.string.home_tab));
                    return true;
                case R.id.navigation_add:
                    loadFragment(AddProductFragment.newInstance(), getString(R.string.add_product_tab));
                    return true;
                case R.id.navigation_chat:
                    loadFragment(ChatFragment.newInstance(), getString(R.string.chat_tab));
                    return true;
                case R.id.navigation_cart:
                    loadFragment(CartFragment.newInstance(), getString(R.string.cart_tab));
                    return true;
                case R.id.navigation_profile:
                    loadFragment(ProfileFragment.newInstance(), getString(R.string.profile_tab));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BaseApplication.getInstance().getFireBaseUtils().getUserList();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        tvUserName = findViewById(R.id.tv_user_name);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BaseApplication.getInstance().getFireBaseUtils().getAuthUserName(this);
        loadFragment(homeFragment, "profile");
    }

    public void loadFragment(Fragment fragment, String profile) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_home, fragment)
                    .addToBackStack(profile)
                    .commit();
        }
    }

    @Override
    public void onSuccess(String name) {
        tvUserName.setText(name);
    }

    public void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        BaseApplication.getInstance().getPreferenceManager().clearPreference();
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProfileFragment fragment = ProfileFragment.newInstance();
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.frame_home);
        if (currentFragment == fragment) {
            loadFragment(fragment, "profile");
        }
    }

    public void hideKeyboard() {
        hideKeyboard(this);
    }

    public void changeLanguageAlert() {

    }
}
