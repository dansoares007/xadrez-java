package Chess;

import Board.Board;
import Board.Piece;
import Board.Position;
import Chess.pieces.Bishop;
import Chess.pieces.King;
import Chess.pieces.Knight;
import Chess.pieces.Pawn;
import Chess.pieces.Queen;
import Chess.pieces.Rook;

public class ChessMatch {

    private int turn;
    private Color currentPlayer;
    private Board board;
    private boolean check; // Indica se o jogador atual está em xeque
    private boolean checkMate; // Indica se o jogo acabou por xeque-mate
    private ChessPiece enPassantVulnerable; // Guarda o peão vulnerável ao En Passant

    // Construtor: Inicializa a partida criando um tabuleiro 8x8 e colocando as peças
    public ChessMatch() {
        board = new Board(8, 8);
        turn = 1;
        currentPlayer = Color.WHITE;
        check = false;
        checkMate = false;
        initialSetup(); // Coloca as peças no tabuleiro para o jogo começar
    }

    public int getTurn() {
        return turn;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public boolean getCheck() {
        return check;
    }

    public boolean getCheckMate() {
        return checkMate;
    }

    public ChessPiece getEnPassantVulnerable() {
        return enPassantVulnerable;
    }

    // Retorna a matriz de peças de xadrez correspondente ao tabuleiro atual (usado pela UI)
    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }
        return mat;
    }

    // Retorna a matriz de movimentos possíveis para uma peça em uma posição de origem
    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    // Executa a jogada movendo a peça da origem para o destino
    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();

        validateSourcePosition(source);
        validateTargetPosition(source, target);

        // 1. Faz a jogada fisicamente
        Piece capturedPiece = makeMove(source, target);

        // 2. Regra de Ouro: O jogador não pode se colocar em xeque!
        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece); // Desfaz o movimento ilegal
            throw new ChessException("Você não pode se colocar em xeque.");
        }

        // 3. Verifica se essa jogada colocou o oponente em xeque
        check = testCheck(opponent(currentPlayer));

        // 4. Verifica se essa jogada causou um xeque-mate no oponente
        if (testCheckMate(opponent(currentPlayer))) {
            checkMate = true;
        } else {
            nextTurn(); // Só passa o turno se o jogo não acabou
        }

        // #Movimento Especial: En Passant
        ChessPiece movedPiece = (ChessPiece) board.piece(target);
        if (movedPiece instanceof Pawn && (target.getRow() == source.getRow() - 2 || target.getRow() == source.getRow() + 2)) {
            enPassantVulnerable = movedPiece;
        } else {
            enPassantVulnerable = null;
        }

        return (ChessPiece) capturedPiece;
    }

    // Método auxiliar que realiza o movimento físico na matriz do tabuleiro
    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        // #Movimento Especial: En Passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == null) {
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(target.getRow() + 1, target.getColumn());
                } else {
                    pawnPosition = new Position(target.getRow() - 1, target.getColumn());
                }
                capturedPiece = board.removePiece(pawnPosition);
            }
        }

        // #Movimento Especial: Roque Pequeno (Lado do Rei)
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        // #Movimento Especial: Roque Grande (Lado da Rainha)
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(sourceT);
            board.placePiece(rook, targetT);
            rook.increaseMoveCount();
        }

        return capturedPiece;
    }

    // NOVO MÉTODO: Desfaz um movimento (crucial para testar jogadas hipotéticas)
    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount(); //
        board.placePiece(p, source);
        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
        }

        // #Movimento Especial: En Passant
        if (p instanceof Pawn) {
            if (source.getColumn() != target.getColumn() && capturedPiece == enPassantVulnerable) {
                ChessPiece pawn = (ChessPiece) board.removePiece(target);
                Position pawnPosition;
                if (p.getColor() == Color.WHITE) {
                    pawnPosition = new Position(3, target.getColumn());
                } else {
                    pawnPosition = new Position(4, target.getColumn());
                }
                board.placePiece(pawn, pawnPosition);
            }
        }

        // #Movimento Especial: Desfazer Roque Pequeno
        if (p instanceof King && target.getColumn() == source.getColumn() + 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() + 3);
            Position targetT = new Position(source.getRow(), source.getColumn() + 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }

        // #Movimento Especial: Desfazer Roque Grande
        if (p instanceof King && target.getColumn() == source.getColumn() - 2) {
            Position sourceT = new Position(source.getRow(), source.getColumn() - 4);
            Position targetT = new Position(source.getRow(), source.getColumn() - 1);
            ChessPiece rook = (ChessPiece) board.removePiece(targetT);
            board.placePiece(rook, sourceT);
            rook.decreaseMoveCount();
        }
    }

    // Valida se o jogador escolheu uma peça válida para mover
    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("Não existe peça na posição de origem.");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("A peça escolhida não é sua.");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("Não existem movimentos possíveis para a peça escolhida.");
        }
    }

    // Valida se a posição de destino é um movimento válido para a peça de origem
    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMove(target)) {
            throw new ChessException("A peça escolhida não pode se mover para a posição de destino.");
        }
    }

    // Passa o turno para o próximo jogador
    private void nextTurn() {
        turn++;
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // Retorna quem é o oponente da cor informada
    private Color opponent(Color color) {
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    // NOVO MÉTODO: Varre o tabuleiro para encontrar a posição do Rei de uma cor específica
    private Position findKingPosition(Color color) {
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                Piece p = board.piece(i, j);
                if (p instanceof King && ((ChessPiece) p).getColor() == color) {
                    return new Position(i, j);
                }
            }
        }
        throw new IllegalStateException("Não existe o rei " + color + " no tabuleiro.");
    }

    // NOVO MÉTODO: Testa se o Rei de uma determinada cor está sob ataque de alguma peça inimiga
    private boolean testCheck(Color color) {
        Position kingPosition = findKingPosition(color);

        // Varre o tabuleiro inteiro procurando peças do oponente
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                Piece p = board.piece(i, j);
                if (p != null && ((ChessPiece) p).getColor() == opponent(color)) {
                    boolean[][] mat = p.possibleMoves();
                    // Se alguma peça inimiga puder se mover para a casa do Rei, é XEQUE!
                    if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // NOVO MÉTODO: Testa se o jogador não tem NENHUMA jogada que o salve do Xeque
    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false; // Se não está em xeque, não pode ser xeque-mate
        }

        // Varre o tabuleiro procurando todas as peças do jogador que está em xeque
        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                Piece p = board.piece(i, j);
                if (p != null && ((ChessPiece) p).getColor() == color) {
                    boolean[][] mat = p.possibleMoves();

                    // Testa cada movimento possível dessa peça para ver se algum deles salva o Rei
                    for (int r = 0; r < board.getRows(); r++) {
                        for (int c = 0; c < board.getColumns(); c++) {
                            if (mat[r][c]) {
                                Position source = new Position(i, j);
                                Position target = new Position(r, c);

                                // Simula o movimento
                                Piece capturedPiece = makeMove(source, target);
                                boolean stillInCheck = testCheck(color);
                                undoMove(source, target, capturedPiece); // Desfaz a simulação

                                if (!stillInCheck) {
                                    return false; // Achou uma saída! O Rei consegue se salvar, logo NÃO é xeque-mate.
                                }
                            }
                        }
                    }
                }
            }
        }
        return true; // Tentou todas as peças e nenhuma salvou o Rei: XEQUE-MATE!
    }

    // Método utilitário para colocar peças passando a coordenada do xadrez (ex: 'b', 4)
    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
    }

    // Configuração inicial do tabuleiro: coloca as 32 peças nas posições corretas
    private void initialSetup() {
        // --- PEÇAS BRANCAS ---
        placeNewPiece('a', 1, new Rook(board, Color.WHITE));
        placeNewPiece('b', 1, new Knight(board, Color.WHITE));
        placeNewPiece('c', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('d', 1, new Queen(board, Color.WHITE));
        placeNewPiece('e', 1, new King(board, Color.WHITE, this)); // Passando 'this' para o Rei
        placeNewPiece('f', 1, new Bishop(board, Color.WHITE));
        placeNewPiece('g', 1, new Knight(board, Color.WHITE));
        placeNewPiece('h', 1, new Rook(board, Color.WHITE));

        for (char c = 'a'; c <= 'h'; c++) {
            placeNewPiece(c, 2, new Pawn(board, Color.WHITE, this));
        }

        // --- PEÇAS PRETAS ---
        placeNewPiece('a', 8, new Rook(board, Color.BLACK));
        placeNewPiece('b', 8, new Knight(board, Color.BLACK));
        placeNewPiece('c', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('d', 8, new Queen(board, Color.BLACK));
        placeNewPiece('e', 8, new King(board, Color.BLACK, this)); // Passando 'this' para o Rei
        placeNewPiece('f', 8, new Bishop(board, Color.BLACK));
        placeNewPiece('g', 8, new Knight(board, Color.BLACK));
        placeNewPiece('h', 8, new Rook(board, Color.BLACK));

        for (char c = 'a'; c <= 'h'; c++) {
            placeNewPiece(c, 7, new Pawn(board, Color.BLACK, this));
        }
    }

    public boolean isCheckMate() {
        return checkMate;
    }
}