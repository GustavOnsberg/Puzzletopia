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
        int newIndexZero = 0;
        if(isSidePiece) {
            int jumps = (corners.size() - 1) / 3;
            ArrayList<Float> sideScore = new ArrayList<>();
            for (int i = 0; i < corners.size(); i++) {
                sideScore.add((float)(
                        (Math.pow(corners.get(i).x - corners.get((i + 1) % corners.size()).x, 2) + Math.pow(corners.get(i).y - corners.get((i + 1) % corners.size()).y, 2)) *
                                Math.sqrt(Math.pow(corners.get((i + 1) % corners.size()).x - corners.get((i + 1 + jumps) % corners.size()).x, 2) + Math.pow(corners.get((i + 1) % corners.size()).y - corners.get((i + 1 + jumps) % corners.size()).y, 2)) *
                                Math.sqrt(Math.pow(corners.get((i + 1 + jumps) % corners.size()).x - corners.get((i + 1 + jumps * 2) % corners.size()).x, 2) + Math.pow(corners.get((i + 1 + jumps) % corners.size()).y - corners.get((i + 1 + jumps * 2) % corners.size()).y, 2)) *
                                Math.sqrt(Math.pow(corners.get((i + 1 + jumps * 2) % corners.size()).x - corners.get(i).x, 2) + Math.pow(corners.get((i + 1 + jumps * 2) % corners.size()).y - corners.get(i).y, 2))
                ));
                if(sideScore.get(i) > sideScore.get(newIndexZero)){
                    newIndexZero = i;
                }
            }
        }
        else if(isCornerPiece){
            int jumps = (corners.size() - 2) / 2;
            ArrayList<Float> sideScore = new ArrayList<>();
            for (int i = 0; i < corners.size(); i++) {
                sideScore.add((float)(
                        (Math.pow(corners.get(i).x - corners.get((i + 1) % corners.size()).x, 2) + Math.pow(corners.get(i).y - corners.get((i + 1) % corners.size()).y, 2)) *
                                (Math.pow(corners.get((i + 1) % corners.size()).x - corners.get((i + 2) % corners.size()).x, 2) + Math.pow(corners.get((i + 1) % corners.size()).y - corners.get((i + 2) % corners.size()).y, 2)) *
                                Math.sqrt(Math.pow(corners.get((i + 2) % corners.size()).x - corners.get((i + 1 + jumps) % corners.size()).x, 2) + Math.pow(corners.get((i + 2) % corners.size()).y - corners.get((i + 1 + jumps) % corners.size()).y, 2)) *
                                Math.sqrt(Math.pow(corners.get((i + 1 + jumps) % corners.size()).x - corners.get(i).x, 2) + Math.pow(corners.get((i + 1 + jumps) % corners.size()).y - corners.get(i).y, 2))
                ));
            }

            boolean cornerNotFound = true;
            float invalidHighScore = 999999;
            while (cornerNotFound){

                float foundHighScore = 0;

                for (int i = 0; i < sideScore.size(); i++) {
                    if (sideScore.get(i) < invalidHighScore && sideScore.get(i) > foundHighScore){
                        foundHighScore = sideScore.get(i);
                        newIndexZero = i;
                    }
                }

                float a = (float) Math.sqrt(Math.pow(corners.get(newIndexZero).x - corners.get((newIndexZero + 1) % corners.size()).x, 2) + Math.pow(corners.get(newIndexZero).y - corners.get((newIndexZero + 1) % corners.size()).y, 2));
                float b = (float) Math.sqrt(Math.pow(corners.get((newIndexZero + 1) % corners.size()).x - corners.get((newIndexZero + 2) % corners.size()).x, 2) + Math.pow(corners.get((newIndexZero + 1) % corners.size()).y - corners.get((newIndexZero + 2) % corners.size()).y, 2));
                float c = (float) Math.sqrt(Math.pow(corners.get((newIndexZero + 2) % corners.size()).x - corners.get((newIndexZero + 3) % corners.size()).x, 2) + Math.pow(corners.get((newIndexZero + 2) % corners.size()).y - corners.get((newIndexZero + 3) % corners.size()).y, 2));
                cornerNotFound = false;
                if(Math.pow(a,2)+Math.pow(b,2)==Math.pow(c,2)){
                    cornerNotFound = false;
                }
            }


        }



        for (int i = 0; i < newIndexZero; i++) {
            corners.add(corners.get(0));
            corners.remove(0);
        }
    }
}
