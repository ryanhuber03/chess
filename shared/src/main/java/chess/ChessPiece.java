package chess;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private ChessGame.TeamColor pieceColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (type == PieceType.KNIGHT) {
            // knight moves
            return knight(board, myPosition);
        }
        else if (type == PieceType.KING) {
            // king moves
            return king(board, myPosition);
        }
        else if (type == PieceType.QUEEN) {
            // queen moves
            Collection<ChessMove> moves = straight(board, myPosition);
            Collection<ChessMove> temp = diagonal(board, myPosition);
            moves.addAll(temp);
            return moves;
        }
        else if (type == PieceType.PAWN) {
            // pawn moves
            return pawn(board, myPosition);
        }
        else if (type == PieceType.BISHOP) {
            // bishop moves
            return diagonal(board, myPosition);
        }
        else if (type == PieceType.ROOK) {
            // rook moves
            return straight(board, myPosition);
        }
        return null;
    }

    // method that gives all moves of distance 1
    private Collection<ChessMove> king (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        // check all 8 moves of 1 space
        if (row < 8) {
            // all 3 moves where row is increased can be considered
            if (isAvaliableMove(row + 1, col, board)) {
                moves.add(new ChessMove(position, new ChessPosition(row + 1, col), null));
            }
            if (col < 8) {
                // increasing both is legal
                if (isAvaliableMove(row + 1, col + 1, board)) {
                    moves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), null));
                }
            }
            if (col > 1) {
                // decreasing col is legal
                if (isAvaliableMove(row + 1, col - 1, board)) {
                    moves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), null));
                }
            }
        }
        if (row > 1) {
            // all 3 moves where row is decreased can be considered
            if (isAvaliableMove(row - 1, col, board)) {
                moves.add (new ChessMove(position, new ChessPosition(row - 1, col), null));
            }
            if (col < 8) {
                // col can be increased
                if (isAvaliableMove(row - 1, col + 1, board)) {
                    moves.add(new ChessMove(position, new ChessPosition(row - 1, col + 1), null));
                }
            }
            if (col > 1) {
                // col can be decreased
                if (isAvaliableMove(row - 1, col - 1, board)) {
                    moves.add(new ChessMove(position, new ChessPosition(row - 1, col - 1), null));
                }
            }
        }
        if (col < 8) {
            // get right side
            if (isAvaliableMove(row, col + 1, board)) {
                moves.add(new ChessMove(position, new ChessPosition(row, col + 1), null));
            }
        }
        if (col > 1) {
            // get left side
            if (isAvaliableMove(row, col - 1, board)){
                moves.add(new ChessMove(position, new ChessPosition(row, col - 1), null));
            }
        }
        return moves;
    }

    private boolean isAvaliableMove (int row, int col, ChessBoard board) {
        if (board.getPiece(new ChessPosition(row, col)) == null) {
            return true;
        }
        if (board.getPiece(new ChessPosition(row, col)).getTeamColor() != pieceColor) {
            return true;
        }
        return false;
    }

    // method that gives all moves that are diagonal
    private Collection<ChessMove> diagonal (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        // look at increasing rows and columns until one is occupied, add that one too if the piece is the opposite color
        int x = 1;
        while (row + x < 9 && col + x < 9) {
            ChessPosition temp = new ChessPosition(row + x, col + x);
            ChessPiece piece = board.getPiece(temp);
            if (piece == null) {
                moves.add (new ChessMove(position, temp, null));
                x++;
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add (new ChessMove(position, temp, null));
                break;
            } else {
                break;
            }
        }
        // look at increasing rows and decreasing cols until one is occupied, add that one too if the piece is the opposite color
        x = 1;
        while (row + x < 9 && col - x > 0) {
            ChessPosition temp = new ChessPosition(row + x, col - x);
            ChessPiece piece = board.getPiece(temp);
            if (piece == null) {
                moves.add (new ChessMove(position, temp, null));
                x++;
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add (new ChessMove(position, temp, null));
                break;
            } else {
                break;
            }
        }
        // look at decreasing rows and increasing cols until one is occupied, add that one too if the piece is the opposite color
        x = 1;
        while (row - x > 0 && col + x < 9) {
            ChessPosition temp = new ChessPosition(row - x, col + x);
            ChessPiece piece = board.getPiece(temp);
            if (piece == null) {
                moves.add (new ChessMove(position, temp, null));
                x++;
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add (new ChessMove(position, temp, null));
                break;
            } else {
                break;
            }
        }
        // look at decreasing rows and cols until one is occupied, add that one too if the piece is the opposite color
        x = 1;
        while (row - x > 0 && col - x > 0) {
            ChessPosition temp = new ChessPosition(row - x, col - x);
            ChessPiece piece = board.getPiece(temp);
            if (piece == null) {
                moves.add (new ChessMove(position, temp, null));
                x++;
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add (new ChessMove(position, temp, null));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    // method that gives all moves that are straight
    private Collection<ChessMove> straight (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return null;
        }
        // look at increasing rows until one is occupied, add that one too if the piece is the opposite color
        int x = 1;
        while (row + x < 9) {
            ChessPosition temp = new ChessPosition(row + x, col);
            ChessPiece piece = board.getPiece(temp);
            if (piece == null) {
                moves.add (new ChessMove(position, temp, null));
                x++;
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add (new ChessMove(position, temp, null));
                break;
            } else {
                break;
            }
        }
        // look at decreasing rows until one is occupied, add that one too if the piece is the opposite color
        x = 1;
        while (row - x > 0) {
            ChessPosition temp = new ChessPosition(row - x, col);
            ChessPiece piece = board.getPiece(temp);
            if (piece == null) {
                moves.add(new ChessMove(position, temp, null));
                x++;
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add(new ChessMove(position, temp, null));
                break;
            } else {
                break;
            }
        }
        // look at increasing cols until one is occupied, add that one too if the piece is the opposite color
        x = 1;
        while (col + x < 9) {
            ChessPosition temp = new ChessPosition(row, col + x);
            ChessPiece piece = board.getPiece(temp);
            if (piece == null) {
                moves.add (new ChessMove(position, temp, null));
                x++;
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add (new ChessMove(position, temp, null));
                break;
            } else {
                break;
            }
        }
        // look at decreasing cols until one is occupied, add that one too if the piece is the opposite color
        x = 1;
        while (col - x > 0) {
            ChessPosition temp = new ChessPosition(row, col - x);
            ChessPiece piece = board.getPiece(temp);
            if (piece == null) {
                moves.add(new ChessMove(position, temp, null));
                x++;
            } else if (piece.getTeamColor() != pieceColor) {
                moves.add(new ChessMove(position, temp, null));
                break;
            } else {
                break;
            }
        }
        return moves;
    }

    // method for pawn movement, take color into account for direction
    private Collection<ChessMove> pawn (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            if (row > 1 && row < 7) { // move by 1, no promotion, white
                if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                    moves.add (new ChessMove(position, new ChessPosition(row + 1, col), null));
                }
            }
            if (row == 2) { // move by 2, white
                boolean x = board.getPiece(new ChessPosition(row + 1, col)) == null;
                boolean y = board.getPiece(new ChessPosition(row + 2, col)) == null;
                if (x && y) {
                    moves.add( new ChessMove(position, new ChessPosition(row + 2, col), null));
                }
            }
            if (row == 7) { // promotion, white
                if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                    ChessPosition temp = new ChessPosition(row + 1, col);
                    moves.addAll(pawnPromotion(position, temp));
                }
            }
            if (row > 1 && row < 7) { // capturing, white
                ChessPosition temp = new ChessPosition(row + 1, col + 1);
                if (col < 7 && board.getTeamOfSquare(temp) == ChessGame.TeamColor.BLACK) {
                    moves.add (new ChessMove(position, temp, null));
                }
                temp = new ChessPosition(row + 1, col - 1);
                if (col > 1 && board.getTeamOfSquare(temp) == ChessGame.TeamColor.BLACK) {
                    moves.add (new ChessMove(position, temp, null));
                }
            }
            if (row == 7) { // capturing with promotion, white
                ChessPosition temp = new ChessPosition(row + 1, col + 1);
                if (col < 7 && board.getTeamOfSquare(temp) == ChessGame.TeamColor.BLACK) {
                    moves.addAll(pawnPromotion(position, temp));
                }
                temp = new ChessPosition(row + 1, col - 1);
                if (col > 1 && board.getTeamOfSquare(temp) == ChessGame.TeamColor.BLACK) {
                    moves.addAll(pawnPromotion(position, temp));
                }
            }
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK){
            if (row < 8 && row > 2) { // move by 1, no promotion, black
                ChessPosition temp = new ChessPosition(row - 1, col);
                if (board.getPiece(temp) == null) {
                    moves.add (new ChessMove(position, temp, null));
                }
            }
            if (row == 7) { // move by 2, black
                boolean x = board.getPiece(new ChessPosition(row - 1, col)) == null;
                boolean y = board.getPiece(new ChessPosition(row - 2, col)) == null;
                if (x && y) {
                    moves.add(new ChessMove(position, new ChessPosition(row - 2, col), null));
                }
            }
            if (row == 2) { // promotion, black
                if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                    ChessPosition temp = new ChessPosition(row - 1, col);
                    moves.addAll(pawnPromotion(position, temp));
                }
            }
            if (row < 8 && row > 2) { // capturing, black
                ChessPosition temp = new ChessPosition(row - 1, col + 1);
                if (col < 7 && board.getTeamOfSquare(temp) == ChessGame.TeamColor.WHITE) {
                    moves.add (new ChessMove(position, temp, null));
                }
                temp = new ChessPosition(row - 1, col - 1);
                if (col > 1 && board.getTeamOfSquare(temp) == ChessGame.TeamColor.WHITE) {
                    moves.add (new ChessMove(position, temp, null));
                }
            }
            if (row == 2) { // capturing with promotion, black
                ChessPosition temp = new ChessPosition(row - 1, col + 1);
                if (col < 7 && board.getTeamOfSquare(temp) == ChessGame.TeamColor.WHITE) {
                    moves.addAll (pawnPromotion(position, temp));
                }
                temp = new ChessPosition(row - 1, col - 1);
                if (col > 1 && board.getTeamOfSquare(temp) == ChessGame.TeamColor.WHITE) {
                    moves.addAll (pawnPromotion(position, temp));
                }
            }
        }
        return moves;
    }

    private boolean pawnCanCapture(int row, int col, ChessBoard board) {
        ChessPosition temp = new ChessPosition(row, col);
        if (board.getTeamOfSquare(temp) != pieceColor) {
            return true;
        }
        return false;
    }

    private Collection<ChessMove> pawnPromotion (ChessPosition start, ChessPosition end) {
        Collection<ChessMove> moves = new HashSet<ChessMove>();
        moves.add (new ChessMove(start, end, PieceType.ROOK));
        moves.add (new ChessMove(start, end, PieceType.KNIGHT));
        moves.add (new ChessMove(start, end, PieceType.BISHOP));
        moves.add (new ChessMove(start, end, PieceType.QUEEN));
        return moves;
    }

    // method that gives knight movements
    private Collection<ChessMove> knight (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        if (row < 8) {
            if (col < 7) {
                // add move, row + 1, col + 2
                ChessPosition temp = new ChessPosition(row + 1, col + 2);
                if (board.getPiece(temp) == null || pieceColor != board.getTeamOfSquare(temp)) {
                    moves.add(new ChessMove(position, temp, null));
                }
            }
            if (col > 2) {
                // add move, row + 1, col - 2
                ChessPosition temp = new ChessPosition(row + 1, col - 2);
                if (board.getPiece(temp) == null || pieceColor != board.getTeamOfSquare(temp)) {
                    moves.add(new ChessMove(position, temp, null));
                }
            }
        }
        if (row < 7) {
            if (col < 8) {
                // add move, row + 2, col + 1
                ChessPosition temp = new ChessPosition(row + 2, col + 1);
                if (board.getPiece(temp) == null || pieceColor != board.getTeamOfSquare(temp)) {
                    moves.add(new ChessMove(position, temp, null));
                }
            }
            if (col > 1) {
                // add move, row + 2, col - 1
                ChessPosition temp = new ChessPosition(row + 2, col - 1);
                if (board.getPiece(temp) == null || pieceColor != board.getTeamOfSquare(temp)) {
                    moves.add(new ChessMove(position, temp, null));
                }
            }
        }
        if (row > 2) {
            if (col < 8) {
                // add move, row - 2, col + 1
                ChessPosition temp = new ChessPosition(row - 2, col + 1);
                if (pieceColor != board.getTeamOfSquare(temp)) {
                    moves.add(new ChessMove(position, temp, null));
                }
            }
            if (col > 1) {
                // add move, row - 2, col - 1
                ChessPosition temp = new ChessPosition(row - 2, col - 1);
                if (pieceColor != board.getTeamOfSquare(temp)) {
                    moves.add(new ChessMove(position, temp, null));
                }
            }
        }
        if (row > 1) {
            if (col < 7) {
                // add move, row - 1, col + 2
                ChessPosition temp = new ChessPosition(row - 1, col + 2);
                if (pieceColor != board.getTeamOfSquare(temp)) {
                    moves.add(new ChessMove(position, temp, null));
                }
            }
            if (col > 2) {
                // add move, row - 1, col - 2
                ChessPosition temp = new ChessPosition(row - 1, col - 2);
                if (pieceColor != board.getTeamOfSquare(temp)) {
                    moves.add(new ChessMove(position, temp, null));
                }
            }
        }
        return moves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }
    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}