package com.example.studentmove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class registerPageActivity extends AppCompatActivity {

    private Button findDonation, donateItems;
    private TextView existingAcc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        donateItems = findViewById(R.id.donateItems);
        findDonation = findViewById(R.id.findDonation);
        existingAcc = findViewById(R.id.existingAcc);

        findDonation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerPageActivity.this, getStuffActivity.class);
                startActivity(intent);
                finish();
            }
        });

        donateItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerPageActivity.this, GiveAwayActivity.class);
                startActivity(intent);
                finish();
            }
        });

        existingAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(registerPageActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }}

