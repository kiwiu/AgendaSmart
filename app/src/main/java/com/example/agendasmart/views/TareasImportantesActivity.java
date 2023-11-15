package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.agendasmart.Objetos.Tarea;
import com.example.agendasmart.R;
import com.example.agendasmart.ViewHolder.ViewHolder_Tarea;
import com.example.agendasmart.ViewHolder.ViewHolder_Tarea_Importante;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import es.dmoral.toasty.Toasty;

public class TareasImportantesActivity extends AppCompatActivity {

    RecyclerView RecyclerViewTareasImportantes;
    FirebaseDatabase firebaseDatabase;

    DatabaseReference Mis_Usuarios;
    DatabaseReference Tareas_Importantes;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    FirebaseRecyclerAdapter<Tarea, ViewHolder_Tarea_Importante> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Tarea> firebaseRecyclerOptions;

    LinearLayoutManager linearLayoutManager;

    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tareas_importantes);

        btnBack = (ImageButton) findViewById(R.id.btnBack);
        RecyclerViewTareasImportantes = findViewById(R.id.RecyclerViewTareasImportantes);
        RecyclerViewTareasImportantes.setHasFixedSize(true);
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        Mis_Usuarios = firebaseDatabase.getReference("Usuarios");
        Tareas_Importantes = firebaseDatabase.getReference("Mis tareas importantes");

        ComprobarUsuario();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TareasImportantesActivity.super.onBackPressed();
            }
        });
    }

    private void ComprobarUsuario(){
        if(user == null){
            Toasty.info(TareasImportantesActivity.this,"Ha ocurrido un error", Toasty.LENGTH_SHORT).show();
        }else{
            ListarTareasImportantes();
        }
    }

    private void ListarTareasImportantes() {
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Tarea>().setQuery(Mis_Usuarios.child(user.getUid()).child("Mis tareas importantes"), Tarea.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Tarea, ViewHolder_Tarea_Importante>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Tarea_Importante viewHolder_tarea_impotante, int position, @NonNull Tarea tarea) {
                viewHolder_tarea_impotante.SetearDatos(
                        getApplicationContext(),
                        tarea.getId_tarea(),
                        tarea.getUid_usuario(),
                        tarea.getCorreo_usuario(),
                        tarea.getFecha_horaactual(),
                        tarea.getTitulo(),
                        tarea.getDescripcion(),
                        tarea.getFecha_nota(),
                        tarea.getEstado()

                );
            }

            @Override
            public ViewHolder_Tarea_Importante onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea_importante, parent, false);
                ViewHolder_Tarea_Importante viewHolder_tarea_importante = new ViewHolder_Tarea_Importante(view);
                viewHolder_tarea_importante.setOnclickListener(new ViewHolder_Tarea_Importante.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Obtener los datos de la nota seleccionada
                        String id_tarea = getItem(position).getId_tarea();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_horaactual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_nota = getItem(position).getFecha_nota();
                        String estado = getItem(position).getEstado();

                        Intent intent = new Intent(TareasImportantesActivity.this, DetalleTareaActivity.class);
                        intent.putExtra("id_tarea", id_tarea);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("correo_usuario", correo_usuario);
                        intent.putExtra("fecha_hora_actual", fecha_registro);
                        intent.putExtra("titulo", titulo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("fecha_nota", fecha_nota);
                        intent.putExtra("estado", estado);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {


                    }
                });
                return viewHolder_tarea_importante;
            }
        };



        linearLayoutManager= new LinearLayoutManager(TareasImportantesActivity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);/*Listar notas desde la ultima*/
        linearLayoutManager.setStackFromEnd(true);/*empiece desde la parte superior*/

        RecyclerViewTareasImportantes.setLayoutManager(linearLayoutManager);
        RecyclerViewTareasImportantes.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        if(firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();/*estar pendiente de las notas de la base de datos*/
        }
        super.onStart();
    }
}