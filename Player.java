
public class Player{
    private String color;
    
    // Constructor
    Player(String color){
        setColor(color);
    }
    
    // Set color of player 
    public void setColor(String color){
        this.color = color;
    }
    
    // Get color of player 
    public String getColor(){
        return this.color;
    }
}