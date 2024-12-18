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
    private int x;
    private int y;
    private List<Edge> edges;


    public Node(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.edges = new ArrayList<>();
    }

    public void addEdge(Edge edge) {
        edges.add(edge);
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String toString() {
        return "Node " + id + " (" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node other = (Node) obj;
            return other.id == this.id;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id;
    }

}