import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.IntStream;

/**
 * Represents the Kwazam Chess game.
 * Manages the game state, player turns, piece movements, and file operations for saving/loading the game.
 */
public class KwazamChess {
    public KwazamChessBoard chessboard;
    private Player player1;
    private Player player2;
    private final List<Player> playerList = new ArrayList<>();

    private static ChessPiece queue = null;
    private static BoardSquare temp = null;
    private static boolean hasWinner;
    private static boolean canMove = false;
    private static String type;
    private static int fromX, fromY, toX, toY, x, y;
    private int playerTurnNum = 0;

    // Constructor
    public KwazamChess() {
        chessboard = new KwazamChessBoard();
        player1 = new Player("B"); // Blue
        player2 = new Player("R"); // Red
        playerList.add(player2);
        playerList.add(player1);
        setPlayerTurnNum(0); // Initialize turn number
    }

    // Restart the game
    public void restart() {
        chessboard.clear();
        setupPieces();
        setPlayerTurnNum(0);
        hasWinner = false;
    }

    // Save current progress into a .txt file
    public void save() throws IOException {
    File file = new File("gameData.txt");
    try (PrintWriter printWriter = new PrintWriter(file)) {
        // Save board state
        for (int i = 0; i < chessboard.getHeight(); i++) {
            for (int j = 0; j < chessboard.getWidth(); j++) {
                ChessPiece piece = chessboard.getSlot(i * chessboard.getWidth() + j).getPlacedPiece();
                if (piece == null) {
                    printWriter.print("null ");
                } else {
                    String pieceStr = piece.getOwner().getColor() + piece.getPieceName();
                    if (piece.getPieceName().equals("Ram") && piece.hasReachedEnd()) {
                        pieceStr += "ReachEnd";
                    }
                    printWriter.print(pieceStr + " ");
                }
            }
            printWriter.println(); // Add line break after each row
        }
        // Save game state
        printWriter.println(getPlayerTurnNum());
        printWriter.println(getPlayerTurn().getColor());
    }
}

    // Load previous game from .txt file
    public void load() throws FileNotFoundException {
    File file = new File("gameData.txt");
    try (Scanner scanner = new Scanner(file)) {
        chessboard.clear();
        
        // Load board state
        for (int i = 0; i < chessboard.getHeight(); i++) {
            for (int j = 0; j < chessboard.getWidth(); j++) {
                String pieceStr = scanner.next();
                if (!pieceStr.equals("null")) {
                    // Extract color (first character)
                    String color = pieceStr.substring(0, 1);
                    // Extract piece name and check for ReachEnd
                    boolean reachEnd = pieceStr.endsWith("ReachEnd");
                    String pieceName = reachEnd ? 
                        pieceStr.substring(1, pieceStr.length() - 8) : 
                        pieceStr.substring(1);
                    
                    // Create piece with proper owner
                    Player owner = color.equals("B") ? player1 : player2;
                    ChessPiece piece = new ChessPiece(pieceName, owner, reachEnd);
                    chessboard.addChessPiece(i, j, piece);
                }
            }
            if (scanner.hasNextLine()) {
                scanner.nextLine(); // Consume the line break
            }
        }
        
        // Load game state if available
        if (scanner.hasNextLine()) {
            setPlayerTurnNum(Integer.parseInt(scanner.nextLine().trim()));
        }
        hasWinner = false;
    }
}

    // Setup pieces on the board
    public void setupPieces() {
        String[] redArrangement = {"Tor", "Biz", "Sau", "Biz", "Xor"}; // Row 0 (Red team)
        String[] blueArrangement = {"Xor", "Biz", "Sau", "Biz", "Tor"}; // Row 7 (Blue team)
        String ramPiece = "Ram";
        boolean ramHasReachedEnd = false;
        boolean otherPiecesReachedEnd = true;

        IntStream.range(0, chessboard.getHeight()).forEach(i -> {
            IntStream.range(0, chessboard.getWidth()).forEach(j -> {
                if (i == 0 && j < redArrangement.length) {
                    chessboard.addChessPiece(i, j, new ChessPiece(redArrangement[j], player2, otherPiecesReachedEnd));
                } else if (i == 1) {
                    chessboard.addChessPiece(i, j, new ChessPiece(ramPiece, player2, ramHasReachedEnd));
                } else if (i == 6) {
                    chessboard.addChessPiece(i, j, new ChessPiece(ramPiece, player1, ramHasReachedEnd));
                } else if (i == 7 && j < blueArrangement.length) {
                    chessboard.addChessPiece(i, j, new ChessPiece(blueArrangement[j], player1, otherPiecesReachedEnd));
                }
            });
        });
    }

