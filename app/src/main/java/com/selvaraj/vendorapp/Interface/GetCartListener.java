package com.selvaraj.vendorapp.Interface;

import com.selvaraj.vendorapp.model.Cart;

import java.util.Set;

public interface GetCartListener {
    void onSuccess(Set<Cart> cartList);
}
