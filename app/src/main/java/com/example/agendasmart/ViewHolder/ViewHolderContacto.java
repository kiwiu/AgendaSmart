package com.example.agendasmart.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agendasmart.R;

public class ViewHolderContacto extends RecyclerView.ViewHolder {
    View mview;

    private ViewHolderContacto.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); /*se ejecuta al presionar el item*/
        void onItemLongClick(View view, int position); /*se ejecuta al presionar el item*/
    }

    public void setOnclickListener(ViewHolderContacto.ClickListener clicklistener) {
        mClickListener = clicklistener;

    }

    public ViewHolderContacto(@NonNull View itemView) {
        super(itemView);
        mview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClickListener.onItemClick(view, getBindingAdapterPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                mClickListener.onItemLongClick(view, getBindingAdapterPosition());
                return false;
            }
        });
    }

    public void SetearDatosContacto(Context context, String id_contacto, String uid_contacto,
                                    String nombres, String apellidos, String correo, String telefono,
                                    String edad, String direccion,  String foto){
        ImageView imgContacto;
        TextView tvIdContacto, tvUidUsuario, tvNombreContacto, tvApellidoContacto, tvCorreoContacto,
                tvTelefonoContacto, tvEdadContacto, tvDireccionContacto;
        imgContacto = mview.findViewById(R.id.imgContacto);
        tvIdContacto = mview.findViewById(R.id.tvIdContacto);
        tvUidUsuario = mview.findViewById(R.id.tvUidUsuario);
        tvNombreContacto = mview.findViewById(R.id.tvNombreContacto);
        tvApellidoContacto = mview.findViewById(R.id.tvApellidoContacto);
        tvCorreoContacto = mview.findViewById(R.id.tvCorreoContacto);
        tvTelefonoContacto = mview.findViewById(R.id.tvTelefonoContacto);
        tvEdadContacto = mview.findViewById(R.id.tvEdadContacto);
        tvDireccionContacto = mview.findViewById(R.id.tvDireccionContacto);

        //setear datos
        tvIdContacto.setText(id_contacto);
        tvUidUsuario.setText(uid_contacto);
        tvNombreContacto.setText(nombres);
        tvApellidoContacto.setText(apellidos);
        tvCorreoContacto.setText(correo);
        tvTelefonoContacto.setText(telefono);
        tvEdadContacto.setText(edad);
        tvDireccionContacto.setText(direccion);

        //setear imagen
        try {
            // si la imagen del contacto existe en la base de datos se carga
            Glide.with(context).load(foto).placeholder(R.drawable.image).into(imgContacto);
        }catch (Exception e){
            // si la imagen del contacto no existe en la base de datos se carga una imagen por defecto
            Glide.with(context).load(R.drawable.image).into(imgContacto);
        }
    }
}
