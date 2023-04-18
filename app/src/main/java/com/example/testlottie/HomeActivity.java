package com.example.testlottie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {
    Button registercustomer;
    FirebaseFirestore firestore;
    FirebaseAuth mAuth;
    ListView customerlist;
    ArrayAdapter<String> adapter;

    ProgressDialog progressDialog;
    TextView search;
    SwipeRefreshLayout swipe_refresh;

    List<String> documentNames;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        progressDialog=new ProgressDialog(this);
        registercustomer=findViewById(R.id.registercustomer);
        customerlist=findViewById(R.id.customerlist);
        search=findViewById(R.id.search);
        swipe_refresh=findViewById(R.id.swipe_refresh);

        firestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();



        progressDialog.show();
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        CollectionReference collectionRef = firestore.collection(uid);
        collectionRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {
                documentNames = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : querySnapshot) {
                    String documentName = documentSnapshot.getId();
                    DocumentReference docRef = firestore.collection(uid).document(documentName);
                    documentNames.add(documentName);
                    ArrayList<String> usernames= new ArrayList<>();
                    docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    for (DocumentSnapshot documentSnap : querySnapshot) {
                                        String username = documentSnap.getString("Name");
                                        usernames.add(username);
                                    }
                                }
                            progressDialog.cancel();
                            adapter = new ArrayAdapter<>(HomeActivity.this, R.layout.activity_customer_list, R.id.customer_name, usernames);
                            customerlist.setAdapter(adapter);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.cancel();
                            Toast.makeText(HomeActivity.this, "Error getting document: " + e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        customerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(HomeActivity.this, adapter.getItem(i), Toast.LENGTH_SHORT).show();
            }
        });

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                customerlist.setAdapter(adapter);
                swipe_refresh.setRefreshing(false);
            }
        });

        customerlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(HomeActivity.this)

                        .setTitle("Do you want to delete customer"+adapter.getItem(i)+"From List")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(HomeActivity.this,"Element Deleted",Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(HomeActivity.this,"Element not Deleted",Toast.LENGTH_SHORT).show();
                            }
                        }).create().show();

                return false;
            }
        });


        registercustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              startActivity(new Intent(HomeActivity.this,CustomerRegistrationActivity.class));
              finish();
            }
        });
    }

}