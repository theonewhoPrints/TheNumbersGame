package puzzles.slide.solver;
import puzzles.slide.model.SlideConfig;
import puzzles.slide.model.SlideModel;
import puzzles.common.solver.Configuration;
import puzzles.common.solver.Solver;
import java.io.IOException;
import java.util.List;

/**
 * The main class for solving Slide Puzzle configurations.
 * NOTE: only for slide-2 solution steps may differ a little but same amount of steps are still taken
 * and solution
 */
public class Slide {

    /**
     * The main point for running the Slide Puzzle solver.
     * @param args Command-line arguments. Expects a single puzzle filename argument.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Slide <puzzle_file>");
            System.exit(1);
        }

        String puzzleFileName = args[0];
        Slide slideSolver = new Slide();
        slideSolver.solveSlidePuzzle(puzzleFileName);
    }

    /**
     * Solves a Slide Puzzle configuration loaded from the specified file.
     * @param puzzleFileName The name of the file containing the puzzle configuration.
     */

    private void solveSlidePuzzle(String puzzleFileName) {
        try {
            SlideModel slideModel = new SlideModel(puzzleFileName);
            Solver solver = new Solver(slideModel.getCurrentConfig());
            boolean solutionFound = solver.solve();
            if (solutionFound) {
                List<Configuration> solutionPath = solver.getSolutionPath();
                System.out.println("File: " + puzzleFileName);
                System.out.println(slideModel.getCurrentConfig() + "Total configs:" +solver.getTotalConfigurations());
                System.out.println("Unique configs: " + solver.getUniqueConfigurations());
                int step = 0;
                for (Configuration config : solutionPath) {
                    System.out.println("Step " + step++ + ":");
                    System.out.println(config);
                }
            } else {
                System.out.println("No solution found for the puzzle.");
            }
        } catch (IOException e) {
            System.out.println("Error reading the puzzle file: " + e.getMessage());
        }
    }
}
