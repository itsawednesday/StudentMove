package com.example.studentmove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity {
    private Button logoutButton;
    Toolbar toolbar = (Toolbar)findViewById(R.id.mainToolbar);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        setSupportActionBar(toolbar);
        toolbar.showOverflowMenu();


        logoutButton = findViewById(R.id.loginButton);

        //onclicklistener to sign out
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                //direct to login afterwards
                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });
    }
}