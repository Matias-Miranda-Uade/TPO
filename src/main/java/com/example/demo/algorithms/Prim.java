package com.example.demo.algorithms;

import com.example.demo.model.NodeEntity;
import com.example.demo.model.RoadRelationship;
import com.example.demo.repository.NodeRepository;

import java.util.*;

public class Prim {
    /**
     * Calcula el árbol generador mínimo comenzando desde startId.
     * Devuelve las relaciones seleccionadas (una por arista del MST).
     */
    public static List<RoadRelationship> minimumSpanningTree(NodeRepository nodeRepo, Integer startId) {
        List<RoadRelationship> result = new ArrayList<>();
        if (nodeRepo == null || startId == null) return result;

        Optional<NodeEntity> startOpt = nodeRepo.findById(startId);
        if (!startOpt.isPresent()) return result;

        // Mapa de todos los nodos (para fácil acceso)
        Map<Integer, NodeEntity> allNodes = new HashMap<>();
        for (NodeEntity n : nodeRepo.findAll()) {
            if (n != null && n.getEsquinaId() != null)
                allNodes.put(n.getEsquinaId(), n);
        }

        // Conjunto de nodos ya incluidos en el MST
        Set<Integer> inMST = new HashSet<>();
        // Cola de prioridad por peso (menor primero)
        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));

        Integer start = startId;
        inMST.add(start);
        NodeEntity startNode = allNodes.get(start);
        if (startNode == null) return result;

        // Inicializar con las aristas del nodo inicial
        addEdgesFromNode(startNode, pq);

        // Mientras haya aristas candidatas
        while (!pq.isEmpty() && inMST.size() < allNodes.size()) {
            Edge e = pq.poll();
            if (inMST.contains(e.v)) continue; // ya conectado

            // Añadir la arista al resultado
            result.add(e.road);

            // Incluir el nuevo nodo
            inMST.add(e.v);

            // Añadir nuevas aristas desde ese nodo
            NodeEntity nextNode = allNodes.get(e.v);
            addEdgesFromNode(nextNode, pq);
        }

        return result;
    }

    // Función auxiliar: agrega las aristas de un nodo a la cola de prioridad
    private static void addEdgesFromNode(NodeEntity node, PriorityQueue<Edge> pq) {
        if (node == null || node.getRoads() == null) return;
        for (RoadRelationship r : node.getRoads()) {
            if (r == null || r.getTarget() == null) continue;
            Integer targetId = r.getTarget().getEsquinaId();
            if (targetId == null) continue;
            int peso = r.getPeso() != null ? r.getPeso() : 0;
            pq.add(new Edge(node.getEsquinaId(), targetId, peso, r));
        }
    }

    // Clase auxiliar: representa una arista
    private static class Edge {
        final int u;
        final int v;
        final int weight;
        final RoadRelationship road;

        Edge(int u, int v, int weight, RoadRelationship road) {
            this.u = u;
            this.v = v;
            this.weight = weight;
            this.road = road;
        }
    }
}