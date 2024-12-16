/**
 * EliTsp: Edge
 *
 * @author robbe
 * @version 16/12/2024
 */

public class Edge {
    private int id;
    private Node from;
    private Node to;
    private int weight;

    public Edge(int id, Node from, Node to, int weight) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Node getOther(Node node) {
        if (node != from && node != to) {
            throw new RuntimeException("Node is not part of this edge");
        }

        return node == from ? to : from;
    }

    public int getWeight() {
        return weight;
    }

    public Node getFrom() {
        return from;
    }

    public Node getTo() {
        return to;
    }

}