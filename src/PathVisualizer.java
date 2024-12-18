import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * EliTsp: PathVisualizer
 *
 * @author robbe
 * @version 18/12/2024
 */

public class PathVisualizer {
    private static class CustomPanel extends JPanel {
        private List<Node> path;
        private DistanceMatrix distanceMatrix;
        public CustomPanel(List<Node> path, DistanceMatrix distanceMatrix) {
            super();
            this.path = path;
            this.distanceMatrix = distanceMatrix;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Draw stuff

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());

            // Draw cities
            g.setColor(Color.RED);
            for (Node city : path) {
                g.fillOval(city.getX(), city.getY(), 10, 10);
            }
            double cost = 0;

            // Draw paths
            g.setColor(Color.BLUE);
            for (int i = 1; i < path.size(); i++) {
                // from prev to current
                Node city1 = path.get(i - 1);
                Node city2 = path.get(i);
                cost += distanceMatrix.getDistance(city1, city2);
                g.drawLine(city1.getX() + 5, city1.getY() + 5, city2.getX() + 5, city2.getY() + 5);
            }

            // display the cost
            g.setColor(Color.BLACK);
            g.drawString("Cost: " + cost, 10, 10);
        }
    }



    public static void showPath(List<Node> path, DistanceMatrix distanceMatrix) {
        JFrame frame = new JFrame("Path Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.add(new CustomPanel(path, distanceMatrix));
        frame.setVisible(true);
    }
}