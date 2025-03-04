package com.example.travel_app.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel_app.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class ProfileActivity extends AppCompatActivity {

    TextView fullName,config_account;
    SharedPreferences sharedPreferences;
    ChipNavigationBar bottomNavBarProfile;
    LinearLayout DangXuat; // Khai báo LinearLayout Đăng xuất

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        fullName = findViewById(R.id.fullName);
        config_account = findViewById(R.id.config_account);
        bottomNavBarProfile = findViewById(R.id.chipNavigationBar); // Sửa ID bottomNavBarProfile
        DangXuat = findViewById(R.id.DangXuat); // Ánh xạ LinearLayout Đăng xuất

        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE); // Khởi tạo SharedPreferences

        loadUserProfileFromSharedPreferences();
        bottomNavBarProfile.setItemSelected(R.id.profile, true);

        config_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ConFigActivity.class);
                startActivity(intent);
            }
        });

        // Set OnClickListener cho LinearLayout Đăng xuất
        DangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogoutConfirmationDialog(); // Gọi hàm hiển thị dialog xác nhận đăng xuất
            }
        });


        bottomNavBarProfile.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                if (id == R.id.explorer) {
                    // Khi chọn Home từ ProfileActivity, quay lại MainActivity (Home)
                    Intent intentHome = new Intent(ProfileActivity.this, MainActivity.class);
                    intentHome.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // Clear stack để tránh back về ProfileActivity
                    startActivity(intentHome);
                    finish(); // Đóng ProfileActivity
                } else if (id == R.id.favorites) {
                    // TODO: Xử lý khi chọn Explorer từ ProfileActivity (nếu có trang Explorer riêng)
                    // Ví dụ: startActivity(new Intent(ProfileActivity.this, ExplorerActivity.class));
//                    Toast.makeText(ProfileActivity.this, "Explorer from Profile", Toast.LENGTH_SHORT).show(); // Placeholder
                } else if (id == R.id.cart) {
                    // TODO: Xử lý khi chọn Bookmark từ ProfileActivity (nếu có trang Bookmark riêng)
                    startActivity(new Intent(ProfileActivity.this, CartActivity.class));
//                    Toast.makeText(ProfileActivity.this, "Bookmark from Profile", Toast.LENGTH_SHORT).show(); // Placeholder
                } else if (id == R.id.profile) {
                    // Không cần làm gì khi đã ở trang Profile và chọn Profile icon
                }
            }
        });



    }

    // Hàm load thông tin người dùng từ SharedPreferences
    private void loadUserProfileFromSharedPreferences() {
        String ho = sharedPreferences.getString("ho", "Không có"); // Lấy Họ, mặc định "Không có" nếu không tìm thấy
        String ten = sharedPreferences.getString("ten", "Thông tin"); // Lấy Tên, mặc định "Thông tin"
        fullName.setText(ho + " " + ten);
    }

    // Hàm hiển thị Dialog xác nhận đăng xuất
    private void showLogoutConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận đăng xuất"); // Tiêu đề dialog
        builder.setMessage("Bạn có chắc chắn muốn đăng xuất?"); // Thông báo dialog

        // Nút "Có" (Đăng xuất)
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Thực hiện đăng xuất Firebase
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear stack
                startActivity(intent);
                finish();
            }
        });

        // Nút "Không" (Hủy)
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show(); // Hiển thị dialog
    }
}