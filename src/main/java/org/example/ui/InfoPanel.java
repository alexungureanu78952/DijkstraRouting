package org.example.ui;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.GridLayout;

public class InfoPanel extends JPanel {
    private final JLabel startNodeLabel;
    private final JLabel endNodeLabel;
    private final JLabel distanceLabel;

    public InfoPanel() {
        setLayout(new GridLayout(3, 1));
        setBackground(new Color(0, 0, 0, 128));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        startNodeLabel = new JLabel("Start Node: -");
        startNodeLabel.setForeground(Color.WHITE);
        endNodeLabel = new JLabel("End Node: -");
        endNodeLabel.setForeground(Color.WHITE);
        distanceLabel = new JLabel("Distance: -");
        distanceLabel.setForeground(Color.WHITE);

        add(startNodeLabel);
        add(endNodeLabel);
        add(distanceLabel);
    }

    public void setStartNode(String id) {
        startNodeLabel.setText("Start Node: " + (id != null ? id : "-"));
    }

    public void setEndNode(String id) {
        endNodeLabel.setText("End Node: " + (id != null ? id : "-"));
    }

    public void setDistance(double distance) {
        distanceLabel.setText("Distance: " + (distance >= 0 ? String.format("%.2f", distance) : "-"));
    }
}
