package com.juanjo.example.juanjoaguado_tfc;


public class Nomina {
    private String userId;
    private String url;
    private String nombreArchivo;

    public Nomina() {
        // Constructor vacío requerido para Firebase
    }

    public Nomina(String userId, String nombreArchivo, String url) {
        this.userId = userId;
        this.nombreArchivo = nombreArchivo;
        this.url = url;
    }
    public Nomina(String userId, String nombreArchivo) {
        this.userId = userId;
        this.nombreArchivo = nombreArchivo;
    }

    // Getters y setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }
}



