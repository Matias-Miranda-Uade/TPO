package com.example.demo.model;

public class NodeDTO {
    private Integer id;
    private String nombre;

    public NodeDTO(Integer id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}
