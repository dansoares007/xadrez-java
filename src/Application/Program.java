package Application;

import java.util.InputMismatchException;
import java.util.Scanner;
import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;
import Chess.ChessException;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        ChessMatch chessMatch = new ChessMatch();

        // CORREÇÃO AQUI: Alterado de getCheckMate() para isCheckMate()
        while (!chessMatch.isCheckMate()) {
            try {

                UI.clearScreen();
                UI.printMatch(chessMatch);

                System.out.println();
                System.out.print("Posição de origem: ");
                ChessPosition source = UI.readChessPosition(sc);

                boolean[][] possibleMoves = chessMatch.possibleMoves(source);
                UI.clearScreen();
                UI.printBoard(chessMatch.getPieces(), possibleMoves);

                System.out.println();
                System.out.print("Posição de destino: ");
                ChessPosition target = UI.readChessPosition(sc);

                ChessPiece capturedPiece = chessMatch.performChessMove(source, target);

            }
            catch (ChessException | InputMismatchException e) {
                System.out.println(e.getMessage());
                System.out.println("Pressione Enter para continuar...");
                sc.nextLine();
            }
        }

        UI.clearScreen();
        UI.printMatch(chessMatch);
    }
}