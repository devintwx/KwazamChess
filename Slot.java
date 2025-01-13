
public class Slot{
    private Piece piece;
    private int x;
    private int y;
    
    // Constructor 
    Slot(int x, int y){
        this.x = x;
        this.y = y;
    }
    
    // Constructor 
    Slot(int x, int y, Piece piece){
        this.x = x;
        this.y = y;
        this.piece = piece;
    }
    
    // Set piece of this slot 
    public void setPiece(Piece piece){
        this.piece = piece;
    }
    
    // Set vertical position of this slot 
    public void setX(int x){
        this.x = x;
    }
    
    // Set horizontal position of this slot 
    public void setY(int y){
        this.y = y;
    }
    
    // Get piece of this slot 
    public Piece getPiece(){
        return this.piece;
    }
    
    // Get X of this slot 
    public int getX(){
        return this.x;
    }
    
    // Get Y of this slot 
    public int getY(){
        return this.y;
    }
}
