package com.example.demo.algorithms;

import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ProgramacionDinamica {

    private static NodeRepository nodeRepo ;

    public ProgramacionDinamica(NodeRepository nodeRepo) {
        this.nodeRepo = nodeRepo;
    }

    /**
     * Aplica Floyd–Warshall al grafo almacenado en Neo4j.
     * Devuelve el camino más corto entre startId y endId (si existe).
     */
    public static Map<String, Object> floydWarshall(Integer startId, Integer endId) {
        List<NodeEntity> nodes = nodeRepo.findAll();
        if (nodes.isEmpty()) {
            return Map.of("error", "El grafo está vacío");
        }

        // Mapear índices <-> IDs
        List<Integer> nodeIds = new ArrayList<>();
        Map<Integer, Integer> idToIndex = new HashMap<>();
        for (int i = 0; i < nodes.size(); i++) {
            Integer id = nodes.get(i).getEsquinaId();
            nodeIds.add(id);
            idToIndex.put(id, i);
        }

        int n = nodes.size();
        final int INF = Integer.MAX_VALUE / 4;
        int[][] dist = new int[n][n];
        int[][] next = new int[n][n];

        // Inicialización
        for (int i = 0; i < n; i++) {
            Arrays.fill(dist[i], INF);
            dist[i][i] = 0;
            Arrays.fill(next[i], -1);
        }

        // Cargar distancias desde Neo4j
        for (NodeEntity node : nodes) {
            if (node.getRoads() == null) continue;
            int u = idToIndex.get(node.getEsquinaId());
            for (RoadRelationship road : node.getRoads()) {
                if (road == null || road.getTarget() == null) continue;
                Integer vId = road.getTarget().getEsquinaId();
                if (!idToIndex.containsKey(vId)) continue;
                int v = idToIndex.get(vId);
                int peso = road.getPeso() != null ? road.getPeso() : 1;
                dist[u][v] = peso;
                next[u][v] = v;
            }
        }

        // Algoritmo Floyd–Warshall
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                if (dist[i][k] >= INF) continue;
                for (int j = 0; j < n; j++) {
                    if (dist[k][j] >= INF) continue;
                    int nuevo = dist[i][k] + dist[k][j];
                    if (nuevo < dist[i][j]) {
                        dist[i][j] = nuevo;
                        next[i][j] = next[i][k];
                    }
                }
            }
        }

        // Validar nodos
        if (!idToIndex.containsKey(startId) || !idToIndex.containsKey(endId)) {
            return Map.of("error", "Uno o ambos nodos no existen en el grafo");
        }

        int uIdx = idToIndex.get(startId);
        int vIdx = idToIndex.get(endId);

        List<NodeEntity> path = reconstructNodePath(next, nodes, nodeIds, uIdx, vIdx);
        Integer distance = dist[uIdx][vIdx] >= INF ? null : dist[uIdx][vIdx];

        // 🔹 Calcular velocidad promedio del camino encontrado
        double velocidadTotal = 0;
        int cantidad = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            NodeEntity from = path.get(i);
            NodeEntity to = path.get(i + 1);
            if (from.getRoads() == null) continue;
            for (RoadRelationship r : from.getRoads()) {
                if (r.getTarget() != null && r.getTarget().getEsquinaId().equals(to.getEsquinaId())) {
                    if (r.getVelocidad() != null) {
                        velocidadTotal += r.getVelocidad();
                        cantidad++;
                    }
                    break;
                }
            }
        }

        Double velocidadPromedio = cantidad == 0 ? null : velocidadTotal / cantidad;

        return Map.of(
                "inicio", startId,
                "destino", endId,
                "distancia", distance,
                "camino", path,
                "velocidad_promedio", velocidadPromedio
        );
    }

    /**
     * Reconstruye el camino como lista de NodeEntity.
     */
    private static List<NodeEntity> reconstructNodePath(int[][] next, List<NodeEntity> nodes, List<Integer> nodeIds, int u, int v) {
        List<NodeEntity> path = new ArrayList<>();
        if (next[u][v] == -1) return path;

        int at = u;
        path.add(findNodeById(nodes, nodeIds.get(at)));

        while (at != v) {
            at = next[at][v];
            if (at == -1) return new ArrayList<>();
            path.add(findNodeById(nodes, nodeIds.get(at)));
        }

        return path;
    }

    /**
     * Busca un NodeEntity por su ID dentro de la lista.
     */
    private static NodeEntity findNodeById(List<NodeEntity> nodes, Integer id) {
        for (NodeEntity n : nodes) {
            if (Objects.equals(n.getEsquinaId(), id)) return n;
        }
        return null;
    }
}
