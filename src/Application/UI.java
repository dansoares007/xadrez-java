package Application;

import java.util.InputMismatchException;
import java.util.Scanner;
import Chess.ChessMatch;
import Chess.ChessPiece;
import Chess.ChessPosition;
import Chess.Color;

public class UI {

    // Códigos ANSI para mudar as cores do texto no terminal
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    // Códigos ANSI para mudar a cor do fundo (Background) da casa selecionada
    public static final String ANSI_BLACK_BACKGROUND = "\u001B[40m";
    public static final String ANSI_RED_BACKGROUND = "\u001B[41m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_BLUE_BACKGROUND = "\u001B[44m";
    public static final String ANSI_PURPLE_BACKGROUND = "\u001B[45m";
    public static final String ANSI_CYAN_BACKGROUND = "\u001B[46m";
    public static final String ANSI_WHITE_BACKGROUND = "\u001B[47m";

    // Limpa a tela do terminal para o tabuleiro não ficar empilhando na rolagem
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Lê a entrada do usuário (ex: "a1", "h8") e converte para ChessPosition
    public static ChessPosition readChessPosition(Scanner sc) {
        try {
            String s = sc.nextLine().toLowerCase();
            char column = s.charAt(0);
            int row = Integer.parseInt(s.substring(1));
            return new ChessPosition(column, row);
        } catch (RuntimeException e) {
            throw new InputMismatchException("Erro lendo posicao: formato valido eh de a1 a h8.");
        }
    }

    // Imprime o estado da partida: Tabuleiro, Turno e quem joga
    public static void printMatch(ChessMatch chessMatch) {
        printBoard(chessMatch.getPieces());
        System.out.println();
        System.out.println("Turno: " + chessMatch.getTurn());

        // --- ALTERAÇÃO AQUI: Lógica de Xeque e Xeque-Mate ---
        if (!chessMatch.isCheckMate()) {
            System.out.println("Aguardando jogador: " + chessMatch.getCurrentPlayer());
            if (chessMatch.getCheck()) {
                System.out.println(ANSI_RED + "ATENÇÃO: VOCÊ ESTÁ EM XEQUE!" + ANSI_RESET);
            }
        } else {
            System.out.println(ANSI_RED + "XEQUE-MATE!" + ANSI_RESET);
            System.out.println("Vencedor: " + chessMatch.getCurrentPlayer());
        }
    }

    // Desenha o tabuleiro normal na tela
    public static void printBoard(ChessPiece[][] pieces) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " "); // Números laterais (8 a 1)
            for (int j = 0; j < pieces[i].length; j++) {
                printPiece(pieces[i][j], false);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h"); // Letras inferiores
    }

    // SOBRECARGA: Desenha o tabuleiro destacando os movimentos possíveis em azul
    public static void printBoard(ChessPiece[][] pieces, boolean[][] possibleMoves) {
        for (int i = 0; i < pieces.length; i++) {
            System.out.print((8 - i) + " ");
            for (int j = 0; j < pieces[i].length; j++) {
                printPiece(pieces[i][j], possibleMoves[i][j]);
            }
            System.out.println();
        }
        System.out.println("  a b c d e f g h");
    }

    // Método auxiliar para colorir e imprimir uma única peça
    private static void printPiece(ChessPiece piece, boolean background) {
        if (background) {
            System.out.print(ANSI_BLUE_BACKGROUND); // Pinta o fundo se for movimento válido
        }
        if (piece == null) {
            System.out.print("-" + ANSI_RESET + " "); // Casa vazia externa
        } else {
            if (piece.getColor() == Color.WHITE) {
                System.out.print(ANSI_WHITE + piece + ANSI_RESET + " "); // Peças Brancas
            } else {
                System.out.print(ANSI_YELLOW + piece + ANSI_RESET + " "); // Peças Pretas (Amarelo no console)
            }
        }
    }
}