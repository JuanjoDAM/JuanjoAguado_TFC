package com.juanjo.example.juanjoaguado_tfc;

public class DatosPersonales {

    private String dni;
    private String nombre;
    private String apellidos;
    private String correo;

    public DatosPersonales() {
        // Constructor vacío requerido para Firebase
    }

    public DatosPersonales(String dni, String nombre, String apellidos, String correo) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.correo = correo;
    }

    public String getDni() {
        return dni;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public String getCorreo() {
        return correo;
    }
}

