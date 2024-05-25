package com.juanjo.example.juanjoaguado_tfc;



public class DatosPersonales {
    private String dni;
    private String nombre;
    private String apellidos;
    private String correo;

    public DatosPersonales() {
        // Default constructor required for calls to DataSnapshot.getValue(DatosPersonales.class)
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

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
