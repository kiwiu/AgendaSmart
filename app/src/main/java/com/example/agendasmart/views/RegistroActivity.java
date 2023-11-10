package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendasmart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class RegistroActivity extends AppCompatActivity {

    TextView inicioSesion;
    Button btnRegistrarse, btnCancelar;

    EditText Nombre, Correo, Contraseña, ConfirmarContraseña;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;

    String nombre = " " , correo = " ", contraseña = "" , confirmarcontraseña = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Registrar");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }


        inicioSesion = findViewById(R.id.txtIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrar);
        btnCancelar = findViewById(R.id.btnCancelar);

        Nombre = findViewById(R.id.Nombre);
        Correo = findViewById(R.id.Correo);
        Contraseña = findViewById(R.id.Contraseña);
        ConfirmarContraseña = findViewById(R.id.ConfirmarContraseña);

        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(RegistroActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidarDatos();
            }
        });

        inicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
            }
        });

        //inicioSesion.setOnClickListener( v -> startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class)));
        //btnRegistrarse.setOnClickListener( v -> startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class)));
        btnCancelar.setOnClickListener( v -> startActivity(new Intent(RegistroActivity.this, InicioActivity.class)));
    }

    private void ValidarDatos(){
        nombre = Nombre.getText().toString();
        correo = Correo.getText().toString();
        contraseña = Contraseña.getText().toString();
        confirmarcontraseña = ConfirmarContraseña.getText().toString();

        if (TextUtils.isEmpty(nombre)){
            Toasty.error(this, "Ingrese nombre", Toast.LENGTH_SHORT).show();
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()){
            Toasty.error(this, "Ingrese correo", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(contraseña)){
            Toasty.error(this, "Ingrese contraseña", Toast.LENGTH_SHORT).show();

        }
        else if (TextUtils.isEmpty(confirmarcontraseña)){
            Toasty.error(this, "Confirme contraseña", Toast.LENGTH_SHORT).show();

        }
        else if (!contraseña.equals(confirmarcontraseña)){
            Toasty.error(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
        }
        else {
            CrearCuenta();
        }
    }

    private void CrearCuenta() {
        progressDialog.setMessage("Creando su cuenta...");
        progressDialog.show();

        //Crear un usuario en Firebase
        firebaseAuth.createUserWithEmailAndPassword(correo, contraseña)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        //
                        GuardarInformacion();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toasty.error(RegistroActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void GuardarInformacion() {
        progressDialog.setMessage("Guardando su información");
        progressDialog.dismiss();

        //Obtener la identificación de usuario actual
        String uid = firebaseAuth.getUid();

        HashMap<String, String> Datos = new HashMap<>();
        Datos.put("uid",  uid);
        Datos.put("correo", correo);
        Datos.put("nombres", nombre);
        Datos.put("password", contraseña);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(uid)
                .setValue(Datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toasty.success(RegistroActivity.this, "Cuenta creada con éxito", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegistroActivity.this, InicioSesionActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        progressDialog.dismiss();
                        Toasty.error(RegistroActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}