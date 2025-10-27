package com.example.demo.service;


import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;
import com.example.demo.repository.RoadRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GraphService {

    private final NodeRepository nodeRepository;
    private final RoadRepository roadRepository;

    public GraphService(NodeRepository nodeRepository, RoadRepository roadRepository) {
        this.nodeRepository = nodeRepository;
        this.roadRepository = roadRepository;
    }

    public NodeEntity saveNode(NodeEntity node) {
        return nodeRepository.save(node);
    }

    public List<NodeEntity> getAllNodes() {
        return nodeRepository.findAll();
    }

    public RoadRelationship saveRoad(RoadRelationship road) {
        return roadRepository.save(road);
    }
}
