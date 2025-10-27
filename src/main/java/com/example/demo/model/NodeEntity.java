package com.example.demo.model;

import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Id;

@Node
public class NodeEntity {

    @Id
    private Integer esquinaId;

    public NodeEntity() {}

    public NodeEntity(Integer esquinaId) {
        this.esquinaId = esquinaId;
    }

    public Integer getEsquinaId() {
        return esquinaId;
    }

    public void setEsquinaId(Integer esquinaId) {
        this.esquinaId = esquinaId;
    }
}

