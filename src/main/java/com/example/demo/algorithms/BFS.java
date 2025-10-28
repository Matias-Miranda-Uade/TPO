package com.example.demo.algorithms;

import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

public class BFS {

    /**
     * Realiza un recorrido BFS a partir de startId.
     * Devuelve la lista de NodeEntity en orden de visita.
     */
    public static List<NodeEntity> traverse (NodeRepository nodeRepo, Integer startId) {
        List<NodeEntity> order = new ArrayList<>();
        if (startId == null || nodeRepo == null) return order;

        Optional<NodeEntity> startOpt = nodeRepo.findById(startId);
        if (!startOpt.isPresent()) return order;

        Queue<NodeEntity> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();

        queue.add(startOpt.get());
        visited.add(startId);

        while (!queue.isEmpty()) {
            NodeEntity current = queue.poll();
            // recargar desde repo para asegurar relaciones/propiedades completas
            NodeEntity currentFull = nodeRepo.findById(current.getEsquinaId()).orElse(current);
            order.add(currentFull);

            List<RoadRelationship> roads = currentFull.getRoads();
            if (roads == null) continue;

            for (RoadRelationship road : roads) {
                if (road == null) continue;
                NodeEntity neighbor = road.getTarget();
                if (neighbor == null || neighbor.getEsquinaId() == null) continue;
                Integer nid = neighbor.getEsquinaId();
                if (!visited.contains(nid)) {
                    visited.add(nid);
                    // cargar vecino completo si existe en repo
                    NodeEntity neighborFull = nodeRepo.findById(nid).orElse(neighbor);
                    queue.add(neighborFull);
                }
            }
        }

        return order;
    }
}
