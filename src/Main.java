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
        this.random = new Random();
    }

    public void doThing() {
        Graph graph = Graph.generateRandomFullyConnectedGraph(random, 500);
        graph.convertToMSP();
        System.out.println(graph);
    }

}