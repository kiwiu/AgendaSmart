package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.agendasmart.R;

public class LobbyActivity extends AppCompatActivity {

    ImageButton acercade, perfil, configuracion;

    Dialog acercadeVista;

    LinearLayout btn_agregar_tarea, btn_mis_tareas, btn_tareas_importantes, btn_contactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        acercade = (ImageButton) findViewById(R.id.btn_acerca_de);
        perfil = (ImageButton) findViewById(R.id.btn_perfil);
        configuracion = (ImageButton) findViewById(R.id.btn_configuracion);

        btn_agregar_tarea = (LinearLayout) findViewById(R.id.btn_agregar_tarea);
        btn_mis_tareas = (LinearLayout) findViewById(R.id.btn_mis_tareas);
        btn_tareas_importantes = (LinearLayout) findViewById(R.id.btn_tareas_importantes);
        btn_contactos = (LinearLayout) findViewById(R.id.btn_contactos);
        acercadeVista = new Dialog(this);

        acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acercade();
            }
        });

        perfil.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, ActualizarPerfilActivity.class)));
        configuracion.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, EliminarCuentaActivity.class)));

        btn_agregar_tarea.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, AgregarTareaActivity.class)));
        btn_mis_tareas.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, MisTareasActivity.class)));
        btn_tareas_importantes.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, TareasImportantesActivity.class)));
        btn_contactos.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, TareasImportantesActivity.class)));
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