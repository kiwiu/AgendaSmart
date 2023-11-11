package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.agendasmart.R;

public class ActualizarPerfilActivity extends AppCompatActivity {

    ImageButton btnBack, btnActualizarContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnActualizarContrasena = (ImageButton) findViewById(R.id.btnActualizarContraseña);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarPerfilActivity.super.onBackPressed();
            }
        });

        btnActualizarContrasena.setOnClickListener( v -> startActivity(new Intent(ActualizarPerfilActivity.this, CambiarContrasenaActivity.class)));

    }
}