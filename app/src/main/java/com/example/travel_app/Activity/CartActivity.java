package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.travel_app.Adapter.CartAdapter;
import com.example.travel_app.Domain.ItemDomain;
import com.example.travel_app.R;
import com.example.travel_app.databinding.ActivityCartBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CartActivity extends BaseActivity {

    ActivityCartBinding binding;
    private RecyclerView.Adapter cartAdapter;
    private RecyclerView recyclerViewCartItems;
    ChipNavigationBar bottomNavBar;

    public static List<ItemDomain> cartItemList = new ArrayList<>(); // Static in-memory cart list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavBar = findViewById(R.id.chipNavigationBar);
        bottomNavBar.setItemSelected(R.id.cart, true);

        initView();
        initList();
        calculateTotal();

        bottomNavBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.explorer) {
                    // Khi chọn Home từ ProfileActivity, quay lại MainActivity (Home)
                    Intent intentHome = new Intent(CartActivity.this, MainActivity.class);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear stack để tránh back về ProfileActivity
                    startActivity(intentHome);
                    finish(); // Đóng ProfileActivity
                } else if (id == R.id.favorites) {
                    // TODO: Xử lý khi chọn Explorer từ ProfileActivity (nếu có trang Explorer riêng)
                    // Ví dụ: startActivity(new Intent(ProfileActivity.this, ExplorerActivity.class));
//                    Toast.makeText(ProfileActivity.this, "Explorer from Profile", Toast.LENGTH_SHORT).show(); // Placeholder
                } else if (id == R.id.cart) {

                } else if (id == R.id.profile) {
                    // TODO: Xử lý khi chọn Bookmark từ ProfileActivity (nếu có trang Bookmark riêng)
                    startActivity(new Intent(CartActivity.this, ProfileActivity.class));
//                    Toast.makeText(ProfileActivity.this, "Bookmark from Profile", Toast.LENGTH_SHORT).show(); // Placeholder
                }
            }
        });
    }


    private void initView() {
        recyclerViewCartItems = binding.cartItemsRecyclerView;

    }

    private void initList() {
        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        // **THÊM CODE DƯỚI ĐÂY ĐỂ LẤY DỮ LIỆU TỪ INTENT**
        Intent intent = getIntent();
        String selectedPackageName = intent.getStringExtra("selectedPackage"); // Lấy tên gói dịch vụ từ Intent
        String selectedDate = intent.getStringExtra("selectedDate"); // Lấy thời gian từ Intent
        int adultCount = intent.getIntExtra("adultCount", 0); // Lấy số lượng người lớn từ Intent
        int childCount = intent.getIntExtra("childCount", 0); // Lấy số lượng trẻ em từ Intent
        String passengerCountText = adultCount + " Người lớn, " + childCount + " Trẻ em"; // Tạo chuỗi hiển thị số lượng khách


        cartAdapter = new CartAdapter(cartItemList, this, selectedPackageName, selectedDate, passengerCountText); // **CODE ĐÚNG - TRUYỀN DỮ LIỆU VÀO ADAPTER**
        recyclerViewCartItems.setAdapter(cartAdapter);
        cartAdapter.notifyDataSetChanged();
    }

//    private void initList() {
//        recyclerViewCartItems.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
//
//        // **Lấy cartItemList từ Intent (nếu có)**
//        Intent intent = getIntent();
//        if (intent != null && intent.hasExtra("cart_item_list")) {
//            cartItemList = (List<ItemDomain>) intent.getSerializableExtra("cart_item_list"); // Lấy cartItemList từ Intent
//            if (cartItemList == null) {
//                cartItemList = new ArrayList<>(); // Nếu không lấy được hoặc null, khởi tạo danh sách rỗng
//            }
//        } else {
//            // Nếu không có Intent extra, sử dụng cartItemList static (in-memory)
//            if (cartItemList == null) {
//                cartItemList = new ArrayList<>();
//            }
//        }
//
//
//        cartAdapter = new CartAdapter(cartItemList, this, "", "", ""); // Khởi tạo adapter với cartItemList (lấy từ Intent hoặc static)
//        recyclerViewCartItems.setAdapter(cartAdapter);
//        cartAdapter.notifyDataSetChanged();
//    }

    private void calculateTotal() {
        double totalAmount = 0;
        for (ItemDomain item : cartItemList) {
            totalAmount += item.getPrice() * item.getQuantity();
        }

        NumberFormat currencyFormatVN = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedTotal = currencyFormatVN.format(totalAmount);
        formattedTotal = formattedTotal.replace("₫", "").trim();

    }



    public  void updateItemQuantityInCart(ItemDomain item, int quantity) {
        // No Firebase update needed for in-memory cart
        if (quantity > 0) {
            item.setQuantity(quantity); // Update quantity in the list
        } else {
            removeItemFromCart(item);
        }
        calculateTotal(); // Recalculate total
        cartAdapter.notifyDataSetChanged(); // Update RecyclerView
    }

    public void removeItemFromCart(ItemDomain item) {
        cartItemList.remove(item); // Remove item from the list
        calculateTotal(); // Recalculate total
        cartAdapter.notifyDataSetChanged(); // Update RecyclerView
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (cartAdapter != null) {
            cartAdapter.notifyDataSetChanged();
            calculateTotal();
        }
    }

    // Static method to add item to the cart
    public static void addItemToNewCart(ItemDomain item) { // Static method to add item
        if (cartItemList == null) {
            cartItemList = new ArrayList<>();
        }

        boolean itemExists = false;
        for (ItemDomain cartItem : cartItemList) {
            if (cartItem.getId().equals(item.getId())) {
                cartItem.setQuantity(cartItem.getQuantity() + 1);
                itemExists = true;
                break;
            }
        }
        if (!itemExists) {
            item.setQuantity(1);
            cartItemList.add(item);
        }
    }
}