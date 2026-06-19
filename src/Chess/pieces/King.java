package Chess.pieces;

import Board.Board;
import Chess.ChessPiece;
import Chess.Color;

public class King extends ChessPiece {

    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        // A lógica de mover 1 casa para qualquer direção entrará aqui
        return mat;
    }
}