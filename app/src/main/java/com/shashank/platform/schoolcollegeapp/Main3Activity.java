package com.shashank.platform.schoolcollegeapp;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Main3Activity extends AppCompatActivity {

    TextView back;
    Button logout;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main3);
        /*back = findViewById(R.id.back);
        back.setOnClickListener(view -> finish());*/
        logout = findViewById(R.id.logout);
        logout.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            finish();
        });
    }
}
