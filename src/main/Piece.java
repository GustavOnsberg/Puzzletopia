package main;

import java.util.ArrayList;

public class Piece {
    public ArrayList<FPoint> getCorners() {
        return corners;
    }


    ArrayList<FPoint> corners = new ArrayList<>();
    boolean isSidePiece = false;
    boolean isCornerPiece = false;


    public void updateCornerArrayRotation(){
        if (isSidePiece || isCornerPiece) {
            ArrayList<Float> cornersDist = new ArrayList<>();
            for (int i = 0; i < corners.size()-1; i++) {
                cornersDist.add((float)Math.sqrt(Math.pow(corners.get(i).x - corners.get(i+1).x,2)+Math.pow(corners.get(i).y - corners.get(i+1).y,2)));
            }
            cornersDist.add((float)Math.sqrt(Math.pow(corners.get(corners.size()-1).x - corners.get(0).x,2)+Math.pow(corners.get(corners.size()-1).y - corners.get(0).y,2)));
        }
    }
}
