package Chess.pieces;

import Board.Board;
import Chess.ChessPiece;
import Chess.Color;

public class Pawn extends ChessPiece {

    public Pawn(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        // A lógica de andar para frente e capturar nas diagonais entrará aqui
        return mat;
    }
}