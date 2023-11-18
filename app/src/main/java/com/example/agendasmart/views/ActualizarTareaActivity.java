package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendasmart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import es.dmoral.toasty.Toasty;

public class ActualizarTareaActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextView Id_tarea_A, Uid_Usuario_A, Correo_usuario_A, Fecha_registro_A , Fecha_A, Estado_A, Estado_nuevo, fecha;
    EditText Titulo_A, Descripcion_A;
    Button Btn_Calendario_A;
    ImageButton btnBack, btn_actualizar_tarea;

    //Aqui se guarda los datos del registro seleccionado
    String id_tarea_R , correo_usuario_R, fecha_registro_R, titulo_R, descripcion_R, fecha_R, estado_R;

    ImageView Tarea_Finalizada, Tarea_No_Finalizada;

    Spinner Spinner_estado;

    int dia, mes , anio;

    FirebaseAuth firebaseAuth;/*--------------*/
    FirebaseUser user;/*--------------*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_nota);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Actualizar tarea");
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        InicializarVistas();
        RecuperarDatos();
        SetearDatos();
        ComprobarEstadoNota();
        Spinner_Estado();


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarTareaActivity.super.onBackPressed();
            }
        });

        btn_actualizar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarNotaBD();
            }
        });


        Btn_Calendario_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeleccionarFecha();
            }
        });
    }
    private void InicializarVistas(){
        Id_tarea_A = findViewById(R.id.itenid_A);
        Correo_usuario_A = findViewById(R.id.correo_usuario_A);
        Fecha_registro_A = findViewById(R.id.fecha_creacion_A);
        Fecha_A = findViewById(R.id.fecha_hora_actual_A);
        fecha = findViewById(R.id.fecha);
        Estado_A = findViewById(R.id.estado_A);
        Titulo_A = findViewById(R.id.titulo_A);
        Descripcion_A = findViewById(R.id.descripcion_A);
        Btn_Calendario_A = findViewById(R.id.btn_calendario_A);

        Tarea_Finalizada = findViewById(R.id.Tarea_Finalizada);
        Tarea_No_Finalizada = findViewById(R.id.Tarea_No_Finalizada);

        Spinner_estado = findViewById(R.id.Spinner_estado);
        Estado_nuevo = findViewById(R.id.Estado_nuevo);
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        btn_actualizar_tarea = (ImageButton) findViewById(R.id.btn_actualizar_tarea);

        firebaseAuth = FirebaseAuth.getInstance();/*--------------*/
        user = firebaseAuth.getCurrentUser();/*--------------*/
    }

    private void RecuperarDatos(){
        Bundle intent = getIntent().getExtras();

        id_tarea_R = intent.getString("id_tarea");
        //uid_usuario_R = intent.getString("uid_usuario");
        correo_usuario_R = intent.getString("correo_usuario");
        titulo_R = intent.getString("titulo");
        descripcion_R = intent.getString("descripcion");
        fecha_R = intent.getString("fecha_nota");
        estado_R = intent.getString("estado");
    }

    private void SetearDatos(){
        Id_tarea_A.setText(id_tarea_R);
        //Uid_Usuario_A.setText(uid_usuario_R);
        Correo_usuario_A.setText(correo_usuario_R);
        Titulo_A.setText(titulo_R);
        Descripcion_A.setText(descripcion_R);
        fecha.setText(fecha_R);
        Estado_A.setText(estado_R);

    }

    private void ComprobarEstadoNota(){
        String estado_tarea = Estado_A.getText().toString();

        if (estado_tarea.equals("No finalizada")){
            Tarea_No_Finalizada.setVisibility(View.VISIBLE);
        }
        if (estado_tarea.equals("Finalizada")){
            Tarea_Finalizada.setVisibility(View.VISIBLE);
        }
    }

    private void SeleccionarFecha(){
        final Calendar calendario = Calendar.getInstance();
        dia = calendario.get(Calendar.DAY_OF_MONTH);
        mes = calendario.get(Calendar.MONTH);
        anio = calendario.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ActualizarTareaActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int anioSeleccionado, int mesSeleccionado, int diaSeleccionado) {

                // Obtén la fecha actual
                Calendar fechaActual = Calendar.getInstance();

                // Crear una instancia de la fecha seleccionada
                Calendar fechaSeleccionada = Calendar.getInstance();
                fechaSeleccionada.set(anioSeleccionado, mesSeleccionado, diaSeleccionado);

                // Verificar si la fecha seleccionada es anterior a la actual
                if (fechaSeleccionada.before(fechaActual)) {
                    // Muestra un mensaje o realiza la acción que desees cuando la fecha es anterior
                    Toasty.error(ActualizarTareaActivity.this, "No se pueden seleccionar fechas anteriores", Toast.LENGTH_SHORT).show();
                } else {
                    // Formatea y muestra la fecha
                    String diaFormateado, mesFormateado;
                    if (diaSeleccionado < 10) {
                        diaFormateado = "0" + String.valueOf(diaSeleccionado);
                    } else {
                        diaFormateado = String.valueOf(diaSeleccionado);
                    }

                    int Mes = mesSeleccionado + 1;

                    if (Mes < 10) {
                        mesFormateado = "0" + String.valueOf(Mes);
                    } else {
                        mesFormateado = String.valueOf(Mes);
                    }

                    /*Mostrar fecha*/
                    fecha.setText(diaFormateado + "/" + mesFormateado + "/" + anioSeleccionado);
                }
            }
        },anio, mes, dia);
        datePickerDialog.show();

    }

    private void Spinner_Estado(){
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Estados_tarea, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_estado.setAdapter(adapter);
        Spinner_estado.setOnItemSelectedListener(this);

    }

    private void ActualizarNotaBD(){
        String tituloActualizar = Titulo_A.getText().toString();
        String descripcionActualizar = Descripcion_A.getText().toString();
        String fechaActualizar = fecha.getText().toString();
        String estadoActualizar = Estado_nuevo.getText().toString();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Usuarios");/*--------------*/

        //Consulta
        Query query = databaseReference.child(user.getUid()).child("Tareas_publicadas").orderByChild("id_tarea").equalTo(id_tarea_R);/*--------------*/
        Query query1 = databaseReference.child(user.getUid()).child("Mis tareas importantes").orderByChild("id_tarea").equalTo(id_tarea_R);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("titulo").setValue(tituloActualizar);
                    ds.getRef().child("descripcion").setValue(descripcionActualizar);
                    ds.getRef().child("fecha_nota").setValue(fechaActualizar);
                    ds.getRef().child("estado").setValue(estadoActualizar);
                }

                Toasty.info(ActualizarTareaActivity.this, "Tarea actualizada con éxito", Toasty.LENGTH_SHORT).show();
                onBackPressed();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("titulo").setValue(tituloActualizar);
                    ds.getRef().child("descripcion").setValue(descripcionActualizar);
                    ds.getRef().child("fecha_nota").setValue(fechaActualizar);
                    ds.getRef().child("estado").setValue(estadoActualizar);
                }

                Toasty.info(ActualizarTareaActivity.this, "Tarea actualizada con éxito", Toasty.LENGTH_SHORT).show();
                onBackPressed();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String ESTADO_ACTUAL = Estado_A.getText().toString();

        String Posicion_1 = adapterView.getItemAtPosition(1).toString();

        String estado_seleccionado = adapterView.getItemAtPosition(i).toString();
        Estado_nuevo.setText(estado_seleccionado);

        if (ESTADO_ACTUAL.equals("Finalizada")){
            Estado_nuevo.setText(Posicion_1);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}