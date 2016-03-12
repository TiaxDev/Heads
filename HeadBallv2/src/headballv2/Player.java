
package headballv2;

import java.awt.Dimension;
import java.awt.Toolkit;

/*
 @author Tiax
*/
public class Player {
    
    private final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    private final double ATTRICT = 20;
    
    private double x;
    private double y;
    private double w;
    private double h;
    
    private double Vx;
    private double Vy;
    private double Mass;
    private double Gravity;
    private boolean Jump;
    private Sprite Sprite;

    public Player() {
        this.x = 800;
        this.y = 300;
        this.w = 100;
        this.h = 100;
        this.Vx = 0;
        this.Vy = 0;
        this.Mass = 2.5;
        this.Gravity = 0.2;
        this.Jump = false;
        this.Sprite = new Sprite("til1.png", 0);
    }
    
    public double getCenterX(){
        return this.x + this.w/2;
    }
    
    public double getCenterY(){
        return this.y + this.h/2;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public double getH() {
        return h;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getVx() {
        return Vx;
    }

    public void setVx(double Vx) {
        this.Vx = Vx;
    }

    public double getVy() {
        return Vy;
    }

    public void setVy(double Vy) {
        this.Vy = Vy;
    }

    public double getMass() {
        return Mass;
    }

    public void setMass(double Mass) {
        this.Mass = Mass;
    }

    public boolean isJump() {
        return Jump;
    }

    public void setJump(boolean Jump) {
        this.Jump = Jump;
    }
    
    public Sprite getTile(){
        return this.Sprite;
    }
    
    public void incX(double Fact){
        this.Vx += Fact;
    }
    
    public void decX(double Fact){
        this.Vx -= Fact;
    }

    public void movePlayer(){
        
        this.x += this.Vx;
        this.y += this.Vy;
        
        this.Vy += this.Gravity;
        
        if(this.y > SCREEN_SIZE.getHeight() - this.h*2.1){ //Floor
            this.y = SCREEN_SIZE.getHeight() - this.h*2.1;
            this.Vy = 0;
            this.Jump = false;
            this.Vx -= (this.Vx/(ATTRICT/4)); //Attrito (sul piano)
        }
        else
            this.Vx -= (this.Vx/ATTRICT); //Attrito (aria)
        
        if(this.x < 0) //Left NOT rebound (il personaggio non rimbalza!)
            this.x = 0;
        if(this.x + this.w > SCREEN_SIZE.getWidth()) //Right NOT rebound
            this.x = SCREEN_SIZE.getWidth() - this.w;   
    }
    
    public void Kick(Ball b){
        double xDist, yDist;
        xDist = this.getCenterX() - b.getCenterX();
        yDist = this.getCenterY() - b.getCenterY();
        
        /*I VALORI SONO SETTATI PER QUESTE IMPOSTAZIONI DI GIOCO*/
        if(xDist > 0){ //Giocatore di destra
            if(xDist < 120 && Math.abs(yDist) < 60){ //La palla è nella posizione ideale
                b.setGravity(0.2);
                b.setVx(-8);
                if(Math.abs(b.getVy()) < 1)
                    b.setVy(5);
                b.setVy(-(15 - (xDist/10))); //Più la palla è lontana dal giocatore più il tiro sarà basso
            }
        }
    }
}
