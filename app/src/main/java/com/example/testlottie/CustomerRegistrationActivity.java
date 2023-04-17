package com.example.testlottie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CustomerRegistrationActivity extends AppCompatActivity {
    EditText name,email,address,contact;
    ImageView submit_btn;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_registration);
        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        contact=findViewById(R.id.contact);
        submit_btn=findViewById(R.id.submit_btn);


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String S_name=String.valueOf(name.getText()).trim();
                String S_email=String.valueOf(email.getText()).trim();
                String S_address=String.valueOf(address.getText()).trim();
                String S_contact=String.valueOf(contact.getText()).trim();

                if(TextUtils.isEmpty(S_name)){
                    Toast.makeText(CustomerRegistrationActivity.this,"Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(S_email)){
                    Toast.makeText(CustomerRegistrationActivity.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(S_address)){
                    Toast.makeText(CustomerRegistrationActivity.this,"Enter Address",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(S_contact)){
                    Toast.makeText(CustomerRegistrationActivity.this,"Enter Contact Number",Toast.LENGTH_SHORT).show();
                    return;
                }
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                CollectionReference userCollection=firestore.collection(uid);
                DocumentReference newDocument = userCollection.document(S_email);
                Map<String,Object> data=new HashMap<>();
                data.put("Name", S_name);
                data.put("Email", S_email);
                data.put("Address",S_address);
                data.put("Contact",S_contact);
                newDocument.set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(CustomerRegistrationActivity.this, "Data added to document",Toast.LENGTH_SHORT);
                                startActivity(new Intent(CustomerRegistrationActivity.this,HomeActivity.class));
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CustomerRegistrationActivity.this, "Error adding data to document: " + e.getMessage(),Toast.LENGTH_LONG);
                            }
                        });

            }
        });
    }
}