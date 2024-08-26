package puzzles.slide.gui;
import puzzles.common.solver.*;
import java.util.List;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import puzzles.common.model.Observer;
import puzzles.slide.model.SlideConfig;
import puzzles.slide.model.SlideModel;

import java.io.File;
import java.io.IOException;

/**
 * Graphical user interface for the Slide Puzzle game.
 * This class extends the JavaFX Application class and implements the Observer interface,
 * This class allows users to interact with the Slide Puzzle game through a GUI interface.
 * @author Isaac Soares
 * NOTE ABOUT HINT:
 * hint sometimes takes long to process. If more than 10 seconds to load hint the solution
 * path is taking too long and better to close GUI board and run again.
 */

public class SlideGUI extends Application implements Observer<SlideModel, String> {


    /**The initial configuration of Slide Puzzle board.*/
    private SlideConfig initialConfig;

    /**The row index of the current piece.*/
    private int selectedRow = -1;

    /**The column index of the current piece.*/
    private int selectedCol = -1;

    /** The SlideModel instance representing the game's logic and state.*/
    private SlideModel model;

    /**Size of the number icon.*/
    private final static int ICON_SIZE = 75;

    /**Font size for displaying numbers */
    private final static int NUMBER_FONT_SIZE = 24;

    /**Background color for even-numbered tiles.*/
    private final static String EVEN_COLOR = "#ADD8E6";

    /** Background color for odd-numbered tiles.*/
    private final static String ODD_COLOR = "#FED8B1";

    /**Background color for empty tiles*/
    private final static String EMPTY_COLOR = "#FFFFFF";

    /** The main application window stage.*/
    private Stage stage;

    /**The grid pane to display the game board.*/
    private GridPane gridPane;

    /**Label to display status messages.*/
    private Label statusLabel;

    /**Initialize the SlideModel with data from a file.*/
    @Override
    public void init() {
        String filename = getParameters().getRaw().get(0);
        try {
            SlideModel slideModel = new SlideModel(filename);
            this.model = slideModel;
            this.model.addObserver(this);
            this.initialConfig = new SlideConfig(slideModel.getCurrentConfig().getBoard()); // Store a copy of the initial config

        } catch (IOException e) {
            System.out.println("Error initializing SlideModel: " + e.getMessage());
        }
    }

    /**
     * Start the Slide Puzzle game GUI.
     * @param stage The main stage for the GUI.
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.setTitle("Slide GUI");
        gridPane = createGridPane();
        HBox buttonBox = createButtonBox();
        VBox mainLayout = new VBox(gridPane, buttonBox);
        Scene scene = new Scene(mainLayout);
        stage.setScene(scene);
        attachButtonEventHandlers();
        stage.show();
    }

    /**Update the visual representation of the game board.*/
    private void updateGrid() {
        gridPane.getChildren().clear();
        int[][] board = model.getCurrentConfig().getBoard();
        gridPane.add(statusLabel, 0, 0, board[0].length, 1);
        GridPane.setHalignment(statusLabel, HPos.CENTER);
        generateNumberButtons();
    }

