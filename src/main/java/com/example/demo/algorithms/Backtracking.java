
    package com.example.demo.algorithms;

    import com.example.demo.model.NodeEntity;
    import com.example.demo.model.RoadRelationship;
    import com.example.demo.repository.NodeRepository;

    import java.util.ArrayList;
    import java.util.HashSet;
    import java.util.List;
    import java.util.Optional;
    import java.util.Set;

    public class Backtracking {

        /**
         * Encuentra todos los caminos simples entre startId y endId.
         * Si maxDepth < 0 no hay límite de profundidad.
         * Devuelve una lista de caminos, cada camino es una lista de NodeEntity en orden desde start hasta end.
         */
        public static List<List<NodeEntity>> findAllSimplePaths(NodeRepository nodeRepo, Integer startId, Integer endId, int maxDepth) {
            List<List<NodeEntity>> results = new ArrayList<>();
            if (nodeRepo == null || startId == null || endId == null) return results;
            Set<Integer> visited = new HashSet<>();
            List<NodeEntity> currentPath = new ArrayList<>();
            dfs(nodeRepo, startId, endId, maxDepth, 0, visited, currentPath, results);
            return results;
        }

        private static void dfs(NodeRepository nodeRepo,
                                Integer currentId,
                                Integer targetId,
                                int maxDepth,
                                int depth,
                                Set<Integer> visited,
                                List<NodeEntity> currentPath,
                                List<List<NodeEntity>> results) {

            if (currentId == null) return;
            if (maxDepth >= 0 && depth > maxDepth) return;
            if (visited.contains(currentId)) return;

            Optional<NodeEntity> opt = nodeRepo.findById(currentId);
            if (!opt.isPresent()) return;
            NodeEntity node = opt.get();

            // agregar nodo al camino actual
            visited.add(currentId);
            currentPath.add(node);

            // si llegamos al objetivo, clonar camino y agregar a resultados
            if (currentId.equals(targetId)) {
                results.add(new ArrayList<>(currentPath));
            } else {
                List<RoadRelationship> roads = node.getRoads();
                if (roads != null) {
                    for (RoadRelationship road : roads) {
                        if (road == null) continue;
                        NodeEntity neighbor = road.getTarget();
                        if (neighbor == null || neighbor.getEsquinaId() == null) continue;
                        Integer nid = neighbor.getEsquinaId();
                        dfs(nodeRepo, nid, targetId, maxDepth, depth + 1, visited, currentPath, results);
                    }
                }
            }

            // backtrack
            currentPath.remove(currentPath.size() - 1);
            visited.remove(currentId);
        }
    }
