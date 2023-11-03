package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.agendasmart.R;

public class LobbyActivity extends AppCompatActivity {

    ImageButton acercade, perfil;

    Dialog acercadeVista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        acercade = (ImageButton) findViewById(R.id.btn_acerca_de);
        perfil = (ImageButton) findViewById(R.id.btn_perfil);
        acercadeVista = new Dialog(this);

        acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acercade();
            }
        });

        perfil.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, ActualizarPerfilActivity.class)));
    }

    private void acercade() {
        Button entendido;

        acercadeVista.setContentView(R.layout.acerca_de);
        entendido = (Button) acercadeVista.findViewById(R.id.btn_entendido);

        entendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acercadeVista.dismiss();
            }
        });

        acercadeVista.show();
        acercadeVista.setCanceledOnTouchOutside(false);
    }
}