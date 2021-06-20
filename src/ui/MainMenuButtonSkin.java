package ui;

import com.sun.javafx.scene.control.skin.ButtonSkin;
import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.util.Duration;

public class MainMenuButtonSkin extends ButtonSkin {
    public MainMenuButtonSkin(Button button) {
        // Makes the fade in and fade out animation for the menu buttons - Frederik
        super(button);
        FadeTransition fadeIn = new FadeTransition(Duration.millis(100));
        fadeIn.setNode(button);
        fadeIn.setToValue(1);
        button.setOnMouseEntered(event -> fadeIn.playFromStart());
        FadeTransition fadeOut = new FadeTransition(Duration.millis(100));
        fadeOut.setNode(button);
        fadeOut.setToValue(0.8);
        button.setOnMouseExited(event -> fadeOut.playFromStart());
        button.setOpacity(0.8);
        
    }
}
