package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.agendasmart.R;

import es.dmoral.toasty.Toasty;

public class DetalleContactoActivity extends AppCompatActivity {

    ImageView imagen_Perfil;

    TextView txtIdContactoDetalle,txtUidUsuarioDetalle,txtNombreContactoDetalle,
            txtApellidoContactoDetalle,txtCorreoContactoDetalle,txtEdadContactoDetalle,txtDireccionContactoDetalle,
            txtTelefonoContactoDetalle;

    //String donde se alamacenaran los datos del contacto
    String idContacto,uidUsuario,nombreContacto,apellidoContacto,correoContacto,edadContacto,direccionContacto,telefonoContacto;

    Button btnLlamarContacto,btnMensajeContacto;
    ImageButton btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_contacto);

        InicializarVariebles();
        ObtenerDatosContacto();
        SettearDatosContacto();
        ObtenerImagen();

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> DetalleContactoActivity.super.onBackPressed());
    }

    private void InicializarVariebles(){
        imagen_Perfil = findViewById(R.id.imagen_Perfil);
        txtIdContactoDetalle = findViewById(R.id.txtIdContactoDetalle);
        txtUidUsuarioDetalle = findViewById(R.id.txtUidUsuarioDetalle);
        txtNombreContactoDetalle = findViewById(R.id.txtNombreContactoDetalle);
        txtApellidoContactoDetalle = findViewById(R.id.txtApellidoContactoDetalle);
        txtCorreoContactoDetalle = findViewById(R.id.txtCorreoContactoDetalle);
        txtEdadContactoDetalle = findViewById(R.id.txtEdadContactoDetalle);
        txtDireccionContactoDetalle = findViewById(R.id.txtDireccionContactoDetalle);
        txtTelefonoContactoDetalle = findViewById(R.id.txtTelefonoContactoDetalle);

        btnLlamarContacto = findViewById(R.id.btnLlamarContacto);
        btnMensajeContacto = findViewById(R.id.btnMensajeContacto);
    }

    private void ObtenerDatosContacto(){
        Bundle datosContacto = getIntent().getExtras();

        idContacto = datosContacto.getString("id_contacto");
        uidUsuario = datosContacto.getString("uid_contacto");
        nombreContacto = datosContacto.getString("nombres");
        apellidoContacto = datosContacto.getString("apellidos");
        correoContacto = datosContacto.getString("correo");
        edadContacto = datosContacto.getString("edad");
        direccionContacto = datosContacto.getString("direccion");
        telefonoContacto = datosContacto.getString("telefono");
    }

    private void SettearDatosContacto(){
        txtIdContactoDetalle.setText(idContacto);
        txtUidUsuarioDetalle.setText(uidUsuario);
        txtNombreContactoDetalle.setText(nombreContacto);
        txtApellidoContactoDetalle.setText(apellidoContacto);
        txtCorreoContactoDetalle.setText(correoContacto);
        txtEdadContactoDetalle.setText(edadContacto);
        txtDireccionContactoDetalle.setText(direccionContacto);
        txtTelefonoContactoDetalle.setText(telefonoContacto);
    }

    private void ObtenerImagen(){
        String imagen = getIntent().getStringExtra("foto");

        try {
            Glide.with(getApplicationContext()).load(imagen).placeholder(R.drawable.image_color).into(imagen_Perfil);
        }catch (Exception e){
            Toasty.info(this, "Esperando imagen", Toasty.LENGTH_SHORT).show();
        }
    }
}