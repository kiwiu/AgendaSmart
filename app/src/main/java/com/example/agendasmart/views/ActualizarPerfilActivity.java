package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.agendasmart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class ActualizarPerfilActivity extends AppCompatActivity {

    ImageView imagen_Perfil;
    TextView correo_Perfil;
    EditText nombres_Perfil, apellidos_Perfil, edad_Perfil,
            domicilio_Perfil, profesion_Perfil, telefono_Perfil;
    Button btnActualizarPerfil;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    DatabaseReference Usuarios;

    ImageButton btnBack, btnActualizarContrasena;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil);

        inicializarVariables();

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

    private void inicializarVariables(){
        imagen_Perfil = (ImageView) findViewById(R.id.imagen_Perfil);
        correo_Perfil = (TextView) findViewById(R.id.correo_Perfil);
        nombres_Perfil = (EditText) findViewById(R.id.nombres_Perfil);
        apellidos_Perfil = (EditText) findViewById(R.id.apellidos_Perfil);
        edad_Perfil = (EditText) findViewById(R.id.edad_Perfil);
        domicilio_Perfil = (EditText) findViewById(R.id.domicilio_Perfil);
        profesion_Perfil = (EditText) findViewById(R.id.profesion_Perfil);
        telefono_Perfil = (EditText) findViewById(R.id.telefono_Perfil);

        btnActualizarPerfil = (Button) findViewById(R.id.btnActualizarPerfil);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
    }

    private void lecturaDatos(){
      Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot snapshot) {
              if (snapshot.exists()){
                  //obtenemos los datos del usuario
                    String correo = "" + snapshot.child("correo").getValue();
                    String nombres = "" + snapshot.child("nombres").getValue();
                    String apellidos = "" + snapshot.child("apellidos").getValue();
                    String edad = "" + snapshot.child("edad").getValue();
                    String domicilio = "" + snapshot.child("domicilio").getValue();
                    String profesion = "" + snapshot.child("profesion").getValue();
                    String telefono = "" + snapshot.child("telefono").getValue();
                    String imagen = "" + snapshot.child("imagen").getValue();

                    //asignamos los datos a los campos
                    correo_Perfil.setText(correo);
                    nombres_Perfil.setText(nombres);
                    apellidos_Perfil.setText(apellidos);
                    edad_Perfil.setText(edad);
                    domicilio_Perfil.setText(domicilio);
                    profesion_Perfil.setText(profesion);
                    telefono_Perfil.setText(telefono);

                    cargarImagen(imagen);

              }
              else {
                  Toasty.warning(ActualizarPerfilActivity.this, "Esperando datos", Toasty.LENGTH_SHORT).show();
              }
          }

          @Override
          public void onCancelled(@NonNull DatabaseError error) {
              Toasty.error(ActualizarPerfilActivity.this, " "+error, Toasty.LENGTH_SHORT).show();
          }
      });
    }

    private void cargarImagen(String imagen) {
        try {
            //cargamos la imagen
            Glide.with(getApplicationContext()).load(imagen).placeholder(R.drawable.image).into(imagen_Perfil);
        }catch (Exception e){
            //si hay error cargamos la imagen por defecto
            Glide.with(getApplicationContext()).load(R.drawable.image).into(imagen_Perfil);
        }
    }

    private void comprobarSesion(){
        if (user != null){
            lecturaDatos();
        }
        else {
            startActivity(new Intent(ActualizarPerfilActivity.this, InicioActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        comprobarSesion();
        super.onStart();
    }
}