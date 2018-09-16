package com.example.rohangoyal2014.agrokart;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class PassbookActivity extends AppCompatActivity {

    TextView balanceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passbook);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        balanceView=findViewById(R.id.balance);

        balanceView.setText("₹ 2500/-");

    }
}
