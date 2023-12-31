package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendasmart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class DetalleTareaActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Button Boton_Importante;

    TextView Id_tarea_Detalle, Uid_usuario_Detalle, Correo_usuario_Detalle, Titulo_Detalle, Descripcion_Detalle,
            Fecha_Registro_Detalle, Fecha_Nota_Detalle, Estado_Detalle;

    String id_tarea_R, uid_usuario_R, correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    boolean ComprobarNotaImportante = false;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_tarea);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Detalle de tarea");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }

        InicializarVistas();
        RecuperarDatos();
        SetearDatosRecuperados();
        VerificarTareaImportante();

        Boton_Importante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ComprobarNotaImportante){
                    Eliminar_Tarea_Importante();
                }else {
                    Agregar_Tareas_Importantes();
                }
            }
        });

        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetalleTareaActivity.super.onBackPressed();
            }
        });
    }
    private void InicializarVistas(){
        Id_tarea_Detalle = findViewById(R.id.Id_tarea_Detalle);
        Uid_usuario_Detalle = findViewById(R.id.Uid_usuario_Detalle);
        Correo_usuario_Detalle = findViewById(R.id.Correo_usuario_Detalle);
        Titulo_Detalle = findViewById(R.id.Titulo_Detalle);
        Descripcion_Detalle = findViewById(R.id.Descripcion_Detalle);
        Fecha_Registro_Detalle = findViewById(R.id.Fecha_Registro_Detalle);
        Fecha_Nota_Detalle = findViewById(R.id.Fecha_Nota_Detalle);
        Estado_Detalle = findViewById(R.id.Estado_Detalle);
        Boton_Importante = findViewById(R.id.Boton_Importante);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    private void RecuperarDatos(){
        Bundle intent = getIntent().getExtras();

        id_tarea_R = intent.getString("id_tarea");
        uid_usuario_R = intent.getString("uid_usuario");
        correo_usuario_R = intent.getString("correo_usuario");
        fecha_registro_R = intent.getString("fecha_registro");
        titulo_R = intent.getString("titulo");
        descripcion_R = intent.getString("descripcion");
        fecha_R = intent.getString("fecha_nota");
        estado_R = intent.getString("estado");

    }

    private void SetearDatosRecuperados(){
        Id_tarea_Detalle.setText(id_tarea_R);
        Uid_usuario_Detalle.setText(uid_usuario_R);
        Correo_usuario_Detalle.setText(correo_usuario_R);
        Fecha_Registro_Detalle.setText(fecha_registro_R);
        Titulo_Detalle.setText(titulo_R);
        Descripcion_Detalle.setText(descripcion_R);
        Fecha_Nota_Detalle.setText(fecha_R);
        Estado_Detalle.setText(estado_R);
    }

   private void Agregar_Tareas_Importantes(){
        if (user == null){
            Toasty.error(DetalleTareaActivity.this, "Error", Toasty.LENGTH_SHORT).show();
        }else {
            /*obtener datos de la tarea de la actividad anterior*/
            Bundle intent = getIntent().getExtras();

            id_tarea_R = intent.getString("id_tarea");
            uid_usuario_R = intent.getString("uid_usuario");
            correo_usuario_R = intent.getString("correo_usuario");
            fecha_registro_R = intent.getString("fecha_registro");
            titulo_R = intent.getString("titulo");
            descripcion_R = intent.getString("descripcion");
            fecha_R = intent.getString("fecha_nota");
            estado_R = intent.getString("estado");

            HashMap<String , String> Nota_Importante = new HashMap<>();
            Nota_Importante.put("id_tarea", id_tarea_R);
            Nota_Importante.put("uid_usuario", uid_usuario_R);
            Nota_Importante.put("correo_usuario", correo_usuario_R);
            Nota_Importante.put("fecha_registro", fecha_registro_R);
            Nota_Importante.put("titulo", titulo_R);
            Nota_Importante.put("descripcion", descripcion_R);
            Nota_Importante.put("fecha_nota", fecha_R);
            Nota_Importante.put("estado", estado_R);


            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis tareas importantes").child(id_tarea_R)
                    .setValue(Nota_Importante)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toasty.info(DetalleTareaActivity.this, "Se ha añadido a tareas importantes", Toasty.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetalleTareaActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void Eliminar_Tarea_Importante(){
        if (user == null){
            Toasty.error(DetalleTareaActivity.this, "Ha ocurrido un error", Toasty.LENGTH_SHORT).show();
        }else {
            Bundle intent = getIntent().getExtras();
            id_tarea_R = intent.getString("id_tarea");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis tareas importantes").child(id_tarea_R)
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toasty.info(DetalleTareaActivity.this, "La tarea ya no es importante", Toasty.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(DetalleTareaActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void VerificarTareaImportante(){
        if (user == null){
            Toasty.error(DetalleTareaActivity.this, "Error", Toasty.LENGTH_SHORT).show();
        }else {
            Bundle intent = getIntent().getExtras();
            id_tarea_R = intent.getString("id_tarea");

            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
            reference.child(firebaseAuth.getUid()).child("Mis tareas importantes").child(id_tarea_R)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            ComprobarNotaImportante = snapshot.exists();
                            if (ComprobarNotaImportante){
                                String importante = "Importante";
                                Boton_Importante.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.importantee, 0 , 0);
                                Boton_Importante.setText(importante);
                            }else {
                                String no_importante = "No importante";
                                Boton_Importante.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.no_importante, 0 , 0);
                                Boton_Importante.setText(no_importante);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }
}