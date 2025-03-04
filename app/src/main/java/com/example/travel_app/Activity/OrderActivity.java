package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CalendarView;

import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityOrderBinding;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class OrderActivity extends BaseActivity {

    ActivityOrderBinding binding;
    private ItemDomain object;
    private int adultCount = 1;
    private int childCount = 0;
    private double adultPrice = 365000;
    private double childPrice = 182500;
    private double totalPrice = 0;
    private String selectedDateString = ""; // Biến để lưu ngày đã chọn

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOrderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntentExtra();
        setDetails();
        initListeners();
        calculateTotal();
    }

    private void getIntentExtra() {
        object = (ItemDomain) getIntent().getSerializableExtra("object");
        if (object != null) {
            // **TODO: Lấy giá vé người lớn và trẻ em từ object.getPrice() hoặc nguồn dữ liệu khác**
            // Ví dụ (cần điều chỉnh theo cấu trúc dữ liệu của bạn):
            // adultPrice = object.getAdultTicketPrice();
            // childPrice = object.getChildTicketPrice();
        }
    }

    private void setDetails() {
        binding.titleOrderTxt.setText("Chọn gói dịch vụ");
        binding.packageStandardRadio.setText("Gói Tiêu Chuẩn (Đường Tuệ Phát)");
        binding.packagePremiumRadio.setText("Gói tiêu chuẩn + Tắm bùn");

        // Hiển thị giá vé người lớn và trẻ em
        NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedAdultPrice = currencyFormatVN.format(adultPrice);
        String formattedChildPrice = currencyFormatVN.format(childPrice);

        binding.adultPriceTxt.setText(formattedAdultPrice.replace("₫", "").trim() + "₫");
        binding.childPriceTxt.setText(formattedChildPrice.replace("₫", "").trim() + "₫");

        binding.adultCountTxt.setText(String.valueOf(adultCount));
        binding.childCountTxt.setText(String.valueOf(childCount));
    }

    private void initListeners() {
        binding.backBtn.setOnClickListener(v -> finish());

        binding.adultMinusBtn.setOnClickListener(v -> adjustAdultCount(-1));
        binding.adultPlusBtn.setOnClickListener(v -> adjustAdultCount(1));

        binding.childMinusBtn.setOnClickListener(v -> adjustChildCount(-1));
        binding.childPlusBtn.setOnClickListener(v -> adjustChildCount(1));

        // **Bắt sự kiện chọn ngày trên CalendarView**
        binding.calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDateString = getSelectedDate(year, month, dayOfMonth); // Lấy ngày đã chọn và format
        });


        binding.continueBtn.setOnClickListener(v -> {
            // **Chuyển sang ConfirmationActivity và truyền dữ liệu**
            Intent intent = new Intent(OrderActivity.this, ConfirmationActivity.class);
            intent.putExtra("object", object);
            intent.putExtra("adultCount", adultCount);
            intent.putExtra("childCount", childCount);
            intent.putExtra("selectedPackage", getSelectedPackageName());
            intent.putExtra("totalPrice", totalPrice);
            intent.putExtra("selectedDate", selectedDateString);

            // **Thêm các extra DƯỚI ĐÂY để truyền sang TicketActivity**
            intent.putExtra("adultCount", adultCount); // Truyền số lượng người lớn
            intent.putExtra("childCount", childCount); // Truyền số lượng trẻ em
            intent.putExtra("selectedPackage", getSelectedPackageName()); // Truyền tên gói dịch vụ
            intent.putExtra("selectedDate", selectedDateString); // Truyền ngày đã chọn
            intent.putExtra("totalPriceValue", totalPrice); // Truyền tổng giá vé

            startActivity(intent);
        });
    }

    private String getSelectedPackageName() {
        int selectedId = binding.packageRadioGroup.getCheckedRadioButtonId();
        if (selectedId == R.id.packageStandardRadio) {
            return binding.packageStandardRadio.getText().toString();
        } else if (selectedId == R.id.packagePremiumRadio) {
            return binding.packagePremiumRadio.getText().toString();
        }
        return "Không chọn gói";
    }

    private void adjustAdultCount(int change) {
        adultCount += change;
        if (adultCount < 1) adultCount = 1;
        binding.adultCountTxt.setText(String.valueOf(adultCount));
        calculateTotal();
    }

    private void adjustChildCount(int change) {
        childCount += change;
        if (childCount < 0) childCount = 0;
        binding.childCountTxt.setText(String.valueOf(childCount));
        calculateTotal();
    }

    private void calculateTotal() {
        totalPrice = (adultCount * adultPrice) + (childCount * childPrice);
        NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotal = currencyFormatVN.format(totalPrice);
        binding.totalPriceValueTxt.setText(formattedTotal.replace("₫", "").trim() + "₫");
    }

    // **Hàm lấy ngày đã chọn từ CalendarView và format thành String**
    private String getSelectedDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE - dd/MM/yyyy", new Locale("vi", "VN")); // Định dạng ngày kiểu "Thứ Ba - 25/02/2025"
        return dateFormat.format(calendar.getTime());
    }
}