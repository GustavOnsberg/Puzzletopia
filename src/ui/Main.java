package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Stage pStage;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("PuzzleScreen.fxml"));
        primaryStage.setTitle("Puzzle Topia");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        pStage = primaryStage;
        primaryStage.show();
    }

    public void setStage(String fxmlName, Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        Scene oldScene = primaryStage.getScene();
        primaryStage.setScene(new Scene(root,oldScene.getWidth(),oldScene.getHeight()));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getpStage() {
        return pStage;
    }
}
