package com.example.agendasmart.views;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.agendasmart.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class ActualizarImagenPerfilActivity extends AppCompatActivity {

    ImageView imagenPerfilActual;
    Button btnElegirImagen,btnActualizarImagen;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;

    Dialog dialog_elejir_imagen;

    Uri imagen_uri = null;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_imagen_perfil);

        // Inicializamos los componentes
        imagenPerfilActual = findViewById(R.id.imagenPerfilActual);
        btnElegirImagen = findViewById(R.id.btnElegirImagen);
        btnActualizarImagen = findViewById(R.id.btnActualizarImagen);

        // Inicializamos Firebase
        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        // Inicializamos el dialogo
        dialog_elejir_imagen = new Dialog(ActualizarImagenPerfilActivity.this);

        btnElegirImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toasty.info(ActualizarImagenPerfilActivity.this,"Elegir imagen",Toasty.LENGTH_SHORT).show();
                ElegirImagenDe();
            }
        });

        btnActualizarImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imagen_uri == null){
                    Toasty.error(ActualizarImagenPerfilActivity.this,"Por favor seleccione una imagen",Toasty.LENGTH_SHORT).show();
                }else {
                    SubirImagenStorage();
                }
            }
        });

        progressDialog = new ProgressDialog(ActualizarImagenPerfilActivity.this);
        progressDialog.setTitle("Por favor espere");
        progressDialog.setCanceledOnTouchOutside(false);

        LecturaImagenPerfilActual();
    }

    private void LecturaImagenPerfilActual(){
        // Leer imagen de perfil actual
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");
        ref.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Ontener la imagen de la base de datos
                String imagenPerfil = ""+snapshot.child("imagen").getValue();

                // Si la imagen es igual a la cadena vacia
                Glide.with(getApplicationContext()).load(imagenPerfil).placeholder(R.drawable.image).into(imagenPerfilActual);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SubirImagenStorage() {
        progressDialog.setMessage("Suibiendo imagen...");
        progressDialog.show();
        String carpetaImagenes = "ImagenesPerfil/";
        String NombreImagen = carpetaImagenes+firebaseAuth.getUid();
        StorageReference reference = FirebaseStorage.getInstance().getReference(NombreImagen);
        reference.putFile(imagen_uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask  = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                String uriImagen = "" + uriTask.getResult();

                ActualizarImagenBD(uriImagen);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(ActualizarImagenPerfilActivity.this,""+e.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void ActualizarImagenBD(String uriImagen) {
        progressDialog.setMessage("Actualizando imagen...");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        if (imagen_uri != null){
            hashMap.put("imagen", ""+ uriImagen);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Usuarios");
        ref.child(user.getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                progressDialog.dismiss();
                Toasty.success(ActualizarImagenPerfilActivity.this,"Imagen actualizada",Toasty.LENGTH_SHORT).show();
                onBackPressed();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull  Exception e) {
                progressDialog.dismiss();
                Toasty.error(ActualizarImagenPerfilActivity.this,""+e.getMessage(),Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void ElegirImagenDe() {

        Button btnCamara,btnGaleria;

        // Inicializamos el dialogo
        dialog_elejir_imagen.setContentView(R.layout.cuadro_dialogo_elegir_imagen);

        // Inicializamos los componentes
        btnCamara = dialog_elejir_imagen.findViewById(R.id.btnCamara);
        btnGaleria = dialog_elejir_imagen.findViewById(R.id.btnGaleria);

        // Acciones de los botones
        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeleccionarImagenGaleria();
                dialog_elejir_imagen.dismiss();
                if (ContextCompat.checkSelfPermission(ActualizarImagenPerfilActivity.this,
                        Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                    //Permiso aceptado
                    SeleccionarImagenGaleria();
                    dialog_elejir_imagen.dismiss();
                }else {
                    //Permiso denegado
                    SolicitudPermisoGaleria.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                    dialog_elejir_imagen.dismiss();
                }
            }
        });
        btnCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(ActualizarImagenPerfilActivity.this,
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    //Permiso aceptado
                    SeleccionarImagenCamara();
                    dialog_elejir_imagen.dismiss();
                }else {
                    //Permiso denegado
                    SolicitudPermisoCamara.launch(Manifest.permission.CAMERA);
                    dialog_elejir_imagen.dismiss();
                }

            }
        });


        // Mostrar el dialogo
        dialog_elejir_imagen.show();
        dialog_elejir_imagen.setCanceledOnTouchOutside(true);
    }


    private void SeleccionarImagenGaleria() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        galeriaActivityResultLauncher.launch(intent);

    }

    private final ActivityResultLauncher<Intent> galeriaActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        //Obtener Uri de la imagen seleccionada
                        Intent data = result.getData();
                        imagen_uri = data.getData();

                        //setear imagen en el imageview
                        imagenPerfilActual.setImageURI(imagen_uri);

                    }else {
                        Toasty.error(ActualizarImagenPerfilActivity.this,"Cancelado por el usuario",Toasty.LENGTH_SHORT).show();
                    }
                }
            }
    );

    /*PERMISOS PARA ACCEDER A LA GALERIA*/
    private final ActivityResultLauncher<String> SolicitudPermisoGaleria =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    //Permiso aceptado
                    SeleccionarImagenGaleria();
                }else {
                    //Permiso denegado
                    Toasty.error(ActualizarImagenPerfilActivity.this,"Permiso denegado",Toasty.LENGTH_SHORT).show();
                }
            });

    private void SeleccionarImagenCamara() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Imagen nueva");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Descripcion de la imagen");
        imagen_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imagen_uri);

        CamaraActivityResultLauncher.launch(intent);
    }

    private final ActivityResultLauncher<Intent> CamaraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode()== Activity.RESULT_OK){
                        //Obtener Uri de la imagen tomada desde la camara
                        imagenPerfilActual.setImageURI(imagen_uri);
                    }else {
                        Toasty.error(ActualizarImagenPerfilActivity.this,"Cancelado por el usuario",Toasty.LENGTH_SHORT).show();}
                }
            }
    );

    //PERMISO PARA ACCEDER A LA CAMARA
    private final ActivityResultLauncher<String> SolicitudPermisoCamara =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
                if (isGranted){
                    //Permiso aceptado
                    SeleccionarImagenCamara();
                }else {
                    //Permiso denegado
                    Toasty.error(ActualizarImagenPerfilActivity.this,"Permiso denegado",Toasty.LENGTH_SHORT).show();
                }
            });

}