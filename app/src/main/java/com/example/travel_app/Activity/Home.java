package com.example.travel_app.Activity;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.travel_app.R;
import com.google.firebase.firestore.FirebaseFirestore;

public class Home extends AppCompatActivity {
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        db = FirebaseFirestore.getInstance();

        ghiDulieu();

        docDulieu();
    }

    private void ghiDulieu() {

    }

    private void docDulieu() {

    }
}