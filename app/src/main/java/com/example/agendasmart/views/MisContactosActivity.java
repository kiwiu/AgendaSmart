package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.agendasmart.Objetos.Contacto;
import com.example.agendasmart.R;
import com.example.agendasmart.ViewHolder.ViewHolderContacto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import es.dmoral.toasty.Toasty;

public class MisContactosActivity extends AppCompatActivity {

    RecyclerView recyclerViewContactos;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference BD_Usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    FirebaseRecyclerAdapter<Contacto,ViewHolderContacto> firebaseRecyclerAdapter;
    FirebaseRecyclerOptions<Contacto> firebaseRecyclerOptions;

    LinearLayout linearLayoutBotones, linearLayoutBuscar;
    SearchView searchView;
    ImageButton btnAgregarContacto, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_contactos);

        linearLayoutBotones = findViewById(R.id.linearLayoutBotones);
        linearLayoutBuscar = findViewById(R.id.linearLayoutSearch);
        searchView = findViewById(R.id.searchView);
        btnAgregarContacto = findViewById(R.id.btn_agregar_contacto);
        btnBack = findViewById(R.id.btnBack);

        recyclerViewContactos = findViewById(R.id.recyclerViewContactos);
        recyclerViewContactos.setHasFixedSize(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        BD_Usuarios = firebaseDatabase.getReference("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        btnBack.setOnClickListener(v -> MisContactosActivity.super.onBackPressed());

        ListarContacto();

        ImageButton imageButtonBuscar = findViewById(R.id.btn_buscar);

        imageButtonBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutBotones.setVisibility(View.GONE);
                linearLayoutBuscar.setVisibility(View.VISIBLE);
                searchView.requestFocus();
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

    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseRecyclerAdapter != null){
            firebaseRecyclerAdapter.startListening();
        }
    }
}