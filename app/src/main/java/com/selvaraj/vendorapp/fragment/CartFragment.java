package com.selvaraj.vendorapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.selvaraj.vendorapp.Interface.GetCartListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.adapter.CartAdapter;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.base.BaseFragment;
import com.selvaraj.vendorapp.model.Cart;

import java.util.Set;


public class CartFragment extends BaseFragment implements GetCartListener {

    private RecyclerView rvCart;
    private TextView tvNoCartItems;
    private CartAdapter cartAdapter;
    private Set<Cart> cartList = new ArraySet<>();

    public CartFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance() {
        return new CartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvCart = view.findViewById(R.id.rv_cart);
        tvNoCartItems = view.findViewById(R.id.tv_no_cart_items);
        tvNoCartItems.setVisibility(View.VISIBLE);
        initRecyclerView();
        getCartDetails();
    }

    private void getCartDetails() {
        BaseApplication.getInstance().getFireBaseUtils().getCartList(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvCart.setLayoutManager(linearLayoutManager);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvCart.addItemDecoration(itemDecoration);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        rvCart.setItemAnimator(itemAnimator);
        cartAdapter = new CartAdapter(cartList);
        rvCart.setAdapter(cartAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onSuccess(Set<Cart> cartList) {
        if (cartList.size() != 0) {
            tvNoCartItems.setVisibility(View.INVISIBLE);
            this.cartList = cartList;
            cartAdapter.updateDetails(cartList);
        } else {
            tvNoCartItems.setVisibility(View.VISIBLE);
        }
    }
}
