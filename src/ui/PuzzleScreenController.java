package ui;

import com.sun.javafx.css.Stylesheet;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.*;
import javafx.scene.transform.Affine;
import main.Piece;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PuzzleScreenController {
    public AnchorPane puzzleCanvas;
    public double originalPieceX, originalPieceY, originalMouseX, originalMouseY, mouseX, mouseY;
    public AnchorPane windowPane;
    private final float puzzleScale = 300;
    Random random = new Random();
    ArrayList<Rectangle> puzzlePieces = new ArrayList<>();
    ArrayList<Path> puzzleShapesList = new ArrayList<>();
    ArrayList<Color> pieceColors = new ArrayList<>();
    HBox bottomBtns = new HBox();
    Button shuffleBtn = new Button("SHUFFLE");
    Button solveBtn = new Button("SOLVE");
    Button mainMenuBtn = new Button("MAIN MENU");
    boolean isComp = false;
    double ramma;

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
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().n * MainMenuController.mainMenuController.getPuzzle().m; i++) {
            pieceColors.add(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        }
        int color = 0;
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().size(); i++) {
            makePuzzlePiece(i, color);
            color++;
        }
        setupShuffle();
        puzzleCanvas.getChildren().addAll(puzzlePieces);
    }

    private void setupShuffle() {
        shuffleBtn.setOnAction(event -> {
            for (int i = 0; i < puzzlePieces.size(); i++) {
                puzzlePieces.get(i).setX(random.nextInt((int) (Main.pStage.getWidth() - 600)));
                puzzlePieces.get(i).setY(random.nextInt((int) (Main.pStage.getHeight() - 600)));
                puzzleShapesList.get(i).setTranslateX(puzzlePieces.get(i).getX());
                puzzleShapesList.get(i).setTranslateY(puzzlePieces.get(i).getY());
                puzzleShapesList.get(i).getTransforms().add(Affine.rotate(random.nextInt((180 - (-180) + (-180))), puzzleScale, puzzleScale));
                puzzleShapesList.get(i).setRotate(random.nextInt((360 - (-360) + (-360))));
            }
        });
    }

    private void setupPuzzleCanvas() {
        puzzleCanvas.setPrefWidth(Main.pStage.getWidth());
        puzzleCanvas.setPrefHeight(Main.pStage.getHeight() - 300);
        puzzleCanvas.getStylesheets().add(main_stylesheet.getUrl());
        puzzleCanvas.setId("puzzle_canvas");

        // Setup bottom HBox for buttons
        bottomBtns.setPrefHeight(99);
        bottomBtns.setPrefWidth(Main.pStage.getWidth());
        bottomBtns.setStyle("-fx-background-color: #ff9900;");
        bottomBtns.toFront();
        AnchorPane.setBottomAnchor(bottomBtns, 0.0);
        bottomBtns.setPadding(new Insets(22, 30, 22, 10));
        bottomBtns.setSpacing(20);

        // Setup spacers
        Pane spacer1 = new Pane();
        spacer1.setMinWidth(bottomBtns.getPrefWidth() / 15);
        Pane spacer2 = new Pane();
        spacer2.setMinWidth(bottomBtns.getPrefWidth() / 15);

        // Setup exit button
        mainMenuBtn.setPrefHeight(50);
        mainMenuBtn.setPrefWidth(bottomBtns.getPrefWidth() / 3 - 150);
        mainMenuBtn.getStylesheets().add(main_stylesheet.getUrl());
        mainMenuBtn.setOnAction(event -> {
            try {
                Main.main.setStage("MainMenu.fxml", Main.pStage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // Setup shuffle button
        shuffleBtn.setPrefHeight(50);
        shuffleBtn.setPrefWidth(bottomBtns.getPrefWidth() / 3 - 150);
        shuffleBtn.getStylesheets().add(main_stylesheet.getUrl());

        // Setup solve button
        solveBtn.setPrefHeight(50);
        solveBtn.setPrefWidth(bottomBtns.getPrefWidth() / 3 - 150);
        AnchorPane.setRightAnchor(solveBtn, 0.0);
        solveBtn.getStylesheets().add(main_stylesheet.getUrl());
        solveBtn.setOnAction(event -> {
            solvePuzzle();
            solvePuzzle();
        });
        windowPane.getChildren().add(bottomBtns);
        bottomBtns.getChildren().addAll(mainMenuBtn, spacer1, shuffleBtn, spacer2, solveBtn);
    }

    private void solvePuzzle() {
        int n = MainMenuController.mainMenuController.getPuzzle().n;
        int m = MainMenuController.mainMenuController.getPuzzle().m;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == 0 && j == 0) {
                    double angle = Math.atan2(puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getLocalToSceneTransform().getMyx(), puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getLocalToSceneTransform().getMyy());
                    Rectangle selRect = puzzlePieces.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index);
                    Piece selPiece = MainMenuController.mainMenuController.getPuzzle().getPieces().get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index);
                    double pointCornerOneX = selPiece.getCorners().get(selPiece.getCornerIndexes()[1]).x;
                    double pointCornerOneY = selPiece.getCorners().get(selPiece.getCornerIndexes()[1]).y;
                    double pointCornerTwoX = selPiece.getCorners().get(selPiece.getCornerIndexes()[2]).x;
                    double pointCornerTwoY = selPiece.getCorners().get(selPiece.getCornerIndexes()[2]).y;
                    double pointLength = Math.sqrt(Math.pow(pointCornerOneX - pointCornerTwoX, 2) + Math.pow(pointCornerOneY - pointCornerTwoY, 2));
                    double pointAngle = (Math.acos((pointCornerTwoX - pointCornerOneX) / pointLength) * Math.copySign(1, pointCornerTwoY - pointCornerOneY) - angle);
                    puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getTransforms().add(Affine.rotate((pointAngle * 180) / 3.14, puzzleScale, puzzleScale));
                    angle = Math.atan2(puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getLocalToSceneTransform().getMyx(), puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getLocalToSceneTransform().getMyy());
                    double xOne = (selPiece.getCorners().get(1).getX() * Math.cos(angle) - selPiece.getCorners().get(1).getY() * Math.sin(angle) * puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getScaleX() * 100 - 300);
                    double xTwo = (selPiece.getCorners().get(2).getX() * Math.cos(angle) - selPiece.getCorners().get(2).getY() * Math.sin(angle) * puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getScaleX() * 100 - 300);
                    double yOne = (selPiece.getCorners().get(1).getY() * Math.cos(angle) - selPiece.getCorners().get(1).getX() * Math.sin(angle) * puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getScaleX() * 100 - 300);
                    double yTwo = (selPiece.getCorners().get(2).getY() * Math.cos(angle) - selPiece.getCorners().get(2).getX() * Math.sin(angle) * puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getScaleX() * 100 - 300);
                    double offsetX = -300 + puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getScaleX() * 60 ;
                    double offsetY = -300 + puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).getScaleX() * 60;
                    selRect.setX(offsetX);
                    selRect.setY(offsetY);
                    puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).setTranslateX(selRect.getX());
                    puzzleShapesList.get(MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(0).index).setTranslateY(selRect.getY());
                } else {
                    int indexSnapSnap = j + i * n;
                    if (j == 0) {
                        int indexPiece = MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(indexSnapSnap).index;
                        int indexSnapSnapOther = indexSnapSnap - n;
                        int indexPieceOther = MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(indexSnapSnapOther).index;
                        Rectangle pieceRect = puzzlePieces.get(indexPiece);
                        int edgeOne = MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(indexSnapSnap).edgeUp;
                        int edgeTwo = (MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(indexSnapSnapOther).edgeUp + 2) % 4;
                        Piece piece = MainMenuController.mainMenuController.getPuzzle().getPieces().get(indexPiece);
                        Piece otherPiece = MainMenuController.mainMenuController.getPuzzle().getPieces().get(indexPieceOther);
                        int cornerOneIndex = MainMenuController.mainMenuController.getPuzzle().getPieces().get(indexPiece).getCornerIndexes()[edgeOne];
                        int cornerTwoIndex = MainMenuController.mainMenuController.getPuzzle().getPieces().get(indexPieceOther).getCornerIndexes()[(edgeTwo + 1) % 4];
                        snapTo(puzzleShapesList.get(indexPiece), pieceRect, indexPieceOther, puzzlePieces.get(indexPieceOther), cornerOneIndex, cornerTwoIndex, piece, otherPiece, edgeOne, edgeTwo);
                    } else {
                        int indexPiece = MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(indexSnapSnap).index;
                        int indexSnapSnapOther = indexSnapSnap - 1;
                        int indexPieceOther = MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(indexSnapSnapOther).index;
                        Rectangle pieceRect = puzzlePieces.get(indexPiece);
                        int edgeOne = (MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(indexSnapSnap).edgeUp + 3) % 4;
                        int edgeTwo = (MainMenuController.mainMenuController.getPuzzle().placedPiecesFinal.get(indexSnapSnapOther).edgeUp + 1) % 4;
                        Piece piece = MainMenuController.mainMenuController.getPuzzle().getPieces().get(indexPiece);
                        Piece otherPiece = MainMenuController.mainMenuController.getPuzzle().getPieces().get(indexPieceOther);
                        int cornerOneIndex = MainMenuController.mainMenuController.getPuzzle().getPieces().get(indexPiece).getCornerIndexes()[edgeOne];
                        int cornerTwoIndex = MainMenuController.mainMenuController.getPuzzle().getPieces().get(indexPieceOther).getCornerIndexes()[(edgeTwo + 1) % 4];
                        snapTo(puzzleShapesList.get(indexPiece), pieceRect, indexPieceOther, puzzlePieces.get(indexPieceOther), cornerOneIndex, cornerTwoIndex, piece, otherPiece, edgeOne, edgeTwo);
                    }
                }
            }
        }
    }

    private void makePuzzlePiece(int pieceNumber, int color) {
        Rectangle newPuzzlePiece = new Rectangle(10000, 10000);
        newPuzzlePiece.setFill(pieceColors.get(color));
        Path puzzleShape = new Path();
        puzzleShape.getElements().add(new MoveTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + puzzleScale,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + puzzleScale));
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().size(); i++) {
            puzzleShape.getElements().add(new LineTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(i).getX() * 100 + puzzleScale,
                    MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(i).getY() * 100 + puzzleScale));
        }
        puzzleShape.setScaleX(Math.min(Main.pStage.getWidth() / MainMenuController.mainMenuController.getPuzzle().puzzleWidth, Main.pStage.getHeight() / MainMenuController.mainMenuController.getPuzzle().puzzleHeight) / 150);
        puzzleShape.setScaleY(puzzleShape.getScaleX());
        newPuzzlePiece.setX(random.nextInt((int) (Main.pStage.getWidth() - 600)));
        newPuzzlePiece.setY(random.nextInt((int) (Main.pStage.getHeight() - 600)));
        puzzleShape.setTranslateX(newPuzzlePiece.getX());
        puzzleShape.setTranslateY(newPuzzlePiece.getY());
        puzzleShape.getTransforms().add(Affine.rotate(random.nextInt((180 - (-180) + (-180))), puzzleScale, puzzleScale));
        puzzleShape.setRotate(random.nextInt((360 - (-360) + (-360))));

