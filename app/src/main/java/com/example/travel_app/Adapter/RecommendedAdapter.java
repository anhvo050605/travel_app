package com.example.travel_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.Activity.DeltaiActivity;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.databinding.ViewholderRecommendedBinding;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import java.util.ArrayList;

public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.Viewhoder>{
    List<ItemDomain> items; // Sử dụng List thay vì ArrayList để tương thích với updateData
    Context context;
    ViewholderRecommendedBinding binding;

    public RecommendedAdapter(List<ItemDomain> items) { // Sử dụng List trong constructor
        this.items = items;
    }

    // **Hàm updateData mới**
    public void updateData(List<ItemDomain> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecommendedAdapter.Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ViewholderRecommendedBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new Viewhoder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedAdapter.Viewhoder holder, int position) {
        double price = items.get(position).getPrice();
        binding.titleTxt.setText(items.get(position).getTitle());
        NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormatVN.format(price);
        formattedPrice = formattedPrice.replace("₫", "").trim();
        binding.priceTxt.setText(formattedPrice + "₫");
        binding.addressTxt.setText(items.get(position).getAddress());
        binding.scoreTxt.setText("" + items.get(position).getScore());

        Glide.with(context).load(items.get(position).getPic()).into(binding.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, DeltaiActivity.class);
                intent.putExtra("object",items.get(position));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewhoder extends RecyclerView.ViewHolder {
        public Viewhoder(ViewholderRecommendedBinding binding) {
            super(binding.getRoot());
        }
    }
}