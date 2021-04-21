package ui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import org.w3c.dom.css.Rect;
import javafx.scene.shape.Polygon;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class PuzzleScreenController {
    public AnchorPane puzzleCanvas;
    BufferedImage canvas =  new BufferedImage(500, 500, BufferedImage.TYPE_3BYTE_BGR);
    ArrayList<Point> pointCross = new ArrayList<Point>() {
        {
            add(new Point(50,30));
            add(new Point(120,45));
            add(new Point(40,145));
            add(new Point(130,120));
        }
    };
    ArrayList<Point> pointHorizontal = new ArrayList<Point>() {
        {
            add(new Point(70,35));
            add(new Point(100,40));
            add(new Point(70,135));
            add(new Point(100,125));
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

    public PuzzleScreenController() {

    }

    @FXML
    public void initialize() throws IOException {
        canvas = ImageIO.read(new File("../PuzzleTopia/src/ui/test.png"));
//        for (int i = 0; i < canvas.getWidth(); i++) {
//            for (int j = 0; j < canvas.getHeight(); j++) {
//                canvas.setRGB(i, j, Color.BLACK.getRGB());
//            }
//        }

        Polygon polygon = new Polygon();
        polygon.getPoints().add(pointCross.get(0).getX());
        polygon.getPoints().add(pointCross.get(0).getY());
        polygon.getPoints().add(pointCross.get(1).getX());
        polygon.getPoints().add(pointCross.get(1).getY());
        polygon.getPoints().add(pointCross.get(3).getX());
        polygon.getPoints().add(pointCross.get(3).getY());
        polygon.getPoints().add(pointCross.get(2).getX());
        polygon.getPoints().add(pointCross.get(2).getY());
        Image img = new Image("ui/test.png");
        polygon.setFill(new ImagePattern(img));
        puzzleCanvas.getChildren().add(polygon);



//        WritableImage wr = new WritableImage(canvas.getWidth(), canvas.getHeight());
//        PixelWriter pw = wr.getPixelWriter();
//        for (int i = 0; i < canvas.getWidth(); i++) {
//            for (int j = 0; j < canvas.getHeight(); j++) {
//                pw.setArgb(i,j,canvas.getRGB(i,j));
//            }
//        }
//        ImageView imageView = new ImageView(wr);
//        imageView.setFitHeight(1000);
//        imageView.setFitWidth(1000);
//        puzzleCanvas.getChildren().add(imageView);

    }

}
