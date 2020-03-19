package com.selvaraj.vendorapp.base;

import android.app.Application;

import com.selvaraj.vendorapp.utils.FireBaseUtils;
import com.selvaraj.vendorapp.utils.PreferenceUtils;

public class BaseApplication extends Application {

    private static BaseApplication instance;
    private UserManager userManager;
    private FireBaseUtils fireBaseUtils;
    private PreferenceUtils preferenceManager;

    public static BaseApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        userManager = new UserManager();
        instance = this;
        fireBaseUtils = new FireBaseUtils();
        preferenceManager = new PreferenceUtils();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public FireBaseUtils getFireBaseUtils() {
        return fireBaseUtils;
    }

    public PreferenceUtils getPreferenceManager() {
        return preferenceManager;
    }
}
