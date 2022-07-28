package com.example.studentmove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {



        private TextView existingAcc;

        private TextInputEditText loginEmail, loginPassword;
        private TextView forgotPassword;
        private Button loginButton;
        private FirebaseAuth mAuth;
        //show loading
        private ProgressDialog loading;

        private FirebaseAuth.AuthStateListener authStateListener;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_login);

            //if user is logged in already then direct user to main activity
            mAuth = FirebaseAuth.getInstance();
            authStateListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser user = mAuth.getCurrentUser();
                    if(user !=null){
                        Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                        startActivity(intent);
                        finish();

                    }


                }
            };

            existingAcc = findViewById(R.id.existingAcc);
            loginEmail = findViewById(R.id.loginEmail);
            loginPassword = findViewById(R.id.loginPassword);
            forgotPassword = findViewById(R.id.forgotPassword);
            loginButton = findViewById(R.id.loginButton);
            loading = new ProgressDialog(this);



            existingAcc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(LoginActivity.this, registerPageActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final String email = loginEmail.getText().toString().trim();
                    final String password = loginPassword.getText().toString().trim();

                    if(TextUtils.isEmpty(email)){
                        loginEmail.setError("Email is required");
                    }

                    if(TextUtils.isEmpty(password)) {
                        loginPassword.setError("Password is required");
                    }
                    else{
                        loading.setMessage("Loading");
                        loading.setCanceledOnTouchOutside(false);
                        loading.show();

                        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, MainActivity2.class);
                                    startActivity(intent);
                                    finish();

                                }else{
                                    Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                                }
                                loading.dismiss();

                            }
                        });

                    }



                }
            });
        }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}
