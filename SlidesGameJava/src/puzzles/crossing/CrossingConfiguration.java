package puzzles.crossing;

import puzzles.common.solver.Configuration;
import java.util.*;

/**
 * The CrossingConfiguration class represents a configuration of Crossing puzzle.
 *  number of animals on river sides.
 *  gives position of the boat, and provides methods to check if it is a solution and creates neighbors.
 */
public class CrossingConfiguration implements Configuration {
    private int RTpups;
    private int LTpups;
    private int LTwolf;
    private int RTwolf;
    private Side boat;

    /**
     * Constructs a Crossing Configuration with the specified numbers of pups and wolves
     * on the left side and initializes the other side with zero pups, zero wolves, and the boat on the left side.
     *
     * @param LTpups   the number of pups on  left side
     * @param LTwolf   the number of wolves on  left side
     * @param RTpups   the number of pups on right side
     * @param RTwolf   the number of wolves on  right side
     * @param boat     the position of the boat (LEFT or RIGHT)
     */
    public CrossingConfiguration(int LTpups, int LTwolf, int RTpups, int RTwolf, Side boat) {
        this.LTpups = LTpups;
        this.RTpups = RTpups;
        this.LTwolf = LTwolf;
        this.RTwolf = RTwolf;
        this.boat = boat;
    }

    /**
     * Checks current configuration solution or no.
     * setting to left.
     * @return true if the current configuration is a solution, false otherwise
     */
    @Override
    public boolean isSolution() {
        return LTpups == 0 && LTwolf == 0;
    }

    /**
     * Constructs a CrossingConfiguration object with the specified numbers of pups and wolves on the left side,
     * and initializes the other side zero animals.
     * @param numOfPups    the number of pups on left side
     * @param numOfWolves  the number of wolves on left side
     */
    public CrossingConfiguration(int numOfPups, int numOfWolves) {
        this(numOfPups, numOfWolves, 0, 0, Side.LEFT);
    }

    /**
     * Generates neighboring configurations for the current configuration.
     * @return a collection of neighboring configurations
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        if (boat == Side.LEFT) {
            if (LTwolf >= 1) {
                neighbors.add(new CrossingConfiguration(LTpups, LTwolf - 1, RTpups, RTwolf + 1, Side.RIGHT));
            }
            for (int pups = 1; pups <= 2; pups++) {
                if (LTpups >= pups) {
                    neighbors.add(new CrossingConfiguration(LTpups - pups, LTwolf, RTpups + pups, RTwolf, Side.RIGHT));                }
            }
        } else {
            if (RTwolf >= 1) {
                neighbors.add(new CrossingConfiguration(LTpups, LTwolf + 1, RTpups, RTwolf - 1, Side.LEFT));
            }
            for (int pups = 0; pups <= 2; pups++) {
                if (RTpups >= pups) {
                    neighbors.add(new CrossingConfiguration(LTpups + pups, LTwolf, RTpups - pups, RTwolf, Side.LEFT));
                }
            }
        }
        return neighbors;
    }

    /**
     * Checks if current object is equal to another object.
     * equal if all the corresponding fields are equal.
     * @param other object to compare
     * @return true if objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        CrossingConfiguration that = (CrossingConfiguration) other;
        return LTpups == that.LTpups && RTpups == that.RTpups &&
                LTwolf == that.LTwolf && RTwolf == that.RTwolf &&
                boat == that.boat;
    }


    /**
     * hash code for the current object.
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(LTpups, RTpups, LTwolf, RTwolf, boat);
    }

    /**
     * Returns a string of the current object.
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return "left=[" + LTpups + ", " + LTwolf + "], right=[" + RTpups + ", " + RTwolf + "]" +
                "  (BOAT: " + boat + ")";
    }
}

/**
 * enums for side of river with boat.
 */
enum Side {
    LEFT,
    RIGHT
}
