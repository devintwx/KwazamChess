
public class Piece{
    private String pieceName;
    private Player player;
    private boolean reachEnd;
    
    // Constructor
    Piece(String pieceName, Player player, boolean reachEnd){
        this.pieceName = pieceName;
        this.player = player;
        this.reachEnd = reachEnd;
    }

    // Set piece name
    public void setPieceName(String pieceName){
        this.pieceName = pieceName;
    }
    
    // Set player
    public void setPlayer(Player player){
        this.player = player;
    }
    
    // Get piece name)
    public String getPieceName(){
        return this.pieceName;
    }
    
    // Get player 
    public Player getPlayer(){
        return this.player;
    }

    // Set true or false to reachEnd to determine the Ram piece has reached the end of the chessboard or not 
    public void setReachEnd(boolean reachEnd){
        this.reachEnd = reachEnd;
    }
    
    // Get reachEnd to know whether the Ram piece has reached the end of the chessboard or not 
    public boolean getReachEnd(){
        return this.reachEnd;
    }
}
