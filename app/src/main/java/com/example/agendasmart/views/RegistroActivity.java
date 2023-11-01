package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.agendasmart.R;

public class RegistroActivity extends AppCompatActivity {

    TextView inicioSesion;
    Button btnRegistrarse, btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        inicioSesion = findViewById(R.id.txtIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrar);
        btnCancelar = findViewById(R.id.btnCancelar);

        inicioSesion.setOnClickListener( v -> startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class)));
        btnRegistrarse.setOnClickListener( v -> startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class)));
        btnCancelar.setOnClickListener( v -> startActivity(new Intent(RegistroActivity.this, InicioActivity.class)));
    }
}