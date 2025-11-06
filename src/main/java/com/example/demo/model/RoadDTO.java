package com.example.demo.model;

public class RoadDTO {
    public Integer targetId;
    public String nombre;
    public Integer peso;
    public Integer velocidad;
    public Integer calleId;

    public RoadDTO(Integer calleId, Integer targetId, String nombre, Integer velocidad, Integer peso) {
        this.calleId=calleId;
        this.targetId = targetId;
        this.nombre = nombre;
        this.velocidad = velocidad;
        this.peso = peso;
    }



    public Integer getTargetId() {
        return targetId;
    }

    public void setTargetId(Integer targetId) {
        this.targetId = targetId;
    }


    public String getNombre() {
        return nombre;
    }


    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public Integer getVelocidad() {
        return velocidad;
    }


    public void setVelocidad(Integer velocidad) {
        this.velocidad = velocidad;
    }


    public Integer getPeso() {
        return peso;
    }


    public void setPeso(Integer peso) {
        this.peso = peso;
    }


    public Integer getCalleId() {
        return calleId;
    }


    public void setCalleId(Integer calleId) {
        this.calleId = calleId;
    }
}

