package main;

import java.util.ArrayList;

public class Piece {
    public ArrayList<FPoint> getCorners() {
        return corners;
    }


    ArrayList<FPoint> corners = new ArrayList<>();
    ArrayList<EdgeData> edgeData = new ArrayList<>();
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

                float a = (float) (Math.pow(corners.get(newIndexZero).x - corners.get((newIndexZero + 1) % corners.size()).x, 2) + Math.pow(corners.get(newIndexZero).y - corners.get((newIndexZero + 1) % corners.size()).y, 2));
                float b = (float) (Math.pow(corners.get((newIndexZero + 1) % corners.size()).x - corners.get((newIndexZero + 2) % corners.size()).x, 2) + Math.pow(corners.get((newIndexZero + 1) % corners.size()).y - corners.get((newIndexZero + 2) % corners.size()).y, 2));
                float c = (float) (Math.pow(corners.get(newIndexZero).x - corners.get((newIndexZero + 2) % corners.size()).x, 2) + Math.pow(corners.get(newIndexZero).y - corners.get((newIndexZero + 2) % corners.size()).y, 2));

                if(Math.abs(a+b-c) < 0.005){
                    cornerNotFound = false;
                }
            }
        }


        for (int i = 0; i < newIndexZero; i++) {
            corners.add(corners.get(0));
            corners.remove(0);
        }
    }
    public void generateEdgeData(){
        if (!isSidePiece && !isCornerPiece){
            for (int i = 0; i < 4; i++) {
                edgeData.add(new EdgeData());
                for (int j = 0; j < corners.size() / 4; j++) {
                    edgeData.get(i).lengths.add((float)Math.sqrt(Math.pow(corners.get((corners.size() / 4) * i + j).x - corners.get(((corners.size() / 4) * i + j + 1) % corners.size()).x, 2) + Math.pow(corners.get((corners.size() / 4) * i + j).y - corners.get(((corners.size() / 4) * i + j + 1) % corners.size()).y, 2)));
                    if (j > 0){
                        float x1 = corners.get((corners.size() / 4) * i + j - 1).x;
                        float x2 = corners.get((corners.size() / 4) * i + j).x;
                        float x3 = corners.get(((corners.size() / 4) * i + j + 1) % corners.size()).x;
                        float y1 = corners.get((corners.size() / 4) * i + j - 1).y;
                        float y2 = corners.get((corners.size() / 4) * i + j).y;
                        float y3 = corners.get(((corners.size() / 4) * i + j + 1) % corners.size()).y;

                        if(y1 != y2 && y2 != y3) {
                            float dx1 = x2 - x1;
                            float dy1 = y2 - y1;
                            float dx2 = x3 - x2;
                            float dy2 = y3 - y2;

                            if (dx1 / dy1 > dx2 / dy2){
                                //Grater than 180 degree
                            }
                            else{
                                //Less than 180 degree
                            }
                        }
                        else if((x1 < x2 && ((y1 == y2 && y1 > y3) || (y2 == y3 && y1 < y3))) || x1 > x2 && ((y1 == y2 && y1 < y3) || (y2 == y3 && y1 > y3))){
                            //Less than 180 degree
                        }
                        else{
                            //Grater than 180 degree
                        }


                    }
                }
            }
        }
    }
}
