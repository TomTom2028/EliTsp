import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * EliTsp: InsertMoveGenerator
 *
 * @author robbe
 * @version 19/12/2024
 */

public class InsertMoveGenerator implements MoveGenerator {
    private TwoOptMoveGenerator internalTwoOptMoveGenerator;


    private Random random;
    private DistanceMatrix distanceMatrix;

    public InsertMoveGenerator(Random random, DistanceMatrix distanceMatrix) {
        this.random = random;
        this.distanceMatrix = distanceMatrix;
        internalTwoOptMoveGenerator = new TwoOptMoveGenerator(random, distanceMatrix);
    }


    @Override
    public Move generate(List<Node> path) {
        int fromIndex = getRandomIndexFromPath(path);

        int toIndex = getRandomIndexFromPath(fromIndex, path);
        double delta = getDelta(fromIndex, toIndex, path);

        return new InsertMove(fromIndex, toIndex, delta, path);
    }


    private double getDelta(int fromIndex, int toIndex, List<Node> path) {
        // good way!
        int indexDelta = Math.abs(fromIndex - toIndex);
        if (indexDelta == 1) {
            // handle it as a swap!
            return internalTwoOptMoveGenerator.calculate2SwapDelta(fromIndex, toIndex, path);
        }

        if (fromIndex < toIndex) {
            // simple case. order will be like this: beforeTo, to, from, afterTo (unless is right next to each other)
            if (indexDelta > 1) {
                Node beforeFrom = path.get(fromIndex - 1);
                Node from = path.get(fromIndex);
                Node afterFrom = path.get(fromIndex + 1);

                Node to = path.get(toIndex);
                Node afterTo = path.get(toIndex + 1);


                double delta = 0;
                delta -= distanceMatrix.getDistance(beforeFrom, from);
                delta -= distanceMatrix.getDistance(from, afterFrom);
                delta -= distanceMatrix.getDistance(to, afterTo);

                delta += distanceMatrix.getDistance(beforeFrom, afterFrom);
                delta += distanceMatrix.getDistance(to, from);
                delta += distanceMatrix.getDistance(from, afterTo);

                return delta;
            }
        } else if (fromIndex > toIndex) {
            if (indexDelta > 1) {
                Node beforeFrom = path.get(fromIndex - 1);
                Node from = path.get(fromIndex);
                Node afterFrom = path.get(fromIndex + 1);

                Node beforeTo = path.get(toIndex - 1);
                Node to = path.get(toIndex);


                double delta = 0;
                delta -= distanceMatrix.getDistance(beforeFrom, from);
                delta -= distanceMatrix.getDistance(from, afterFrom);
                delta -= distanceMatrix.getDistance(beforeTo, to);

                delta += distanceMatrix.getDistance(beforeFrom, afterFrom);
                delta += distanceMatrix.getDistance(beforeTo, from);
                delta += distanceMatrix.getDistance(from, to);

                return delta;
            }
        }
        throw new RuntimeException("Invalid index delta");
        /*

       // just calculate delta the hard way
        List<Node> deltaCalcPath = new ArrayList<>(path);
        Node from = deltaCalcPath.remove(fromIndex);
        deltaCalcPath.add(toIndex, from);

        double delta = 0;
        for (int i = 0; i < deltaCalcPath.size() - 1; i++) {
            delta += distanceMatrix.getDistance(deltaCalcPath.get(i), deltaCalcPath.get(i + 1));
        }

        double pathCost = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            pathCost += distanceMatrix.getDistance(path.get(i), path.get(i + 1));
        }

        return delta - pathCost;*/
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