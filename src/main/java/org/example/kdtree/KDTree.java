package org.example.kdtree;

import org.example.graph.Node;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class KDTree {

    private static class KDNode {
        Node node;
        KDNode left;
        KDNode right;

        KDNode(Node node) {
            this.node = node;
        }
    }

    private KDNode root;

    public KDTree(List<Node> nodes) {
        root = buildTree(nodes);
    }

    private KDNode buildTree(List<Node> nodes) {
        if (nodes == null || nodes.isEmpty()) {
            return null;
        }

        class BuildJob {
            List<Node> jobNodes;
            int depth;
            KDNode parent;
            boolean isLeft;

            BuildJob(List<Node> nodes, int depth, KDNode parent, boolean isLeft) {
                this.jobNodes = nodes;
                this.depth = depth;
                this.parent = parent;
                this.isLeft = isLeft;
            }
        }

        Stack<BuildJob> stack = new Stack<>();
        KDNode newRoot = null;
        stack.push(new BuildJob(new ArrayList<>(nodes), 0, null, false));

        while (!stack.isEmpty()) {
            BuildJob job = stack.pop();
            List<Node> currentNodes = job.jobNodes;
            int depth = job.depth;
            KDNode parent = job.parent;

            if (currentNodes.isEmpty()) {
                continue;
            }

            int axis = depth % 2;
            currentNodes.sort((a, b) -> (axis == 0) ? Double.compare(a.getX(), b.getX()) : Double.compare(a.getY(), b.getY()));

            int medianIndex = currentNodes.size() / 2;
            KDNode newNode = new KDNode(currentNodes.get(medianIndex));

            if (parent == null) {
                newRoot = newNode;
            } else if (job.isLeft) {
                parent.left = newNode;
            } else {
                parent.right = newNode;
            }

            List<Node> leftSublist = new ArrayList<>(currentNodes.subList(0, medianIndex));
            if (!leftSublist.isEmpty()) {
                stack.push(new BuildJob(leftSublist, depth + 1, newNode, true));
            }

            List<Node> rightSublist = new ArrayList<>(currentNodes.subList(medianIndex + 1, currentNodes.size()));
            if (!rightSublist.isEmpty()) {
                stack.push(new BuildJob(rightSublist, depth + 1, newNode, false));
            }
        }
        return newRoot;
    }

    public Node findNearest(double x, double y) {
        if (root == null) {
            return null;
        }

        Stack<KDNode> path = new Stack<>();
        KDNode current = root;
        int depth = 0;

        while (current != null) {
            path.push(current);
            int axis = depth % 2;
            double nodeCoord = getCoord(current.node, axis);
            double pointCoord = (axis == 0) ? x : y;

            if (pointCoord < nodeCoord) {
                current = current.left;
            } else {
                current = current.right;
            }
            depth++;
        }

        KDNode best = path.peek();
        double bestDist = distance(best.node, x, y);

        while (!path.isEmpty()) {
            KDNode node = path.pop();
            int currentDepth = path.size();

            if (distance(node.node, x, y) < bestDist) {
                best = node;
                bestDist = distance(best.node, x, y);
            }

            int axis = currentDepth % 2;
            double nodeCoord = getCoord(node.node, axis);
            double pointCoord = (axis == 0) ? x : y;

            if (Math.abs(nodeCoord - pointCoord) < bestDist) {
                KDNode otherBranch = (pointCoord < nodeCoord) ? node.right : node.left;
                if (otherBranch != null) {
                    best = searchSubtree(otherBranch, x, y, best, currentDepth + 1);
                    bestDist = distance(best.node, x, y);
                }
            }
        }
        return best.node;
    }

    private KDNode searchSubtree(KDNode subtreeRoot, double x, double y, KDNode currentBest, int initialDepth) {
        Stack<Object[]> stack = new Stack<>();
        stack.push(new Object[]{subtreeRoot, initialDepth});

        KDNode best = currentBest;
        double bestDist = distance(best.node, x, y);

        while (!stack.isEmpty()) {
            Object[] currentPair = stack.pop();
            KDNode node = (KDNode) currentPair[0];
            int depth = (int) currentPair[1];

            if (node == null) {
                continue;
            }

            if (distance(node.node, x, y) < bestDist) {
                best = node;
                bestDist = distance(best.node, x, y);
            }

            int axis = depth % 2;
            double nodeCoord = getCoord(node.node, axis);
            double pointCoord = (axis == 0) ? x : y;

            KDNode firstBranch, secondBranch;
            if (pointCoord < nodeCoord) {
                firstBranch = node.left;
                secondBranch = node.right;
            } else {
                firstBranch = node.right;
                secondBranch = node.left;
            }

            if (firstBranch != null) {
                stack.push(new Object[]{firstBranch, depth + 1});
            }
            if (secondBranch != null && Math.abs(nodeCoord - pointCoord) < bestDist) {
                stack.push(new Object[]{secondBranch, depth + 1});
            }
        }
        return best;
    }

    private double getCoord(Node node, int axis) {
        return (axis == 0) ? node.getX() : node.getY();
    }

    private double distance(Node node, double x, double y) {
        return Math.sqrt(Math.pow(node.getX() - x, 2) + Math.pow(node.getY() - y, 2));
    }
}