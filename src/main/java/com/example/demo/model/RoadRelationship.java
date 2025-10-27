package com.example.demo.model;


import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.TargetNode;

@RelationshipProperties
public class RoadRelationship {

    @Property
    private Integer velocidad;

    @Property
    private Integer calleId;

    @Property
    private Integer peso;

    @TargetNode
    private NodeEntity target;

    public RoadRelationship() {}

    public RoadRelationship(Integer velocidad, Integer calleId, Integer peso, NodeEntity target) {
        this.velocidad = velocidad;
        this.calleId = calleId;
        this.peso = peso;
        this.target = target;
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
}
