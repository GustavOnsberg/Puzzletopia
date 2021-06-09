package ui;

import com.sun.javafx.css.Stylesheet;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Affine;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class PuzzleScreenController {
    public AnchorPane puzzleCanvas;
    public double originalPieceX, originalPieceY, originalMouseX, originalMouseY, mouseX, mouseY;
    public AnchorPane windowPane;
    float angleChange = 0;
    public Image puzzlePicture = new Image("ui/test.png");
    private final float puzzleScale = 300;
    Random random = new Random();
    ArrayList<Rectangle> puzzlePieces = new ArrayList<>();
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
        for (int i = 0; i < 100; i++) {
            testColors.add(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        }
        int color = 0;
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().size(); i++) {
            makePuzzlePiece(i, color);
            color++;
        }
        puzzleCanvas.getChildren().addAll(puzzlePieces);
    }

    private void setupPuzzleCanvas() {
        puzzleCanvas.setPrefWidth(Main.pStage.getWidth());
        puzzleCanvas.setPrefHeight(Main.pStage.getHeight() - 300);
        puzzleCanvas.getStylesheets().add(main_stylesheet.getUrl());
        puzzleCanvas.setId("puzzle_canvas");
        HBox bottemBtns = new HBox();
        bottemBtns.setPrefHeight(99);
        bottemBtns.setPrefWidth(Main.pStage.getWidth());
        bottemBtns.setStyle("-fx-background-color: #ff9900;");
        bottemBtns.toFront();
        AnchorPane.setBottomAnchor(bottemBtns, 0.0);
        windowPane.getChildren().add(bottemBtns);

    }

    private void makePuzzlePiece(int pieceNumber, int color) {
        Rectangle newPuzzlePiece = new Rectangle(10000, 10000);
//        ImagePattern picturePattern = new ImagePattern(puzzlePicture);
//        newPuzzlePiece.setFill(picturePattern);
        newPuzzlePiece.setFill(testColors.get(color));
        Path puzzleShape = new Path();
        puzzleShape.getElements().add(new MoveTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + puzzleScale,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + puzzleScale));
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().size(); i++) {
            puzzleShape.getElements().add(new LineTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(i).getX() * 100 + puzzleScale,
                    MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(i).getY() * 100 + puzzleScale));
        }
        puzzleShape.setScaleX(1 + (3 - 1) * random.nextDouble());
        puzzleShape.setScaleY(puzzleShape.getScaleX());
        Random random = new Random();
        newPuzzlePiece.setX(random.nextInt((int) (Main.pStage.getWidth() - 165) - (-35) + 1) + (-35));
        newPuzzlePiece.setY(random.nextInt((int) (Main.pStage.getHeight() - 270) - (-20) + 1) + (-20));
        puzzleShape.setTranslateX(newPuzzlePiece.getX());
        puzzleShape.setTranslateY(newPuzzlePiece.getY());

//        debugCorners(pieceNumber, newPuzzlePiece);

        puzzleShape.setFill(Color.BLACK);
        newPuzzlePiece.setClip(puzzleShape);

        movementOfPieces(newPuzzlePiece, puzzleShape);
        rescalePieces(puzzleShape, newPuzzlePiece);

        newPuzzlePiece.setStroke(Color.BLACK);
        puzzlePieces.add(newPuzzlePiece);
    }

    private void movementOfPieces(Rectangle newPuzzlePiece, Path puzzleShape) {
        newPuzzlePiece.setCursor(Cursor.HAND);
        newPuzzlePiece.setOnMousePressed(event -> {
            newPuzzlePiece.toFront();
            if (event.isPrimaryButtonDown()) {
                originalPieceX = event.getSceneX();
                originalPieceY = event.getSceneY();
            } else if (event.isSecondaryButtonDown()) {
                originalMouseX = MouseInfo.getPointerInfo().getLocation().x;
                originalMouseY = MouseInfo.getPointerInfo().getLocation().y;
            }
        });

        newPuzzlePiece.setOnMouseDragged(event -> {
            if (event.isPrimaryButtonDown()) {
                double offsetX = event.getSceneX() - originalPieceX;
                double offsetY = event.getSceneY() - originalPieceY;
                Rectangle piece = (Rectangle) (event.getSource());
                piece.setX(piece.getX() + offsetX);
                piece.setY(piece.getY() + offsetY);
                originalPieceX = event.getSceneX();
                originalPieceY = event.getSceneY();
                if (piece.getX() + offsetX < -300 + puzzleShape.getScaleX() * 60) {
                    piece.setX(-300 + puzzleShape.getScaleX() * 60);
                    puzzleShape.setTranslateX(piece.getX());
                } else if (piece.getX() + offsetX > windowPane.getWidth() - 300 - puzzleShape.getScaleX() * 60) {
                    piece.setX(windowPane.getWidth() - 300 - puzzleShape.getScaleX() * 60);
                    puzzleShape.setTranslateX(piece.getX());
                } else if (piece.getY() + offsetY > puzzleCanvas.getHeight() - 300 - puzzleShape.getScaleY() * 60) {
                    piece.setY(puzzleCanvas.getHeight() - 300 - puzzleShape.getScaleY() * 60);
                    puzzleShape.setTranslateY(piece.getY());
                } else if (piece.getY() + offsetY < -300 + puzzleShape.getScaleY() * 60) {
                    piece.setY(-300 + puzzleShape.getScaleY() * 60);
                    puzzleShape.setTranslateY(piece.getY());
                }

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
                        puzzleShape.getTransforms().add(Affine.rotate(ramma * 180 / 3.14,puzzleScale,puzzleScale));
                    }
                    else{
                        puzzleShape.getTransforms().add(Affine.rotate(-ramma * 180 / 3.14,puzzleScale,puzzleScale));
                    }


                    prevDeltaX = deltaX;
                    prevDeltaY = deltaY;

                }
                mouseX = MouseInfo.getPointerInfo().getLocation().x;
                mouseY = MouseInfo.getPointerInfo().getLocation().y;
            }
        });
    }

    private void rescalePieces(Path puzzleShape, Rectangle newPuzzlePiece) {
        Main.pStage.widthProperty().addListener(((observable, oldValue, newValue) -> {
            if (puzzleShape.getTranslateX() > newValue.intValue() - 180) {
                newPuzzlePiece.setX(newValue.intValue() - 180);
                puzzleShape.setTranslateX(newPuzzlePiece.getX());
            }
        }));
        Main.pStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (puzzleShape.getTranslateY() > newValue.intValue() - 310) {
                newPuzzlePiece.setY(newValue.intValue() - 310);
                puzzleShape.setTranslateY(newPuzzlePiece.getY());
            }
        });
    }

    private void debugCorners(int pieceNumber, Rectangle newPuzzlePiece) {
        // First corner
        Line debug1 = new Line(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + 100,MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + 100);
        debug1.setStrokeWidth(10);
        debug1.setTranslateX(newPuzzlePiece.getX());
        debug1.setTranslateY(newPuzzlePiece.getY());
        debug1.setStroke(Color.BLUE);
        // Second corner
        Line debug2 = new Line(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getY() * 100 + 100,MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getY() * 100 + 100);
        debug2.setStrokeWidth(10);
        debug2.setTranslateX(newPuzzlePiece.getX());
        debug2.setTranslateY(newPuzzlePiece.getY());
        debug2.setStroke(Color.GREEN);
        puzzleCanvas.getChildren().addAll(debug1, debug2);
    }

}
