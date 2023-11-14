package com.example.agendasmart.ViewHolder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agendasmart.R;

public class ViewHolder_Tarea extends RecyclerView.ViewHolder {

    View mview;

    private ViewHolder_Tarea.ClickListener mClickListener;

    public interface ClickListener{
        void onItemClick(View view, int position); /*se ejecuta al presionar el item*/
        void onItemLongClick(View view, int position); /*se ejecuta al presionar el item*/
    }

    public void setOnclickListener(ViewHolder_Tarea.ClickListener clicklistener) {
        mClickListener = clicklistener;

    }

    public ViewHolder_Tarea(@NonNull View itemView) {
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

    public void SetearDatos(Context context, String id_tarea, String uid_usuario,  String correo_usuario,
                            String fecha_hora_registro, String titulo, String descripcion, String fecha_tarea, String estado){

        /*Declarar las vistas*/

        TextView id_tarea_item, uid_usuario_item, correo_usuario_item, titulo_item,
                descripcion_item, fecha_hora_registro_item, fecha_item, estado_item;

        ImageView Tarea_Finalizada_Item, Tarea_No_Finalizada_Item;

        /*Establecer conexion con el item*/

        id_tarea_item = mview.findViewById(R.id.id_tarea_item);
        uid_usuario_item = mview.findViewById(R.id.uid_usuario_item);
        correo_usuario_item = mview.findViewById(R.id.correo_usuario_item);
        fecha_hora_registro_item = mview.findViewById(R.id.fecha_hora_registro_item);
        titulo_item = mview.findViewById(R.id.titulo_item);
        descripcion_item = mview.findViewById(R.id.descripcion_item);
        fecha_item = mview.findViewById(R.id.fecha_item);
        estado_item = mview.findViewById(R.id.estado_item);

        Tarea_Finalizada_Item = mview.findViewById(R.id.Tarea_Finalizada_Item);
        Tarea_No_Finalizada_Item = mview.findViewById(R.id.Tarea_No_Finalizada_Item);

        /*setear info dentro del item*/
        id_tarea_item.setText(id_tarea);
        uid_usuario_item.setText(uid_usuario);
        correo_usuario_item.setText(correo_usuario);
        fecha_hora_registro_item.setText(fecha_hora_registro);
        titulo_item.setText(titulo);
        descripcion_item.setText(descripcion);
        fecha_item.setText(fecha_tarea);
        estado_item.setText(estado);

        /*Gestion del color del estado*/
        if(estado.equals("Finalizada")){
            Tarea_Finalizada_Item.setVisibility((View.VISIBLE));
        }else{
            Tarea_No_Finalizada_Item.setVisibility((View.VISIBLE));
        }
    }
}
