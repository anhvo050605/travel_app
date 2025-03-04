package com.example.travel_app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.travel_app.databinding.ActivityIntroBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IntroActivity extends BaseActivity {
    ActivityIntroBinding binding;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIntroBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.introBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(IntroActivity.this, LoginActivity.class));
            }
        });

        db = FirebaseFirestore.getInstance();

        ghiDulieu();
        docDulieu();

    }

    private void ghiDulieu() {
        CollectionReference cities = db.collection("cities");

        Map<String, Object> data1 = new HashMap<>();
        data1.put( "name",  "San Francisco");
        data1.put("state", "CA") ;
        data1.put ( "country",  "USA") ;
        data1.put("capital", false);
        data1.put ("population", 860000);
        data1.put( "regions", Arrays.asList ("west_coast", "norcal"));
        cities. document (  "SF") . set(data1);

        Map<String, Object> data2 = new HashMap<>();
        data2. put("name",  "Los Angeles") ;
        data2.put( "state",  "CA") ;
        data2.put (  "country",  "USA") ;
        data2.put("capital",  false);
        data2.put( "population",  3900000);
        data2.put ( "regions", Arrays.asList ("west_coast", "socal"));
        cities. document (  "LA") .set (data2) ;

        Map<String, Object> data3 = new HashMap<>();
        data3. put ( "name",  "Washington D.C.");
        data3. put( "state",  null);
        data3. put ( "country",  "USA");
        data3. put ("capital", true);
        data3. put ("population",  680000);
        data3. put ("regions", Arrays. asList ("east_coast")) ;
        cities. document ( "DC"). set (data3);

        Map<String, Object> data4 = new HashMap<>();
        data4.put ( "name",  "Tokyo");
        data4.put( "state",  null);
        data4.put( "country", "Japan");
        data4.put( "capital", true);
        data4.put( "population",  9000000);
        data4.put( "regions", Arrays.asList("kanto" , "honshu"));
        cities.document (  "TOK") .set(data4);

        Map<String, Object> data5 = new HashMap<>() ;
        data5.put( "name",  "Beijing");
        data5.put( "state",  null);
        data5.put( "country",  "China");
        data5.put("capital", true);
        data5.put( "population",  21500000) ;
        data5. put( "regions", Arrays.asList ("jingiinii", "hebei"));
        cities. document (  "BJ") .set(data5) ;

    }

    String TAG = "IntroActivity";

    private void docDulieu() {
        db.collection("cities").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                 if (task.isSuccessful()){
                     for (QueryDocumentSnapshot document: task.getResult()){
                         Log.d(TAG,document.getId()+"=>"+document.getData());
                     }
                 }else {
                     Log.d(TAG,"Error getting document: ",task.getException());
                 }
            }
        });
    }
}