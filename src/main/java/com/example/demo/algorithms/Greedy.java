package com.example.demo.algorithms;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Greedy {
    private static Driver driver;

    public Greedy(Driver driver) {
        this.driver = driver;
    }

    public static class PathResult {
        private final List<Integer> path;
        private final int totalPeso;
        private final double velocidadPromedio;

        public PathResult(List<Integer> path, int totalPeso, double velocidadPromedio) {
            this.path = path;
            this.totalPeso = totalPeso;
            this.velocidadPromedio = velocidadPromedio;
        }

        public List<Integer> getPath() {
            return path;
        }

        public int getTotalPeso() {
            return totalPeso;
        }

        public double getVelocidadPromedio() {
            return velocidadPromedio;
        }
    }

    public static PathResult findGreedyPath(Integer startId, Integer endId) {
        List<Integer> path = new ArrayList<>();
        path.add(startId);

        int totalPeso = 0;
        double totalVelocidad = 0;
        int edges = 0;
        Integer currentNode = startId;

        // Conjunto para evitar ciclos
        Set<Integer> visited = new HashSet<>();
        visited.add(startId);

        while (currentNode != null && !currentNode.equals(endId)) {
            // Consulta Cypher para obtener vecinos no visitados
            String query = """
                MATCH (current:NodeEntity {esquinaId: $currentId})-[r:ROAD]->(next:NodeEntity)
                WHERE NOT next.esquinaId IN $visitedIds
                RETURN next.esquinaId as nextId, r.peso as peso, r.velocidad as velocidad
                ORDER BY r.peso ASC
                LIMIT 1
            """;

            try (Session session = driver.session()) {
                Result result = session.run(query,
                        Map.of("currentId", currentNode,
                                "visitedIds", visited));

                if (!result.hasNext()) {
                    // No hay camino disponible
                    break;
                }

                Record record = result.next();
                Integer nextId = record.get("nextId").asInt();
                int peso = record.get("peso").asInt();
                int velocidad = record.get("velocidad").asInt();

                // Actualizar el camino y métricas
                path.add(nextId);
                visited.add(nextId);
                totalPeso += peso;
                totalVelocidad += velocidad;
                edges++;
                currentNode = nextId;
            }
        }

        double velocidadPromedio = edges > 0 ? totalVelocidad / edges : 0;
        return new PathResult(path, totalPeso, velocidadPromedio);
    }
}
