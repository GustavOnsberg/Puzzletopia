package ui;

import javafx.event.EventHandler;
import javafx.fxml.FXML;

import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.*;
import javafx.scene.shape.Rectangle;

import java.awt.*;
import java.awt.Shape;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class PuzzleScreenController {
    public AnchorPane puzzleCanvas;
    public double originalX, originalY;
    public int puzzleBoardWidth = 1000;
    public int puzzleBoardHeight = 1000;
    public Image puzzlePicture = new Image("ui/test.png");
    ArrayList<Point> pointCross = new ArrayList<Point>() {
        {
            add(new Point(50,30));
            add(new Point(120,45));
            add(new Point(130,120));
            add(new Point(40,145));
        }
    };
    ArrayList<Point> pointHorizontal = new ArrayList<Point>() {
        {
            add(new Point(200,35));
            add(new Point(260,40));
            add(new Point(230,135));
            add(new Point(215,125));
        }

    };
    ArrayList<Point> pointVertical = new ArrayList<Point>() {
        {
            add(new Point(48,60));
            add(new Point(44,110));
            add(new Point(124,138));
            add(new Point(128,125));
        }
    };
    ArrayList<Color> testColors = new ArrayList<Color>() {
        {
            add(Color.BLACK);
            add(Color.OLIVE);
            add(Color.BLUE);
            add(Color.YELLOW);
            add(Color.VIOLET);

        }
    };

    public PuzzleScreenController() {
    }

    @FXML
    public void initialize() throws IOException {
        Random random = new Random();
        for (int i = 0; i < 20; i++) {
            pointCross.add(new Point(random.nextInt(1000),random.nextInt(1000)));
            pointCross.add(new Point(random.nextInt(1000),random.nextInt(1000)));
            pointCross.add(new Point(random.nextInt(1000),random.nextInt(1000)));
            pointCross.add(new Point(random.nextInt(1000),random.nextInt(1000)));
        }
        int firstPoint = 0;
        int secondPoint = 1;
        int thirdPoint = 2;
        int fourthPoint = 3;
        for (int i = 0; i < 10; i++) {
            makePuzzlePiece(pointCross,puzzlePicture, firstPoint, secondPoint, thirdPoint, fourthPoint);
            i++;
            firstPoint++;
            secondPoint++;
            thirdPoint++;
            fourthPoint++;
        }
    }

    private void makePuzzlePiece(ArrayList<Point> points, Image puzzlePicture, int firstPoint, int secondPoint, int thirdPoint, int fourthPoint) {
        Rectangle newPuzzlePiece = new Rectangle(puzzleBoardWidth, puzzleBoardHeight);
        ImagePattern picturePattern = new ImagePattern(puzzlePicture);
        newPuzzlePiece.setFill(picturePattern);
        Path puzzleShape = new Path(
                new MoveTo(points.get(firstPoint).getX(), points.get(firstPoint).getY()),
                new LineTo(points.get(secondPoint).getX(), points.get(secondPoint).getY()),
                new LineTo(points.get(thirdPoint).getX(), points.get(thirdPoint).getY()),
                new LineTo(points.get(fourthPoint).getX(), points.get(fourthPoint).getY()),
                new LineTo(points.get(firstPoint).getX(), points.get(firstPoint).getY()),
                new ClosePath()
        );
        puzzleShape.setFill(Color.BLACK);
        newPuzzlePiece.setClip(puzzleShape);
        newPuzzlePiece.setFocusTraversable(true);

        newPuzzlePiece.setCursor(Cursor.HAND);
        newPuzzlePiece.setOnMousePressed(event -> {
            originalX = event.getSceneX();
            originalY = event.getSceneY();
            Rectangle piece = (Rectangle) (event.getSource());
            piece.toFront();
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
        });

        newPuzzlePiece.setStroke(Color.BLACK);
        puzzleCanvas.getChildren().add(new Group(newPuzzlePiece));
    }

}
