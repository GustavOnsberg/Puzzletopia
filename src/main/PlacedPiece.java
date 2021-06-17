package main;

public class PlacedPiece {
    Piece piece;
    int edgeUp;
    int index;
    public PlacedPiece(Piece piece,int edgeUp,int index){
        this.piece = piece;
        this.edgeUp = edgeUp;
        this.index = index;
    }
}
