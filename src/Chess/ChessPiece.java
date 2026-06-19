package Chess;

import Board.Board;
import Board.Piece;
import Board.Position;

public abstract class ChessPiece extends Piece {
    private Color color;
    private int moveCount;

    public ChessPiece(Board board, Color color) {
        
    }

    public Color getColor() {
        
    }

    public int getMoveCount() {
        
    }

    protected void increaseMoveCount() {
        
    }

    protected void decreaseMoveCount() {
        
    }

    public ChessPosition getChessPosition() {
        
    }

    protected boolean isThereOpponentPiece(Position position) {
        
    }
}