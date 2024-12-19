import java.util.*;

/**
 * EliTsp: Solver
 *
 * @author robbe
 * @version 18/12/2024
 */

public class Solver {

    private static class EdgeSwapData {
        public int firstFrom;
        public int firstTo;

        public int secondFrom;
        public int secondTo;


        public double delta;
    }


   private Graph graph;

   private List<Node> path;

   private List<Node> absoluteBestPath;
   private double absoluteBestDistance;

   private DistanceMatrix distanceMatrix;
   private Random random;



   private double distance;
   SimulatedAnnealing annealing;

   private MoveGenerator moveGenerator;



    public Solver(Graph graph, Random random) {
         this.graph = graph;
         this.path = new ArrayList<>();
         this.distanceMatrix = DistanceMatrix.createFromGraph(graph);
         this.random = random;
         double delta = (1 - 0.00002 / graph.getNodes().size());
         double temp = 12 * graph.getNodes().size();
        System.out.println("delta: " + delta);
        System.out.println("temp: " + temp);
        this.annealing = new SimulatedAnnealing(temp, delta, random);
        this.moveGenerator = new MultiMoveGenerator(random, distanceMatrix);

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
        while (!toVisit.isEmpty()) {
            int index = random.nextInt(toVisit.size());
            Node next = toVisit.get(index);
            path.add(next);
            toVisit.remove(index);
        }

        path.add(path.get(0));

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

        while (timesRefused < 100000 || annealing.getTemperature() > 0.5) {
            // take a random node from path (except the first and last node)
            Move move = moveGenerator.generate(path);
            index++;
            if (index % 10000000 == 0) {
                index = 0;
                System.out.println("Current distance: " + distance);
                System.out.println("current Temperature: " + annealing.getTemperature());
            }


            // et voila, delta is calculated, the following shoulldddd be just moved to a fn but lazy
            if (!annealing.acceptDelta(move.getDelta())) {
                timesRefused++;
                continue;
            }

           // apply
            move.apply();
            distance += move.getDelta();

            timesRefused /= 1.005;

            if (distance < absoluteBestDistance) {
                absoluteBestDistance = distance;
                absoluteBestPath = new ArrayList<>(path);
                timesRefused = 0;
            }
        }
    }





    public void showPath() {
        PathVisualizer.showPath(absoluteBestPath, distanceMatrix);
    }


}