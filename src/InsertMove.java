import java.util.List;

/**
 * EliTsp: InsertMove
 *
 * @author robbe
 * @version 19/12/2024
 */

public class InsertMove implements Move {
    private final int fromIndex;
    private final int toIndex;

    private final double delta;

    private final List<Node> path;

    public InsertMove(int fromIndex, int toIndex, double delta, List<Node> path) {
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.delta = delta;
        this.path = path;
    }

    @Override
    public double getDelta() {
        return delta;
    }

    @Override
    public void apply() {
        Node from = path.remove(fromIndex);
        path.add(toIndex, from);
    }
}