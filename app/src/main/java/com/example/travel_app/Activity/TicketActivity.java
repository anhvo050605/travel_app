package com.example.travel_app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import android.content.SharedPreferences;
import android.content.Context;
import android.widget.TextView;

import com.example.travel_app.databinding.ActivityTicketBinding;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class TicketActivity extends BaseActivity {
    ActivityTicketBinding binding;
    private ItemDomain object;
    private int adultCount, childCount;
    private String selectedPackageName;
    private double totalPriceValue;
    private String selectedDate;
    private String userPhoneNumber;
    private String userFirstName; // Variable to store user's first name (ho)
    private String userLastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        userPhoneNumber = sharedPreferences.getString("sdt", "");
        userFirstName = sharedPreferences.getString("ho", ""); // Retrieve user's first name (ho)
        userLastName = sharedPreferences.getString("ten", "");


        getIntentExtra(); // Get data from Intent
        setVariable();   // Populate views with data




    }


    private void getIntentExtra() {
        if (getIntent().getExtras() != null) {
            object = (ItemDomain) getIntent().getSerializableExtra("object");
            adultCount = getIntent().getIntExtra("adultCount", 1); // Lấy số lượng người lớn, mặc định là 1
            childCount = getIntent().getIntExtra("childCount", 0); // Lấy số lượng trẻ em, mặc định là 0
            selectedPackageName = getIntent().getStringExtra("selectedPackage"); // Lấy tên gói dịch vụ
            selectedDate = getIntent().getStringExtra("selectedDate"); // Lấy ngày đã chọn
            totalPriceValue = getIntent().getDoubleExtra("totalPriceValue", 0); // Lấy tổng giá vé

        }
    }

    private void setVariable() {
        if (object != null) {
            Glide.with(TicketActivity.this).load(object.getPic()).into(binding.pic);
            Glide.with(TicketActivity.this).load(object.getTourGuidePic()).into(binding.profile);

            binding.titleTxt.setText(object.getTitle());
            binding.durationTxt.setText(object.getDuration());
            binding.tourGuideTxt.setText(selectedPackageName); // **Hiển thị Gói dịch vụ**
            binding.timeTxt.setText(selectedDate); // **Hiển thị Thời gian (Ngày đi)**
            binding.tourGuideNameTxt.setText(userFirstName + " " + userLastName);
            binding.guestTxt.setText(adultCount + " Người lớn, " + childCount + " Trẻ em"); // **Hiển thị Số lượng khách**

            String orderId = generateRandomOrderId();
            binding.textView12.setText("Order Id:" + orderId);

            NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedPrice = currencyFormatVN.format(totalPriceValue);


        }
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent intent = new Intent(TicketActivity.this,MainActivity.class);
                // startActivity(intent);
                // **Thêm item vào giỏ hàng mới (vẫn giữ nguyên) - THỰC HIỆN TRƯỚC KHI CHUYỂN TRANG**
                CartActivity.addItemToNewCart(object); // Thêm item vào cartItemList STATIC

                // **Sửa Intent: Chuyển đến CartActivity và TRUYỀN DỮ LIỆU - THỰC HIỆN SAU KHI ĐÃ THÊM ITEM VÀO CART**
                Intent intentToCart = new Intent(TicketActivity.this, CartActivity.class);
                intentToCart.putExtra("cart_item_list", (ArrayList<ItemDomain>) CartActivity.cartItemList); // **ĐẢM BẢO TRUYỀN cartItemList**
                intentToCart.putExtra("selectedPackage", selectedPackageName); // Truyền tên gói dịch vụ
                intentToCart.putExtra("selectedDate", selectedDate); // Truyền ngày đã chọn
                intentToCart.putExtra("adultCount", adultCount); // Truyền số lượng người lớn
                intentToCart.putExtra("childCount", childCount); // Truyền số lượng trẻ em

                startActivity(intentToCart);
                finish(); // Đóng TicketActivity sau khi chuyển sang giỏ hàng (tùy chọn)
            }
        });

        binding.BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setData(Uri.parse("sms:"+userPhoneNumber));
                sendIntent.putExtra("sms_body","type your message");
                startActivity(sendIntent);
            }
        });
        binding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = userPhoneNumber;;
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",phone,null));
                startActivity(intent);
            }
        });
    }

    private String generateRandomOrderId() {
        int randomNumber = new Random().nextInt(1000000); // Tạo số ngẫu nhiên từ 0 đến 999999
        return String.format("%06d", randomNumber);
    }
}