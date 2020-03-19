package com.selvaraj.vendorapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.selvaraj.vendorapp.R;
import com.selvaraj.vendorapp.model.Cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Set<Cart> cartList;

    public CartAdapter(Set<Cart> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_cart, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        List<Cart> carts = new ArrayList<>(cartList);
        Cart cart = carts.get(i);
        viewHolder.tvBuyerName.setText(cart.getBuyerName());
        viewHolder.tvProductQuantity.setText(cart.getProductQuantity());
        viewHolder.tvTotalPrice.setText(cart.getTotalPrice());
        viewHolder.tvProductName.setText(cart.getBuyerName());
        viewHolder.tvDateOfBuying.setText(cart.getDateOfBuying());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void updateDetails(Set<Cart> cartList) {
        this.cartList.clear();
        this.cartList.addAll(cartList);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvBuyerName, tvProductName, tvProductQuantity, tvTotalPrice, tvDateOfBuying;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBuyerName = itemView.findViewById(R.id.tv_cart_buyer_name);
            tvProductName = itemView.findViewById(R.id.tv_product_cart_name);
            tvProductQuantity = itemView.findViewById(R.id.tv_product_cart_quantity);
            tvTotalPrice = itemView.findViewById(R.id.tv_product_cart_price);
            tvDateOfBuying = itemView.findViewById(R.id.tv_product_cart_date);
        }
    }
}
