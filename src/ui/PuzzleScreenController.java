package ui;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PuzzleScreenController {
    public AnchorPane puzzleCanvas;
    public double originalX, originalY;
    public int puzzleBoardWidth = 1000;
    public int puzzleBoardHeight = 1000;
    public Image puzzlePicture = new Image("ui/test.png");
    private float puzzleScale = 100;
    ArrayList<Point> pointCorners = new ArrayList<>();
    ArrayList<Color> testColors = new ArrayList<>();
    public PuzzleScreenController() {
    }

    @FXML
    public void initialize() {
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().size() - 1; i++) {
            for (int j = 0; j < MainMenuController.mainMenuController.getPuzzle().getPieces().get(i).getCorners().size() - 1; j++) {
                int pointX = (int) Math.floor(MainMenuController.mainMenuController.getPuzzle().getPieces().get(i).getCorners().get(j).getX() * puzzleScale);
                int pointY = (int) Math.floor(MainMenuController.mainMenuController.getPuzzle().getPieces().get(i).getCorners().get(j).getY() * puzzleScale);
                pointCorners.add(new Point(pointX, pointY));
            }
        }

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            testColors.add(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        }
        int color = 1;
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().size(); i++) {
            makePuzzlePiece(i, color);
            i++;
            color++;
        }
    }

    private void makePuzzlePiece(int pieceNumber, int color) {
        Rectangle newPuzzlePiece = new Rectangle(puzzleBoardWidth, puzzleBoardHeight);
        // ImagePattern picturePattern = new ImagePattern(puzzlePicture);
        newPuzzlePiece.setFill(testColors.get(color));
        Path puzzleShape = new Path();
        puzzleShape.getElements().add(new MoveTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + 100));
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().size(); i++) {
            puzzleShape.getElements().add(new LineTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(i).getX() * 100 + 100,
                    MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(i).getY() * 100 + 100));
        }

        puzzleShape.setFill(Color.BLACK);
        newPuzzlePiece.setClip(puzzleShape);

        newPuzzlePiece.setCursor(Cursor.HAND);
        newPuzzlePiece.setOnMousePressed(event -> {
            originalX = event.getSceneX();
            originalY = event.getSceneY();

        });

        newPuzzlePiece.setOnMouseDragged(event -> {
            double offsetX = event.getSceneX() - originalX;
            double offsetY = event.getSceneY() - originalY;
            Rectangle piece = (Rectangle) (event.getSource());
            piece.setX(piece.getX() + offsetX);
            piece.setY(piece.getY() + offsetY);
            originalX = event.getSceneX();
            originalY = event.getSceneY();
            puzzleShape.setTranslateX(piece.getX() + offsetX);
            puzzleShape.setTranslateY(piece.getY() + offsetY);

             // Debug piece position
             // System.out.println("Piece X: " + piece.getX());
             // System.out.println("Piece Y: " + piece.getY());
        });

        newPuzzlePiece.setStroke(Color.BLACK);
        puzzleCanvas.getChildren().add(new Group(newPuzzlePiece));
    }

}
