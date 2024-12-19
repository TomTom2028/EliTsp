import java.util.List;

/**
 * EliTsp: MoveGenerator
 *
 * @author robbe
 * @version 19/12/2024
 */
public interface MoveGenerator {
    public Move generate(List<Node> path);
}
