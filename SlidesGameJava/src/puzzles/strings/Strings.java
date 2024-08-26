package puzzles.strings;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;

/**
 * The Strings class solves a string transformation puzzle using the Solver class.
 */
public class Strings {
    /**
     * The main method of the Strings program.
     * It takes two command-line arguments, start and finish, and solves the  transformation puzzle.
     * Prints the solution path if a solution is found.
     * @param args the command-line arguments: start and finish strings
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Strings start finish");
            return;
        }
        String start = args[0];
        String finish = args[1];
        System.out.println("Start: " + start + ", Finish: " + finish);
        StringsConfiguration startConfig = new StringsConfiguration(start, finish);
        Solver solver = new Solver(startConfig);
        boolean hasSolution = solver.solve();
        if (hasSolution) {
            System.out.println("Total configs: " + solver.getTotalConfigurations());
            System.out.println("Unique configs: " + solver.getUniqueConfigurations());
            int step = 0;
            for (Configuration config : solver.getSolutionPath()) {
                StringsConfiguration stringsConfig = (StringsConfiguration) config;
                System.out.println("Step " + step + ": " + stringsConfig.getString());
                step++;
            }
        } else {
            System.out.println("No solution");
        }
    }
}
