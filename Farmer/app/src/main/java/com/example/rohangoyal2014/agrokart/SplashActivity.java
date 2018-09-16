package com.example.rohangoyal2014.agrokart;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences=getSharedPreferences("serviceState",MODE_PRIVATE);
                boolean res=sharedPreferences.getBoolean("state",false);
                if(!res){

                    startService(new Intent(SplashActivity.this,AddtionSyncService.class));

                }


                startActivity(new Intent(SplashActivity.this,MainActivity.class));
                finish();
            }
        },2000);

    }
}
