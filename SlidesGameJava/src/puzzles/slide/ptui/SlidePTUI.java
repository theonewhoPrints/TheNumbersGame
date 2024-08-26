package puzzles.slide.ptui;
import puzzles.common.Coordinate;
import puzzles.common.model.Observer;
import puzzles.slide.model.SlideConfig;
import puzzles.slide.model.SlideModel;
import puzzles.common.solver.*;
import java.io.IOException;
import java.util.Scanner;
import java.util.*;

/**
 * Represents the plain text user interface (PTUI) for the Slide Puzzle game.
 * This class handles user interaction and displays the game state and messages in the console.
 */

public class SlidePTUI implements Observer<SlideModel, String> {

    /** The SlideModel instance associated with the PTUI */
    private SlideModel model;

    /** filename of the loaded puzzle */
    private String loadedFilename;

    /** Selected row for user input */
    private int selectedRow = -1;

    /** Selected column for user */
    private int selectedCol = -1;

    /**
     * Initializes the PTUI with a puzzle configuration.
     * @param filename The name of the file containing the puzzle configuration.
     * @throws IOException If there is an error loading the puzzle configuration.
     */
    public void init(String filename) throws IOException {
        this.model = new SlideModel(filename);
        this.model.addObserver(this);
        displayPuzzleInfo(filename);
        displayHelp();
    }

    /**
     * Displays puzzle information after loading and shows the help commands.
     * @param filename loaded puzzle configuration file.
     */
    private void displayPuzzleInfo(String filename) {
        String strippedFilename = filename.substring(filename.lastIndexOf("/") + 1);
        System.out.println("Loaded: " + strippedFilename);
        System.out.println(model.getCurrentConfig().getFormattedStringForPTUI());
        loadedFilename = filename;
    }

    /** null - not used  */
    @Override
    public void update(SlideModel model, String data) {}

    /** Displays a list of available commands and their explanations.*/
    private void displayHelp() {
        System.out.println("h(int)              -- hint next move");
        System.out.println("l(oad) filename     -- load new puzzle file");
        System.out.println("s(elect) r c        -- select cell at r, c");
        System.out.println("q(uit)              -- quit the game");
        System.out.println("r(eset)             -- reset the current game");
    }

    /** Starts the loop for the PTUI.*/
    public void run() {
        Scanner in = new Scanner(System.in);
        for (;;) {
            String line = in.nextLine();
            String[] words = line.split("\\s+");
            if (words.length > 0) {
                String command = words[0].toLowerCase();
                switch (command) {
                    case "q":
                        handleQuitCommand();
                        return;
                    case "h":
                        handleHintCommand();
                        break;
                    case "l":
                        if (words.length >= 2) {
                            handleLoadCommand(words[1]);
                        } else {
                            System.out.println("Usage: l(oad) filename");
                        }
                        break;
                    case "s":
                        if (words.length == 3) {
                            int row = Integer.parseInt(words[1]);
                            int col = Integer.parseInt(words[2]);
                            handleSelectCommand(row, col);
                        } else {
                            displayHelp();
                        }
                        break;
                    case "r":
                        handleResetCommand();
                        break;
                    default:
                        displayHelp();
                        break;
                }
            }
        }
    }

    /**
     * Handles the selection of a cell.
     * @param row The selected row.
     * @param col The selected column.
     */
    private void handleSelectCommand(int row, int col) {
        if (row < 0 || row >= model.getCurrentConfig().getBoard().length ||
                col < 0 || col >= model.getCurrentConfig().getBoard()[0].length) {
            SlideConfig config = model.getCurrentConfig();
            System.out.println("Invalid selection (" + row + ", " + col + ")");
            System.out.println(config.getFormattedStringForPTUI());
            return;
        }
        if (selectedRow == -1 && selectedCol == -1) {
            // First selection: Selecting a piece
            SlideConfig config = model.getCurrentConfig();
            int pieceValue = config.getBoard()[row][col];
            if (pieceValue != 0) {
                selectedRow = row;
                selectedCol = col;
                System.out.println("Selected (" + row + ", " + col + ")");
                System.out.println(config.getFormattedStringForPTUI());
            } else {
                System.out.println("No number at (" + row + ", " + col + ")");
                System.out.println(config.getFormattedStringForPTUI());

            }
        } else {
            // Second selection: Moving the selected piece
            boolean success = false;
            SlideConfig newConfig = null;
            try {
                newConfig = model.getCurrentConfig().movePiece(selectedRow, selectedCol, row, col);
                success = true;
            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());
            }
            if (success && newConfig.isLastMoveSuccessful()) {
                System.out.println("Moved from (" + selectedRow + ", " + selectedCol + ") to (" + row + ", " + col + ")");
                System.out.println(newConfig.getFormattedStringForPTUI());
                model.setCurrentConfig(newConfig);
            }
            selectedRow = -1;
            selectedCol = -1;
        }
    }

    /**
     * Handles the "load" command to load a new puzzle configuration
     * @param filename The name of the file containing the new puzzle configuration.
     */
    private void handleLoadCommand(String filename) {
        try {
            SlideConfig newConfig = model.loadConfigFromFile(filename);
            model.setCurrentConfig(newConfig);
            loadedFilename = filename;
            displayPuzzleInfo(loadedFilename);
            displayHelp();
        } catch (IOException e) {
            System.out.println("Error loading the puzzle file: " + e.getMessage());
        }
    }

    /** Handles the "reset" command to reset the current puzzle configuration.*/
    private void handleResetCommand() {
        model.reset();
        displayPuzzleInfo(loadedFilename);
        System.out.println("Puzzle reset!");
        System.out.println(model.getCurrentConfig().getFormattedStringForPTUI());
    }

    /** Handles the "hint" command to provide a hint for the next move.*/
    private void handleHintCommand() {
        Solver solver = new Solver(model.getCurrentConfig());
        boolean solutionFound = solver.solve();
        if (solutionFound) {
            List<Configuration> solutionPath = solver.getSolutionPath();
            int currentStep = solutionPath.indexOf(model.getCurrentConfig());
            if (currentStep < solutionPath.size() - 1) {
                SlideConfig nextStep = (SlideConfig) solutionPath.get(currentStep + 1);
                model.setCurrentConfig(nextStep);
                System.out.println("Next step!");
                System.out.println(nextStep.getFormattedStringForPTUI());
            } else {
                System.out.println("Already solved!");
                System.out.println(model.getCurrentConfig().getFormattedStringForPTUI());
            }
        } else {
            System.out.println("No solution found for the puzzle.");
            System.out.println(model.getCurrentConfig().getFormattedStringForPTUI());
        }
    }

    /** Handles the "quit" command to exit the PTUI.*/
    private void handleQuitCommand() {
        System.out.println("Goodbye! Thank you for playing!");
        System.exit(0);
    }

    /**
     * The main entry point for the SlidePTUI application.
     * @param args The command-line arguments.
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java SlidePTUI filename");
        } else {
            try {
                SlidePTUI ptui = new SlidePTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }

}
