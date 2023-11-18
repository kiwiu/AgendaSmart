package com.example.agendasmart.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.agendasmart.R;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;


import java.net.URI;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class ActualizarContactoActivity extends AppCompatActivity {

    ImageView Imagen_C_A, Actualizar_imagen_C_A, Actualizar_Telefono_C_A;
    EditText Nombres_C_A, Apellido_C_A, Correo_C_A, Edad_C_A, Direccion_C_A;
    TextView Id_C_A, uid_C_A, Telefono_C_A;
    Button Btn_Actualizar_C_A;
    ImageButton btnBack;
    String id_c, uid_usuario, nombres_c, apellidos_c, correo_c, telefono_c, edad_c, direccion_c;

    Dialog dialog_telefono;

    FirebaseAuth firebaseAuth;

    FirebaseUser user;

    Uri imageUri = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_contacto);

        InicializarVistas();
        RecuperarDatos();
        SetearDatosRecuperados();
        ObtenerImagen();

        Actualizar_Telefono_C_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Establecer_Telefono();
            }
        });

        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarContactoActivity.super.onBackPressed();
            }
        });

        Btn_Actualizar_C_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActualizarInfoContacto();
            }
        });

        Actualizar_imagen_C_A.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(ActualizarContactoActivity.this,
                       Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    SeleccionarImagenGaleria();
                }else{
                    SolicitarPermisoGaleria.launch(Manifest.permission.READ_MEDIA_IMAGES);
                }
            }
        });

        progressDialog = new ProgressDialog(ActualizarContactoActivity.this);
        progressDialog.setTitle("Espere por favor");

        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void InicializarVistas(){
        Id_C_A = findViewById(R.id.Id_C_A);
        uid_C_A = findViewById(R.id.uid_C_A);
        Telefono_C_A = findViewById(R.id.Telefono_C_A);
        Nombres_C_A = findViewById(R.id.Nombres_C_A);
        Apellido_C_A = findViewById(R.id.Apellido_C_A);
        Correo_C_A = findViewById(R.id.Correo_C_A);
        Edad_C_A = findViewById(R.id.Edad_C_A);
        Direccion_C_A = findViewById(R.id.Direccion_C_A);
        Imagen_C_A = findViewById(R.id.Imagen_C_A);
        Actualizar_imagen_C_A = findViewById(R.id.Actualizar_imagen_C_A);
        Actualizar_Telefono_C_A = findViewById(R.id.Actualizar_Telefono_C_A);
        Btn_Actualizar_C_A = findViewById(R.id.Btn_Actualizar_C_A);
        dialog_telefono = new Dialog(ActualizarContactoActivity.this);
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

    }

    private void RecuperarDatos(){
        Bundle bundle = getIntent().getExtras();
        id_c = bundle.getString("id_contacto");
        uid_usuario = bundle.getString("uid_contacto");
        nombres_c = bundle.getString("nombres");
        apellidos_c = bundle.getString("apellidos");
        correo_c = bundle.getString("correo");
        telefono_c = bundle.getString("telefono");
        edad_c = bundle.getString("edad");
        direccion_c = bundle.getString("direccion");
    }

    private void SetearDatosRecuperados(){
        Id_C_A.setText(id_c);
        uid_C_A.setText(uid_usuario);
        Nombres_C_A.setText(nombres_c);
        Apellido_C_A.setText(apellidos_c);
        Correo_C_A.setText(correo_c);
        Telefono_C_A.setText(telefono_c);
        Edad_C_A.setText(edad_c);
        Direccion_C_A.setText(direccion_c);
    }

    private void ObtenerImagen(){
        String imagen_c = getIntent().getStringExtra("imagen");

        try {
            Glide.with(getApplicationContext()).load(imagen_c).placeholder(R.drawable.image).into(Imagen_C_A);
        }catch (Exception e){
            Toasty.warning(this, ""+e.getMessage(), Toasty.LENGTH_SHORT).show();
        }
    }

    private void Establecer_Telefono(){
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
                    Telefono_C_A.setText(telefonoCompleto);
                    dialog_telefono.dismiss();
                }
                else {
                    Toasty.warning(ActualizarContactoActivity.this, "Ingrese un número de teléfono", Toasty.LENGTH_SHORT).show();
                    dialog_telefono.dismiss();
                }
            }
        });
        dialog_telefono.show();
        dialog_telefono.setCanceledOnTouchOutside(true);
    }

    private void ActualizarInfoContacto(){
        String NombresActualizar = Nombres_C_A.getText().toString().trim();
        String ApellidosActualizar = Apellido_C_A.getText().toString().trim();
        String CorreoActualizar = Correo_C_A.getText().toString().trim();
        String TelefonoActualizar = Telefono_C_A.getText().toString().trim();
        String DireccionActualizar = Direccion_C_A.getText().toString().trim();
        String EdadActualizar = Edad_C_A.getText().toString().trim();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReferences = firebaseDatabase.getReference("Usuarios");

        Query query = databaseReferences.child(user.getUid()).child("Contactos").orderByChild("id_contacto").equalTo(id_c);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    ds.getRef().child("nombres").setValue(NombresActualizar);
                    ds.getRef().child("apellidos").setValue(ApellidosActualizar);
                    ds.getRef().child("correo").setValue(CorreoActualizar);
                    ds.getRef().child("telefono").setValue(TelefonoActualizar);
                    ds.getRef().child("edad").setValue(EdadActualizar);
                    ds.getRef().child("direccion").setValue(DireccionActualizar);
                }
                Toasty.success(ActualizarContactoActivity.this, "Información de contacto actualizada", Toasty.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void subirImagenStorage(){
        progressDialog.setMessage("Subiendo imagen");
        progressDialog.show();
        String id_C = getIntent().getStringExtra("id_c");

        String carpetaImagenes = "ImagenesPerfilContactos/";
        String NombreImagen = carpetaImagenes+id_C;
        StorageReference reference = FirebaseStorage.getInstance().getReference(NombreImagen);
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uritask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uritask.isSuccessful());
                        String UriIMAGEN = ""+uritask.getResult();
                        ActualizarImagenDB(UriIMAGEN);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toasty.error(ActualizarContactoActivity.this, ""+e.getMessage(), Toasty.LENGTH_SHORT).show();

                    }
                });
    }

    private void ActualizarImagenDB(String uriIMAGEN) {
        progressDialog.setMessage("Actualizando la imagen");
        progressDialog.show();

        String Id_C = getIntent().getStringExtra("id_c");

        HashMap<String, Object> hashMap = new HashMap<>();
        if(imageUri != null){
            hashMap.put("foto", ""+uriIMAGEN);
        }

        Log.d("DEBUG", "Valor de id_c: " + id_c);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Usuarios");
        databaseReference.child(user.getUid()).child("Contactos").child(id_c)
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toasty.info(ActualizarContactoActivity.this, "Imagen actualizada", Toasty.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toasty.info(ActualizarContactoActivity.this, ""+e.getMessage(), Toasty.LENGTH_SHORT).show();

                    }
                });
    }

    private void SeleccionarImagenGaleria() {
        /*abrir galria*/
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galeriaActivytyResultLauncher.launch(intent);
    }

    private ActivityResultLauncher<Intent> galeriaActivytyResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK) {
                        Intent data = result.getData();
                        imageUri = data.getData();
                        Imagen_C_A.setImageURI(imageUri);
                        subirImagenStorage();
                    }else{
                        Toasty.info(ActualizarContactoActivity.this, "Cancelado", Toasty.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<String> SolicitarPermisoGaleria = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if(isGranted){
                    SeleccionarImagenGaleria();
                }else{
                    Toasty.error(ActualizarContactoActivity.this, "Permiso denegado", Toasty.LENGTH_SHORT).show();
                }
            }
    );
}