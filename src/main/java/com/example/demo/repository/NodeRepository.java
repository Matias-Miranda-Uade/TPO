package com.example.demo.repository;


import com.example.demo.model.NodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface NodeRepository extends Neo4jRepository<NodeEntity, Integer> {
}
