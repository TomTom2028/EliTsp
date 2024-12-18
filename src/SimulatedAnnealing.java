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
        double maxDelta =  -Math.log(randomValue) * temperature;
        temperature *= coolingRate;
        return maxDelta;
    }

    public double getTemperature() {
        return temperature;
    }
}