import java.util.HashMap;
import java.util.Map;

/**
 * EliTsp: DistanceMatrix
 *
 * @author robbe
 * @version 18/12/2024
 */

public class DistanceMatrix {
    private double[][] distances;
    private DistanceMatrix(int maxNodes) {
        distances = new double[maxNodes][maxNodes];
    }


    public double getDistance(Node from, Node to) {
        return distances[from.getId()][to.getId()];
    }

    public static DistanceMatrix createFromGraph(Graph graph) {
        DistanceMatrix distanceMatrix = new DistanceMatrix(graph.getNodes().size());
        for (Node node : graph.getNodes()) {
            for (Edge edge : node.getEdges()) {
                Node other = edge.getOther(node);
                distanceMatrix.distances[node.getId()][other.getId()] = edge.getWeight();
            }
        }
        return distanceMatrix;
    }


}