package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;

public class Main extends Application {
    public static Stage pStage;
    public static Main main;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
        primaryStage.setTitle("Puzzle Topia");
        primaryStage.setScene(new Scene(root));
        primaryStage.setMaximized(true);
        pStage = primaryStage;
        main = this;
        pStage.setMinHeight(1000);
        pStage.setMinWidth(1000);
        pStage.maxHeightProperty().bind(pStage.maxWidthProperty());
        Image icon = new Image("ui/icon.png");
        pStage.getIcons().add(icon);
        primaryStage.show();
        
    }

    public void setStage(String fxmlName, Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlName));
        Scene oldScene = primaryStage.getScene();
        primaryStage.setScene(new Scene(root,oldScene.getWidth(),oldScene.getHeight()));
        main = this;
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
