package main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;


//Gustav and Viktor
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

    //Only used for development of this class
    public static void main(String [] args) throws IOException, ParseException {
        Puzzle puzzle = new Puzzle();
        //puzzle.loadPuzzle("C:/Users/vikto/Downloads/Puzzles_set_1 (1)/Puzzle-15r-20c-8696-sol.json");
        Generator.generate(20,10,2,0.1f,puzzle);
        puzzle.preparePuzzle();
        System.out.println("Has solution: "+puzzle.findSolution());


    }

    //Prepares variables and arrays for a new puzzle, then calls generate() from the Generator class
    public void generatePuzzle(int n, int m, int cuts, float var){
        boolean puzzleIsUnique = false;
        while(!puzzleIsUnique){
            pieces.clear();
            highCornerCountCount = 0;
            midCornerCountCount = 0;
            lowCornerCountCount = 0;
            highCornerCount = -1;
            midCornerCount = -1;
            lowCornerCount = -1;
            Generator.generate(n,m,cuts,var,this);
            preparePuzzle();
            puzzleIsUnique = checkUnique(pieces);
            if (puzzleIsUnique){
                findSolution();
            }
        }
    }

    //Loads a puzzle from a given JSON file
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


        }

        JSONArray formJsonArray = (JSONArray) ((JSONObject) jsonObject.get("puzzle")).get("form");
        iterator = formJsonArray.iterator();
        while(iterator.hasNext()){
            JSONObject coord = (JSONObject) iterator.next().get("coord");
            float x = Float.parseFloat(coord.get("x").toString());
            float y = Float.parseFloat(coord.get("y").toString());
            if (x >= 0.01){
                puzzleWidth = x;
            }
            if (y >= 0.01){
                puzzleHeight = y;
            }
        }


        preparePuzzle();

        checkUnique(pieces);

        findSolution();
    }

    //When a puzzle is generated or loaded, this function categorizes each piece and determine the dimensions of the puzzle.
    private void preparePuzzle(){
        for (int i = 0; i < pieces.size(); i++) {
            int cornerCount = pieces.get(i).corners.size();

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
        }


        for (int i = 3; i <= midCornerCountCount / 4 + 2; i++) {
            if ((highCornerCountCount/(i-2)+i==midCornerCountCount/2+2)){
                m = i;
                n = highCornerCountCount/(i-2)+2;
            }
        }


        System.out.println("Puzzle size: "+n+" x "+m);
        System.out.println("Puzzle dimensions: " + puzzleWidth + " x " + puzzleHeight);

        System.out.println("Number of corners - center: "+highCornerCount+"   side: "+midCornerCount+"   corner: "+lowCornerCount);
        System.out.println("Number of pieces  - center: "+highCornerCountCount+"   side: "+midCornerCountCount+"   corner: "+lowCornerCountCount);
    }

    //Takes one piece from two different pieces and checks if they match
    public boolean matchEdge(Piece piece1, Piece piece2, int edge1, int edge2) {
        if ((piece1.isSidePiece && edge1 == 0) || (piece2.isSidePiece && edge2 == 0) || (piece1.isCornerPiece && edge1 <= 1) || (piece2.isCornerPiece && edge2 <= 1)) {
            return false;
        }

        float p1c1x = piece1.getCorners().get(piece1.getCornerIndexes()[edge1]).x;
        float p1c1y = piece1.getCorners().get(piece1.getCornerIndexes()[edge1]).y;
        float p1c2x = piece1.getCorners().get(piece1.getCornerIndexes()[(edge1 + 1) % 4]).x;
        float p1c2y = piece1.getCorners().get(piece1.getCornerIndexes()[(edge1 + 1) % 4]).y;
        float p1eLength = (float) Math.sqrt(Math.pow(p1c1x - p1c2x, 2) + Math.pow(p1c1y - p1c2y, 2));
        float p1eAngle = (float) Math.acos((p1c2x - p1c1x) / p1eLength) * Math.copySign(1, p1c2y - p1c1y);

        float p2c1x = piece2.getCorners().get(piece2.getCornerIndexes()[edge2]).x;
        float p2c1y = piece2.getCorners().get(piece2.getCornerIndexes()[edge2]).y;
        float p2c2x = piece2.getCorners().get(piece2.getCornerIndexes()[(edge2 + 1) % 4]).x;
        float p2c2y = piece2.getCorners().get(piece2.getCornerIndexes()[(edge2 + 1) % 4]).y;
        float p2eLength = (float) Math.sqrt(Math.pow(p2c1x - p2c2x, 2) + Math.pow(p2c1y - p2c2y, 2));
        float p2eAngle = (float) Math.acos((p2c1x - p2c2x) / p2eLength) * Math.copySign(1, p2c1y - p2c2y);


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

            if (i == 0) {
                xDiff = p1xR - p2xR;
                yDiff = p1yR - p2yR;
            } else {
                if (Math.abs((p1xR - p2xR) - xDiff) > 0.01 || Math.abs((p1yR - p2yR) - yDiff) > 0.01) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkUnique(ArrayList<Piece> Pieces) {//Checks all the corners, side and center pieces are unique returns true if unique
        ArrayList<Piece> cornerPiece = new ArrayList<>();
        ArrayList<Piece> sidePiece = new ArrayList<>();
        ArrayList<Piece> centerPiece = new ArrayList<>();
        ArrayList<CheckedPiece> cornerChecked = new ArrayList<>();
        ArrayList<CheckedPiece> sideChecked = new ArrayList<>();
        ArrayList<CheckedPiece> centerChecked = new ArrayList<>();

        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).isCornerPiece) {
                cornerPiece.add(pieces.get(i));
            } else if (pieces.get(i).isSidePiece) {
                sidePiece.add(pieces.get(i));
            } else {
                centerPiece.add(pieces.get(i));
            }
        }
        for (int i = 0; i < cornerPiece.size(); i++) {//for loop to check the corner pieces
            float x0 = cornerPiece.get(i).corners.get(0).x;
            float y0 = cornerPiece.get(i).corners.get(0).y;

            float x2 = cornerPiece.get(i).corners.get(cornerPiece.get(i).getCornerIndexes()[2]).x;
            float y2 = cornerPiece.get(i).corners.get(cornerPiece.get(i).getCornerIndexes()[2]).y;

            float diagonal1 = (float) Math.sqrt(Math.pow(x2 - x0, 2) + Math.pow(y2 - y0, 2));

            float x1 = cornerPiece.get(i).corners.get(cornerPiece.get(i).getCornerIndexes()[1]).x;
            float y1 = cornerPiece.get(i).corners.get(cornerPiece.get(i).getCornerIndexes()[1]).y;

            float x3 = cornerPiece.get(i).corners.get(cornerPiece.get(i).getCornerIndexes()[3]).x;
            float y3 = cornerPiece.get(i).corners.get(cornerPiece.get(i).getCornerIndexes()[3]).y;

            float diagonal2 = (float) Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2));


            float rotation = (float) Math.acos((x2 - x0) / diagonal1) * Math.copySign(1, y2 - y0);

            cornerChecked.add(new CheckedPiece(cornerPiece.get(i), diagonal1, diagonal2, rotation));
            for (int j = 0; j < cornerChecked.size() - 1; j++) {
                if (Math.abs(cornerChecked.get(j).diagonal1 - cornerChecked.get(cornerChecked.size() - 1).diagonal1) < 0.001 && Math.abs(cornerChecked.get(j).diagonal2 - cornerChecked.get(cornerChecked.size() - 1).diagonal2) < 0.001) {
                    float newX1;
                    float newY1;
                    float newX2;
                    float newY2;

                    for (int k = 0; k < cornerChecked.get(cornerChecked.size() - 1).piece.corners.size(); k++) {
                        newX1 = (float) (cornerChecked.get(cornerChecked.size() - 1).piece.corners.get(k).x * Math.cos(cornerChecked.get(cornerChecked.size() - 1).rotation) - cornerChecked.get(cornerChecked.size() - 1).piece.corners.get(k).x * Math.sin(cornerChecked.get(cornerChecked.size() - 1).rotation));
                        newY1 = (float) (cornerChecked.get(cornerChecked.size() - 1).piece.corners.get(k).x * Math.sin(cornerChecked.get(cornerChecked.size() - 1).rotation) + cornerChecked.get(cornerChecked.size() - 1).piece.corners.get(k).x * Math.cos(cornerChecked.get(cornerChecked.size() - 1).rotation));
                        newX2 = (float) (cornerChecked.get(j).piece.corners.get(k).x * Math.cos(cornerChecked.get(j).rotation) - cornerChecked.get(j).piece.corners.get(k).x * Math.sin(cornerChecked.get(j).rotation));
                        newY2 = (float) (cornerChecked.get(j).piece.corners.get(k).x * Math.sin(cornerChecked.get(j).rotation) + cornerChecked.get(j).piece.corners.get(k).x * Math.cos(cornerChecked.get(j).rotation));
                        if (Math.abs(newX1-newX2)>0.001 || Math.abs(newY1-newY2)>0.001) {
                            break;
                        } else if (Math.abs(newX1-newX2)<0.001 && Math.abs(newY1 -newY1)<0.001&& k == cornerChecked.get(cornerChecked.size() - 1).piece.corners.size() - 1) {
                            System.out.println("All pieces unique: false");
                            return false;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < sidePiece.size(); i++) {//for loop to check the side pieces
            float x0 = sidePiece.get(i).corners.get(0).x;
            float y0 = sidePiece.get(i).corners.get(0).y;

            float x2 = sidePiece.get(i).corners.get(sidePiece.get(i).getCornerIndexes()[2]).x;
            float y2 = sidePiece.get(i).corners.get(sidePiece.get(i).getCornerIndexes()[2]).y;

            float diagonal1 = (float) Math.sqrt(Math.pow(x2 - x0, 2) + Math.pow(y2 - y0, 2));

            float x1 = sidePiece.get(i).corners.get(sidePiece.get(i).getCornerIndexes()[1]).x;
            float y1 = sidePiece.get(i).corners.get(sidePiece.get(i).getCornerIndexes()[1]).y;

            float x3 = sidePiece.get(i).corners.get(sidePiece.get(i).getCornerIndexes()[3]).x;
            float y3 = sidePiece.get(i).corners.get(sidePiece.get(i).getCornerIndexes()[3]).y;

            float diagonal2 = (float) Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2));


            float rotation = (float) Math.acos((x2 - x0) / diagonal1) * Math.copySign(1, y2 - y0);

            sideChecked.add(new CheckedPiece(sidePiece.get(i), diagonal1, diagonal2, rotation));
            for (int j = 0; j < sideChecked.size() - 1; j++) {
                if (Math.abs(sideChecked.get(j).diagonal1 - sideChecked.get(sideChecked.size() - 1).diagonal1) < 0.001 && Math.abs(sideChecked.get(j).diagonal2 - sideChecked.get(sideChecked.size() - 1).diagonal2) < 0.001) {
                    float newX1;
                    float newY1;
                    float newX2;
                    float newY2;

                    for (int k = 0; k < sideChecked.get(sideChecked.size() - 1).piece.corners.size(); k++) {
                        newX1 = (float) (sideChecked.get(sideChecked.size() - 1).piece.corners.get(k).x * Math.cos(sideChecked.get(sideChecked.size() - 1).rotation) - sideChecked.get(sideChecked.size() - 1).piece.corners.get(k).x * Math.sin(sideChecked.get(sideChecked.size() - 1).rotation));
                        newY1 = (float) (sideChecked.get(sideChecked.size() - 1).piece.corners.get(k).x * Math.sin(sideChecked.get(sideChecked.size() - 1).rotation) + sideChecked.get(sideChecked.size() - 1).piece.corners.get(k).x * Math.cos(sideChecked.get(sideChecked.size() - 1).rotation));
                        newX2 = (float) (sideChecked.get(j).piece.corners.get(k).x * Math.cos(sideChecked.get(j).rotation) - sideChecked.get(j).piece.corners.get(k).x * Math.sin(sideChecked.get(j).rotation));
                        newY2 = (float) (sideChecked.get(j).piece.corners.get(k).x * Math.sin(sideChecked.get(j).rotation) + sideChecked.get(j).piece.corners.get(k).x * Math.cos(sideChecked.get(j).rotation));
                        if (Math.abs(newX1-newX2)>0.001 || Math.abs(newY1-newY2)>0.001) {
                            break;
                        } else if (Math.abs(newX1-newX2)<0.001 && Math.abs(newY1 -newY1)<0.001&& k == sideChecked.get(sideChecked.size() - 1).piece.corners.size() - 1) {
                            System.out.println("All pieces unique: false");
                            return false;
                        }
                    }
                }
            }
        }
        for (int i = 0; i < centerPiece.size(); i++) {//for loop to check the center pieces
            float x0 = centerPiece.get(i).corners.get(0).x;
            float y0 = centerPiece.get(i).corners.get(0).y;

            float x2 = centerPiece.get(i).corners.get(centerPiece.get(i).getCornerIndexes()[2]).x;
            float y2 = centerPiece.get(i).corners.get(centerPiece.get(i).getCornerIndexes()[2]).y;

            float diagonal1 = (float) Math.sqrt(Math.pow(x2 - x0, 2) + Math.pow(y2 - y0, 2));

            float x1 = centerPiece.get(i).corners.get(centerPiece.get(i).getCornerIndexes()[1]).x;
            float y1 = centerPiece.get(i).corners.get(centerPiece.get(i).getCornerIndexes()[1]).y;

            float x3 = centerPiece.get(i).corners.get(centerPiece.get(i).getCornerIndexes()[3]).x;
            float y3 = centerPiece.get(i).corners.get(centerPiece.get(i).getCornerIndexes()[3]).y;

            float diagonal2 = (float) Math.sqrt(Math.pow(x3 - x1, 2) + Math.pow(y3 - y1, 2));


            float rotation = (float) Math.acos((x2 - x0) / diagonal1) * Math.copySign(1, y2 - y0);

            centerChecked.add(new CheckedPiece(centerPiece.get(i), diagonal1, diagonal2, rotation));
            for (int j = 0; j < centerChecked.size() - 1; j++) {
                if (Math.abs(centerChecked.get(j).diagonal1 - centerChecked.get(centerChecked.size() - 1).diagonal1) < 0.001 && Math.abs(centerChecked.get(j).diagonal2 - centerChecked.get(centerChecked.size() - 1).diagonal2) < 0.001) {
                    float newX1;
                    float newY1;
                    float newX2;
                    float newY2;

                    for (int l = 0; l < 4; l++) {
                        if(l<2){
                            centerChecked.get(centerChecked.size() - 1).rotation += Math.PI*l;
                        }else{
                            float rotation2 = (float) (Math.acos((x3 - x1) / diagonal1) * Math.copySign(1, y3 - y1)+Math.PI*(l-2));
                        }
                        for (int k = 0; k < centerChecked.get(centerChecked.size() - 1).piece.corners.size(); k++) {
                            newX1 = (float) (centerChecked.get(centerChecked.size() - 1).piece.corners.get(k).x * Math.cos(centerChecked.get(centerChecked.size() - 1).rotation) - centerChecked.get(centerChecked.size() - 1).piece.corners.get(k).x * Math.sin(centerChecked.get(centerChecked.size() - 1).rotation));
                            newY1 = (float) (centerChecked.get(centerChecked.size() - 1).piece.corners.get(k).x * Math.sin(centerChecked.get(centerChecked.size() - 1).rotation) + centerChecked.get(centerChecked.size() - 1).piece.corners.get(k).x * Math.cos(centerChecked.get(centerChecked.size() - 1).rotation));
                            newX2 = (float) (centerChecked.get(j).piece.corners.get(k).x * Math.cos(centerChecked.get(j).rotation) - centerChecked.get(j).piece.corners.get(k).x * Math.sin(centerChecked.get(j).rotation));
                            newY2 = (float) (centerChecked.get(j).piece.corners.get(k).x * Math.sin(centerChecked.get(j).rotation) + centerChecked.get(j).piece.corners.get(k).x * Math.cos(centerChecked.get(j).rotation));
                            if (Math.abs(newX1-newX2)>0.001 || Math.abs(newY1-newY2)>0.001) {
                                break;
                            } else if (Math.abs(newX1-newX2)<0.001 && Math.abs(newY1 -newY1)<0.001 && k == centerChecked.get(centerChecked.size() - 1).piece.corners.size() - 1) {
                                System.out.println("All pieces unique: false");
                                return false;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("All pieces unique: true");
        return true;
    }


    public boolean findSolution() {//prepares the data and starts the recursive call that checks if the puzzle has a solution
        ArrayList<Piece> cornerPiece = new ArrayList<>();
        ArrayList<Piece> sidePiece = new ArrayList<>();
        ArrayList<Piece> centerPiece = new ArrayList<>();
        ArrayList<PlacedPiece> placedPieces = new ArrayList<>();
        for (int i = 0; i < pieces.size(); i++) {
            if (pieces.get(i).isCornerPiece) {
                cornerPiece.add(pieces.get(i));
            } else if (pieces.get(i).isSidePiece) {
                sidePiece.add(pieces.get(i));
            } else {
                centerPiece.add(pieces.get(i));
            }
        }


        return findSolutionRec(cornerPiece, sidePiece, centerPiece, placedPieces, 0);
    }

    //The recursive function for findSolution()
    public boolean findSolutionRec(ArrayList<Piece> cornerPiece, ArrayList<Piece> sidePiece, ArrayList<Piece> centerPiece, ArrayList<PlacedPiece> placedPieces, int piecePosition) {
        //Zones of the puzzle to simplify solving the puzzle
        //|1|2|3|
        //|4|5|6|
        //|7|8|9|
        //zone 1
        if (piecePosition == 0) {
            for (int i = 0; i < cornerPiece.size()-1; i++) {
                ArrayList<Piece> cornerP = (ArrayList<Piece>) cornerPiece.clone();
                cornerP.remove(i);
                ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                placedP.add(new PlacedPiece(cornerPiece.get(i), 1, pieces.indexOf(cornerPiece.get(i))));
                if (findSolutionRec(cornerP, (ArrayList<Piece>) sidePiece.clone(), (ArrayList<Piece>) centerPiece.clone(), placedP, piecePosition + 1)) {
                    return true;
                }
            }
        }
        //zone 2
        else if (piecePosition < n - 1) {
            for (int i = 0; i < sidePiece.size(); i++) {
                if (matchEdge(placedPieces.get(piecePosition - 1).piece, sidePiece.get(i), placedPieces.get(piecePosition - 1).edgeUp + 1, 3)) {
                    ArrayList<Piece> sideP = (ArrayList<Piece>) sidePiece.clone();
                    sideP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(sidePiece.get(i), 0, pieces.indexOf(sidePiece.get(i))));

                    if (findSolutionRec((ArrayList<Piece>) cornerPiece.clone(), sideP, (ArrayList<Piece>) centerPiece.clone(), placedP, piecePosition + 1)) {
                        return true;
                    }
                }
            }
        }
        // zone 3
        else if (piecePosition == n - 1) {
            for (int i = 0; i < cornerPiece.size(); i++) {
                if (matchEdge(placedPieces.get(piecePosition - 1).piece, cornerPiece.get(i), placedPieces.get(piecePosition - 1).edgeUp + 1, 3)) {
                    ArrayList<Piece> cornerP = (ArrayList<Piece>) cornerPiece.clone();
                    cornerP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(cornerPiece.get(i), 0, pieces.indexOf(cornerPiece.get(i))));
                    if (findSolutionRec(cornerP, (ArrayList<Piece>) sidePiece.clone(), (ArrayList<Piece>) centerPiece.clone(), placedP, piecePosition + 1)) {
                        return true;
                    }
                }
            }
        }
        //zone 7
        else if (piecePosition == n * (m - 1)) {
            for (int i = 0; i < cornerPiece.size(); i++) {
                if (matchEdge(placedPieces.get(piecePosition - n).piece, cornerPiece.get(i), 3, 2)) {
                    ArrayList<Piece> cornerP = (ArrayList<Piece>) cornerPiece.clone();
                    cornerP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(cornerPiece.get(i), 2, pieces.indexOf(cornerPiece.get(i))));
                    if (findSolutionRec(cornerP, (ArrayList<Piece>) sidePiece.clone(), (ArrayList<Piece>) centerPiece.clone(), placedP, piecePosition + 1)) {
                        return true;
                    }
                }
            }
        }
        //zone 9
        else if (piecePosition == n * m - 1) {
            if (matchEdge(placedPieces.get(piecePosition - n).piece, cornerPiece.get(0), 1, 3) && matchEdge(placedPieces.get(piecePosition - 1).piece, cornerPiece.get(0), 3, 2)) {
                ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                placedP.add(new PlacedPiece(cornerPiece.get(0), 3, pieces.indexOf(cornerPiece.get(0))));
                this.placedPiecesFinal = placedP;
                for (int i = 0; i < m; i++) {
                    for (int j = 0; j < n; j++) {
                        if (String.valueOf(pieces.indexOf(placedP.get(i * n + j).piece)).length() == 1) {
                            System.out.print("|  ");
                        } else if (String.valueOf(pieces.indexOf(placedP.get(i * n + j).piece)).length() == 2) {
                            System.out.print("| ");
                        } else {
                            System.out.print("|");
                        }
                        System.out.print(pieces.indexOf(placedP.get(i * n + j).piece) + ":" + placedP.get(i * n + j).edgeUp);
                    }
                    System.out.println("|");
                }

                return true;
            }
        }
        //zone 8
        else if (piecePosition > n * (m - 1)) {
            for (int i = 0; i < sidePiece.size(); i++) {
                if (matchEdge(placedPieces.get(piecePosition - n).piece, sidePiece.get(i), (placedPieces.get(piecePosition - n).edgeUp + 2) % 4, 2) && matchEdge(placedPieces.get(piecePosition - 1).piece, sidePiece.get(i), 3, 1)) {
                    ArrayList<Piece> sideP = (ArrayList<Piece>) sidePiece.clone();
                    sideP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(sidePiece.get(i), 2, pieces.indexOf(sidePiece.get(i))));
                    if (findSolutionRec((ArrayList<Piece>) cornerPiece.clone(), sideP, (ArrayList<Piece>) centerPiece.clone(), placedP, piecePosition + 1)) {
                        return true;
                    }
                }
            }
        }
        //zone 4
        else if (piecePosition % n == 0) {
            for (int i = 0; i < sidePiece.size(); i++) {
                if (matchEdge(placedPieces.get(piecePosition - n).piece, sidePiece.get(i), 3, 1)) {
                    ArrayList<Piece> sideP = (ArrayList<Piece>) sidePiece.clone();
                    sideP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(sidePiece.get(i), 1, pieces.indexOf(sidePiece.get(i))));
                    if (findSolutionRec((ArrayList<Piece>) cornerPiece.clone(), sideP, (ArrayList<Piece>) centerPiece.clone(), placedP, piecePosition + 1)) {
                        return true;
                    }
                }
            }
        }
        //zone 6
        else if (piecePosition % n == n - 1) {
            for (int i = 0; i < sidePiece.size(); i++) {
                if (matchEdge(placedPieces.get(piecePosition - n).piece, sidePiece.get(i), (placedPieces.get(piecePosition - n).edgeUp + 2) % 4, 3) && matchEdge(placedPieces.get(piecePosition - 1).piece, sidePiece.get(i), (placedPieces.get(piecePosition - 1).edgeUp + 1) % 4, 2)) {
                    ArrayList<Piece> sideP = (ArrayList<Piece>) sidePiece.clone();
                    sideP.remove(i);
                    ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                    placedP.add(new PlacedPiece(sidePiece.get(i), 3, pieces.indexOf(sidePiece.get(i))));
                    if (findSolutionRec((ArrayList<Piece>) cornerPiece.clone(), sideP, (ArrayList<Piece>) centerPiece.clone(), placedP, piecePosition + 1)) {
                        return true;
                    }
                }
            }
        }
        //zone 5
        else {
            for (int i = 0; i < centerPiece.size(); i++) {
                for (int j = 0; j < 4; j++) {
                    if (matchEdge(placedPieces.get(piecePosition - n).piece, centerPiece.get(i), (placedPieces.get(piecePosition - n).edgeUp + 2) % 4, j) && matchEdge(placedPieces.get(piecePosition - 1).piece, centerPiece.get(i), (placedPieces.get(piecePosition - 1).edgeUp + 1) % 4, (j + 3) % 4)) {
                        ArrayList<Piece> centerP = (ArrayList<Piece>) centerPiece.clone();
                        centerP.remove(i);
                        ArrayList<PlacedPiece> placedP = (ArrayList<PlacedPiece>) placedPieces.clone();
                        placedP.add(new PlacedPiece(centerPiece.get(i), j, pieces.indexOf(centerPiece.get(i))));
                        if (findSolutionRec((ArrayList<Piece>) cornerPiece.clone(), (ArrayList<Piece>) sidePiece.clone(), centerP, placedP, piecePosition + 1)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
