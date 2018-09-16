package com.example.rohangoyal2014.agrokart;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        switch (id){
            case R.id.sign_out:
                mAuth.signOut();
                startActivity(new Intent(this,MainActivity.class));
                finish();
            case R.id.analytics:
                startActivity(new Intent(this,TrendingActivity.class));
//                finish();
        }
        return true;
    }

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
