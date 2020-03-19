package com.selvaraj.vendorapp.Interface;

import com.selvaraj.vendorapp.model.AddProduct;

import java.util.List;

public interface GetProductListener {
    void onSuccess(List<AddProduct> productList);
}
