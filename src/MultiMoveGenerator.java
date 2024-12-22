import java.util.List;
import java.util.Random;

/**
 * EliTsp: MultiMoveGenerator
 *
 * @author robbe
 * @version 19/12/2024
 */

public class MultiMoveGenerator implements MoveGenerator {
    private InsertMoveGenerator insertMoveGenerator;
    private TwoOptMoveGenerator twoOptMoveGenerator;
    private ReverseMoveGenerator reverseMoveGenerator;


    public MultiMoveGenerator(Random random, DistanceMatrix distanceMatrix) {
        insertMoveGenerator = new InsertMoveGenerator(random, distanceMatrix);
        twoOptMoveGenerator = new TwoOptMoveGenerator(random, distanceMatrix);
        reverseMoveGenerator = new ReverseMoveGenerator(random, distanceMatrix);
    }


    @Override
    public Move generate(List<Node> path) {


        double randomValue = Math.random();
        if (randomValue < 0.9) {
            return reverseMoveGenerator.generate(path);
        } else if (randomValue < 0.98) {
            return insertMoveGenerator.generate(path);
        } else {
            return twoOptMoveGenerator.generate(path);
        }
    }
}