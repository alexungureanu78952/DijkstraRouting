package org.example.ui;

import org.example.graph.Graph;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import java.awt.BorderLayout;

public class MainFrame extends JFrame {
    private final MapPanel mapPanel;
    private final InfoPanel infoPanel;

    public MainFrame(Graph graph) {
        setTitle("Dijkstra Routing");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JLayeredPane layeredPane = new JLayeredPane();
        getContentPane().add(layeredPane, BorderLayout.CENTER);

        infoPanel = new InfoPanel();
        infoPanel.setBounds(580, 10, 200, 80); // Position top-right

        mapPanel = new MapPanel(graph, infoPanel);
        mapPanel.setBounds(0, 0, 800, 600);

        layeredPane.add(mapPanel, JLayeredPane.DEFAULT_LAYER);
        layeredPane.add(infoPanel, JLayeredPane.PALETTE_LAYER);

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                mapPanel.setSize(layeredPane.getSize());
                infoPanel.setLocation(layeredPane.getWidth() - infoPanel.getWidth() - 20, 10);
            }
        });
    }
}

