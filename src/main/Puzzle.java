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
    ArrayList<Piece> pieces = new ArrayList<>();

    public static void main(String [] args) throws IOException, ParseException {
        Puzzle puzzle = new Puzzle();
        puzzle.loadPuzzle("C:/Users/gusta/Desktop/Puzzle-3r-3c-3756-sol.json");
    }



    public void loadPuzzle(String filePath) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(filePath));
        JSONArray piecesJsonArray = (JSONArray) jsonObject.get("pieces");

        Iterator<JSONObject> iterator = piecesJsonArray.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

    }
}
