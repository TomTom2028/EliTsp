import java.util.List;

/**
 * EliTsp: ReverseMove
 *
 * @author robbe
 * @version 22/12/2024
 */

public class ReverseMove implements Move{
    private int startIndex;
    private int endIndex;

    private double delta;

    private List<Node> path;

    public ReverseMove(int startIndex, int endIndex, double delta, List<Node> path) {
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.delta = delta;
        this.path = path;
    }

    @Override
    public double getDelta() {
        return delta;
    }

    @Override
    public void apply() {
        int length = endIndex - startIndex + 1;
        for (int i = 0; i < length / 2; i++) {
            Node temp = path.get(startIndex + i);
            path.set(startIndex + i, path.get(endIndex - i));
            path.set(endIndex - i, temp);
        }

    }
}