package main;

public class GameManager {
    public PuzzleInfo getPuzzleInfo() {
        return puzzleInfo;
    }

    PuzzleInfo puzzleInfo;
    Generator generator;
    Validator validator;

    public GameManager(){
        generator = new Generator();
        validator = new Validator();
    }

    public void makeNewPuzzle(int n, int m){
        puzzleInfo = new PuzzleInfo(n, m);
        boolean puzzleIsValid = false;
        while(!puzzleIsValid){
            generator.generate(puzzleInfo);
            puzzleIsValid = validator.validate(puzzleInfo);
        }
    }
}
