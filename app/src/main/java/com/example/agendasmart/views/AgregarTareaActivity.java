package com.example.agendasmart.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.agendasmart.Objetos.Tarea;
import com.example.agendasmart.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

public class AgregarTareaActivity extends AppCompatActivity {

    TextView uid_usuario, correo_usuario, fecha_hora_actual, fecha, estado;
    EditText titulo, descripcion;
    Button btn_calendario;
    ImageButton btnBack, btn_agregar_tarea;

    int dia, mes, anio;

    DatabaseReference BD_Firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_tarea);

        InicializarVariables();
        ObtenerDatos();
        ObtenerFechaHoraActual();

        btn_calendario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendario = Calendar.getInstance();
                dia = calendario.get(Calendar.DAY_OF_MONTH);
                mes = calendario.get(Calendar.MONTH);
                anio = calendario.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(AgregarTareaActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                            Toasty.error(AgregarTareaActivity.this, "No se pueden seleccionar fechas anteriores", Toast.LENGTH_SHORT).show();
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
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarTareaActivity.super.onBackPressed();
            }
        });

        btn_agregar_tarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarTareas();
            }
        });

    }

    private void InicializarVariables(){
        btnBack = (ImageButton)findViewById(R.id.btnBack);
        uid_usuario = (TextView)findViewById(R.id.uid_usuario);
        correo_usuario = (TextView)findViewById(R.id.correo_usuario);
        fecha_hora_actual = (TextView)findViewById(R.id.fecha_hora_actual);
        fecha = (TextView)findViewById(R.id.fecha);
        estado = (TextView)findViewById(R.id.estado);
        titulo = (EditText)findViewById(R.id.titulo);
        descripcion = (EditText)findViewById(R.id.descripcion);
        btn_calendario = (Button)findViewById(R.id.btn_calendario);
        btn_agregar_tarea = (ImageButton) findViewById(R.id.btn_agregar_tarea);

        BD_Firebase = FirebaseDatabase.getInstance().getReference();
    }

    private void  ObtenerDatos(){
        String uid_recuperado = getIntent().getStringExtra("uid");
        String correo_recuperado = getIntent().getStringExtra("correo");

        uid_usuario.setText(uid_recuperado);
        correo_usuario.setText(correo_recuperado);
    }

    private void ObtenerFechaHoraActual(){
        String FechaHoraRegistro = new SimpleDateFormat("dd-MM-yyyy/HH:mm:ss a", Locale.getDefault()).format(System.currentTimeMillis());

        fecha_hora_actual.setText(FechaHoraRegistro);
    }

    private void AgregarTareas(){
        String Uid_usuario = uid_usuario.getText().toString();
        String Correo_usuario = correo_usuario.getText().toString();
        String FechaHoraActual = fecha_hora_actual.getText().toString();
        String Titulo = titulo.getText().toString();
        String Descripcion = descripcion.getText().toString();
        String Fecha = fecha.getText().toString();
        String Estado = estado.getText().toString();

        /*validar datos*/
        if (!Uid_usuario.equals("") && !Correo_usuario.equals("") && !FechaHoraActual.equals("") &&
                !Titulo.equals("") && !Descripcion.equals("") && !Fecha.equals("") && !Estado.equals("")) {

            Tarea tarea = new Tarea(Correo_usuario + "/" + FechaHoraActual,
                    Uid_usuario,
                    Correo_usuario,
                    FechaHoraActual,
                    Titulo,
                    Descripcion,
                    Fecha,
                    Estado);

            String Tarea_usuario = BD_Firebase.push().getKey();
            String Nombre_BD = "Tareas_publicadas";

            BD_Firebase.child(Nombre_BD).child(Tarea_usuario).setValue(tarea);

            Toasty.success(this, "Tarea agregada con éxito", Toast.LENGTH_SHORT).show();

            onBackPressed();

        }
        else{
            Toasty.error(this, "Por favor, llene todos los campos", Toast.LENGTH_SHORT).show();
        }



    }
}