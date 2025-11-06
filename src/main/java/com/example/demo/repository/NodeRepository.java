package com.example.demo.repository;


import com.example.demo.model.NodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;

import java.util.List;

public interface NodeRepository extends Neo4jRepository<NodeEntity, Integer> {
}