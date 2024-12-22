import java.util.List;
import java.util.Random;

/**
 * EliTsp: ReverseMoveGenerator
 *
 * @author robbe
 * @version 22/12/2024
 */

public class ReverseMoveGenerator implements MoveGenerator {
    private Random random;
    private DistanceMatrix distanceMatrix;

    public ReverseMoveGenerator(Random random, DistanceMatrix distanceMatrix) {
        this.random = random;
        this.distanceMatrix = distanceMatrix;
    }

    @Override
    public Move generate(List<Node> path) {
        int fromIndex = getRandomIndexFromPath(path);
        int toIndex = getRandomIndexFromPath(fromIndex, path);

        int smallestIndex = Math.min(fromIndex, toIndex);
        int largestIndex = Math.max(fromIndex, toIndex);

        double delta = getDelta(smallestIndex, largestIndex, path);

        return new ReverseMove(smallestIndex, largestIndex, delta, path);
    }


    private double getDelta(int fromIndex, int toIndex, List<Node> path) {
        Node beforeFrom = path.get(fromIndex - 1);
        Node from = path.get(fromIndex);
        Node afterTo = path.get(toIndex + 1);
        Node to = path.get(toIndex);

        double delta = 0;
        delta -= distanceMatrix.getDistance(beforeFrom, from);
        delta -= distanceMatrix.getDistance(to, afterTo);

        delta += distanceMatrix.getDistance(beforeFrom, to);
        delta += distanceMatrix.getDistance(from, afterTo);
        return delta; // the rest doesn't change! it is symmetric!
    }

    private int getRandomIndexFromPath(List<Node> path) {
        return random.nextInt(path.size() - 2) + 1;
    }

    private int getRandomIndexFromPath(int except, List<Node> path) {
        int index = getRandomIndexFromPath(path);
        while (index == except) {
            index = getRandomIndexFromPath(path);
        }
        return index;
    }
}