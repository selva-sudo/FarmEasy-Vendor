package com.selvaraj.vendorapp.fragment;

import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.ArraySet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.selvaraj.vendorapp.Interface.EditListener;
import com.selvaraj.vendorapp.Interface.GetProductListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.adapter.AddItemAdapter;
import com.selvaraj.vendorapp.base.BaseApplication;
import com.selvaraj.vendorapp.base.BaseFragment;
import com.selvaraj.vendorapp.model.AddProduct;
import com.selvaraj.vendorapp.model.SaveVendor;
import com.selvaraj.vendorapp.utils.FireBaseUtils;
import com.selvaraj.vendorapp.utils.LocationUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Set;


public class AddProductFragment extends BaseFragment implements View.OnClickListener, EditListener, GetProductListener, LocationUtils.LocationCallBack  {

    public String lastKnownLocation;
    private RecyclerView rvAddItem;
    private Handler handler;
    private ConstraintLayout constraintLayout;
    private TextView tv_no_add_items;
    private Set<AddProduct> productList = new ArraySet<>();
    private AddItemAdapter addItemAdapter;

    public AddProductFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static AddProductFragment newInstance() {
        return new AddProductFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        handler = new Handler();
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        LocationUtils locationUtils = new LocationUtils(fusedLocationProviderClient, context, this);
        locationUtils.getLastLocation();
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button fabAdd = view.findViewById(R.id.floatingActionButton_add);
        rvAddItem = view.findViewById(R.id.rv_add_product);
        tv_no_add_items = view.findViewById(R.id.tv_no_add_items);
        constraintLayout = view.findViewById(R.id.cl_add);
        fabAdd.setOnClickListener(this);
        initRecyclerView();
        BaseApplication.getInstance().getFireBaseUtils().getDetailsFromFireBase(this);
    }

    private void initRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        rvAddItem.setLayoutManager(linearLayoutManager);
        rvAddItem.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(context, DividerItemDecoration.VERTICAL);
        rvAddItem.addItemDecoration(itemDecoration);
        addItemAdapter = new AddItemAdapter(productList, this);
        rvAddItem.setAdapter(addItemAdapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.floatingActionButton_add:
                showAlert();
        }
    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialog);
        View view = getLayoutInflater().inflate(R.layout.add_new_item_alert, null);
        final EditText etProductName = view.findViewById(R.id.et_product_name);
        final EditText etProductQuantity = view.findViewById(R.id.et_product_quantity);
        final EditText etProductPrice = view.findViewById(R.id.et_product_price);
        builder.setIcon(R.drawable.newgardeners);
        builder.setView(view);
        builder.setTitle(getString(R.string.add_product_title));
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name, price, quantity;
                name = etProductName.getText().toString();
                price = etProductPrice.getText().toString();
                quantity = etProductQuantity.getText().toString();
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
                String strDate = dateFormat.format(date);
                SaveVendor user = BaseApplication.getInstance().getUserManager().getAuthUser();
                String vendorName = user.getFirstName() + " " + user.getLastName();
                double lat,lng;
                lat = user.getLat();
                lng=user.getLng();
                AddProduct addProduct = new AddProduct(name, vendorName, quantity, price, lastKnownLocation, 0, strDate,lat,lng);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users" + "/" + new FireBaseUtils().userID + "/" + "Products" + "/" + name);
                reference.setValue(addProduct);
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onEdit(AddProduct product) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.MyDialog);
        View view = getLayoutInflater().inflate(R.layout.add_new_item_alert, null);
        final EditText etProductName = view.findViewById(R.id.et_product_name);
        final EditText etProductQuantity = view.findViewById(R.id.et_product_quantity);
        final EditText etProductPrice = view.findViewById(R.id.et_product_price);
        builder.setIcon(R.drawable.newgardeners);
        builder.setView(view);
        builder.setTitle(getString(R.string.add_product));
        etProductName.setText(product.getName());
        etProductQuantity.setText(product.getQuantity());
        etProductPrice.setText(product.getPrice());
        builder.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name, price, quantity;
                name = etProductName.getText().toString();
                price = etProductPrice.getText().toString();
                quantity = etProductQuantity.getText().toString();
                Date date = new Date();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss", Locale.getDefault());
                String strDate = dateFormat.format(date);
                SaveVendor user = BaseApplication.getInstance().getUserManager().getAuthUser();
                String vendorName = user.getFirstName() + " " + user.getLastName();
                double lat,lng;
                lat = user.getLat();
                lng=user.getLng();
                String location = BaseApplication.getInstance().getUserManager().getLastKnownLocation();
                AddProduct addProduct = new AddProduct(name, vendorName, quantity, price, location, 0, strDate,lat,lng);
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users" + "/" + new FireBaseUtils().userID + "/" + "Products" + "/" + name);
                reference.setValue(addProduct);
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    @Override
    public void onSuccess(List<AddProduct> productList) {
        this.productList.clear();
        if (productList != null) {
            tv_no_add_items.setVisibility(View.INVISIBLE);
            addItemAdapter.updateDetails(productList);
        } else {
            tv_no_add_items.setVisibility(View.VISIBLE);
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
