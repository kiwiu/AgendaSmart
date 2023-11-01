package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.agendasmart.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        int Tiempo = 2000;

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                android.content.Intent intent = new android.content.Intent(SplashActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();
            }
        }, Tiempo);
    }
}