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



    public static Graph generateRandomTSPGraph(int amountOfNodes, Random random) {
        List<Node> nodes = new ArrayList<>();
        for (int i = 0; i < amountOfNodes; i++) {
            int x = random.nextInt(800);
            int y = random.nextInt(800);
            nodes.add(new Node(i, x, y));
        }


        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < amountOfNodes; i++) {
            Node from = nodes.get(i);
            for (int j = i + 1; j < amountOfNodes; j++) {
                Node to = nodes.get(j);
                int weight = (int) Math.sqrt(Math.pow(from.getX() - to.getX(), 2) + Math.pow(from.getY() - to.getY(), 2));
                Edge edge = new Edge(i * amountOfNodes + j, from, to, weight);
                from.addEdge(edge);
                to.addEdge(edge);
                edges.add(edge);
            }
        }

        Graph graph = new Graph();
        graph.nodes = nodes;
        graph.edges = edges;
        return graph;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    
}