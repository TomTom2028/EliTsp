import java.util.*;

/**
 * EliTsp: Solver
 *
 * @author robbe
 * @version 18/12/2024
 */

public class Solver {
   private Graph graph;

   private List<Node> path;

   private List<Node> absoluteBestPath;
   private double absoluteBestDistance;

   private DistanceMatrix distanceMatrix;
   private Random random;



   private double distance;
   SimulatedAnnealing annealing;



    public Solver(Graph graph, Random random) {
         this.graph = graph;
         this.path = new ArrayList<>();
         this.distanceMatrix = DistanceMatrix.createFromGraph(graph);
         this.random = random;
        this.annealing = new SimulatedAnnealing(550, 0.9999995, random);

    }


    public void solve () {
        randomInitialPath();
        //generateInitialPath();
        System.out.println("begin iterative improve");
        iterativeImprove();
        System.out.println("si");
    }

    // works better!
    private void randomInitialPath() {
        List<Node> toVisit = new LinkedList<>(graph.getNodes());
        toVisit.remove(0);
        Node current = graph.getNodes().get(0);
        path.add(current);
        while (!toVisit.isEmpty()) {
            int index = random.nextInt(toVisit.size());
            Node next = toVisit.get(index);
            path.add(next);
            toVisit.remove(index);
        }

        path.add(graph.getNodes().get(0));

        distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            distance += distanceMatrix.getDistance(path.get(i), path.get(i + 1));
        }

        absoluteBestPath = new ArrayList<>(path);
        absoluteBestDistance = Double.MAX_VALUE;
        distance = Double.MAX_VALUE;
    }

    private void generateInitialPath() {
        // take the first node as starting point
        Node next = graph.getNodes().get(0);



        while (next != null) {
            // add current node to path
            path.add(next);

            // get the greedy closest node
            Node finalNext = next;
            next = next.getEdges().stream().sorted(Comparator.comparingInt(Edge::getWeight))
                    .map(edge -> edge.getOther(finalNext)).filter(node -> !path.contains(node)).findFirst().orElse(null);
        }

        // add the first node to the end to complete the cycle
        path.add(path.get(0));

        // calculate the distance of the path
        distance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            distance += distanceMatrix.getDistance(path.get(i), path.get(i + 1));
        }

        absoluteBestPath = new ArrayList<>(path);
        absoluteBestDistance = distance;
    }


    public void iterativeImprove() {
        double timesRefused = 0;
        int index = 0;
        while (timesRefused < 100000 || annealing.getTemperature() > 1) {
            // take a random node from path (except the first and last node)
            int firstIndex = getRandomIndexFromPath();
            int secondIndex = getRandomIndexFromPath(firstIndex);

            List<Node> newPath = new ArrayList<>(path);

            // swap the nodes
            Node temp = newPath.get(firstIndex);
            newPath.set(firstIndex, newPath.get(secondIndex));
            newPath.set(secondIndex, temp);

            double maxDelta = annealing.getMaxDelta();
            double maxDistance = distance + maxDelta;
            if (maxDelta == Double.MAX_VALUE) {
                maxDistance = Double.MAX_VALUE;
            }
            index++;
            if (index % 1000000 == 0) {
                index = 0;
                System.out.println("Current distance: " + distance);
                System.out.println("current Temperature: " + annealing.getTemperature());
            }

            // calculate the new distance
            double newDistance = 0;
            for (int i = 0; i < newPath.size() - 1; i++) {
                newDistance += distanceMatrix.getDistance(newPath.get(i), newPath.get(i + 1));
                if (newDistance > maxDistance) {
                    timesRefused++;
                    break;
                }
            }
            if (newDistance > maxDistance) {
                timesRefused++;
                continue;
            }

            distance = newDistance;
            timesRefused /= 1.005;

            path = newPath;
            if (distance < absoluteBestDistance) {
                absoluteBestDistance = distance;
                absoluteBestPath = path;
                timesRefused = 0;
            }
        }
    }

    private int getRandomIndexFromPath() {
        return random.nextInt(path.size() - 2) + 1;
    }

    private int getRandomIndexFromPath(int except) {
        int index = getRandomIndexFromPath();
        while (index == except) {
            index = getRandomIndexFromPath();
        }
        return index;
    }



    public void showPath() {
        PathVisualizer.showPath(absoluteBestPath, distanceMatrix);
    }


}