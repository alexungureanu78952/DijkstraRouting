package org.example.ui;

import org.example.algorithm.Dijkstra;
import org.example.graph.Edge;
import org.example.graph.Graph;
import org.example.graph.Node;
import org.example.kdtree.KDTree;

import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MapPanel extends JPanel {
private final Graph graph;
private final KDTree kdTree;
private final InfoPanel infoPanel;
private double scale = 1.0;
private double offsetX = 0;
private double offsetY = 0;
private Node startNode;
private Node endNode;
private List<Edge> path = new ArrayList<>();
private int clickCount = 0;
    private java.awt.Point lastMousePosition;

    public MapPanel(Graph graph, InfoPanel infoPanel) {
        this.graph = graph;
        this.kdTree = new KDTree(graph.getNodes());
        this.infoPanel = infoPanel;

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point2D transformedPoint = transformPoint(e.getPoint());
                Node nearestNode = kdTree.findNearest(transformedPoint.getX(), transformedPoint.getY());
                if (clickCount % 2 == 0) {
                    setStartNode(nearestNode);
                    setEndNode(null);
                    setPath(null);
                    infoPanel.setStartNode(startNode.getId());
                    infoPanel.setEndNode(null);
                    infoPanel.setDistance(-1);
                } else {
                    setEndNode(nearestNode);
                    infoPanel.setEndNode(endNode.getId());
                    if (startNode != null && endNode != null) {
                        Dijkstra dijkstra = new Dijkstra();
                        List<Edge> shortestPath = dijkstra.findShortestPath(graph, startNode, endNode);
                        setPath(shortestPath);
                        double distance = shortestPath.stream().mapToDouble(Edge::getWeight).sum();
                        infoPanel.setDistance(distance);
                    }
                }
                clickCount++;
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                lastMousePosition = e.getPoint();
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                double dx = e.getX() - lastMousePosition.getX();
                double dy = e.getY() - lastMousePosition.getY();
                offsetX += dx;
                offsetY += dy;
                lastMousePosition = e.getPoint();
                repaint();
            }
        });
    }

    private Point2D transformPoint(java.awt.Point p) {
        try {
            AffineTransform at = new AffineTransform();
            at.translate(offsetX, offsetY);
            at.scale(scale, scale);
            return at.inverseTransform(p, null);
        } catch (Exception e) {
            e.printStackTrace();
            return p;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        AffineTransform at = new AffineTransform();
        at.translate(offsetX, offsetY);
        at.scale(scale, scale);
        g2d.transform(at);

        drawGraph(g2d);
        drawPath(g2d);
        drawSelectedNodes(g2d);
    }

    private void drawGraph(Graphics2D g2d) {
        g2d.setStroke(new BasicStroke(2));
        g2d.setColor(Color.LIGHT_GRAY);
        for (List<Edge> edges : graph.getAdj().values()) {
            for (Edge edge : edges) {
                Node source = edge.getSource();
                Node dest = edge.getDestination();
                g2d.drawLine((int) source.getX(), (int) source.getY(), (int) dest.getX(), (int) dest.getY());
            }
        }
    }

    private void drawSelectedNodes(Graphics2D g2d) {
        if (startNode != null) {
            g2d.setColor(Color.GREEN);
            g2d.fillOval((int) (startNode.getX() - 5), (int) (startNode.getY() - 5), 10, 10);
        }
        if (endNode != null) {
            g2d.setColor(Color.RED);
            g2d.fillOval((int) (endNode.getX() - 5), (int) (endNode.getY() - 5), 10, 10);
        }
    }

    private void drawPath(Graphics2D g2d) {
        if (path != null && !path.isEmpty()) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.BLUE);
            for (Edge edge : path) {
                Node source = edge.getSource();
                Node dest = edge.getDestination();
                g2d.drawLine((int) source.getX(), (int) source.getY(), (int) dest.getX(), (int) dest.getY());
            }
        }
    }

    public void setStartNode(Node node) {
        this.startNode = node;
        repaint();
    }

    public void setEndNode(Node node) {
        this.endNode = node;
        repaint();
    }

    public void setPath(List<Edge> path) {
        this.path = path;
        repaint();
    }
}
