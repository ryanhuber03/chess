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

    /*
    public ChessPiece newPiece (ChessGame.TeamColor pieceColor; ChessPiece.PieceType type) {
        return new ChessPiece(pieceColor, type);
    }
    */

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
            return Knight(board, myPosition);
        }
        else if (type == PieceType.KING) {
            // king moves
            return King(board, myPosition);
        }
        else if (type == PieceType.QUEEN) {
            // queen moves
            Collection<ChessMove> moves = Straight(board, myPosition);
            Collection<ChessMove> temp = Diagonal(board, myPosition);
            moves.addAll(temp);
            return moves;
        }
        else if (type == PieceType.PAWN) {
            // pawn moves
            return Pawn(board, myPosition);
        }
        else if (type == PieceType.BISHOP) {
            // bishop moves
            return Diagonal(board, myPosition);
        }
        else if (type == PieceType.ROOK) {
            // rook moves
            return Straight(board, myPosition);
        }
        /*
        else {
            throw new RuntimeException("No Piece Found");
        }
        */
        return null;
    }

    // for all these, must verify that we aren't passing through pieces or capturing own pieces

    // method that gives all moves of distance 1
    private Collection<ChessMove> King (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        // check all 8 moves of 1 space
        if (row < 8) {
            // all 3 moves where row is increased can be considered
            if (board.getPiece(new ChessPosition(row + 1, col)) == null || board.getPiece(new ChessPosition(row + 1, col)).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(position, new ChessPosition(row + 1, col), null));
            }
            if (col < 8) {
                // increasing both is legal
                if (board.getPiece(new ChessPosition(row + 1, col + 1)) == null || board.getPiece(new ChessPosition(row + 1, col + 1)).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(position, new ChessPosition(row + 1, col + 1), null));
                }
            }
            if (col > 1) {
                // decreasing col is legal
                if (board.getPiece(new ChessPosition(row + 1, col - 1)) == null || board.getPiece(new ChessPosition(row + 1, col - 1)).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(position, new ChessPosition(row + 1, col - 1), null));
                }
            }
        }
        if (row > 1) {
            // all 3 moves where row is decreased can be considered
            if (board.getPiece(new ChessPosition(row - 1, col)) == null || board.getPiece(new ChessPosition(row - 1, col)).getTeamColor() != pieceColor) {
            moves.add (new ChessMove(position, new ChessPosition(row - 1, col), null));
            }
            if (col < 8) {
                // col can be increased
                if (board.getPiece(new ChessPosition(row - 1, col + 1)) == null || board.getPiece(new ChessPosition(row - 1, col + 1)).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(position, new ChessPosition(row - 1, col + 1), null));
                }
            }
            if (col > 1) {
                // col can be decreased
                if (board.getPiece(new ChessPosition(row - 1, col - 1)) == null || board.getPiece(new ChessPosition(row - 1, col - 1)).getTeamColor() != pieceColor) {
                    moves.add(new ChessMove(position, new ChessPosition(row - 1, col - 1), null));
                }
            }
        }
        if (col < 8) {
            // get right side
            if (board.getPiece(new ChessPosition(row, col + 1)) == null || board.getPiece(new ChessPosition(row, col + 1)).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(position, new ChessPosition(row, col + 1), null));
            }
        }
        if (col > 1) {
            // get left side
            if (board.getPiece(new ChessPosition(row, col - 1)) == null || board.getPiece(new ChessPosition(row, col - 1)).getTeamColor() != pieceColor) {
                moves.add(new ChessMove(position, new ChessPosition(row, col - 1), null));
            }
        }


        return moves;
    }

    // method that gives all moves that are diagonal
    private Collection<ChessMove> Diagonal (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        int x = 1;

        // look at increasing rows and columns until one is occupied, add that one too if the piece is the opposite color
        while (board.getPiece(new ChessPosition(row + x, col + x)) != null) {
            if (row + x > 8 || col + x > 8) {
                break;
            }
            moves.add (new ChessMove(position, new ChessPosition(row + x, col + x), null));
            x++;
        }
        if (row + x < 9 && (board.getPiece(new ChessPosition(row + x, col + x)) == null || board.getPiece(new ChessPosition(row + x, col + x)).getTeamColor() != pieceColor)) {
            // capture piece
            moves.add (new ChessMove(position, new ChessPosition(row + x, col + x), null));
        }

        // look at increasing rows and decreasing cols until one is occupied, add that one too if the piece is the opposite color
        while (board.getPiece(new ChessPosition(row + x, col - x)) != null) {
            if (row + x > 8 || col - x < 1) {
                break;
            }
            moves.add (new ChessMove(position, new ChessPosition(row + x, col - x), null));
            x++;
        }
        if (row + x < 9 && (board.getPiece(new ChessPosition(row + x, col - x)) == null || board.getPiece(new ChessPosition(row + x, col - x)).getTeamColor() != pieceColor)) {
            // capture piece
            moves.add (new ChessMove(position, new ChessPosition(row + x, col - x), null));
        }

        // look at decreasing rows and increasing cols until one is occupied, add that one too if the piece is the opposite color
        while (board.getPiece(new ChessPosition(row + x, col)) != null) {
            if (row - x < 1 || col + x > 8) {
                break;
            }
            moves.add (new ChessMove(position, new ChessPosition(row - x, col + x), null));
            x++;
        }
        if (row + x < 9 && (board.getPiece(new ChessPosition(row - x, col + x)) == null || board.getPiece(new ChessPosition(row - x, col + x)).getTeamColor() != pieceColor)) {
            // capture piece
            moves.add (new ChessMove(position, new ChessPosition(row - x, col + x), null));
        }

        // look at decreasing rows and cols until one is occupied, add that one too if the piece is the opposite color
        while (board.getPiece(new ChessPosition(row + x, col)) != null) {
            if (row + x < 1 || col + x < 1) {
                break;
            }
            moves.add (new ChessMove(position, new ChessPosition(row - x, col - x), null));
            x++;
        }
        if (row + x < 9 && (board.getPiece(new ChessPosition(row - x, col - x)) == null || board.getPiece(new ChessPosition(row - x, col - x)).getTeamColor() != pieceColor)) {
            // capture piece
            moves.add (new ChessMove(position, new ChessPosition(row - x, col - x), null));
        }

        return moves;
    }

    // method that gives all moves that are straight
    private Collection<ChessMove> Straight (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        int x = 1;
        // look at increasing rows until one is occupied, add that one too if the piece is the opposite color
        while (board.getPiece(new ChessPosition(row + x, col)) != null) {
            if (row + x > 8) {
                break;
            }
            moves.add (new ChessMove(position, new ChessPosition(row + x, col), null));
            x++;
        }
        if (row + x < 9 && (board.getPiece(new ChessPosition(row + x, col)) == null || board.getPiece(new ChessPosition(row + x, col)).getTeamColor() != pieceColor)) {
            // capture piece
            moves.add (new ChessMove(position, new ChessPosition(row + x, col), null));
        }

        // look at decreasing rows until one is occupied, add that one too if the piece is the opposite color
        x = 0;
        while (board.getPiece(new ChessPosition(row - x, col)) != null) {
            if (row - x < 1) {
                break;
            }
            moves.add (new ChessMove(position, new ChessPosition(row - x, col), null));
            x++;
        }
        if (row - x > 0 && (board.getPiece(new ChessPosition(row - x, col)) == null || board.getPiece(new ChessPosition(row - x, col)).getTeamColor() != pieceColor)) {
            // capture piece
            moves.add (new ChessMove(position, new ChessPosition(row - x, col), null));
        }

        // look at increasing cols until one is occupied, add that one too if the piece is the opposite color
        x = 0;
        while (board.getPiece(new ChessPosition(row, col + x)) != null) {
            if (col + x > 8) {
                break;
            }
            moves.add (new ChessMove(position, new ChessPosition(row, col + x), null));
            x++;
        }
        if (col + x < 9 && (board.getPiece(new ChessPosition(row, col + x)) == null || board.getPiece(new ChessPosition(row, col + x)).getTeamColor() != pieceColor)) {
            // capture piece
            moves.add (new ChessMove(position, new ChessPosition(row, col + x), null));
        }

        // look at decreasing cols until one is occupied, add that one too if the piece is the opposite color
        x = 0;
        while (board.getPiece(new ChessPosition(row, col - x)) != null) {
            if (col - x < 1) {
                break;
            }
            moves.add (new ChessMove(position, new ChessPosition(row, col - x), null));
            x++;
        }
        if (col - x > 0 && (board.getPiece(new ChessPosition(row, col - x)) == null || board.getPiece(new ChessPosition(row, col - x)).getTeamColor() != pieceColor)) {
            // capture piece
            moves.add (new ChessMove(position, new ChessPosition(row, col - x), null));
        }

        return moves;
    }

    // method for pawn advancement, take color into account for direction
    private Collection<ChessMove> Pawn (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        // verify color
        if (pieceColor == ChessGame.TeamColor.WHITE) {
            // move to increasing rows
            // move by 1, no promotion, white
            if (row > 1 && row < 7) {
                if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                    moves.add (new ChessMove(position, new ChessPosition(row + 1, col), null));
                }
            }
            // move by 2, white
            if (row == 2) {
                if (board.getPiece(new ChessPosition(row + 1, col)) == null && board.getPiece(new ChessPosition(row + 2, col)) == null) {
                    moves.add( new ChessMove(position, new ChessPosition(row + 2, col), null));
                }
            }
            // promotion, white
            if (row == 7) {
                if (board.getPiece(new ChessPosition(row + 1, col)) == null) {
                    ChessPosition temp = new ChessPosition(row + 1, col);
                    // Rook, Knight, Bishop, Queen
                    moves.add (new ChessMove(position, temp, PieceType.ROOK));
                    moves.add (new ChessMove(position, temp, PieceType.KNIGHT));
                    moves.add (new ChessMove(position, temp, PieceType.BISHOP));
                    moves.add (new ChessMove(position, temp, PieceType.QUEEN));
                }
            }

            // capturing, white
            if (row > 1 && row < 7) {
                if (col < 7 && board.getTeamOfSquare(new ChessPosition(row + 1, col + 1)) == ChessGame.TeamColor.BLACK) {
                    // capture right
                    moves.add (new ChessMove(position, new ChessPosition(row + 1, col + 1), null));
                }
                if (col > 1 && board.getTeamOfSquare(new ChessPosition(row + 1, col - 1)) == ChessGame.TeamColor.BLACK) {
                    // capture left
                    moves.add (new ChessMove(position, new ChessPosition(row + 1, col - 1), null));
                }
            }

            // capturing with promotion, white
            if (row == 7) {
                if (col < 7 && board.getTeamOfSquare(new ChessPosition(row + 1, col + 1)) == ChessGame.TeamColor.BLACK) {
                    // capture right
                    ChessPosition temp = new ChessPosition(row + 1, col + 1);
                    // Rook, Knight, Bishop, Queen
                    moves.add (new ChessMove(position, temp, PieceType.ROOK));
                    moves.add (new ChessMove(position, temp, PieceType.KNIGHT));
                    moves.add (new ChessMove(position, temp, PieceType.BISHOP));
                    moves.add (new ChessMove(position, temp, PieceType.QUEEN));
                }
                if (col > 1 && board.getTeamOfSquare(new ChessPosition(row + 1, col - 1)) == ChessGame.TeamColor.BLACK) {
                    // capture left
                    ChessPosition temp = new ChessPosition(row + 1, col - 1);
                    // Rook, Knight, Bishop, Queen
                    moves.add (new ChessMove(position, temp, PieceType.ROOK));
                    moves.add (new ChessMove(position, temp, PieceType.KNIGHT));
                    moves.add (new ChessMove(position, temp, PieceType.BISHOP));
                    moves.add (new ChessMove(position, temp, PieceType.QUEEN));
                }
            }
        }
        else if (pieceColor == ChessGame.TeamColor.BLACK){
            // move to decreasing rows
            // move by 1, no promotion, black
            if (row < 7 && row > 2) {
                if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                    moves.add (new ChessMove(position, new ChessPosition(row - 1, col), null));
                }
            }
            // move by 2, black
            if (row == 7) {
                if (board.getPiece(new ChessPosition(row - 1, col)) == null && board.getPiece(new ChessPosition(row - 2, col)) == null) {
                    moves.add(new ChessMove(position, new ChessPosition(row - 2, col), null));
                }
            }
            // promotion, black
            if (row == 2) {
                if (board.getPiece(new ChessPosition(row - 1, col)) == null) {
                    ChessPosition temp = new ChessPosition(row - 1, col);
                    // Rook, Knight, Bishop, Queen
                    moves.add (new ChessMove(position, temp, PieceType.ROOK));
                    moves.add (new ChessMove(position, temp, PieceType.KNIGHT));
                    moves.add (new ChessMove(position, temp, PieceType.BISHOP));
                    moves.add (new ChessMove(position, temp, PieceType.QUEEN));
                }
            }

            // capturing, black
            if (row < 8 && row > 2) {
                if (col < 7 && board.getTeamOfSquare(new ChessPosition(row - 1, col + 1)) == ChessGame.TeamColor.WHITE) {
                    // capture right
                    moves.add (new ChessMove(position, new ChessPosition(row - 1, col + 1), null));
                }
                if (col > 1 && board.getTeamOfSquare(new ChessPosition(row - 1, col - 1)) == ChessGame.TeamColor.WHITE) {
                    // capture left
                    moves.add (new ChessMove(position, new ChessPosition(row - 1, col - 1), null));
                }
            }

            // capturing with promotion, black
            if (row == 2) {
                if (col < 7 && board.getTeamOfSquare(new ChessPosition(row - 1, col + 1)) == ChessGame.TeamColor.WHITE) {
                    // capture right
                    ChessPosition temp = new ChessPosition(row - 1, col + 1);
                    // Rook, Knight, Bishop, Queen
                    moves.add (new ChessMove(position, temp, PieceType.ROOK));
                    moves.add (new ChessMove(position, temp, PieceType.KNIGHT));
                    moves.add (new ChessMove(position, temp, PieceType.BISHOP));
                    moves.add (new ChessMove(position, temp, PieceType.QUEEN));
                }
                if (col > 1 && board.getTeamOfSquare(new ChessPosition(row - 1, col - 1)) == ChessGame.TeamColor.WHITE) {
                    // capture left
                    ChessPosition temp = new ChessPosition(row - 1, col - 1);
                    // Rook, Knight, Bishop, Queen
                    moves.add (new ChessMove(position, temp, PieceType.ROOK));
                    moves.add (new ChessMove(position, temp, PieceType.KNIGHT));
                    moves.add (new ChessMove(position, temp, PieceType.BISHOP));
                    moves.add (new ChessMove(position, temp, PieceType.QUEEN));
                }
            }
        }
        return moves;
    }
    // method for pawn capture, take color into account for direction

    // method for pawn promotion

    // method that gives knight movements
    private Collection<ChessMove> Knight (ChessBoard board, ChessPosition position) {
        Collection<ChessMove> moves = new HashSet<>();
        int row = position.getRow();
        int col = position.getColumn();
        if (row < 1 || row > 8 || col < 1 || col > 8) {
            return moves;
        }
        // {1,2}, {2,1}, {-1,-2}, {-2,-1}, {1,-2}, {-2,1}, {-1,2}, {2,-1}
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
                if (board.getPiece(temp) == null || pieceColor != board.getTeamOfSquare(temp))) {
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
