package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor teamTurn;
    private ChessBoard board;

    public ChessGame() {
        board = new ChessBoard();
        setTeamTurn(TeamColor.WHITE);
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK;

        public String toString() {
            if (this == WHITE) {
                return "white";
            }
            return "black";
        }
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // find what piece is at startPosition
        ChessPiece piece = board.getPiece(startPosition);
        // throw an error if there isn't a piece there
        if (piece == null) {
            return null;
        }
        HashSet<ChessMove> possible = (HashSet<ChessMove>) piece.pieceMoves(board, startPosition);
        Collection<ChessMove> moves = new HashSet<>();
        // iterate through possible, for all that are valid and don't leave you in check, add them to moves
        for (ChessMove move : possible) { // somehow this works as a for loop
            ChessPiece temp = board.getPiece(move.getEndPosition());
            board.addPiece(startPosition, null);
            board.addPiece(move.getEndPosition(), piece);
            if (!isInCheck(piece.getTeamColor())) {
                moves.add(move);
            }
            board.addPiece(move.getEndPosition(), temp);
            board.addPiece(startPosition, piece);
        }
        return moves;
        // throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // verify it is your turn
        if (board.getTeamOfSquare(move.getStartPosition()) != getTeamTurn()) {
            throw new InvalidMoveException("Not your turn");
        }
        // run validMoves function
        Collection<ChessMove> availableMoves = validMoves(move.getStartPosition());
        // if valid moves is empty, throw error
        if (availableMoves == null) {
            throw new InvalidMoveException("No legal moves available");
        }
        if (!availableMoves.contains(move)) {
            throw new InvalidMoveException("Not a legal move");
        }
        // move piece
        board.addPiece(move.getEndPosition(), board.getPiece(move.getStartPosition()));
        board.addPiece(move.getEndPosition(), null);
        // if pawn, check for promotion
        if (move.getPromotionPiece() != null) {
            // is pawn, set promotion
            ChessPiece promPiece = new ChessPiece(getTeamTurn(), move.getPromotionPiece());
            board.addPiece(move.getEndPosition(), promPiece);
        }
        // switch team turn
        if (getTeamTurn() == TeamColor.WHITE) {
            setTeamTurn(TeamColor.BLACK);
        }
        else {
            setTeamTurn(TeamColor.WHITE);
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        // find king
        ChessPosition king = null;
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                ChessPosition temp = new ChessPosition(x, y);
                if ((board.getPiece(temp) != null) && (board.getPiece(temp).getTeamColor() == teamColor) && (board.getPiece(temp).getPieceType() == ChessPiece.PieceType.KING)) {
                    king = temp;
                    break;
                }
            }
        }
        // ChessPosition king = new ChessPosition(x, y);

        // look through all enemy pieces
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                // check for piece at position and verify it isn't same color
                ChessPosition temp = new ChessPosition(x, y);
                if ((board.getPiece(temp) != null) && (board.getPiece(temp).getTeamColor() != teamColor)) {
                    // get all moves
                    ChessPiece enemy = board.getPiece(temp);
                    Collection<ChessMove> moves = enemy.pieceMoves(board, temp);
                    // iterate through moves, if any ends on position "king" return true
                    Iterator<ChessMove> iter = moves.iterator();
                    while (iter.hasNext()) {
                        ChessMove curr = iter.next();
                        if (curr.getEndPosition() == king) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
        // throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // cycle through all squares
        for (int x = 1; x < 9; x++) {
            for (int y = 1; y < 9; y++) {
                // if piece has valid move and is correct color, return false
                ChessPosition temp = new ChessPosition(x, y);
                if (board.getPiece(temp) != null) {
                    // this isn't an empty square
                    if (board.getPiece(temp).getTeamColor() == teamColor) {
                        if (validMoves(temp) != null) {
                            return false;
                        }
                    }
                }
            }
        }
        // return true
        return true;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor){
        return isInCheck(teamColor) && isInStalemate(teamColor);
    }


    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", board=" + board +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return teamTurn == chessGame.teamTurn && Objects.equals(board, chessGame.board);
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamTurn, board);
    }
}
