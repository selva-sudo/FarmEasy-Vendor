package com.selvaraj.vendorapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.model.AddProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {
    private Set<AddProduct> productList;

    public HomeAdapter(Set<AddProduct> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_home, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        List<AddProduct> products = new ArrayList<>(productList);
        AddProduct product = products.get(i);
        viewHolder.tvAddProductName.setText(product.getName());
        viewHolder.tvAddProductQuantity.setText(product.getQuantity());
        viewHolder.tvAddProductPrice.setText(product.getPrice());
        viewHolder.tvHomeProductName.setText(product.getDate());
        viewHolder.ratingBar.setRating(product.getRating());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void updateDetails(List<AddProduct> products) {
        this.productList.clear();
        this.productList.addAll(products);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvAddProductName, tvAddProductQuantity, tvAddProductPrice, tvHomeProductName;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddProductName = itemView.findViewById(R.id.tv_home_product_name);
            tvAddProductQuantity = itemView.findViewById(R.id.tv_home_quantity);
            tvAddProductPrice = itemView.findViewById(R.id.tv_home_item_price);
            tvHomeProductName = itemView.findViewById(R.id.tv_home_item_date);
            ratingBar = itemView.findViewById(R.id.rating_bar_home_item);
        }
    }
}
