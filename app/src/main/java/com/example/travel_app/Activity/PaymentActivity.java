package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityPaymentBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class PaymentActivity extends BaseActivity {

    ActivityPaymentBinding binding;
    private ItemDomain object;
    private int adultCount, childCount;
    private String selectedPackageName;
    private double totalPriceValue;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setPaymentDetails();
        initListeners();
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

    private void setPaymentDetails() {
        if (object != null) {
            binding.tourNameTxt.setText(object.getTitle()); // Set Tour Name
            binding.packageValueTxt.setText(selectedPackageName); // Set Package Name
            binding.passengerCountTxt.setText(adultCount + " Người lớn, " + childCount + " Trẻ em"); // Set Passenger Count
//            binding.dateDetailTxt.setText("Thời gian: " + selectedDate); // Set Selected Date

            NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedPrice = currencyFormatVN.format(totalPriceValue);
            binding.totalPriceValueTxt.setText(formattedPrice.replace("₫", "").trim() + "₫"); // Set Total Price (Big)
//            binding.totalPriceDetailTxt.setText(binding.totalPriceValueTxt.getText()); // Set Total Price (Detail Section)

//            Glide.with(this).load(object.getPic()).into(binding.tourImage); // Load Tour Image
        }
    }

    private void initListeners() {
        binding.backBtn.setOnClickListener(v -> finish());


        binding.confirmPayButton.setOnClickListener(v -> {
            // **TODO: Implement Payment Processing Logic Here**
            Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show(); // Show temporary success message

            // **Navigate to TicketActivity and pass data**
            Intent intent = new Intent(PaymentActivity.this, TicketActivity.class);
            intent.putExtra("object", object);
            intent.putExtra("adultCount", adultCount);
            intent.putExtra("childCount", childCount);
            intent.putExtra("selectedPackage", selectedPackageName);
            intent.putExtra("selectedDate", selectedDate);
            intent.putExtra("totalPrice", totalPriceValue);
            startActivity(intent);
            finish(); // Optional: Close PaymentActivity after payment
        });
    }
}