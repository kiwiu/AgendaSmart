package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendasmart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CambiarContrasenaActivity extends AppCompatActivity {

    TextView textView;
    EditText actualpass, newpass, confirmpass;
    Button btnActualizar;

    DatabaseReference BD_Usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    ProgressDialog progressDialog;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambiar_contrasena);

        InicializarVariables();
        LecturaDatos();

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String actualPass = actualpass.getText().toString().trim();
                String newPass = newpass.getText().toString().trim();
                String confirmPass = confirmpass.getText().toString().trim();

                if(actualPass.equals("")){
                    actualpass.setError("Campo requerido");
                } else if (newPass.equals("")) {
                    newpass.setError("Campo requerido");
                } else if (confirmPass.equals("")) {
                    confirmpass.setError("Campo requerido");
                } else if (!newPass.equals(confirmPass)) {
                    Toast.makeText(CambiarContrasenaActivity.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                } else if (newPass.length()<6) {
                    newpass.setError("La contraseña debe tener al menos 6 caracteres");
                }else {
                    Actualizar_password(actualPass, newPass);
                }
            }
        });

        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CambiarContrasenaActivity.super.onBackPressed();
            }
        });
    }

    private void Actualizar_password(String actualPass, String newPass) {
        progressDialog.show();
        progressDialog.setTitle("Actualizando contraseña");
        progressDialog.setMessage("Por favor espere");

        AuthCredential authCredential = EmailAuthProvider.getCredential(user.getEmail(), actualPass);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                user.updatePassword(newPass)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        String Nuevo_pass = newpass.getText().toString().trim();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("password", Nuevo_pass);
                        BD_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
                        BD_Usuarios.child(user.getUid()).updateChildren(hashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(CambiarContrasenaActivity.this, "Contraseña actualizada", Toast.LENGTH_SHORT).show();
                                        firebaseAuth.signOut();
                                        Intent intent = new Intent(CambiarContrasenaActivity.this, InicioSesionActivity.class)
                                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(CambiarContrasenaActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CambiarContrasenaActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CambiarContrasenaActivity.this, "La contraseña actual no es la correcta", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void InicializarVariables(){
        textView = findViewById(R.id.textView);
        actualpass = findViewById(R.id.actualpass);
        newpass = findViewById(R.id.newpass);
        confirmpass = findViewById(R.id.confirmpass);
        btnActualizar = findViewById(R.id.btnActualizar);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        progressDialog = new ProgressDialog(this);
    }

    private void LecturaDatos(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference( "Usuarios" );
        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String pass = ""+ snapshot.child("password").getValue();
                textView.setText(pass);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}