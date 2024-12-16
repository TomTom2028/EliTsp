import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * EliTsp: Graph
 *
 * @author robbe
 * @version 16/12/2024
 */

public class Graph {
    private List<Node> nodes;
    private List<Edge> edges;

    public Graph() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }


    public void convertToMSP() {
        List<Node> mspNodes = new ArrayList<>();
        List<Edge> mspEdges = new ArrayList<>();
        List<Edge> adjacentEdges = new ArrayList<>();


        Node startNode = nodes.get(0);
        nodes.remove(startNode);
        mspNodes.add(startNode);
        for (Edge edge : startNode.getEdges()) {
            adjacentEdges.add(edge);
        }
        while (!nodes.isEmpty()) {
            Edge minEdge = adjacentEdges.stream().min(Comparator.comparingInt(Edge::getWeight)).get();
            Node from = minEdge.getFrom();
            Node to = minEdge.getTo();
            // find out which node is not in mspNodes
            Node newNode = mspNodes.contains(from) ? to : from;
            mspNodes.add(newNode);
            mspEdges.add(minEdge);
            nodes.remove(newNode);

            adjacentEdges.remove(minEdge);
            // also remove all edges from the adjacent edges were both nodes are already in mspNodes
            adjacentEdges.removeIf(edge -> mspNodes.contains(edge.getFrom()) && mspNodes.contains(edge.getTo()));

            for (Edge edge : newNode.getEdges()) {
                if (!mspEdges.contains(edge) && (
                        (mspNodes.contains(edge.getFrom()) && !mspNodes.contains(edge.getTo())) ||
                                (!mspNodes.contains(edge.getFrom()) && mspNodes.contains(edge.getTo())))
                        ) {
                    adjacentEdges.add(edge);
                }
            }
        }

        nodes = mspNodes;
        edges = mspEdges;
        return;
    }




    public static Graph generateRandomFullyConnectedGraph(Random random, int n) {
        Graph graph = new Graph();
        for (int i = 0; i < n; i++) {
            Node node = new Node(i);
            graph.nodes.add(node);
        }
        int edgeId = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                Node from = graph.nodes.get(i);
                Node to = graph.nodes.get(j);
                int edgeWeight = generateRandomEdge(random);
                Edge edge = new Edge(edgeId, from, to, edgeWeight);
                edgeId++;
                from.addEdge(edge);
                to.addEdge(edge);
            }
        }
        return graph;
    }



    private static int generateRandomEdge(Random random) {
        return random.nextInt(100, 10000);
    }
}