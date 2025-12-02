package org.example.algorithm;

import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Dijkstra {
    public List<Edge> findShortestPath(Graph graph, Node start, Node end) {
        Map<Node, Double> distances = new HashMap<>();
        Map<Node, Edge> predecessors = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>((a, b) -> Double.compare(distances.get(a), distances.get(b)));

        for (Node node : graph.getNodes()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(start, 0.0);
        pq.add(start);

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            System.out.println("Processing node: " + current.getId() + " with distance: " + distances.get(current));

            if (current.equals(end)) {
                break;
            }

            // Ensure current node exists in adj map before iterating over its edges
            if (!graph.getAdj().containsKey(current)) {
                // This case should ideally not happen if graph is correctly built and all nodes in 'distances' map are in 'graph.getAdj()'
                // If a node is in 'distances' but not 'adj', it means it has no outgoing edges.
                continue;
            }

            for (Edge edge : graph.getAdj().get(current)) {
                Node neighbor = edge.getDestination();
                // Check if neighbor is in distances map. If not, it means it's an isolated node not initialized in distances
                // This scenario should be handled during graph initialization, ensuring all nodes are in distances map with MAX_VALUE
                if (!distances.containsKey(neighbor)) {
                    continue;
                }

                double newDist = distances.get(current) + edge.getWeight();
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, edge);
                    pq.add(neighbor); // Simply add the neighbor with the new, shorter distance
                    System.out.println("  Updating distance for neighbor: " + neighbor.getId() + " to " + newDist + " via " + current.getId());
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
