package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Puzzle {
    public ArrayList<PlacedPiece> placedPiecesFinal = new ArrayList<>();
    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    int highCornerCountCount = 0;
    int midCornerCountCount = 0;
    int lowCornerCountCount = 0;

    int highCornerCount = -1;
    int midCornerCount = -1;
    int lowCornerCount = -1;

    public int n = 0;
    public int m = 0;

    public float puzzleWidth = 0;
    public float puzzleHeight = 0;

    ArrayList<Piece> pieces = new ArrayList<>();

    public static void main(String [] args) throws IOException, ParseException {
        Puzzle puzzle = new Puzzle();
        puzzle.loadPuzzle("C:/Users/vikto/Downloads/Puzzles_set_1 (1)/Puzzle-15r-20c-8696-sol.json");
        System.out.println("has solution: "+puzzle.findSolution());


    }



    public void loadPuzzle(String filePath) throws IOException, ParseException {
        pieces.clear();
        highCornerCountCount = 0;
        midCornerCountCount = 0;
        lowCornerCountCount = 0;
        highCornerCount = -1;
        midCornerCount = -1;
        lowCornerCount = -1;

        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));
        JSONArray piecesJsonArray = (JSONArray) jsonObject.get("pieces");

        Iterator<JSONObject> iterator = piecesJsonArray.iterator();
        while (iterator.hasNext()){
            JSONArray cornersJsonArray = (JSONArray) iterator.next().get("corners");
            Iterator<JSONObject> iterator1 = cornersJsonArray.iterator();
            pieces.add(new Piece());

            float highX = -999999;
            float lowX = 999999;
            float highY = -999999;
            float lowY = 999999;
            while (iterator1.hasNext()){
                JSONObject coord = (JSONObject) iterator1.next().get("coord");
                float x = Float.parseFloat(coord.get("x").toString());
                float y = Float.parseFloat(coord.get("y").toString());

                if(x > highX)
                    highX = x;
                if(x < lowX)
                    lowX = x;
                if(y > highY)
                    highY = y;
                if(y < lowY)
                    lowY = y;

                pieces.get(pieces.size()-1).corners.add(new FPoint(x,y));

            }
            float toCenterX = -(highX + lowX) / 2;
            float toCenterY = -(highY + lowY) / 2;

            for (int i = 0; i < pieces.get(pieces.size()-1).corners.size(); i++) {
                pieces.get(pieces.size()-1).corners.get(i).x += toCenterX;
                pieces.get(pieces.size()-1).corners.get(i).y += toCenterY;
            }

            int cornerCount = pieces.get(pieces.size()-1).corners.size();

            if (highCornerCount == -1){
                highCornerCount = cornerCount;
                highCornerCountCount++;
            }
            else if(highCornerCount == cornerCount){
                highCornerCountCount++;
            }
            else if(lowCornerCount == -1){
                if (cornerCount < highCornerCount){
                    lowCornerCount = cornerCount;
                    lowCornerCountCount++;
                }
                else{
                    lowCornerCount = highCornerCount;
                    lowCornerCountCount = highCornerCountCount;
                    highCornerCount = cornerCount;
                    highCornerCountCount = 1;
                }
            }
            else if (lowCornerCount == cornerCount){
                lowCornerCountCount++;
            }
            else if (midCornerCount == -1){
                if (cornerCount < highCornerCount && cornerCount > lowCornerCount){
                    midCornerCount = cornerCount;
                    midCornerCountCount++;
                }
                else if(cornerCount > highCornerCount){
                    midCornerCount = highCornerCount;
                    midCornerCountCount = highCornerCountCount;
                    highCornerCount = cornerCount;
                    highCornerCountCount = 1;
                }
                else{
                    midCornerCount = lowCornerCount;
                    midCornerCountCount = lowCornerCountCount;
                    lowCornerCount = cornerCount;
                    lowCornerCountCount = 1;
                }
            }
            else {
                midCornerCountCount++;
            }
        }

        JSONArray formJsonArray = (JSONArray) ((JSONObject) jsonObject.get("puzzle")).get("form");
        iterator = formJsonArray.iterator();
        while(iterator.hasNext()){
            JSONObject coord = (JSONObject) iterator.next().get("coord");
            float x = Float.parseFloat(coord.get("x").toString());
            float y = Float.parseFloat(coord.get("x").toString());
            if (x >= 0.01){
                puzzleWidth = x;
            }
            if (y >= 0.01){
                puzzleHeight = x;
            }
        }

        System.out.println("Puzzle size: " + puzzleWidth + " x " + puzzleHeight);

        System.out.println("Number of corners - center: "+highCornerCount+"   side: "+midCornerCount+"   corner: "+lowCornerCount);
        System.out.println("Number of pieces  - center: "+highCornerCountCount+"   side: "+midCornerCountCount+"   corner: "+lowCornerCountCount);

        for (int i = 0; i < pieces.size(); i++) {
            int cornerCount = pieces.get(i).corners.size();
            if ((lowCornerCountCount == 4 && lowCornerCount == cornerCount) || lowCornerCount == -1){
                pieces.get(i).isCornerPiece = true;
                pieces.get(i).updateCornerArrayRotation();
            }
            else if(cornerCount == midCornerCount || midCornerCount == -1){
                pieces.get(i).isSidePiece = true;
                pieces.get(i).updateCornerArrayRotation();
            }
            System.out.println("piece"+i);
            pieces.get(i).generateEdgeData();
        }


        for (int i = 3; i <= midCornerCountCount / 4 + 2; i++) {
            if ((highCornerCountCount/(i-2)+i==midCornerCountCount/2+2)){
                m = i;
                n = highCornerCountCount/(i-2)+2;
            }
        }

        System.out.println("Puzzle size: "+n+" x "+m);
        findSolution();
    }

    /*public boolean matchEdge(Piece piece1, Piece piece2, int edge1, int edge2){
        if ((piece1.isSidePiece && edge1 == 0) || (piece2.isSidePiece && edge2 == 0) || (piece1.isCornerPiece && edge1 <= 1) || (piece2.isCornerPiece && edge2 <= 1)) {
            System.out.println("match error");
            return false;
        }
        System.out.println("match start:"+pieces.indexOf(piece1)+" "+pieces.indexOf(piece2));
        for (int i = 0; i < piece1.edgeData.get(edge1).lengths.size(); i++) {
            int oppi = piece1.edgeData.get(edge1).lengths.size() - 1 - i;
            if (Math.abs(piece1.edgeData.get(edge1).lengths.get(i) - piece2.edgeData.get(edge2).lengths.get(oppi)) > 0.01){
                System.out.println("match failed 1");
                return false;
            }
            else if (i > 0) {
                int iA = i-1;
                int oppiA = piece1.edgeData.get(edge1).angles.size()-1-iA;
                if (Math.abs((piece1.edgeData.get(edge1).angles.get(iA) + piece2.edgeData.get(edge2).angles.get(oppiA)) - 360) > 0.01){
                    System.out.println("match failed 2");
                    return false;
                }
            }
        }
        System.out.println("match true");
        return true;
    }*/
    /*
    public boolean matchEdge(Piece piece1, Piece piece2, int edge1, int edge2) {
        System.out.println("match start: p1: "+pieces.indexOf(piece1)+" p2: "+pieces.indexOf(piece2)+" e1: "+edge1+" e2: "+edge2);
        if ((piece1.isSidePiece && edge1 == 0) || (piece2.isSidePiece && edge2 == 0) || (piece1.isCornerPiece && edge1 <= 1) || (piece2.isCornerPiece && edge2 <= 1)) {
            System.out.println("match error");
            return false;
        }
        int[] p1Corners = piece1.getCornerIndexes();
        int[] p2Corners = piece2.getCornerIndexes();
        float p1XDiff = piece1.corners.get(p1Corners[edge1]).x-piece1.corners.get(p1Corners[(edge1+1)%4]).x;
        float p1YDiff = piece1.corners.get(p1Corners[edge1]).y-piece1.corners.get(p1Corners[(edge1+1)%4]).y;
        float p2XDiff = piece2.corners.get(p2Corners[(edge2+1)%4]).x-piece2.corners.get(p2Corners[edge2]).x;
        float p2YDiff = piece2.corners.get(p2Corners[(edge2+1)%4]).y-piece2.corners.get(p2Corners[edge2]).y;
        float p1VecLength = (float) Math.sqrt(Math.pow(p1XDiff,2)+Math.pow(p1YDiff,2));
        float p2VecLength = (float) Math.sqrt(Math.pow(p2XDiff,2)+Math.pow(p2YDiff,2));
        if(Math.abs(p1VecLength-p2VecLength) > 0.001){
            return false;
        }
        p1XDiff/=p1VecLength;
        p1YDiff/=p1VecLength;
        p2XDiff/=p2VecLength;
        p2YDiff/=p2VecLength;
        float p1Angle = (float) Math.acos(p1XDiff);
        if (p1XDiff<0){
            p1Angle+=180;
        }
        float p2Angle = (float) Math.acos(p2XDiff);
        if (p2XDiff<0){
            p2Angle+=180;
        }
        float xDiff=0;
        float yDiff=0;
        for (int i = 0; i < (highCornerCount/4)+1; i++) {

            int p1Edge = ((p1Corners[(edge1+1)%4]-i+piece1.corners.size())%piece1.corners.size());
            float p1XNew = piece1.corners.get(edge1).x*p1YDiff-piece1.corners.get(edge1).y*p1XDiff;
            float p1YNew = piece1.corners.get(edge1).x*p1XDiff+piece1.corners.get(edge1).y*p1YDiff;

            int p2Edge = ((p2Corners[(edge2+1)%4]-i+piece2.corners.size())%piece2.corners.size());
            float p2XNew = (piece2.corners.get(p2Corners[(edge2+i)%4]).x*p2YDiff-piece2.corners.get(p2Corners[(edge2+i)%4]).y*p2XDiff);
            float p2YNew = (piece2.corners.get(p2Corners[(edge2+i)%4]).x*p2XDiff+piece2.corners.get(p2Corners[(edge2+i)%4]).y*p2YDiff);

            System.out.println("p1XNew: "+ p1XNew);
            System.out.println("p2XNew: "+ p2XNew);
            System.out.println("p1YNew: "+ p1YNew);
            System.out.println("p2YNew: "+ p2YNew);
            System.out.println("veclength: "+ p1VecLength+" "+p2VecLength);
            if(i==0){
                System.out.println("dif set");
                xDiff=p1XNew-p2XNew;
                yDiff=p1YNew-p2XNew;
            }else if(Math.abs(p1XNew-p2XNew-xDiff)<0.001){
                    if(Math.abs(p1YNew-p2YNew-yDiff)<0.001){
                        System.out.println("match true");

                    }else {
                        System.out.println("match failed y");
                        return false;
                    }
            }else{
                System.out.println("match failed x");
                return false;
            }
        }
        System.out.println("match true");
        return true;
    }*/
    public boolean matchEdge(Piece piece1, Piece piece2, int edge1, int edge2) {
        System.out.println("match start: p1: "+pieces.indexOf(piece1)+" p2: "+pieces.indexOf(piece2)+" e1: "+edge1+" e2: "+edge2);
        if ((piece1.isSidePiece && edge1 == 0) || (piece2.isSidePiece && edge2 == 0) || (piece1.isCornerPiece && edge1 <= 1) || (piece2.isCornerPiece && edge2 <= 1)) {
            System.out.println("match error");
            return false;
        }

        float p1c1x = piece1.getCorners().get(piece1.getCornerIndexes()[edge1]).x;
        float p1c1y = piece1.getCorners().get(piece1.getCornerIndexes()[edge1]).y;
        float p1c2x = piece1.getCorners().get(piece1.getCornerIndexes()[(edge1 + 1) % 4]).x;
        float p1c2y = piece1.getCorners().get(piece1.getCornerIndexes()[(edge1 + 1) % 4]).y;
        float p1eLength = (float) Math.sqrt(Math.pow(p1c1x - p1c2x,2) + Math.pow(p1c1y - p1c2y,2));
        float p1eAngle = (float) Math.acos((p1c2x - p1c1x) / p1eLength) * Math.copySign(1,p1c2y - p1c1y);

        float p2c1x = piece2.getCorners().get(piece2.getCornerIndexes()[edge2]).x;
        float p2c1y = piece2.getCorners().get(piece2.getCornerIndexes()[edge2]).y;
        float p2c2x = piece2.getCorners().get(piece2.getCornerIndexes()[(edge2 + 1) % 4]).x;
        float p2c2y = piece2.getCorners().get(piece2.getCornerIndexes()[(edge2 + 1) % 4]).y;
        float p2eLength = (float) Math.sqrt(Math.pow(p2c1x - p2c2x,2) + Math.pow(p2c1y - p2c2y,2));
        float p2eAngle = (float) Math.acos((p2c1x - p2c2x) / p2eLength) * Math.copySign(1,p2c1y - p2c2y);


        float xDiff = 0;
        float yDiff = 0;
        for (int i = 0; i <= highCornerCount / 4; i++) {

            float p1x = piece1.corners.get((piece1.getCornerIndexes()[edge1] + i) % piece1.corners.size()).x;
            float p1y = piece1.corners.get((piece1.getCornerIndexes()[edge1] + i) % piece1.corners.size()).y;
            float p2x = piece2.corners.get((piece2.getCornerIndexes()[(edge2 + 1) % 4] - i + piece2.corners.size()) % piece2.corners.size()).x;
            float p2y = piece2.corners.get((piece2.getCornerIndexes()[(edge2 + 1) % 4] - i + piece2.corners.size()) % piece2.corners.size()).y;

            float p1xR = (float) (p1x * Math.cos(-p1eAngle) - p1y * Math.sin(-p1eAngle));
            float p1yR = (float) (p1x * Math.sin(-p1eAngle) + p1y * Math.cos(-p1eAngle));
            float p2xR = (float) (p2x * Math.cos(-p2eAngle) - p2y * Math.sin(-p2eAngle));
            float p2yR = (float) (p2x * Math.sin(-p2eAngle) + p2y * Math.cos(-p2eAngle));

            if (i == 0){
                xDiff = p1xR - p2xR;
                yDiff = p1yR - p2yR;
            }
            else {
                if (Math.abs((p1xR - p2xR) - xDiff) > 0.01 || Math.abs((p1yR - p2yR) - yDiff) > 0.01 ) {
                    return false;
                }
            }
        }
        System.out.println("match true:"+pieces.indexOf(piece1)+" p2: "+pieces.indexOf(piece2));
        return true;
    }

    public boolean findSolution(){
        ArrayList<Piece> cornerPiece = new ArrayList<>();
        ArrayList<Piece> sidePiece = new ArrayList<>();
        ArrayList<Piece> centerPiece = new ArrayList<>();
        ArrayList<PlacedPiece> placedPieces = new ArrayList<>();
        for (int i = 0; i < pieces.size(); i++) {
            if(pieces.get(i).isCornerPiece){
                cornerPiece.add(pieces.get(i));
            }else if(pieces.get(i).isSidePiece){
                sidePiece.add(pieces.get(i));
            }else{
                centerPiece.add(pieces.get(i));
            }
        }
        System.out.println("corner size:"+cornerPiece.size());


        return findSolutionRec(cornerPiece,sidePiece,centerPiece,placedPieces,0);
    }
    public boolean findSolutionRec(ArrayList<Piece> cornerPiece,ArrayList<Piece> sidePiece,ArrayList<Piece> centerPiece,ArrayList<PlacedPiece> placedPieces,int piecePosition){
        System.out.println("pp: "+piecePosition);
        //zone 1
        if(piecePosition==0){
            System.out.println("zone 1");
            for (int i = 0; i < 3; i++) {
                ArrayList<Piece>cornerP = (ArrayList<Piece>) cornerPiece.clone();
                System.out.println("cornerP size"+cornerP.size());
                cornerP.remove(i);
                ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                placedP.add(new PlacedPiece(cornerPiece.get(i),1,pieces.indexOf(cornerPiece.get(i))));
                if(findSolutionRec(cornerP,(ArrayList<Piece>)sidePiece.clone(), (ArrayList<Piece>)centerPiece.clone(),placedP,piecePosition+1)){
                    return true;
                }
            }
        }
        //zone 2
        else if(piecePosition<n-1){
            System.out.println("zone 2");
            for (int i = 0; i < sidePiece.size(); i++) {
                System.out.println("zone 2 in");
                if(matchEdge(placedPieces.get(piecePosition-1).piece,sidePiece.get(i),placedPieces.get(piecePosition-1).edgeUp+1,3) ){
                    ArrayList<Piece> sideP = (ArrayList<Piece>) sidePiece.clone();
                    sideP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(sidePiece.get(i),0,pieces.indexOf(sidePiece.get(i))));

                    if(findSolutionRec((ArrayList<Piece>) cornerPiece.clone(),sideP, (ArrayList<Piece>)centerPiece.clone(),placedP,piecePosition+1)){
                        return true;
                    }
                }
            }
        }
        // zone 3
        else if (piecePosition == n-1) {
            System.out.println("zone 3");
            for (int i = 0; i < cornerPiece.size(); i++) {
                if(matchEdge(placedPieces.get(piecePosition-1).piece,cornerPiece.get(i),placedPieces.get(piecePosition-1).edgeUp+1,3) ){
                    ArrayList<Piece>cornerP = (ArrayList<Piece>) cornerPiece.clone();
                    cornerP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(cornerPiece.get(i),0,pieces.indexOf(cornerPiece.get(i))));
                    if(findSolutionRec(cornerP,(ArrayList<Piece>)sidePiece.clone(), (ArrayList<Piece>)centerPiece.clone(),placedP,piecePosition+1)){
                        return true;
                    }
                }
            }
        }
        //zone 7
        else if(piecePosition == n*(m-1)){
            System.out.println("zone 7");
            for (int i = 0; i < cornerPiece.size(); i++) {
                if(matchEdge(placedPieces.get(piecePosition-n).piece,cornerPiece.get(i),3,2) ){
                    ArrayList<Piece>cornerP = (ArrayList<Piece>) cornerPiece.clone();
                    cornerP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(cornerPiece.get(i),2,pieces.indexOf(cornerPiece.get(i))));
                    if(findSolutionRec(cornerP,(ArrayList<Piece>)sidePiece.clone(), (ArrayList<Piece>)centerPiece.clone(),placedP,piecePosition+1)){
                        return true;
                    }
                }
            }
        }
        //zone 9
        else if(piecePosition==n*m-1){
            System.out.println("zone 9");
            if(matchEdge(placedPieces.get(piecePosition-n).piece,cornerPiece.get(0),1,3) && matchEdge(placedPieces.get(piecePosition-1).piece,cornerPiece.get(0),3,2)){
                ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                placedP.add(new PlacedPiece(cornerPiece.get(0),3,pieces.indexOf(cornerPiece.get(0))));
                this.placedPiecesFinal=placedP;
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        if (String.valueOf(pieces.indexOf(placedP.get(i*n+j).piece)).length()==1) {
                            System.out.print("|  ");
                        }else if (String.valueOf(pieces.indexOf(placedP.get(i*n+j).piece)).length()==2) {
                            System.out.print("| ");
                        }else{
                            System.out.print("|");
                        }
                        System.out.print(pieces.indexOf(placedP.get(i*n+j).piece)+":"+placedP.get(i*n+j).edgeUp);
                    }
                    System.out.println("|");
                }

                return true;
            }
        }
        //zone 8
        else if(piecePosition > n*(m-1)){
            System.out.println("zone 8");
            for (int i = 0; i < sidePiece.size(); i++) {
                if(matchEdge(placedPieces.get(piecePosition-n).piece,sidePiece.get(i),(placedPieces.get(piecePosition-n).edgeUp+2)%4,2) && matchEdge(placedPieces.get(piecePosition-1).piece,sidePiece.get(i),3,1)){
                    ArrayList<Piece> sideP = (ArrayList<Piece>) sidePiece.clone();
                    sideP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(sidePiece.get(i),2,pieces.indexOf(sidePiece.get(i))));
                    if(findSolutionRec((ArrayList<Piece>) cornerPiece.clone(),sideP, (ArrayList<Piece>)centerPiece.clone(),placedP,piecePosition+1)){
                        return true;
                    }
                }
            }
        }
        //zone 4
        else if(piecePosition%n==0){
            System.out.println("zone 4");
            for (int i = 0; i < sidePiece.size(); i++) {
                if(matchEdge(placedPieces.get(piecePosition-n).piece,sidePiece.get(i),3,1)){
                    ArrayList<Piece> sideP = (ArrayList<Piece>) sidePiece.clone();
                    sideP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(sidePiece.get(i),1,pieces.indexOf(sidePiece.get(i))));
                    if(findSolutionRec((ArrayList<Piece>) cornerPiece.clone(),sideP, (ArrayList<Piece>)centerPiece.clone(),placedP,piecePosition+1)){
                        return true;
                    }
                }
            }
        }
        //zone 6
        else if(piecePosition%n==n-1){
            System.out.println("zone 6");
            for (int i = 0; i < sidePiece.size(); i++) {
                if(matchEdge(placedPieces.get(piecePosition-n).piece,sidePiece.get(i),(placedPieces.get(piecePosition-n).edgeUp+2)%4,3) && matchEdge(placedPieces.get(piecePosition-1).piece,sidePiece.get(i),(placedPieces.get(piecePosition-1).edgeUp+1)%4,2)){
                    ArrayList<Piece> sideP = (ArrayList<Piece>) sidePiece.clone();
                    sideP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(sidePiece.get(i),3,pieces.indexOf(sidePiece.get(i))));
                    if(findSolutionRec((ArrayList<Piece>) cornerPiece.clone(),sideP, (ArrayList<Piece>)centerPiece.clone(),placedP,piecePosition+1)){
                        return true;
                    }
                }
            }
        }
        //zone 5
        else {
            System.out.println("zone 5");
            for (int i = 0; i < centerPiece.size(); i++) {
                for (int j = 0; j < 4; j++) {
                    if (matchEdge(placedPieces.get(piecePosition-n).piece,centerPiece.get(i),(placedPieces.get(piecePosition-n).edgeUp+2)%4,j) && matchEdge(placedPieces.get(piecePosition-1).piece,centerPiece.get(i),(placedPieces.get(piecePosition-1).edgeUp+1)%4,(j+3)%4)) {
                        ArrayList<Piece> centerP = (ArrayList<Piece>) centerPiece.clone();
                        centerP.remove(i);
                        ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                        placedP.add(new PlacedPiece(centerPiece.get(i),j,pieces.indexOf(centerPiece.get(i))));
                        if(findSolutionRec((ArrayList<Piece>) cornerPiece.clone(),(ArrayList<Piece>)sidePiece.clone(),centerP,placedP,piecePosition+1)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
