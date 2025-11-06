package com.example.demo.model;

public class PathEdgeDTO {
    private Integer calleId;
    private String nombre;
    private Integer velocidad;
    private Integer peso;

    public PathEdgeDTO(Integer calleId, String nombre, Integer velocidad, Integer peso) {
        this.calleId = calleId;
        this.nombre = nombre;
        this.velocidad = velocidad;
        this.peso = peso;
    }

    public Integer getCalleId() { return calleId; }
    public String getNombre() { return nombre; }
    public Integer getVelocidad() { return velocidad; }
    public Integer getPeso() { return peso; }
}

