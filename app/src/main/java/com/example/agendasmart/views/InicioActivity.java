package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.agendasmart.R;

public class InicioActivity extends AppCompatActivity {
    Button btnIniciarSesion, btnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio);

        btnIniciarSesion = findViewById(R.id.btnIngresarInicio);
        btnRegistrarse = findViewById(R.id.btnRegistrarInicio);

        btnIniciarSesion.setOnClickListener(v -> {
            startActivity(new Intent(InicioActivity.this, InicioSesionActivity.class));
            finish();
        });

        btnRegistrarse.setOnClickListener(v -> {
            startActivity(new Intent(InicioActivity.this, RegistroActivity.class));
            finish();
        });
    }
}