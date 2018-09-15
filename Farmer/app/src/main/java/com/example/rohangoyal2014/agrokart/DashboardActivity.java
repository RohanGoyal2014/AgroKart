package com.example.rohangoyal2014.agrokart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.rohangoyal2014.agrokart.Utils.mAuth;
import static com.example.rohangoyal2014.agrokart.Utils.phoneNumber;


public class DashboardActivity extends AppCompatActivity {


    ImageView add,view,passbook,orders;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        getSupportActionBar().setTitle("डैशबोर्ड");

        tv=findViewById(R.id.tv);
        add=findViewById(R.id.add);
        view=findViewById(R.id.view);
        orders=findViewById(R.id.orders);
        passbook=findViewById(R.id.passbook);


        FirebaseDatabase.getInstance().getReference().child("users").child("farmers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
//                    Log.e("Dashboard",ds.child("email").toString());
                    if(ds.child("email").getValue().toString().equals(mAuth.getCurrentUser().getEmail())){
                        tv.setText("आपका स्वागत है "+ds.child("name").getValue().toString());
                        phoneNumber=ds.getKey().toString();
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,AddActivity.class));
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,ViewActivity.class));
            }
        });

        orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,OrderActivity.class));
            }
        });

        passbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DashboardActivity.this,PassbookActivity.class));
            }
        });


    }
}
