package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.agendasmart.Objetos.Tarea;
import com.example.agendasmart.R;
import com.example.agendasmart.ViewHolder.ViewHolder_Tarea;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;

import es.dmoral.toasty.Toasty;

public class MisTareasActivity extends AppCompatActivity {

    ImageButton btnBack;
    RecyclerView recyclerViewTareas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference Base_De_Datos;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Tarea, ViewHolder_Tarea> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Tarea> options;

    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_tareas);

        recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setHasFixedSize(true);/*Adaptarse a nuevos elementos, osea se apadta*/

        auth  = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        if (user != null) {
            // Imprimir el UID del usuario en el Logcat
            Log.d("UID_DEBUG", "UID del usuario actual: " + user.getUid());
        } else {
            Log.e("UID_DEBUG", "El usuario es nulo");
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        Base_De_Datos = firebaseDatabase.getReference("Tareas_publicadas");

        ListarTareasUsuarios();


        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MisTareasActivity.super.onBackPressed();

            }
        });
    }

    private void ListarTareasUsuarios(){

        Query query = Base_De_Datos.orderByChild("uid_usuario").equalTo(user.getUid());
        options = new FirebaseRecyclerOptions.Builder<Tarea>().setQuery(query, Tarea.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Tarea, ViewHolder_Tarea>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder_Tarea viewHolder_tarea, int position, @NonNull Tarea tarea) {
                viewHolder_tarea.SetearDatos(
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
            public ViewHolder_Tarea onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tarea, parent, false);
                ViewHolder_Tarea viewHolder_tarea = new ViewHolder_Tarea(view);
                viewHolder_tarea.setOnclickListener(new ViewHolder_Tarea.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Toasty.info(MisTareasActivity.this, "on item click", Toasty.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toasty.info(MisTareasActivity.this, "on item Long click", Toasty.LENGTH_SHORT).show();
                    }
                });
                return viewHolder_tarea;
            }
        };
        linearLayoutManager= new LinearLayoutManager(MisTareasActivity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setReverseLayout(true);/*Listar notas desde la ultima*/
        linearLayoutManager.setStackFromEnd(true);/*empiece desde la parte superior*/

        recyclerViewTareas.setLayoutManager(linearLayoutManager);
        recyclerViewTareas.setAdapter(firebaseRecyclerAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}