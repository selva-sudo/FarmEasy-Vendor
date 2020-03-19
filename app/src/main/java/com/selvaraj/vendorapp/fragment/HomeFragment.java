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

import com.selvaraj.vendorapp.Interface.GetProductListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.adapter.HomeAdapter;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.base.BaseFragment;
import com.selvaraj.vendorapp.model.AddProduct;

import java.util.List;
import java.util.Set;


public class HomeFragment extends BaseFragment implements GetProductListener {
    private RecyclerView rvHome;
    private HomeAdapter homeAdapter;
    private TextView tvNoList;
    private Set<AddProduct> productList = new ArraySet<>();

    public HomeFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvHome = view.findViewById(R.id.rv_home);
        tvNoList = view.findViewById(R.id.tv_no_product_list);
        initRecyclerView();
        getDetailsFromFireBase();
    }

    private void getDetailsFromFireBase() {
        BaseApplication.getInstance().getFireBaseUtils().getDetailsFromFireBase(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvHome.setLayoutManager(linearLayoutManager);
        rvHome.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvHome.addItemDecoration(itemDecoration);
        homeAdapter = new HomeAdapter(productList);
        rvHome.setAdapter(homeAdapter);
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
    public void onSuccess(List<AddProduct> productList) {
        if (productList != null) {
            homeAdapter.updateDetails(productList);
            tvNoList.setVisibility(View.INVISIBLE);
        } else {
            tvNoList.setVisibility(View.VISIBLE);
        }
    }
}
