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
import com.example.travel_app.databinding.ViewholderPopularBinding;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import java.util.ArrayList;

public class PopularAdapter extends RecyclerView.Adapter<PopularAdapter.Viewhoder>{
    List<ItemDomain> items; // Sử dụng List thay vì ArrayList để tương thích với updateData
    Context context;
    ViewholderPopularBinding binding;

    public PopularAdapter(ArrayList<ItemDomain> items) {
        this.items = (List<ItemDomain>) items; // Cast ArrayList sang List trong constructor
    }

    // **Hàm updateData mới**
    public void updateData(List<ItemDomain> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PopularAdapter.Viewhoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding=ViewholderPopularBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        context = parent.getContext();
        return new Viewhoder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularAdapter.Viewhoder holder, int position) {
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
        public Viewhoder(ViewholderPopularBinding binding) {
            super(binding.getRoot());
        }
    }
}