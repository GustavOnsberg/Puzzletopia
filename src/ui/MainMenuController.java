package ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import main.Puzzle;
import org.json.simple.parser.ParseException;
import sun.plugin.javascript.navig.Anchor;

import java.io.File;
import java.io.IOException;

public class MainMenuController {

    public Button btnStartGame;
    public Button btnUploadGameFile;
    public Puzzle puzzle;
    public static MainMenuController mainMenuController;
    public AnchorPane mainMenuCanvas;

    @FXML
    public void initialize() {
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


        // Setup canvas
        mainMenuCanvas.getStylesheets().add("ui/main_stylesheet.css");
        mainMenuCanvas.setId("main_menu_canvas");
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
