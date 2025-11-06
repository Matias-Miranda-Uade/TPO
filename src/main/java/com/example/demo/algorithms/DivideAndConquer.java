package com.example.demo.algorithms;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DivideAndConquer {
    private final Driver driver;

    public DivideAndConquer(Driver driver) {
        this.driver = driver;
    }

    public static class PathSegment {
        private final List<Integer> nodes;
        private final int totalPeso;
        private final double velocidadPromedio;

        public PathSegment(List<Integer> nodes, int totalPeso, double velocidadPromedio) {
            this.nodes = nodes;
            this.totalPeso = totalPeso;
            this.velocidadPromedio = velocidadPromedio;
        }

        public List<Integer> getNodes() {
            return nodes;
        }

        public int getTotalPeso() {
            return totalPeso;
        }

        public double getVelocidadPromedio() {
            return velocidadPromedio;
        }
    }

    public PathSegment findOptimalPath(Integer startId, Integer endId) {
        // Obtener todos los nodos y relaciones del camino
        String query = """
            MATCH path = shortestPath((start:Esquina {esquinaId: $startId})-[:Calle*]-(end:Esquina {esquinaId: $endId}))
            UNWIND relationships(path) as r
            RETURN collect(distinct [startNode(r).esquinaId, endNode(r).esquinaId, r.peso, r.velocidad]) as segments
        """;

        try (Session session = driver.session()) {
            Result result = session.run(query,
                    Map.of("startId", startId, "endId", endId));

            if (!result.hasNext()) {
                return new PathSegment(List.of(), 0, 0);
            }

            Record record = result.next();
            List<List<Object>> segments = record.get("segments").asList(Value -> Value.asList());

            // Convertir los segmentos en subproblemas
            List<List<PathSegment>> subpaths = divideIntoSubpaths(segments);

            // Resolver recursivamente
            return solvePathRecursively(subpaths, 0, subpaths.size() - 1);
        }
    }

    private List<List<PathSegment>> divideIntoSubpaths(List<List<Object>> segments) {
        List<List<PathSegment>> result = new ArrayList<>();
        int size = segments.size();
        int subpathSize = (int) Math.sqrt(size); // Dividir en subgrupos aproximadamente iguales

        for (int i = 0; i < size; i += subpathSize) {
            List<PathSegment> subpath = new ArrayList<>();
            int end = Math.min(i + subpathSize, size);

            for (int j = i; j < end; j++) {
                List<Object> segment = segments.get(j);
                List<Integer> nodes = Arrays.asList(
                        ((Number) segment.get(0)).intValue(),
                        ((Number) segment.get(1)).intValue()
                );
                int peso = ((Number) segment.get(2)).intValue();
                int velocidad = ((Number) segment.get(3)).intValue();

                subpath.add(new PathSegment(nodes, peso, velocidad));
            }
            result.add(subpath);
        }
        return result;
    }

    private PathSegment solvePathRecursively(List<List<PathSegment>> subpaths, int start, int end) {
        // Caso base: un solo subpath
        if (start == end) {
            return mergePath(subpaths.get(start));
        }

        // Dividir y conquistar
        int mid = (start + end) / 2;
        PathSegment leftPath = solvePathRecursively(subpaths, start, mid);
        PathSegment rightPath = solvePathRecursively(subpaths, mid + 1, end);

        // Combinar resultados
        return mergePaths(leftPath, rightPath);
    }

    private PathSegment mergePath(List<PathSegment> segments) {
        List<Integer> mergedNodes = new ArrayList<>();
        int totalPeso = 0;
        double totalVelocidad = 0;

        for (PathSegment segment : segments) {
            if (mergedNodes.isEmpty()) {
                mergedNodes.addAll(segment.getNodes());
            } else {
                mergedNodes.add(segment.getNodes().get(1));
            }
            totalPeso += segment.getTotalPeso();
            totalVelocidad += segment.getVelocidadPromedio();
        }

        return new PathSegment(
                mergedNodes,
                totalPeso,
                segments.isEmpty() ? 0 : totalVelocidad / segments.size()
        );
    }

    private PathSegment mergePaths(PathSegment left, PathSegment right) {
        List<Integer> mergedNodes = new ArrayList<>(left.getNodes());
        if (!right.getNodes().isEmpty()) {
            mergedNodes.addAll(right.getNodes().subList(1, right.getNodes().size()));
        }

        return new PathSegment(
                mergedNodes,
                left.getTotalPeso() + right.getTotalPeso(),
                (left.getVelocidadPromedio() + right.getVelocidadPromedio()) / 2
        );
    }
}
