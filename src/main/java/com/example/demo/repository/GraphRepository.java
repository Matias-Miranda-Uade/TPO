package com.example.demo.repository;

import com.example.demo.model.NodeEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GraphRepository extends Neo4jRepository<NodeEntity, Integer> {

        @Query("""
           MATCH (n:Esquina)-[r:Calle]->(m:Esquina)
           RETURN n, collect(r), collect(m)
           """)
        List<NodeEntity> findAllWithRoads();
}
