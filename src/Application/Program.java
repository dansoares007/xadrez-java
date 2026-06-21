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

                if (chessMatch.getPromoted() != null) {
                    System.out.print("Peão promovido! Digite a inicial da peça escolhida (B/N/R/Q): ");
                    String type = sc.nextLine().toUpperCase();

                    while (!type.equals("B") && !type.equals("N") && !type.equals("R") && !type.equals("Q")) {
                        System.out.print("Letra inválida! Digite (B) para Bispo, (N) para Cavalo, (R) para Torre ou (Q) para Rainha: ");
                        type = sc.nextLine().toUpperCase();
                    }
                    chessMatch.replacePromotedPiece(type);
                }

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