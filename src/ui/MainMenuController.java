package ui;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import main.Puzzle;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class MainMenuController {

    public Button btnStartGame;
    public Button btnUploadGameFile;
    public Puzzle puzzle;
    public static MainMenuController mainMenuController;


    public void handleStartGameBtn(ActionEvent actionEvent) throws IOException {
        Main main = new Main();
        main.setStage("/ui/PuzzleScreen.fxml", Main.pStage);
    }

    public void handleUploadGameFileBtn(ActionEvent actionEvent) throws IOException, ParseException {
        FileChooser fc = new FileChooser();
        fc.setTitle("Choose a JSON File");
        File gameFile = fc.showOpenDialog(Main.pStage);
        if (gameFile != null && (gameFile.getName().endsWith(".json") || gameFile.getName().endsWith(".JSON"))) {
            puzzle = new Puzzle();
            puzzle.loadPuzzle(gameFile.getPath());
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
