package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageButton;

import com.example.agendasmart.R;

public class AgregarContactoActivity extends AppCompatActivity {

    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> AgregarContactoActivity.super.onBackPressed());

    }
}