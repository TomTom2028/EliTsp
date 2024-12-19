import java.nio.file.Path;
import java.util.List;
import java.util.Random;

/**
 * EliTsp: TwoOptMoveGenerator
 *
 * @author robbe
 * @version 19/12/2024
 */

public class TwoOptMoveGenerator implements MoveGenerator {
    private Random random;
    private DistanceMatrix distanceMatrix;

    public TwoOptMoveGenerator(Random random, DistanceMatrix distanceMatrix) {
        this.random = random;
        this.distanceMatrix = distanceMatrix;
    }

    @Override
    public Move generate(List<Node> path) {
        int firstIndex = getRandomIndexFromPath(path);
        int secondIndex = getRandomIndexFromPath(firstIndex, path);
        double delta = calculate2SwapDelta(firstIndex, secondIndex, path);
        return new TwoOptMove(firstIndex, secondIndex, delta, path);
    }


    private double calculate2SwapDelta(int firstIndex, int secondIndex, List<Node> path) {
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