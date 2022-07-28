package com.example.studentmove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.window.SplashScreen;

public class MainActivity extends AppCompatActivity {
    private ImageView logo;
    private TextView title, idea;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logo);
        title = findViewById(R.id.title);
        idea = findViewById(R.id.idea);


        //   public void run(){
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }



}
