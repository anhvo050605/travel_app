package com.example.travel_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.Category;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ViewholderCategoryBinding;

import java.util.List;
import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.Viewholder> {
    private List<Category> items; // Sử dụng List thay vì ArrayList để tương thích với updateData
    private int selectedPosition=-1;
    private int lastSelectedPosition=-1;
    private Context context;

    public CategoryAdapter(List<Category> items) { // Sử dụng List trong constructor
        this.items = items;
    }

    // **Hàm updateData mới**
    public void updateData(List<Category> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CategoryAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        ViewholderCategoryBinding binding = ViewholderCategoryBinding.inflate(inflater,parent,false);
        return new Viewholder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.Viewholder holder, int position) {
        Category item = items.get(position);
        holder.binding.title.setText(item.getName());

        Glide.with(holder.itemView.getContext()).load(item.getImagePath()).into(holder.binding.pic);

        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastSelectedPosition = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(lastSelectedPosition);
                notifyItemChanged(selectedPosition);
            }
        });
        holder.binding.title.setTextColor(context.getResources().getColor(R.color.white));

        if(selectedPosition == position){
            holder.binding.pic.setBackgroundResource(0);
            holder.binding.mainLayout.setBackgroundResource(R.drawable.blue_bg);
            holder.binding.title.setVisibility(View.VISIBLE);
        }else {
            holder.binding.pic.setBackgroundResource(R.drawable.grey_bg);
            holder.binding.mainLayout.setBackgroundResource(0);
            holder.binding.title.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        private final ViewholderCategoryBinding binding;
        public Viewholder(ViewholderCategoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }
    }
}