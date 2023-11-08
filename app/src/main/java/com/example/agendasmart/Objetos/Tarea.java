package com.example.agendasmart.Objetos;

public class Tarea {

    String id_tarea, uid_usuario, correo_usuario, fecha_hora_actual, titulo, descripcion, fecha_nota, estado;

    public Tarea() {
    }

    public Tarea(String id_tarea, String uid_usuario, String correo_usuario, String fecha_hora_actual, String titulo, String descripcion, String fecha_nota, String estado) {
        this.id_tarea = id_tarea;
        this.uid_usuario = uid_usuario;
        this.correo_usuario = correo_usuario;
        this.fecha_hora_actual = fecha_hora_actual;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha_nota = fecha_nota;
        this.estado = estado;
    }

    public String getId_tarea() {
        return id_tarea;
    }

    public void setId_tarea(String id_tarea) {
        this.id_tarea = id_tarea;
    }

    public String getUid_usuario() {
        return uid_usuario;
    }

    public void setUid_usuario(String uid_usuario) {
        this.uid_usuario = uid_usuario;
    }

    public String getCorreo_usuario() {
        return correo_usuario;
    }

    public void setCorreo_usuario(String correo_usuario) {
        this.correo_usuario = correo_usuario;
    }

    public String getFecha_horaactual() {
        return fecha_hora_actual;
    }

    public void setFecha_horaactual(String fecha_horaactual) {
        this.fecha_hora_actual = fecha_horaactual;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha_nota() {
        return fecha_nota;
    }

    public void setFecha_nota(String fecha_nota) {
        this.fecha_nota = fecha_nota;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
