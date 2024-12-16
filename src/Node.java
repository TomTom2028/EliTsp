import java.util.ArrayList;
import java.util.List;

/**
 * EliTsp: Node
 *
 * @author robbe
 * @version 16/12/2024
 */

public class Node {
    private int id;
    private List<Edge> edges;

    public Node(int id) {
        this.id = id;
        this.edges = new ArrayList<>();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public List<Edge> getEdges() {
        return edges;
    }
}