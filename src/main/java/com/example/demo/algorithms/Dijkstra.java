// src/main/java/com/example/demo/algorithms/Dijkstra.java
package com.example.demo.algorithms;

import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;

import java.util.*;

public class Dijkstra {

    public static DijkstraResult shortestPath(NodeRepository nodeRepo, Integer startId, Integer targetId) {
        DijkstraResult result = new DijkstraResult();
        if (startId == null || nodeRepo == null) return result;

        Optional<NodeEntity> startOpt = nodeRepo.findById(startId);
        if (!startOpt.isPresent()) return result;

        Map<Integer, Integer> dist = new HashMap<>();
        Map<Integer, Integer> prev = new HashMap<>();
        PriorityQueue<PQNode> pq = new PriorityQueue<>(Comparator.comparingInt(n -> n.dist));
        Set<Integer> visited = new HashSet<>();

        dist.put(startId, 0);
        pq.add(new PQNode(startId, 0));

        while (!pq.isEmpty()) {
            PQNode cur = pq.poll();
            if (visited.contains(cur.id)) continue;
            visited.add(cur.id);

            if (targetId != null && cur.id == targetId) break;

            NodeEntity current = nodeRepo.findById(cur.id).orElse(null);
            if (current == null) continue;

            List<RoadRelationship> roads = current.getRoads();
            if (roads == null) continue;

            for (RoadRelationship road : roads) {
                if (road == null || road.getTarget() == null || road.getTarget().getEsquinaId() == null) continue;
                int nid = road.getTarget().getEsquinaId();
                int w = road.getPeso() != null ? road.getPeso() : 1;
                int nd = cur.dist + w;
                if (nd < dist.getOrDefault(nid, Integer.MAX_VALUE)) {
                    dist.put(nid, nd);
                    prev.put(nid, cur.id);
                    pq.add(new PQNode(nid, nd));
                }
            }
        }

        result.setDistances(dist);

        if (targetId != null && dist.containsKey(targetId)) {
            LinkedList<NodeEntity> path = new LinkedList<>();
            Integer at = targetId;
            while (at != null) {
                NodeEntity n = nodeRepo.findById(at).orElse(new NodeEntity(at));
                path.addFirst(n);
                at = prev.get(at);
            }
            result.setPath(new ArrayList<>(path));
            result.setDistance(dist.get(targetId));
        } else {
            result.setPath(Collections.emptyList());
            result.setDistance(null);
        }

        return result;
    }

    private static class PQNode {
        final int id;
        final int dist;
        PQNode(int id, int dist) { this.id = id; this.dist = dist; }
    }
}
