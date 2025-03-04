package com.example.travel_app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.travel_app.Activity.CartActivity;
import com.example.travel_app.Activity.TicketDetailActivity;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ViewholderCartItemBinding;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private List<ItemDomain> items;
    private Context context;
    private CartActivity cartActivity;
    // **Thêm các biến thành viên để lưu dữ liệu từ CartActivity**
    private String selectedPackageName;
    private String selectedDate;
    private String passengerCountText;

    // **Sửa Constructor để nhận thêm dữ liệu**
    public CartAdapter(List<ItemDomain> cartItems, CartActivity cartActivity, String selectedPackageName, String selectedDate, String passengerCountText) {
        this.items = cartItems;
        this.cartActivity = cartActivity;
        this.selectedPackageName = selectedPackageName; // Lưu tên gói dịch vụ
        this.selectedDate = selectedDate; // Lưu thời gian
        this.passengerCountText = passengerCountText; // Lưu số lượng khách
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        ViewholderCartItemBinding binding = ViewholderCartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ItemDomain item = items.get(position);
        holder.binding.itemTitle.setText(item.getTitle());

        double price = item.getPrice();
        NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = currencyFormatVN.format(price);
        formattedPrice = formattedPrice.replace("₫", "").trim();
        holder.binding.itemPrice.setText("Giá: " + formattedPrice + "₫");
//        holder.binding.itemQuantity.setText(String.valueOf(item.getQuantity()));

//         Set text cho các TextView mới
        holder.binding.itemPackage.setText("Gói dịch vụ: " + selectedPackageName);
        holder.binding.itemTime.setText("Thời gian: " + selectedDate);
        holder.binding.itemPassengers.setText("Khách: " + passengerCountText);
        // holder.binding.itemName.setText("Người đặt: Nguyễn Văn A"); // Placeholder

        Glide.with(context).load(item.getPic()).into(holder.binding.itemImage);

        // **Set OnClickListener cho CardView item**
        holder.binding.detailTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TicketDetailActivity.class); // Intent đến TicketDetailActivity
                intent.putExtra("object", item); // Truyền object ItemDomain qua Intent

                context.startActivity(intent); // Khởi chạy TicketDetailActivity
            }
        });


        // ... (listeners for quantity buttons - giữ nguyên) ...
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ViewholderCartItemBinding binding;
        TextView itemPackage, itemTime, itemPassengers, itemName;

        public ViewHolder(@NonNull ViewholderCartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}