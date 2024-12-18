import java.util.Random;

/**
 * EliTsp: SimulatedAnnealing
 *
 * @author robbe
 * @version 18/12/2024
 */

public class SimulatedAnnealing {
    private double temperature;
    private double coolingRate;
    private Random random;

    public SimulatedAnnealing(double temperature, double coolingRate, Random random) {
        this.temperature = temperature;
        this.coolingRate = coolingRate;
        this.random = random;
    }

    public double getMaxDelta() {
        double randomValue = random.nextDouble();
        if (randomValue == 0) {
            temperature *= coolingRate;
            return Double.MAX_VALUE;
        }
        double maxDelta =  -Math.log(randomValue) * temperature;
        temperature *= coolingRate;
        return maxDelta;
    }

    public boolean acceptDelta(double delta) {
        double chance = Math.exp(-delta / temperature);
        temperature *= coolingRate;
        return random.nextDouble() < chance;
    }

    public double getTemperature() {
        return temperature;
    }
}