package org.example.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
    private final List<Node> nodes = new ArrayList<>();
    private final Map<Node, List<Edge>> adj = new HashMap<>();

    public void addNode(Node node) {
        nodes.add(node);
        adj.putIfAbsent(node, new ArrayList<>());
    }

    public void addEdge(Node source, Node destination, double weight) {
        Edge edge = new Edge(source, destination, weight);
        adj.get(source).add(edge);
    }

    public void addEdge(Node source, Node destination) {
        Edge edge = new Edge(source, destination);
        adj.get(source).add(edge);
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Map<Node, List<Edge>> getAdj() {
        return adj;
    }
}
