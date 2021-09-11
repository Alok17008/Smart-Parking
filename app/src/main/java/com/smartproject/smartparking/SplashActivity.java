package com.smartproject.smartparking;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new  Runnable() {
            @Override
            public void run() {
                finish();
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        },2000);
    }
}