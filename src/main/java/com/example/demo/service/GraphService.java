package com.example.demo.service;


import com.example.demo.algorithms.*;
import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;
import com.example.demo.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class GraphService {

    private final NodeRepository nodeRepo;
    private final RoadRepository roadRepository;
    private final ProgramacionDinamica programacionDinamica;

    public GraphService(NodeRepository nodeRepository, RoadRepository roadRepository, ProgramacionDinamica programacionDinamica) {
        this.nodeRepo = nodeRepository;
        this.roadRepository = roadRepository;
        this.programacionDinamica = programacionDinamica;
    }

    public NodeEntity saveNode(NodeEntity node) {
        return nodeRepo.save(node);
    }

    public List<NodeEntity> getAllNodes() {
        return nodeRepo.findAll();
    }

    public RoadRelationship saveRoad(RoadRelationship road) {
        return roadRepository.save(road);
    }

    // BFS
    public List<NodeEntity> bfs(Integer startId) {
        return BFS.traverse(nodeRepo, startId);
    }

    // DFS
    public List<NodeEntity> dfs(Integer startId) {
        return DFS.traverse(nodeRepo, startId);
    }

    // Backtracking
    public List<List<NodeEntity>> backtracking(Integer startId, Integer endId, int maxDepth) {
        return Backtracking.findAllSimplePaths(nodeRepo, startId, endId, maxDepth);
    }

    // Dijkstra
    public DijkstraResult dijkstra(Integer startId, Integer endId) {
        return Dijkstra.shortestPath(nodeRepo, startId, endId);
    }

    // Kruskal
    public List<RoadRelationship> kruskal() {
        return Kruskal.minimumSpanningForest(nodeRepo);
    }

    // Prim
    public List<RoadRelationship> primMST(Integer startId) {
        return Prim.minimumSpanningTree(nodeRepo, startId);
    }
    // divide y conquista
    @Autowired
    private DivideAndConquer divideAndConquerPath;

    public DivideAndConquer.PathSegment divideAndConquer(Integer startId, Integer endId) {
        if (startId == null || endId == null) {
            throw new IllegalArgumentException("Los IDs de inicio y fin no pueden ser nulos");
        }
        return divideAndConquerPath.findOptimalPath(startId, endId);
    }


    //greedy

    public Greedy.PathResult greedy(Integer startId, Integer endId) {
        if (startId == null || endId == null) {
            throw new IllegalArgumentException("Los IDs de inicio y fin no pueden ser nulos");
        }
        return Greedy.findGreedyPath(startId, endId);
    }


    /**
     * Ejecuta el algoritmo de Floyd–Warshall para obtener el camino más corto entre dos nodos.
     */
    public Map<String, Object> calcularCaminoMinimo(Integer startId, Integer endId) {
        return programacionDinamica.floydWarshall(startId, endId);
    }

    public Map<String, Object> caminoRamificacionYPoda(Integer startId, Integer endId) {
        RamificacionYPoda ryp = new RamificacionYPoda(nodeRepo);
        return ryp.shortestPath(startId, endId);
    }
}

