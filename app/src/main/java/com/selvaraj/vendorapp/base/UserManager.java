package com.selvaraj.vendorapp.base;

import android.location.Location;

import com.selvaraj.vendorapp.model.AddProduct;
import com.selvaraj.vendorapp.model.SaveVendor;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private SaveVendor user;
    private SaveVendor authUser;
    private List<String> userList = new ArrayList<>();
    private List<AddProduct> productList = new ArrayList<>();
    private String userEmail, userPassword;
    private String lastKnownLocation;
    private Location userLocation;

    public String getLastKnownLocation() {
        return lastKnownLocation;
    }

    public void setLastKnownLocation(String lastKnownLocation) {
        this.lastKnownLocation = lastKnownLocation;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    public SaveVendor getUser() {
        return user;
    }

    public void setUser(SaveVendor user) {
        this.user = user;
    }

    public List<String> getUserList() {
        return userList;
    }

    public void setUserList(List<String> userList) {
        this.userList = userList;
    }

    public SaveVendor getAuthUser() {
        return authUser;
    }

    public void setAuthUser(SaveVendor authUser) {
        this.authUser = authUser;
    }

    public List<AddProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<AddProduct> productList) {
        this.productList = productList;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
}
