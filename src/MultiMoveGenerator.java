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

    private MoveGenerator[] moveGenerators;

    public MultiMoveGenerator(Random random, DistanceMatrix distanceMatrix) {
        insertMoveGenerator = new InsertMoveGenerator(random, distanceMatrix);
        twoOptMoveGenerator = new TwoOptMoveGenerator(random, distanceMatrix);
        moveGenerators = new MoveGenerator[]{
                insertMoveGenerator,
                twoOptMoveGenerator
        };
    }


    @Override
    public Move generate(List<Node> path) {
        // distribution: 75% insert, 25% twoOpt
        double randomValue = Math.random();
        if (randomValue < 0.75) {
            return insertMoveGenerator.generate(path);
        } else {
            return twoOptMoveGenerator.generate(path);
        }
    }
}