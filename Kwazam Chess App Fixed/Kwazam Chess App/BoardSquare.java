/**
 * Represents a board square on a chessboard with coordinates and a piece.
 */
public class BoardSquare {
    private ChessPiece placedPiece;
    private Integer rowPosition;
    private Integer colPosition;
    private boolean isHighlighted;
    private boolean isValidMove;

    public BoardSquare(int row, int col) {
        this.rowPosition = row;
        this.colPosition = col;
        this.placedPiece = null;
        this.isHighlighted = false;
        this.isValidMove = false;
    }

    public BoardSquare(int row, int col, ChessPiece piece) {
        this.rowPosition = row;
        this.colPosition = col;
        this.placedPiece = piece;
        this.isHighlighted = false;
        this.isValidMove = false;
    }

    public ChessPiece getPlacedPiece() {
        return placedPiece;
    }
    
    public void setPlacedPiece(ChessPiece piece) {
        this.placedPiece = piece;
    }

    public void assignPiece(ChessPiece piece) {
        this.placedPiece = piece;
    }

    public Integer getRowPosition() {
        return rowPosition;
    }

    public Integer getColPosition() {
        return colPosition;
    }

    public boolean hasPiece() {
        return placedPiece != null;
    }
    
    public void setRowPosition(int row) {
        this.rowPosition = row;
    }

    public void setColPosition(int col) {
        this.colPosition = col;
    }

    public boolean isHighlighted() {
        return isHighlighted;
    }

    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public void setValidMove(boolean validMove) {
        isValidMove = validMove;
    }

    public void clearHighlights() {
        isHighlighted = false;
        isValidMove = false;
    }

    @Override
    public String toString() {
        return "BoardSquare{" +
                "row=" + rowPosition +
                ", col=" + colPosition +
                ", highlighted=" + isHighlighted +
                ", validMove=" + isValidMove +
                '}';
    }
}
