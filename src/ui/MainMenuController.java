package ui;

import javafx.animation.Animation;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import main.Puzzle;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class MainMenuController {

    public Button btnStartGame;
    public Button btnUploadGameFile;
    public Puzzle puzzle;
    public static MainMenuController mainMenuController;
    public AnchorPane mainMenuCanvas;
    private final ArrayList<Color> blockColors = new ArrayList<>();

    @FXML
    public void initialize() {
        // Setup background
        createBackground();

        //Setup buttons
        btnStartGame.getStylesheets().add("ui/main_stylesheet.css");
        btnUploadGameFile.getStylesheets().add("ui/main_stylesheet.css");
        btnStartGame.setId("main_menu_button");
        btnUploadGameFile.setId("main_menu_button");
        btnStartGame.setText("START NEW PUZZLE");
        btnUploadGameFile.setText("UPLOAD GAME FILE");
        btnStartGame.setPrefWidth(300);
        btnUploadGameFile.setPrefWidth(300);
        AnchorPane.setTopAnchor(btnStartGame, 50.0);
        AnchorPane.setLeftAnchor(btnStartGame,50.0);
        AnchorPane.setTopAnchor(btnUploadGameFile, 200.0);
        AnchorPane.setLeftAnchor(btnUploadGameFile,50.0);
        btnStartGame.setSkin(new MainMenuButtonSkin(btnStartGame));
        btnUploadGameFile.setSkin(new MainMenuButtonSkin(btnUploadGameFile));


        // Setup canvas
        mainMenuCanvas.getStylesheets().add("ui/main_stylesheet.css");
        mainMenuCanvas.setId("main_menu_canvas");
    }

    private void createBackground() {
        Random random = new Random();
        for (int i = 0; i < 50; i++) {
            blockColors.add(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        }
        int color = 0;
        int[] durations = {1000, 1200, 1300, 1400, 1600, 1800, 2000, 2200, 2400, 2600, 2800, 3000};
        int durationNum;
        ArrayList<Rectangle> blocks = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Rectangle newBlock = new Rectangle();
            newBlock.setWidth(random.nextInt(150 - 20) + 20);
            newBlock.setHeight(random.nextInt(150 - 20) + 20);
            newBlock.setFill(blockColors.get(color));
            blocks.add(newBlock);
            mainMenuCanvas.getChildren().add(blocks.get(i));
            color++;
        }
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


    public void handleStartGameBtn(ActionEvent actionEvent) throws IOException {
        mainMenuController = this;
        Main.main.setStage("/ui/PuzzleScreen.fxml", Main.pStage);
    }

    public void handleUploadGameFileBtn(ActionEvent actionEvent) throws IOException, ParseException {
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
