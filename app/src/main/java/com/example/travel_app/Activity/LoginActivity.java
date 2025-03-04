package com.example.travel_app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel_app.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button btnLogin;
    TextView connectSignUp;
    ImageView showPasswordIcon;
    private FirebaseAuth mAuth;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editTextPassword);
        btnLogin = findViewById(R.id.buttonLogin);
        connectSignUp = findViewById(R.id.connectSignUp);
        showPasswordIcon = findViewById(R.id.show);

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE); // Khởi tạo SharedPreferences

        Intent intent = getIntent();

        if (intent != null) {
            Bundle ex = intent.getExtras();
            if (ex != null) {
                email.setText(ex.getString("email"));
                password.setText(ex.getString("password"));
            }
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = email.getText().toString();
                String pass = password.getText().toString();

                if (mail.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Không được bỏ trống", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(mail)) {
                    Toast.makeText(LoginActivity.this, "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(mail, pass).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("main", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();

                            // Lấy thông tin người dùng từ SharedPreferences
                            String ho = sharedPreferences.getString("ho", "Không có");
                            String ten = sharedPreferences.getString("ten", "Thông tin");
                            String userEmail = sharedPreferences.getString("email", "Không có");
                            String sdt = sharedPreferences.getString("sdt", "Không có");

                            // Tạo Intent để chuyển sang MainActivity và truyền thông tin người dùng
                            Intent intent1 = new Intent(LoginActivity.this, MainActivity.class);
                            intent1.putExtra("ho", ho);
                            intent1.putExtra("ten", ten);
                            intent1.putExtra("email", userEmail);
                            intent1.putExtra("sdt", sdt);
                            startActivity(intent1);
                            finish(); // Đóng LoginActivity
                        } else {
                            Log.w("main", "signInWithEmail:fail", task.getException());
                            Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        connectSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent1);
            }
        });

        showPasswordIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (password.getTransformationMethod() instanceof PasswordTransformationMethod) {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    showPasswordIcon.setImageResource(R.drawable.ic_eye);
                } else {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showPasswordIcon.setImageResource(R.drawable.ic_eye);
                }
                password.setSelection(password.getText().length());
            }
        });
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}