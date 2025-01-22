import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a chessboard for the game.
 * The class stores information about the board's size, the chess slots,
 * and the piece icons for each team. It also provides methods to manipulate
 * the board, such as adding pieces, reversing the board, and getting slot details.
 */
public class KwazamChessBoard {

    private int width;
    private int height;
    private final ArrayList<BoardSquare> chessSlots = new ArrayList<>();
    private final ArrayList<String> blueIcons = new ArrayList<>();  // Icons for blue team
    private final ArrayList<String> redIcons = new ArrayList<>();   // Icons for red team

    // Default constructor that sets the board size to 5x8 and initializes the chess slots.
    public KwazamChessBoard() {
        this(5, 8);  // Set default size (5x8) and initialize slots
    }

    // Custom constructor that sets the board size to the specified width and height
    // and initializes the chess slots.
    public KwazamChessBoard(int width, int height) {
        setBoardSize(width, height);
        addChessSlots();
    }

    // Clears all the slots in the chessboard and creates new empty slots.
    public void clear() {
        chessSlots.clear();
        addChessSlots();
    }

    // Sets the size of the chessboard.
    public void setBoardSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    // Creates and adds new chess slots to the chessboard based on the current width and height.
    private void addChessSlots() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                BoardSquare slot = new BoardSquare(i, j);
                chessSlots.add(slot);
            }
        }
    }

    // Adds a chess piece to a specific slot on the chessboard.
    public void addChessPiece(int x, int y, ChessPiece piece) {
        chessSlots.get(x * width + y).assignPiece(piece);
    }

    // Adds a piece icon for the red team.
    public void addRedIcon(String iconName) {
        redIcons.add(iconName);
    }

    // Adds a piece icon for the blue team.
    public void addBlueIcon(String iconName) {
        blueIcons.add(iconName);
    }

    // Gets a chess slot from the list by its index.
    public BoardSquare getSlot(int i) {
        return chessSlots.get(i);
    }

    // Gets a chess slot from the list by its (x, y) position.
    public BoardSquare getSlot(int x, int y) {
        return chessSlots.get(x * width + y);
    }

    // Reverses the order of the slots on the chessboard. This is used to flip the chessboard
    // after each move by a player, such as reversing the board when switching players.
    public void reverse() {
        Collections.reverse(chessSlots);
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                BoardSquare slot = chessSlots.get((i * width) + j);
                slot.setRowPosition(i);
                slot.setColPosition(j);
            }
        }
    }

    // Gets the file path for the icon of a given piece.
    public String getIcon(String icon) {
        return "Assets/" + icon + ".png";
    }

    // Gets the width of the chessboard.
    public int getWidth() {
        return width;
    }

    // Gets the height of the chessboard.
    public int getHeight() {
        return height;
    }

    // Gets the total number of slots on the chessboard (i.e., board size).
    public int getBoardSize() {
        return height * width;
    }

    // Gets the list of all chess slots on the board.
    public List<BoardSquare> getSlots() {
        return Collections.unmodifiableList(chessSlots);
    }
}