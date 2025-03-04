package com.example.travel_app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityConfirmationBinding;

import java.text.NumberFormat;
import java.util.Locale;

public class ConfirmationActivity extends BaseActivity {

    ActivityConfirmationBinding binding;
    private ItemDomain object;
    private int adultCount, childCount;
    private String selectedPackageName;
    private double totalPriceValue;
    private String selectedDate; // Thêm biến để nhận ngày đã chọn
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);

        getIntentExtra();
        setOrderDetails();
        initListeners();
    }

    private void getIntentExtra() {
        if (getIntent().getExtras() != null) {
            object = (ItemDomain) getIntent().getSerializableExtra("object");
            adultCount = getIntent().getIntExtra("adultCount", 0);
            childCount = getIntent().getIntExtra("childCount", 0);
            selectedPackageName = getIntent().getStringExtra("selectedPackage");
            totalPriceValue = getIntent().getDoubleExtra("totalPrice", 0);
            selectedDate = getIntent().getStringExtra("selectedDate"); // **Lấy ngày đã chọn từ Intent**
        }
    }

    private void setOrderDetails() {
        if (object != null) {
            binding.tourNameTxt.setText(object.getTitle());
            binding.packageValueTxt.setText(selectedPackageName);
            binding.passengerCountTxt.setText(adultCount + " Người lớn, " + childCount + " Trẻ em");
            binding.dateDetailTxt.setText("Thời gian: " + selectedDate); // **Hiển thị ngày đã chọn**

            NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedPrice = currencyFormatVN.format(totalPriceValue);
            binding.totalPriceValueTxt.setText(formattedPrice.replace("₫", "").trim() + "₫");

            Glide.with(this).load(object.getPic()).into(binding.tourImage);

            binding.adultPriceDetailTxt.setText(binding.totalPriceValueTxt.getText());

//            binding.childPriceDetailTxt.setText(binding.totalPriceValueTxt.getText());

        }

        String ho = sharedPreferences.getString("ho", "");
        String ten = sharedPreferences.getString("ten", "");
        String email = sharedPreferences.getString("email", "");
        String sdt = sharedPreferences.getString("sdt", "");

        binding.lastNameEditText.setText(ho);
        binding.firstNameEditText.setText(ten);
        binding.emailEditText.setText(email);
        binding.phoneEditText.setText(sdt);

        binding.holderLastNameEditText.setText(ho);
        binding.holderFirstNameEditText.setText(ten);
        binding.contactLastNameEditText.setText(ho);
        binding.contactFirstNameEditText.setText(ten);
    }


    private void initListeners() {
        binding.backBtn.setOnClickListener(v -> finish());
//        binding.closeBtn.setOnClickListener(v -> { // Nút close (ví dụ: quay về MainActivity)
//            Intent intent = new Intent(ConfirmationActivity.this, MainActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//            finish();
//        });

        binding.payButton.setOnClickListener(v -> {
            if (validateForm()) {
                // **Chuyển sang PaymentActivity và truyền dữ liệu**
                Intent intent = new Intent(ConfirmationActivity.this, PaymentActivity.class);
                intent.putExtra("object", object);
                intent.putExtra("adultCount", adultCount);
                intent.putExtra("childCount", childCount);
                intent.putExtra("selectedPackage", selectedPackageName);
                intent.putExtra("totalPrice", totalPriceValue);
                intent.putExtra("selectedDate", selectedDate);

                // **THÊM CÁC EXTRA DƯỚI ĐÂY ĐỂ CHUYỂN TIẾP SANG PaymentActivity**
                intent.putExtra("adultCount", adultCount); // Truyền số lượng người lớn
                intent.putExtra("childCount", childCount); // Truyền số lượng trẻ em
                intent.putExtra("selectedPackage", selectedPackageName); // Truyền tên gói dịch vụ
                intent.putExtra("selectedDate", selectedDate); // Truyền ngày đã chọn
                intent.putExtra("totalPriceValue", totalPriceValue); // Truyền tổng giá vé

                startActivity(intent);
            }
        });
    }

    private boolean validateForm() {
        if (binding.lastNameEditText.getText().toString().isEmpty() ||
                binding.firstNameEditText.getText().toString().isEmpty() ||
                binding.emailEditText.getText().toString().isEmpty() ||
                binding.phoneEditText.getText().toString().isEmpty() ||
                !binding.termsCheckbox.isChecked()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin và đồng ý điều khoản.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}