package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agendasmart.MainActivity;
import com.example.agendasmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class InicioSesionActivity extends AppCompatActivity {

    EditText Correo, Contraseña;

    ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    //Validar los datos
    String correo = "" , contraseña = "";

    Button btnIniciarSesion, btnRegistrarse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Iniciar Secion");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        Correo = findViewById(R.id.Correo);
        Contraseña = findViewById(R.id.Contraseña);

        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);


        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(InicioSesionActivity.this);
        progressDialog.setTitle("Espere por favor");
        progressDialog.setCanceledOnTouchOutside(false);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginDeUsuario();
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InicioSesionActivity.this, RegistroActivity.class));
            }
        });

        //btnIniciarSesion.setOnClickListener( v -> startActivity(new Intent(InicioSesionActivity.this, LobbyActivity.class)));
        // btnRegistrarse.setOnClickListener( v -> startActivity(new Intent(InicioSesionActivity.this, RegistroActivity.class)));
    }

    private void LoginDeUsuario() {
        progressDialog.setMessage("Iniciando sesión ...");
        progressDialog.show();
        firebaseAuth.signInWithEmailAndPassword(correo,contraseña)
                .addOnCompleteListener(InicioSesionActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(InicioSesionActivity.this,LobbyActivity.class));
                            Toast.makeText(InicioSesionActivity.this, "Bienvenido(a): "+user.getEmail(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            progressDialog.dismiss();
                            Toast.makeText(InicioSesionActivity.this, "Verifique si el correo y contraseña son los correctos", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(InicioSesionActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}