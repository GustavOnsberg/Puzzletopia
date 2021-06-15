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
                generatedEdgeDataFromCorner((corners.size() / 4) * i, corners.size() / 4);
            }
        }
        else if (isSidePiece){
            generatedEdgeDataFromCorner(0,1);
            for (int i = 0; i < 3; i++) {
                generatedEdgeDataFromCorner(((corners.size() - 1) / 3) * i + 1, (corners.size() - 1) / 3);
            }
        }
        else{
            generatedEdgeDataFromCorner(0,1);
            generatedEdgeDataFromCorner(1,1);
            generatedEdgeDataFromCorner(2, (corners.size() - 2) / 2);
            generatedEdgeDataFromCorner(((corners.size() - 2) / 2) + 2, (corners.size() - 2) / 2);
        }
    }

    public void generatedEdgeDataFromCorner(int startCorner, int cornersPerSide){
        edgeData.add(new EdgeData());
        int edgeIndex = edgeData.size() - 1;
        for (int i = 0; i < cornersPerSide; i++) {
            edgeData.get(edgeIndex).lengths.add((float)Math.sqrt(Math.pow(corners.get(startCorner + i).x - corners.get((startCorner + i + 1) % corners.size()).x, 2) + Math.pow(corners.get(startCorner + i).y - corners.get((startCorner + i + 1) % corners.size()).y, 2)));

            if (i > 0){
                float x1 = corners.get(startCorner + i - 1).x;
                float x2 = corners.get(startCorner + i).x;
                float x3 = corners.get((startCorner + i + 1) % corners.size()).x;
                float y1 = corners.get(startCorner + i - 1).y;
                float y2 = corners.get(startCorner + i).y;
                float y3 = corners.get((startCorner + i + 1) % corners.size()).y;

                float a = edgeData.get(edgeIndex).lengths.get(edgeData.get(edgeIndex).lengths.size()-1);
                float b = edgeData.get(edgeIndex).lengths.get(edgeData.get(edgeIndex).lengths.size()-2);
                float c_sq = (float)(Math.pow(x1-x3,2)+Math.pow(y1-y3,2));

                float angle = (float) Math.acos((Math.pow(a,2)+Math.pow(b,2)-c_sq)/(2*a*b));

                //System.out.println("c:" + Math.sqrt(c_sq));
                //System.out.println("a:" +a);
                //System.out.println("b:"+b);

                float dx1 = x2 - x1;
                float dy1 = y2 - y1;
                float dx2 = x3 - x2;
                float dy2 = y3 - y2;

                if(y1 != y2 && y2 != y3) {
                    if (dx1 / dy1 > dx2 / dy2){
                        angle = 360 - angle;
                    }
                }
                else if(!((x1 < x2 && ((y1 == y2 && y1 > y3) || (y2 == y3 && y1 < y3))) || x1 > x2 && ((y1 == y2 && y1 < y3) || (y2 == y3 && y1 > y3)))){
                    angle = 360 - angle;
                }

                edgeData.get(edgeIndex).angles.add(angle);
            }
        }
    }

    public int[] getCornerIndexes(){
        if (!isSidePiece && !isCornerPiece)
            return new int[]{0, corners.size() / 4, (corners.size() / 4) * 2, (corners.size() / 4) * 3};
        else if (isSidePiece)
            return new int[]{0, 1, (corners.size() - 1) / 3 + 1, ((corners.size() - 1) / 3) * 2 + 1};
        else
            return new int[]{0, 1, 2, (corners.size() - 2) / 2 + 2};
    }
}
