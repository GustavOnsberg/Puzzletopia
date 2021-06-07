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

    ArrayList<Piece> pieces = new ArrayList<>();

    public static void main(String [] args) throws IOException, ParseException {
        Puzzle puzzle = new Puzzle();
        puzzle.loadPuzzle("C:/Users/gusta/Desktop/Puzzle-3r-3c-3756-sol.json");
    }



    public void loadPuzzle(String filePath) throws IOException, ParseException {
        pieces.clear();
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

    }
}