    /**Generate number buttons for the game board.*/
    private void generateNumberButtons() {
        int[][] board = model.getCurrentConfig().getBoard();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Button button = createButton(board[row][col]);
                gridPane.add(button, col, row + 1);
                GridPane.setValignment(button, VPos.TOP);
                GridPane.setHalignment(button, HPos.LEFT);
                final int finalRow = row;
                final int finalCol = col;
                button.setOnAction(event -> handleButtonClick(finalRow, finalCol));
            }
        }
    }

    /**
     * Create a new GridPane to represent game board.
     * @return The newly created GridPane.
     */
    private GridPane createGridPane() {
        gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.setVgap(5);
        statusLabel = new Label("Status Area");
        gridPane.add(statusLabel, 0, 0);
        GridPane.setHalignment(statusLabel, HPos.CENTER);
        updateGrid();
        return gridPane;
    }

    /**
     * Create a new button with styling based on number.
     * @param number The number to display on button.
     * @return  newly created button.
     */
    private Button createButton(int number) {
        Button button = new Button();
        button.setMinSize(ICON_SIZE, ICON_SIZE);
        button.setMaxSize(ICON_SIZE, ICON_SIZE);
        button.setPadding(new Insets(5));
        button.setStyle(
                "-fx-font-family: Arial;" +
                        "-fx-font-size: " + NUMBER_FONT_SIZE + ";" +
                        "-fx-font-weight: bold;");
        if (number == 0) {
            button.setStyle("-fx-background-color: " + EMPTY_COLOR + ";");
        } else if (number % 2 == 0) {
            button.setStyle("-fx-background-color: " + EVEN_COLOR + ";");
        } else {
            button.setStyle("-fx-background-color: " + ODD_COLOR + ";");
        }
        button.setText(String.valueOf(number));
        return button;
    }

    /**
     * Attach event handlers to the buttons on the game board.
     */
    private void attachButtonEventHandlers() {
        int[][] board = model.getCurrentConfig().getBoard();
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Button button = createButton(board[row][col]);
                final int finalRow = row;
                final int finalCol = col;
                button.setOnAction(event -> handleButtonClick(finalRow, finalCol));
                gridPane.add(button, col, row + 1);
                GridPane.setValignment(button, VPos.TOP);
                GridPane.setHalignment(button, HPos.LEFT);
            }
        }
    }

    /**
     * Handle button click events.
     * @param row The row index of the clicked button.
     * @param col The column index of the clicked button.
     */
    private void handleButtonClick(int row, int col) {
        if (selectedRow == -1 && selectedCol == -1) {
            // First selection: Selecting piece
            int pieceValue = model.getCurrentConfig().getBoard()[row][col];
            if (pieceValue != 0) {
                selectedRow = row;
                selectedCol = col;
                statusLabel.setText("Selected (" + row + ", " + col + ")");
            } else {
                statusLabel.setText("No number at (" + row + ", " + col + ")");
            }
        } else {
            // Second selection: Moving the selected piece
            SlideConfig currentConfig = model.getCurrentConfig();
            int fromRow = selectedRow;
            int fromCol = selectedCol;
            // Clear selection
            selectedRow = -1;
            selectedCol = -1;
            if (Math.abs(fromRow - row) + Math.abs(fromCol - col) == 1 && currentConfig.getBoard()[row][col] == 0) {
                // legal move
                SlideConfig newConfig = currentConfig.movePiece(fromRow, fromCol, row, col);
                model.setCurrentConfig(newConfig);
                statusLabel.setText("Moved from (" + fromRow + ", " + fromCol + ") to (" + row + ", " + col + ")");
                updateGrid();
            } else {
                // illegal move
                statusLabel.setText("Can't move from (" + fromRow + ", " + fromCol + ") to (" + row + ", " + col + ")");
            }
        }

    }

    /**
     * Create a horizontal box containing buttons for interactions.
     * @return The newly created HBox.
     */
    private HBox createButtonBox() {
        Button loadButton = new Button("Load");
        Button resetButton = new Button("Reset");
        Button hintButton = new Button("Hint");
        loadButton.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("data/slide"));
            File selectedFile = fileChooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    SlideConfig newConfig = model.loadConfigFromFile(selectedFile.getPath());
                    model.setCurrentConfig(newConfig);
                    statusLabel.setText("Loaded: " + selectedFile.getName() + " extend window if you can't see");
                    initialConfig = model.getCurrentConfig();
                    updateGrid();
                } catch (IOException e) {
                    System.out.println("Error loading the puzzle file: " + e.getMessage());
                }
            }
        });
        resetButton.setOnAction(event -> {
            model.setCurrentConfig(initialConfig);
            statusLabel.setText("Puzzle reset!");
            updateGrid();
        });
        hintButton.setOnAction(event -> {
            Solver solver = new Solver(model.getCurrentConfig());
            boolean solutionFound = solver.solve();
            if (solutionFound) {
                List<Configuration> solutionPath = solver.getSolutionPath();
                int currentStep = solutionPath.indexOf(model.getCurrentConfig());
                if (currentStep < solutionPath.size() - 1) {
                    SlideConfig nextStep = (SlideConfig) solutionPath.get(currentStep + 1);
                    // Create a new config based on the solved step
                    SlideConfig newConfig = new SlideConfig(nextStep.getBoard());
                    model.setCurrentConfig(newConfig);
                    statusLabel.setText("Hint: Next step!");
                    updateGrid();
                } else {
                    statusLabel.setText("Hint: Already solved!");
                }
            } else {
                statusLabel.setText("Hint: No solution found for the puzzle.");
            }
        });
        HBox buttonBox = new HBox(loadButton, resetButton, hintButton);
        buttonBox.setSpacing(10);
        buttonBox.setStyle("-fx-padding: 10px;");
        HBox.setHgrow(loadButton, Priority.ALWAYS);
        HBox.setHgrow(resetButton, Priority.ALWAYS);
        HBox.setHgrow(hintButton, Priority.ALWAYS);
        buttonBox.setAlignment(Pos.CENTER);
        return buttonBox;
    }

    /** null not used  */
    @Override
    public void update(SlideModel model, String data) {}

    /**
     * The main method to launch the application.
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
