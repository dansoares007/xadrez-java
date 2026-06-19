package Chess;

import Board.Position;

public class ChessPosition {
    private char column;
    private int row;

    public ChessPosition(char column, int row) {

    }

    public char getColumn() {
        return column;
    }

    public int getRow() {
        return row;
    }

    // Métodos protegidos de conversão interna matriz <-> xadrez (#)
    protected Position toPosition() {

    }

    protected static ChessPosition fromPosition(Position position) {

    }

    @Override
    public String toString() {
        return "" + column + row;
    }
}