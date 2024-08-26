package puzzles.slide.model;
import puzzles.common.Coordinate;
import puzzles.common.model.Observer;
import puzzles.common.solver.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The SlideConfig class represents a configuration of the sliding numbers puzzle.
 */
public class SlideConfig implements Configuration {

    /** The 2D array representing the puzzle board. */
    private final int[][] board;

    /** The coordinate of the empty space on the puzzle board. */
    private final Coordinate emptySpace;

    /** List of registered observers for this configuration. */
    private final List<Observer<SlideConfig, Coordinate>> observers;


    /** Check whether the last move was successful.*/
    private boolean lastMoveSuccessful = true;

    /** Instantiates checking whether the last move was successful.*/
    public boolean isLastMoveSuccessful() {
        return lastMoveSuccessful;
    }


    /**
     * Constructor to create a new SlideConfig with the given puzzle board.
     * @param board the 2D array representing the puzzle board
     */
    public SlideConfig(int[][] board) {
        this.board = board;
        this.emptySpace = findEmptySpace();
        this.observers = new ArrayList<>();
    }

    /**
     * Find the coordinate of the empty space (0) on the puzzle board.
     * @return the coordinate of the empty space
     */
    private Coordinate findEmptySpace() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col] == 0) {
                    return new Coordinate(row, col);
                }
            }
        }
        throw new IllegalStateException("Puzzle board does not contain an empty space.");
    }


    /**
     * Get the current puzzle board as a 2D array.
     * @return the 2D array representing the puzzle board
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * Get the neighboring configurations of the current configuration.
     * A neighboring configuration is a valid configuration that can be reached
     * by sliding a numbered block into the empty space.
     * @return a list of neighboring configurations
     */
    @Override
    public List<Configuration> getNeighbors() {
        List<Configuration> neighbors = new ArrayList<>();
        int emptyRow = emptySpace.row();
        int emptyCol = emptySpace.col();

// Check if a block can be slid from north
        if (emptyRow > 0) {
            neighbors.add(slideBlock(emptyRow - 1, emptyCol));
        }

// Check if a block can be slid from south
        if (emptyRow < board.length - 1) {
            neighbors.add(slideBlock(emptyRow + 1, emptyCol));
        }

// Check if a block can be slid from west
        if (emptyCol > 0) {
            neighbors.add(slideBlock(emptyRow, emptyCol - 1));
        }

// Check if a block can be slid from east
        if (emptyCol < board[0].length - 1) {
            neighbors.add(slideBlock(emptyRow, emptyCol + 1));
        }

        return neighbors;
    }

    /**
     * Slide the numbered block at the specified position (newRow, newCol) into the empty space.
     * @param newRow row of the numbered block to slide
     * @param newCol column of the numbered block to slide
     * @return new SlideConfig after sliding the block
     */
    public SlideConfig slideBlock(int newRow, int newCol) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(board[row], 0, newBoard[row], 0, board[row].length);
        }
        int numToSlide = newBoard[newRow][newCol];
        newBoard[emptySpace.row()][emptySpace.col()] = numToSlide;
        newBoard[newRow][newCol] = 0;

        SlideConfig newConfig = new SlideConfig(newBoard);
        notifyObservers(emptySpace);
        return newConfig;
    }

    /**
     * Notify all registered observers with the given move.
     * @param move the coordinate of the empty space representing the move
     */
    private void notifyObservers(Coordinate move) {
        for (Observer<SlideConfig, Coordinate> observer : observers) {
            observer.update(this, move);
        }
    }

    /**
     * Moves a puzzle piece from the source cell to the target cell and generates a new configuration.
     * @param fromRow row index of the source cell.
     * @param fromCol column index of the source cell.
     * @param toRow row index of target cell.
     * @param toCol column index of target cell.
     * @return new SlideConfig representing the configuration after the move, or the current configuration if the move is illegal.
     */
    public SlideConfig movePiece(int fromRow, int fromCol, int toRow, int toCol) {
        int[][] newBoard = new int[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            System.arraycopy(board[row], 0, newBoard[row], 0, board[row].length);
        }
        int pieceValue = newBoard[fromRow][fromCol];
        if (Math.abs(fromRow - toRow) + Math.abs(fromCol - toCol) == 1 && newBoard[toRow][toCol] == 0) {
            newBoard[fromRow][fromCol] = 0; // Set source cell to empty
            newBoard[toRow][toCol] = pieceValue;
            SlideConfig newConfig = new SlideConfig(newBoard);
            notifyObservers(new Coordinate(toRow, toCol));
            return newConfig;
        } else {
            System.out.println("Can't move from (" + fromRow + ", " + fromCol + ") to (" + toRow + ", " + toCol + ")");
            lastMoveSuccessful = false;
            System.out.println(this.getFormattedStringForPTUI());
            return this; // Return current config if illegal move
        }
    }

    /**
     * Checks if current configuration is a solution.
     * @return true if the configuration is a solution, false otherwise.
     */
    @Override
    public boolean isSolution() {
        int expectedValue = 1;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                if (board[row][col] != expectedValue) {
                    if (expectedValue == board.length * board[row].length && board[row][col] == 0) {
                        return true;
                    }
                    return false;
                }
                expectedValue++;
            }
        }
        return true;
    }

    /**
     * Compares this SlideConfig object with another object.
     * @param obj The object to compare.
     * @return true if the objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SlideConfig)) {
            return false;
        }
        SlideConfig other = (SlideConfig) obj;
        return Arrays.deepEquals(this.board, other.board);
    }

    /**
     * Returns the hash code value for this SlideConfig object.
     * @return The hash code value.
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(this.board);
    }

    /**
     * Generates a formatted string representation of the configuration for PTUI.
     * @return formatted string.
     */
    public String getFormattedStringForPTUI() {
        StringBuilder sb = new StringBuilder();
        int rows = board.length;
        int cols = board[0].length;
        // Print column indexes
        sb.append("    ");
        for (int col = 0; col < cols; col++) {
            sb.append(col).append("   ");
        }
        sb.append("\n");
        sb.append("  ");
        for (int col = 0; col < cols; col++) {
            sb.append("----");
        }
        sb.append("\n");
        //axis for rows
        for (int row = 0; row < rows; row++) {
            sb.append(row).append("|");
            for (int col = 0; col < cols; col++) {
                int num = board[row][col];
                if (num == 0) {
                    sb.append("  . ");
                } else {
                    sb.append(String.format("%3d ", num));
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns a string representation of the configuration.
     * @return string representation.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int num : row) {
                if (num == 0) {
                    sb.append(" .");
                } else {
                    sb.append(String.format("%2d", num));
                }
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
