package org.example.algorithm;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Node;

import java.util.*;

public class Dijkstra {
    public List<Edge> findShortestPath(Graph graph, Node start, Node end) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Edge> predecessors = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingDouble(distances::get));
        Set<Node> visited = new HashSet<>();

        for (Node node : graph.getNodes()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();

            if (visited.contains(current)) {
                continue;
            }
            visited.add(current);

            if (current.equals(end)) {
                break;
            }

            if (!graph.getAdj().containsKey(current)) {
                continue;
            }

            for (Edge edge : graph.getAdj().get(current)) {
                Node neighbor = edge.getDestination();
                if (!distances.containsKey(neighbor)) {
                    continue;
                }

                double newDist = distances.get(current) + edge.getWeight();
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, edge);
                    pq.add(neighbor);
                }
            }
        }

        return reconstructPath(predecessors, end);
    }

    private List<Edge> reconstructPath(Map<Node, Edge> predecessors, Node end) {
        List<Edge> path = new ArrayList<>();
        Edge pred = predecessors.get(end);
        while (pred != null) {
            path.add(pred);
            pred = predecessors.get(pred.getSource());
        }
        Collections.reverse(path);
        return path;
    }
}
