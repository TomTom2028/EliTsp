import java.util.List;

/**
* EliTsp: TwoOptMove
* @author robbe
* @version 19/12/2024
*/

public class TwoOptMove implements Move {
    private final int firstIndex;
    private final int secondIndex;
    private final double delta;
    private final List<Node> path;

    public TwoOptMove(int firstIndex, int secondIndex, double delta, List<Node> path) {
        this.firstIndex = firstIndex;
        this.secondIndex = secondIndex;
        this.delta = delta;
        this.path = path;
    }

    @Override
    public double getDelta() {
        return delta;
    }

    @Override
    public void apply() {
        Node first = path.get(firstIndex);
        Node second = path.get(secondIndex);
        path.set(firstIndex, second);
        path.set(secondIndex, first);
    }
}