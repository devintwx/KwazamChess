import java.util.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.*;

public class KwazamChess{
    ChessBoard chessboard;
    Player player1;
    Player player2;
    ArrayList<Player> playerList = new ArrayList<Player>();

    private static Piece queue = null;
    private static Slot temp = null;
    private static boolean hasWinner;
    private static boolean canMove = false;
    private static String type;
    private static int fromX,fromY, toX, toY, x, y;
    private int playerTurnNum = 0;

    // Constructor 
    KwazamChess(){
        chessboard = new ChessBoard();
        player1 = new Player("B"); //Blue
        player2 = new Player("R"); //Red
        playerList.add(player2);
        playerList.add(player1);
        setPLayerTurnNum(0); // Initialize turn number
        
    }

    // Restart the game 
    public void restart(){
        chessboard.clear();
        pieceSetup();
        setPLayerTurnNum(0);
        hasWinner = false;
    }

    // Save current progress into a .txt file 
    public void save() throws IOException{
        File file = new File("gameData.txt");
        PrintWriter printWriter = new PrintWriter(file);
        String currentPlayer = getPlayerTurn().getColor();
        if(currentPlayer.equals("R")){
            currentPlayer = "Red";
        }
        else{
            currentPlayer = "Blue";
        }

        // Write piece name into txt file based on player color and position
        for(int i = 1; i <= 56; i++){
            if(chessboard.getSlot(i-1).getPiece() == null){
                printWriter.print("null ");
            }
            else{
                if(chessboard.getSlot(i-1).getPiece().getPieceName().equals("Ram") && chessboard.getSlot(i-1).getPiece().getReachEnd()){
                    printWriter.print(chessboard.getSlot(i-1).getPiece().getPlayer().getColor() +
                    chessboard.getSlot(i-1).getPiece().getPieceName() + "ReachEnd ");
                }
                else{
                    printWriter.print(chessboard.getSlot(i-1).getPiece().getPlayer().getColor() +
                                        chessboard.getSlot(i-1).getPiece().getPieceName() + " ");
                }
            }
            if(i > 0 && i % 7 == 0){
                printWriter.print("\n");
            }
        }
        printWriter.println("Current total of turns:" + getPlayerTurnNum());
        printWriter.println("Current Player's turn:" + currentPlayer);
        printWriter.close();
    }
    
    // Load previous game from .txt file 
    public void load() throws FileNotFoundException{
        File file = new File("gameData.txt");
        Scanner scan = new Scanner(file);
        // clean the chess board
        chessboard.clear();
        // setting up pieces according to row and column with top-down and left-to-right
        for(int i = 0; i < chessboard.getHeight(); i++){
            for(int j = 0; j < chessboard.getWidth(); j++){
                pieceSetup(i, j, scan.next());
            }
        }
        scan.nextLine();
        // skip the words not to be read by the scanner
        scan.skip("Current total of turns:");
        // Read player turn number 
        setPLayerTurnNum(Integer.parseInt(scan.next()));
        hasWinner = false;
        scan.close();
    }

    // Setup piece 
    public void pieceSetup() {      
    String[] redArrangement = {"Tor", "Biz", "Sau", "Biz", "Xor"}; // Row 0 (Red team)
    String[] blueArrangement = {"Xor", "Biz", "Sau", "Biz", "Tor"}; // Row 7 (Blue team)
    String arrangement2 = "Ram"; // Ram pieces for the second rows
    boolean reachEnd = false;
    boolean reachEnd2 = true;

    for (int i = 0; i < chessboard.getHeight(); i++) {
        for (int j = 0; j < chessboard.getWidth(); j++) {
            if (i == 0) { // Red first row
                if (j < redArrangement.length) {
                    chessboard.addChessPiece(i, j, new Piece(redArrangement[j], player2, reachEnd2));
                }
            }
            if (i == 1) { // Red second row
                chessboard.addChessPiece(i, j, new Piece(arrangement2, player2, reachEnd));
            }
            if (i == 6) { // Blue second row
                chessboard.addChessPiece(i, j, new Piece(arrangement2, player1, reachEnd));
            }
            if (i == 7) { // Blue first row
                if (j < blueArrangement.length) {
                    chessboard.addChessPiece(i, j, new Piece(blueArrangement[j], player1, reachEnd2));
                }
            }
        }
    }
}
    
