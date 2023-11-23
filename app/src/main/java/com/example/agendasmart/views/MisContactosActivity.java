package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.agendasmart.Objetos.Contacto;
import com.example.agendasmart.R;
import com.example.agendasmart.ViewHolder.ViewHolderContacto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.StartupTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class MisContactosActivity extends AppCompatActivity {

    RecyclerView recyclerViewContactos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BD_Usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FloatingActionButton btn_info_contacto;
    Dialog info_contacto;

    FirebaseRecyclerAdapter<Contacto,ViewHolderContacto> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Contacto> firebaseRecyclerOptions;

    LinearLayout linearLayoutBotones, linearLayoutBuscar;
    SearchView searchView;
    ImageButton btnAgregarContacto, btnBack, VaciarContactos;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_contactos);

        RadioButton radioNombres = findViewById(R.id.radioNombres);
        RadioButton radioCorreo = findViewById(R.id.radioCorreo);

        linearLayoutBotones = findViewById(R.id.linearLayoutBotones);
        linearLayoutBuscar = findViewById(R.id.linearLayoutSearch);
        searchView = findViewById(R.id.searchView);
        btnAgregarContacto = findViewById(R.id.btn_agregar_contacto);
        VaciarContactos = findViewById(R.id.btn_eliminar_contactos);
        btnBack = findViewById(R.id.btnBack);
        btn_info_contacto = findViewById(R.id.info_btn);
        info_contacto = new Dialog(this);

        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        recyclerViewContactos.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        BD_Usuarios = firebaseDatabase.getReference("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        btnBack.setOnClickListener(v -> MisContactosActivity.super.onBackPressed());

        dialog = new Dialog(MisContactosActivity.this);

        ListarContacto();

        ImageButton imageButtonBuscar = findViewById(R.id.btn_buscar);

        imageButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutBotones.setVisibility(View.GONE);
                linearLayoutBuscar.setVisibility(View.VISIBLE);
                searchView.requestFocus();

                // Listener para la búsqueda dinámica mientras se escribe en el SearchView

                searchView.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        boolean busquedaPorNombres = radioNombres.isChecked();

                        if (busquedaPorNombres) {
                            buscarContactoNombres(query);
                        } else {
                            buscarContactoCorreos(query);
                        }

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        boolean busquedaPorNombres = radioNombres.isChecked();

                        if (busquedaPorNombres) {
                            buscarContactoNombres(newText);
                        } else {
                            buscarContactoCorreos(newText);
                        }
                        return true;
                    }
                });
            }
        });


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                linearLayoutBotones.setVisibility(View.VISIBLE);
                linearLayoutBuscar.setVisibility(View.GONE);
                return false;
            }
        });

        btn_info_contacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoContacto();
            }
        });

        btnAgregarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*esto e para obtener el texto de lo textview*/
                String uidRecuperado = getIntent().getStringExtra("uid");
                /*pasar los datos a la actividad de AgregarContactoActivity*/
                Intent intent = new Intent(MisContactosActivity.this, AgregarContactoActivity.class);
                intent.putExtra("uid", uidRecuperado);
                startActivity(intent);
            }
        });

        VaciarContactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vaciar_Contactos();
                dialog.dismiss();
            }
        });

    }

    private void infoContacto() {
        Button entendido;

        info_contacto.setContentView(R.layout.info_funciones_contactos);
        entendido = (Button) info_contacto.findViewById(R.id.btn_entendido);

        entendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                info_contacto.dismiss();
            }
        });

        info_contacto.show();
        info_contacto.setCanceledOnTouchOutside(false);
    }

    private void ListarContacto(){
        Query query = BD_Usuarios.child(user.getUid()).child("Contactos").orderByChild("nombres");
        firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Contacto>().setQuery(query, Contacto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacto, ViewHolderContacto>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderContacto viewHolderContacto, int position, @NonNull Contacto contacto) {
                viewHolderContacto.SetearDatosContacto(getApplicationContext(), contacto.getId_contacto(), contacto.getUid_contacto(),
                        contacto.getNombres(), contacto.getApellidos(), contacto.getCorreo(), contacto.getTelefono(),
                        contacto.getEdad(), contacto.getDireccion(), contacto.getFoto());
            }

            @NonNull
            @Override
            public ViewHolderContacto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
                ViewHolderContacto viewHolderContacto = new ViewHolderContacto(view);
                viewHolderContacto.setOnclickListener(new ViewHolderContacto.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Obtener datos del contacto seleccionado
                        String id_contacto = getItem(position).getId_contacto();
                        String uid_contacto = getItem(position).getUid_contacto();
                        String nombres = getItem(position).getNombres();
                        String apellidos = getItem(position).getApellidos();
                        String correo = getItem(position).getCorreo();
                        String telefono = getItem(position).getTelefono();
                        String edad = getItem(position).getEdad();
                        String direccion = getItem(position).getDireccion();
                        String foto = getItem(position).getFoto();

                        //enviar datos a la actividad DetalleContactoActivity
                        Intent intent = new Intent(MisContactosActivity.this, DetalleContactoActivity.class);
                        intent.putExtra("id_contacto", id_contacto);
                        intent.putExtra("uid_contacto", uid_contacto);
                        intent.putExtra("nombres", nombres);
                        intent.putExtra("apellidos", apellidos);
                        intent.putExtra("correo", correo);
                        intent.putExtra("telefono", telefono);
                        intent.putExtra("edad", edad);
                        intent.putExtra("direccion", direccion);
                        intent.putExtra("foto", foto);
                        
                        startActivity(intent);
                    }

                    @Override
                    public void onItemLongClick(View view, int position) {

                        String id_c = getItem(position).getId_contacto();
                        String uid_contacto = getItem(position).getUid_contacto();
                        String nombres = getItem(position).getNombres();
                        String apellidos = getItem(position).getApellidos();
                        String correo = getItem(position).getCorreo();
                        String telefono = getItem(position).getTelefono();
                        String edad = getItem(position).getEdad();
                        String direccion = getItem(position).getDireccion();
                        String foto = getItem(position).getFoto();


                        //Toasty.info(getApplicationContext(), "Opciones de contacto", Toasty.LENGTH_SHORT).show();
                        Button EliminarC, ActualizarC;

                        dialog.setContentView(R.layout.cuadro_dialogo_opciones_contacto);

                        EliminarC = dialog.findViewById(R.id.EliminarC);
                        ActualizarC = dialog.findViewById(R.id.ActualizarC);

                        EliminarC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                EliminarContacto(id_c);
                                dialog.dismiss();
                            }
                        });

                        ActualizarC.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MisContactosActivity.this, ActualizarContactoActivity.class);
                                intent.putExtra("id_contacto", id_c);
                                intent.putExtra("uid_contacto", uid_contacto);
                                intent.putExtra("nombres", nombres);
                                intent.putExtra("apellidos", apellidos);
                                intent.putExtra("correo", correo);
                                intent.putExtra("telefono", telefono);
                                intent.putExtra("edad", edad);
                                intent.putExtra("direccion", direccion);
                                intent.putExtra("foto", foto);
                                startActivity(intent);
                                //Toasty.warning(getApplicationContext(), "Actualizar contacto", Toasty.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });

                return viewHolderContacto;
            }
        };

        recyclerViewContactos.setLayoutManager(new GridLayoutManager(MisContactosActivity.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewContactos.setAdapter(firebaseRecyclerAdapter);
    }

    private void buscarContactoNombres(String query) {

        Query queryNombres = BD_Usuarios.child(user.getUid()).child("Contactos")
                .orderByChild("nombres")
                .startAt(query)
                .endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<Contacto> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Contacto>().setQuery(queryNombres, Contacto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacto, ViewHolderContacto>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderContacto viewHolderContacto, int position, @NonNull Contacto contacto) {
                viewHolderContacto.SetearDatosContacto(getApplicationContext(), contacto.getId_contacto(), contacto.getUid_contacto(),
                        contacto.getNombres(), contacto.getApellidos(), contacto.getCorreo(), contacto.getTelefono(),
                        contacto.getEdad(), contacto.getDireccion(), contacto.getFoto());
            }

            @NonNull
            @Override
            public ViewHolderContacto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
                ViewHolderContacto viewHolderContacto = new ViewHolderContacto(view);
                viewHolderContacto.setOnclickListener(new ViewHolderContacto.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Obtener datos del contacto seleccionado
                        String id_contacto = getItem(position).getId_contacto();
                        String uid_contacto = getItem(position).getUid_contacto();
                        String nombres = getItem(position).getNombres();
                        String apellidos = getItem(position).getApellidos();
                        String correo = getItem(position).getCorreo();
                        String telefono = getItem(position).getTelefono();
                        String edad = getItem(position).getEdad();
                        String direccion = getItem(position).getDireccion();
                        String foto = getItem(position).getFoto();

                        //enviar datos a la actividad DetalleContactoActivity
                        Intent intent = new Intent(MisContactosActivity.this, DetalleContactoActivity.class);
                        intent.putExtra("id_contacto", id_contacto);
                        intent.putExtra("uid_contacto", uid_contacto);
                        intent.putExtra("nombres", nombres);
                        intent.putExtra("apellidos", apellidos);
                        intent.putExtra("correo", correo);
                        intent.putExtra("telefono", telefono);
                        intent.putExtra("edad", edad);
                        intent.putExtra("direccion", direccion);
                        intent.putExtra("foto", foto);

                        startActivity(intent);
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toasty.info(getApplicationContext(), "Click largo en el item", Toasty.LENGTH_SHORT).show();
                    }
                });

                return viewHolderContacto;
            }
        };

        recyclerViewContactos.setLayoutManager(new GridLayoutManager(MisContactosActivity.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewContactos.setAdapter(firebaseRecyclerAdapter);
    }

    private void buscarContactoCorreos(String query) {

        Query queryCorreos = BD_Usuarios.child(user.getUid()).child("Contactos")
                .orderByChild("correo")
                .startAt(query)
                .endAt(query + "\uf8ff");

        FirebaseRecyclerOptions<Contacto> firebaseRecyclerOptions =
                new FirebaseRecyclerOptions.Builder<Contacto>().setQuery(queryCorreos, Contacto.class).build();
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Contacto, ViewHolderContacto>(firebaseRecyclerOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolderContacto viewHolderContacto, int position, @NonNull Contacto contacto) {
                viewHolderContacto.SetearDatosContacto(getApplicationContext(), contacto.getId_contacto(), contacto.getUid_contacto(),
                        contacto.getNombres(), contacto.getApellidos(), contacto.getCorreo(), contacto.getTelefono(),
                        contacto.getEdad(), contacto.getDireccion(), contacto.getFoto());
            }

            @NonNull
            @Override
            public ViewHolderContacto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contacto, parent, false);
                ViewHolderContacto viewHolderContacto = new ViewHolderContacto(view);
                viewHolderContacto.setOnclickListener(new ViewHolderContacto.ClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        //Obtener datos del contacto seleccionado
                        String id_contacto = getItem(position).getId_contacto();
                        String uid_contacto = getItem(position).getUid_contacto();
                        String nombres = getItem(position).getNombres();
                        String apellidos = getItem(position).getApellidos();
                        String correo = getItem(position).getCorreo();
                        String telefono = getItem(position).getTelefono();
                        String edad = getItem(position).getEdad();
                        String direccion = getItem(position).getDireccion();
                        String foto = getItem(position).getFoto();

                        //enviar datos a la actividad DetalleContactoActivity
                        Intent intent = new Intent(MisContactosActivity.this, DetalleContactoActivity.class);
                        intent.putExtra("id_contacto", id_contacto);
                        intent.putExtra("uid_contacto", uid_contacto);
                        intent.putExtra("nombres", nombres);
                        intent.putExtra("apellidos", apellidos);
                        intent.putExtra("correo", correo);
                        intent.putExtra("telefono", telefono);
                        intent.putExtra("edad", edad);
                        intent.putExtra("direccion", direccion);
                        intent.putExtra("foto", foto);

                        startActivity(intent);
                    }
                    @Override
                    public void onItemLongClick(View view, int position) {
                        Toasty.info(getApplicationContext(), "Click largo en el item", Toasty.LENGTH_SHORT).show();
                    }
                });

                return viewHolderContacto;
            }
        };

        recyclerViewContactos.setLayoutManager(new GridLayoutManager(MisContactosActivity.this, 2));
        firebaseRecyclerAdapter.startListening();
        recyclerViewContactos.setAdapter(firebaseRecyclerAdapter);
    }

    private void EliminarContacto(String id_c) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MisContactosActivity.this);
        builder.setTitle("Eliminar");
        builder.setMessage("¿Desea eliminar este contacto?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = BD_Usuarios.child(user.getUid()).child("Contactos").orderByChild("id_contacto").equalTo(id_c);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toasty.error(getApplicationContext(), "Contacto eliminado", Toasty.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toasty.error(getApplicationContext(), error.getMessage(), Toasty.LENGTH_SHORT).show();
                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toasty.info(getApplicationContext(), "Cancelado", Toasty.LENGTH_SHORT).show();
            }
        });

        builder.create().show();
    }

    private void Vaciar_Contactos(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MisContactosActivity.this);
        builder.setTitle("Vaciar todos los contactos");
        builder.setMessage("¿Estas seguro de eliminar todos los contactos?");

        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Query query = BD_Usuarios.child(user.getUid()).child("Contactos");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ds : snapshot.getChildren()){
                            ds.getRef().removeValue();
                        }
                        Toasty.error(getApplicationContext(), "Todos los contactos se han eliminado correctamente", Toasty.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Toasty.info(getApplicationContext(), "Cancelado", Toasty.LENGTH_SHORT).show();

            }
        });

        builder.create().show();
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}