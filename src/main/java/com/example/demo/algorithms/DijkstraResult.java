package com.example.demo.algorithms;

import com.example.demo.model.NodeEntity;

import java.util.List;
import java.util.Map;

public class DijkstraResult {
    private Integer distance;
    private List<NodeEntity> path;
    private Map<Integer, Integer> distances;

    public DijkstraResult() {}

    public Integer getDistance() { return distance; }
    public void setDistance(Integer distance) { this.distance = distance; }

    public List<NodeEntity> getPath() { return path; }
    public void setPath(List<NodeEntity> path) { this.path = path; }

    public Map<Integer, Integer> getDistances() { return distances; }
    public void setDistances(Map<Integer, Integer> distances) { this.distances = distances; }
}