    // Overloaded piece setup method for game loading purpose
    public void setupPiece(int x, int y, String pieceName) {
        boolean reachEnd = false;
        if (pieceName.contains("ReachEnd")) {
            pieceName = pieceName.substring(0, pieceName.length() - 8); // remove "ReachEnd" from the pieceName
            reachEnd = true;
        }
        Player player = pieceName.charAt(0) == 'B' ? player1 : player2;
        chessboard.addChessPiece(x, y, new ChessPiece(pieceName.substring(1), player, reachEnd));
    }

    // Move a piece from a slot to another slot
    public boolean move(BoardSquare slot) {
        if (slot.getPlacedPiece() != null && isMovable(slot)) {
            if (queue == null) {
                type = slot.getPlacedPiece().getPieceName();
                fromX = slot.getRowPosition();
                fromY = slot.getColPosition();
                queue = slot.getPlacedPiece();
                temp = slot;
            } else {
                toX = slot.getRowPosition();
                toY = slot.getColPosition();
                canMove = isValidMove(type, fromX, fromY, toX, toY, queue);
                if (!queue.getOwner().equals(slot.getPlacedPiece().getOwner()) && canMove) {
                    temp.setPlacedPiece(null);
                    slot.setPlacedPiece(queue);
                    queue = null;
                    temp = null;
                    playerTurnNum++;
                    return true;
                }
                queue = null;
                temp = null;
            }
        } else if (temp != null) {
            toX = slot.getRowPosition();
            toY = slot.getColPosition();
            canMove = isValidMove(type, fromX, fromY, toX, toY, queue);
            if (canMove) {
                slot.setPlacedPiece(queue);
                queue = null;
                temp.setPlacedPiece(null);
                temp = null;
                playerTurnNum++;
                return true;
            }
            queue = null;
            temp = null;
        }
        return false;
    }

    // Check whether a piece can be moved in that turn
    public boolean isMovable(BoardSquare slot) {
        return slot.getPlacedPiece().getOwner().equals(getPlayerTurn());
    }

    // Check whether a piece follows its movement rules
    public boolean isValidMove(String type, int fromX, int fromY, int toX, int toY, ChessPiece queue) {
        x = fromX - toX;
        y = fromY - toY;

        switch (type) {
            case "Ram":
                return isValidRamMove(fromY, toX, toY, queue);
            case "Tor":
                return isValidTorMove(fromX, fromY, toX, toY);
            case "Xor":
                return isValidXorMove(fromX, fromY, toX, toY);
            case "Biz":
                return isValidBizMove(x, y);
            case "Sau":
                return isValidSauMove(x, y);
            default:
                return false;
        }
    }

