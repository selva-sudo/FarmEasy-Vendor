package com.selvaraj.vendorapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selvaraj.vendorapp.Interface.EditListener;
import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.model.AddProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.ViewHolder> {
    private Set<AddProduct> productList;
    private EditListener listener;


    public AddItemAdapter(Set<AddProduct> productList, EditListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_add, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        List<AddProduct> products = new ArrayList<>(productList);
        final AddProduct product = products.get(i);
        viewHolder.tvAddProductName.setText(product.getName());
        viewHolder.tvAddProductQuantity.setText(product.getQuantity());
        viewHolder.tvAddProductPrice.setText(product.getPrice());
        viewHolder.tvAddProductDate.setText(product.getDate());
        viewHolder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEdit(product);
            }
        });
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
        private TextView tvAddProductName, tvAddProductQuantity, tvAddProductPrice, tvAddProductDate;
        private Button btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAddProductName = itemView.findViewById(R.id.tv_add_product_name);
            tvAddProductQuantity = itemView.findViewById(R.id.tv_add_quantity);
            tvAddProductPrice = itemView.findViewById(R.id.tv_add_item_price);
            tvAddProductDate = itemView.findViewById(R.id.tv_add_item_date);
            btnEdit = itemView.findViewById(R.id.btn_edit_entry);
        }
    }
}
