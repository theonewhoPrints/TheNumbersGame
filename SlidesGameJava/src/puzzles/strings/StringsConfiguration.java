package puzzles.strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import puzzles.common.solver.Configuration;

/**
 * The Strings Configuration class represents config of Strings puzzle.
 * Contains a string and provides methods to check if it is a solution, generate its neighbors.
 */
public class StringsConfiguration implements Configuration {

    /**The string representing the current state in the strings puzzle.*/
    private String string;

    /**The string representing the target or finish in the strings puzzle.*/
    private String FinishedString;

    /**
     * Gets the current string config.
     * @return the current string
     */
    public String getString() {
        return string;
    }

    /**
     * Constructs a Strings Configuration object with the initial and finish strings.
     * @param string       the initial string
     * @param FinishedString the finish string
     */
    public StringsConfiguration(String string, String FinishedString) {
        this.string = string;
        this.FinishedString = FinishedString;
    }

    /**
     * Checks if the current string configuration = solution.
     * @return true if the current string matches finish string, false otherwise
     */
    @Override
    public boolean isSolution() {
        return string.equals(FinishedString);
    }

    /**
     * Generates the neighboring config for current string configuration.
     * @return a collection of neighboring configurations
     */
    @Override
    public Collection<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        // neighbors by moving chars
        for (int i = 0; i < string.length(); i++) {
            char currentChar = string.charAt(i);
            char nextChar = getNextCharacter(currentChar);
            char prevChar = getPreviousCharacter(currentChar);
            // make new strings by replacing position
            String nextString = string.substring(0, i) + nextChar + string.substring(i + 1);
            String prevString = string.substring(0, i) + prevChar + string.substring(i + 1);
            //neigbors as configuration.
            neighbors.add(new StringsConfiguration(nextString, FinishedString));
            neighbors.add(new StringsConfiguration(prevString, FinishedString));
        }
        return neighbors;
    }

    /**
     * Checks if the current object is equal to another object.
     * they are equal if strings are equal
     * @param other object to compare
     * @return true if objects equal, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        if (this == other) {
            return true;
        }
        StringsConfiguration that = (StringsConfiguration) other;
        return string.equals(that.string);
    }

    /**
     * Returns hash for the current object.
     *
     * @return the hash code value
     */
    @Override
    public int hashCode() {
        return string.hashCode();
    }

    /**
     * Returns a string representation of current object.
     * @return a string representation of the object
     */
    @Override
    public String toString() {
        return string;
    }

    /**
     * Retrieves the character that follows the given character in the alphabet.
     * @param ch The input character.
     * @return The next character in the alphabet sequence.
     */
    private char getNextCharacter(char ch) {
        if (ch == 'Z') {
            return 'A';
        } else {
            return (char) (ch + 1);
        }
    }

    /**
     * Retrieves the character that precedes the given character in the alphabet.
     * @param ch The input character.
     * @return The previous character in the alphabet sequence.
     */
    private char getPreviousCharacter(char ch) {
        if (ch == 'A') {
            return 'Z';
        } else {
            return (char) (ch - 1);
        }
    }
}
