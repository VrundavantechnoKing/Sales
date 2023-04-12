package com.example.testlottie;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testlottie.databinding.ActivityMainBinding;
import com.example.testlottie.databinding.ActivityRegistrationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegistrationActivity extends AppCompatActivity {
    EditText name,email,password,C_password,address,contact;
    ImageView submit_btn;
    private TextView SignIn_btn;
    ActivityRegistrationBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityRegistrationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        firestore=FirebaseFirestore.getInstance();

        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        address=findViewById(R.id.address);
        contact=findViewById(R.id.contact);
        password=findViewById(R.id.password);
        C_password=findViewById(R.id.C_password);
        submit_btn=findViewById(R.id.submit_btn);
        SignIn_btn=findViewById(R.id.Signin_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String S_name,S_email,S_password,S_Cpassword,S_address,S_contact;
                S_name=String.valueOf(name.getText()).trim();
                S_email=String.valueOf(email.getText()).trim();
                S_password=String.valueOf(password.getText()).trim();
                S_Cpassword=String.valueOf(C_password.getText()).trim();
                S_address=String.valueOf(address.getText()).trim();
                S_contact=String.valueOf(contact.getText()).trim();

                if(TextUtils.isEmpty(S_name)){
                    Toast.makeText(RegistrationActivity.this,"Enter Name",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(S_email)){
                    Toast.makeText(RegistrationActivity.this,"Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(S_address)){
                    Toast.makeText(RegistrationActivity.this,"Enter Address",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(S_contact)){
                    Toast.makeText(RegistrationActivity.this,"Enter Contact Number",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(S_password)){
                    Toast.makeText(RegistrationActivity.this,"Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(S_Cpassword)){
                    Toast.makeText(RegistrationActivity.this,"Enter Confirm Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(S_email, S_password)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                                finish();
                                progressDialog.cancel();

                                firestore.collection("User")
                                        .document(FirebaseAuth.getInstance().getUid())
                                        .set(new UserModel(S_name,S_email,S_address,S_contact,S_password));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegistrationActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                                progressDialog.cancel();
                            }
                        });
            }
        });

        SignIn_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistrationActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
}