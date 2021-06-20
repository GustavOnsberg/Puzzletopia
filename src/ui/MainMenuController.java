package ui;

import javafx.animation.Animation;
import javafx.animation.PathTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import main.Puzzle;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainMenuController {

    public Button btnGenerateGame;
    public Button btnUploadGameFile;
    public Puzzle puzzle;
    public static MainMenuController mainMenuController;
    public AnchorPane mainMenuCanvas;
    private final ArrayList<Color> blockColors = new ArrayList<>();

    @FXML
    public void initialize() {
        // First method to run when the controller loads - Frederik

        // Setup background
        createBackground();

        //Setup buttons
        btnGenerateGame.getStylesheets().add("ui/main_stylesheet.css");
        btnUploadGameFile.getStylesheets().add("ui/main_stylesheet.css");
        btnGenerateGame.setId("main_menu_button");
        btnUploadGameFile.setId("main_menu_button");
        btnGenerateGame.setText("START NEW PUZZLE");
        btnUploadGameFile.setText("UPLOAD GAME FILE");
        btnGenerateGame.setPrefWidth(300);
        btnUploadGameFile.setPrefWidth(300);
        AnchorPane.setTopAnchor(btnGenerateGame, 50.0);
        AnchorPane.setLeftAnchor(btnGenerateGame,50.0);
        AnchorPane.setTopAnchor(btnUploadGameFile, 200.0);
        AnchorPane.setLeftAnchor(btnUploadGameFile,50.0);
        btnGenerateGame.setSkin(new MainMenuButtonSkin(btnGenerateGame));
        btnUploadGameFile.setSkin(new MainMenuButtonSkin(btnUploadGameFile));


        // Setup canvas
        mainMenuCanvas.getStylesheets().add("ui/main_stylesheet.css");
        mainMenuCanvas.setId("main_menu_canvas");
    }

    private void createBackground() {
        // Creates the moving background - Frederik

        // Setup block colors
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            blockColors.add(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        }
        int color = 0;
        int[] durations = {1000, 1200, 1300, 1400, 1600, 1800, 2000, 2200, 2400, 2600, 2800, 3000};
        int durationNum;
        ArrayList<Rectangle> blocks = new ArrayList<>();

        // Create the blocks
        for (int i = 0; i < 50; i++) {
            Rectangle newBlock = new Rectangle();
            newBlock.setWidth(random.nextInt(150 - 20) + 20);
            newBlock.setHeight(random.nextInt(150 - 20) + 20);
            newBlock.setFill(blockColors.get(color));
            blocks.add(newBlock);
            mainMenuCanvas.getChildren().add(blocks.get(i));
            color++;
        }

        // Make the blocks fall
        for (Rectangle block : blocks) {
            Path path = new Path();
            durationNum = random.nextInt(durations.length);
            int xVal = random.nextInt(2500 - 500) + 500;
            path.getElements().add(new MoveTo(xVal, random.nextInt((100) + (1000) - 1000) * -1));
            path.getElements().add(new LineTo(xVal, 1500));
            PathTransition pathTransition = new PathTransition();
            pathTransition.setDuration(Duration.millis(durations[durationNum]));
            pathTransition.setCycleCount(Animation.INDEFINITE);
            pathTransition.setPath(path);
            pathTransition.setNode(block);
            pathTransition.play();
        }
    }


    public void handleGenerateGameBtn(ActionEvent actionEvent) throws IOException {
        // Setup the inputs for the generation of a puzzle - Frederik

        mainMenuController = this;
        puzzle = new Puzzle();

        // Create the dialog to input the numbers for generation
        Dialog generationSettings = new Dialog();
        generationSettings.setTitle("Input settings for generation of the puzzle");
        generationSettings.setHeaderText("Input Settings");
        DialogPane dialogPane = generationSettings.getDialogPane();
        dialogPane.setPrefWidth(780);
        dialogPane.setPrefHeight(200);
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Stage dialog = (Stage) dialogPane.getScene().getWindow();
        dialog.getIcons().add(new Image(String.valueOf(this.getClass().getResource("cog.png"))));

        // Create the textfields
        TextField nText = new TextField();
        TextField mText = new TextField();
        TextField cutsText = new TextField();
        TextField varText = new TextField();
        nText.getStylesheets().add("ui/main_stylesheet.css");
        mText.getStylesheets().add("ui/main_stylesheet.css");
        cutsText.getStylesheets().add("ui/main_stylesheet.css");
        varText.getStylesheets().add("ui/main_stylesheet.css");
        nText.setPromptText("Input n (must be 3 or above)");
        mText.setPromptText("Input m (must be 3 or above)");
        cutsText.setPromptText("Input number of cuts (must be 1 or above)");
        varText.setPromptText("Input distance between cuts (must be above 0, but less than 1/(cuts + 2))");
        dialogPane.setContent(new VBox(10, nText, mText, cutsText, varText));

        // Create logic and alerts for the inputs
        Alert error = new Alert(Alert.AlertType.ERROR);
        error.setTitle("INPUT ERROR");
        dialogPane.lookupButton(ButtonType.OK).addEventFilter(ActionEvent.ACTION, event -> {
            if (!nText.getText().matches("\\d*") || nText.getText().equals("")) {
                error.setHeaderText("The input, n, must be an integer");
                error.showAndWait();
                event.consume();
            } else if (nText.getText().matches("\\d*") && !(Integer.parseInt(nText.getText()) >= 3)) {
                error.setHeaderText("The input, n, must be 3 or above");
                error.showAndWait();
                event.consume();
            } else if (!mText.getText().matches("\\d*") || mText.getText().equals("")) {
                error.setHeaderText("The input, m, must be an integer");
                error.showAndWait();
                event.consume();
            } else if (mText.getText().matches("\\d*") && !(Integer.parseInt(mText.getText()) >= 3)) {
                error.setHeaderText("The input, m, must be 3 or above");
                error.showAndWait();
                event.consume();
            } else if (!cutsText.getText().matches("\\d*") || cutsText.getText().equals("")) {
                error.setHeaderText("The input, cuts, must be an integer");
                error.showAndWait();
                event.consume();
            } else if (cutsText.getText().matches("\\d*") && !(Integer.parseInt(cutsText.getText()) >= 1)) {
                error.setHeaderText("The input, cuts, must be 1 or above");
                error.showAndWait();
                event.consume();
            } else if (!varText.getText().matches("[+-]?([0-9]*[.])?[0-9]+") || varText.getText().equals("")) {
                error.setHeaderText("The input, distance, must be a float");
                error.showAndWait();
                event.consume();
            } else if (varText.getText().matches("[+-]?([0-9]*[.])?[0-9]+") && Float.parseFloat(varText.getText()) > 0 && (Float.parseFloat(varText.getText())) > 1/(Float.parseFloat(cutsText.getText()) + 2)) {
                error.setHeaderText("The input, distance, must be above 0, but less than 1/(cuts + 2)");
                error.showAndWait();
                event.consume();
            } else {
                mainMenuController.getPuzzle().generatePuzzle(Integer.parseInt(nText.getText()), Integer.parseInt(mText.getText()), Integer.parseInt(cutsText.getText()), Float.parseFloat(varText.getText()));
                try {
                    Main.main.setStage("/ui/PuzzleScreen.fxml", Main.pStage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            });

        generationSettings.show();

    }

    public void handleUploadGameFileBtn(ActionEvent actionEvent) throws IOException, ParseException {
        // Setup the button for uploading a JSON file - Frederik
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a JSON File");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JSON files (*.json)", "*.json");
        fc.getExtensionFilters().add(extensionFilter);
        File gameFile = fc.showOpenDialog(Main.pStage);
        if (gameFile != null && (gameFile.getName().endsWith(".json") || gameFile.getName().endsWith(".JSON"))) {
            puzzle = new Puzzle();
            puzzle.loadPuzzle(gameFile.getPath());
            mainMenuController = this;
            Main.main.setStage("/ui/PuzzleScreen.fxml", Main.pStage);
        } else if (gameFile == null) {
            // Do nothing
        } else {
           Alert fileErorr = new Alert(Alert.AlertType.ERROR);
           fileErorr.setTitle("File Extension Error");
           fileErorr.setHeaderText("Please choose a JSON file");
           fileErorr.showAndWait();
        }
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }
}
