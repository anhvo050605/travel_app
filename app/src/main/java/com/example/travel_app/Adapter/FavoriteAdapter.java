package com.example.travel_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ViewholderFavoriteItemBinding;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private List<ItemDomain> favoriteItems;
    private Context context;

    public FavoriteAdapter(List<ItemDomain> favoriteItems) {
        this.favoriteItems = favoriteItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderFavoriteItemBinding binding = ViewholderFavoriteItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDomain item = favoriteItems.get(position);

        holder.binding.favoriteItemTitle.setText(item.getTitle());

        Glide.with(context)
                .load(item.getPic())
                .into(holder.binding.favoriteItemImage);
    }

    @Override
    public int getItemCount() {
        return favoriteItems.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderFavoriteItemBinding binding;
        public ViewHolder(ViewholderFavoriteItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}