    // overloaded pieceSetup method for game loading purpose 
    public void pieceSetup(int x, int y, String pieceName){
        boolean reachEnd = false;
        // Checking for Xor piece if it has reached end
        if(pieceName.contains("ReachEnd")){
            pieceName = pieceName.substring(0, pieceName.length()-8); // remove "ReachEnd" from the pieceName
            reachEnd = true;
        }
        // add chess pieces according to the player's color
        if(pieceName.charAt(0) == 'B'){
            chessboard.addChessPiece(x, y, new Piece(pieceName.substring(1), player1, reachEnd));
        }
        if(pieceName.charAt(0) == 'R'){
            chessboard.addChessPiece(x, y, new Piece(pieceName.substring(1), player2, reachEnd));
        }            
    }
    
    // move a piece from a slot to another slot 
    public boolean move(Slot slot){
        // if clicked button has piece
        if(slot.getPiece() != null && movable(slot))
        {
            //if queue is empty
            if(queue == null)
            {
                type = slot.getPiece().getPieceName();
                fromX = slot.getX();
                fromY = slot.getY();
                queue = slot.getPiece();
                temp = slot;
            }
            //if queue is occupied
            else
            {
                toX = slot.getX();
                toY = slot.getY();
                canMove = validMove(type,fromX,fromY,toX,toY,queue);
                if(!queue.getPlayer().equals(slot.getPiece().getPlayer()) && canMove)
                {
                    temp.setPiece(null);
                    slot.setPiece(queue);
                    queue = null;
                    temp = null;
                    playerTurnNum++;
                    return true;
                }
                queue = null;
                temp = null;
            }
        }
        //if clicked button has no piece
        else
        {
            if(temp != null)
            {
                toX = slot.getX();
                toY = slot.getY();
                canMove = validMove(type,fromX,fromY,toX,toY,queue);
                if(canMove)
                {
                    slot.setPiece(queue);
                    queue = null;
                    temp.setPiece(null);
                    temp = null;
                    playerTurnNum++;
                    return true;
                }
            }
            queue = null;
            temp = null;
        }
        return false;
    }

    // Check whether a piece can be moved in that turn 
    public boolean movable(Slot slot){
        if(slot.getPiece().getPlayer().equals(getPlayerTurn())){
            return true;
        }
        return false;
    }
    
