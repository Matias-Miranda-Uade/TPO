package com.example.demo.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.TargetNode;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;

@RelationshipProperties
public class RoadRelationship {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    @Property
    private String nombre;

    @Property
    private Integer velocidad;

    @Property
    private Integer calleId;

    @Property
    private Integer peso;

    @TargetNode
    @JsonIgnoreProperties ({"roads"})
    private NodeEntity target;

    public RoadRelationship() {}

    public RoadRelationship(String nombre, Integer velocidad, Integer calleId, Integer peso, NodeEntity target) {
        this.nombre = nombre;
        this.velocidad = velocidad;
        this.calleId = calleId;
        this.peso = peso;
        this.target = target;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(Integer velocidad) {
        this.velocidad = velocidad;
    }

    public Integer getCalleId() {
        return calleId;
    }

    public void setCalleId(Integer calleId) {
        this.calleId = calleId;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public NodeEntity getTarget() {
        return target;
    }

    public void setTarget(NodeEntity target) {
        this.target = target;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getTargetId() {
        return target != null ? target.getEsquinaId() : null;
    }

}
