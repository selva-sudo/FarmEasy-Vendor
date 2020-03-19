package com.selvaraj.vendorapp.model;

import android.location.Location;

public class SaveVendor {

    private String email;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String address;

    private String dateOfJoining;

    private double lat,lng;

    public SaveVendor() {
    }

    public SaveVendor(String email, String firstName, String lastName, String phoneNumber, String address, String dateOfJoining, double lat, double lng) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfJoining = dateOfJoining;
        this.lat = lat;
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getDateOfJoining() {
        return dateOfJoining;
    }
}
