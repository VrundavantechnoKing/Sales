package com.example.testlottie;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    Button registercustomer;
    ListView customerlist;
    String name[]={"Abhijeet","Sahil","Rohit","Yash","Pritesh","Hetal","Udit","Dnyaneshwari","Rutuja","Pritesh","Aniket","Aman","Happy","Radhe"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        registercustomer=findViewById(R.id.registercustomer);
        customerlist=findViewById(R.id.customerlist);
        ArrayAdapter arr=new ArrayAdapter(this,R.layout.activity_customer_list,R.id.customer_name,name);
        customerlist.setAdapter(arr);

        customerlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(HomeActivity.this, arr.getItem(i)+"", Toast.LENGTH_SHORT).show();
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