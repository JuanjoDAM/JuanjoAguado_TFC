package com.juanjo.example.juanjoaguado_tfc;


import java.io.Serializable;

public class Solicitud implements Serializable {
    private String id;
    private String userId;
    private String tipo;
    private String fechaInicio;
    private String fechaFin;
    private String motivo;
    private String estado;
    private String comentario;

    // Constructor vacío requerido para Firebase
    public Solicitud() {}

    public Solicitud(String id, String userId, String tipo, String fechaInicio, String fechaFin, String motivo, String estado, String comentario) {
        this.id = id;
        this.userId = userId;
        this.tipo = tipo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.motivo = motivo;
        this.estado = estado;
        this.comentario = comentario;
    }

    // Getters y setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(String fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}



