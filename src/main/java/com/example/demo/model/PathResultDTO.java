package com.example.demo.model;

    import java.util.List;

    public class PathResultDTO {
        private List<PathNodeDTO> nodes;
        private List<PathEdgeDTO> edges;
        private Integer totalPeso;
        private Double velocidadPromedio;

        public PathResultDTO(List<PathNodeDTO> nodes, List<PathEdgeDTO> edges,
                             Integer totalPeso, Double velocidadPromedio) {
            this.nodes = nodes;
            this.edges = edges;
            this.totalPeso = totalPeso;
            this.velocidadPromedio = velocidadPromedio;
        }

        public PathResultDTO(List<PathNodeDTO> nodes, List<RoadDTO> edges, int totalPeso, double velocidadPromedio) {
            this.nodes=nodes;
            this.edges=edges.stream().map(edge -> new PathEdgeDTO(
                    edge.getCalleId(),
                    edge.getNombre(),
                    edge.getPeso(),
                    edge.getVelocidad()
            )).toList();
            this.totalPeso=totalPeso;
            this.velocidadPromedio=velocidadPromedio;
        }

        public List<PathNodeDTO> getNodes() { return nodes; }
        public List<PathEdgeDTO> getEdges() { return edges; }
        public Integer getTotalPeso() { return totalPeso; }
        public Double getVelocidadPromedio() { return velocidadPromedio; }
    }
