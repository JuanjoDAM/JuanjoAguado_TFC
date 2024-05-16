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

    // Getters
    public String getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getBreakHours() {
        return breakHours;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setBreakHours(String breakHours) {
        this.breakHours = breakHours;
    }
}


