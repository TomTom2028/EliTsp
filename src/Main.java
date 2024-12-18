import java.util.Random;

/**
 * Default (Template) Project: ${NAME}
 *
 * @author robbe
 * @version 16/12/2024
 */
public class Main {

    private Random random;
    public static void main(String[] args) {
        Main main = new Main();
        main.doThing();
    }

    public Main() {
        this.random = new Random(14);
    }

    public void doThing() {
        Graph graph = Graph.generateRandomTSPGraph(75, random);
       //Graph graph = Graph.generateRandomTSPGraph(200, random);
       Solver solver = new Solver(graph, new Random());
       solver.solve();
       solver.showPath();
    }

}