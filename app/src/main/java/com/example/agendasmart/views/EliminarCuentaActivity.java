package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.agendasmart.R;

public class EliminarCuentaActivity extends AppCompatActivity {

    ImageButton btnBack;
    Button btnCancelar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cuenta);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarCuentaActivity.super.onBackPressed();
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarCuentaActivity.super.onBackPressed();
            }
        });
    }
}