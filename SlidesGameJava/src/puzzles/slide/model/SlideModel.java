package puzzles.slide.model;
import puzzles.common.solver.*;
import puzzles.common.Coordinate;
import puzzles.common.model.Observer;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents the model for the Slide Puzzle game.
 * This class handles the logic of managing puzzle configurations and notifying observers.
 */

public class SlideModel {
    /** the current configuration */
    private SlideConfig currentConfig;

    /** The filename of the initial configuration */
    private final String initialFilename;

    /** The initial configuration of the puzzle */
    private SlideConfig initialConfig;

    /** the collection of observers of this model */
    private final List<Observer<SlideModel, String>> observers = new LinkedList<>();

    /**
     * Constructs a SlideModel object with the initial configuration loaded from specified file.
     * @param filename The name of the file containing the puzzle configuration.
     * @throws IOException If there is an error reading the file.
     */
    public SlideModel(String filename) throws IOException {
        this.initialFilename = filename;
        this.initialConfig = loadConfigFromFile(filename);
        this.currentConfig = initialConfig;
    }

    /**
     * Load the puzzle configuration from a file and create a SlideConfig object.
     * @param filename the name of the file containing puzzle configuration
     * @return the initial SlideConfig
     * @throws IOException if there is an error reading the file
     */
    public SlideConfig loadConfigFromFile(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line = reader.readLine();
            String[] dimensions = line.trim().split("\\s+");
            int rows = Integer.parseInt(dimensions[0]);
            int cols = Integer.parseInt(dimensions[1]);
            int[][] board = new int[rows][cols];
            for (int row = 0; row < rows; row++) {
                line = reader.readLine();
                String[] values = line.trim().split("\\s+");
                for (int col = 0; col < cols; col++) {
                    board[row][col] = values[col].equals(".") ? 0 : Integer.parseInt(values[col]);
                }
            }
            return new SlideConfig(board);
        }
    }

    /**
     * Resets the current configuration to the initial configuration.
     */
    public void reset() {
        this.currentConfig = new SlideConfig(initialConfig.getBoard()); // Create a copy of the initial configuration
        alertObservers("");
    }

    /**
     * The view calls this to add itself as an observer.
     * @param observer the view
     */
    public void addObserver(Observer<SlideModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * update method
     */
    private void alertObservers(String data) {
        for (var observer : observers) {
            observer.update(this, data);
        }
    }

    /**
     * Get the current configuration of the puzzle.
     * @return the current SlideConfig
     */
    public SlideConfig getCurrentConfig() {
        return currentConfig;
    }

    /**
     * Sets the current configuration of puzzle.
     * @param newConfig The new SlideConfig representing updated configuration.
     */
    public void setCurrentConfig(SlideConfig newConfig) {
        this.currentConfig = newConfig;
        alertObservers("Loaded: " + newConfig);
    }
}