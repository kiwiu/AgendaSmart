package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.agendasmart.R;

public class MisContactosActivity extends AppCompatActivity {

    LinearLayout linearLayoutBotones, linearLayoutBuscar;
    SearchView searchView;

    ImageButton btnAgregarContacto, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_contactos);

        linearLayoutBotones = findViewById(R.id.linearLayoutBotones);
        linearLayoutBuscar = findViewById(R.id.linearLayoutSearch);
        searchView = findViewById(R.id.searchView);
        btnAgregarContacto = findViewById(R.id.btn_agregar_contacto);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> MisContactosActivity.super.onBackPressed());

        ImageButton imageButtonBuscar = findViewById(R.id.btn_buscar);

        imageButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutBotones.setVisibility(View.GONE);
                linearLayoutBuscar.setVisibility(View.VISIBLE);
                searchView.requestFocus();
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                linearLayoutBotones.setVisibility(View.VISIBLE);
                linearLayoutBuscar.setVisibility(View.GONE);
                return false;
            }
        });

        btnAgregarContacto.setOnClickListener( v -> startActivity(new Intent(MisContactosActivity.this, AgregarContactoActivity.class)));

    }
}