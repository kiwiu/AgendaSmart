package com.example.agendasmart.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.agendasmart.Objetos.Contacto;
import com.example.agendasmart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import es.dmoral.toasty.Toasty;

public class AgregarContactoActivity extends AppCompatActivity {

    TextView uidUsuario, telefono_Perfil;
    EditText nombre_Contacto, apellido_Contacto, correo_Contacto,edad_Contacto, direccion_Contacto;
    ImageButton btnBack,btnTelefono;
    Button btnGuardarContacto;
    Dialog dialog_telefono;
    DatabaseReference BD_Usuarios;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_contacto);

        InicializarVariables();
        obtenerUidUsuario();

        btnGuardarContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarContacto();
            }
        });

        btnTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Establecer_Telefono_Contacto();
            }
        });

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> AgregarContactoActivity.super.onBackPressed());
    }

    private void InicializarVariables(){
        uidUsuario = findViewById(R.id.uidUsuario);
        telefono_Perfil = findViewById(R.id.telefono_Perfil);
        nombre_Contacto = findViewById(R.id.nombre_Contacto);
        apellido_Contacto = findViewById(R.id.apellido_Contacto);
        correo_Contacto = findViewById(R.id.correo_Contacto);
        edad_Contacto = findViewById(R.id.edad_Contacto);
        direccion_Contacto = findViewById(R.id.direccion_Contacto);
        btnTelefono = findViewById(R.id.btnTelefono);
        btnGuardarContacto = findViewById(R.id.btnGuardarContacto);

        dialog_telefono = new Dialog(AgregarContactoActivity.this);
        BD_Usuarios = FirebaseDatabase.getInstance().getReference("Usuarios");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        dialog = new Dialog(AgregarContactoActivity.this);
    }

    private void obtenerUidUsuario(){
        String UidUsuario = getIntent().getStringExtra("uid");
        uidUsuario.setText(UidUsuario);
    }

    private void Establecer_Telefono_Contacto(){
        CountryCodePicker ccp;
        EditText Establecer_Telefono;
        Button btn_Aceptar_Telefono;

        dialog_telefono.setContentView(R.layout.cuadro_dialogo_telefono);

        ccp = dialog_telefono.findViewById(R.id.ccp);
        Establecer_Telefono = dialog_telefono.findViewById(R.id.Establecer_Telefono);
        btn_Aceptar_Telefono = dialog_telefono.findViewById(R.id.btn_Aceptar_Telefono);

        btn_Aceptar_Telefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigoPais = ccp.getSelectedCountryCodeWithPlus();
                String telefono = Establecer_Telefono.getText().toString();
                String telefonoCompleto = codigoPais + telefono;

                if (!telefono.equals("")){
                    telefono_Perfil.setText(telefonoCompleto);
                    dialog_telefono.dismiss();
                }
                else {
                    Toasty.warning(AgregarContactoActivity.this, "Ingrese un número de teléfono", Toasty.LENGTH_SHORT).show();
                    dialog_telefono.dismiss();
                }
            }
        });
        dialog_telefono.show();
        dialog_telefono.setCanceledOnTouchOutside(true);
    }

    private void AgregarContacto(){
        //Obtener datos
        String uid = uidUsuario.getText().toString();
        String nombre = nombre_Contacto.getText().toString();
        String apellido = apellido_Contacto.getText().toString();
        String correo = correo_Contacto.getText().toString();
        String telefono = telefono_Perfil.getText().toString();
        String edad = edad_Contacto.getText().toString();
        String direccion = direccion_Contacto.getText().toString();

        // creamos cadena unica para cada contacto
        String id_contacto = BD_Usuarios.push().getKey();

        //Validar los datos
        if (!uid.equals("") && !nombre.equals("")){
            //Creamos un objeto
            Contacto contacto = new Contacto(id_contacto,uid,nombre,apellido,correo,telefono,edad,direccion,"");

            //Establecer nombre de la BD
            String Nombre_BD = "Contactos";
            assert id_contacto != null;
            BD_Usuarios.child(user.getUid()).child(Nombre_BD).child(id_contacto).setValue(contacto);
            Toasty.success(AgregarContactoActivity.this, "Contacto agregado", Toasty.LENGTH_SHORT).show();
            onBackPressed();
        }
        else {
            ValidarRegistroContacto();
        }
    }

    private void ValidarRegistroContacto(){
        Button btn_entendido;

        dialog.setContentView(R.layout.cuadro_dialogo_validar_registro);

        btn_entendido = dialog.findViewById(R.id.btn_entendido);

        btn_entendido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
    }
}