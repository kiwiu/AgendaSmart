package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendasmart.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class EliminarCuentaActivity extends AppCompatActivity {

    TextView uid_Eliminar, gmail;
    ImageButton btnBack;
    Button btnCancelar, btnEliminarCuenta;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    Dialog dialog_autenticacion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_cuenta);

        InicializarVariables();


        ObtenerUid();

        btnEliminarCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EliminarUsuarioAutenticacion();
            }
        });


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

    private void InicializarVariables(){
        btnBack = (ImageButton) findViewById(R.id.btnBack);
        btnCancelar = (Button) findViewById(R.id.btnCancelar);
        btnEliminarCuenta = (Button) findViewById(R.id.btnEliminarCuenta);
        uid_Eliminar = (TextView) findViewById(R.id.uid_user);
        gmail = (TextView) findViewById(R.id.Gmail);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Usuarios");

        dialog_autenticacion = new Dialog(EliminarCuentaActivity.this);
    }

    private void ObtenerUid(){
        String uid = getIntent().getStringExtra("uid");
        String correo = getIntent().getStringExtra("correo");
        uid_Eliminar.setText(uid);
        gmail.setText(correo);;
    }

    private void EliminarUsuarioAutenticacion() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(EliminarCuentaActivity.this);
        alertDialog.setTitle("Estas seguro de eliminar cuenta?");
        alertDialog.setMessage("Su cuenta se eliminara permanentemente");
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            EliminarUsuarioBD();
                            Intent intent = new Intent(EliminarCuentaActivity.this, InicioSesionActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                            Toasty.info(EliminarCuentaActivity.this, "Se ha eliminado su cuenta con exito", Toast.LENGTH_SHORT).show();
                        }else{
                            Toasty.info(EliminarCuentaActivity.this, "Ha ocurrido un problema", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {

                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toasty.info(EliminarCuentaActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
                        Autenticacion();
                    }
                });
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toasty.info(EliminarCuentaActivity.this, "Cancelado por el usuario", Toast.LENGTH_SHORT).show();

            }
        });

        alertDialog.create().show();
    }

    private void EliminarUsuarioBD(){
        String uid_eliminar = uid_Eliminar.getText().toString();
        Query query = databaseReference.child(uid_eliminar);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().removeValue();
                }
                Toasty.info(EliminarCuentaActivity.this, "Se a eliminado su cuenta", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Autenticacion(){
        Button btn_Entendido_Aut, btn_Cerrar_Sesion_Aut;

        dialog_autenticacion.setContentView(R.layout.cuadro_dialogo_autenticacion);

        btn_Entendido_Aut = dialog_autenticacion.findViewById(R.id.btn_Entendido_Aut);
        btn_Cerrar_Sesion_Aut = dialog_autenticacion.findViewById(R.id.btn_Cerrar_Sesion_Aut);

        btn_Entendido_Aut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_autenticacion.dismiss();
            }
        });

        btn_Cerrar_Sesion_Aut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CerrarSesion();
                dialog_autenticacion.dismiss();
            }
        });

        dialog_autenticacion.show();
        dialog_autenticacion.setCanceledOnTouchOutside(false);/*pa q no se oculte cuando e toca fuera del cuadro de dialogo*/
    }

    private void CerrarSesion() {
        firebaseAuth.signOut();
        startActivity(new Intent(EliminarCuentaActivity.this, InicioSesionActivity.class));
        Toasty.info(EliminarCuentaActivity.this, "Cerraste sesión exitosamente", Toast.LENGTH_SHORT).show();

    }

}