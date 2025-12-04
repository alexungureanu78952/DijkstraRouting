package org.example.graph;

public class Edge {
    private final Node source;
    private final Node destination;
    private final double weight;

    public Edge(Node source, Node destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Edge(Node source, Node destination) {
        this.source = source;
        this.destination = destination;
        this.weight = Math.sqrt(Math.pow(destination.getX() - source.getX(), 2) + Math.pow(destination.getY() - source.getY(), 2));
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "source=" + source +
                ", destination=" + destination +
                ", weight=" + weight +
                '}';
    }
}