//        debugCorners(pieceNumber, newPuzzlePiece);

        puzzleShape.setFill(Color.BLACK);
        newPuzzlePiece.setClip(puzzleShape);
        movementOfPieces(newPuzzlePiece, puzzleShape);
        rescaleCanvas(puzzleShape, newPuzzlePiece);
        checkSnapTo(pieceNumber, newPuzzlePiece, puzzleShape);


        newPuzzlePiece.setStroke(Color.BLACK);
        puzzlePieces.add(newPuzzlePiece);
        puzzleShapesList.add(puzzleShape);
    }

    private void checkSnapTo(int pieceNumber, Rectangle newPuzzlePiece, Path puzzleShape) {
        newPuzzlePiece.setOnMouseReleased(event -> {
            int numCheckPiece = 0;
            for (Rectangle puzzlePiece : puzzlePieces) {
                if (newPuzzlePiece.getX() > puzzlePiece.getX() - 125 * puzzleShape.getScaleX() && newPuzzlePiece.getX() < puzzlePiece.getX() + 125 * puzzleShape.getScaleX() &&
                        newPuzzlePiece.getY() > puzzlePiece.getY() - 125 * puzzleShape.getScaleY() && newPuzzlePiece.getY() < puzzlePiece.getY() + 125 * puzzleShape.getScaleY()) {
                    if (newPuzzlePiece != puzzlePiece) {
                        int[] cornersSelected = MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCornerIndexes();
                        int[] cornersCheck = MainMenuController.mainMenuController.getPuzzle().getPieces().get(numCheckPiece).getCornerIndexes();
                        Piece pieceSelected = MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber);
                        Piece pieceChecked = MainMenuController.mainMenuController.getPuzzle().getPieces().get(numCheckPiece);
                        for (int i = 0; i < cornersSelected.length; i++) {
                            for (int j = 0; j < cornersCheck.length; j++) {
                                if (checkCorners(pieceSelected, pieceChecked, newPuzzlePiece, puzzlePiece, cornersSelected, cornersCheck, puzzleShape, i, j, numCheckPiece)) {
                                    // Debug matches
//                                    System.out.println("Courner match - moved: "+i+"   other: "+j);

                                    int nextSelCorner = checkArray(cornersSelected, i + 1);
                                    int prevSelCorner = checkArray(cornersSelected, i - 1);
                                    int nextCheckCorner = checkArray(cornersCheck, j + 1);
                                    int prevCheckCorner = checkArray(cornersCheck, j - 1);
                                    int edgeOne = -1;
                                    int edgeTwo = -1;
                                    if (checkCorners(pieceSelected, pieceChecked, newPuzzlePiece, puzzlePiece, cornersSelected, cornersCheck, puzzleShape, nextSelCorner, prevCheckCorner, numCheckPiece)) {
                                        edgeOne = i;
                                        edgeTwo = prevCheckCorner;
                                        isComp = true;
                                    } else if (checkCorners(pieceSelected, pieceChecked, newPuzzlePiece, puzzlePiece, cornersSelected, cornersCheck, puzzleShape, prevSelCorner, nextCheckCorner, numCheckPiece)) {
                                        edgeOne = prevSelCorner;
                                        edgeTwo = j;
                                        isComp = true;
                                    } else {
                                        isComp = false;
                                    }
                                    if (isComp) {
                                        if (MainMenuController.mainMenuController.getPuzzle().matchEdge(pieceSelected, pieceChecked, edgeOne, edgeTwo)) {
                                            snapTo(puzzleShape, (Rectangle) event.getSource(), numCheckPiece, puzzlePiece, cornersSelected[i], cornersCheck[j], pieceSelected, pieceChecked, edgeOne, edgeTwo);
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
                numCheckPiece++;
            }

        });
    }

    private void snapTo(Path puzzleShape, Rectangle piece, int numCheckPiece, Rectangle puzzlePiece, int index, int index1, Piece pieceSelected, Piece pieceChecked, int edgeOne, int edgeTwo) {
        double angleSelected = Math.atan2(puzzleShape.getLocalToSceneTransform().getMyx(), puzzleShape.getLocalToSceneTransform().getMyy());
        double angleChecked = Math.atan2(puzzleShapesList.get(numCheckPiece).getLocalToSceneTransform().getMyx(), puzzleShapesList.get(numCheckPiece).getLocalToSceneTransform().getMyy());
        float p1c1x = pieceSelected.getCorners().get(pieceSelected.getCornerIndexes()[edgeOne]).x;
        float p1c1y = pieceSelected.getCorners().get(pieceSelected.getCornerIndexes()[edgeOne]).y;
        float p1c2x = pieceSelected.getCorners().get(pieceSelected.getCornerIndexes()[(edgeOne + 1) % 4]).x;
        float p1c2y = pieceSelected.getCorners().get(pieceSelected.getCornerIndexes()[(edgeOne + 1) % 4]).y;
        float p1eLength = (float) Math.sqrt(Math.pow(p1c1x - p1c2x, 2) + Math.pow(p1c1y - p1c2y, 2));
        float p1eAngle = (float) (Math.acos((p1c2x - p1c1x) / p1eLength) * Math.copySign(1, p1c2y - p1c1y) + angleSelected);

        float p2c1x = pieceChecked.getCorners().get(pieceChecked.getCornerIndexes()[edgeTwo]).x;
        float p2c1y = pieceChecked.getCorners().get(pieceChecked.getCornerIndexes()[edgeTwo]).y;
        float p2c2x = pieceChecked.getCorners().get(pieceChecked.getCornerIndexes()[(edgeTwo + 1) % 4]).x;
        float p2c2y = pieceChecked.getCorners().get(pieceChecked.getCornerIndexes()[(edgeTwo + 1) % 4]).y;
        float p2eLength = (float) Math.sqrt(Math.pow(p2c1x - p2c2x, 2) + Math.pow(p2c1y - p2c2y, 2));
        float p2eAngle = (float) (Math.acos((p2c1x - p2c2x) / p2eLength) * Math.copySign(1, p2c1y - p2c2y) + angleChecked);
        puzzleShape.getTransforms().add(Affine.rotate(((p2eAngle - p1eAngle) * 180) / 3.14, puzzleScale, puzzleScale));
        angleSelected = Math.atan2(puzzleShape.getLocalToSceneTransform().getMyx(), puzzleShape.getLocalToSceneTransform().getMyy());
        angleChecked = Math.atan2(puzzleShapesList.get(numCheckPiece).getLocalToSceneTransform().getMyx(), puzzleShapesList.get(numCheckPiece).getLocalToSceneTransform().getMyy());

        double x1 = (pieceSelected.getCorners().get(index).getX() * Math.cos(angleSelected) - pieceSelected.getCorners().get(index).getY() * Math.sin(angleSelected)) * puzzleShape.getScaleX() * 100;
        double x2 = (pieceChecked.getCorners().get(index1).getX() * Math.cos(angleChecked) - pieceChecked.getCorners().get(index1).getY() * Math.sin(angleChecked)) * puzzleShape.getScaleX() * 100;
        double offsetX = x1 - x2;
        double y1 = (pieceSelected.getCorners().get(index).getY() * Math.cos(angleSelected) + pieceSelected.getCorners().get(index).getX() * Math.sin(angleSelected)) * puzzleShape.getScaleX() * 100;
        double y2 = (pieceChecked.getCorners().get(index1).getY() * Math.cos(angleChecked) + pieceChecked.getCorners().get(index1).getX() * Math.sin(angleChecked)) * puzzleShape.getScaleX() * 100;
        double offsetY = y1 - y2;

        piece.setX(puzzlePiece.getX() - offsetX);
        piece.setY(puzzlePiece.getY() - offsetY);
        puzzleShape.setTranslateX(piece.getX());
        puzzleShape.setTranslateY(piece.getY());
    }

    private boolean checkCorners(Piece pieceSelectedCorner, Piece pieceCheckedCorner, Rectangle newPuzzlePiece, Rectangle puzzlePiece, int[] cornersSelected, int[] cornersCheck, Path puzzleShape, int i, int j, int numCheckPiece) {
        int tol = 20;
        double scale = puzzleShape.getScaleX() * 100;
        double angleSelected = Math.atan2(puzzleShape.getLocalToSceneTransform().getMyx(), puzzleShape.getLocalToSceneTransform().getMyy());
        double angleChecked = Math.atan2(puzzleShapesList.get(numCheckPiece).getLocalToSceneTransform().getMyx(), puzzleShapesList.get(numCheckPiece).getLocalToSceneTransform().getMyy());
        double pieceSelectedCornerY = pieceSelectedCorner.getCorners().get(cornersSelected[i]).getX() * Math.sin(angleSelected) + pieceSelectedCorner.getCorners().get(cornersSelected[i]).getY() * Math.cos(angleSelected);
        double pieceSelectedCornerX = pieceSelectedCorner.getCorners().get(cornersSelected[i]).getX() * Math.cos(angleSelected) - pieceSelectedCorner.getCorners().get(cornersSelected[i]).getY() * Math.sin(angleSelected);
        double pieceCheckedCornerY = pieceCheckedCorner.getCorners().get(cornersCheck[j]).getX() * Math.sin(angleChecked) + pieceCheckedCorner.getCorners().get(cornersCheck[j]).getY() * Math.cos(angleChecked);
        double pieceCheckedCornerX = pieceCheckedCorner.getCorners().get(cornersCheck[j]).getX() * Math.cos(angleChecked) - pieceCheckedCorner.getCorners().get(cornersCheck[j]).getY() * Math.sin(angleChecked);

        boolean enableDebug = false;
        if (enableDebug) {
            System.out.println("\nTotal Moved x: " + (newPuzzlePiece.getX() + pieceSelectedCornerX * scale) + ", Other x: " + (puzzlePiece.getX() + pieceCheckedCornerX * scale));
            System.out.println("Total Moved y: " + (newPuzzlePiece.getY() + pieceSelectedCornerY * scale) + ", Other y: " + (puzzlePiece.getY() + pieceCheckedCornerY * scale));
            System.out.println("Relative Moved x: " + (pieceSelectedCornerX * scale) + ", Other x: " + (pieceCheckedCornerX * scale));
            System.out.println("Relative Moved y: " + (pieceSelectedCornerY * scale) + ", Other y: " + (pieceCheckedCornerY * scale));
            System.out.println("Piece Moved x: " + (newPuzzlePiece.getX()) + ", Other x: " + (puzzlePiece.getX()));
            System.out.println("Piece Moved y: " + (newPuzzlePiece.getY()) + ", Other y: " + (puzzlePiece.getY()));
            System.out.println("Angle moved: " + angleSelected + ", other: " + angleChecked);
            System.out.println("Corner checking function - moved: " + i + "   other: " + j);
        }

        return newPuzzlePiece.getX() + pieceSelectedCornerX * scale > puzzlePiece.getX() + pieceCheckedCornerX * scale - tol &&
                newPuzzlePiece.getX() + pieceSelectedCornerX * scale < puzzlePiece.getX() + pieceCheckedCornerX * scale + tol &&
                newPuzzlePiece.getY() + pieceSelectedCornerY * scale > puzzlePiece.getY() + pieceCheckedCornerY * scale - tol &&
                newPuzzlePiece.getY() + pieceSelectedCornerY * scale < puzzlePiece.getY() + pieceCheckedCornerY * scale + tol;
    }


    private int checkArray(int[] array, int i) {
        if (i > array.length - 1) {
            return 0;
        } else if (i < 0) {
            return array.length - 1;
        } else {
            return i;
        }
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
                if (((Math.pow(MouseInfo.getPointerInfo().getLocation().x - originalMouseX, 2) + Math.pow((MouseInfo.getPointerInfo().getLocation().y - originalMouseY), 2)) > 2500)) {
                    double a = Math.sqrt(Math.pow(MouseInfo.getPointerInfo().getLocation().x - originalMouseX, 2) + Math.pow(MouseInfo.getPointerInfo().getLocation().y - originalMouseY, 2));
                    double b = Math.sqrt(Math.pow(mouseX - originalMouseX, 2) + Math.pow(mouseY - originalMouseY, 2));
                    double c = Math.sqrt(Math.pow(MouseInfo.getPointerInfo().getLocation().x - mouseX, 2) + Math.pow(MouseInfo.getPointerInfo().getLocation().y - mouseY, 2));
                    ramma = Math.acos((Math.pow(a, 2) + Math.pow(b, 2) - Math.pow(c, 2)) / (2 * a * b));


                    double relativeX = MouseInfo.getPointerInfo().getLocation().x - originalMouseX;
                    double relativeY = MouseInfo.getPointerInfo().getLocation().y - originalMouseY;
                    double deltaLength = Math.sqrt(Math.pow(relativeX, 2) + Math.pow(relativeY, 2));
                    relativeX /= deltaLength;
                    relativeY /= deltaLength;

                    deltaX = MouseInfo.getPointerInfo().getLocation().x - originalMouseX;
                    deltaY = relativeY = MouseInfo.getPointerInfo().getLocation().y - originalMouseY;
                    deltaLength = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
                    deltaX /= deltaLength;
                    deltaY /= deltaLength;

                    if ((deltaX > 0 && deltaY > prevDeltaY) || (deltaX < 0 && deltaY < prevDeltaY)) {
                        puzzleShape.getTransforms().add(Affine.rotate(ramma * 180 / 3.14, puzzleScale, puzzleScale));
                        puzzleShape.setRotate(ramma * 180 / 3.14);
                    } else {
                        puzzleShape.getTransforms().add(Affine.rotate(-ramma * 180 / 3.14, puzzleScale, puzzleScale));
                        puzzleShape.setRotate(-ramma * 180 / 3.14);
                    }
                    prevDeltaX = deltaX;
                    prevDeltaY = deltaY;

                }
                mouseX = MouseInfo.getPointerInfo().getLocation().x;
                mouseY = MouseInfo.getPointerInfo().getLocation().y;
            }
        });
    }

    private void rescaleCanvas(Path puzzleShape, Rectangle newPuzzlePiece) {
        Main.pStage.widthProperty().addListener(((observable, oldValue, newValue) -> {
            puzzleShape.setScaleX(Math.min(Main.pStage.getWidth() / MainMenuController.mainMenuController.getPuzzle().puzzleWidth, Main.pStage.getHeight() / MainMenuController.mainMenuController.getPuzzle().puzzleHeight) / 120);
            puzzleShape.setScaleY(puzzleShape.getScaleX());
            bottomBtns.setPrefWidth(Main.pStage.getWidth());
            if (puzzleShape.getTranslateX() > newValue.intValue() - puzzleShape.getScaleX() * 60 - 300) {
                newPuzzlePiece.setX(Main.pStage.getWidth() - 300 - puzzleShape.getScaleX() * 60);
                puzzleShape.setTranslateX(newPuzzlePiece.getX());
            }
        }));
        Main.pStage.heightProperty().addListener((observable, oldValue, newValue) -> {
            if (puzzleShape.getTranslateY() > newValue.intValue() - (windowPane.getHeight() - puzzleCanvas.getHeight()) - puzzleShape.getScaleX() * 60 - 330) {
                newPuzzlePiece.setY(puzzleCanvas.getHeight() - 300 - puzzleShape.getScaleY() * 60);
                puzzleShape.setTranslateY(newPuzzlePiece.getY());
            }
        });
    }

    private void debugCorners(int pieceNumber, Rectangle newPuzzlePiece) {
        // First corner
        Line debug1 = new Line(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + 100, MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(0).getY() * 100 + 100);
        debug1.setStrokeWidth(10);
        debug1.setTranslateX(newPuzzlePiece.getX());
        debug1.setTranslateY(newPuzzlePiece.getY());
        debug1.setStroke(Color.BLUE);
        // Second corner
        Line debug2 = new Line(MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getY() * 100 + 100, MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getX() * 100 + 100,
                MainMenuController.mainMenuController.getPuzzle().getPieces().get(pieceNumber).getCorners().get(1).getY() * 100 + 100);
        debug2.setStrokeWidth(10);
        debug2.setTranslateX(newPuzzlePiece.getX());
        debug2.setTranslateY(newPuzzlePiece.getY());
        debug2.setStroke(Color.GREEN);
        puzzleCanvas.getChildren().addAll(debug1, debug2);
    }

}
