package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.agendasmart.MainActivity;
import com.example.agendasmart.R;

public class InicioSesionActivity extends AppCompatActivity {

    Button btnIniciarSesion, btnRegistrarse;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);

        btnIniciarSesion.setOnClickListener( v -> startActivity(new Intent(InicioSesionActivity.this, MainActivity.class)));
        btnRegistrarse.setOnClickListener( v -> startActivity(new Intent(InicioSesionActivity.this, RegistroActivity.class)));
    }
}