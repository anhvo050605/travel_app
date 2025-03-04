package com.example.travel_app.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.travel_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class ConFigActivity extends AppCompatActivity {
    ImageButton btnBack;
    TextInputEditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, dobEditText;
    Button btnUpdate;
    ImageView calendarIcon;

    SharedPreferences sharedPreferences;
    FirebaseAuth mAuth; // Firebase Authentication instance
    DatabaseReference mDatabase; // Firebase Realtime Database instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_con_fig);

        // Initialize Views
        btnBack = findViewById(R.id.btnBack);
        firstNameEditText = findViewById(R.id.firstNameEditText);
        lastNameEditText = findViewById(R.id.lastNameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        dobEditText = findViewById(R.id.dobEditText);
        btnUpdate = findViewById(R.id.btnUpdate);
        calendarIcon = findViewById(R.id.calendarIcon);

        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE);
        mAuth = FirebaseAuth.getInstance(); // Initialize FirebaseAuth
        mDatabase = FirebaseDatabase.getInstance().getReference(); // Initialize FirebaseDatabase

        // Set Listeners
        btnBack.setOnClickListener(v -> finish());
        btnUpdate.setOnClickListener(this::saveChanges);
        dobEditText.setOnClickListener(this::showDatePickerDialog);
        calendarIcon.setOnClickListener(this::showDatePickerDialog);

        loadUserProfileFromSharedPreferences(); // Load existing profile data
    }

    private void loadUserProfileFromSharedPreferences() {
        String ho = sharedPreferences.getString("ho", "");
        String ten = sharedPreferences.getString("ten", "");
        String email = sharedPreferences.getString("email", "");
        String sdt = sharedPreferences.getString("sdt", "");
        String dob = sharedPreferences.getString("dob", "");

        firstNameEditText.setText(ho);
        lastNameEditText.setText(ten);
        emailEditText.setText(email);
        phoneEditText.setText(sdt);
        dobEditText.setText(dob);
    }

    public void saveChanges(View view) {
        String ho = firstNameEditText.getText().toString();
        String ten = lastNameEditText.getText().toString();
        String newEmail = emailEditText.getText().toString(); // Get new email from EditText
        String sdt = phoneEditText.getText().toString();
        String dob = dobEditText.getText().toString();

        FirebaseUser user = mAuth.getCurrentUser(); // Get current FirebaseUser

        if (user == null) {
            Log.w("ConFigActivity", "Không có người dùng nào đăng nhập Firebase!"); // Log cảnh báo
            Toast.makeText(ConFigActivity.this, "Bạn chưa đăng nhập. Vui lòng đăng nhập để cập nhật thông tin.", Toast.LENGTH_LONG).show(); // Thông báo cho người dùng
            return; // Dừng hàm nếu không có user
        }

        Log.d("ConFigActivity", "Email hiện tại trên Firebase Auth: " + user.getEmail()); // Log email hiện tại
        Log.d("ConFigActivity", "Email mới nhập: " + newEmail); // Log email mới

        if (!user.getEmail().equals(newEmail)) { // Check if user exists and email has changed
            Log.d("ConFigActivity", "Email thay đổi, tiến hành cập nhật Firebase Auth..."); // Log thông báo cập nhật
            user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() { // Update email in Firebase Auth
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("ConFigActivity", "Cập nhật email Firebase Auth thành công!"); // Log thành công
                        updateUserDataInSharedPreferences(ho, ten, newEmail, sdt, dob); // Update SharedPreferences
                    } else {
                        Log.e("ConFigActivity", "Lỗi cập nhật email Firebase Auth: ", task.getException()); // Log lỗi chi tiết
                        Toast.makeText(ConFigActivity.this, "Không thể cập nhật email. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Log.d("ConFigActivity", "Email không thay đổi, bỏ qua cập nhật Firebase Auth."); // Log thông báo bỏ qua cập nhật
            updateUserDataInSharedPreferences(ho, ten, newEmail, sdt, dob); // Update SharedPreferences only if email not changed or no Firebase user
        }
    }

    private void updateUserDataInSharedPreferences(String ho, String ten, String email, String sdt, String dob) {
        // Save updated user information to SharedPreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ho", ho);
        editor.putString("ten", ten);
        editor.putString("email", email); // Save new email to SharedPreferences
        editor.putString("sdt", sdt);
        editor.putString("dob", dob);
        editor.apply();

        Toast.makeText(ConFigActivity.this, "Thông tin tài khoản đã được cập nhật", Toast.LENGTH_SHORT).show();
        finish(); // Optionally go back to ProfileActivity after saving
    }


    public void showDatePickerDialog(View view) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                ConFigActivity.this,
                (datePicker, yearPicked, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + yearPicked;
                    dobEditText.setText(selectedDate);
                },
                year, month, day);
        datePickerDialog.show();
    }
}