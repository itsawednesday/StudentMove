package com.example.studentmove;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GiveAwayActivity extends AppCompatActivity {
    private TextView existingAcc;
    private CircleImageView profile_image;
    private TextInputEditText registerName, registerID, registerPhone, registerEmail, registerPassword;

    private Button registerButton;
    private Spinner spinnerItems;
    private ProgressDialog loading;

    private Uri getURI;
    private FirebaseAuth mAuth;
    private DatabaseReference userDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_away);

        existingAcc = findViewById(R.id.existingAcc);

        existingAcc.setOnClickListener((view) ->  {


                Intent intent = new Intent(GiveAwayActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        );


        //initalize all id's
        profile_image = findViewById(R.id.profile_image);
        registerName = findViewById(R.id.registerName);
        registerEmail = findViewById(R.id.registerEmail);
        registerID = findViewById(R.id.registerID);
        registerPhone = findViewById(R.id.registerPhone);
        registerPassword = findViewById(R.id.registerPassword);
        spinnerItems = findViewById(R.id.spinnerItems);
        registerButton = findViewById(R.id.registerButton);

        loading = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();


        //redirect user to gallery when clicking on profile avatar
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/");
                startActivityForResult(intent, 1);
            }
        });
        //use register button and verify user has entered all info
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = registerEmail.getText().toString().trim();
                final String name = registerName.getText().toString().trim();
                final String password = registerPassword.getText().toString().trim();
                final String id = registerID.getText().toString().trim();
                final String phone = registerPhone.getText().toString().trim();
                final String spin = spinnerItems.getSelectedItem().toString();

                //if email field is empty
                if (TextUtils.isEmpty(email)) {
                    registerEmail.setError("Please enter Email");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    registerPhone.setError("Please enter phone number");
                    return;
                }
                if (TextUtils.isEmpty(id)) {
                    registerID.setError("Please enter ID number");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    registerPassword.setError("Please enter password");
                    return;
                }
                if (TextUtils.isEmpty(name)) {
                    registerName.setError("Please enter name number");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    registerPhone.setError("Please enter phone number");
                    return;
                }
                if (spinnerItems.equals("What do you wish to give away?")) {
                    Toast.makeText(GiveAwayActivity.this, "Select category", Toast.LENGTH_SHORT).show();
                    return;

                    //if everything succedes then add loading message
                } else {
                    loading.setMessage("Registering...");
                    loading.setCanceledOnTouchOutside(false);
                    loading.show();

                    //check if user has been registered, then firebase will add to database, else throw error
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(!task.isSuccessful()){
                                String error = task.getException().toString();
                                Toast.makeText(GiveAwayActivity.this, "Error" + error, Toast.LENGTH_SHORT).show();
                            }
                            else{
                                String registeredUser = mAuth.getCurrentUser().getUid();
                                userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").
                                        child(registeredUser);
                                //store all the data 
                                HashMap userInformation = new HashMap();
                                userInformation.put("id", registeredUser);
                                userInformation.put("email", email);
                                userInformation.put("name", name);
                                userInformation.put("id", id);
                                userInformation.put("phone", phone);
                                userInformation.put("spinnerItems", spinnerItems);
                                userInformation.put("category", "giver" );
                                userInformation.put("search", "giver" + spinnerItems);

                                userDatabaseReference.updateChildren(userInformation).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {

                                        if(task.isSuccessful()) {
                                            Toast.makeText(GiveAwayActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                        }else {
                                            Toast.makeText(GiveAwayActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                            finish();

                                            }

                                        }  );
                               if(getURI !=null){
                                   final StorageReference filePath = FirebaseStorage.getInstance().getReference()
                                           .child("profile images").child(registeredUser)  ;
                                   Bitmap bitmap = null;
                                   try {
                                       bitmap  = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), getURI) ;



                                       }catch(IOException e) {
                                       e.printStackTrace();  }
                                       //compress the image from getURI and then upload to firebase
                                       ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                       bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
                                       byte[] data = byteArrayOutputStream.toByteArray();
                                       UploadTask uploadTask = filePath.putBytes(data);

                                            //if it fails
                                       uploadTask.addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                               Toast.makeText(GiveAwayActivity.this, "Image upload failed", Toast.LENGTH_SHORT).show();
                                           }

                                           }
                                       )   ;             //if it succeeds
                                       uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                           @Override
                                           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                               if(taskSnapshot.getMetadata() !=null && taskSnapshot.getMetadata().getReference() !=null)  {
                                                   Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                   result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                       @Override
                                                       public void onSuccess(Uri uri) {
                                                           String imageUrI = uri.toString();
                                                           Map newImageMap =  new HashMap();

                                                           newImageMap.put("profilepictureurl", imageUrI) ;

                                                           userDatabaseReference.updateChildren(newImageMap).addOnCompleteListener(new OnCompleteListener() {
                                                               @Override
                                                               public void onComplete(@NonNull Task task) {
                                                                   if(task.isSuccessful()) {
                                                                       Toast.makeText(GiveAwayActivity.this, "Image url added", Toast.LENGTH_SHORT).show();
                                                                   }else{
                                                                       Toast.makeText(GiveAwayActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                                                   }

                                                               }
                                                           }) ;
                                                           finish();


                                                       }
                                                   }) ;
                                               }
                                           }

                                           } );
                                       Intent intent = new Intent(GiveAwayActivity.this, MainActivity2.class);
                                       startActivity(intent);
                                       finish();
                                       loading.dismiss();
                                       }
                               }
                            }
                        });
                    }
                }
            } );

        }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1 && resultCode == RESULT_OK && data !=null) {
            getURI = data.getData();
            profile_image.setImageURI(getURI);

        }}
    }