    private boolean isValidRamMove(int fromY, int toX, int toY, ChessPiece queue) {
        if (fromY == toY) {
            if (queue.hasReachedEnd()) {
                if ((x == -1 || (x == -2 && chessboard.getSlot(fromX + 1, fromY).getPlacedPiece() == null))) {
                    if (toX == 7) {
                        queue.setHasReachedEnd(false);
                    }
                    return true;
                }
            } else {
                if (x == 1 || (x == 2 && chessboard.getSlot(fromX - 1, fromY).getPlacedPiece() == null)) {
                    if (toX == 0) {
                        queue.setHasReachedEnd(true);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isValidTorMove(int fromX, int fromY, int toX, int toY) {
        x = Math.abs(fromX - toX);
        y = Math.abs(fromY - toY);
        if (x == 0 && y >= 1) {
            for (int i = 1; i <= y - 1; i++) {
                if (chessboard.getSlot(fromX, fromY - i).getPlacedPiece() != null) {
                    return false;
                }
            }
            return true;
        } else if (x >= 1 && y == 0) {
            for (int i = 1; i <= x - 1; i++) {
                if (chessboard.getSlot(fromX - i, fromY).getPlacedPiece() != null) {
                    return false;
                }
            }
            return true;
        } else if (x == 0 && (fromY - toY) <= -1) {
            for (int i = y - 1; i > 0; i--) {
                if (chessboard.getSlot(fromX, fromY + i).getPlacedPiece() != null) {
                    return false;
                }
            }
            return true;
        } else if ((fromX - toX) <= -1 && y == 0) {
            for (int i = x - 1; i > 0; i--) {
                if (chessboard.getSlot(toX - i, toY).getPlacedPiece() != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValidXorMove(int fromX, int fromY, int toX, int toY) {
        if (Math.abs(x) == Math.abs(y)) {
            for (int i = 1; i < x; i++) {
                if ((toX < fromX && toY > fromY && chessboard.getSlot(fromX - i, fromY + i).getPlacedPiece() != null)
                        || (toX > fromX && toY > fromY && chessboard.getSlot(fromX + i, fromY + i).getPlacedPiece() != null)
                        || (toX < fromX && toY < fromY && chessboard.getSlot(fromX - i, fromY - i).getPlacedPiece() != null)
                        || (toX > fromX && toY < fromY && chessboard.getSlot(fromX + i, fromY - i).getPlacedPiece() != null)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    private boolean isValidBizMove(int x, int y) {
        return ((x == -2 || x == 2) && (y == 1 || y == -1)) || ((x == -1 || x == 1) && (y == -2 || y == 2));
    }

    private boolean isValidSauMove(int x, int y) {
        return (x == -1 || x == 0 || x == 1) && (y == -1 || y == 0 || y == 1);
    }

    // Change the Xor to Tor or vice versa after every 4 turns
    public void changeState() {
        IntStream.range(0, chessboard.getBoardSize()).forEach(i -> {
            BoardSquare slot = chessboard.getSlot(i);
            ChessPiece piece = slot.getPlacedPiece();
            if (piece != null) {
                if (piece.getPieceName().equals("Xor")) {
                    piece.setPieceName("Tor");
                } else if (piece.getPieceName().equals("Tor")) {
                    piece.setPieceName("Xor");
                }
            }
        });
    }

    // Check whether a team has won the game
    public String getWinner() {
        long numOfSau = chessboard.getSlots().stream()
                .map(BoardSquare::getPlacedPiece)
                .filter(Objects::nonNull)
                .filter(piece -> piece.getPieceName().equals("Sau"))
                .count();
        if (numOfSau == 1) {
            hasWinner = true;
            return chessboard.getSlots().stream()
                    .map(BoardSquare::getPlacedPiece)
                    .filter(Objects::nonNull)
                    .filter(piece -> piece.getPieceName().equals("Sau"))
                    .map(piece -> piece.getOwner().getColor())
                    .findFirst().orElse(null);
        }
        hasWinner = false;
        return null;
    }

    // Get which player's turn it is
    public Player getPlayerTurn() {
        return hasWinner ? playerList.get((playerTurnNum - 1) % 2) : playerList.get(playerTurnNum % 2);
    }

    // playerTurnNum getter
    public int getPlayerTurnNum() {
        return playerTurnNum;
    }

    // playerTurnNum setter
    public void setPlayerTurnNum(int playerTurnNum) {
        this.playerTurnNum = playerTurnNum % 2 == 0 ? 1 : playerTurnNum; // Start with Blue (Player 1)
    }

    /**
     * Checks if a move from one position to another is valid for a given piece
     * @param fromSquare The starting square
     * @param toSquare The destination square
     * @return true if the move is valid, false otherwise
     */
    public boolean isValidMoveSquares(BoardSquare fromSquare, BoardSquare toSquare) {
        if (fromSquare.getPlacedPiece() == null) {
            return false;
        }

        ChessPiece piece = fromSquare.getPlacedPiece();
        if (!piece.getOwner().equals(getPlayerTurn())) {
            return false;
        }

        int fromX = fromSquare.getRowPosition();
        int fromY = fromSquare.getColPosition();
        int toX = toSquare.getRowPosition();
        int toY = toSquare.getColPosition();

        // Check if destination has a piece of the same color
        if (toSquare.getPlacedPiece() != null && 
            toSquare.getPlacedPiece().getOwner().equals(piece.getOwner())) {
            return false;
        }

        return isValidMove(piece.getPieceName(), fromX, fromY, toX, toY, piece);
    }


    
    /**
     * Gets all valid moves for a piece at a given square
     * @param square The square containing the piece to check
     * @return List of all valid destination squares
     */
    public List<BoardSquare> getValidMoves(BoardSquare square) {
        List<BoardSquare> validMoves = new ArrayList<>();
        
        if (square.getPlacedPiece() == null || 
            !square.getPlacedPiece().getOwner().equals(getPlayerTurn())) {
            return validMoves;
        }

        for (int i = 0; i < chessboard.getHeight(); i++) {
            for (int j = 0; j < chessboard.getWidth(); j++) {
                BoardSquare targetSquare = chessboard.getSlot(i, j);
                if (isValidMoveSquares(square, targetSquare)) {
                    validMoves.add(targetSquare);
                }
            }
        }
        
        return validMoves;
    }
}