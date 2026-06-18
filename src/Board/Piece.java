package Board;

public abstract class Piece {
    protected Position position;

    public Piece(Position position) {
        this.position = position;
    }

    public abstract boolean[][] possibleMoves();

    public boolean possibleMove(Position position){
        return false;
    }

    public boolean isThereAnyPossibleMove(){
        return false;
    }



}
