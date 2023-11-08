package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.agendasmart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        firebaseAuth = FirebaseAuth.getInstance();

        int Tiempo = 2000;

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*android.content.Intent intent = new android.content.Intent(SplashActivity.this, InicioActivity.class);
                startActivity(intent);
                finish();*/
                verificarUser();
            }
        }, Tiempo);
    }

    private void verificarUser() {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(SplashActivity.this, InicioActivity.class));
            finish();
        }else{
            startActivity(new Intent(SplashActivity.this, LobbyActivity.class));
            finish();
        }
    }
}