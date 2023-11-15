package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.agendasmart.Objetos.Tarea;
import com.example.agendasmart.R;
import com.example.agendasmart.ViewHolder.ViewHolder_Tarea;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import es.dmoral.toasty.Toasty;

public class MisTareasActivity extends AppCompatActivity {

    ImageButton btnBack, Vaciar_Todas_Las_Tareas, Filtrar_Tarea;
    RecyclerView recyclerViewTareas;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference Base_De_Datos;

    LinearLayoutManager linearLayoutManager;

    FirebaseRecyclerAdapter<Tarea, ViewHolder_Tarea> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Tarea> options;

    FirebaseAuth auth;
    FirebaseUser user;

    FirebaseAuth firebaseAuth;

    Dialog dialog, dialog_filtrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_tareas);

        recyclerViewTareas = findViewById(R.id.recyclerViewTareas);
        recyclerViewTareas.setHasFixedSize(true);/*Adaptarse a nuevos elementos, osea se apadta*/

        auth  = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        firebaseAuth = FirebaseAuth.getInstance();

        if (user != null) {
            // Imprimir el UID del usuario en el Logcat
            Log.d("UID_DEBUG", "UID del usuario actual: " + user.getUid());
        } else {
            Log.e("UID_DEBUG", "El usuario es nulo");
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        Base_De_Datos = firebaseDatabase.getReference("Tareas_publicadas");
        dialog = new Dialog(MisTareasActivity.this);
        ListarTareasUsuarios();

        Vaciar_Todas_Las_Tareas = (ImageButton) findViewById(R.id.Vaciar_Todas_Las_Tareas);
        Filtrar_Tarea = (ImageButton) findViewById(R.id.Filtrar_Tarea);

        dialog_filtrar = new Dialog(MisTareasActivity.this);

        btnBack = (ImageButton) findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MisTareasActivity.super.onBackPressed();

            }
        });

        Vaciar_Todas_Las_Tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                VaciarRegistroDeTareas();

            }
        });

        Filtrar_Tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FiltrarTareas();

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
                        //Obtener los datos de la nota seleccionada
                        String id_tarea = getItem(position).getId_tarea();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_horaactual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_nota = getItem(position).getFecha_nota();
                        String estado = getItem(position).getEstado();

                        /*enviar datos a la siguiente actividad*/
                        Intent intent = new Intent(MisTareasActivity.this, DetalleTareaActivity.class);
                        intent.putExtra("id_tarea", id_tarea);
                        intent.putExtra("uid_usuario", uid_usuario);
                        intent.putExtra("correo_usuario", correo_usuario);
                        intent.putExtra("fecha_registro", fecha_registro);
                        intent.putExtra("titulo", titulo);
                        intent.putExtra("descripcion", descripcion);
                        intent.putExtra("fecha_nota", fecha_nota);
                        intent.putExtra("estado", estado);
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        //Obtener los datos de la nota seleccionada
                        String id_tarea = getItem(position).getId_tarea();
                        String uid_usuario = getItem(position).getUid_usuario();
                        String correo_usuario = getItem(position).getCorreo_usuario();
                        String fecha_registro = getItem(position).getFecha_horaactual();
                        String titulo = getItem(position).getTitulo();
                        String descripcion = getItem(position).getDescripcion();
                        String fecha_nota = getItem(position).getFecha_nota();
                        String estado = getItem(position).getEstado();


                        Button Eliminar, Actualizar;
                        ImageButton btnEliminar;


                        //Realizar la conexión con el diseño
                        dialog.setContentView(R.layout.cuadro_dialogo_opciones);

                        //Inicializar las vistas
                        Eliminar = dialog.findViewById(R.id.Eliminar);
                        Actualizar = dialog.findViewById(R.id.Actualizar);


                        Eliminar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                EliminarNota(id_tarea);
                                dialog.dismiss();
                            }
                        });

                        Actualizar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                //Toast.makeText(MisTareasActivity.this, "Actualizar Tarea", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MisTareasActivity.this, ActualizarTareaActivity.class);
                                intent.putExtra("id_tarea", id_tarea);
                                intent.putExtra("uid_usuario", uid_usuario);
                                intent.putExtra("correo_usuario", correo_usuario);
                                intent.putExtra("fecha_registro", fecha_registro);
                                intent.putExtra("titulo", titulo);
                                intent.putExtra("descripcion", descripcion);
                                intent.putExtra("fecha_nota", fecha_nota);
                                intent.putExtra("estado", estado);
                                startActivity(intent);
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
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

    private void EliminarNota(String id_tarea) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MisTareasActivity.this);
        builder.setTitle("Eliminar Tarea");
        builder.setMessage("¿Desea Eliminar la Tarea, si la tarea es importante tambien se eliminara?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //ELIMINAR NOTA EN BD
                Query query = Base_De_Datos.orderByChild("id_tarea").equalTo(id_tarea);

                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toasty.error(MisTareasActivity.this, "Tarea eliminada", Toasty.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toasty.error(MisTareasActivity.this, error.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                });

                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
                reference.child(firebaseAuth.getUid()).child("Mis tareas importantes").child(id_tarea)
                        .removeValue()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                //Toasty.info(MisTareasActivity.this, "La tarea ya no es importante", Toasty.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MisTareasActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toasty.info(MisTareasActivity.this, "Cancelado", Toasty.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void FiltrarTareas() {
        Button Todas_Tareas, Tareas_Finalizadas, Tareas_No_Finalizadas;

        dialog_filtrar.setContentView(R.layout.cuadro_dialogo_filtrar_tareas);

        Todas_Tareas = dialog_filtrar.findViewById(R.id.Todas_Tareas);
        Tareas_Finalizadas = dialog_filtrar.findViewById(R.id.Tareas_Finalizadas);
        Tareas_No_Finalizadas = dialog_filtrar.findViewById(R.id.Tareas_No_Finalizadas);

        Todas_Tareas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(MisTareasActivity.this, "Todas las tareas", Toasty.LENGTH_SHORT).show();

            }
        });

        Tareas_Finalizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(MisTareasActivity.this, "Tareas finalizadas", Toasty.LENGTH_SHORT).show();

            }
        });

        Tareas_No_Finalizadas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(MisTareasActivity.this, "Tareas no finalizadas", Toasty.LENGTH_SHORT).show();

            }
        });

        dialog_filtrar.show();
    }

    private void VaciarRegistroDeTareas() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MisTareasActivity.this);
        builder.setTitle("Vaciar todos los registros");
        builder.setMessage("¿Estas seguro de eliminar todas las tareas, las tareas marcadas como importantes tambien se eliminaran?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //Eliminar todas las tareas
                Query query = Base_De_Datos.orderByChild("uid_usuario").equalTo(user.getUid());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Usuarios");
                            reference.child(firebaseAuth.getUid()).child("Mis tareas importantes")
                                    .removeValue()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            //Toasty.info(MisTareasActivity.this, "Las tareas importante tambien se han eliminado", Toasty.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(MisTareasActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                        Toasty.error(MisTareasActivity.this, "Todas las tareas se han eliminado correctamente", Toasty.LENGTH_SHORT).show();
                        Toasty.info(MisTareasActivity.this, "Las tareas importante tambien se han eliminado", Toasty.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toasty.info(MisTareasActivity.this, "Cancelado", Toasty.LENGTH_SHORT).show();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(firebaseRecyclerAdapter!=null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}