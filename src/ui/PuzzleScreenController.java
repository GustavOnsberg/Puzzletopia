package ui;

import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;

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
//    ArrayList<Point> pointHorizontal = new ArrayList<Point>() {
//        {
//            add(new Point(200,35));
//            add(new Point(260,40));
//            add(new Point(230,135));
//            add(new Point(215,125));
//        }
//
//    };
//    ArrayList<Point> pointVertical = new ArrayList<Point>() {
//        {
//            add(new Point(48,60));
//            add(new Point(44,110));
//            add(new Point(124,138));
//            add(new Point(128,125));
//        }
//    };
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
//        for (int i = 0; i < 20; i++) {
//            pointCross.add(new Point(random.nextInt(1000),random.nextInt(1000)));
//            pointCross.add(new Point(random.nextInt(1000),random.nextInt(1000)));
//            pointCross.add(new Point(random.nextInt(1000),random.nextInt(1000)));
//            pointCross.add(new Point(random.nextInt(1000),random.nextInt(1000)));
//        }
        for (int i = 0; i < 100; i++) {
            testColors.add(new Color(random.nextDouble(), random.nextDouble(), random.nextDouble(), 1));
        }
        int color = 1;
        int firstPoint = 0;
        int secondPoint = 1;
        int thirdPoint = 2;
        int fourthPoint = 3;
        for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().size() - 1; i++) {
            makePuzzlePiece(1, pointCorners, firstPoint, secondPoint, thirdPoint, fourthPoint, color);
            i++;
            firstPoint += 4;
            secondPoint += 4;
            thirdPoint += 4;
            fourthPoint += 4;
            color++;
        }
    }

    private void makePuzzlePiece(int pieceNumber, ArrayList<Point> points, int firstPoint, int secondPoint, int thirdPoint, int fourthPoint, int color) {
        Rectangle newPuzzlePiece = new Rectangle(puzzleBoardWidth, puzzleBoardHeight);
        // ImagePattern picturePattern = new ImagePattern(puzzlePicture);
        newPuzzlePiece.setFill(testColors.get(color));
        Path puzzleShape = new Path();

        for (int j = 0; j < MainMenuController.mainMenuController.getPuzzle().getPieces().size() - 1; j++) {
            puzzleShape.getElements().add(new MoveTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(0).getCorners().get(0).getX() * 100 + 100,
                    MainMenuController.mainMenuController.getPuzzle().getPieces().get(0).getCorners().get(0).getY() * 100 + 100));
            for (int i = 0; i < MainMenuController.mainMenuController.getPuzzle().getPieces().get(j).getCorners().size() - 1; i++) {
                puzzleShape.getElements().add(new LineTo(MainMenuController.mainMenuController.getPuzzle().getPieces().get(j).getCorners().get(i).getX() * 100 + 100,
                        MainMenuController.mainMenuController.getPuzzle().getPieces().get(j).getCorners().get(i).getY() * 100 + 100));
            }
        }
//        Path puzzleShape = new Path(
//                new MoveTo(points.get(firstPoint).getX(), points.get(firstPoint).getY()),
//                new LineTo(points.get(secondPoint).getX(), points.get(secondPoint).getY()),
//                new LineTo(points.get(thirdPoint).getX(), points.get(thirdPoint).getY()),
//                new LineTo(points.get(fourthPoint).getX(), points.get(fourthPoint).getY()),
//                new ClosePath()
//        );
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
