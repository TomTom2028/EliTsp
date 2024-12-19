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
        this.annealing = new SimulatedAnnealing(600, 0.9999998, random);

    }


    public void solve () {
        //generateInitialPath();
        randomInitialPath();
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
        absoluteBestDistance = distance;
    }

    private void generateInitialPath() {
        // take the first node as starting point
        Node next = graph.getNodes().get(0);



        while (next != null) {
            // add current node to path
            path.add(next);

            // get the greedy closest node
            Node finalNext = next;
            next = next.getEdges().stream().sorted(Comparator.comparingDouble(Edge::getWeight))
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
            Node first = path.get(firstIndex);
            Node second = path.get(secondIndex);


            double delta = calculate2SwapDelta(firstIndex, secondIndex);
            index++;
            if (index % 10000000 == 0) {
                index = 0;
                System.out.println("Current distance: " + distance);
                System.out.println("current Temperature: " + annealing.getTemperature());
            }


            // et voila, delta is calculated, the following shoulldddd be just moved to a fn but lazy
            if (!annealing.acceptDelta(delta)) {
                timesRefused++;
                continue;
            }

            // swap
            path.set(firstIndex, second);
            path.set(secondIndex, first);

            distance += delta;

            timesRefused /= 1.005;

            if (distance < absoluteBestDistance) {
                absoluteBestDistance = distance;
                absoluteBestPath = new ArrayList<>(path);
                timesRefused = 0;
            }
        }
    }

    private double calculate2SwapDelta(int firstIndex, int secondIndex) {
        int indexDelta = Math.abs(firstIndex - secondIndex);
        if (indexDelta > 1) {
            // base case
            // take one before, and one after for each
            Node firstBefore = path.get(firstIndex - 1);
            Node first = path.get(firstIndex);
            Node firstAfter = path.get(firstIndex + 1);

            Node secondBefore = path.get(secondIndex - 1);
            Node second = path.get(secondIndex);
            Node secondAfter = path.get(secondIndex + 1);

            // now, we "swap" first and second. create a singed delta
            double delta = 0;
            delta -= distanceMatrix.getDistance(firstBefore, first);
            delta -= distanceMatrix.getDistance(first, firstAfter);
            delta -= distanceMatrix.getDistance(secondBefore, second);
            delta -= distanceMatrix.getDistance(second, secondAfter);

            delta += distanceMatrix.getDistance(firstBefore, second);
            delta += distanceMatrix.getDistance(second, firstAfter);
            delta += distanceMatrix.getDistance(secondBefore, first);
            delta += distanceMatrix.getDistance(first, secondAfter);

            return delta;
        } else if (indexDelta == 1){
            int smallestIndex = Math.min(firstIndex, secondIndex);
            int largestIndex = Math.max(firstIndex, secondIndex);

            Node beforeSmallest = path.get(smallestIndex - 1);
            Node smallest = path.get(smallestIndex);
            Node largest = path.get(largestIndex);
            Node afterLargest = path.get(largestIndex + 1);

            double delta = 0;
            delta -= distanceMatrix.getDistance(beforeSmallest, smallest);
            delta -= distanceMatrix.getDistance(largest, afterLargest);

            delta += distanceMatrix.getDistance(beforeSmallest, largest);
            delta += distanceMatrix.getDistance(smallest, afterLargest);
            return delta;
        }
        throw new RuntimeException("Invalid index delta");
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