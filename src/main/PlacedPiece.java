package main;

public class PlacedPiece {
    Piece piece;
    public int edgeUp;
    public int index;
    public PlacedPiece(Piece piece,int edgeUp,int index){
        this.piece = piece;
        this.edgeUp = edgeUp;
        this.index = index;
    }
}
