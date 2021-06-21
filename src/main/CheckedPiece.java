//Made by Viktor: this is the constructor for handling the Checked pieces data
package main;

public class CheckedPiece {
    Piece piece;
    float diagonal1;
    float diagonal2;
    float rotation;
    public CheckedPiece(Piece piece,float diagonal1,float diagonal2,float rotation){
        this.piece = piece;
        this.diagonal1 = diagonal1;
        this.diagonal2 = diagonal2;
        this.rotation = rotation;

    }
}
