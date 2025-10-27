package com.example.demo.controller;


import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.service.GraphService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/graph")
public class GraphController {

    private final GraphService graphService;

    public GraphController(GraphService graphService) {
        this.graphService = graphService;
    }

    @PostMapping("/nodes")
    public NodeEntity createNode(@RequestBody NodeEntity node) {
        return graphService.saveNode(node);
    }

    @GetMapping("/nodes")
    public List<NodeEntity> getAllNodes() {
        return graphService.getAllNodes();
    }

    @PostMapping("/roads")
    public RoadRelationship createRoad(@RequestBody RoadRelationship road) {
        return graphService.saveRoad(road);
    }
}
