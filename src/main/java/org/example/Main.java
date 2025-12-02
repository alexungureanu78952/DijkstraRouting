package org.example;

import org.example.graph.Graph;
import org.example.parser.MapParser;
import org.example.ui.MainFrame;

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                MapParser parser = new MapParser();
                Graph graph = parser.parse("src/main/resources/luxembourg.xml");
                MainFrame frame = new MainFrame(graph);
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}