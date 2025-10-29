package com.example.demo.algorithms;

import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DFS {

    /**
     * Realiza un recorrido DFS (profundidad) a partir de startId.
     * Devuelve la lista de NodeEntity en orden de visita.
     */
    public static List<NodeEntity> traverse(NodeRepository nodeRepo, Integer startId) {
        List<NodeEntity> order = new ArrayList<>();
        if (nodeRepo == null || startId == null) return order;
        Set<Integer> visited = new HashSet<>();
        dfs(nodeRepo, startId, visited, order);
        return order;
    }

    private static void dfs(NodeRepository nodeRepo, Integer id, Set<Integer> visited, List<NodeEntity> order) {
        if (id == null || visited.contains(id)) return;

        Optional<NodeEntity> opt = nodeRepo.findById(id);
        if (!opt.isPresent()) return;

        NodeEntity node = opt.get();
        visited.add(id);
        order.add(node);

        List<RoadRelationship> roads = node.getRoads();
        if (roads == null) return;

        for (RoadRelationship road : roads) {
            if (road == null) continue;
            NodeEntity neighbor = road.getTarget();
            if (neighbor == null || neighbor.getEsquinaId() == null) continue;
            dfs(nodeRepo, neighbor.getEsquinaId(), visited, order);
        }
    }
}
