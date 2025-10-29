package com.example.demo.controller;


import com.example.demo.algorithms.*;
import com.example.demo.algorithms.DivideAndConquer;
import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.service.GraphService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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
    @GetMapping("/bfs")
    public List<NodeEntity> bfs(@RequestParam Integer startId) {
        return graphService.bfs(startId);
    }

    @GetMapping("/dfs")
    public List<NodeEntity> dfs(@RequestParam Integer startId) {
        return graphService.dfs(startId);
    }

    @GetMapping("/backtracking")
    public List<List<NodeEntity>> backtracking(
            @RequestParam Integer startId,
            @RequestParam Integer endId,
            @RequestParam(defaultValue = "-1") int maxDepth) {
        return graphService.backtracking(startId, endId, maxDepth);
    }

    @GetMapping("/dijkstra")
    public DijkstraResult dijkstra(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {
        return graphService.dijkstra(startId, endId);
    }

    @GetMapping("/kruskal")
    public List<RoadRelationship> kruskal() {
        return graphService.kruskal();
    }

    @GetMapping
    public List<RoadRelationship> getPrimMST(@RequestParam Integer start) {
        return graphService.primMST(start);
    }

    @GetMapping("/greedy")
    public Greedy.PathResult findGreedyPath(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {
        return graphService.greedy(startId, endId);
    }

    @GetMapping("/divide-conquer")
    public DivideAndConquer.PathSegment findDivideConquerPath(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {
        return graphService.divideAndConquer(startId, endId);
    }


    /**
     * Endpoint: ejecuta el algoritmo de Programación Dinámica (Floyd–Warshall)
     * Ejemplo de uso:
     * GET /api/grafo/floyd-warshall?startId=1&endId=5
     */
    @GetMapping("/floyd-warshall")
    public ResponseEntity<Map<String, Object>> floydWarshall(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {

        Map<String, Object> result = graphService.calcularCaminoMinimo(startId, endId);

        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/ramificacion-poda")
    public ResponseEntity<Map<String, Object>> ramificacionYPoda(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {

        Map<String, Object> result = graphService.caminoRamificacionYPoda(startId, endId);

        if (result.containsKey("error")) {
            return ResponseEntity.badRequest().body(result);
        }

        return ResponseEntity.ok(result);
    }
}
