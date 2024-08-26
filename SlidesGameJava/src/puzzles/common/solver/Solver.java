package puzzles.common.solver;
import puzzles.slide.model.SlideConfig;
import java.util.*;
import java.util.*;

/**
 * The Solver class is a generic implementation of a solver using the BFS (breadth-first search) algorithm.
 * NOTE: only for slide-2 solution steps may differ a little but same amount of steps are still taken
 * and solution
 */
public class Solver {

    /** The initial configuration for solver. */
    private Configuration initialConfig;

    /** A set to keep track of visited configurations during search. */
    private Set<Configuration> visit;

    /** A map to store the predecessor configuration for each visited config.*/
    private Map<Configuration, Configuration> predecessor;

    /** The total number of configurations generated during search. */
    private int totalConfigurations;

    /** A flag indicating whether a solution found. */
    private boolean sol;

    /**
     * Constructs an object with the initial configuration.
     * @param initialConfig the initial configuration
     */
    public Solver(Configuration initialConfig) {
        this.initialConfig = initialConfig;
        this.visit = new HashSet<>();
        this.predecessor = new HashMap<>();
        this.totalConfigurations = 0;
        this.sol = false;
    }

    /**
     * Solves the puzzle using the BFS algorithm.
     * @return true if a solution is found, false otherwise
     */
    public boolean solve() {
        Queue<Configuration> queue = new LinkedList<>();
        queue.offer(initialConfig);
        visit.add(initialConfig);
        while (!queue.isEmpty()) {
            Configuration currentConfig = queue.poll();
            if (currentConfig.isSolution()) {
                sol = true;
                break;
            }
            Collection<Configuration> neighbors = currentConfig.getNeighbors();
            for (Configuration neighbor : neighbors) {
                totalConfigurations ++;
                if (!visit.contains(neighbor)) {
                    visit.add(neighbor);
                    queue.offer(neighbor);
                    predecessor.put(neighbor, currentConfig);
                }
            }
        }
        return sol;
    }

    /**
     * Returns the solution path as a list of configurations.
     * @return the solution path
     */
    public List<Configuration> getSolutionPath() {
        List<Configuration> path = new ArrayList<>();
        if (sol) {
            Configuration currentConfig = null;
            for (Configuration config : visit) {
                if (config.isSolution()) {
                    currentConfig = config;
                    break;
                }
            }
            while (currentConfig != null) {
                path.add(currentConfig);
                currentConfig = predecessor.get(currentConfig);
            }
            Collections.reverse(path);
        }
        return path;
    }

    /**
     * Returns the total number of configurations generated during solving.
     * @return total number of configurations
     */
    public int getTotalConfigurations() {
        return totalConfigurations;
    }

    /**
     * Returns the number of unique configurations encountered during the solving process.
     * @return the number of unique configurations
     */
    public int getUniqueConfigurations() {
        return predecessor.size();
    }
}