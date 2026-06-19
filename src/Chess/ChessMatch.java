package Chess;

import Board.Board;
import Board.Piece;
import Board.Position;
import Chess.pieces.King;
import Chess.pieces.Rook;

public class ChessMatch {
    private int turn;
    private Color currentPlayer;
    private Board board; 
    private boolean check;
    private boolean checkMate;
    private ChessPiece enPassantVulnerable;
    private ChessPiece promoted;

    public ChessMatch() {
        
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean isCheck() {
        return check;
    }

    public boolean isCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    public ChessPiece getPromoted() {
        return promoted;
    }

    public ChessPiece[][] getPieces() {
        
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        
    }

    public ChessPiece replacePromotedPiece(String type) {
        
    }

    private void validateSourcePosition(Position position) {
        
    }

    private void validateTargetPosition(Position source, Position target) {
       
    }

    private Piece makeMove(Position source, Position target) {
       
    }

    private void nextTurn() {
        
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
    }

    private void initialSetup() {
        
    }
}
