package com.example.demo.algorithms;

import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;

import java.util.*;

/**
 * Algoritmo de Ramificación y Poda (Branch & Bound) para encontrar el camino más corto
 * entre dos nodos en el grafo de Neo4j.
 */
public class RamificacionYPoda {

    private final NodeRepository nodeRepo;

    public RamificacionYPoda(NodeRepository nodeRepo) {
        this.nodeRepo = nodeRepo;
    }

    public Map<String, Object> shortestPath(Integer startId, Integer endId) {
        List<NodeEntity> nodes = nodeRepo.findAll();
        if (nodes.isEmpty()) {
            return Map.of("error", "El grafo está vacío");
        }

        Optional<NodeEntity> startOpt = nodeRepo.findById(startId);
        Optional<NodeEntity> endOpt = nodeRepo.findById(endId);

        if (!startOpt.isPresent() || !endOpt.isPresent()) {
            return Map.of("error", "Nodos no encontrados en el grafo");
        }

        NodeEntity startNode = startOpt.get();
        NodeEntity endNode = endOpt.get();

        // Mejor camino y distancia mínima global
        List<NodeEntity> bestPath = new ArrayList<>();
        int[] bestDistance = {Integer.MAX_VALUE};

        // Estado inicial
        Set<Integer> visited = new HashSet<>();
        visited.add(startId);
        List<NodeEntity> currentPath = new ArrayList<>();
        currentPath.add(startNode);

        // Iniciar búsqueda
        branchAndBound(startNode, endNode, visited, currentPath, 0, bestPath, bestDistance);

        if (bestPath.isEmpty()) {
            return Map.of("inicio", startId, "destino", endId, "mensaje", "No hay camino disponible");
        }

        // 🔹 Calcular velocidad promedio del mejor camino
        double velocidadTotal = 0;
        int cantidad = 0;
        for (int i = 0; i < bestPath.size() - 1; i++) {
            NodeEntity from = bestPath.get(i);
            NodeEntity to = bestPath.get(i + 1);
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
                "distancia_minima", bestDistance[0],
                "camino", bestPath,
                "velocidad_promedio", velocidadPromedio
        );
    }

    /**
     * realiza ramificacion y poda de forma recursiva
     */
    private void branchAndBound(NodeEntity current, NodeEntity target,
                                Set<Integer> visited,
                                List<NodeEntity> currentPath,
                                int currentDist,
                                List<NodeEntity> bestPath,
                                int[] bestDistance) {

        if (current.getEsquinaId().equals(target.getEsquinaId())) {
            // Si encontramos el destino, actualizar mejor solución
            if (currentDist < bestDistance[0]) {
                bestDistance[0] = currentDist;
                bestPath.clear();
                bestPath.addAll(new ArrayList<>(currentPath));
            }
            return;
        }

        // Si ya tenemos una ruta mejor, poda
        if (currentDist >= bestDistance[0]) return;

        List<RoadRelationship> roads = current.getRoads();
        if (roads == null) return;

        for (RoadRelationship road : roads) {
            NodeEntity neighbor = road.getTarget();
            if (neighbor == null || neighbor.getEsquinaId() == null) continue;

            Integer neighborId = neighbor.getEsquinaId();
            int peso = road.getPeso() != null ? road.getPeso() : 1;

            if (visited.contains(neighborId)) continue; // evitar ciclos

            visited.add(neighborId);
            currentPath.add(neighbor);

            // Llamada recursiva con nueva distancia acumulada
            branchAndBound(neighbor, target, visited, currentPath, currentDist + peso, bestPath, bestDistance);

            // Backtracking
            visited.remove(neighborId);
            currentPath.remove(currentPath.size() - 1);
        }
    }
}