    // Check whether a piece follow their own rules to move 
    public boolean validMove(String type, int fromX, int fromY, int toX, int toY, Piece queue)
    {
        x = fromX - toX;
        y = fromY - toY;

        if(type.equals("Ram"))
        {
            if(fromY == toY)
            {   
                if(queue.getReachEnd())
                {
                    if(x == -1 || (x == -2 && chessboard.getSlot(fromX + 1,fromY).getPiece() == null)){ 
                        if(toX == 7){
                            queue.setReachEnd(false);
                        }    
                        return true;
                    }
                }
                else
                {
                    if(x == 1 || (x == 2 && chessboard.getSlot(fromX - 1,fromY).getPiece() == null)){
                        if(toX == 0){
                            queue.setReachEnd(true);
                        }
                        return true;
                    }
                }
            }
        }
        else if (type.equals("Tor"))
        { 
            x = Math.abs(fromX - toX);
            y = Math.abs(fromY - toY);
            //move left or forward
            if((fromX - toX) > 0 || (fromY - toY) > 0)
            {
                //move left
                if((x == 0 && y >= 1))
                {

                    for(int i = 1; i <= y-1; i++)
                    {
                        if(chessboard.getSlot(fromX,fromY-i).getPiece() != null)
                        {    
                            return false;
                        }
                    }
                    return true;
                }

                //move forward
                else if(x >= 1 && y == 0)
                {
                    for(int i = 1; i <= x-1; i++)
                    {
                        if(chessboard.getSlot(fromX - i,fromY).getPiece() != null)
                        {   
                            return false;
                        }
                    }
                    return true;
                }
            }
            
            //move right or backward
            else if((fromX - toX) < 0 || (fromY - toY) < 0)
            {
                //move right
                if(x == 0 && (fromY - toY) <= -1)
                {
                    for(int i = y-1 ; i > 0;  i--)
                    {
                        if(chessboard.getSlot(fromX, fromY + i).getPiece() != null)
                        {    
                            return false;
                        }
                    }
                    return true;
                }

                //move backward
                else if((fromX - toX) <= -1 && y == 0)
                {
                    for(int i = x-1 ; i > 0;  i--)
                    {
                        if(chessboard.getSlot(toX - i, toY).getPiece() != null)
                        {    
                            return false;
                        }
                    }
                    return true;
                }
            }
        }

        else if(type.equals("Xor")){
            // Xor ruled to move diagonally, so check abs(x) is equals to abs(y)
            if(Math.abs(x) == Math.abs(y)){
                //obstruction checking
                //upper left
                if(toX < fromX && toY > fromY){
                    for(int i = 1; i < x; i++){
                        if(chessboard.getSlot(fromX-i, fromY+i).getPiece() != null) {
                            return false; // obstruction found
                        }
                    }
                    return true;
                }
                // upper right
                else if(toX > fromX && toY > fromY){
                    for(int i = 1; i < x; i++){
                        if(chessboard.getSlot(fromX+i, fromY+i).getPiece() != null) {
                            return false; // obstruction found
                        }
                    }
                    return true;
                }
                //lower left
                else if(toX < fromX && toY < fromY){
                    for(int i = 1; i < x; i++){
                        if(chessboard.getSlot(fromX-i, fromY-i).getPiece() != null) {
                            return false; // obstruction found
                        }
                    }
                    return true;
                }
                //lower right
                else if(toX > fromX && toY < fromY){
                    for(int i = 1; i < x; i++){
                        if(chessboard.getSlot(fromX+i, fromY-i).getPiece() != null) {
                            return false; // obstruction found
                        }
                    }
                    return true;
                }
            }
            return false; // Movement rules for Xor has been broken
        }

        else if(type.equals("Biz")){
            if(((x == -2 || x == 2) && (y == 1 || y == -1)) || ((x == -1 || x == 1) && (y == -2 || y == 2))){
                return true;
            }
        }
        else if(type.equals("Sau")){
            if((x == -1 || x == 0 || x == 1) && (y == -1 || y == 0 || y == 1)){
                return true;
            }
        }
        return false;
    }
    
    // Change thE Xor to Tor or vice versa after every 4 turns
    public void changeState(){
        for(int i = 0; i < chessboard.getBoardSize(); i++){
            if(chessboard.getSlot(i).getPiece() != null){
                if(chessboard.getSlot(i).getPiece().getPieceName().equals("Xor")){
                    chessboard.getSlot(i).getPiece().setPieceName("Tor");
                    continue;
                }
                else if(chessboard.getSlot(i).getPiece().getPieceName().equals("Tor")){
                    chessboard.getSlot(i).getPiece().setPieceName("Xor");
                    continue;
                }
            }
        }
    }
    

    // Check whether a team has won the game 
    public String getWinner(){
        int numOfSau = 0;
        String winner = null;
        for(int i = 0; i < chessboard.getBoardSize(); i++){
            Slot slot = chessboard.getSlot(i);
            Piece piece = slot.getPiece();
            if(piece != null){
                if(piece.getPieceName().equals("Sau")){
                    winner = piece.getPlayer().getColor();
                    hasWinner = true;
                    numOfSau++;
                }
            }
        }
        if(numOfSau == 2){
            winner = null;
            hasWinner = false;
        }
        return winner;        
    }

    // Get which player turn now 
    public Player getPlayerTurn(){
        if(hasWinner){
            return playerList.get((playerTurnNum - 1) % 2);
        }
        else{
            return playerList.get(playerTurnNum % 2);
        }
    }
    
    // playerTurnNum getter 
    public int getPlayerTurnNum(){
        return playerTurnNum;
    }
    
    // playerTurnNum setter
    public void setPLayerTurnNum(int playerTurnNum){
         this.playerTurnNum = playerTurnNum % 2 == 0 ? 1 : playerTurnNum; // Start with Blue (Player 1)
    }
}
