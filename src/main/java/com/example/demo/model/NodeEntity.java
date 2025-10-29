package com.example.demo.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.ArrayList;
import java.util.List;

@Node
public class NodeEntity {

    @Id
    private Integer esquinaId;
    private String nombre;

    @Relationship(direction=Relationship.Direction.OUTGOING)
    private List<RoadRelationship> roads = new ArrayList<>();

    public NodeEntity() {}

    public NodeEntity(Integer esquinaId) {
        this.esquinaId = esquinaId;
    }

    public NodeEntity (Integer esquinaId, String nombre) {
        this.esquinaId = esquinaId;
        this.nombre = nombre;
    }

    public Integer getEsquinaId() {
        return esquinaId;
    }
    public void setEsquinaId(Integer esquinaId){
        this.esquinaId = esquinaId;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public List<RoadRelationship> getRoads() {
        return roads;
    }
    public void setRoads(List<RoadRelationship> roads) {
        this.roads = roads;
    }
}

