package org.example.kdtree;

import org.example.graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KDTree {
    private KDNode root;

    private static class KDNode {
        Node node;
        KDNode left;
        KDNode right;

        KDNode(Node node) {
            this.node = node;
        }
    }

    public KDTree(List<Node> nodes) {
        root = buildTree(nodes, 0);
    }

    private KDNode buildTree(List<Node> nodes, int depth) {
        if (nodes.isEmpty()) {
            return null;
        }

        int axis = depth % 2;
        nodes.sort((a, b) -> {
            if (axis == 0) {
                return Double.compare(a.getX(), b.getX());
            } else {
                return Double.compare(a.getY(), b.getY());
            }
        });

        int median = nodes.size() / 2;
        KDNode node = new KDNode(nodes.get(median));
        node.left = buildTree(new ArrayList<>(nodes.subList(0, median)), depth + 1);
        node.right = buildTree(new ArrayList<>(nodes.subList(median + 1, nodes.size())), depth + 1);

        return node;
    }

    public Node findNearest(double x, double y) {
        return findNearest(root, x, y, 0).node;
    }

    private KDNode findNearest(KDNode node, double x, double y, int depth) {
        if (node == null) {
            return null;
        }

        int axis = depth % 2;
        KDNode nextBranch = null;
        KDNode otherBranch = null;

        if (axis == 0) {
            if (x < node.node.getX()) {
                nextBranch = node.left;
                otherBranch = node.right;
            } else {
                nextBranch = node.right;
                otherBranch = node.left;
            }
        } else {
            if (y < node.node.getY()) {
                nextBranch = node.left;
                otherBranch = node.right;
            } else {
                nextBranch = node.right;
                otherBranch = node.left;
            }
        }

        KDNode best = findNearest(nextBranch, x, y, depth + 1);
        if (best == null || distance(node.node, x, y) < distance(best.node, x, y)) {
            best = node;
        }

        double radius = distance(best.node, x, y);
        double dist;
        if (axis == 0) {
            dist = Math.abs(node.node.getX() - x);
        } else {
            dist = Math.abs(node.node.getY() - y);
        }

        if (radius > dist) {
            KDNode otherBest = findNearest(otherBranch, x, y, depth + 1);
            if (otherBest != null && distance(otherBest.node, x, y) < distance(best.node, x, y)) {
                best = otherBest;
            }
        }

        return best;
    }

    private double distance(Node node, double x, double y) {
        return Math.sqrt(Math.pow(node.getX() - x, 2) + Math.pow(node.getY() - y, 2));
    }
}
