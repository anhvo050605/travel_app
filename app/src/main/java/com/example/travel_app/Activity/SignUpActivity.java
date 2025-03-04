package com.example.travel_app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
    EditText dk_email, dk_pass, dk_repass, edt_ho, edt_ten, SDT;
    TextView connectLogin;
    Button btnSignUp;
    ImageView back;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    SharedPreferences sharedPreferences; // Thêm SharedPreferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dk_email = findViewById(R.id.dk_email);
        dk_pass = findViewById(R.id.dk_pass);
        dk_repass = findViewById(R.id.dk_repass);
        btnSignUp = findViewById(R.id.btnSignUp);
        back = findViewById(R.id.back);
        connectLogin = findViewById(R.id.connectLogin);
        edt_ho = findViewById(R.id.edt_ho);
        edt_ten = findViewById(R.id.edt_ten);
        SDT = findViewById(R.id.SDT);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedPreferences = getSharedPreferences("UserData", Context.MODE_PRIVATE); // Khởi tạo SharedPreferences

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail = dk_email.getText().toString();
                String pass = dk_pass.getText().toString();
                String repass = dk_repass.getText().toString();
                String ho = edt_ho.getText().toString();
                String ten = edt_ten.getText().toString();
                String sdt = SDT.getText().toString();

                if (mail.equals("") || pass.equals("") || repass.isEmpty() || ho.isEmpty() || ten.isEmpty() || sdt.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Vui lòng nhập đầy đủ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!pass.equals(repass)) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu không khớp nhau", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidEmail(mail)) {
                    Toast.makeText(SignUpActivity.this, "Địa chỉ email không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (pass.length() < 8 || !Character.isUpperCase(pass.charAt(0))) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu phải có ít nhất 8 kí tự và phải viết hoa chữ cái đầu tiên", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(mail, pass).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("main", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            writeNewUser(user.getUid(), ho, ten, mail, sdt);
                            // Lưu thông tin vào SharedPreferences sau khi đăng ký thành công
                            saveUserDataToSharedPreferences(ho, ten, mail, sdt); // Gọi hàm lưu SharedPreferences
                            Intent in = new Intent(SignUpActivity.this, LoginActivity.class);
                            in.putExtra("email", mail);
                            in.putExtra("password", pass);
                            startActivity(in);
                            Toast.makeText(SignUpActivity.this, "Đăng kí thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(SignUpActivity.this, "createUserWithEmail:fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        connectLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void writeNewUser(String userId, String ho, String ten, String email, String sdt) {
        User user = new User(ho, ten, email, sdt);
        mDatabase.child("users").child(userId).setValue(user);
    }

    // Hàm lưu thông tin người dùng vào SharedPreferences
    private void saveUserDataToSharedPreferences(String ho, String ten, String email, String sdt) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ho", ho);
        editor.putString("ten", ten);
        editor.putString("email", email);
        editor.putString("sdt", sdt);
        editor.apply(); // Sử dụng apply() thay vì commit() để chạy bất đồng bộ
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static class User {
        public String ho;
        public String ten;
        public String email;
        public String sdt;

        public User() {
        }

        public User(String ho, String ten, String email, String sdt) {
            this.ho = ho;
            this.ten = ten;
            this.email = email;
            this.sdt = sdt;
        }
    }
}