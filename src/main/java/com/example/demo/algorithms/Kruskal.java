package com.example.demo.algorithms;

import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Kruskal {

    /**
     * Calcula un bosque generador mínimo (Kruskal) sobre el grafo.
     * Devuelve la lista de RoadRelationship seleccionadas (una por arista del MST).
     * Trata las aristas como no dirigidas: si existe A->B y B->A se considera la misma arista.
     */
    public static List<RoadRelationship> minimumSpanningForest(NodeRepository nodeRepo) {
        List<RoadRelationship> result = new ArrayList<>();
        if (nodeRepo == null) return result;

        List<NodeEntity> nodes = nodeRepo.findAll();
        if (nodes == null || nodes.isEmpty()) return result;

        Map<Integer, NodeEntity> nodeMap = new HashMap<>();
        for (NodeEntity n : nodes) {
            if (n != null && n.getEsquinaId() != null) nodeMap.put(n.getEsquinaId(), n);
        }

        // Recolectar aristas (u,v, peso) evitando duplicados (no dirigidas)
        List<Edge> edges = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (NodeEntity uNode : nodes) {
            if (uNode == null || uNode.getEsquinaId() == null) continue;
            Integer uId = uNode.getEsquinaId();
            List<RoadRelationship> roads = uNode.getRoads();
            if (roads == null) continue;
            for (RoadRelationship r : roads) {
                if (r == null || r.getTarget() == null || r.getTarget().getEsquinaId() == null) continue;
                Integer vId = r.getTarget().getEsquinaId();
                // clave canónica para arista no dirigida
                int a = Math.min(uId, vId);
                int b = Math.max(uId, vId);
                String key = a + "-" + b + "-" + (r.getCalleId() != null ? r.getCalleId() : "nocalle");
                if (seen.contains(key)) continue;
                seen.add(key);
                int weight = r.getPeso() != null ? r.getPeso() : 0;
                edges.add(new Edge(uId, vId, weight, r));
            }
        }

        // ordenar aristas por peso ascendente
        Collections.sort(edges, Comparator.comparingInt(e -> e.weight));

        // preparar DSU
        DSU dsu = new DSU();
        for (Integer id : nodeMap.keySet()) dsu.makeSet(id);

        // seleccionar aristas
        for (Edge e : edges) {
            if (dsu.find(e.u) != dsu.find(e.v)) {
                dsu.union(e.u, e.v);
                // construir RoadRelationship resultante apuntando al nodo target real (v)
                NodeEntity targetNode = nodeMap.get(e.v);
                RoadRelationship rel = new RoadRelationship(
                        e.orig.getNombre(),
                        e.orig.getVelocidad(),
                        e.orig.getCalleId(),
                        e.orig.getPeso(),
                        targetNode
                );
                result.add(rel);
            }
        }

        return result;
    }

    // helper: representación de arista
    private static class Edge {
        final int u;
        final int v;
        final int weight;
        final RoadRelationship orig;

        Edge(int u, int v, int weight, RoadRelationship orig) {
            this.u = u;
            this.v = v;
            this.weight = weight;
            this.orig = orig;
        }
    }

    // DSU / Union-Find simple con path compression + rank
    private static class DSU {
        private final Map<Integer, Integer> parent = new HashMap<>();
        private final Map<Integer, Integer> rank = new HashMap<>();

        void makeSet(int x) {
            parent.putIfAbsent(x, x);
            rank.putIfAbsent(x, 0);
        }

        int find(int x) {
            Integer p = parent.get(x);
            if (p == null) return x;
            if (p != x) {
                int root = find(p);
                parent.put(x, root);
                return root;
            }
            return x;
        }

        void union(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return;
            int raRank = rank.getOrDefault(ra, 0);
            int rbRank = rank.getOrDefault(rb, 0);
            if (raRank < rbRank) {
                parent.put(ra, rb);
            } else if (raRank > rbRank) {
                parent.put(rb, ra);
            } else {
                parent.put(rb, ra);
                rank.put(ra, raRank + 1);
            }
        }
    }
}
