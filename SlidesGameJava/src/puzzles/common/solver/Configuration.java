package puzzles.common.solver;

import java.util.Collection;

/**
 * Represents a configuration or state in a problem-solving domain.
 * A configuration is a possible state in the problem space that can be
 * explored during the solving process.
 */
public interface Configuration {

    /**
     * Checks if the current configuration is a solution to the problem.
     * @return True if the configuration is a solution, false otherwise.
     */
    boolean isSolution();

    /**
     * Retrieves a collection of neighboring configurations from the current
     * configuration.
     * @return A collection of neighboring configurations.
     */
    Collection<Configuration> getNeighbors();

    /**
     * Compares the current configuration with another object for equality.
     * @param other The object to compare with.
     * @return True if the objects are equal, false otherwise.
     */
    boolean equals(Object other);

    /**
     * Generates a hash code for the current configuration.
     * @return The hash code of the configuration.
     */
    int hashCode();

    /**
     * Returns a string representation of the configuration.
     * @return A string describing the configuration.
     */
    String toString();
}
