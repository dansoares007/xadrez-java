package Chess.pieces;

import Board.Board;
import Chess.ChessPiece;
import Chess.Color;

public class Bishop extends ChessPiece {

    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "B";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        // A lógica de movimentação nas diagonais entrará aqui
        return mat;
    }
}