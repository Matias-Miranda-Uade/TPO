package com.example.demo.service;


import com.example.demo.algorithms.*;
import com.example.demo.model.*;
import com.example.demo.repository.GraphRepository;
import com.example.demo.repository.NodeRepository;
import com.example.demo.repository.RoadRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;

import java.util.*;

@Service
public class GraphService {

    private final NodeRepository nodeRepo;
    @Autowired
    private final GraphRepository graphRepository;


    public GraphService(NodeRepository nodeRepository, GraphRepository graphRepository) {
        this.nodeRepo = nodeRepository;

        this.graphRepository = graphRepository;
    }

    public NodeEntity saveNode(NodeEntity node) {
        return nodeRepo.save(node);
    }

    public List<NodeEntity> getAllNodes() {
        return graphRepository.findAllWithRoads();
    }

    public NodeEntity conectarCalles(Integer origenId, RoadRelationship road) {
        NodeEntity origen = nodeRepo.findById(origenId).orElseThrow();

        origen.getRoads().add(road);
        return nodeRepo.save(origen);
    }

    // BFS
    public List<NodeDTO> bfs(Integer startId) {
        List<NodeEntity> result= BFS.traverse(nodeRepo, startId);
        return toNodeDTOList(result);
    }

    // DFS
    public List<NodeDTO> dfs(Integer startId) {
        List<NodeEntity> result= DFS.traverse(nodeRepo, startId);
        return toNodeDTOList(result);
    }

    // Backtracking
    public List<List<NodeDTO>> backtracking(Integer startId, Integer endId, int maxDepth) {
        List<List<NodeEntity>> paths = Backtracking.findAllSimplePaths(nodeRepo, startId, endId, maxDepth);
        return paths.stream().map(this::toNodeDTOList).toList();
    }

    // Dijkstra
    public DijkstraResultDTO dijkstra(Integer startId, Integer endId) {
        DijkstraResult res = Dijkstra.shortestPath(nodeRepo, startId, endId);

        return new DijkstraResultDTO(
                toNodeDTOList(res.getPath()),
                res.getDistance()
        );
    }

    public record DijkstraResultDTO(List<NodeDTO> path, double distancia) {}


    // Kruskal
    public List<RoadDTO> kruskal() {
        return toRoadDTOList(Kruskal.minimumSpanningForest(nodeRepo));
    }

    // Prim
    public List<RoadDTO> primMST(Integer startId) {
        return toRoadDTOList(Prim.minimumSpanningTree(nodeRepo, startId));
    }
    // divide y conquista
    @Autowired
    private DivideAndConquer divideAndConquerPath;

    public PathResultDTO divideAndConquer(Integer startId, Integer endId) {
        DivideAndConquer.PathSegment result = divideAndConquerPath.findOptimalPath(startId, endId);

        List<PathNodeDTO> nodes = result.getNodes().stream()
                .map(id -> {
                    NodeEntity n = nodeRepo.findById(id).orElse(null);
                    return new PathNodeDTO(id, n != null ? n.getNombre() : "??");
                })
                .toList();

        List<RoadDTO> edges = buildEdgesFromNodes(result.getNodes());

        return new PathResultDTO(nodes, edges, result.getTotalPeso(), result.getVelocidadPromedio());
    }



    //greedy

    public PathResultDTO greedy(Integer startId, Integer endId) {
        Greedy.PathResult result = Greedy.findGreedyPath(startId, endId);

        List<PathNodeDTO> nodes = result.getPath().stream()
                .map(id -> {
                    NodeEntity n = nodeRepo.findById(id).orElse(null);
                    return new PathNodeDTO(id, n != null ? n.getNombre() : "??");
                })
                .toList();

        List<RoadDTO> edges = buildEdgesFromNodes(result.getPath());

        return new PathResultDTO(nodes, edges, result.getTotalPeso(), result.getVelocidadPromedio());
    }



    /**
     * Ejecuta el algoritmo de Floyd–Warshall para obtener el camino más corto entre dos nodos.
     */
    public PathResultDTO calcularCaminoMinimo(Integer startId, Integer endId) {
        Map<String, Object> r = ProgramacionDinamica.floydWarshall(startId, endId);

        List<NodeEntity> list = (List<NodeEntity>) r.get("camino");
        if (list == null) list = List.of();

        List<PathNodeDTO> nodes = list.stream()
                .map(n -> new PathNodeDTO(n.getEsquinaId(), n.getNombre()))
                .toList();

        List<Integer> ids = list.stream().map(NodeEntity::getEsquinaId).toList();
        List<RoadDTO> edges = buildEdgesFromNodes(ids);

        Integer dist = (Integer) r.get("distancia");

        Double velocidadProm = (Double) r.get("velocidad_promedio");

        return new PathResultDTO(nodes, edges, dist, velocidadProm);
    }


    public PathResultDTO caminoRamificacionYPoda(Integer startId, Integer endId) {
        RamificacionYPoda ryp = new RamificacionYPoda(nodeRepo);
        Map<String, Object> r = ryp.shortestPath(startId, endId);

        List<NodeEntity> list = (List<NodeEntity>) r.get("camino");
        if (list == null) list = List.of();

        List<PathNodeDTO> pathNodes = list.stream()
                .map(n -> new PathNodeDTO(n.getEsquinaId(), n.getNombre()))
                .toList();

        List<Integer> ids = list.stream().map(NodeEntity::getEsquinaId).toList();
        List<RoadDTO> edges = buildEdgesFromNodes(ids);

        Integer dist = (Integer) r.get("distancia_minima");

        Double velocidadProm = (Double) r.get("velocidad_promedio");

        return new PathResultDTO(pathNodes, edges, dist, velocidadProm);
    }



    private NodeDTO toNodeDTO(NodeEntity n) {
        return new NodeDTO(n.getEsquinaId(), n.getNombre());
    }

    private List<NodeDTO> toNodeDTOList(List<NodeEntity> nodes) {
        return nodes.stream().map(this::toNodeDTO).toList();
    }

    private RoadDTO toRoadDTO(RoadRelationship r) {
        return new RoadDTO(
                r.getCalleId(),
                r.getTargetId(),
                r.getNombre(),
                r.getPeso(),
                r.getVelocidad()
        );
    }

    private List<RoadDTO> toRoadDTOList(List<RoadRelationship> roads) {
        return roads.stream().map(this::toRoadDTO).toList();
    }


    private List<RoadDTO> buildEdgesFromNodes(List<Integer> nodeIds) {
        List<RoadDTO> edges = new ArrayList<>();

        for (int i = 0; i < nodeIds.size() - 1; i++) {
            Integer from = nodeIds.get(i);
            Integer to = nodeIds.get(i + 1);

            // Buscar la arista real en Neo4j
            NodeEntity node = nodeRepo.findById(from).orElse(null);
            if (node == null || node.getRoads() == null) continue;

            for (RoadRelationship r : node.getRoads()) {
                if (r.getTarget() != null && r.getTarget().getEsquinaId().equals(to)) {
                    edges.add(new RoadDTO(
                            r.getCalleId(),
                            to,
                            r.getNombre(),
                            r.getPeso(),
                            r.getVelocidad()
                    ));
                    break;
                }
            }
        }

        return edges;
    }

}

