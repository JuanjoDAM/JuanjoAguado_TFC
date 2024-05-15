package com.juanjo.example.juanjoaguado_tfc;

public class HorasTrabajo {
    private String id;
    private String startTime;
    private String endTime;
    private String breakHours;

    public HorasTrabajo() {
        // Constructor vacío requerido para Firebase
    }

    public HorasTrabajo(String id, String startTime, String endTime, String breakHours) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.breakHours = breakHours;
    }

    // Getters y setters
}

