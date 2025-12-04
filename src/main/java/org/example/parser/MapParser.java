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
        double minLon = Double.MAX_VALUE, maxLon = Double.MIN_VALUE;
        double minLat = Double.MAX_VALUE, maxLat = Double.MIN_VALUE;


        for (int i = 0; i < nodeNodeList.getLength(); i++) {
            Element nodeElement = (Element) nodeNodeList.item(i);
            double lat = Double.parseDouble(nodeElement.getAttribute("latitude"));
            double lon = Double.parseDouble(nodeElement.getAttribute("longitude"));
            if (lon < minLon) minLon = lon;
            if (lon > maxLon) maxLon = lon;
            if (lat < minLat) minLat = lat;
            if (lat > maxLat) maxLat = lat;
        }

        double lonScale = 700 / (maxLon - minLon);
        double latScale = 500 / (maxLat - minLat);
        double scale = Math.min(lonScale, latScale);


        for (int i = 0; i < nodeNodeList.getLength(); i++) {
            Element nodeElement = (Element) nodeNodeList.item(i);
            String id = nodeElement.getAttribute("id");
            double lat = Double.parseDouble(nodeElement.getAttribute("latitude"));
            double lon = Double.parseDouble(nodeElement.getAttribute("longitude"));
            
            double scaledX = (lon - minLon) * scale + 50;
            double scaledY = (lat - minLat) * scale + 50;

            Node node = new Node(id, scaledX, scaledY);
            graph.addNode(node);
            nodeMap.put(id, node);
        }

        NodeList edgeNodeList = doc.getElementsByTagName("arc");
        for (int i = 0; i < edgeNodeList.getLength(); i++) {
            Element edgeElement = (Element) edgeNodeList.item(i);
            String sourceId = edgeElement.getAttribute("from");
            String destId = edgeElement.getAttribute("to");
            double length = Double.parseDouble(edgeElement.getAttribute("length"));
            Node source = nodeMap.get(sourceId);
            Node destination = nodeMap.get(destId);
            if (source != null && destination != null) {
                graph.addEdge(source, destination, length);
            }
        }
        return graph;
    }
}
