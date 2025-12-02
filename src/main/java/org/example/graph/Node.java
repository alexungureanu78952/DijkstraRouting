package org.example.graph;

public class Node {
    private final String id;
    private final double x;
    private final double y;

    public Node(String id, double x, double y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id='" + id + "'" +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
