package puzzles.crossing;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import java.util.List;

/**
 * The Crossing class represents the main entry point for the Crossing puzzle solver.
 * It takes the number of pups and wolves as command line arguments, creates the initial configuration,
 * and uses a solver to find a solution path if existing.
 * (if you want to see working steps run crossing 0 1 and 2 1 those ones are the only ones that kinda
 * work for showing steps.
 */
public class Crossing {

    /**
     * The main method of the Crossing puzzle solver.
     * @param args the command line arguments specifying the number of pups and wolves
     */
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java Crossing pups wolves");
        } else {
            int numPups = Integer.parseInt(args[0]);
            int numWolves = Integer.parseInt(args[1]);
            CrossingConfiguration initialConfig = new CrossingConfiguration(numPups, numWolves);
            Solver solver = new Solver(initialConfig);
            boolean hasSolution = solver.solve();
            System.out.println("Pups: " + numPups + ", Wolves: " + numWolves);
            System.out.println("Total configs: " + solver.getTotalConfigurations());
            System.out.println("Unique configs: " + solver.getUniqueConfigurations());
            if (hasSolution) {
                List<Configuration> solutionPath = solver.getSolutionPath();
                for (int i = 0; i < solutionPath.size(); i++) {
                    CrossingConfiguration crossingConfig = (CrossingConfiguration) solutionPath.get(i);
                    System.out.println("Step " + i + ": " + crossingConfig);
                }
            } else {
                System.out.println("No solution found.");
            }
        }
    }
}
