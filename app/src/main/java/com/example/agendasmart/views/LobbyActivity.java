package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendasmart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class LobbyActivity extends AppCompatActivity {

    ImageButton acercade, perfil, configuracion, salir;

    TextView tv_nombre_usuario,tv_correo_usuario, tv_ui;

    DatabaseReference Usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Dialog acercadeVista;

    LinearLayout btn_agregar_tarea, btn_mis_tareas, btn_tareas_importantes, btn_contactos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);


        tv_nombre_usuario = findViewById(R.id.tv_nombre_usuario);
        tv_correo_usuario = findViewById(R.id.tv_correo_usuario);
        tv_ui =  findViewById(R.id.tv_uid);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        acercade = (ImageButton) findViewById(R.id.btn_acerca_de);
        perfil = (ImageButton) findViewById(R.id.btn_perfil);
        configuracion = (ImageButton) findViewById(R.id.btn_configuracion);
        salir = (ImageButton) findViewById(R.id.btn_salir);

        btn_agregar_tarea = (LinearLayout) findViewById(R.id.btn_agregar_tarea);
        btn_mis_tareas = (LinearLayout) findViewById(R.id.btn_mis_tareas);
        btn_tareas_importantes = (LinearLayout) findViewById(R.id.btn_tareas_importantes);
        btn_contactos = (LinearLayout) findViewById(R.id.btn_contactos);
        acercadeVista = new Dialog(this);

       salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SalirApp();
            }
        });
        acercade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                acercade();
            }
        });

        perfil.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, ActualizarPerfilActivity.class)));
        configuracion.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, EliminarCuentaActivity.class)));


        btn_agregar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*esto e para obtener el texto de lo textview*/
                String uid_usuario = tv_ui.getText().toString();
                String correo_usuario = tv_correo_usuario.getText().toString();

                /*pasar los datos a la actividad de agregar_tarea*/
                Intent intent = new Intent(LobbyActivity.this, AgregarTareaActivity.class);
                intent.putExtra("uid", uid_usuario);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });

        btn_mis_tareas.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, MisTareasActivity.class)));
        btn_tareas_importantes.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, TareasImportantesActivity.class)));
        btn_contactos.setOnClickListener( v -> startActivity(new Intent(LobbyActivity.this, TareasImportantesActivity.class)));
    }

    private void SalirApp() {
        firebaseAuth.signOut();
        Toasty.success(LobbyActivity.this, "Sesión cerrada correctamente", Toasty.LENGTH_SHORT).show();
        startActivity(new Intent(LobbyActivity.this, InicioActivity.class));
        finish();
    }

    @Override
    protected void onStart() {
        InicioSesion();
        super.onStart();
    }

    private void InicioSesion() {
        if (user!=null){
            CargaDeDatos();
        }else{
            startActivity(new Intent(LobbyActivity.this, InicioActivity.class));
            finish();
        }
    }

    private void CargaDeDatos() {
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String uid = "" + snapshot.child("uid").getValue();
                    String nombre = "" + snapshot.child("nombres").getValue();
                    String correo = "" + snapshot.child("correo").getValue();

                    tv_ui.setText(uid);
                    tv_nombre_usuario.setText(nombre);
                    tv_correo_usuario.setText(correo);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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