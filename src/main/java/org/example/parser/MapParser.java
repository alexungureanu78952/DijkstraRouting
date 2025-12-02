package org.example.parser;

import org.example.graph.Graph;
import org.example.graph.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MapParser {
    public Graph parse(String filePath) throws Exception {
        Graph graph = new Graph();
        Map<String, Node> nodeMap = new HashMap<>();

        File xmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(xmlFile);
        doc.getDocumentElement().normalize();

        NodeList nodeNodeList = doc.getElementsByTagName("node");
        for (int i = 0; i < nodeNodeList.getLength(); i++) {
            Element nodeElement = (Element) nodeNodeList.item(i);
            String id = nodeElement.getAttribute("id");
            double x = Double.parseDouble(nodeElement.getAttribute("x"));
            double y = Double.parseDouble(nodeElement.getAttribute("y"));
            Node node = new Node(id, x, y);
            graph.addNode(node);
            nodeMap.put(id, node);
        }

        NodeList edgeNodeList = doc.getElementsByTagName("edge");
        for (int i = 0; i < edgeNodeList.getLength(); i++) {
            Element edgeElement = (Element) edgeNodeList.item(i);
            String sourceId = edgeElement.getAttribute("source");
            String destId = edgeElement.getAttribute("destination");
            Node source = nodeMap.get(sourceId);
            Node destination = nodeMap.get(destId);
            if (source != null && destination != null) {
                graph.addEdge(source, destination);
                graph.addEdge(destination, source);
            }
        }
        return graph;
    }
}
