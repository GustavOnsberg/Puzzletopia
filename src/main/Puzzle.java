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
    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    int highCornerCountCount = 0;
    int midCornerCountCount = 0;
    int lowCornerCountCount = 0;

    int highCornerCount = -1;
    int midCornerCount = -1;
    int lowCornerCount = -1;

    int n = 0;
    int m = 0;

    ArrayList<Piece> pieces = new ArrayList<>();

    public static void main(String [] args) throws IOException, ParseException {
        Puzzle puzzle = new Puzzle();
        puzzle.loadPuzzle("C:/Users/gusta/Desktop/Puzzles_set_1/Puzzle-15r-20c-8696-sol.json");
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
        }


        for (int i = 3; i <= midCornerCountCount / 4 + 2; i++) {
            if ((highCornerCountCount/(i-2)+i==midCornerCountCount/2+2)){
                m = i;
                n = highCornerCountCount/(i-2)+2;
            }
        }

        System.out.println("Puzzle size: "+n+" x "+m);
    }
}
