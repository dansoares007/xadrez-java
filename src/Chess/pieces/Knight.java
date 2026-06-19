package Chess.pieces;

import Board.Board;
import Chess.ChessPiece;
import Chess.Color;

public class Knight extends ChessPiece {

    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "N"; // Usamos 'N' para o Cavalo (Knight) para não confundir com o 'K' do Rei (King)
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        // A lógica de movimentação em "L" entrará aqui
        return mat;
    }
}