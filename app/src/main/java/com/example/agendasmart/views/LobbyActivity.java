package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import es.dmoral.toasty.Toasty;

public class LobbyActivity extends AppCompatActivity {

    ImageButton acercade, perfil, configuracion, salir;
    ImageView iv_foto_perfil;

    TextView tv_nombre_usuario,tv_correo_usuario, tv_ui;
    Button btn_Estado;
    ProgressDialog progressDialog;
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
        iv_foto_perfil = findViewById(R.id.iv_foto_perfil);
        tv_ui =  findViewById(R.id.tv_uid);
        btn_Estado = findViewById(R.id.btn_Estado);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setCanceledOnTouchOutside(false);

        Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        btn_Estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.isEmailVerified()) {
                    //si la cuenta esta verificada
                    Toasty.success(LobbyActivity.this, "Tu cuenta ya esta verificada", Toasty.LENGTH_SHORT).show();
                } else {
                    //si la cuenta no esta verificada
                    VerificarCuenta();
                }
            }
        });

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
        configuracion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*esto e para obtener el texto de lo textview*/
                String uid_usuario = tv_ui.getText().toString();
                String correo_usuario = tv_correo_usuario.getText().toString();

                /*pasar los datos a la actividad de agregar_tarea*/
                Intent intent = new Intent(LobbyActivity.this, EliminarCuentaActivity.class);
                intent.putExtra("uid", uid_usuario);
                intent.putExtra("correo", correo_usuario);
                startActivity(intent);
            }
        });


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
        btn_contactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*esto e para obtener el texto de lo textview*/
                String uid_usuarioR = tv_ui.getText().toString();

                /*pasar los datos a la actividad de MisContactosActivity*/
                Intent intent = new Intent(LobbyActivity.this, MisContactosActivity.class);
                intent.putExtra("uid", uid_usuarioR);
                startActivity(intent);
            }
        });
    }

    private void VerificarCuenta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LobbyActivity.this);
        builder.setTitle("Verificar cuenta").setMessage("Estas seguro(a) de verificar tu cuenta? " + user.getEmail())
                .setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EnviarEmailVerificacion();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.info(LobbyActivity.this, "Operación cancelada", Toasty.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void EnviarEmailVerificacion() {
        progressDialog.setMessage("Enviando correo de verificación " + user.getEmail());
        progressDialog.show();

        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //correo enviado
                progressDialog.dismiss();
                Toasty.success(LobbyActivity.this, "Correo de verificación enviado a " + user.getEmail(), Toasty.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                    //error al enviar el correo
                progressDialog.dismiss();
                 Toasty.error(LobbyActivity.this, "Error al enviar el correo de verificación", Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void VerificarEstado() {
        String verificado = "Verificado";
        String No_verificado = "No verificado";

        Drawable icono = getResources().getDrawable(R.drawable.email_verificado);
        Drawable icono2 = getResources().getDrawable(R.drawable.mail_error);


        if (user.isEmailVerified()){
            btn_Estado.setText(verificado);
            btn_Estado.setBackgroundResource(R.drawable.btn_inicio);
            btn_Estado.setCompoundDrawablesWithIntrinsicBounds(icono, null, null, null);
        }else{
            btn_Estado.setText(No_verificado);
            btn_Estado.setBackgroundResource(R.drawable.btn_option_2);
            btn_Estado.setCompoundDrawablesWithIntrinsicBounds(icono2, null, null, null);
            ;}
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
        //obtenemos el estado de VERIFICACION de la cuenta
        VerificarEstado();
        Usuarios.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    //obtenemos los datos del usuario
                    String uid = "" + snapshot.child("uid").getValue();
                    String nombre = "" + snapshot.child("nombres").getValue();
                    String correo = "" + snapshot.child("correo").getValue();
                    String imagen = "" + snapshot.child("imagen").getValue();

                    //asignamos los datos a los campos
                    tv_ui.setText(uid);
                    tv_nombre_usuario.setText(nombre);
                    tv_correo_usuario.setText(correo);

                    //cargamos la imagen
                    ObtenerImagen(imagen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ObtenerImagen(String imagen) {
        try {
            //cargamos la imagen
            Glide.with(getApplicationContext()).load(imagen).placeholder(R.drawable.image).into(iv_foto_perfil);
        }catch (Exception e){
            //si hay error cargamos la imagen por defecto
            Glide.with(getApplicationContext()).load(R.drawable.image).into(iv_foto_perfil);
        }
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