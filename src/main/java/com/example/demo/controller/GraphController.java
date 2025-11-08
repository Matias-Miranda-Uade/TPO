package com.example.demo.controller;


import com.example.demo.algorithms.*;
import com.example.demo.algorithms.DivideAndConquer;
import com.example.demo.model.*;
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


    @GetMapping("/bfs")
    public List<NodeDTO> bfs(@RequestParam Integer startId) {
        return graphService.bfs(startId);
    }

    @GetMapping("/dfs")
    public List<NodeDTO> dfs(@RequestParam Integer startId) {
        return graphService.dfs(startId);
    }

    @GetMapping("/backtracking")
    public List<List<NodeDTO>> backtracking(
            @RequestParam Integer startId,
            @RequestParam Integer endId,
            @RequestParam(defaultValue = "-1") int maxDepth) {
        return graphService.backtracking(startId, endId, maxDepth);
    }

    @GetMapping("/dijkstra")
    public GraphService.DijkstraResultDTO dijkstra(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {
        return graphService.dijkstra(startId, endId);
    }

    @GetMapping("/kruskal")
    public List<RoadDTO> kruskal() {
        return graphService.kruskal();
    }

    @GetMapping("/prim")
    public List<RoadDTO> getPrimMST(@RequestParam Integer start) {
        return graphService.primMST(start);
    }

    @GetMapping("/greedy")
    public ResponseEntity<PathResultDTO> greedy(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {

        PathResultDTO result = graphService.greedy(startId, endId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/divide-conquer")
    public ResponseEntity<PathResultDTO> divideAndConquer(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {

        PathResultDTO result = graphService.divideAndConquer(startId, endId);
        return ResponseEntity.ok(result);
    }


    /**
     * Endpoint: ejecuta el algoritmo de Programación Dinámica (Floyd–Warshall)
     * Ejemplo de uso:
     * GET /api/grafo/floyd-warshall?startId=1&endId=5
     */
    @GetMapping("/floyd-warshall")
    public ResponseEntity<PathResultDTO> floydWarshall(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {

        PathResultDTO result = graphService.calcularCaminoMinimo(startId, endId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/branch-bound")
    public ResponseEntity<PathResultDTO> branchBound(
            @RequestParam Integer startId,
            @RequestParam Integer endId) {

        PathResultDTO result = graphService.caminoRamificacionYPoda(startId, endId);
        return ResponseEntity.ok(result);
    }
}
