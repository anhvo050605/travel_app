package com.example.travel_app.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.databinding.ActivityTicketBinding; // **IMPORTANT: Use ActivityTicketBinding**

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

public class TicketDetailActivity extends BaseActivity { // **Đổi tên class thành TicketDetailActivity**
    ActivityTicketBinding binding; // **Use ActivityTicketBinding**
    private ItemDomain object;
    private int adultCount, childCount;
    private String selectedPackageName;
    private double totalPriceValue;
    private String selectedDate;
    private String userPhoneNumber;
    private String userFirstName;
    private String userLastName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketBinding.inflate(getLayoutInflater()); // Inflate ActivityTicketBinding
        setContentView(binding.getRoot());

        getIntentExtra();
        setVariable();
    }


    private void getIntentExtra() {
        if (getIntent().getExtras() != null) {
            object = (ItemDomain) getIntent().getSerializableExtra("object");
            adultCount = getIntent().getIntExtra("adultCount", 0);
            childCount = getIntent().getIntExtra("childCount", 0);
            selectedPackageName = getIntent().getStringExtra("selectedPackage");
            selectedDate = getIntent().getStringExtra("selectedDate");
            totalPriceValue = getIntent().getDoubleExtra("totalPrice", 0);
        }
    }

    private void setVariable() {
        if (object != null) {
            Glide.with(TicketDetailActivity.this).load(object.getPic()).into(binding.pic);
            Glide.with(TicketDetailActivity.this).load(object.getTourGuidePic()).into(binding.profile);

            binding.titleTxt.setText(object.getTitle());
            binding.durationTxt.setText(object.getDuration());
            binding.tourGuideTxt.setText(selectedPackageName); // **Hiển thị Package Name**
            binding.timeTxt.setText(selectedDate); // **Hiển thị Selected Date**
            binding.tourGuideNameTxt.setText(userFirstName + " " + userLastName); // Hiển thị User Full Name
            binding.guestTxt.setText(adultCount + " Người lớn, " + childCount + " Trẻ em");

            String orderId = generateRandomOrderId();
            binding.textView12.setText("Order Id:" + orderId);

            NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedPrice = currencyFormatVN.format(totalPriceValue);
//            binding.guestTxt.setText(binding.guestTxt().getText() + " - Tổng tiền: " + formattedPrice.replace("₫", "").trim() + "₫");

        }
        binding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TicketDetailActivity.this,MainActivity.class);
                startActivity(intent);
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
                String phone = object.getTourGuidePhone();
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",phone,null));
                startActivity(intent);
            }
        });
    }

    private String generateRandomOrderId() {
        int randomNumber = new Random().nextInt(1000000);
        return String.format("%06d", randomNumber);
    }
}