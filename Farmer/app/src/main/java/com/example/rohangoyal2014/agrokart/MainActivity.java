package com.example.rohangoyal2014.agrokart;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.rohangoyal2014.agrokart.Utils.mAuth;

public class MainActivity extends AppCompatActivity {

    LoginFragmentAdapter loginFragmentAdapter;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager=findViewById(R.id.viewpager);
        loginFragmentAdapter=new LoginFragmentAdapter(getSupportFragmentManager());
        viewPager.setAdapter(loginFragmentAdapter);
        viewPager.beginFakeDrag();

        mAuth=FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser()!=null){

            startActivity(new Intent(this,DashboardActivity.class));
            finish();

        }

    }

    @Override
    public void onBackPressed() {

        if(viewPager.getCurrentItem()==1){
            viewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }
}
