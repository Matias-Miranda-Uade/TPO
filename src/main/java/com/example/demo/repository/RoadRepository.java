package com.example.demo.repository;


import com.example.demo.model.RoadRelationship;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface RoadRepository extends Neo4jRepository<RoadRelationship, Integer> {
}
