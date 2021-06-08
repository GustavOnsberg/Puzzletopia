package ui;

import com.sun.javafx.css.Stylesheet;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PuzzleScreenController {
    public AnchorPane puzzleCanvas;
    public double originalPieceX, originalPieceY, originalMouseX, originalMouseY, mouseX, mouseY;
    public int puzzleBoardWidth = 2000;
    public int puzzleBoardHeight = 2000;
    float angleChange = 0;
    public Image puzzlePicture = new Image("ui/test.png");
    private float puzzleScale = 100;
    ArrayList<Color> testColors = new ArrayList<>();

    double deltaX = 0;
    double deltaY = 0;
    double prevDeltaX = 0;
    double prevDeltaY = 0;

    private final Stylesheet main_stylesheet = new Stylesheet("/ui/main_stylesheet.css");


    public PuzzleScreenController() {
    }

    @FXML
    public void initialize() {
        setupPuzzleCanvas();

        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            testColors.add(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        }
        int color = 1;
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().size(); i++) {
            makePuzzlePiece(i, color);
            color++;
        }
    }

    private void setupPuzzleCanvas() {
        puzzleCanvas.setPrefWidth(Main.pStage.getWidth());
        puzzleCanvas.setPrefHeight(Main.pStage.getHeight() - 300);
        puzzleCanvas.getStylesheets().add(main_stylesheet.getUrl());
        puzzleCanvas.setId("puzzle_canvas");
    }

    private void makePuzzlePiece(int pieceNumber, int color) {
        Rectangle newPuzzlePiece = new Rectangle(puzzleBoardWidth, puzzleBoardHeight);
        // ImagePattern picturePattern = new ImagePattern(puzzlePicture);
        newPuzzlePiece.setFill(testColors.get(color));
        Path puzzleShape = new Path();
        puzzleShape.getElements().add(new MoveTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + 100));

        // Debug corner
//        Line debug = new Line(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + 100,
//                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + 100,MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getX() * 100 + 100,
//                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getY() * 100 + 100);
//        debug.setStrokeWidth(10);
//        debug.setStroke(Color.RED);
//        puzzleCanvas.getChildren().add(debug);

        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().size(); i++) {
            puzzleShape.getElements().add(new LineTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(i).getX() * 100 + 100,
                    MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(i).getY() * 100 + 100));
        }

        puzzleShape.setFill(Color.BLACK);
        newPuzzlePiece.setClip(puzzleShape);

        newPuzzlePiece.setCursor(Cursor.HAND);
        newPuzzlePiece.setOnMousePressed(event -> {
            if (event.isPrimaryButtonDown()) {
                originalPieceX = event.getSceneX();
                originalPieceY = event.getSceneY();
            } else if (event.isSecondaryButtonDown()) {
                originalMouseX = MouseInfo.getPointerInfo().getLocation().x;
                originalMouseY = MouseInfo.getPointerInfo().getLocation().y;
            }
        });
        Rotate rotate = new Rotate();

        newPuzzlePiece.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double offsetX = event.getSceneX() - originalPieceX;
                double offsetY = event.getSceneY() - originalPieceY;
                Rectangle piece = (Rectangle) (event.getSource());
                piece.setX(piece.getX() + offsetX);
                piece.setY(piece.getY() + offsetY);
                originalPieceX = event.getSceneX();
                originalPieceY = event.getSceneY();
                puzzleShape.setTranslateX(piece.getX() + offsetX);
                puzzleShape.setTranslateY(piece.getY() + offsetY);

                // Debug piece position
                // System.out.println("Piece X: " + piece.getX());
                // System.out.println("Piece Y: " + piece.getY());

            } else if (event.isSecondaryButtonDown()) {
                if (((Math.pow(MouseInfo.getPointerInfo().getLocation().x - originalMouseX,2) + Math.pow((MouseInfo.getPointerInfo().getLocation().y - originalMouseY),2)) > 2500)) {
                    double a = Math.sqrt(Math.pow(MouseInfo.getPointerInfo().getLocation().x - originalMouseX,2) + Math.pow(MouseInfo.getPointerInfo().getLocation().y - originalMouseY,2));
                    double b = Math.sqrt(Math.pow(mouseX - originalMouseX,2) + Math.pow(mouseY - originalMouseY, 2));
                    double c = Math.sqrt(Math.pow(MouseInfo.getPointerInfo().getLocation().x - mouseX,2) + Math.pow(MouseInfo.getPointerInfo().getLocation().y - mouseY, 2));
                    double ramma = Math.acos((Math.pow(a,2) + Math.pow(b,2) - Math.pow(c,2))/(2 * a * b));



                    double relativeX = MouseInfo.getPointerInfo().getLocation().x - originalMouseX;
                    double relativeY = MouseInfo.getPointerInfo().getLocation().y - originalMouseY;
                    double deltaLength = Math.sqrt(Math.pow(relativeX, 2) + Math.pow(relativeY, 2));
                    relativeX/=deltaLength;
                    relativeY/=deltaLength;

                    deltaX = MouseInfo.getPointerInfo().getLocation().x - originalMouseX;
                    deltaY = relativeY = MouseInfo.getPointerInfo().getLocation().y - originalMouseY;
                    deltaLength = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                    deltaX/=deltaLength;
                    deltaY/=deltaLength;

                    if ((deltaX > 0 && deltaY > prevDeltaY) || (deltaX < 0 && deltaY < prevDeltaY)){
                        puzzleShape.getTransforms().add(Affine.rotate(ramma * 180 / 3.14,100,100));
                    }
                    else{
                        puzzleShape.getTransforms().add(Affine.rotate(-ramma * 180 / 3.14,100,100));
                    }


                    prevDeltaX = deltaX;
                    prevDeltaY = deltaY;

                }
                mouseX = MouseInfo.getPointerInfo().getLocation().x;
                mouseY = MouseInfo.getPointerInfo().getLocation().y;
            }
        });


        newPuzzlePiece.setStroke(Color.BLACK);
        puzzleCanvas.getChildren().add(new Group(newPuzzlePiece));
    }

}
